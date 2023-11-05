package com.courseConnect.admin.web;

import java.util.Collection;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import static com.courseConnect.admin.constantes.CourseConnectConstantes.*;

@ControllerAdvice
public class AuthController {

	@ModelAttribute("userName")
	public String getUserName(Model model) {
		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		if (autenticacion != null) {
			authoridadDeAutenticacion(model, autenticacion);
			model.addAttribute(IS_AUTHENTICATION, true);
			return autenticacion.getName();
		}
		return null;
	}

	public void authoridadDeAutenticacion(Model model, Authentication autenticacion) {
		if (autenticacion != null) {
			Collection<? extends GrantedAuthority> authorities = autenticacion.getAuthorities();
			if (authorities.stream().anyMatch(auth -> auth.getAuthority().equals(ADMIN))) {
				model.addAttribute(HAS_AUTHORITY_ADMIN, true);
				model.addAttribute(HAS_AUTHORITY_INSTRUCTOR, false);
			} else if (authorities.stream().anyMatch(auth -> auth.getAuthority().equals(ESTUDIANTE))) {
				model.addAttribute(HAS_AUTHORITY_STUDENT, true);
				model.addAttribute(HAS_AUTHORITY_ADMIN, false);
				model.addAttribute(HAS_AUTHORITY_INSTRUCTOR, false);
			} else if (authorities.stream().anyMatch(auth -> auth.getAuthority().equals(INSTRUCTOR))) {
				model.addAttribute(HAS_AUTHORITY_INSTRUCTOR, true);
				model.addAttribute(HAS_AUTHORITY_ADMIN, false);
			}
		}
	}
}
