package org.exemple.clinica.repositories;

import org.exemple.clinica.domain.Consulta;
import org.exemple.clinica.domain.Medico;
import org.exemple.clinica.domain.MotivoCancelamento;
import org.exemple.clinica.domain.Paciente;
import org.exemple.clinica.exception.EntidadeNaoEncontradaException;
import org.exemple.clinica.infrastructure.ConnectionFactory;

import javax.naming.NamingException;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ConsultaRepository {
    private static final String INSERT = "INSERT INTO CONSULTA (ID, PACIENTE_CPF, MEDICO_CRM, DATA_HORA, CANCELADA, MOTIVO_CANCELAMENTO) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String DELETE = "UPDATE CONSULTA SET CANCELADA = true, MOTIVO_CANCELAMENTO = ? WHERE ID = ?";
    private static final String SELECT_BY_ID = "SELECT ID, PACIENTE_CPF, MEDICO_CRM, DATA_HORA, CANCELADA, MOTIVO_CANCELAMENTO FROM CONSULTA WHERE ID = ?";
    private static final String SELECT_BY_MEDICO_DATA = "SELECT ID, PACIENTE_CPF, MEDICO_CRM, DATA_HORA, CANCELADA, MOTIVO_CANCELAMENTO FROM CONSULTA WHERE MEDICO_CRM = ? AND DATA_HORA = ? AND CANCELADA = false";
    private static final String SELECT_BY_PACIENTE_DATA = "SELECT ID, PACIENTE_CPF, MEDICO_CRM, DATA_HORA, CANCELADA, MOTIVO_CANCELAMENTO FROM CONSULTA WHERE PACIENTE_CPF = ? AND DATE(DATA_HORA) = DATE(?) AND CANCELADA = false";

    private final MedicoRepository medicoRepository;
    private final PacienteRepository pacienteRepository;

    public ConsultaRepository() {
        this.medicoRepository = new MedicoRepository();
        this.pacienteRepository = new PacienteRepository();
    }

    public Consulta salvar(Consulta consulta) throws SQLException {
        try (Connection conn = new ConnectionFactory().getConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT)) {

            stmt.setLong(1, consulta.getId());
            stmt.setString(2, consulta.getPaciente().getCpf());
            stmt.setString(3, consulta.getMedico().getCrm());
            stmt.setTimestamp(4, Timestamp.valueOf(consulta.getDataHora()));
            stmt.setBoolean(5, consulta.isCancelada());
            stmt.setString(6, consulta.getMotivoCancelamento() != null ? consulta.getMotivoCancelamento().name() : null);

            stmt.executeUpdate();
        } catch (NamingException e) {
            throw new RuntimeException("Erro ao obter conexão com o banco de dados", e);
        }
        return consulta;
    }

    public Consulta buscarPorId(Long id) throws SQLException {
        try (Connection conn = new ConnectionFactory().getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_BY_ID)) {

            stmt.setLong(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearConsulta(rs);
                } else {
                    throw new EntidadeNaoEncontradaException("Consulta não encontrada com ID: " + id);
                }
            }
        } catch (NamingException e) {
            throw new RuntimeException("Erro ao obter conexão com o banco de dados", e);
        }
    }

    public List<Consulta> listarPorMedicoEData(String crm, LocalDateTime dataHora) throws SQLException {
        List<Consulta> consultas = new ArrayList<>();

        try (Connection conn = new ConnectionFactory().getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_BY_MEDICO_DATA)) {

            stmt.setString(1, crm);
            stmt.setTimestamp(2, Timestamp.valueOf(dataHora));

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    consultas.add(mapearConsulta(rs));
                }
            }
        } catch (NamingException e) {
            throw new RuntimeException("Erro ao obter conexão com o banco de dados", e);
        }
        return consultas;
    }

    public List<Consulta> listarPorPacienteEData(String cpf, LocalDateTime dataHora) throws SQLException {
        List<Consulta> consultas = new ArrayList<>();

        try (Connection conn = new ConnectionFactory().getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_BY_PACIENTE_DATA)) {

            stmt.setString(1, cpf);
            stmt.setTimestamp(2, Timestamp.valueOf(dataHora));

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    consultas.add(mapearConsulta(rs));
                }
            }
        } catch (NamingException e) {
            throw new RuntimeException("Erro ao obter conexão com o banco de dados", e);
        }
        return consultas;
    }

    public void cancelar(Long id, MotivoCancelamento motivo) throws SQLException {
        try (Connection conn = new ConnectionFactory().getConnection();
             PreparedStatement stmt = conn.prepareStatement(DELETE)) {

            stmt.setString(1, motivo.name());
            stmt.setLong(2, id);

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new EntidadeNaoEncontradaException("Consulta não encontrada com ID: " + id);
            }
        } catch (NamingException e) {
            throw new RuntimeException("Erro ao obter conexão com o banco de dados", e);
        }
    }

    private Consulta mapearConsulta(ResultSet rs) throws SQLException {
        String pacienteCpf = rs.getString("PACIENTE_CPF");
        String medicoCrm = rs.getString("MEDICO_CRM");

        Paciente paciente = pacienteRepository.buscarPorCpf(pacienteCpf);
        Medico medico = medicoRepository.buscarPorid(Long.valueOf(medicoCrm));

        return new Consulta(
                rs.getLong("ID"),
                paciente,
                medico,
                rs.getTimestamp("DATA_HORA").toLocalDateTime(),
                rs.getBoolean("CANCELADA"),
                rs.getString("MOTIVO_CANCELAMENTO") != null ?
                        MotivoCancelamento.valueOf(rs.getString("MOTIVO_CANCELAMENTO")) : null
        );
    }

    public void atualizar(Consulta consulta) {
    }
}
