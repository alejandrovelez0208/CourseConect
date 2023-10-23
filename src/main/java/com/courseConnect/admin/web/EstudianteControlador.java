package com.courseConnect.admin.web;

import static com.courseConnect.admin.constantes.CourseConnectConstantes.KEYWORD;
import static com.courseConnect.admin.constantes.CourseConnectConstantes.LIST_ESTUDIANTES;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.courseConnect.admin.entidad.Estudiante;
import com.courseConnect.admin.servicio.EstudianteServicio;

@Controller
@RequestMapping("estudiantes")
public class EstudianteControlador {

	@Autowired
	private EstudianteServicio estudianteServicio;

	public EstudianteControlador(EstudianteServicio estudianteServicio) {
		this.estudianteServicio = estudianteServicio;
	}

	@GetMapping(value = "/index")
	public String estudiantes(Model model, @RequestParam(name = KEYWORD, defaultValue = "") String keyword) {
		List<Estudiante> estudiantes = estudianteServicio.cargarEstudiantePorNombre(keyword);
		model.addAttribute(LIST_ESTUDIANTES, estudiantes);
		model.addAttribute(KEYWORD, keyword);
		return "estudiante-views/estudiantes";
	}
	
	@GetMapping(value = "/formUpdate")
	public String eliminarEstudiante(Model model, Long estudianteId) {
		Estudiante estudiante = estudianteServicio.cargarEstudiantePorId(estudianteId);
		model.addAttribute("estudiante", estudiante);
		return "estudiante-views/formUpdate";
	}
}
