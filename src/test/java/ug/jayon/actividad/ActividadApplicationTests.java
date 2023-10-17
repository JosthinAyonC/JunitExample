package ug.jayon.actividad;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import ug.jayon.actividad.controller.ProjectController;
import ug.jayon.actividad.entity.Project;

@SpringBootTest
class ActividadApplicationTests {

	@Autowired
	private ProjectController projectController;

	//En caso de que el creado haya sido exitoso
	@Test
	public void postProject_ReturnsCreatedProject() {
		Project project = new Project();
		project.setName("Test Project");
		project.setGrade(9.55);

		ResponseEntity<Project> response = projectController.postProject(project);

		Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		Assertions.assertThat(response.getBody()).isEqualTo(project);
	}

	//En caso de que el creado haya sido errorneo por nota no valida.
	@Test
	public void postProject_ReturnsCreatedProjectFailure() {
		Project project = new Project();
		project.setName("Test Project");
		project.setGrade(15.0);

		ResponseEntity<Project> response = projectController.postProject(project);

		Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		Assertions.assertThat(response.getBody()).isEqualTo(project);
	}

	//En caso de que el creado haya sido errorneo por datos nulos.
	@Test
	public void postProject_ReturnsCreatedProjectFailure2() {
		Project project = new Project();
		project.setName("Prueba 2");
		project.setGrade(null);

		ResponseEntity<Project> response = projectController.postProject(project);

		Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		Assertions.assertThat(response.getBody()).isEqualTo(project);
	}

	//En caso que si tengamos registros
	@Test
	public void getProject_ReturnsEmptyList() {
		ResponseEntity<List<Project>> response = projectController.getProject();
		Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
	}

	//En caso de estar vacia o no existir registros en base
	@Test
	public void getProject_ReturnsNotFoundList() {
		ResponseEntity<List<Project>> response = projectController.getProject();
		Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
	}
}
