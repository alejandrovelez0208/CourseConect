package com.courseConnect.admin.entidad;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "cursos")
public class Curso {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "curso_id", nullable = false)
	private Long cursoId;

	@Basic
	@Column(name = "curso_nombre", nullable = false, length = 45)
	private String cursoNombre;

	@Basic
	@Column(name = "curso_duracion", nullable = false, length = 45)
	private String cursoDuracion;

	@Basic
	@Column(name = "curso_descripcion", nullable = false, length = 45)
	private String cursoDescripcion;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "instructor_id", referencedColumnName = "instructor_id", nullable = false)
	private Instructor instructor;

	@ManyToMany
	@JoinTable(name = "matriculado_en", joinColumns = { @JoinColumn(name = "curso_id") }, inverseJoinColumns = {
			@JoinColumn(name = "estudiante_id") })
	private Set<Estudiante> estudiante = new HashSet<>();

	public Curso() {
	}

	public Curso(String cursoNombre, String cursoDuracion, String cursoDescripcion, Instructor instructor) {
		super();
		this.cursoNombre = cursoNombre;
		this.cursoDuracion = cursoDuracion;
		this.cursoDescripcion = cursoDescripcion;
		this.instructor = instructor;
	}

	@Override
	public String toString() {
		return "Curso [cursoId=" + cursoId + ", CursoNombre=" + cursoNombre + ", cursoDuracion=" + cursoDuracion
				+ ", cursoDescripcion=" + cursoDescripcion + "]";
	}

	public void asignarEstudianteAlCurso(Estudiante estudiante) {
		this.estudiante.add(estudiante);
		estudiante.getCurso().add(this);
	}

	public void retirarEstudianteDelCurso(Estudiante estudiante) {
		this.estudiante.remove(estudiante);
		estudiante.getCurso().add(this);
	}
}
