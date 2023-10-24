package com.courseConnect.admin.web;

import static com.courseConnect.admin.constantes.CourseConnectConstantes.KEYWORD;
import static com.courseConnect.admin.constantes.CourseConnectConstantes.LIST_ESTUDIANTES;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.courseConnect.admin.entidad.Estudiante;
import com.courseConnect.admin.entidad.Usuario;
import com.courseConnect.admin.servicio.EstudianteServicio;
import com.courseConnect.admin.servicio.UsuarioServicio;

@Controller
@RequestMapping("estudiantes")
public class EstudianteControlador {

	@Autowired
	private EstudianteServicio estudianteServicio;

	@Autowired
	private UsuarioServicio usuarioServicio;

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

	@GetMapping(value = "/delete")
	public String eliminarEstudiante(Long estudianteId, String keyword) {
		estudianteServicio.removerEstudiante(estudianteId);
		return "redirect:/estudiantes/index?keyword=" + keyword;
	}

	@GetMapping(value = "/formUpdate")
	public String actualizarEstudiante(Model model, Long estudianteId) {
		Estudiante estudiante = estudianteServicio.cargarEstudiantePorId(estudianteId);
		model.addAttribute("estudiante", estudiante);
		return "estudiante-views/formUpdate";
	}

	@PostMapping(value = "/update")
	public String actualizarEstudiante(Estudiante estudiante) {
		estudianteServicio.actualizarEstudiante(estudiante);
		return "redirect:/estudiantes/index";
	}

	@GetMapping(value = "/formCreate")
	public String formEstudiante(Model model) {
		model.addAttribute("estudiante", new Estudiante());
		return "estudiante-views/formCreate";
	}

	@PostMapping(value = "/save")
	public String save(Estudiante estudiante, BindingResult bindingResult) {
		Usuario usuario = usuarioServicio.cargarUsuarioPorEmail(estudiante.getUsuario().getEmail());
		if (usuario != null)
			bindingResult.rejectValue("usuario.email", null, "Hay una cuenta registrado con ese email");
		if (bindingResult.hasErrors())
			return "estudiante-views/formCreate";
		estudianteServicio.crearEstudiante(estudiante.getNombres(), estudiante.getApellidos(), estudiante.getNivel(),
				estudiante.getUsuario().getEmail(), estudiante.getUsuario().getContraseña());
		return "redirect:/estudiantes/index";
	}
}
