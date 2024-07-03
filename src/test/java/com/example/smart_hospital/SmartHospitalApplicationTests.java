package com.example.smart_hospital;

import com.example.smart_hospital.entities.Utente;
import com.example.smart_hospital.entities.Visita;
import com.example.smart_hospital.entities.Prenotazione;
import com.example.smart_hospital.enums.Role;
import com.example.smart_hospital.exceptions.InvalidRoleException;
import com.example.smart_hospital.repositories.PrenotazioneRepository;
import com.example.smart_hospital.repositories.UtenteRepository;
import com.example.smart_hospital.repositories.VisitaRepository;
import com.example.smart_hospital.requests.PrenotazioneRequest;
import com.example.smart_hospital.requests.RegistrationRequest;
import com.example.smart_hospital.responses.AuthenticationResponse;
import com.example.smart_hospital.responses.PrenotazioneResponse;
import com.example.smart_hospital.security.AuthenticationService;
import com.example.smart_hospital.security.JwtService;
import com.example.smart_hospital.services.PazienteService;
import com.example.smart_hospital.services.PrenotazioneService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class SmartHospitalApplicationTests {

	@Autowired
	private PrenotazioneService prenotazioneService;
	@Autowired
	private PrenotazioneRepository prenotazioneRepository;
	@Autowired
	private VisitaRepository visitaRepository;
	@Autowired
	private PazienteService pazienteService;
	@Autowired
	private UtenteRepository utenteRepository;
	@Autowired
	private JwtService jwtService;
	@Autowired
	private AuthenticationService authenticationService;
	@Autowired
	private PasswordEncoder passwordEncoder;

	 Utente paziente;
	 Utente medico;
	 Visita visita;

	@BeforeEach
	@Transactional
	public void setUp() {

		paziente = Utente.builder()
				.nome("Mario")
				.cognome("Rossi")
				.email("mario.rossi@example.com")
				.role(Role.PAZIENTE)
				.password(passwordEncoder.encode("password"))
				.saldo(100.0)
				.codiceFiscale("RSSMRA85M01H501Z")
				.build();
		String pazienteToken = jwtService.generateToken(paziente);
		paziente.setRegistrationToken(pazienteToken);
		utenteRepository.saveAndFlush(paziente);

		medico = Utente.builder()
				.nome("Luigi")
				.cognome("Verdi")
				.email("luigi.verdi@example.com")
				.role(Role.MEDICO)
				.saldo(100.0)
				.password(passwordEncoder.encode("password"))
				.codiceFiscale("VRDLGU85M01H501Z")
				.specializzazione("Cardiologia")
				.build();
		String medicoToken = jwtService.generateToken(medico);
		medico.setRegistrationToken(medicoToken);
		utenteRepository.saveAndFlush(medico);

		visita = Visita.builder()
				.inizioDisponibilita(LocalDateTime.of(2023, 1, 1, 9, 0))
				.fineDisponibilita(LocalDateTime.of(2023, 1, 1, 17, 0))
				.prezzo(50.0)
				.durata(1) // durata in ore
				.medico(medico)
				.isTerminata(false)
				.build();
		visitaRepository.saveAndFlush(visita);
	}

	@Test
	@Transactional
	public void testRegisterSuccess() throws InvalidRoleException {
		RegistrationRequest registrationRequest = RegistrationRequest.builder()
				.nome("Mario")
				.cognome("Rossi")
				.email("mario.rossi@example.com")
				.password("password")
				.desiredRole(Role.PAZIENTE)
				.saldo(100.0)
				.codiceFiscale("RSSMRA85M01H501Z")
				.build();

		AuthenticationResponse response = authenticationService.register(registrationRequest);

		assertNotNull(response);
		assertNotNull(response.getToken());

		Utente user = utenteRepository.findUtenteByEmail("mario.rossi@example.com");
		assertNotNull(user);
		assertEquals("mario.rossi@example.com", user.getEmail());
		assertTrue(passwordEncoder.matches("password", user.getPassword()));
	}

	@Test
	@Transactional
	public void testPrenotaVisitaSuccess() {
		PrenotazioneRequest prenotazioneRequest = PrenotazioneRequest.builder()
				.idPaziente(paziente.getId())
				.idVisita(visita.getId())
				.dataPrenotazione(LocalDateTime.of(2023, 1, 1, 10, 0))
				.build();

		PrenotazioneResponse response = prenotazioneService.prenotaVisita(prenotazioneRequest);

		assertNotNull(response);
		assertEquals(paziente.getId(), response.getIdPaziente());
		assertEquals(visita.getId(), response.getIdVisita());
	}

	@Test
	@Transactional
	public void testPrenotaVisitaSaldoInsufficiente() {
		paziente.setSaldo(20.0);
		utenteRepository.saveAndFlush(paziente);

		PrenotazioneRequest prenotazioneRequest = PrenotazioneRequest.builder()
				.idPaziente(paziente.getId())
				.idVisita(visita.getId())
				.dataPrenotazione(LocalDateTime.of(2023, 1, 1, 10, 0))
				.build();

		assertThrows(IllegalArgumentException.class, () -> {
			prenotazioneService.prenotaVisita(prenotazioneRequest);
		});
	}

	@Test
	@Transactional
	public void testPrenotaVisitaDataNonDisponibile() {
		PrenotazioneRequest prenotazioneRequest = PrenotazioneRequest.builder()
				.idPaziente(paziente.getId())
				.idVisita(visita.getId())
				.dataPrenotazione(LocalDateTime.of(2023, 1, 1, 18, 0))
				.build();

		assertThrows(IllegalArgumentException.class, () -> {
			prenotazioneService.prenotaVisita(prenotazioneRequest);
		});
	}

	@Test
	@Transactional
	public void testPrenotaVisitaSovrapposizione() {
		Prenotazione prenotazioneEsistente = Prenotazione.builder()
				.paziente(paziente)
				.visita(visita)
				.dataPrenotazione(LocalDateTime.of(2023, 1, 1, 10, 0))
				.build();
		prenotazioneRepository.saveAndFlush(prenotazioneEsistente);

		PrenotazioneRequest prenotazioneRequest = PrenotazioneRequest.builder()
				.idPaziente(paziente.getId())
				.idVisita(visita.getId())
				.dataPrenotazione(LocalDateTime.of(2023, 1, 1, 10, 30))
				.build();

		assertThrows(IllegalArgumentException.class, () -> {
			prenotazioneService.prenotaVisita(prenotazioneRequest);
		});
	}
}
