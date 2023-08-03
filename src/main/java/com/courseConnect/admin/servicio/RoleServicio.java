package com.courseConnect.admin.servicio;

import com.courseConnect.admin.entidad.Role;

public interface RoleServicio {

	Role cargarRolePorNombre(String roleNombre);

	Role crearRole(String roleNombre);
}
