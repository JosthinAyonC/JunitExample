package ug.jayon.actividad.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ug.jayon.actividad.entity.Project;
import ug.jayon.actividad.repository.ProjectRepo;

@RestController
@RequestMapping("/project")
@CrossOrigin(origins = "*")
public class ProjectController {

    @Autowired
    private ProjectRepo projectRepo;
    
    @GetMapping
    public ResponseEntity<List<Project>> getProject() {
        return projectRepo.findAll().isEmpty()
                ? ResponseEntity.notFound().build()
                : ResponseEntity.ok(projectRepo.findAll());
    }

    @PostMapping
    public ResponseEntity<Project> postProject(@RequestBody @Valid Project project) {
        try {
            return ResponseEntity.ok(projectRepo.save(project));
        } catch (Exception e) {
            throw new RuntimeException("Error al crear el proyecto."+ e.getMessage());
        }
    }
}
