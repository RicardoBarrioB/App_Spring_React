package com.gm.rh.controlador;

import org.springframework.web.bind.annotation.RestController;

import com.gm.rh.excepcion.RecursoNoEncontradoExcepcion;
import com.gm.rh.modelo.Empleado;
import com.gm.rh.servicio.EmpleadoServicio;
import com.gm.rh.servicio.IEmpleadoServicio;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;




@RestController
@RequestMapping("rh-app")
@CrossOrigin(value = "http://localhost:3000")
public class EmpleadoControlador {

	private static final Logger logger = LoggerFactory.getLogger(EmpleadoControlador.class);

	@Autowired
	private IEmpleadoServicio empleadoServicio;

	@GetMapping("/empleados")
	public List<Empleado> obtenerEmpleados(){
		List<Empleado> empleados = empleadoServicio.listarEmpleados();
		empleados.forEach((empleado -> logger.info(empleado.toString())));
		return empleados;
	}
	
	@PostMapping("/empleados")
	public Empleado agregarEmpleado(@RequestBody Empleado empleado) {
		logger.info("Empleado a agregar: " + empleado);
		return empleadoServicio.guardarEmpleado(empleado);
	}
	
	@GetMapping("/empleados/{id}")
	public ResponseEntity<Empleado> obtenerEmpleadoPorId(@PathVariable Integer id) {
		Optional<Empleado> empleado = empleadoServicio.buscarEmpleadoporId(id);
		if(empleado.isEmpty())
			throw new RecursoNoEncontradoExcepcion("No se encontro el id: " + id);
		return ResponseEntity.ok(empleado.get());
	}

	@PutMapping("/empleados/{id}")
	public ResponseEntity<Empleado> atualizarEmpleado(@PathVariable Integer id, @RequestBody Empleado empleadoRecibido) {
		Optional<Empleado> empleado = empleadoServicio.buscarEmpleadoporId(id);
		if(empleado.isEmpty())
			throw new RecursoNoEncontradoExcepcion("El id recibido no existe: " + id);
		empleado.get().setNombre(empleadoRecibido.getNombre());
		empleado.get().setDepartamento(empleadoRecibido.getDepartamento());
		empleado.get().setSueldo(empleadoRecibido.getSueldo());
		return ResponseEntity.ok(empleadoServicio.guardarEmpleado(empleado.get()));
	}

	@DeleteMapping("/empleados/{id}")
	public ResponseEntity<Map<String, Boolean>> eliminarEmpleado(@PathVariable Integer id){
		Optional<Empleado> empleado = empleadoServicio.buscarEmpleadoporId(id);
		if (empleado.isEmpty())
			throw new RecursoNoEncontradoExcepcion("El id recibido no existe:" + id);
		empleadoServicio.eliminarEmpleado(empleado.get());
		Map<String, Boolean> respuesta = new HashMap<>();
		respuesta.put("eliminado", Boolean.TRUE);
		return ResponseEntity.ok(respuesta);
	}

}
