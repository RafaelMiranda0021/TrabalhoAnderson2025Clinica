package org.exemple.clinica.repositories;

import org.exemple.clinica.domain.Paciente;
import org.exemple.clinica.exception.EntidadeNaoEncontradaException;
import org.exemple.clinica.infrastructure.ConnectionFactory;

import javax.naming.NamingException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class PacienteRepository {
    private static final String INSERT = "INSERT INTO PACIENTE (CPF, NOME, EMAIL, TELEFONE, ATIVO) VALUES (?, ?, ?, ?, ?)";
    private static final String UPDATE = "UPDATE PACIENTE SET NOME = ?, TELEFONE = ?, ATIVO = ? WHERE CPF = ?";
    private static final String DELETE = "UPDATE PACIENTE SET ATIVO = false WHERE CPF = ?";
    private static final String SELECT_ALL = "SELECT CPF, NOME, EMAIL, TELEFONE, ATIVO FROM PACIENTE WHERE ATIVO = true ORDER BY NOME";
    private static final String SELECT_BY_CPF = "SELECT CPF, NOME, EMAIL, TELEFONE, ATIVO FROM PACIENTE WHERE CPF = ?";

    public Paciente salvar(Paciente paciente) throws SQLException {
        try (Connection conn = new ConnectionFactory().getConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT)) {

            stmt.setString(1, paciente.getCpf());
            stmt.setString(2, paciente.getNome());
            stmt.setString(3, paciente.getEmail());
            stmt.setString(4, paciente.getTelefone());
            stmt.setBoolean(5, paciente.isAtivo());

            stmt.executeUpdate();
        } catch (NamingException e) {
            throw new RuntimeException("Erro ao obter conexão com o banco de dados", e);
        }
        return paciente;
    }

    public List<Paciente> listarTodos() throws SQLException {
        List<Paciente> pacientes = new ArrayList<>();

        try (Connection conn = new ConnectionFactory().getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_ALL);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                pacientes.add(mapearPaciente(rs));
            }
        } catch (NamingException e) {
            throw new RuntimeException("Erro ao obter conexão com o banco de dados", e);
        }
        return pacientes;
    }

    public Paciente buscarPorCpf(String cpf) throws SQLException {
        try (Connection conn = new ConnectionFactory().getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_BY_CPF)) {

            stmt.setString(1, cpf);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearPaciente(rs);
                } else {
                    throw new EntidadeNaoEncontradaException("Paciente não encontrado com CPF: " + cpf);
                }
            }
        } catch (NamingException e) {
            throw new RuntimeException("Erro ao obter conexão com o banco de dados", e);
        }
    }

    public void atualizar(String cpf, Paciente paciente) throws SQLException {
        try (Connection conn = new ConnectionFactory().getConnection();
             PreparedStatement stmt = conn.prepareStatement(UPDATE)) {

            stmt.setString(1, paciente.getNome());
            stmt.setString(2, paciente.getTelefone());
            stmt.setBoolean(3, paciente.isAtivo());
            stmt.setString(4, cpf);

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new EntidadeNaoEncontradaException("Paciente não encontrado com CPF: " + cpf);
            }
        } catch (NamingException e) {
            throw new RuntimeException("Erro ao obter conexão com o banco de dados", e);
        }
    }

    public void excluir(String cpf) throws SQLException {
        try (Connection conn = new ConnectionFactory().getConnection();
             PreparedStatement stmt = conn.prepareStatement(DELETE)) {

            stmt.setString(1, cpf);

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new EntidadeNaoEncontradaException("Paciente não encontrado com CPF: " + cpf);
            }
        } catch (NamingException e) {
            throw new RuntimeException("Erro ao obter conexão com o banco de dados", e);
        }
    }

    private Paciente mapearPaciente(ResultSet rs) throws SQLException {
        return null;
    }


    public void atualizar(Paciente paciente) {
    }
}
