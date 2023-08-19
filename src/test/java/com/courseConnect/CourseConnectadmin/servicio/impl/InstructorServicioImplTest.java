package com.courseConnect.CourseConnectadmin.servicio.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doNothing;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

import com.courseConnect.admin.dao.InstructorDao;
import com.courseConnect.admin.entidad.Curso;
import com.courseConnect.admin.entidad.Instructor;
import com.courseConnect.admin.entidad.Usuario;
import com.courseConnect.admin.servicio.CursoServicio;
import com.courseConnect.admin.servicio.UsuarioServicio;
import com.courseConnect.admin.servicio.impl.InstructorServicioImpl;

import jakarta.persistence.EntityNotFoundException;

@ExtendWith(MockitoExtension.class)
public class InstructorServicioImplTest {

	@InjectMocks
	private InstructorServicioImpl servicio;

	@Mock
	private InstructorDao instructorDao;

	@Mock
	private UsuarioServicio usuarioServicio;

	@Mock
	private CursoServicio cursoServicio;

	private List<Instructor> instructorLst;

	private Instructor instructor;

	private Usuario usuario;

	private Curso curso;

	private Long instructorId = 1L;
	private String nombre = "Juan";
	private String email = "usuario1@gmail.com";
	private String apellidos = "Hernandez";
	private String summary = "";
	private String password = "652312";

	@BeforeEach
	public void init() {

		usuario = new Usuario();
		usuario.setUsuarioId(2L);
		usuario.setContraseña("1312424");
		usuario.setEmail("usuario1@gmail.com");

		instructorLst = new ArrayList<>();
		instructor = new Instructor();
		instructor.setInstructorId(1L);
		instructor.setNombres("Carlos");
		instructor.setUsuario(usuario);
		instructorLst.add(instructor);

		curso = new Curso();
		curso.setCursoId(10L);
		curso.setInstructor(instructor);
		curso.setCursoNombre("Excel");
	}

	@Test
	public void cargarInstructorPorId() {
		when(instructorDao.findById(any(Long.class))).thenReturn(Optional.of(instructor));

		Instructor respuesta = servicio.cargarInstructorPorId(instructorId);
		assertNotNull(respuesta);
		assertEquals(1L, respuesta.getInstructorId());

		verify(instructorDao, times(1)).findById(any(Long.class));
	}

	@Test
	public void cargarInstructorPorIdException() {
		assertThrows(EntityNotFoundException.class, () -> servicio.cargarInstructorPorId(2L));
	}

	@Test
	public void buscarInstructorPorNombre() {
		when(instructorDao.findInstructoresByNombre(anyString())).thenReturn(instructorLst);

		List<Instructor> respuesta = servicio.buscarInstructorPorNombre(nombre);
		assertNotNull(respuesta);
		assertEquals(1, respuesta.size());
		assertEquals("Carlos", respuesta.get(0).getNombres());

		verify(instructorDao, times(1)).findInstructoresByNombre(anyString());
	}

	@Test
	public void cargarInstructorPorEmail() {
		when(instructorDao.findInstructorByEmail(anyString())).thenReturn(instructor);

		Instructor respuesta = servicio.cargarInstructorPorEmail(email);
		assertNotNull(respuesta);
		assertEquals("usuario1@gmail.com", respuesta.getUsuario().getEmail());

		verify(instructorDao, times(1)).findInstructorByEmail(anyString());
	}

	@Test
	public void crearInstructor() {
		when(usuarioServicio.crearUsuarios(anyString(), anyString())).thenReturn(usuario);
		doNothing().when(usuarioServicio).asignarRoleToUsuario(anyString(), anyString());
		when(instructorDao.save(any())).thenReturn(instructor);

		Instructor respuesta = servicio.crearInstructor(nombre, apellidos, email, summary, password);
		assertNotNull(respuesta);
		assertEquals(2L, respuesta.getUsuario().getUsuarioId());
		assertEquals("1312424", respuesta.getUsuario().getContraseña());
		assertEquals("Carlos", respuesta.getNombres());

		verify(usuarioServicio, times(1)).crearUsuarios(anyString(), anyString());
		verify(usuarioServicio).asignarRoleToUsuario(anyString(), anyString());
		verify(instructorDao, times(1)).save(any());
	}

	@Test
	public void actualizarInstructor() {
		instructor.setNombres("Gabriel");
		when(instructorDao.save(any())).thenReturn(instructor);

		Instructor respuesta = servicio.actualizarInstructor(instructor);
		assertEquals("Gabriel", respuesta.getNombres());

		verify(instructorDao, times(1)).save(any());
	}

	@Test
	public void fetchInstructor() {
		when(instructorDao.findAll()).thenReturn(instructorLst);

		List<Instructor> respuesta = servicio.fetchInstructor();
		assertNotNull(respuesta);
		assertEquals(1, respuesta.size());
		assertEquals("Carlos", respuesta.get(0).getNombres());
		assertEquals("usuario1@gmail.com", respuesta.get(0).getUsuario().getEmail());

		verify(instructorDao, times(1)).findAll();
	}

	@Test
	public void removerInstructor() {
		instructor.setInstructorId(10L);
		instructor.getCurso().add(curso);

		when(instructorDao.findById(any(Long.class))).thenReturn(Optional.of(instructor));
		doNothing().when(cursoServicio).removerCurso(any(Long.class));
		doNothing().when(instructorDao).deleteById(any(Long.class));

		servicio.removerInstructor(10L);

		verify(instructorDao, times(1)).findById(any(Long.class));
		verify(cursoServicio).removerCurso(any(Long.class));
		verify(instructorDao).deleteById(any(Long.class));
	}
}
