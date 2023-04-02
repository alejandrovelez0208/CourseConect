package com.courseconect.admin.entidad;

import lombok.Data;
import java.util.*;

@Data
public class Instructor {

	private Long instructorId;
	private String nombres;
	private String apellidos;
	private String summary;
	
	private Set<Curso> curso = new HashSet<>();
	
	private Usuario usuario;
	
	public Instructor() {
	}

	public Instructor(String nombres, String apellidos, String summary, Usuario usuario) {
		super();
		this.nombres = nombres;
		this.apellidos = apellidos;
		this.summary = summary;
		this.usuario = usuario;
	}

	@Override
	public String toString() {
		return "Instructor [instructorId=" + instructorId + ", nombres=" + nombres + ", apellidos=" + apellidos
				+ ", summary=" + summary + "]";
	}
}
