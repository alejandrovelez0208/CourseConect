package com.courseConnect.admin.web;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.courseConnect.admin.dao.ContenidoDao;
import com.courseConnect.admin.dao.InstructorDao;
import com.courseConnect.admin.entidad.Contenido;
import com.courseConnect.admin.entidad.Curso;
import com.courseConnect.admin.entidad.Instructor;
import com.courseConnect.admin.servicio.ContenidoServicio;
import com.courseConnect.admin.servicio.CursoServicio;
import com.courseConnect.admin.servicio.InstructorServicio;

@Controller
@RequestMapping(value = "/cursos")
public class CursoControlador {

	@Autowired
	private CursoServicio cursoServicio;

	@Autowired
	private InstructorServicio instructorServicio;

	@Autowired
	private ContenidoDao contenidoDao;

	@Autowired
	private ContenidoServicio contenidoServicio;

	public CursoControlador(CursoServicio cursoServicio) {
		this.cursoServicio = cursoServicio;
	}

	@GetMapping(value = "/index")
	public String cursos(Model model, @RequestParam(name = "keyword", defaultValue = "") String keyword) {
		List<Curso> cursos = cursoServicio.encontrarCursosPorNombre(keyword);
		model.addAttribute("listCursos", cursos);
		model.addAttribute("keyword", keyword);
		return "curso-views/cursos";
	}

	@GetMapping(value = "/delete")
	public String deleteCursos(Long cursoId, String keyword) {
		cursoServicio.removerCurso(cursoId);
		return "redirect:/cursos/index?keyword=" + keyword;
	}

	@GetMapping(value = "/formUpdate")
	public String updateCursos(Model model, Long cursoId) {
		Curso curso = cursoServicio.cargarCursoPorId(cursoId);
		Contenido contenido = new Contenido();
		List<Instructor> instructors = instructorServicio.fetchInstructor();
		model.addAttribute("curso", curso);
		model.addAttribute("listInstructors", instructors);
		model.addAttribute("contenido", contenido);
		return "curso-views/formUpdate";
	}

	@GetMapping(value = "/formCrear")
	public String formCrear(Model model) {
		List<Instructor> instructors = instructorServicio.fetchInstructor();
		model.addAttribute("listInstructors", instructors);
		model.addAttribute("curso", new Curso());
		model.addAttribute("contenido", new Contenido());
		return "curso-views/formCrear";
	}

	@PostMapping(value = "/save", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
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
		return "redirect:/cursos/index";
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
		model.addAttribute("listCursos", cursosSubcritos);
		model.addAttribute("otrosCursos", otrosCursos);
		return "curso-views/estudiante-cursos";
	}

	@GetMapping(value = "/inscribirEstudiante")
	public String inscribirEstudianteActualEnCurso(Long cursoId) {
		Long estudianteId = 1L;
		cursoServicio.asignarEstudianteToCurso(cursoId, estudianteId);
		return "redirect:/cursos/index/estudiante";
	}
}
