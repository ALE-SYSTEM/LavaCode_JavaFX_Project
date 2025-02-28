package br.edu.ifsc.fln.model.dao;

import br.edu.ifsc.fln.model.domain.ItemOS;
import br.edu.ifsc.fln.model.domain.OrdemServico;
import br.edu.ifsc.fln.model.domain.Servico;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ItemOSDAO {
    private Connection connection;

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public boolean inserir(ItemOS item) {
        String sql = "INSERT INTO item_os (id_servico, numero_os, valor_servico, observacoes) VALUES (?, ?, ?, ?)";
        try (Connection conn = connection;
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            conn.setAutoCommit(false);
            stmt.setInt(1, item.getServico().getId());
            stmt.setLong(2, item.getOrdemServico().getNumero());
            stmt.setDouble(3, item.getValorServico());
            stmt.setString(4, item.getObservacoes());
            stmt.executeUpdate();
            conn.commit();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(ItemOSDAO.class.getName()).log(Level.SEVERE, "Erro ao inserir item OS: " + ex.getMessage(), ex);
            return false;
        }
    }

    public boolean alterar(ItemOS item) {
        String sql = "UPDATE item_os SET valor_servico=?, observacoes=? WHERE id_servico=? AND numero_os=?";
        try (Connection conn = connection;
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            conn.setAutoCommit(false);
            stmt.setDouble(1, item.getValorServico());
            stmt.setString(2, item.getObservacoes());
            stmt.setInt(3, item.getServico().getId());
            stmt.setLong(4, item.getOrdemServico().getNumero());
            stmt.executeUpdate();
            conn.commit();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(ItemOSDAO.class.getName()).log(Level.SEVERE, "Erro ao alterar item OS: " + ex.getMessage(), ex);
            return false;
        }
    }

    public boolean remover(ItemOS item) {
        String sql = "DELETE FROM item_os WHERE id_servico=? AND numero_os=?";
        try (Connection conn = connection;
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            conn.setAutoCommit(false);
            stmt.setInt(1, item.getServico().getId());
            stmt.setLong(2, item.getOrdemServico().getNumero());
            stmt.executeUpdate();
            conn.commit();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(ItemOSDAO.class.getName()).log(Level.SEVERE, "Erro ao remover item OS: " + ex.getMessage(), ex);
            return false;
        }
    }

    public List<ItemOS> listarPorOrdem(OrdemServico os) {
        String sql = "SELECT * FROM item_os WHERE numero_os=?";
        List<ItemOS> retorno = new ArrayList<>();
        try (Connection conn = connection;
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet resultado = stmt.executeQuery()) {
            stmt.setLong(1, os.getNumero());
            while (resultado.next()) {
                ItemOS item = new ItemOS();
                item.setValorServico(resultado.getDouble("valor_servico"));
                item.setObservacoes(resultado.getString("observacoes"));

                Servico servico = new Servico();
                servico.setId(resultado.getInt("id_servico"));
                item.setServico(servico);

                OrdemServico ordem = new OrdemServico();
                ordem.setNumero(resultado.getLong("numero_os"));
                item.setOrdemServico(ordem);

                // Buscar objetos completos usando DAOs
                ServicoDAO servicoDAO = new ServicoDAO();
                servicoDAO.setConnection(connection);
                item.setServico(servicoDAO.buscar(servico));

                OrdemServicoDAO ordemDAO = new OrdemServicoDAO();
                ordemDAO.setConnection(connection);
                item.setOrdemServico(ordemDAO.buscar(ordem));

                retorno.add(item);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ItemOSDAO.class.getName()).log(Level.SEVERE, "Erro ao listar itens por ordem: " + ex.getMessage(), ex);
        }
        return retorno;
    }

    public ItemOS buscar(ItemOS item) {
        String sql = "SELECT * FROM item_os WHERE id_servico=? AND numero_os=?";
        ItemOS retorno = null;
        try (Connection conn = connection;
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet resultado = stmt.executeQuery()) {
            stmt.setInt(1, item.getServico().getId());
            stmt.setLong(2, item.getOrdemServico().getNumero());
            if (resultado.next()) {
                ItemOS newItem = new ItemOS();
                newItem.setValorServico(resultado.getDouble("valor_servico"));
                newItem.setObservacoes(resultado.getString("observacoes"));

                Servico servico = new Servico();
                servico.setId(resultado.getInt("id_servico"));
                newItem.setServico(servico);

                OrdemServico os = new OrdemServico();
                os.setNumero(resultado.getLong("numero_os"));
                newItem.setOrdemServico(os);

                ServicoDAO servicoDAO = new ServicoDAO();
                servicoDAO.setConnection(connection);
                newItem.setServico(servicoDAO.buscar(servico));

                OrdemServicoDAO ordemDAO = new OrdemServicoDAO();
                ordemDAO.setConnection(connection);
                newItem.setOrdemServico(ordemDAO.buscar(os));

                retorno = newItem;
            }
        } catch (SQLException ex) {
            Logger.getLogger(ItemOSDAO.class.getName()).log(Level.SEVERE, "Erro ao buscar item OS: " + ex.getMessage(), ex);
        }
        return retorno;
    }
}