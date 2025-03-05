package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class FirebirdConnection {
    public static void main(String[] args) {
        String url = "jdbc:firebirdsql://localhost:3050/" + System.getProperty("user.home") + "/sol.fdb";
        String user = "SYSDBA";
        String password = "masterkey";

        try (Connection connection = DriverManager.getConnection(url, user, password);
             Statement statement = connection.createStatement()) {

            System.out.println("Conectado ao banco Firebird com sucesso!");

            String sql = "SELECT ID_PRODUTO, DESCRICAO, PERC_IPI FROM TPRODUTOS;";

            try (ResultSet resultSet = statement.executeQuery(sql)) {
                System.out.println("Dados da tabela TPRODUTOS:");
                while (resultSet.next()) {
                    int idProduto = resultSet.getInt("ID_PRODUTO");
                    String descricao = resultSet.getString("DESCRICAO");
                    double percIpi = resultSet.getDouble("PERC_IPI");

                    System.out.println("ID_PRODUTO: " + idProduto + ", DESCRICAO: " + descricao + ", PERC_IPI: " + percIpi);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
