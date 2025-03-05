package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class FirebirdConnection {

    // URL de conexão com o banco Firebird
    private static final String URL = "jdbc:firebirdsql://localhost:3050/" + System.getProperty("user.home") + "/sol.fdb";
    // Credenciais de acesso ao banco Firebird
    private static final String USER = "SYSDBA";
    private static final String PASSWORD = "masterkey";

    public static void main(String[] args) {
        // Conecta ao banco e realiza a consulta
        try (Connection connection = createConnection()) {
            if (connection != null) {
                System.out.println("Conectado ao banco Firebird com sucesso!");
                String sql = buildQuery();
                executeQuery(connection, sql);
            } else {
                System.out.println("Falha ao conectar ao banco de dados.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Método para criar a conexão com o banco de dados
    private static Connection createConnection() {
        try {
            // Verifica se o driver Firebird está carregado
            Class.forName("org.firebirdsql.jdbc.FBDriver");
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            System.err.println("Driver do Firebird não encontrado: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Erro ao tentar conectar ao banco de dados: " + e.getMessage());
        }
        return null;
    }

    // Método para construir a query SQL
    private static String buildQuery() {
        return "SELECT P.ID_PRODUTO, P.DESCRICAO, T.PREC_VENDA, T.MARG_VENDA, T.PREC_OFERTA " +
                "FROM TPRECOS T " +
                "JOIN TPRODUTOS P ON T.ID_PRODUTO = P.ID_PRODUTO " +
                "WHERE P.EH_ACOUGUE = 1";
    }

    // Método para executar a consulta e exibir os resultados
    private static void executeQuery(Connection connection, String sql) {
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            System.out.println("Produtos encontrados:");
            while (resultSet.next()) {
                int idProduto = resultSet.getInt("ID_PRODUTO");
                String descricao = resultSet.getString("DESCRICAO");
                double preco = resultSet.getDouble("PREC_VENDA");

                System.out.println("ID Produto: " + idProduto + ", Descrição: " + descricao + ", Preço: " + preco);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao executar a consulta: " + e.getMessage());
        }
    }
}
