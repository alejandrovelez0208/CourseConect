package com.courseConnect.admin.utilidades;

import java.util.Optional;

import com.courseConnect.admin.dao.CursoDao;
import com.courseConnect.admin.dao.EstudianteDao;
import com.courseConnect.admin.dao.InstructorDao;
import com.courseConnect.admin.dao.RoleDao;
import com.courseConnect.admin.dao.UsuarioDao;
import com.courseConnect.admin.entidad.Curso;
import com.courseConnect.admin.entidad.Estudiante;
import com.courseConnect.admin.entidad.Instructor;
import com.courseConnect.admin.entidad.Role;
import com.courseConnect.admin.entidad.Usuario;

import jakarta.persistence.EntityNotFoundException;

public class UtilidadOperaciones {

	public static void OperacionesUsuarios(UsuarioDao usuarioDao) {
		crearUsuarios(usuarioDao);
		actualizarUsuarios(usuarioDao);
		eliminarUsuarios(usuarioDao);
		fetchUsuarios(usuarioDao);
	}

	public static void operacionesRoles(RoleDao roleDao) {
		crearRoles(roleDao);
		actualizarRole(roleDao);
		eliminarRole(roleDao);
		fetchRole(roleDao);
	}

	public static void operacionesInstructor(UsuarioDao usuarioDao, InstructorDao instructorDao, RoleDao roleDao) {
		crearInstructores(usuarioDao, instructorDao, roleDao);
		actualizarInstructor(instructorDao);
		removerInstructor(instructorDao);
		fetchInstructor(instructorDao);
	}

	public void estudiantesOperaciones(UsuarioDao usuarioDao, EstudianteDao estudianteDao, RoleDao roleDao) {
		crearEstudiantes(usuarioDao, estudianteDao, roleDao);
		actualizarEstudiante(estudianteDao);
		removerEstudiante(estudianteDao);
		fetchEstudiantes(estudianteDao);
	}

	public void operacionesDelCurso(CursoDao cursoDao, InstructorDao instructorDao, EstudianteDao estudianteDao) {
		crearCursos(cursoDao, instructorDao);
		actualizarCursos(cursoDao);
		eliminarCursos(cursoDao);
		fetchCursos(cursoDao);
		assignarEstudiantesAlCurso(cursoDao, estudianteDao);
		fetchCursosPorEstudiante(cursoDao);
	}

	private void fetchCursosPorEstudiante(CursoDao cursoDao) {
		cursoDao.getCursosPorEstudianteId(1L).forEach(curso -> System.out.print(curso.toString()));
	}

	private void assignarEstudiantesAlCurso(CursoDao cursoDao, EstudianteDao estudianteDao) {
		Optional<Estudiante> estudiante1 = estudianteDao.findById(1L);
		Optional<Estudiante> estudiante2 = estudianteDao.findById(2L);

		Curso curso = cursoDao.findById(1L).orElseThrow(() -> new EntityNotFoundException("Curso No Encontrado"));

		estudiante1.ifPresent(curso::asignarEstudianteAlCurso);
		estudiante2.ifPresent(curso::asignarEstudianteAlCurso);

		cursoDao.save(curso);
	}

	private void fetchCursos(CursoDao cursoDao) {
		cursoDao.findAll().forEach(curso -> System.out.print(curso.toString()));
	}

	private void eliminarCursos(CursoDao cursoDao) {
		cursoDao.deleteById(2L);
	}

	private void actualizarCursos(CursoDao cursoDao) {
		Curso curso = cursoDao.findById(1L).orElseThrow(() -> new EntityNotFoundException("Curso No Encontrado"));
		curso.setCursoDuracion("20 Hours");
		cursoDao.save(curso);
	}

	private void crearCursos(CursoDao cursoDao, InstructorDao instructorDao) {
		Instructor instructor = instructorDao.findById(1L)
				.orElseThrow(() -> new EntityNotFoundException("Instructor No Encontrado"));

		Curso curso = new Curso("Javascript", "5", "Introduccion a Javascript", instructor);
		cursoDao.save(curso);

		Curso curso2 = new Curso("Angula", "3", "Introduccion a Angular", instructor);
		cursoDao.save(curso2);
	}

	private void fetchEstudiantes(EstudianteDao estudianteDao) {
		estudianteDao.findAll().forEach(estudiante -> System.out.print(estudiante.toString()));
	}

	private void removerEstudiante(EstudianteDao estudianteDao) {
		estudianteDao.deleteById(1L);
	}

	private void actualizarEstudiante(EstudianteDao estudianteDao) {
		Estudiante estudiante = estudianteDao.findById(2L)
				.orElseThrow(() -> new EntityNotFoundException("Estudiante No Encontrado"));
		estudiante.setNombres("NombreActualizado");
		estudiante.setApellidos("ApellidoActualizado");
		estudianteDao.save(estudiante);
	}

