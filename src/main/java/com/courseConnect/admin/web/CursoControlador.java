package com.courseConnect.admin.web;

import java.io.IOException;
import java.security.Principal;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.courseConnect.admin.dao.ContenidoDao;
import com.courseConnect.admin.entidad.Contenido;
import com.courseConnect.admin.entidad.Curso;
import com.courseConnect.admin.entidad.Instructor;
import com.courseConnect.admin.entidad.Usuario;
import com.courseConnect.admin.servicio.ContenidoServicio;
import com.courseConnect.admin.servicio.CursoServicio;
import com.courseConnect.admin.servicio.InstructorServicio;
import com.courseConnect.admin.servicio.UsuarioServicio;

import static com.courseConnect.admin.constantes.CourseConnectConstantes.*;

@Controller
@RequestMapping(value = "/cursos")
public class CursoControlador {

	private CursoServicio cursoServicio;

	private InstructorServicio instructorServicio;

	@Autowired
	private ContenidoDao contenidoDao;

	@Autowired
	private ContenidoServicio contenidoServicio;

	@Autowired
	private UsuarioServicio usuarioServicio;

	public CursoControlador(CursoServicio cursoServicio, InstructorServicio instructorServicio,
			UsuarioServicio usuarioServicio) {
		this.cursoServicio = cursoServicio;
		this.instructorServicio = instructorServicio;
		this.usuarioServicio = usuarioServicio;
	}

	@GetMapping(value = "/index")
	@PreAuthorize("hasAuthority('Admin')")
	public String cursos(Model model, @RequestParam(name = KEYWORD, defaultValue = "") String keyword) {
		List<Curso> cursos = cursoServicio.encontrarCursosPorNombre(keyword);
		model.addAttribute(LIST_CURSOS, cursos);
		model.addAttribute(KEYWORD, keyword);
		return "curso-views/cursos";
	}

	@GetMapping(value = "/delete")
	@PreAuthorize("hasAuthority('Admin')")
	public String deleteCursos(Long cursoId, String keyword) {
		cursoServicio.removerCurso(cursoId);
		return "redirect:/cursos/index?keyword=" + keyword;
	}

	@GetMapping(value = "/formUpdate")
	@PreAuthorize("hasAnyAuthority('Admin','Instructor')")
	public String updateCursos(Model model, Long cursoId, Principal principal, Authentication autenticacion) {
		if (usuarioServicio.usuarioActualTieneRolAhora("Instructor")) {
			Instructor instructor = instructorServicio.cargarInstructorPorEmail(principal.getName());
			model.addAttribute(INSTRUCTOR_ACTUAL, instructor);
		}
		if (autenticacion != null) {
			Collection<? extends GrantedAuthority> autoridades = autenticacion.getAuthorities();
			if (autoridades.stream().anyMatch(auth -> auth.getAuthority().equals("Admin"))) {
				model.addAttribute("adminSelect", true);
			} else if (autoridades.stream().anyMatch(auth -> auth.getAuthority().equals("Instructor"))) {
				model.addAttribute("instructorSelect", true);
			}
		}
		Curso curso = cursoServicio.cargarCursoPorId(cursoId);
		Contenido contenido = new Contenido();
		List<Instructor> instructors = instructorServicio.fetchInstructor();
		model.addAttribute(CURSO, curso);
		model.addAttribute(LIST_INSTRUCTORS, instructors);
		model.addAttribute(CONTENIDO, contenido);
		return "curso-views/formActualizar";
	}

	@GetMapping(value = "/formCrear")
	public String formCrear(Model model) {
		List<Instructor> instructors = instructorServicio.fetchInstructor();
		model.addAttribute(LIST_INSTRUCTORS, instructors);
		model.addAttribute(CURSO, new Curso());
		model.addAttribute(CONTENIDO, new Contenido());
		return "curso-views/formCrear";
	}

