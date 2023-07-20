package com.courseConnect.admin.servicio;

import java.util.List;

import com.courseConnect.admin.entidad.Curso;

public interface CursoServicio {

	Curso cargarCursoPorId(Long cursoId);

	Curso crearCurso(String cursoNombre, String cursoDuracion, String cursoDescripcion, Long instructorId);

	Curso crearOrActualizarCurso(Curso curso);

	List<Curso> EncontrarCursosPorNombre(String keyword);

	void asignarEstudianteToCurso(Long cursoId, Long estudianteId);

	List<Curso> fetchAll();

	List<Curso> fetchCursosPorEstudiante(Long estudianteId);

	void removerCurso(Long cursoId);
}
