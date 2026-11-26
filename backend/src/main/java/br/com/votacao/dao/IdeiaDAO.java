package br.com.votacao.dao;

import br.com.votacao.model.Ideia;
import br.com.votacao.util.ConnectionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class IdeiaDAO {

    public void inserir(Ideia ideia) throws SQLException {
        String sql = "INSERT INTO ideias (titulo, descricao, usuario_id) VALUES (?, ?, ?)";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            preencherDados(ps, ideia);
            ps.executeUpdate();
        }
    }

    public List<Ideia> listar() throws SQLException {
        List<Ideia> ideias = new ArrayList<>();
        String sql = "SELECT id, titulo, descricao, usuario_id FROM ideias ORDER BY id DESC";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                ideias.add(mapear(rs));
            }
        }
        return ideias;
    }

    public Ideia buscarPorId(int id) throws SQLException {
        String sql = "SELECT id, titulo, descricao, usuario_id FROM ideias WHERE id = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapear(rs) : null;
            }
        }
    }

    public void atualizar(Ideia ideia) throws SQLException {
        String sql = "UPDATE ideias SET titulo = ?, descricao = ?, usuario_id = ? WHERE id = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            preencherDados(ps, ideia);
            ps.setInt(4, ideia.getId());
            ps.executeUpdate();
        }
    }

    public void excluir(int id) throws SQLException {
        try (Connection conn = ConnectionFactory.getConnection()) {
            conn.setAutoCommit(false);
            try {
                try (PreparedStatement psVotos = conn.prepareStatement("DELETE FROM votos WHERE ideia_id = ?")) {
                    psVotos.setInt(1, id);
                    psVotos.executeUpdate();
                }
                try (PreparedStatement psIdeia = conn.prepareStatement("DELETE FROM ideias WHERE id = ?")) {
                    psIdeia.setInt(1, id);
                    psIdeia.executeUpdate();
                }
                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        }
    }

    private void preencherDados(PreparedStatement ps, Ideia ideia) throws SQLException {
        ps.setString(1, ideia.getTitulo());
        ps.setString(2, ideia.getDescricao());
        if (ideia.getUsuarioId() == null) {
            ps.setNull(3, Types.INTEGER);
        } else {
            ps.setInt(3, ideia.getUsuarioId());
        }
    }

    private Ideia mapear(ResultSet rs) throws SQLException {
        int usuarioId = rs.getInt("usuario_id");
        Integer valorUsuarioId = rs.wasNull() ? null : usuarioId;
        return new Ideia(
                rs.getInt("id"),
                rs.getString("titulo"),
                rs.getString("descricao"),
                valorUsuarioId
        );
    }
}
