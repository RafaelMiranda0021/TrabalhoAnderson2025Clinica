package org.exemple.clinica.repositories;

import org.exemple.clinica.domain.Especialidade;
import org.exemple.clinica.domain.Medico;
import org.exemple.clinica.exception.EntidadeNaoEncontradaException;
import org.exemple.clinica.infrastructure.ConnectionFactory;
import javax.naming.NamingException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MedicoRepository {
    private static final String INSERT = "INSERT INTO MEDICO (ID, CRM, NOME, EMAIL, TELEFONE, ESPECIALIDADE, ATIVO) VALUES (?, ?, ?, ?, ?, ?, ?)";
    private static final String UPDATE = "UPDATE MEDICO SET NOME = ?, TELEFONE = ?, ATIVO = ? WHERE CRM = ?";
    private static final String DELETE = "UPDATE MEDICO SET ATIVO = false WHERE CRM = ?";
    private static final String SELECT_ALL = "SELECT CRM, NOME, EMAIL, TELEFONE, ESPECIALIDADE, ATIVO FROM MEDICO WHERE ATIVO = true ORDER BY NOME";
    private static final String SELECT_BY_ID = "SELECT ID,CRM, NOME, EMAIL, TELEFONE, ESPECIALIDADE, ATIVO FROM MEDICO WHERE CRM = ?";
    private static final String SELECT_BY_ESPECIALIDADE = "SELECT CRM, NOME, EMAIL, TELEFONE, ESPECIALIDADE, ATIVO FROM MEDICO WHERE ESPECIALIDADE = ? AND ATIVO = true";

    public Medico salvar(Medico medico) throws SQLException {
        try (Connection conn = new ConnectionFactory().getConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT)) {

            stmt.setLong(1, medico.getId());
            stmt.setString(2, medico.getCrm());
            stmt.setString(3, medico.getNome());
            stmt.setString(4, medico.getEmail());
            stmt.setString(5, medico.getTelefone());
            stmt.setString(6, medico.getEspecialidade().name());
            stmt.setBoolean(7, medico.isAtivo());

            stmt.executeUpdate();
        } catch (NamingException e) {
            throw new RuntimeException("Erro ao obter conexão com o banco de dados", e);
        }
        return medico;
    }

    public List<Medico> listarTodos() throws SQLException {
        List<Medico> medicos = new ArrayList<>();

        try (Connection conn = new ConnectionFactory().getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_ALL);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                medicos.add(mapearMedico(rs));
            }
        } catch (NamingException e) {
            throw new RuntimeException("Erro ao obter conexão com o banco de dados", e);
        }
        return medicos;
    }

    public Medico buscarPorid(Long id) throws SQLException {
        try (Connection conn = new ConnectionFactory().getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_BY_ID)) {

            stmt.setString(1, String.valueOf(id));

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearMedico(rs);
                } else {
                    throw new EntidadeNaoEncontradaException("Médico não encontrado : " + id);
                }
            }
        } catch (NamingException e) {
            throw new RuntimeException("Erro ao obter conexão com o banco de dados", e);
        }
    }

    public List<Medico> buscarPorEspecialidade(Especialidade especialidade) throws SQLException {
        List<Medico> medicos = new ArrayList<>();

        try (Connection conn = new ConnectionFactory().getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_BY_ESPECIALIDADE)) {

            stmt.setString(1, especialidade.name());

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    medicos.add(mapearMedico(rs));
                }
            }
        } catch (NamingException e) {
            throw new RuntimeException("Erro ao obter conexão com o banco de dados", e);
        }
        return medicos;
    }

    public void atualizar(String crm, Medico medico) throws SQLException {
        try (Connection conn = new ConnectionFactory().getConnection();
             PreparedStatement stmt = conn.prepareStatement(UPDATE)) {

            stmt.setString(1, medico.getNome());
            stmt.setString(2, medico.getTelefone());
            stmt.setBoolean(3, medico.isAtivo());
            stmt.setString(4, crm);

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new EntidadeNaoEncontradaException("Médico não encontrado com CRM: " + crm);
            }
        } catch (NamingException e) {
            throw new RuntimeException("Erro ao obter conexão com o banco de dados", e);
        }
    }

    public void excluir(String crm) throws SQLException {
        try (Connection conn = new ConnectionFactory().getConnection();
             PreparedStatement stmt = conn.prepareStatement(DELETE)) {

            stmt.setString(1, crm);

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new EntidadeNaoEncontradaException("Médico não encontrado com CRM: " + crm);
            }
        } catch (NamingException e) {
            throw new RuntimeException("Erro ao obter conexão com o banco de dados", e);
        }
    }

    private Medico mapearMedico(ResultSet rs) throws SQLException {
        return new Medico(
                rs.getString("CRM"),
                rs.getString("NOME"),
                rs.getString("EMAIL"),
                rs.getString("TELEFONE"),
                Especialidade.valueOf(rs.getString("ESPECIALIDADE")),
                rs.getBoolean("ATIVO")
        );
    }

    public void atualizar(Medico medico) {
    }


}
