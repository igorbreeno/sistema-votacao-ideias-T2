package br.com.votacao.service;

import br.com.votacao.dao.IdeiaDAO;
import br.com.votacao.dao.UsuarioDAO;
import br.com.votacao.dao.VotoDAO;
import br.com.votacao.model.Voto;

import java.sql.SQLException;

public class VotoService {
    private final VotoDAO votoDAO = new VotoDAO();
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();
    private final IdeiaDAO ideiaDAO = new IdeiaDAO();

    public void votar(int usuarioId, int ideiaId) throws SQLException {
        if (usuarioId <= 0 || ideiaId <= 0) {
            throw new IllegalArgumentException("IDs devem ser maiores que zero.");
        }
        if (usuarioDAO.buscarPorId(usuarioId) == null) {
            throw new IllegalArgumentException("Usuario nao encontrado.");
        }
        if (ideiaDAO.buscarPorId(ideiaId) == null) {
            throw new IllegalArgumentException("Ideia nao encontrada.");
        }
        votoDAO.inserir(new Voto(0, usuarioId, ideiaId));
    }

    public int contarVotos(int ideiaId) throws SQLException {
        if (ideiaId <= 0) throw new IllegalArgumentException("ID da ideia invalido.");
        return votoDAO.contarPorIdeia(ideiaId);
    }
}
