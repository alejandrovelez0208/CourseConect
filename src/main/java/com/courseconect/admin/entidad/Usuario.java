package com.courseconect.admin.entidad;

import java.util.HashSet;
import java.util.Set;

import lombok.Data;

@Data
public class Usuario {

	private Long usuarioId;
	private String email;
	private String contraseña;

	private Set<Role> roles = new HashSet<>();

	private Estudiante estudiante;

	private Instructor instructor;

	public Usuario() {
	}

	public Usuario(String email, String contraseña) {
		super();
		this.email = email;
		this.contraseña = contraseña;
	}

	@Override
	public String toString() {
		return "Usuario [usuarioId=" + usuarioId + ", email=" + email + ", contraseña=" + contraseña + "]";
	}

	public void asignarRoleAUsuario(Role role) {
		this.roles.add(role);
		role.getUsuario().add(this);
	}

	public void retirarRoleDeUsuario(Role role) {
		this.roles.remove(role);
		role.getUsuario().add(this);
	}
}