	@PostMapping(value = "/save", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
	@PreAuthorize("hasAnyAuthority('Admin', 'Instructor')")
	public String save(@RequestParam("archivo1Pdf") MultipartFile archivo1Pdf,
			@RequestParam("archivo2Pdf") MultipartFile archivo2Pdf,
			@RequestParam("archivo1Doc") MultipartFile archivo1Doc,
			@RequestParam("archivo2Doc") MultipartFile archivo2Doc,
			@RequestParam("archivoVideo") MultipartFile archivoVideo,
			@RequestParam("imagenGuia") MultipartFile imagenGuia, Curso curso) throws IOException {
		cursoServicio.crearOrActualizarCurso(curso);
		Optional<Contenido> contenido = contenidoDao.findById(curso.getCursoId());
		if (contenido != null && !contenido.isEmpty()) {
			actualizarOrCrearContenidoCurso(archivo1Pdf, archivo2Pdf, archivo1Doc, archivo2Doc, archivoVideo,
					imagenGuia, curso.getCursoId(), true);
		} else {
			actualizarOrCrearContenidoCurso(archivo1Pdf, archivo2Pdf, archivo1Doc, archivo2Doc, archivoVideo,
					imagenGuia, curso.getCursoId(), false);
		}
		return usuarioServicio.usuarioActualTieneRolAhora(INSTRUCTOR) ? "redirect:/cursos/index/instructor"
				: "redirect:/cursos/index";
	}

	public void actualizarOrCrearContenidoCurso(MultipartFile archivo1Pdf, MultipartFile archivo2Pdf,
			MultipartFile archivo1Doc, MultipartFile archivo2Doc, MultipartFile archivoVideo, MultipartFile imagenGuia,
			Long cursoId, Boolean estado) throws IOException {
		Contenido contenido = new Contenido();
		if (estado) {
			contenido = contenidoServicio.cargarContenidoById(cursoId);
		}
		contenido.setNombre1Pdf(archivo1Pdf.getOriginalFilename());
		contenido.setArchivo1Pdf(archivo1Pdf.getBytes());
		contenido.setNombre2Pdf(archivo2Pdf.getOriginalFilename());
		contenido.setArchivo2Pdf(archivo2Pdf.getBytes());
		contenido.setNombre1Doc(archivo1Doc.getOriginalFilename());
		contenido.setArchivo1Doc(archivo1Doc.getBytes());
		contenido.setNombre2Doc(archivo2Doc.getOriginalFilename());
		contenido.setArchivo2Doc(archivo2Doc.getBytes());
		contenido.setTutorialVideo(archivoVideo.getOriginalFilename());
		contenido.setArchivoVideo(archivoVideo.getBytes());
		contenido.setImagenNombre(imagenGuia.getOriginalFilename());
		contenido.setImagenGuia(imagenGuia.getBytes());
		if (estado) {
			contenidoServicio.crearOrActualizarContenido(contenido);
			return;
		}
		contenidoServicio.guardarContenido(contenido.getNombre1Pdf(), contenido.getArchivo1Pdf(),
				contenido.getNombre2Pdf(), contenido.getArchivo2Pdf(), contenido.getNombre1Doc(),
				contenido.getArchivo1Doc(), contenido.getNombre2Doc(), contenido.getArchivo2Doc(),
				contenido.getTutorialVideo(), contenido.getArchivoVideo(), contenido.getImagenNombre(),
				contenido.getImagenGuia(), cursoId);
	}

	@GetMapping(value = "index/estudiante")
	public String cursosParaEstudianteActual(Model model) {
		Long estudianteId = 1L;
		List<Curso> cursosSubcritos = cursoServicio.fetchCursosPorEstudiante(estudianteId);
		List<Curso> otrosCursos = cursoServicio.fetchAll().stream().filter(curso -> !cursosSubcritos.contains(curso))
				.collect(Collectors.toList());
		model.addAttribute(LIST_CURSOS, cursosSubcritos);
		model.addAttribute(OTROS_CURSOS, otrosCursos);
		return "curso-views/estudiante-cursos";
	}

	@GetMapping(value = "/inscribirEstudiante")
	public String inscribirEstudianteActualEnCurso(Long cursoId) {
		Long estudianteId = 1L;
		cursoServicio.asignarEstudianteToCurso(cursoId, estudianteId);
		return "redirect:/cursos/index/estudiante";
	}

	@GetMapping(value = "index/instructor")
	@PreAuthorize("hasAuthority('Instructor')")
	public String cursosParaInstructorActual(Model model, Principal principal) {
		Usuario usuario = usuarioServicio.cargarUsuarioPorEmail(principal.getName());
		Instructor instructor = instructorServicio.cargarInstructorPorId(usuario.getInstructor().getInstructorId());
		model.addAttribute(LIST_CURSOS, instructor.getCurso());
		model.addAttribute(NOMBRE, instructor.getNombres());
		model.addAttribute(APELLIDO, instructor.getApellidos());
		return "curso-views/instructor-cursos";
	}

	@GetMapping(value = "/instructor")
	public String cursosByIdInstructor(Model model, Long instructorId) {
		Instructor instructor = instructorServicio.cargarInstructorPorId(instructorId);
		model.addAttribute(LIST_CURSOS, instructor.getCurso());
		return "curso-views/instructor-cursos";
	}

	@GetMapping(value = "/reanuarAprendizaje")
	public String reanuarAprendizaje(Model model, Long contenidoId) {
		Contenido contenido = contenidoServicio.cargarContenidoById(contenidoId);
		model.addAttribute(CONTENIDO, contenido);
		return "curso-views/formAprendizaje";
	}
}
