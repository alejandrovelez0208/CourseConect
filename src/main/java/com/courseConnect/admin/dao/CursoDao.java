package com.courseConnect.admin.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.courseConnect.admin.entidad.Curso;

public interface CursoDao extends JpaRepository<Curso, Long> {

	List<Curso> findByCursoNombre(String cursoNombre);

	@Query(value = "select * Cursos as c where c.curso_id in (select e.curso_id from matriculado_en as e where e.estudiante_id=:estudianteId)", nativeQuery = true)
	List<Curso> getCursosPorEstudianteId(Long estudianteId);
}
