package com.courseconect.admin.entidad;

import lombok.Data;
import java.util.*;

@Data
public class Role {

	private Long roleId;
	private String nombre;
	
	private Set<Usuario> usuario = new HashSet<>();
	
	public Role() {
	}

	public Role(String nombre, Set<Usuario> usuario) {
		super();
		this.nombre = nombre;
		this.usuario = usuario;
	}

	@Override
	public String toString() {
		return "Role [roleId=" + roleId + ", nombre=" + nombre + "]";
	}
}
