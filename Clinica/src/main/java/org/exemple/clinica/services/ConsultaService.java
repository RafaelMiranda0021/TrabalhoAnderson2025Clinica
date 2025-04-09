package org.exemple.clinica.services;

import org.exemple.clinica.domain.Consulta;
import org.exemple.clinica.domain.Medico;
import org.exemple.clinica.domain.MotivoCancelamento;
import org.exemple.clinica.domain.Paciente;
import org.exemple.clinica.dto.ConsultaDTO;
import org.exemple.clinica.exception.RegraNegocioException;
import org.exemple.clinica.repositories.ConsultaRepository;
import org.exemple.clinica.repositories.MedicoRepository;
import org.exemple.clinica.repositories.PacienteRepository;
import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class ConsultaService {

    private ConsultaRepository consultaRepository;

    private MedicoRepository medicoRepository;

    private PacienteRepository pacienteRepository;

    public void agendarConsulta(ConsultaDTO consultaDTO) throws SQLException {
        validarAgendamento(consultaDTO);

        Consulta consulta = new Consulta();

        Paciente paciente = pacienteRepository.buscarPorCpf(String.valueOf(Long.valueOf(consultaDTO.getPacienteId())));
        if (paciente == null || !paciente.isAtivo()) {
            throw new RegraNegocioException("Paciente não encontrado ou inativo");
        }
        consulta.setPaciente(paciente);

        Medico medico = null;
        if (consultaDTO.getMedicoId() != 0) {
            medico = medicoRepository.buscarPorid(consultaDTO.getMedicoId());
            if (medico == null || !medico.isAtivo()) {
                throw new RegraNegocioException("Médico não encontrado ou inativo");
            }
        } else{
            medico = escolherMedicoAleatorio(consultaDTO.getDataHora());
            if (medico == null) {
                throw new RegraNegocioException("Não há médicos disponíveis para esta data/hora");
            }
        }
        consulta.setMedico(medico);

        consulta.setDataHora(consultaDTO.getDataHora());

        consultaRepository.salvar(consulta);
    }

    public void cancelarConsulta(Long id, String motivo) throws SQLException {
        Consulta consulta = consultaRepository.buscarPorId(id);

        if (consulta == null) {
            throw new RegraNegocioException("Consulta não encontrada");
        }

        LocalDateTime agora = LocalDateTime.now();
        if (consulta.getDataHora().isBefore(agora.plusHours(24))) {
            throw new RegraNegocioException("Consulta só pode ser cancelada com 24h de antecedência");
        }

        consulta.setCancelada(true);
        consulta.setMotivoCancelamento(MotivoCancelamento.valueOf(motivo));
        consultaRepository.atualizar(consulta);
    }

    private void validarAgendamento(ConsultaDTO consultaDTO) throws SQLException {
        LocalDateTime dataHora = consultaDTO.getDataHora();

        if (dataHora.getDayOfWeek() == DayOfWeek.SUNDAY) {
            throw new RegraNegocioException("Clínica não funciona aos domingos");
        }

        LocalTime hora = dataHora.toLocalTime();
        if (hora.isBefore(LocalTime.of(7, 0)) || hora.isAfter(LocalTime.of(19, 0))) {
            throw new RegraNegocioException("Horário fora do funcionamento da clínica");
        }

        if (dataHora.isBefore(LocalDateTime.now().plusMinutes(30))) {
            throw new RegraNegocioException("Consulta deve ser agendada com 30 minutos de antecedência");
        }

        List<Consulta> consultasPaciente = consultaRepository.listarPorPacienteEData(
                String.valueOf(consultaDTO.getPacienteId()), dataHora);
        if (!consultasPaciente.isEmpty()) {
            throw new RegraNegocioException("Paciente já possui consulta agendada para este dia");
        }

        if (consultaDTO.getMedicoId() != 0) {
            List<Consulta> consultasMedico = consultaRepository.listarPorMedicoEData(
                    String.valueOf(consultaDTO.getMedicoId()), dataHora);
            if (!consultasMedico.isEmpty()) {
                throw new RegraNegocioException("Médico já possui consulta agendada para este horário");
            }
        }
    }

    private Medico escolherMedicoAleatorio(LocalDateTime dataHora) throws SQLException {
        List<Medico> medicosAtivos = medicoRepository.listarTodos();
        List<Medico> medicosDisponiveis = medicosAtivos.stream()
                .filter(medico -> {
                    List<Consulta> consultas = null;
                    try {
                        consultas = consultaRepository.listarPorMedicoEData(
                                String.valueOf(medico.getId()), dataHora);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    return consultas.isEmpty();
                })
                .collect(Collectors.toList());

        if (medicosDisponiveis.isEmpty()) {
            return null;
        }

        Random random = new Random();
        return medicosDisponiveis.get(random.nextInt(medicosDisponiveis.size()));
    }
}
