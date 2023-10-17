package ug.jayon.actividad;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("ug.jayon.actividad") 
public class ActividadApplication {

	public static void main(String[] args) {
		SpringApplication.run(ActividadApplication.class, args);
	}

}
