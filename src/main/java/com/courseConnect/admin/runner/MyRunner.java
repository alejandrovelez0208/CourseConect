package com.courseConnect.admin.runner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.courseConnect.admin.entidad.Curso;
import com.courseConnect.admin.entidad.Estudiante;
import com.courseConnect.admin.entidad.Instructor;
import com.courseConnect.admin.entidad.Usuario;
import com.courseConnect.admin.servicio.CursoServicio;
import com.courseConnect.admin.servicio.EstudianteServicio;
import com.courseConnect.admin.servicio.InstructorServicio;
import com.courseConnect.admin.servicio.RoleServicio;
import com.courseConnect.admin.servicio.UsuarioServicio;

@Component
public class MyRunner implements CommandLineRunner {

	@Autowired
	private UsuarioServicio usuarioServicio;

	@Autowired
	private RoleServicio roleServicio;

	@Autowired
	private InstructorServicio instructorServicio;

	@Autowired
	private EstudianteServicio estudianteServicio;

	@Autowired
	private CursoServicio cursoServicio;

	private static final String ESTUDIANTE = "Estudiante";
	private static final String ADMIN = "Admin";
	private static final String INSTRUCTOR = "Instructor";

	@Override
	public void run(String... args) throws Exception {
		Usuario usuario1 = usuarioServicio.crearUsuarios("usuario1@gmail.com", "pass1");
		Usuario usuario2 = usuarioServicio.crearUsuarios("usuario2@gmail.com", "pass2");

		roleServicio.crearRole(ADMIN);
		roleServicio.crearRole(INSTRUCTOR);
		roleServicio.crearRole(ESTUDIANTE);

		usuarioServicio.asignarRoleToUsuario(usuario1.getEmail(), ADMIN);
		usuarioServicio.asignarRoleToUsuario(usuario1.getEmail(), INSTRUCTOR);
		usuarioServicio.asignarRoleToUsuario(usuario2.getEmail(), ESTUDIANTE);

		Instructor instructor1 = instructorServicio.crearInstructor("InstructorFN1", "Instructor1LN",
				"Instructor Experimentado", "instructor1@gmail.com", "pass1");
		Instructor instructor2 = instructorServicio.crearInstructor("InstructorFN2", "Instructor2LN",
				"Instructor Senior", "instructor2@gmail.com", "pass2");

		Estudiante estudiante1 = estudianteServicio.crearEstudiante("std1FN", "std1LN", "Begginner",
				"stdUsuario1@gmail.com", "pass1");

		Estudiante estudiante2 = estudianteServicio.crearEstudiante("std2FN", "std2LN", "Master",
				"stdUsuario2@gmail.com", "pass2");

		Curso curso1 = cursoServicio.crearCurso("Spring Service", "2 Hours", "Master Spring Service",
				instructor1.getInstructorId());

		Curso curso2 = cursoServicio.crearCurso("Spring Data JPA", "4 Hours", "Introduccion a JPA",
				instructor2.getInstructorId());

		cursoServicio.asignarEstudianteToCurso(curso1.getCursoId(), estudiante1.getEstudianteId());
		cursoServicio.asignarEstudianteToCurso(curso2.getCursoId(), estudiante2.getEstudianteId());
	};
}
