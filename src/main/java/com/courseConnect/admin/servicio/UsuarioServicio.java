package com.courseConnect.admin.servicio;

import com.courseConnect.admin.entidad.Usuario;

public interface UsuarioServicio {
	
	Usuario cargarUsuarioPorEmail(String email);
	
	Usuario crearUsuarios(String email,String password);
	
	void asignarRoleToUsuario(String email,String nombreRole);
}
