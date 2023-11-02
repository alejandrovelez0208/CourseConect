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
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "instructores")
public class Instructor {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "instructor_id", nullable = false)
	private Long instructorId;

	@Basic
	@Column(name = "nombres", nullable = false, length = 46)
	private String nombres;

	@Basic
	@Column(name = "apellidos", nullable = false, length = 46)
	private String apellidos;

	@Basic
	@Column(name = "summary", nullable = false, length = 46)
	private String summary;

	@OneToMany(mappedBy = "instructor", fetch = FetchType.LAZY)
	private Set<Curso> curso = new HashSet<>();

	@OneToOne(cascade = CascadeType.REMOVE)
	@JoinColumn(name = "usuario_id", referencedColumnName = "usuario_id", nullable = false)
	private Usuario usuario;

	public Instructor() {
	}

	public Instructor(String nombres, String apellidos, String summary, Usuario usuario) {
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

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Instructor other = (Instructor) obj;
		return Objects.equals(apellidos, other.apellidos) && Objects.equals(instructorId, other.instructorId)
				&& Objects.equals(nombres, other.nombres) && Objects.equals(summary, other.summary);
	}

	@Override
	public int hashCode() {
		return Objects.hash(apellidos, instructorId, nombres, summary);
	}
}
