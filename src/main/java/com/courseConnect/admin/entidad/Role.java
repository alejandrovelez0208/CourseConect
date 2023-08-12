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
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Table(name = "roles")
@EqualsAndHashCode(callSuper = false)
public class Role {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "role_id", nullable = false, length = 45)
	private Long roleId;

	@Basic
	@Column(name = "nombre", nullable = false, length = 45, unique = true)
	private String nombre;

	@ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
	private Set<Usuario> usuario = new HashSet<>();

	public Role() {
	}

	public Role(String nombre) {
		super();
		this.nombre = nombre;
	}

	@Override
	public String toString() {
		return "Role [roleId=" + roleId + ", nombre=" + nombre + "]";
	}
}
