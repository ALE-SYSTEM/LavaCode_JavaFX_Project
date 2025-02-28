package br.edu.ifsc.fln.model.dao;

import br.edu.ifsc.fln.model.domain.ECategoria;
import br.edu.ifsc.fln.model.domain.ETipoCombustivel;
import br.edu.ifsc.fln.model.domain.Marca;
import br.edu.ifsc.fln.model.domain.Modelo;
import br.edu.ifsc.fln.model.domain.Motor;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ModeloDAO {

    private Connection connection;

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public boolean inserir(Modelo modelo) throws SQLException {
        String sqlMotor = "INSERT INTO motor(potencia) VALUES(?)";
        String sqlModelo = "INSERT INTO modelo(descricao, id_marca, id_motor, categoria, combustivel) VALUES(?, ?, ?, ?, ?)";

        try (PreparedStatement stmtMotor = connection.prepareStatement(sqlMotor, Statement.RETURN_GENERATED_KEYS)) {
            // Inserir o Motor e obter o ID gerado
            stmtMotor.setInt(1, modelo.getMotor().getPotencia());
            stmtMotor.executeUpdate();

            try (ResultSet rsMotor = stmtMotor.getGeneratedKeys()) {
                if (rsMotor.next()) {
                    int idMotor = rsMotor.getInt(1);
                    modelo.getMotor().setId(idMotor);
                } else {
                    throw new SQLException("Falha ao obter ID do motor inserido.");
                }
            }
        }

        try (PreparedStatement stmtModelo = connection.prepareStatement(sqlModelo)) {
            // Inserir o Modelo com o ID do Motor
            stmtModelo.setString(1, modelo.getDescricao());
            stmtModelo.setInt(2, modelo.getMarca().getId());
            stmtModelo.setInt(3, modelo.getMotor().getId());
            stmtModelo.setString(4, modelo.getCategoria().getDescricao().toUpperCase());
            stmtModelo.setString(5, modelo.getCombustivel().name().toUpperCase());
            stmtModelo.execute();
        }

        return true;
    }

    public boolean alterar(Modelo modelo) throws SQLException {
        String sqlModelo = "UPDATE modelo SET descricao=?, id_marca=?, id_motor=?, categoria=?, combustivel=? WHERE id=?";
        String sqlMotor = "UPDATE motor SET potencia=? WHERE id=?";

        try (PreparedStatement stmtMotor = connection.prepareStatement(sqlMotor)) {
            // Atualizar o Motor
            stmtMotor.setInt(1, modelo.getMotor().getPotencia());
            stmtMotor.setInt(2, modelo.getMotor().getId());
            stmtMotor.executeUpdate();
        }

        try (PreparedStatement stmtModelo = connection.prepareStatement(sqlModelo)) {
            // Atualizar o Modelo
            stmtModelo.setString(1, modelo.getDescricao());
            stmtModelo.setInt(2, modelo.getMarca().getId());
            stmtModelo.setInt(3, modelo.getMotor().getId());
            stmtModelo.setString(4, modelo.getCategoria().getDescricao().toUpperCase());
            stmtModelo.setString(5, modelo.getCombustivel().name().toUpperCase());
            stmtModelo.setInt(6, modelo.getId());
            stmtModelo.executeUpdate();
        }

        return true;
    }

    public boolean remover(Modelo modelo) throws SQLException {
        String sql = "DELETE FROM modelo WHERE id=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, modelo.getId());
            stmt.executeUpdate();
            return true;
        }
    }

    public List<Modelo> listar() throws SQLException {
        String sql = "SELECT m.id, m.descricao, m.id_marca, m.id_motor, m.categoria, m.combustivel, " +
                "ma.nome AS marca_nome, mo.potencia " +
                "FROM modelo m " +
                "JOIN marca ma ON m.id_marca = ma.id " +
                "JOIN motor mo ON m.id_motor = mo.id";
        List<Modelo> retorno = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet resultado = stmt.executeQuery()) {
            while (resultado.next()) {
                Modelo modelo = populateVO(resultado);
                retorno.add(modelo);
            }
        }

        return retorno;
    }

    public Modelo buscar(Modelo modelo) throws SQLException {
        String sql = "SELECT m.id, m.descricao, m.id_marca, m.id_motor, m.categoria, m.combustivel, " +
                "ma.nome AS marca_nome, mo.potencia " +
                "FROM modelo m " +
                "JOIN marca ma ON m.id_marca = ma.id " +
                "JOIN motor mo ON m.id_motor = mo.id " +
                "WHERE m.id=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, modelo.getId());
            try (ResultSet resultado = stmt.executeQuery()) {
                if (resultado.next()) {
                    return populateVO(resultado);
                }
            }
        }
        return null;
    }

    private Modelo populateVO(ResultSet rs) throws SQLException {
        Modelo modelo = new Modelo();
        modelo.setId(rs.getInt("id"));
        modelo.setDescricao(rs.getString("descricao"));

        Marca marca = new Marca();
        marca.setId(rs.getInt("id_marca"));
        marca.setNome(rs.getString("marca_nome"));
        modelo.setMarca(marca);

        Motor motor = new Motor();
        motor.setId(rs.getInt("id_motor"));
        motor.setPotencia(rs.getInt("potencia"));
        modelo.setMotor(motor);

        modelo.setCategoria(ECategoria.valueOf(rs.getString("categoria").toUpperCase()));
        modelo.setCombustivel(ETipoCombustivel.valueOf(rs.getString("combustivel").toUpperCase()));

        return modelo;
    }

    public List<Modelo> listarPorMarca(Marca marca) throws SQLException {
        String sql = "SELECT m.id, m.descricao, m.id_marca, m.id_motor, m.categoria, m.combustivel, " +
                "ma.nome AS marca_nome, mo.potencia " +
                "FROM modelo m " +
                "JOIN marca ma ON m.id_marca = ma.id " +
                "JOIN motor mo ON m.id_motor = mo.id " +
                "WHERE m.id_marca = ?";
        List<Modelo> retorno = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, marca.getId());
            try (ResultSet resultado = stmt.executeQuery()) {
                while (resultado.next()) {
                    Modelo modelo = populateVO(resultado);
                    retorno.add(modelo);
                }
            }
        }

        return retorno;
    }
}