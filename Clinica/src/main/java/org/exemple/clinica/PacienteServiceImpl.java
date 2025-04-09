package org.exemple.clinica;

import jakarta.jws.WebMethod;
import org.exemple.clinica.dto.PacienteDTO;
import org.exemple.clinica.interfaces.PacienteService;
import jakarta.jws.WebService;
import java.util.List;

@WebService
public class PacienteServiceImpl implements PacienteService {
    private PacienteService pacienteService;

    @Override
    @WebMethod
    public void cadastrarPaciente(PacienteDTO pacienteDTO) {
        pacienteService.cadastrarPaciente(pacienteDTO);
    }

    @Override
    @WebMethod
    public List<PacienteDTO> listarPacientes() {
        return pacienteService.listarPacientes();
    }

    @Override
    @WebMethod
    public void atualizarPaciente(Long id, PacienteDTO pacienteDTO) {
        pacienteService.atualizarPaciente(id, pacienteDTO);
    }

    @Override
    @WebMethod
    public void desativarPaciente(Long id) {
        pacienteService.desativarPaciente(id);
    }
}
