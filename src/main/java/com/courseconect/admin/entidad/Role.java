package com.courseconect.admin.entidad;

import java.util.HashSet;
import java.util.Set;

import lombok.Data;

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
