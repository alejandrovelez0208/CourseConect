package com.courseConnect.admin.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.courseConnect.admin.entidad.Estudiante;

public interface EstudianteDao extends JpaRepository<Estudiante, Long> {

	@Query(value = "select e from Estudiante as e where e.nombres like %:name% or e.apellidos like %:name%", nativeQuery = true)
	List<Estudiante> buscarEstudiantesPorNombre(@Param("name") String name);

	@Query(value = "select e from Estudiante as e where e.usuario.email =:email", nativeQuery = true)
	Estudiante buscarEstudiantePorEmail(@Param("email") String email);
}
