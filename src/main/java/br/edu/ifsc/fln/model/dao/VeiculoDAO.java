package br.edu.ifsc.fln.model.dao;

import br.edu.ifsc.fln.model.domain.Cliente;
import br.edu.ifsc.fln.model.domain.Cor;
import br.edu.ifsc.fln.model.domain.Modelo;
import br.edu.ifsc.fln.model.domain.Veiculo;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class VeiculoDAO {

    private Connection connection;

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public boolean inserir(Veiculo veiculo) {
        String sql = "INSERT INTO veiculo(placa, observacoes, id_modelo, id_cor, id_cliente) VALUES(?, ?, ?, ?, ?)";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, veiculo.getPlaca());
            stmt.setString(2, veiculo.getObservacoes());
            stmt.setInt(3, veiculo.getModelo().getId());
            stmt.setInt(4, veiculo.getCor().getId());
            stmt.setInt(5, veiculo.getCliente().getId());

            stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(OrdemServicoDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean alterar(Veiculo veiculo) {
        String sql = "UPDATE veiculo SET placa=?, observacoes=?, id_modelo=?, id_cor=?, id_cliente=? WHERE id=?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, veiculo.getPlaca());
            stmt.setString(2, veiculo.getObservacoes());
            stmt.setInt(3, veiculo.getModelo().getId());
            stmt.setInt(4, veiculo.getCor().getId());
            stmt.setInt(5, veiculo.getCliente().getId());
            stmt.setInt(6, veiculo.getId());

            stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(OrdemServicoDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean remover(Veiculo veiculo) {
        String sql = "DELETE FROM veiculo WHERE id=?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, veiculo.getId());
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(OrdemServicoDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public List<Veiculo> listar() {
        String sql = "SELECT * FROM veiculo";
        List<Veiculo> retorno = new ArrayList<>();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet resultado = stmt.executeQuery();
            while (resultado.next()) {
                Veiculo veiculo = populateVO(resultado);
                retorno.add(veiculo);
            }
        } catch (SQLException ex) {
            Logger.getLogger(OrdemServicoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retorno;
    }

    public Veiculo buscar(Veiculo veiculo) {
        String sql = "SELECT * FROM veiculo WHERE id=?";
        Veiculo retorno = new Veiculo();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, veiculo.getId());
            ResultSet resultado = stmt.executeQuery();
            if (resultado.next()) {
                retorno = populateVO(resultado);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ModeloDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retorno;
    }

    private Veiculo populateVO(ResultSet rs) throws SQLException {
        Veiculo veiculo = new Veiculo();
        veiculo.setId(rs.getInt("id"));
        veiculo.setPlaca(rs.getString("placa"));
        veiculo.setObservacoes(rs.getString("observacoes"));

        // Inicializar os objetos antes de configurá-los
        Modelo modelo = new Modelo();
        modelo.setId(rs.getInt("id_modelo"));
        veiculo.setModelo(modelo);

        Cor cor = new Cor();
        cor.setId(rs.getInt("id_cor"));
        veiculo.setCor(cor);

        Cliente cliente = new Cliente() {}; // Instância anônima para evitar classe abstrata
        cliente.setId(rs.getInt("id_cliente"));
        veiculo.setCliente(cliente);

        return veiculo;
    }
}