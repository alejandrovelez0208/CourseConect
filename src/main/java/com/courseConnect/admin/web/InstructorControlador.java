package com.courseConnect.admin.web;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.courseConnect.admin.entidad.Instructor;
import com.courseConnect.admin.entidad.Usuario;
import com.courseConnect.admin.servicio.InstructorServicio;
import com.courseConnect.admin.servicio.UsuarioServicio;
import static com.courseConnect.admin.constantes.CourseConnectConstantes.*;
import jakarta.validation.Valid;

@Controller
@RequestMapping(value = "/instructores")
public class InstructorControlador {

	@Autowired
	private InstructorServicio instructorServicio;

	@Autowired
	private UsuarioServicio usuarioServicio;

	@GetMapping(value = "/index")
	@PreAuthorize("hasAuthority('Admin')")
	public String instructor(Model model, @RequestParam(name = KEYWORD, defaultValue = "") String keyword) {
		List<Instructor> instructores = instructorServicio.buscarInstructorPorNombre(keyword != null ? keyword : "");
		model.addAttribute(LIST_INSTRUCTORS, instructores);
		model.addAttribute(KEYWORD, keyword);
		return "instructores-views/instructores";
	}

	@GetMapping(value = "/delete")
	@PreAuthorize("hasAuthority('Admin')")
	public String eliminarInstructor(Long instructorId, String keyword) {
		instructorServicio.removerInstructor(instructorId);
		return "redirect:/instructores/index?keyword=" + keyword;
	}

	@GetMapping(value = "/formUpdate")
	public String actualizarInstructor(Model model, Long instructorId, Principal principal) {
		Instructor instructor = new Instructor();
		if (instructorId != null) {
			instructor = instructorServicio.cargarInstructorPorId(instructorId);
		} else {
			instructor = instructorServicio.cargarInstructorPorEmail(principal.getName());
		}

		model.addAttribute(INSTRUCTOR, instructor);
		return "instructores-views/formActualizar";
	}

	@PostMapping(value = "/update")
	public String actualizar(Instructor instructores) {
		instructorServicio.actualizarInstructor(instructores);
		return "redirect:/cursos/index/instructor";
	}

	@GetMapping(value = "formCreate")
	@PreAuthorize("hasAuthority('Admin')")
	public String formInstructors(Model model) {
		model.addAttribute(INSTRUCTOR, new Instructor());
		return "instructores-views/formCrear";
	}

	@PostMapping(value = "/save")
	@PreAuthorize("hasAuthority('Admin')")
	public String save(@Valid Instructor instructor, BindingResult bindingResult) {
		Usuario usuario = usuarioServicio.cargarUsuarioPorEmail(instructor.getUsuario().getEmail());
		if (usuario != null)
			bindingResult.rejectValue("usuario.email", null, "Hay una cuenta registrado con ese email");
		if (bindingResult.hasErrors())
			return "instructores-views/formCreate";
		instructorServicio.crearInstructor(instructor.getNombres(), instructor.getApellidos(), instructor.getSummary(),
				instructor.getUsuario().getEmail(), instructor.getUsuario().getContraseña());
		return "redirect:/instructores/index";
	}
}
