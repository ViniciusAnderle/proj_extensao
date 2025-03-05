package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class FirebirdConnection {
    public static void main(String[] args) {
        // URL de conexão com o banco
        String url = "jdbc:firebirdsql://localhost:3050/" + System.getProperty("user.home") + "/sol.fdb";
        // Credenciais de acesso
        String user = "SYSDBA";
        String password = "masterkey";

        try {
            // Estabelece a conexão
            Connection connection = DriverManager.getConnection(url, user, password);

            // Se a conexão for bem-sucedida
            System.out.println("Conectado ao banco Firebird com sucesso!");

            // Feche a conexão quando não for mais necessária
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
