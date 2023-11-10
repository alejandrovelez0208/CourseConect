package com.courseConnect.admin.web;

import static com.courseConnect.admin.constantes.CourseConnectConstantes.APELLIDO;
import static com.courseConnect.admin.constantes.CourseConnectConstantes.CONTENIDO;
import static com.courseConnect.admin.constantes.CourseConnectConstantes.CURSO;
import static com.courseConnect.admin.constantes.CourseConnectConstantes.IMG_DATA_URL;
import static com.courseConnect.admin.constantes.CourseConnectConstantes.INSTRUCTOR;
import static com.courseConnect.admin.constantes.CourseConnectConstantes.INSTRUCTOR_ACTUAL;
import static com.courseConnect.admin.constantes.CourseConnectConstantes.KEYWORD;
import static com.courseConnect.admin.constantes.CourseConnectConstantes.LIST_CURSOS;
import static com.courseConnect.admin.constantes.CourseConnectConstantes.LIST_INSTRUCTORS;
import static com.courseConnect.admin.constantes.CourseConnectConstantes.NOMBRE;
import static com.courseConnect.admin.constantes.CourseConnectConstantes.OTROS_CURSOS;
import static com.courseConnect.admin.constantes.CourseConnectConstantes.VIDEO_DATA_URL;

import java.io.IOException;
import java.security.Principal;
import java.util.Base64;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
	@PreAuthorize("hasAnyAuthority('Admin','Instructor')")
	public String formCrear(Model model, Principal principal, Authentication autenticacion) {
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
	@PreAuthorize("hasAuthority('Estudiante')")
	public String cursosParaEstudianteActual(Model model, Principal principal) {
		Usuario usuario = usuarioServicio.cargarUsuarioPorEmail(principal.getName());
		List<Curso> cursosSubcritos = cursoServicio.fetchCursosPorEstudiante(usuario.getEstudiante().getEstudianteId());
		List<Curso> otrosCursos = cursoServicio.fetchAll().stream().filter(curso -> !cursosSubcritos.contains(curso))
				.collect(Collectors.toList());
		model.addAttribute(LIST_CURSOS, cursosSubcritos);
		model.addAttribute(OTROS_CURSOS, otrosCursos);
		model.addAttribute(NOMBRE, usuario.getEstudiante().getNombres());
		model.addAttribute(APELLIDO, usuario.getEstudiante().getApellidos());
		return "curso-views/estudiante-cursos";
	}

	@GetMapping(value = "/inscribirEstudiante")
	@PreAuthorize("hasAuthority('Estudiante')")
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
		formatearArchivos(model, contenido.getImagenGuia(), contenido.getArchivoVideo());
		model.addAttribute(CONTENIDO, contenido);
		return "curso-views/formAprendizaje";
	}

	@GetMapping(value = "/ver-pdf")
	public ResponseEntity<byte[]> verPDF(@RequestParam(name = "id") Long id,
			@RequestParam(name = "tarea") String tarea) {
		byte[] contenidoPDF = null;
		Contenido contenido = contenidoServicio.cargarContenidoById(id);
		if (contenido != null) {
			if (tarea.equalsIgnoreCase("DOC_I")) {
				contenidoPDF = contenido.getArchivo1Pdf();
			} else if (tarea.equalsIgnoreCase("DOC_II")) {
				contenidoPDF = contenido.getArchivo2Pdf();
			}
			if (contenidoPDF != null) {
				HttpHeaders headers = new HttpHeaders();
				headers.setContentType(MediaType.APPLICATION_PDF);
				headers.setContentDispositionFormData("attachment", "archivo.pdf");
				headers.setContentLength(contenidoPDF.length);

				return new ResponseEntity<>(contenidoPDF, headers, HttpStatus.OK);
			}
		}
		// REVISAR NO SAE BIEN EL FORMATO
		return null;
	}

	@GetMapping(value = "/ver-doc")
	public ResponseEntity<byte[]> verDoc(@RequestParam(name = "id") Long id,
			@RequestParam(name = "tarea") String tarea) {
		byte[] contenidoDoc = null;
		Contenido contenido = contenidoServicio.cargarContenidoById(id);
		if (contenido != null) {
			if (tarea.equalsIgnoreCase("TASK_I")) {
				contenidoDoc = contenido.getArchivo1Doc();
			} else if (tarea.equalsIgnoreCase("TASK_II")) {
				contenidoDoc = contenido.getArchivo2Doc();
			}
			if (contenidoDoc != null) {
				HttpHeaders headers = new HttpHeaders();
				headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
				headers.setContentDispositionFormData("attachment", "documento_word.docx");

				return new ResponseEntity<>(contenidoDoc, headers, HttpStatus.OK);
			}
		}
		return null;
	}

	public void formatearArchivos(Model model, byte[] imagen, byte[] video) {
		if (imagen != null && video != null) {
			String imagenDataURL = "data:image/png;base64," + Base64.getEncoder().encodeToString(imagen);
			model.addAttribute(IMG_DATA_URL, imagenDataURL);

			String videoDataURL = "data:video/mp4;base64," + Base64.getEncoder().encodeToString(video);
			model.addAttribute(VIDEO_DATA_URL, videoDataURL);
		}
	}

}
