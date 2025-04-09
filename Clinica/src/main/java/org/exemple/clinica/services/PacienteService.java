package org.exemple.clinica.services;

import org.exemple.clinica.domain.Paciente;
import jakarta.jws.WebMethod;
import org.exemple.clinica.PacienteServiceImpl;
import org.exemple.clinica.dto.PacienteDTO;
import org.exemple.clinica.exception.RegraNegocioException;
import org.exemple.clinica.repositories.PacienteRepository;

import javax.naming.NamingException;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class PacienteService {

    private PacienteRepository pacienteRepository;

    public void cadastrarPaciente(PacienteDTO pacienteDTO) throws SQLException {
        validarCamposObrigatorios(pacienteDTO);

        Paciente paciente = new Paciente();

        pacienteRepository.salvar(paciente);
    }

    public List<PacienteDTO> listarPacientes() throws SQLException {
        return pacienteRepository.listarTodos().stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
    }

    public void atualizarPaciente(String cpf, PacienteDTO pacienteDTO) throws SQLException {
        Paciente paciente = pacienteRepository.buscarPorCpf(cpf);

        if (paciente == null) {
            throw new RegraNegocioException("Paciente não encontrado");
        }

        // Atualizar apenas campos permitidos
        paciente.setNome(pacienteDTO.getNome());
        paciente.setTelefone(pacienteDTO.getTelefone());
        // Atualizar endereço

        pacienteRepository.atualizar(paciente);
    }

    public void desativarPaciente(String cpf) throws RegraNegocioException, SQLException {
        Paciente paciente = pacienteRepository.buscarPorCpf(cpf);

        if (paciente == null) {
            throw new RegraNegocioException("Paciente não encontrado");
        }

        paciente.setAtivo(false);
        pacienteRepository.atualizar(paciente);
    }

    private void validarCamposObrigatorios(PacienteDTO pacienteDTO) {
    }

    private PacienteDTO converterParaDTO(Paciente paciente) {
        PacienteDTO dto = new PacienteDTO();

        return dto;
    }
}
