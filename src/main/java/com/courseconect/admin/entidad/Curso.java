package com.courseconect.admin.entidad;

import lombok.Data;
import java.util.*;

@Data
public class Curso {

	private Long cursoId;
	private String CursoNombre;
	private String cursoDuracion;
	private String cursoDescripcion;
	
	private Instructor instructor;
	
	private Set<Estudiante> estudiante = new HashSet<>();
	
	public Curso() {
	}

	public Curso(String cursoNombre, String cursoDuracion, String cursoDescripcion) {
		super();
		CursoNombre = cursoNombre;
		this.cursoDuracion = cursoDuracion;
		this.cursoDescripcion = cursoDescripcion;
	}

	@Override
	public String toString() {
		return "Curso [cursoId=" + cursoId + ", CursoNombre=" + CursoNombre + ", cursoDuracion=" + cursoDuracion
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
