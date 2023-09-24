package com.courseConnect.admin.runner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.courseConnect.admin.entidad.Contenido;
import com.courseConnect.admin.entidad.Curso;
import com.courseConnect.admin.entidad.Estudiante;
import com.courseConnect.admin.entidad.Instructor;
import com.courseConnect.admin.entidad.Usuario;
import com.courseConnect.admin.servicio.ContenidoServicio;
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

	@Autowired
	private ContenidoServicio contenidoServicio;

	private static final String ESTUDIANTE = "Estudiante";
	private static final String ADMIN = "Admin";
	private static final String INSTRUCTOR = "Instructor";

	@Override
	public void run(String... args) throws Exception {
		byte[] byteArray = { 1, 2, 3, 4, 5 };

		Usuario usuario1 = usuarioServicio.crearUsuarios("usuario1@gmail.com", "pass1");
		Usuario usuario2 = usuarioServicio.crearUsuarios("usuario2@gmail.com", "pass2");

		roleServicio.crearRole(ADMIN);
		roleServicio.crearRole(INSTRUCTOR);
		roleServicio.crearRole(ESTUDIANTE);

		usuarioServicio.asignarRoleToUsuario(usuario1.getEmail(), ADMIN);
		usuarioServicio.asignarRoleToUsuario(usuario1.getEmail(), INSTRUCTOR);
		usuarioServicio.asignarRoleToUsuario(usuario2.getEmail(), ESTUDIANTE);

		Instructor instructor1 = instructorServicio.crearInstructor("Jose Carlo", "Guzman Londoño",
				"Instructor Experimentado", "instructor1@gmail.com", "pass1");
		Instructor instructor2 = instructorServicio.crearInstructor("Hernan Felipe", "Sepulveda Arango",
				"Instructor Senior", "instructor2@gmail.com", "pass2");

		Estudiante estudiante1 = estudianteServicio.crearEstudiante("std1FN", "std1LN", "Begginner",
				"stdUsuario1@gmail.com", "pass1");

		Estudiante estudiante2 = estudianteServicio.crearEstudiante("std2FN", "std2LN", "Master",
				"stdUsuario2@gmail.com", "pass2");

		Curso curso1 = cursoServicio.crearCurso("Spring Framework", "2 Horas",
				"Spring es un framework para el desarrollo de aplicaciones y contenedor de inversión de control, de código abierto para la plataforma Java",
				instructor1.getInstructorId());

		Curso curso2 = cursoServicio.crearCurso("Spring Data JPA", "4 Horas",
				"Spring Data es uno de los frameworks que se encuentra dentro de la plataforma de Spring.  Su objetivo es simplificar al desarrollador la persistencia de datos",
				instructor2.getInstructorId());

		Curso curso3 = cursoServicio.crearCurso("Angular", "7 Horas",
				"Angular es un framework para aplicaciones web desarrollado en TypeScript, de código abierto, mantenido por Google",
				instructor1.getInstructorId());

		Curso curso4 = cursoServicio.crearCurso("React", "3 Horas",
				"React es un framework para aplicaciones web desarrollado en TypeScript, de código abierto, mantenido por Google",
				instructor2.getInstructorId());

		Curso curso5 = cursoServicio.crearCurso("Javascript", "11 Horas",
				"JavaScript es un lenguaje de programación interpretado. Se define como orientado a objetos, ​basado en prototipos.",
				instructor1.getInstructorId());

		Curso curso6 = cursoServicio.crearCurso("Bootstrap", "1 Hora",
				"Bootstrap es una biblioteca multiplataforma o conjunto de herramientas de código abierto para diseño de sitios y aplicaciones web",
				instructor2.getInstructorId());

		Curso curso7 = cursoServicio.crearCurso("Python", "3 Horas",
				"Python es un lenguaje de alto nivel de programación interpretado cuya filosofía hace hincapié en la legibilidad de su código",
				instructor2.getInstructorId());

		Contenido archivos1 = contenidoServicio.guardarContenido("docApoyo1", byteArray, "docApoyo2", byteArray,
				"task1", byteArray, "task2", byteArray, "tutorial", byteArray, "imagen", byteArray,
				curso1.getCursoId());

		cursoServicio.asignarEstudianteToCurso(curso1.getCursoId(), estudiante1.getEstudianteId());
		cursoServicio.asignarEstudianteToCurso(curso2.getCursoId(), estudiante2.getEstudianteId());
		cursoServicio.asignarEstudianteToCurso(curso3.getCursoId(), estudiante1.getEstudianteId());
		cursoServicio.asignarEstudianteToCurso(curso4.getCursoId(), estudiante2.getEstudianteId());
		cursoServicio.asignarEstudianteToCurso(curso5.getCursoId(), estudiante1.getEstudianteId());
		cursoServicio.asignarEstudianteToCurso(curso6.getCursoId(), estudiante2.getEstudianteId());
		cursoServicio.asignarEstudianteToCurso(curso7.getCursoId(), estudiante1.getEstudianteId());
		cursoServicio.asignarEstudianteToCurso(curso1.getCursoId(), estudiante2.getEstudianteId());
	};
}
