package com.courseConnect.admin.utilidades;

import com.courseConnect.admin.dao.InstructorDao;
import com.courseConnect.admin.dao.RoleDao;
import com.courseConnect.admin.dao.UsuarioDao;
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

	//CONTINUAR CREANDO LAS OPERACIONES ESTUDIANTES Y CURSOS....

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
