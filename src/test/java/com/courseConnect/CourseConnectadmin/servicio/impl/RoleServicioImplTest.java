package com.courseConnect.CourseConnectadmin.servicio.impl;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.courseConnect.admin.dao.RoleDao;
import com.courseConnect.admin.entidad.Role;
import com.courseConnect.admin.servicio.impl.RoleServicioImpl;

@ExtendWith(MockitoExtension.class)
public class RoleServicioImplTest {

	@InjectMocks
	private RoleServicioImpl servicio;

	@Mock
	private RoleDao roleDao;

	private Role role;

	private String roleNombre = "Admin";

	@BeforeEach
	public void init() {

		role = new Role();
		role.setNombre("Admin");
		role.setRoleId(1L);
	}

	@Test
	public void cargarRolePorNombre() {
		when(roleDao.findByNombre(anyString())).thenReturn(role);

		Role respuesta = servicio.cargarRolePorNombre(roleNombre);
		assertNotNull(respuesta);
		assertEquals("Admin", respuesta.getNombre());

		verify(roleDao, times(1)).findByNombre(anyString());
	}

	@Test
	public void crearRole() {
		when(roleDao.save(any())).thenReturn(role);

		Role respuesta = servicio.crearRole(roleNombre);
		assertNotNull(respuesta);
		assertEquals(1L, respuesta.getRoleId());

		verify(roleDao, times(1)).save(any());
	}
}
