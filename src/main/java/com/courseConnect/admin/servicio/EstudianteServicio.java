package com.courseConnect.admin.servicio;

import java.util.List;

import com.courseConnect.admin.entidad.Estudiante;

public interface EstudianteServicio {

	Estudiante cargarEstudiantePorId(Long estudianteId);

	List<Estudiante> cargarEstudiantePorNombre(String nombre);

	Estudiante cargarEstudiantePorEmail(String email);

	Estudiante crearEstudiante(String nombres, String apellidos, String nivel, String email, String password);

	List<Estudiante> fetchEstudiantes();

	void actualizarEstudiante(Estudiante estudiante);

	void removerEstudiante(Long estudianteId);
}
