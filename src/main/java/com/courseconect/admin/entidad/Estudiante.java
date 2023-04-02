package com.courseconect.admin.entidad;

import lombok.Data;
import java.util.*;

@Data
public class Estudiante {

	private Long estudianteId;
	private String nombres;
	private String apellidos;
	private String nivel;
	
	private Set<Curso> curso = new HashSet<>();
	
	private Usuario usuario;
	
	public Estudiante() {
	}

	public Estudiante(String nombres, String apellidos, String nivel, Usuario usuario) {
		this.nombres = nombres;
		this.apellidos = apellidos;
		this.nivel = nivel;
		this.usuario = usuario;
	}

	@Override
	public String toString() {
		return "Estudiante [estudianteId=" + estudianteId + ", nombres=" + nombres + ", apellidos=" + apellidos
				+ ", nivel=" + nivel + "]";
	}
	
	
}
