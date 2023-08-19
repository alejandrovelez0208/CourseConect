package com.courseConnect.CourseConnectadmin.servicio.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.courseConnect.admin.dao.EstudianteDao;
import com.courseConnect.admin.entidad.Curso;
import com.courseConnect.admin.entidad.Estudiante;
import com.courseConnect.admin.entidad.Usuario;
import com.courseConnect.admin.servicio.UsuarioServicio;
import com.courseConnect.admin.servicio.impl.EstudianteServicioImpl;

import jakarta.persistence.EntityNotFoundException;

@ExtendWith(MockitoExtension.class)
public class EstudianteServicioImplTest {

	@InjectMocks
	private EstudianteServicioImpl servicio;

	@Mock
	private EstudianteDao estudianteDao;

	@Mock
	private UsuarioServicio usuarioServicio;

	private Estudiante estudiante;

	private List<Estudiante> estudianteLst;

	private Usuario usuario;

	private Curso curso;

	private Long estudianteId = 1L;
	private String nombre = "Samuel";
	private String email = "estudiante1@gmail.com";
	private String apellidos = "Valdez";
	private String nivel = "Junior";
	private String password = "65646";

	@BeforeEach
	public void init() {
		curso = new Curso();
		curso.setCursoId(1L);
		curso.setCursoNombre("Excel");

		estudianteLst = new ArrayList<>();
		estudiante = new Estudiante();
		estudiante.setEstudianteId(1L);
		estudiante.setNombres("Juan");
		estudianteLst.add(estudiante);

		usuario = new Usuario();
		usuario.setUsuarioId(2L);
		usuario.setEstudiante(estudiante);

	}

	@Test
	public void cargarEstudiantePorId() {
		when(estudianteDao.findById(anyLong())).thenReturn(Optional.of(estudiante));

		Estudiante respuesta = servicio.cargarEstudiantePorId(estudianteId);
		assertNotNull(respuesta);

		verify(estudianteDao, times(1)).findById(anyLong());
	}

	@Test
	public void cargarEstudiantePorIdException() {
		assertThrows(EntityNotFoundException.class, () -> servicio.cargarEstudiantePorId(2L));
	}

	@Test
	public void cargarEstudiantePorNombre() {
		when(estudianteDao.findEstudiantesByNombre(anyString())).thenReturn(estudianteLst);

		List<Estudiante> respuesta = servicio.cargarEstudiantePorNombre(nombre);
		assertNotNull(respuesta);
		assertEquals(1, respuesta.size());

		verify(estudianteDao, times(1)).findEstudiantesByNombre(anyString());
	}

	@Test
	public void cargarEstudiantePorEmail() {
		when(estudianteDao.findEstudianteByEmail(anyString())).thenReturn(estudiante);

		Estudiante respuesta = servicio.cargarEstudiantePorEmail(email);
		assertNotNull(respuesta);
		assertEquals("Juan", respuesta.getNombres());
		assertEquals(1L, respuesta.getEstudianteId());

		verify(estudianteDao, times(1)).findEstudianteByEmail(anyString());
	}

	@Test
	public void crearEstudiante() {
		when(usuarioServicio.crearUsuarios(anyString(), anyString())).thenReturn(usuario);
		when(estudianteDao.save(any())).thenReturn(estudiante);

		Estudiante respuesta = servicio.crearEstudiante(nombre, apellidos, nivel, email, password);
		assertNotNull(respuesta);
		assertEquals(1L, respuesta.getEstudianteId());

		verify(usuarioServicio, times(1)).crearUsuarios(anyString(), anyString());
		verify(estudianteDao, times(1)).save(any());
	}

	@Test
	public void fetchEstudiantes() {
		when(estudianteDao.findAll()).thenReturn(estudianteLst);

		List<Estudiante> respuesta = servicio.fetchEstudiantes();
		assertNotNull(respuesta);
		assertEquals(1, respuesta.size());
		assertEquals("Juan", respuesta.get(0).getNombres());

		verify(estudianteDao, times(1)).findAll();
	}

	@Test
	public void actualizarEstudiante() {
		estudiante.setNombres("Sara");
		when(estudianteDao.save(any())).thenReturn(estudiante);

		servicio.actualizarEstudiante(estudiante);

		verify(estudianteDao).save(any());
	}

	@Test
	public void removerEstudiante() {
		estudiante.getCurso().add(curso);
		when(estudianteDao.findById(any())).thenReturn(Optional.of(estudiante));

		servicio.removerEstudiante(estudianteId);

		verify(estudianteDao).deleteById(any());
	}

	@Test
	public void removerEstudianteNoData() {
		when(estudianteDao.findById(any())).thenReturn(Optional.of(estudiante));

		servicio.removerEstudiante(estudianteId);

		verify(estudianteDao).deleteById(any());
	}
}
