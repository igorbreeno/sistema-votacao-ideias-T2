<%@ page import="java.util.List" %>
<%@ page import="br.com.votacao.model.Ideia" %>
<%@ page import="br.com.votacao.model.Usuario" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%
    Ideia ideia = (Ideia) request.getAttribute("ideia");
    List<Usuario> usuarios = (List<Usuario>) request.getAttribute("usuarios");
    boolean editando = ideia != null && ideia.getId() != 0;
%>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <title><%= editando ? "Editar ideia" : "Nova ideia" %></title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
</head>
<body>
<div class="container">
    <h1><%= editando ? "Editar ideia" : "Nova ideia" %></h1>
    <form method="post" action="<%= request.getContextPath() %>/ideias">
        <% if (editando) { %>
            <input type="hidden" name="id" value="<%= ideia.getId() %>">
        <% } %>

        <label>Título</label>
        <input type="text" name="titulo" maxlength="150" required
               value="<%= editando ? ideia.getTitulo() : "" %>">

        <label>Descrição</label>
        <textarea name="descricao" required><%= editando ? ideia.getDescricao() : "" %></textarea>

        <label>Usuário</label>
        <select name="usuarioId">
            <option value="">Sem usuário</option>
            <% for (Usuario usuario : usuarios) {
                boolean selecionado = ideia != null && ideia.getUsuarioId() != null && ideia.getUsuarioId() == usuario.getId();
            %>
                <option value="<%= usuario.getId() %>" <%= selecionado ? "selected" : "" %>><%= usuario.getNome() %></option>
            <% } %>
        </select>

        <button type="submit">Salvar</button>
        <a href="<%= request.getContextPath() %>/ideias">Cancelar</a>
    </form>
</div>
</body>
</html>
