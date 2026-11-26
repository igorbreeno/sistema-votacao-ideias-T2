package br.com.votacao.service;

import br.com.votacao.dao.UsuarioDAO;
import br.com.votacao.model.Usuario;

import java.sql.SQLException;
import java.util.List;

public class UsuarioService {
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    public void cadastrar(Usuario usuario) throws SQLException {
        validar(usuario, true);
        usuario.setSenha(br.com.votacao.util.PasswordHash.hash(usuario.getSenha()));
        usuarioDAO.inserir(usuario);
    }

    public List<Usuario> listar() throws SQLException {
        return usuarioDAO.listar();
    }

    public Usuario buscarPorId(int id) throws SQLException {
        if (id <= 0) throw new IllegalArgumentException("ID deve ser maior que zero.");
        return usuarioDAO.buscarPorId(id);
    }

    public void atualizar(Usuario usuario) throws SQLException {
        validar(usuario, false);
        if (usuario.getId() <= 0) throw new IllegalArgumentException("ID invalido.");
        if (usuarioDAO.buscarPorId(usuario.getId()) == null) {
            throw new IllegalArgumentException("Usuario nao encontrado.");
        }
        if (usuario.getSenha() != null && !usuario.getSenha().isBlank()) usuario.setSenha(br.com.votacao.util.PasswordHash.hash(usuario.getSenha()));
        usuarioDAO.atualizar(usuario);
    }

    public void excluir(int id) throws SQLException {
        if (id <= 0) throw new IllegalArgumentException("ID invalido.");
        if (usuarioDAO.buscarPorId(id) == null) {
            throw new IllegalArgumentException("Usuario nao encontrado.");
        }
        usuarioDAO.excluir(id);
    }

    private void validar(Usuario usuario, boolean exigirSenha) {
        if (usuario == null) throw new IllegalArgumentException("Dados do usuario sao obrigatorios.");
        if (usuario.getNome() == null || usuario.getNome().isBlank()) {
            throw new IllegalArgumentException("Nome e obrigatorio.");
        }
        if (usuario.getEmail() == null || usuario.getEmail().isBlank() || !usuario.getEmail().contains("@")) {
            throw new IllegalArgumentException("Informe um e-mail valido.");
        }
        if (exigirSenha && (usuario.getSenha() == null || usuario.getSenha().isBlank())) {
            throw new IllegalArgumentException("Senha e obrigatoria.");
        }
    }
}
