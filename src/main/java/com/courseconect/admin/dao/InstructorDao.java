package com.courseconect.admin.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.courseconect.admin.entidad.Instructor;

public interface InstructorDao extends JpaRepository<Instructor, Long> {

	@Query(value = "select i from Instructor as i where i.nombres like %:name% or i.apellidos like %:name%", nativeQuery = true)
	List<Instructor> buscarInstructoresPorNombre(@Param("name") String name);

	@Query(value = "select i from Instructor as i where i.usuario.email =:email", nativeQuery = true)
	List<Instructor> buscarInstructorPorEmail(@Param("email") String email);
}
