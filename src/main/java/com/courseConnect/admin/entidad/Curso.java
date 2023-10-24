package com.courseConnect.admin.entidad;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
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
import jakarta.persistence.OneToOne;
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
	@Column(name = "curso_descripcion", nullable = false, length = 200)
	private String cursoDescripcion;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "instructor_id", referencedColumnName = "instructor_id", nullable = false)
	private Instructor instructor;

	@ManyToMany
	@JoinTable(name = "matriculado_en", joinColumns = { @JoinColumn(name = "curso_id") }, inverseJoinColumns = {
			@JoinColumn(name = "estudiante_id") })
	private Set<Estudiante> estudiante = new HashSet<>();

	@OneToOne(mappedBy = "curso", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private Contenido contenido;

	public Curso() {
	}

	public Curso(String cursoNombre, String cursoDuracion, String cursoDescripcion, Instructor instructor) {
		this.cursoNombre = cursoNombre;
		this.cursoDuracion = cursoDuracion;
		this.cursoDescripcion = cursoDescripcion;
		this.instructor = instructor;
	}

	public Curso(Long cursoId, String cursoNombre, String cursoDuracion, String cursoDescripcion, Instructor instructor,
			Set<Estudiante> estudiante) {
		this.cursoId = cursoId;
		this.cursoNombre = cursoNombre;
		this.cursoDuracion = cursoDuracion;
		this.cursoDescripcion = cursoDescripcion;
		this.instructor = instructor;
		this.estudiante = estudiante;
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

	//TODO:REVISAR CODIGO, BORRAR ESTUDIANTE Y RETIRARLO DE TODOS LOS CURSOS
	public void retirarEstudianteDelCurso(Estudiante estudiante) {
		if (this.estudiante.remove(estudiante)) {
			estudiante.getCurso().remove(this);
			if (this.estudiante.isEmpty()) {
				estudiante.getCurso().remove(this);
			}
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Curso other = (Curso) obj;
		return Objects.equals(cursoDescripcion, other.cursoDescripcion)
				&& Objects.equals(cursoDuracion, other.cursoDuracion) && Objects.equals(cursoId, other.cursoId)
				&& Objects.equals(cursoNombre, other.cursoNombre);
	}

	@Override
	public int hashCode() {
		return Objects.hash(cursoDescripcion, cursoDuracion, cursoId, cursoNombre);
	}
}
