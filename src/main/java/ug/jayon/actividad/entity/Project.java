package ug.jayon.actividad.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "project")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre del proyecto es obligatorio.")
    private String name;

    @NotNull(message = "La nota del proyecto es obligatoria.")
    @DecimalMax(value = "10.0", message = "Grade debe ser menor o igual a 10.")
    @DecimalMin(value = "0.0", message = "Grade debe ser mayor o igual a 0.")
    private Double grade;
}
