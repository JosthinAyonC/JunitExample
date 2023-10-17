package ug.jayon.actividad.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ug.jayon.actividad.entity.Project;

public interface ProjectRepo extends JpaRepository<Project, Long> {
    
}
