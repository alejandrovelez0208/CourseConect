package com.courseConnect.admin.entidad;

import java.util.HashSet;
import java.util.Objects;
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
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "usuarios")
public class Usuario {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "usuario_id", nullable = false, length = 45)
	private Long usuarioId;

	@Basic
	@Column(name = "email", nullable = false, length = 45, unique = true)
	private String email;

	@Basic
	@Column(name = "contraseña", nullable = false, length = 255)
	private String contraseña;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "usuario_role", joinColumns = { @JoinColumn(name = "usuario_id") }, inverseJoinColumns = {
			@JoinColumn(name = "role_id") })
	private Set<Role> roles = new HashSet<>();

	@OneToOne(mappedBy = "usuario")
	private Estudiante estudiante;

	@OneToOne(mappedBy = "usuario")
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

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Usuario other = (Usuario) obj;
		return Objects.equals(contraseña, other.contraseña) && Objects.equals(email, other.email)
				&& Objects.equals(usuarioId, other.usuarioId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(contraseña, email, usuarioId);
	}
}
