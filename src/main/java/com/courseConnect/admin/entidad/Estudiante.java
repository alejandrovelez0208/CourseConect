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
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "estudiantes")
public class Estudiante {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "estudiante_id", nullable = false)
	private Long estudianteId;

	@Basic
	@Column(name = "nombres", nullable = false, length = 45)
	private String nombres;

	@Basic
	@Column(name = "apellidos", nullable = false, length = 45)
	private String apellidos;

	@Basic
	@Column(name = "nivel", nullable = false, length = 64)
	private String nivel;

	@ManyToMany(mappedBy = "estudiante", fetch = FetchType.LAZY)
	private Set<Curso> curso = new HashSet<>();

	@OneToOne(cascade = CascadeType.REMOVE)
	@JoinColumn(name = "usuario_id", referencedColumnName = "usuario_id", nullable = false)
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

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Estudiante other = (Estudiante) obj;
		return Objects.equals(apellidos, other.apellidos) && Objects.equals(estudianteId, other.estudianteId)
				&& Objects.equals(nivel, other.nivel) && Objects.equals(nombres, other.nombres);
	}

	@Override
	public int hashCode() {
		return Objects.hash(apellidos, estudianteId, nivel, nombres);
	}

	
}
