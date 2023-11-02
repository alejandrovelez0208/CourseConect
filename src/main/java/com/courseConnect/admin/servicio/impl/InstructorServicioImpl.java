package com.courseConnect.admin.servicio.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.courseConnect.admin.dao.InstructorDao;
import com.courseConnect.admin.entidad.Curso;
import com.courseConnect.admin.entidad.Instructor;
import com.courseConnect.admin.entidad.Usuario;
import com.courseConnect.admin.servicio.CursoServicio;
import com.courseConnect.admin.servicio.InstructorServicio;
import com.courseConnect.admin.servicio.UsuarioServicio;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class InstructorServicioImpl implements InstructorServicio {

	@Autowired
	private InstructorDao instructorDao;

	private CursoServicio cursoServicio;

	private UsuarioServicio usuarioServicio;

	public InstructorServicioImpl(CursoServicio cursoServicio,
			UsuarioServicio usuarioServicio) {
		this.cursoServicio = cursoServicio;
		this.usuarioServicio = usuarioServicio;
	}

	@Override
	public Instructor cargarInstructorPorId(Long instructorId) {
		return instructorDao.findById(instructorId)
				.orElseThrow(() -> new EntityNotFoundException("Instructor con Id " + instructorId + " No Encontrado"));
	}

	@Override
	public List<Instructor> buscarInstructorPorNombre(String name) {
		return instructorDao.findInstructoresByNombre(name);
	}

	@Override
	public Instructor cargarInstructorPorEmail(String Email) {
		return instructorDao.findInstructorByEmail(Email);
	}

	@Override
	public Instructor crearInstructor(String nombre, String apellidos, String summary, String email, String password) {
		Usuario usuario = usuarioServicio.crearUsuarios(email, password);
		usuarioServicio.asignarRoleToUsuario(email, "Instructor");
		return instructorDao.save(new Instructor(nombre, apellidos, summary, usuario));
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
