package com.courseConnect.admin.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.courseConnect.admin.entidad.Curso;
import com.courseConnect.admin.entidad.Instructor;
import com.courseConnect.admin.servicio.CursoServicio;
import com.courseConnect.admin.servicio.InstructorServicio;

@Controller
@RequestMapping(value = "/cursos")
public class CursoControlador {

	@Autowired
	private CursoServicio cursoServicio;

	@Autowired
	private InstructorServicio instructorServicio;

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

	@GetMapping("/delete")
	public String deleteCursos(Long cursoId, String keyword) {
		cursoServicio.removerCurso(cursoId);
		return "redirect:/cursos/index?keyword=" + keyword;
	}

	@GetMapping("/fromUpd")
	public String updateCurso(Model model, Long courseId) {
		Curso curso = cursoServicio.cargarCursoPorId(courseId);
		List<Instructor> instructors = instructorServicio.fetchInstructor();
		model.addAttribute("curso", curso);
		model.addAttribute("listInstructors", instructors);
		return "course-views/formUpdate";
	}
}
