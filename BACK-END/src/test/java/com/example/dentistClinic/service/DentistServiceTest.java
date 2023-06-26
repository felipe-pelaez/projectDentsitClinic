package com.example.dentistClinic.service;

import com.example.dentistClinic.exceptions.BadRequestException;
import com.example.dentistClinic.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
class DentistServiceTest {

    @Autowired
    private DentistService odontologoService;

    @Test
    @Order(1)
    public void guardarOdontologoTest() throws BadRequestException {
        Dentist odontologoAGuardar = new Dentist();
        odontologoAGuardar.setLastName("Gómez");
        odontologoAGuardar.setName("María");
        odontologoAGuardar.setTuition("1234");

        Dentist odontologoGuardado = odontologoService.altaOdontologo(odontologoAGuardar);
        assertNotNull(odontologoGuardado.getId());
    }

    @Test
    @Order(2)
    public void buscarOdontologoPorIdTest() throws ResourceNotFoundException {
        Long idABuscar = 1L;
        Optional<Dentist> odontologoBuscado = odontologoService.buscarOdontologo(idABuscar);
        assertNotNull(odontologoBuscado.get());
    }

    @Test
    @Order(3)
    public void actualizarOdontologoTest() throws ResourceNotFoundException, BadRequestException {
        Long idAActualizar = 1L;
        Optional<Dentist> odontologoBuscado = odontologoService.buscarOdontologo(idAActualizar);
        if (odontologoBuscado.isPresent()) {
            Dentist odontologoAActualizar = new Dentist();
            odontologoAActualizar.setId(idAActualizar);
            odontologoAActualizar.setLastName("Gómez");
            odontologoAActualizar.setName("María Actualizada");
            odontologoAActualizar.setTuition("1234");

            odontologoService.actualizarOdontologo(odontologoAActualizar);
        }
        Optional<Dentist> odontologoBuscadoDespuesDeActualizar = odontologoService.buscarOdontologo(idAActualizar);
        if (odontologoBuscadoDespuesDeActualizar.isPresent()) {
            assertEquals("María Actualizada", odontologoBuscadoDespuesDeActualizar.get().getName());
        }
    }

    @Test
    @Order(4)
    public void eliminarOdontologoTest() throws ResourceNotFoundException {
        Long idAEliminar = 1L;
        odontologoService.eliminarOdontologo(idAEliminar);

        assertThrows(ResourceNotFoundException.class, () -> {
            odontologoService.buscarOdontologo(idAEliminar).orElseThrow(() -> new ResourceNotFoundException(""));
        });
    }
}