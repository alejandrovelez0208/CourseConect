package com.courseConnect.admin.web;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import static com.courseConnect.admin.constantes.CourseConnectConstantes.*;

@Controller
public class HomeController {

	@GetMapping("/")
	public String home(Model model) {
		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		if (autenticacion.getName().compareToIgnoreCase("anonymousUser") == 0) {
			model.addAttribute(IS_AUTHENTICATION, false);
			model.addAttribute(IS_ANONYMOUS, true);
		}
		return "home";
	}
}
