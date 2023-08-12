package com.courseConnect.admin.servicio.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.courseConnect.admin.dao.CursoDao;
import com.courseConnect.admin.dao.EstudianteDao;
import com.courseConnect.admin.dao.InstructorDao;
import com.courseConnect.admin.entidad.Curso;
import com.courseConnect.admin.entidad.Estudiante;
import com.courseConnect.admin.entidad.Instructor;
import com.courseConnect.admin.servicio.CursoServicio;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class CursoServicioImpl implements CursoServicio {

	@Autowired
	private CursoDao cursoDao;

	@Autowired
	private InstructorDao instructorDao;

	@Autowired
	private EstudianteDao estudianteDao;

	public CursoServicioImpl(CursoDao cursoDao, InstructorDao instructorDao) {
		this.cursoDao = cursoDao;
		this.instructorDao = instructorDao;
	}

	@Override
	public Curso cargarCursoPorId(Long cursoId) {
		return cursoDao.findById(cursoId)
				.orElseThrow(() -> new EntityNotFoundException("Curso con Id " + cursoId + " No Encontrado"));
	}

	@Override
	public Curso crearCurso(String cursoNombre, String cursoDuracion, String cursoDescripcion, Long instructorId) {
		Instructor instructor = instructorDao.findById(instructorId)
				.orElseThrow(() -> new EntityNotFoundException("Instructor con Id " + instructorId + " No Encontrado"));
		return cursoDao.save(new Curso(cursoNombre, cursoDuracion, cursoDescripcion, instructor));
	}

	@Override
	public Curso crearOrActualizarCurso(Curso curso) {
		return cursoDao.save(curso);
	}

	@Override
	public List<Curso> EncontrarCursosPorNombre(String keyword) {
		return cursoDao.findCursosByCursoNombreContains(keyword);
	}

	@Override
	public void asignarEstudianteToCurso(Long cursoId, Long estudianteId) {
		Estudiante estudiante = estudianteDao.findById(estudianteId)
				.orElseThrow(() -> new EntityNotFoundException("Estudiante con Id " + estudianteId + " No Encontrado"));
		Curso curso = cursoDao.findById(cursoId)
				.orElseThrow(() -> new EntityNotFoundException("Curso con Id " + cursoId + " No Encontrado"));
		curso.asignarEstudianteAlCurso(estudiante);
	}

	@Override
	public List<Curso> fetchAll() {
		return cursoDao.findAll();
	}

	@Override
	public List<Curso> fetchCursosPorEstudiante(Long estudianteId) {
		return cursoDao.getCursosByEstudianteId(estudianteId);
	}

	@Override
	public void removerCurso(Long cursoId) {
		cursoDao.deleteById(cursoId);
	}

}
