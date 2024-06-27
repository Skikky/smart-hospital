package com.example.smart_hospital.security;


import com.example.smart_hospital.entities.TokenBlackList;
import com.example.smart_hospital.entities.Utente;
import com.example.smart_hospital.enums.Role;
import com.example.smart_hospital.exceptions.*;
import com.example.smart_hospital.repositories.UtenteRepository;
import com.example.smart_hospital.requests.AuthenticationRequest;
import com.example.smart_hospital.requests.ChangePasswordRequest;
import com.example.smart_hospital.requests.RegistrationRequest;
import com.example.smart_hospital.requests.UserDTO;
import com.example.smart_hospital.responses.AuthenticationResponse;
import com.example.smart_hospital.services.EmailService;
import com.example.smart_hospital.services.PasswordValidationService;
import com.example.smart_hospital.services.TokenBlackListService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class AuthenticationService {
    @Autowired
    private JwtService jwtService;
    @Autowired
    private UtenteRepository utenteRepository;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private TokenBlackListService tokenBlackListService;
    @Autowired
    private EmailService emailService;
    @Autowired
    private PasswordValidationService passwordValidationService;

    @Transactional
    public AuthenticationResponse register(RegistrationRequest registrationRequest) throws InvalidRoleException {

        if (registrationRequest.getDesiredRole() != Role.MEDICO && registrationRequest.getDesiredRole() != Role.PAZIENTE) {
            throw new InvalidRoleException();
        }

        var user = Utente.builder()
                .nome(registrationRequest.getNome())
                .cognome(registrationRequest.getCognome())
                .email(registrationRequest.getEmail())
                .role(Role.GUEST)
                .password(passwordEncoder.encode(registrationRequest.getPassword()))
                .saldo(registrationRequest.getSaldo())
                .specializzazione(registrationRequest.getSpecializzazione())
                .codiceFiscale(registrationRequest.getCodiceFiscale())
                .build();

        var jwtToken = jwtService.generateToken(user);
        user.setRegistrationToken(jwtToken);
        utenteRepository.saveAndFlush(user);

        UserDTO userDTO = UserDTO.builder()
                .id(user.getId())
                .email(registrationRequest.getEmail())
                .desiredRole(registrationRequest.getDesiredRole())
                .registrationToken(user.getRegistrationToken())
                .build();
        sendConfirmationEmail(userDTO);

        return AuthenticationResponse.builder().token(jwtToken).build();
    }

    private void sendConfirmationEmail(UserDTO utente) {
        String url = "http://localhost:8080/auth/confirm?id=" + utente.getId() + "&token=" + utente.getRegistrationToken() + "&role=" + utente.getDesiredRole();
        String text = "Clicca per confermare la registrazione: " + url;
        emailService.sendEmail(utente.getEmail(), "Conferma", text);
    }

    public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest) throws UserNotConfirmedException {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                authenticationRequest.getEmail(),
                authenticationRequest.getPassword()
        ));
        var user = utenteRepository.findByEmail(authenticationRequest.getEmail());
        if (user.getRole().equals(Role.GUEST)) {
            throw new UserNotConfirmedException();
        }
        var jwtToken = jwtService.generateToken(user);
        if (tokenBlackListService.tokenNotValidFromUtenteById(user.getId()).contains(jwtToken)) {
            String email = jwtService.extractUsername(jwtToken);
            // Carica l'utente dal database
            UserDetails userDetails = utenteRepository.findByEmail(email);

            // Genera un nuovo token con le informazioni aggiornate
            String newToken = jwtService.generateToken(userDetails);
            return AuthenticationResponse.builder().token(newToken).build();
        }
        return AuthenticationResponse.builder().token(jwtToken).build();
    }

    public void logout(HttpServletRequest httpRequest, Long id) {
        String token = extractTokenFromRequest(httpRequest);
        TokenBlackList tokenBlackList = TokenBlackList.builder()
                .utente(utenteRepository.getReferenceById(id))
                .token(token)
                .build();
        tokenBlackListService.createTokenBlackList(tokenBlackList);
    }

    public String extractTokenFromRequest(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        if (StringUtils.hasText(authorizationHeader) && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7);
        }
        return null;
    }

    public boolean confirmRegistration (Long id, String token, Role role) {
        Utente utente = utenteRepository.getReferenceById(id);
        if (utente.getRegistrationToken().equals(token)) {
            utente.setRole(role);
            utenteRepository.saveAndFlush(utente);
            return true;
        }
        return false;
    }

    public void cambiaPassword(ChangePasswordRequest request) throws EntityNotFoundException, PasswordDeboleException, PasswordUgualiException, PasswordSbagliataException {
        Utente utente = utenteRepository.findById(request.getIdUtente())
                .orElseThrow(() -> new EntityNotFoundException(request.getIdUtente(), "Utente"));
        if (!passwordEncoder.matches(request.getOldPassword(), utente.getPassword())) {
            throw new PasswordSbagliataException();
        }
        if (passwordEncoder.matches(request.getNewPassword(), utente.getPassword())) {
            throw new PasswordUgualiException();
        }
        if (!passwordValidationService.isPasswordValid(request.getNewPassword())) {
            throw new PasswordDeboleException();
        }
        utente.setPassword(passwordEncoder.encode(request.getNewPassword()));
        utenteRepository.saveAndFlush(utente);
    }

    public void passwordDimenticata(String email, String newPassword) throws PasswordDeboleException {
        Utente utente = utenteRepository.findByEmail(email);

        if (utente == null) {
            throw new IllegalArgumentException("Utente non trovato com questa email");
        }
        if (!passwordValidationService.isPasswordValid(newPassword)) {
            throw new PasswordDeboleException();
        }

        sendResetPasswordEmail(email,newPassword);
    }

    protected void resetPassword(String email, String newPassword) {
        Utente utente = utenteRepository.findByEmail(email);
        utente.setPassword(passwordEncoder.encode(newPassword));
        utenteRepository.saveAndFlush(utente);
    }

    private void sendResetPasswordEmail(String email,String password) {
        String url = "http://localhost:8080/auth/reset?email="+ email+"&newPassword=" +password;
        String text = "Clicca per resettare la password: " + url;
        emailService.sendEmail(email, "Reset", text);
    }
}