	private void crearEstudiantes(UsuarioDao usuarioDao, EstudianteDao estudianteDao, RoleDao roleDao) {
		Role role = roleDao.findByNombre("Student");
		if (role == null)
			throw new EntityNotFoundException("Role No Encontrado");

		Usuario usuario1 = new Usuario("estudianteUsuario1@gmail.com", "pass1");
		usuarioDao.save(usuario1);
		usuario1.asignarRoleAUsuario(role);

		Estudiante estudiante1 = new Estudiante("estudiante1FN", "estudiante1LN", "master", usuario1);
		estudianteDao.save(estudiante1);

		Usuario usuario2 = new Usuario("estudianteUsuario2@gmail.com", "pass2");
		usuarioDao.save(usuario2);
		usuario2.asignarRoleAUsuario(role);

		Estudiante estudiante2 = new Estudiante("estudiante2FN", "estudiante2LN", "master", usuario2);
		estudianteDao.save(estudiante2);
	}

	private static void fetchInstructor(InstructorDao instructorDao) {
		instructorDao.findAll().forEach(instructor -> System.out.print(instructor.toString()));
	}

	private static void removerInstructor(InstructorDao instructorDao) {
		instructorDao.deleteById(2L);
	}

	private static void actualizarInstructor(InstructorDao instructorDao) {
		Instructor instructor = instructorDao.findById(2L)
				.orElseThrow(() -> new EntityNotFoundException("Instructor No Encontrado"));
		instructor.setSummary("Instructor Certificado");
		instructorDao.save(instructor);
	}

	private static void crearInstructores(UsuarioDao usuarioDao, InstructorDao instructorDao, RoleDao roleDao) {
		Role role = roleDao.findByNombre("Instructor");
		if (role == null) {
			throw new EntityNotFoundException("Role No Encontrado");
		}

		Usuario usuario1 = new Usuario("instructorUsuario1@gmail.com", "pass1");
		usuarioDao.save(usuario1);
		usuario1.asignarRoleAUsuario(role);

		Instructor instructor1 = new Instructor("instructor1FN", "instructor1LN", "Instructor Experimentado", usuario1);
		instructorDao.save(instructor1);

		Usuario usuario2 = new Usuario("instructorUser1@gmail.com", "pass2");
		usuarioDao.save(usuario2);
		usuario2.asignarRoleAUsuario(role);
		Instructor instructor2 = new Instructor("instructor2FN", "instructor2LN", "Instructor Senior", usuario2);
		instructorDao.save(instructor2);
	}

	private static void fetchRole(RoleDao roleDao) {
		roleDao.findAll().forEach(role -> System.out.print(role.toString()));
	}

	private static void eliminarRole(RoleDao roleDao) {
		roleDao.deleteById(2L);
	}

	private static void actualizarRole(RoleDao roleDao) {
		Role role = roleDao.findById(1L).orElseThrow(() -> new EntityNotFoundException("Role No Encontrado"));
		role.setNombre("Nuevo Admin");
		roleDao.save(role);
	}

	private static void crearRoles(RoleDao roleDao) {
		Role role1 = new Role("Admin");
		roleDao.save(role1);
		Role role2 = new Role("Instructor");
		roleDao.save(role2);
		Role role3 = new Role("Student");
		roleDao.save(role3);
	}

	private static void fetchUsuarios(UsuarioDao usuarioDao) {
		usuarioDao.findAll().forEach(user -> System.out.print(user.toString()));
	}

	private static void eliminarUsuarios(UsuarioDao usuarioDao) {
		Usuario usuario = usuarioDao.findById(3L)
				.orElseThrow(() -> new EntityNotFoundException("Usuario No Encontrado"));
		usuarioDao.delete(usuario);
	}

	private static void actualizarUsuarios(UsuarioDao usuarioDao) {
		Usuario usuario = usuarioDao.findById(2L)
				.orElseThrow(() -> new EntityNotFoundException("Usuario No Encontrado"));
		usuario.setEmail("newEmail@gmail.com");
		usuarioDao.save(usuario);
	}

	private static void crearUsuarios(UsuarioDao usuarioDao) {
		Usuario usuario1 = new Usuario("usuario1@gmail.com", "pass1");
		usuarioDao.save(usuario1);
		Usuario usuario2 = new Usuario("usuario2@gmail.com", "pass2");
		usuarioDao.save(usuario2);
		Usuario usuario3 = new Usuario("usuario3@gmail.com", "pass3");
		usuarioDao.save(usuario3);
		Usuario usuario4 = new Usuario("usuario4@gmail.com", "pass4");
		usuarioDao.save(usuario4);
	}
}
