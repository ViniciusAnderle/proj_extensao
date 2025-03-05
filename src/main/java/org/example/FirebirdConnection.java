package org.example;

import java.sql.*;

public class FirebirdConnection {

    // URL de conexão com o banco Firebird
    private static final String URL = "jdbc:firebirdsql://localhost:3050/" + System.getProperty("user.home") + "/sol.fdb";
    // Credenciais de acesso ao banco Firebird
    private static final String USER = "SYSDBA";
    private static final String PASSWORD = "masterkey";

    public static void main(String[] args) {
        // Conecta ao banco e realiza as consultas
        try (Connection connection = createConnection()) {
            if (connection != null) {
                System.out.println("Conectado ao banco Firebird com sucesso!");

                // Consulta para obter produtos de açougue
                String sqlAçougue = buildQueryAçougue();
                executeQuery(connection, sqlAçougue, "Açougue");

                // Consulta para obter produtos de aves
                String sqlAves = buildQueryAves();
                executeQuery(connection, sqlAves, "Aves");

                // Consulta para obter produtos bovinos
                String sqlBovinos = buildQueryBovinos();
                executeQuery(connection, sqlBovinos, "Bovinos");

                // Consulta para obter outros produtos
                String sqlOutros = buildQueryOutros();
                executeQuery(connection, sqlOutros, "Outros");

                // Consulta para obter produtos em oferta
                String sqlOferta = buildQueryOferta();
                executeQueryOferta(connection, sqlOferta);
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

    // Método para construir a query SQL para produtos de açougue
    private static String buildQueryAçougue() {
        return "SELECT P.ID_PRODUTO, P.DESCRICAO, T.PREC_VENDA, T.MARG_VENDA, T.PREC_OFERTA " +
                "FROM TPRECOS T " +
                "JOIN TPRODUTOS P ON T.ID_PRODUTO = P.ID_PRODUTO " +
                "WHERE P.TABELA1 = '10'";  // Para produtos de açougue
    }

    // Método para construir a query SQL para produtos de aves
    private static String buildQueryAves() {
        return "SELECT P.ID_PRODUTO, P.DESCRICAO, T.PREC_VENDA, T.MARG_VENDA, T.PREC_OFERTA " +
                "FROM TPRECOS T " +
                "JOIN TPRODUTOS P ON T.ID_PRODUTO = P.ID_PRODUTO " +
                "WHERE P.TABELA1 = '12'";  // Para produtos de aves (frango)
    }

    // Método para construir a query SQL para produtos bovinos
    private static String buildQueryBovinos() {
        return "SELECT P.ID_PRODUTO, P.DESCRICAO, T.PREC_VENDA, T.MARG_VENDA, T.PREC_OFERTA " +
                "FROM TPRECOS T " +
                "JOIN TPRODUTOS P ON T.ID_PRODUTO = P.ID_PRODUTO " +
                "WHERE P.TABELA1 = '11'";  // Para produtos bovinos
    }

    // Método para construir a query SQL para outros produtos
    private static String buildQueryOutros() {
        return "SELECT P.ID_PRODUTO, P.DESCRICAO, T.PREC_VENDA, T.MARG_VENDA, T.PREC_OFERTA " +
                "FROM TPRECOS T " +
                "JOIN TPRODUTOS P ON T.ID_PRODUTO = P.ID_PRODUTO " +
                "WHERE P.TABELA1 = '13'";  // Para outros produtos
    }

    // Método para executar a consulta e exibir os resultados
    private static void executeQuery(Connection connection, String sql, String categoria) {
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            System.out.println("\nProdutos encontrados para a categoria: " + categoria);
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

    // Método para construir a query SQL para produtos em oferta com validação de data
    private static String buildQueryOferta() {
        return "SELECT P.ID_PRODUTO, P.DESCRICAO, T.PREC_OFERTA, T.INIC_OFERTA, T.FINA_OFERTA " +
                "FROM TPRECOS T " +
                "JOIN TPRODUTOS P ON T.ID_PRODUTO = P.ID_PRODUTO " +
                "AND T.PREC_OFERTA > 0 " +
                "AND CURRENT_DATE BETWEEN T.INIC_OFERTA AND T.FINA_OFERTA";
    }

    // Método para executar a consulta de produtos em oferta e exibir os resultados
    private static void executeQueryOferta(Connection connection, String sql) {
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            System.out.println();
            System.out.println("Produtos em oferta encontrados:");
            while (resultSet.next()) {
                int idProduto = resultSet.getInt("ID_PRODUTO");
                String descricao = resultSet.getString("DESCRICAO");
                double precoOferta = resultSet.getDouble("PREC_OFERTA");
                Date dataInicialOferta = resultSet.getDate("INIC_OFERTA");
                Date dataFinalOferta = resultSet.getDate("FINA_OFERTA");

                System.out.println("ID Produto: " + idProduto + ", Descrição: " + descricao +
                        ", Preço de Oferta: " + precoOferta + ", Data inicial da oferta: " +
                        dataInicialOferta + ", Data final da oferta: " + dataFinalOferta);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao executar a consulta: " + e.getMessage());
        }
    }
}
