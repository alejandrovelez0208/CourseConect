package com.courseConnect.admin.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.courseConnect.admin.entidad.Instructor;
import com.courseConnect.admin.servicio.InstructorServicio;

@Controller
@RequestMapping(value = "/instructores")
public class InstructorControlador {

	@Autowired
	private InstructorServicio instructorServicio;

	@GetMapping(value = "/index")
	public String instructor(Model model, @RequestParam(name = "keyword", defaultValue = "") String keyword) {
		List<Instructor> instructores = instructorServicio.buscarInstructorPorNombre(keyword != null ? keyword : "");
		model.addAttribute("listInstructors", instructores);
		model.addAttribute("keyword", keyword);
		return "instrutores-views/instructores";
	}

	@GetMapping(value = "/delete")
	public String eliminarInstructor(Long instructorId, String keyword) {
		instructorServicio.removerInstructor(instructorId);
		return "redirect:/instructores/index?keyword=" + keyword;
	}

	@GetMapping(value = "/formUpdate")
	public String actualizarInstructor(Model model, Long instructorId) {
		Instructor instructor = instructorServicio.cargarInstructorPorId(instructorId);
		model.addAttribute("instructor", instructor);
		return "instructor-views/formUpdate";
	}
}
