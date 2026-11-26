<%@ page import="java.util.List" %>
<%@ page import="br.com.votacao.model.Usuario" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%
    List<Usuario> usuarios = (List<Usuario>) request.getAttribute("usuarios");
    String erro = (String) request.getAttribute("erro");
%>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <title>Usuários</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
</head>
<body>
<div class="container">
    <h1>Usuários</h1>
    <a href="<%= request.getContextPath() %>/">Início</a>

    <% if (erro != null) { %>
        <p class="erro"><%= erro %></p>
    <% } %>

    <h2>Cadastrar usuário</h2>
    <form method="post" action="<%= request.getContextPath() %>/usuarios">
        <label>Nome</label>
        <input type="text" name="nome" required>
        <label>E-mail</label>
        <input type="email" name="email" required>
        <label>Senha</label>
        <input type="password" name="senha" required>
        <button type="submit">Cadastrar</button>
    </form>

    <h2>Usuários cadastrados</h2>
    <table>
        <tr><th>ID</th><th>Nome</th><th>E-mail</th></tr>
        <% for (Usuario usuario : usuarios) { %>
            <tr>
                <td><%= usuario.getId() %></td>
                <td><%= usuario.getNome() %></td>
                <td><%= usuario.getEmail() %></td>
            </tr>
        <% } %>
    </table>
</div>
</body>
</html>
