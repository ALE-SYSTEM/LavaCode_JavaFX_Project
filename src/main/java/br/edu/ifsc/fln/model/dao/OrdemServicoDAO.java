package br.edu.ifsc.fln.model.dao;

import br.edu.ifsc.fln.model.domain.EStatus;
import br.edu.ifsc.fln.model.domain.ItemOS;
import br.edu.ifsc.fln.model.domain.OrdemServico;
import br.edu.ifsc.fln.model.domain.Servico;
import br.edu.ifsc.fln.model.domain.Veiculo;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrdemServicoDAO {
    private Connection connection;

    public OrdemServicoDAO() {
    }

    public Connection getConnection() {
        return this.connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public void inserir(OrdemServico ordemServico) throws SQLException {
        String sql = "INSERT INTO ordem_servico(total, agenda, desconto, id_veiculo, os_status) VALUES(?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = this.connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            this.connection.setAutoCommit(false);
            stmt.setBigDecimal(1, BigDecimal.valueOf(ordemServico.getTotal()));
            stmt.setDate(2, Date.valueOf(ordemServico.getAgenda()));
            stmt.setDouble(3, ordemServico.getDesconto()); // Corrigido de taxaDesconto para desconto
            stmt.setString(4, ordemServico.getStatus() != null ? ordemServico.getStatus().name() : EStatus.ABERTA.name());
            stmt.setInt(5, ordemServico.getVeiculo().getId());
            stmt.executeUpdate();

            // Recuperar o número gerado
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                ordemServico.setNumero(rs.getLong(1));
            }
            rs.close();

            ItemOSDAO itemOSDAO = new ItemOSDAO();
            itemOSDAO.setConnection(this.connection);
            ServicoDAO servicoDAO = new ServicoDAO();
            servicoDAO.setConnection(this.connection);
            int quantidade = 0;

            for (ItemOS itemOS : ordemServico.getItensOS()) {
                itemOS.setOrdemServico(ordemServico); // Definir a ordem antes de inserir
                itemOSDAO.inserir(itemOS);
                quantidade += Servico.getPontos();
            }

            PontuacaoDAO pontuacaoDAO = new PontuacaoDAO();
            pontuacaoDAO.setConnection(this.connection);
            int pontuacaoAtual = pontuacaoDAO.buscarQuantidade(ordemServico.getVeiculo().getCliente().getPontuacao());
            ordemServico.getVeiculo().getCliente().getPontuacao().setQuantidade(pontuacaoAtual + quantidade);
            pontuacaoDAO.alterar(ordemServico.getVeiculo().getCliente().getPontuacao());
            this.connection.commit();
        } catch (SQLException ex) {
            this.connection.rollback();
            throw ex;
        } finally {
            this.connection.setAutoCommit(true);
        }
    }

    public boolean alterar(OrdemServico ordemServico) throws SQLException {
        String sql = "UPDATE ordem_servico SET total=?, agenda=?, desconto=?, id_veiculo=?, os_status=? WHERE numero=?";
        try (PreparedStatement stmt = this.connection.prepareStatement(sql)) {
            this.connection.setAutoCommit(false);
            ItemOSDAO itemOSDAO = new ItemOSDAO();
            itemOSDAO.setConnection(this.connection);
            PontuacaoDAO pontuacaoDAO = new PontuacaoDAO();
            pontuacaoDAO.setConnection(this.connection);

            // Remover itens antigos
            List<ItemOS> itensAntigos = itemOSDAO.listarPorOrdem(ordemServico);
            int quantidadeAnterior = 0;
            for (ItemOS iv : itensAntigos) {
                itemOSDAO.remover(iv);
                quantidadeAnterior += Servico.getPontos();
            }

            // Atualizar pontuação anterior
            int pontuacaoAtual = pontuacaoDAO.buscarQuantidade(ordemServico.getVeiculo().getCliente().getPontuacao());
            ordemServico.getVeiculo().getCliente().getPontuacao().setQuantidade(pontuacaoAtual - quantidadeAnterior);
            pontuacaoDAO.alterar(ordemServico.getVeiculo().getCliente().getPontuacao());

            // Atualizar a ordem de serviço
            stmt.setBigDecimal(1, BigDecimal.valueOf(ordemServico.getTotal()));
            stmt.setDate(2, Date.valueOf(ordemServico.getAgenda()));
            stmt.setDouble(3, ordemServico.getDesconto());
            stmt.setInt(4, ordemServico.getVeiculo().getId());
            stmt.setString(5, ordemServico.getStatus() != null ? ordemServico.getStatus().name() : EStatus.ABERTA.name());
            stmt.setLong(6, ordemServico.getNumero());
            stmt.executeUpdate();

            // Inserir novos itens
            int quantidadeNova = 0;
            for (ItemOS iv : ordemServico.getItensOS()) {
                itemOSDAO.inserir(iv);
                quantidadeNova += Servico.getPontos();
            }

            // Atualizar pontuação com novos itens
            pontuacaoAtual = pontuacaoDAO.buscarQuantidade(ordemServico.getVeiculo().getCliente().getPontuacao());
            ordemServico.getVeiculo().getCliente().getPontuacao().setQuantidade(pontuacaoAtual + quantidadeNova);
            pontuacaoDAO.alterar(ordemServico.getVeiculo().getCliente().getPontuacao());

            this.connection.commit();
            return true;
        } catch (SQLException ex) {
            this.connection.rollback();
            throw ex;
        } finally {
            this.connection.setAutoCommit(true);
        }
    }

    public void remover(OrdemServico ordemServico) throws SQLException {
        String sql = "DELETE FROM ordem_servico WHERE numero=?";
        try (PreparedStatement stmt = this.connection.prepareStatement(sql)) {
            this.connection.setAutoCommit(false);
            ItemOSDAO itemOSDAO = new ItemOSDAO();
            itemOSDAO.setConnection(this.connection);
            ServicoDAO servicoDAO = new ServicoDAO();
            servicoDAO.setConnection(this.connection);
            int quantidade = 0;

            for (ItemOS iv : ordemServico.getItensOS()) {
                itemOSDAO.remover(iv);
                quantidade += Servico.getPontos();
            }

            PontuacaoDAO pontuacaoDAO = new PontuacaoDAO();
            pontuacaoDAO.setConnection(this.connection);
            int pontuacaoAtual = pontuacaoDAO.buscarQuantidade(ordemServico.getVeiculo().getCliente().getPontuacao());
            ordemServico.getVeiculo().getCliente().getPontuacao().setQuantidade(pontuacaoAtual - quantidade);
            pontuacaoDAO.alterar(ordemServico.getVeiculo().getCliente().getPontuacao());

            stmt.setLong(1, ordemServico.getNumero());
            stmt.executeUpdate();
            this.connection.commit();
        } catch (SQLException ex) {
            this.connection.rollback();
            throw ex;
        } finally {
            this.connection.setAutoCommit(true);
        }
    }

    public List<OrdemServico> listar() throws SQLException {
        String sql = "SELECT * FROM ordem_servico";
        List<OrdemServico> retorno = new ArrayList<>();
        try (PreparedStatement stmt = this.connection.prepareStatement(sql);
             ResultSet resultado = stmt.executeQuery()) {
            while (resultado.next()) {
                OrdemServico ordemServico = new OrdemServico();
                ordemServico.setNumero(resultado.getLong("numero"));
                ordemServico.setTotal(resultado.getDouble("total"));
                ordemServico.setAgenda(resultado.getDate("agenda").toLocalDate());
                ordemServico.setDesconto(resultado.getDouble("desconto"));
                ordemServico.setStatus(EStatus.valueOf(resultado.getString("os_status").toUpperCase()));

                Veiculo veiculo = new Veiculo();
                veiculo.setId(resultado.getInt("id_veiculo"));
                VeiculoDAO veiculoDAO = new VeiculoDAO();
                veiculoDAO.setConnection(this.connection);
                ordemServico.setVeiculo(veiculoDAO.buscar(veiculo));

                ItemOSDAO itemOSDAO = new ItemOSDAO();
                itemOSDAO.setConnection(this.connection);
                List<ItemOS> itemOS = itemOSDAO.listarPorOrdem(ordemServico);
                ordemServico.setItensOS(itemOS);

                retorno.add(ordemServico);
            }
        }
        return retorno;
    }

    public OrdemServico buscar(OrdemServico ordemServico) throws SQLException {
        String sql = "SELECT * FROM ordem_servico WHERE numero=?";
        OrdemServico ordemServicoRetorno = null;
        try (PreparedStatement stmt = this.connection.prepareStatement(sql);
             ResultSet resultado = stmt.executeQuery()) {
            stmt.setLong(1, ordemServico.getNumero());
            if (resultado.next()) {
                ordemServicoRetorno = new OrdemServico();
                ordemServicoRetorno.setNumero(resultado.getLong("numero"));
                ordemServicoRetorno.setTotal(resultado.getDouble("total"));
                ordemServicoRetorno.setAgenda(resultado.getDate("agenda").toLocalDate());
                ordemServicoRetorno.setDesconto(resultado.getDouble("desconto"));
                ordemServicoRetorno.setStatus(EStatus.valueOf(resultado.getString("os_status").toUpperCase()));

                Veiculo veiculo = new Veiculo();
                veiculo.setId(resultado.getInt("id_veiculo"));
                VeiculoDAO veiculoDAO = new VeiculoDAO();
                veiculoDAO.setConnection(this.connection);
                ordemServicoRetorno.setVeiculo(veiculoDAO.buscar(veiculo));
            }
        }
        return ordemServicoRetorno;
    }

    public OrdemServico buscar(int id) throws SQLException {
        String sql = "SELECT * FROM ordem_servico WHERE numero=?";
        OrdemServico ordemServicoRetorno = null;
        try (PreparedStatement stmt = this.connection.prepareStatement(sql);
             ResultSet resultado = stmt.executeQuery()) {
            stmt.setLong(1, id);
            if (resultado.next()) {
                ordemServicoRetorno = new OrdemServico();
                ordemServicoRetorno.setNumero(resultado.getLong("numero"));
                ordemServicoRetorno.setTotal(resultado.getDouble("total"));
                ordemServicoRetorno.setAgenda(resultado.getDate("agenda").toLocalDate());
                ordemServicoRetorno.setDesconto(resultado.getDouble("desconto"));
                ordemServicoRetorno.setStatus(EStatus.valueOf(resultado.getString("os_status").toUpperCase()));

                Veiculo veiculo = new Veiculo();
                veiculo.setId(resultado.getInt("id_veiculo"));
                VeiculoDAO veiculoDAO = new VeiculoDAO();
                veiculoDAO.setConnection(this.connection);
                ordemServicoRetorno.setVeiculo(veiculoDAO.buscar(veiculo));
            }
        }
        return ordemServicoRetorno;
    }

    public OrdemServico buscarUltimaOrdemServico() throws SQLException {
        String sql = "SELECT max(numero) as max FROM ordem_servico";
        OrdemServico retorno = new OrdemServico();
        try (PreparedStatement stmt = this.connection.prepareStatement(sql);
             ResultSet resultado = stmt.executeQuery()) {
            if (resultado.next()) {
                retorno.setNumero(resultado.getLong("max"));
            }
        }
        return retorno;
    }
}