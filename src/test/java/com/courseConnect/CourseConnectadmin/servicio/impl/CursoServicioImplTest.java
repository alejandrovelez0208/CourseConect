package com.courseConnect.CourseConnectadmin.servicio.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.courseConnect.admin.dao.CursoDao;
import com.courseConnect.admin.dao.EstudianteDao;
import com.courseConnect.admin.dao.InstructorDao;
import com.courseConnect.admin.entidad.Curso;
import com.courseConnect.admin.entidad.Estudiante;
import com.courseConnect.admin.entidad.Instructor;
import com.courseConnect.admin.servicio.impl.CursoServicioImpl;

import jakarta.persistence.EntityNotFoundException;

@ExtendWith(MockitoExtension.class)
public class CursoServicioImplTest {

	@InjectMocks
	private CursoServicioImpl servicio;

	@Mock
	private CursoDao cursoDao;

	@Mock
	private InstructorDao instructorDao;

	@Mock
	private EstudianteDao estudianteDao;

	private Curso curso;

	private Curso curso2;

	private List<Curso> cursosLst;

	private Instructor instructor;

	private Estudiante estudiante;

	private Long cursoId = 1L;
	private String cursoNombre = "Introduccion a la Programacion";
	private String cursoDuracion = "1h 30min";
	private String cursoDescripcion = "Aprende a programa de forma facil y rapida";
	private Long instructorId = 1L;
	private String keyword = "Master en Angular";
	private Long estudianteId = 2L;

	@BeforeEach
	public void init() {
		estudiante = new Estudiante();
		estudiante.setEstudianteId(2L);

		instructor = new Instructor();
		instructor.setInstructorId(1L);

		cursosLst = new ArrayList<Curso>();
		curso = new Curso();
		curso.setCursoId(1L);
		curso.setCursoNombre("Aprende Html Css y Javascript");
		cursosLst.add(curso);

		curso2 = new Curso();
		curso2.setCursoId(2L);
		curso2.setCursoNombre("Master Office 2023");
		cursosLst.add(curso2);

	}

	@Test
	public void cargarCursoPorId() {
		when(cursoDao.findById(any(Long.class))).thenReturn(Optional.of(curso));

		Curso respuesta = servicio.cargarCursoPorId(cursoId);
		assertNotNull(respuesta);
		assertEquals(1L, respuesta.getCursoId());

		verify(cursoDao, times(1)).findById(any(Long.class));
	}

	@Test
	public void cargarCursoPorIdException() {
		assertThrows(EntityNotFoundException.class, () -> servicio.cargarCursoPorId(35L));
	}

	@Test
	public void crearCurso() {
		when(instructorDao.findById(any(Long.class))).thenReturn(Optional.of(instructor));
		when(cursoDao.save(any())).thenReturn(curso);

		Curso respuesta = servicio.crearCurso(cursoNombre, cursoDuracion, cursoDescripcion, instructorId);
		assertNotNull(respuesta);
		assertEquals(1L, respuesta.getCursoId());

		verify(instructorDao, times(1)).findById(any(Long.class));
		verify(cursoDao, times(1)).save(any());
	}

	@Test
	public void crearCursoException() {
		assertThrows(EntityNotFoundException.class,
				() -> servicio.crearCurso(cursoNombre, cursoDuracion, cursoDescripcion, 5L));
	}

	@Test
	public void crearOrActualizarCurso() {
		curso.setCursoId(10L);

		when(cursoDao.save(any())).thenReturn(curso);

		Curso respuesta = servicio.crearOrActualizarCurso(curso);
		assertNotNull(respuesta);
		assertEquals(10L, respuesta.getCursoId());

		verify(cursoDao, times(1)).save(any());
	}

	@Test
	public void encontrarCursosPorNombre() {
		when(cursoDao.findCursosByCursoNombreContains(anyString())).thenReturn(cursosLst);

		List<Curso> respuesta = servicio.encontrarCursosPorNombre(keyword);
		assertNotNull(respuesta);
		assertEquals(2, respuesta.size());
		assertEquals("Aprende Html Css y Javascript", respuesta.get(0).getCursoNombre());

		verify(cursoDao, times(1)).findCursosByCursoNombreContains(anyString());
	}

	@Test
	public void asignarEstudianteToCurso() {
		when(estudianteDao.findById(any(Long.class))).thenReturn(Optional.of(estudiante));
		when(cursoDao.findById(any(Long.class))).thenReturn(Optional.of(curso));

		servicio.asignarEstudianteToCurso(cursoId, estudianteId);

		verify(estudianteDao, times(1)).findById(any(Long.class));
		verify(cursoDao, times(1)).findById(any(Long.class));
	}

	@Test
	public void asignarEstudianteToCursoExcetion() {
		assertThrows(EntityNotFoundException.class, () -> servicio.asignarEstudianteToCurso(135L, 798L));
	}

	@Test
	public void asignarEstudianteToCursoExceptionCurso() {
		when(estudianteDao.findById(any(Long.class))).thenReturn(Optional.of(estudiante));
		when(cursoDao.findById(any(Long.class))).thenReturn(Optional.empty());

		try {
			servicio.asignarEstudianteToCurso(cursoId, estudianteId);
		} catch (EntityNotFoundException e) {
			assertEquals("Curso con Id 1 No Encontrado", e.getMessage());
		}

		verify(estudianteDao, times(1)).findById(any(Long.class));
		verify(cursoDao, times(1)).findById(any(Long.class));
	}

	@Test
	public void fetchAll() {
		when(cursoDao.findAll()).thenReturn(cursosLst);

		List<Curso> respuesta = servicio.fetchAll();
		assertNotNull(respuesta);
		assertEquals(2, respuesta.size());
		assertEquals("Master Office 2023", respuesta.get(1).getCursoNombre());

		verify(cursoDao, times(1)).findAll();
	}

	@Test
	public void fetchCursosPorEstudiante() {
		when(cursoDao.getCursosByEstudianteId(any(Long.class))).thenReturn(cursosLst);

		List<Curso> respuesta = servicio.fetchCursosPorEstudiante(estudianteId);
		assertNotNull(respuesta);
		assertEquals(2, respuesta.size());
		assertEquals(1L, respuesta.get(0).getCursoId());

		verify(cursoDao, times(1)).getCursosByEstudianteId(any(Long.class));
	}

	@Test
	public void removerCurso() {
		doNothing().when(cursoDao).deleteById(any(Long.class));

		servicio.removerCurso(cursoId);

		verify(cursoDao).deleteById(any(Long.class));
	}
}
