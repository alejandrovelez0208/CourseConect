package com.courseConnect.admin.servicio.impl;

import java.util.List;

import com.courseConnect.admin.dao.InstructorDao;
import com.courseConnect.admin.entidad.Curso;
import com.courseConnect.admin.entidad.Instructor;
import com.courseConnect.admin.entidad.Usuario;
import com.courseConnect.admin.servicio.CursoServicio;
import com.courseConnect.admin.servicio.InstructorServicio;

import jakarta.persistence.EntityNotFoundException;

public class InstructorServicioImpl implements InstructorServicio {

	private InstructorDao instructorDao;

	private CursoServicio cursoServicio;

	public InstructorServicioImpl(InstructorDao instructorDao) {
		this.instructorDao = instructorDao;
	}

	@Override
	public Instructor cargarInstructorPorId(Long instructorId) {
		return instructorDao.findById(instructorId)
				.orElseThrow(() -> new EntityNotFoundException("Instructor con Id " + instructorId + " No Encontrado"));
	}

	@Override
	public List<Instructor> buscarInstructorePorNombre(String name) {
		return instructorDao.buscarInstructoresPorNombre(name);
	}

	@Override
	public Instructor cargarInstructorPorEmail(String Email) {
		return instructorDao.buscarInstructorPorEmail(Email);
	}

	@Override
	public Instructor crearInstructor(String nombre, String apellidos, String summary, Usuario email, String password) {
		return instructorDao.save(new Instructor(nombre, apellidos, summary, email, password));
	}

	@Override
	public Instructor actualizarInstructor(Instructor instructor) {
		return instructorDao.save(instructor);
	}

	@Override
	public List<Instructor> fetchInstructor() {
		return instructorDao.findAll();
	}

	@Override
	public void removerInstructor(Long instructorId) {
		Instructor instructor = cargarInstructorPorId(instructorId);
		for (Curso curso : instructor.getCurso()) {
			cursoServicio.removerCurso(curso.getCursoId());
		}
		instructorDao.deleteById(instructorId);
	}

}
