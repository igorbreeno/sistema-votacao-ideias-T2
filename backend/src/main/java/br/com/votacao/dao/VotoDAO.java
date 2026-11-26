package br.com.votacao.dao;

import br.com.votacao.model.Voto;
import br.com.votacao.util.ConnectionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class VotoDAO {

    public void inserir(Voto voto) throws SQLException {
        String sql = "INSERT INTO votos (usuario_id, ideia_id) VALUES (?, ?)";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, voto.getUsuarioId());
            ps.setInt(2, voto.getIdeiaId());
            ps.executeUpdate();
        }
    }

    public int contarPorIdeia(int ideiaId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM votos WHERE ideia_id = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, ideiaId);
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getInt(1);
            }
        }
    }
}
