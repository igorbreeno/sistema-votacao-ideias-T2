<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="br.com.votacao.model.Ideia" %>
<%@ page import="br.com.votacao.model.Usuario" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%
    List<Ideia> ideias = (List<Ideia>) request.getAttribute("ideias");
    List<Usuario> usuarios = (List<Usuario>) request.getAttribute("usuarios");
    Map<Integer, Integer> votos = (Map<Integer, Integer>) request.getAttribute("votos");
%>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <title>Ideias</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
</head>
<body>
<div class="container">
    <h1>Ideias</h1>
    <a href="<%= request.getContextPath() %>/">Início</a>
    <a class="botao" href="<%= request.getContextPath() %>/ideias?acao=novo">Nova ideia</a>

    <table>
        <tr><th>ID</th><th>Título</th><th>Descrição</th><th>Votos</th><th>Ações</th></tr>
        <% for (Ideia ideia : ideias) { %>
            <tr>
                <td><%= ideia.getId() %></td>
                <td><%= ideia.getTitulo() %></td>
                <td><%= ideia.getDescricao() %></td>
                <td><%= votos.getOrDefault(ideia.getId(), 0) %></td>
                <td>
                    <a href="<%= request.getContextPath() %>/ideias?acao=editar&id=<%= ideia.getId() %>">Editar</a>
                    <form method="post" action="<%= request.getContextPath() %>/ideias" onsubmit="return confirm('Excluir esta ideia?')">
                        <input type="hidden" name="acao" value="excluir"><input type="hidden" name="id" value="<%= ideia.getId() %>"><button type="submit">Excluir</button>
                    </form>
                </td>
            </tr>
            <% if (!usuarios.isEmpty()) { %>
                <tr>
                    <td colspan="5">
                        <form class="form-voto" method="post" action="<%= request.getContextPath() %>/votos">
                            <input type="hidden" name="ideiaId" value="<%= ideia.getId() %>">
                            <label>Votar como:</label>
                            <select name="usuarioId" required>
                                <% for (Usuario usuario : usuarios) { %>
                                    <option value="<%= usuario.getId() %>"><%= usuario.getNome() %></option>
                                <% } %>
                            </select>
                            <button type="submit">Votar</button>
                        </form>
                    </td>
                </tr>
            <% } %>
        <% } %>
    </table>
</div>
</body>
</html>
