package br.com.votacao.service;

import br.com.votacao.dao.IdeiaDAO;
import br.com.votacao.model.Ideia;

import java.sql.SQLException;
import java.util.List;

public class IdeiaService {
    private final IdeiaDAO ideiaDAO = new IdeiaDAO();

    public void salvar(Ideia ideia) throws SQLException {
        validar(ideia);
        if (ideia.getId() == 0) {
            ideiaDAO.inserir(ideia);
        } else {
            if (ideiaDAO.buscarPorId(ideia.getId()) == null) {
                throw new IllegalArgumentException("Ideia nao encontrada.");
            }
            ideiaDAO.atualizar(ideia);
        }
    }

    public List<Ideia> listar() throws SQLException {
        return ideiaDAO.listar();
    }

    public Ideia buscarPorId(int id) throws SQLException {
        return ideiaDAO.buscarPorId(id);
    }

    public void excluir(int id) throws SQLException {
        if (id <= 0) throw new IllegalArgumentException("ID invalido.");
        if (ideiaDAO.buscarPorId(id) == null) throw new IllegalArgumentException("Ideia nao encontrada.");
        ideiaDAO.excluir(id);
    }

    private void validar(Ideia ideia) {
        if (ideia.getTitulo() == null || ideia.getTitulo().isBlank()) {
            throw new IllegalArgumentException("Título é obrigatório.");
        }
        if (ideia.getDescricao() == null || ideia.getDescricao().isBlank()) {
            throw new IllegalArgumentException("Descrição é obrigatória.");
        }
    }
}
