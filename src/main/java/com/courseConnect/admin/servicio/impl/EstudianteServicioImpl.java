package com.courseConnect.admin.servicio.impl;

import java.util.Iterator;
import java.util.List;

import com.courseConnect.admin.dao.EstudianteDao;
import com.courseConnect.admin.entidad.Curso;
import com.courseConnect.admin.entidad.Estudiante;
import com.courseConnect.admin.entidad.Usuario;
import com.courseConnect.admin.servicio.EstudianteServicio;
import com.courseConnect.admin.servicio.UsuarioServicio;

import jakarta.persistence.EntityNotFoundException;

public class EstudianteServicioImpl implements EstudianteServicio {

	private EstudianteDao estudianteDao;

	private UsuarioServicio usuarioServicio;

	public EstudianteServicioImpl(EstudianteDao estudianteDao,UsuarioServicio usuarioServicio) {
		this.estudianteDao = estudianteDao;
		this.usuarioServicio = usuarioServicio;
	}

	@Override
	public Estudiante cargarEstudiantePorId(Long estudianteId) {
		return estudianteDao.findById(estudianteId)
				.orElseThrow(() -> new EntityNotFoundException("Estudiante con Id " + estudianteId + " No Encontrado"));
	}

	@Override
	public List<Estudiante> cargarEstudiantePorNombre(String nombre) {
		return estudianteDao.buscarEstudiantesPorNombre(nombre);
	}

	@Override
	public Estudiante cargarEstudiantePorEmail(String email) {
		return estudianteDao.buscarEstudiantePorEmail(email);
	}

	@Override
	public Estudiante crearEstudiante(String nombres, String apellidos, String nivel, String email, String password) {
		Usuario usuario = usuarioServicio.crearUsuarios(email, password);
		usuarioServicio.asignarRoleToUsuario(email, "Estudiante");
		estudianteDao.save(new Estudiante(nombres, apellidos, nivel, usuario));
		return null;
	}

	@Override
	public List<Estudiante> fetchEstudiantes() {
		return estudianteDao.findAll();
	}

	@Override
	public void actualizarEstudiante(Estudiante estudiante) {
		estudianteDao.save(estudiante);
	}

	@Override
	public void removerEstudiante(Long estudianteId) {
		Estudiante estudiante = cargarEstudiantePorId(estudianteId);
		Iterator<Curso> iterador = estudiante.getCurso().iterator();
		if (iterador.hasNext()) {
			Curso curso = iterador.next();
			curso.retirarEstudianteDelCurso(estudiante);
		}
		estudianteDao.deleteById(estudianteId);
	}

}
