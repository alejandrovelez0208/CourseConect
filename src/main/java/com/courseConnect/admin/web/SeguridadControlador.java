package com.courseConnect.admin.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SeguridadControlador {

	@GetMapping("/403")
	public String notAuthorized() {
		return "403";
	}
}
