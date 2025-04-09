package org.exemple.clinica.interfaces;

import org.exemple.clinica.domain.Paciente;
import jakarta.jws.WebMethod;
import jakarta.jws.WebService;
import org.exemple.clinica.dto.PacienteDTO;

import javax.naming.NamingException;
import java.sql.SQLException;
import java.util.List;

@WebService
public interface PacienteService {
    @WebMethod
    void cadastrarPaciente(PacienteDTO pacienteDTO);

    @WebMethod
    List<PacienteDTO> listarPacientes();

    @WebMethod
    void atualizarPaciente(Long id, PacienteDTO pacienteDTO);

    @WebMethod
    void desativarPaciente(Long id);
}
