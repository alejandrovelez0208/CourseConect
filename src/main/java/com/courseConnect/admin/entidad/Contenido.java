package com.courseConnect.admin.entidad;

import java.util.Arrays;
import java.util.Objects;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "archivos")
public class Contenido {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "archivoId", nullable = false)
	private Long archivoId;

	@Column(name = "nombre1Pdf")
	private String nombre1Pdf;

	@Lob
	@Column(name = "archivo1Pdf", length = 34996903)
	@Basic(fetch = FetchType.LAZY)
	private byte[] archivo1Pdf;

	@Column(name = "nombre2Pdf")
	private String nombre2Pdf;

	@Lob
	@Column(name = "archivo2Pdf", length = 34996903)
	@Basic(fetch = FetchType.LAZY)
	private byte[] archivo2Pdf;

	@Column(name = "nombre1Doc")
	private String nombre1Doc;

	@Lob
	@Column(name = "archivo1Doc", length = 34996903)
	@Basic(fetch = FetchType.LAZY)
	private byte[] archivo1Doc;

	@Column(name = "nombre2Doc")
	private String nombre2Doc;

	@Lob
	@Column(name = "archivo2Doc", length = 34996903)
	@Basic(fetch = FetchType.LAZY)
	private byte[] archivo2Doc;

	@Column(name = "tutorialVideo")
	private String tutorialVideo;

	@Lob
	@Column(name = "archivoVideo", length = 34996903)
	@Basic(fetch = FetchType.LAZY)
	private byte[] archivoVideo;

	@Column(name = "imagenNombre")
	private String imagenNombre;

	@Lob
	@Column(name = "imagenGuia", length = 34996903)
	@Basic(fetch = FetchType.LAZY)
	private byte[] imagenGuia;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cursoId")
	private Curso curso;

	public Contenido() {
	}

	public Contenido(String nombre1Pdf, byte[] archivo1Pdf, String nombre2Pdf, byte[] archivo2Pdf, String nombre1Doc,
			byte[] archivo1Doc, String nombre2Doc, byte[] archivo2Doc, String tutorialVideo, byte[] archivoVideo,
			String imagenNombre, byte[] imagenGuia, Curso curso) {
		this.nombre1Pdf = nombre1Pdf;
		this.archivo1Pdf = archivo1Pdf;
		this.nombre2Pdf = nombre2Pdf;
		this.archivo2Pdf = archivo2Pdf;
		this.nombre1Doc = nombre1Doc;
		this.archivo1Doc = archivo1Doc;
		this.nombre2Doc = nombre2Doc;
		this.archivo2Doc = archivo2Doc;
		this.tutorialVideo = tutorialVideo;
		this.archivoVideo = archivoVideo;
		this.imagenNombre = imagenNombre;
		this.imagenGuia = imagenGuia;
		this.curso = curso;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Contenido other = (Contenido) obj;
		return Arrays.equals(archivo1Doc, other.archivo1Doc) && Arrays.equals(archivo1Pdf, other.archivo1Pdf)
				&& Arrays.equals(archivo2Doc, other.archivo2Doc) && Arrays.equals(archivo2Pdf, other.archivo2Pdf)
				&& Arrays.equals(archivoVideo, other.archivoVideo) && Arrays.equals(imagenGuia, other.imagenGuia)
				&& Objects.equals(imagenNombre, other.imagenNombre) && Objects.equals(nombre1Doc, other.nombre1Doc)
				&& Objects.equals(nombre1Pdf, other.nombre1Pdf) && Objects.equals(nombre2Doc, other.nombre2Doc)
				&& Objects.equals(nombre2Pdf, other.nombre2Pdf) && Objects.equals(tutorialVideo, other.tutorialVideo);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(archivo1Doc);
		result = prime * result + Arrays.hashCode(archivo1Pdf);
		result = prime * result + Arrays.hashCode(archivo2Doc);
		result = prime * result + Arrays.hashCode(archivo2Pdf);
		result = prime * result + Arrays.hashCode(archivoVideo);
		result = prime * result + Arrays.hashCode(imagenGuia);
		result = prime * result
				+ Objects.hash(imagenNombre, nombre1Doc, nombre1Pdf, nombre2Doc, nombre2Pdf, tutorialVideo);
		return result;
	}
}
