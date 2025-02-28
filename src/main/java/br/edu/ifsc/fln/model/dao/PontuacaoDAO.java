package br.edu.ifsc.fln.model.dao;

import br.edu.ifsc.fln.model.domain.Pontuacao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PontuacaoDAO {
    private Connection connection;

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public int buscarQuantidade(Pontuacao pontuacao) throws SQLException {
        String sql = "SELECT pontuacao FROM cliente WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, pontuacao.getCliente().getId());
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("pontuacao");
                }
            }
        }
        return 0;
    }

    public boolean alterar(Pontuacao pontuacao) throws SQLException {
        String sql = "UPDATE cliente SET pontuacao = ? WHERE id = ?";
        try (Connection conn = connection;
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            conn.setAutoCommit(false);
            stmt.setInt(1, pontuacao.getQuantidade());
            stmt.setInt(2, pontuacao.getCliente().getId());
            int rowsAffected = stmt.executeUpdate();
            conn.commit();
            return rowsAffected > 0;
        }
    }
}