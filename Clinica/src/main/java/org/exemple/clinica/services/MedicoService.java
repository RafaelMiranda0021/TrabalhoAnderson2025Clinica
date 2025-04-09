package org.exemple.clinica.services;

import org.exemple.clinica.domain.Medico;
import org.exemple.clinica.dto.MedicoDTO;
import org.exemple.clinica.exception.RegraNegocioException;
import org.exemple.clinica.repositories.MedicoRepository;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;


public class MedicoService {

    private MedicoRepository medicoRepository;

    public void cadastrarMedico(MedicoDTO medicoDTO) throws SQLException {
        validarCamposObrigatorios(medicoDTO);

        Medico medico = new Medico();

        medicoRepository.salvar(medico);
    }

    public List<MedicoDTO> listarMedicos() throws SQLException {
        return medicoRepository.listarTodos().stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
    }

    public void atualizarMedico(Long id, MedicoDTO medicoDTO) throws SQLException {
        Medico medico = medicoRepository.buscarPorid(id);

        if (medico == null) {
            throw new RegraNegocioException("Médico não encontrado");
        }

        medico.setNome(medicoDTO.getNome());
        medico.setTelefone(medicoDTO.getTelefone());


        medicoRepository.atualizar(medico);
    }

    public void desativarMedico(Long id) throws SQLException {
        Medico medico = medicoRepository.buscarPorid(id);

        if (medico == null) {
            throw new RegraNegocioException("Médico não encontrado");
        }

        medico.setAtivo(false);
        medicoRepository.atualizar(medico);
    }

    private void validarCamposObrigatorios(MedicoDTO medicoDTO) {

    }

    private MedicoDTO converterParaDTO(Medico medico) {
        MedicoDTO dto = new MedicoDTO();

        return dto;
    }
}
