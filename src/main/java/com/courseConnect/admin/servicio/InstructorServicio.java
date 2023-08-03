package com.courseConnect.admin.servicio;

import java.util.List;

import com.courseConnect.admin.entidad.Instructor;

public interface InstructorServicio {

	Instructor cargarInstructorPorId(Long instructorId);

	List<Instructor> buscarInstructorePorNombre(String name);

	Instructor cargarInstructorPorEmail(String Email);

	Instructor crearInstructor(String nombre, String apellidos, String summary, String email, String password);

	Instructor actualizarInstructor(Instructor instructor);

	List<Instructor> fetchInstructor();

	void removerInstructor(Long instructorId);
}
