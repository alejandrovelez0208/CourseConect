package com.courseConnect.admin.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.courseConnect.admin.entidad.Curso;
import com.courseConnect.admin.servicio.CursoServicio;

@Controller
@RequestMapping(value = "/cursos")
public class CursoControlador {

	@Autowired
	private CursoServicio cursoServicio;

	public CursoControlador(CursoServicio cursoServicio) {
		this.cursoServicio = cursoServicio;
	}

	@GetMapping(value = "/index")
	public String cursos(Model model, @RequestParam(name = "keyword", defaultValue = "") String keyword) {
		List<Curso> cursos = cursoServicio.encontrarCursosPorNombre(keyword);
		model.addAttribute("listCursos",cursos);
		model.addAttribute("keyword", keyword);
		return "curso-views/cursos";
	}
}
