package com.example.smart_hospital.controllers;

import com.example.smart_hospital.entities.Utente;
import com.example.smart_hospital.requests.VisitaRequest;
import com.example.smart_hospital.responses.ErrorResponse;
import com.example.smart_hospital.responses.GenericResponse;
import com.example.smart_hospital.responses.VisitaResponse;
import com.example.smart_hospital.services.VisitaService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/visita")
public class VisitaController {

    @Autowired
    private VisitaService visitaService;

    @GetMapping("/all")
    public List<VisitaResponse> getAllVisite() {
        return visitaService.getAllVisite();
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<VisitaResponse> getVisitaById(@PathVariable Long id) {
        try {
            VisitaResponse visita = visitaService.getVisitaResponseById(id);
            return ResponseEntity.ok(visita);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PostMapping("/create")
    public VisitaResponse createVisita(@RequestBody VisitaRequest visitaRequest) {
        return visitaService.createVisita(visitaRequest);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<VisitaResponse> updateVisita(@PathVariable Long id, @RequestBody VisitaRequest visitaRequest) {
        try {
            VisitaResponse visita = visitaService.updateVisita(id, visitaRequest);
            return ResponseEntity.ok(visita);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteVisita(@PathVariable Long id) {
        try {
            visitaService.deleteVisitaById(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/medico/{idMedico}")
    public List<VisitaResponse> trovaVisitePerMedico(@PathVariable Long idMedico) {
        Utente medico = new Utente();
        medico.setId(idMedico);
        return visitaService.trovaVisitePerMedico(medico);
    }

    @GetMapping("/disponibili")
    public List<VisitaResponse> trovaVisiteDisponibili() {
        return visitaService.trovaVisiteDisponibili();
    }

    @GetMapping("/paziente/{idPaziente}")
    public List<VisitaResponse> trovaVisitePerPaziente(@PathVariable Long idPaziente) {
        Utente paziente = new Utente();
        paziente.setId(idPaziente);
        return visitaService.trovaVisitePerPaziente(paziente);
    }

    @GetMapping("/non-terminate/{idPaziente}")
    public List<VisitaResponse> trovaVisiteNonTerminatePerPaziente(@PathVariable Long idPaziente) {
        Utente paziente = new Utente();
        paziente.setId(idPaziente);
        return visitaService.trovaVisiteNonTerminatePerPaziente(paziente);
    }

    @GetMapping("/terminate/{idPaziente}")
    public List<VisitaResponse> trovaVisiteTerminatePerPaziente(@PathVariable Long idPaziente) {
        Utente paziente = new Utente();
        paziente.setId(idPaziente);
        return visitaService.trovaVisiteTerminatePerPaziente(paziente);
    }

    @PutMapping("/upload_referto/{id}")
    public ResponseEntity<?> uploadReferto(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        try {
            // Controllo il tipo di file
            if (!Objects.equals(file.getContentType(), "application/pdf")) {
                return new ResponseEntity<>(new GenericResponse("Solo file PDF sono ammessi"), HttpStatus.BAD_REQUEST);
            }
            visitaService.uploadReferto(id, file);
            return new ResponseEntity<>(new GenericResponse("File caricato con successo"), HttpStatus.CREATED);
        } catch (IOException e) {
            return new ResponseEntity<>(new ErrorResponse("InputOutputException","Errore nel caricamento del file"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/download_referto/{id}")
    public ResponseEntity<GenericResponse> downloadreferto(@PathVariable Long id, HttpServletResponse response) throws IOException {
        String pathFile = visitaService.getPath(id);
        Path filePath = Path.of(pathFile);
        String mimeType = Files.probeContentType(filePath);
        if (mimeType == null) {
            mimeType = "application/octet-stream";
        }
        response.setContentType(mimeType); // setto l'header content-type
        response.setHeader("Content-Disposition", "attachment; filename=\"" + filePath.getFileName().toString() + "\"");
        response.setContentLength((int) Files.size(filePath));
        Files.copy(filePath, response.getOutputStream());
        return new ResponseEntity<>(new GenericResponse("file scaricato con successo"), HttpStatus.OK);
    }
}
