package br.com.votacao.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class ConnectionFactory {
    private ConnectionFactory() {}

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new ExceptionInInitializerError("Driver MySQL nao encontrado: " + e.getMessage());
        }
    }

    public static Connection getConnection() throws SQLException {
        String url = env("DB_URL", "jdbc:mysql://localhost:3306/sistema_votacao_ideias?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=America/Sao_Paulo");
        String user = env("DB_USER", "root");
        String password = env("DB_PASSWORD", "");
        return DriverManager.getConnection(url, user, password);
    }

    private static String env(String name, String fallback) {
        String value = System.getenv(name);
        return value == null || value.isBlank() ? fallback : value;
    }
}
