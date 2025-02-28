package br.edu.ifsc.fln.model.domain;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class OrdemServico {
    private long numero;
    private double total;
    private LocalDate agenda;
    private double desconto;

    private Veiculo veiculo;
    private List<ItemOS> itensOS;
    private EStatus status;

    public OrdemServico() {
        this.veiculo = null;
        this.itensOS = new ArrayList<>();
    }

    public OrdemServico(long numero, double total, LocalDate agenda) {
        this.numero = numero;
        this.total = total;
        this.agenda = agenda;
        this.veiculo = null;
        this.itensOS = new ArrayList<>();
    }

    public long getNumero() {
        return numero;
    }

    public void setNumero(long numero) {
        this.numero = numero;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public LocalDate getAgenda() {
        return agenda;
    }

    public void setAgenda(LocalDate agenda) {
        this.agenda = agenda;
    }

    public double getDesconto() {
        return desconto;
    }

    public void setDesconto(double desconto) {
        this.desconto = desconto;
    }

    public Veiculo getVeiculo() {
        return veiculo;
    }

    public void setVeiculo(Veiculo veiculo) {
        this.veiculo = veiculo;
    }

    public List<ItemOS> getItensOS() {
        return itensOS;
    }

    public void setItensOS(List<ItemOS> itensOS) {
        this.itensOS = itensOS;
    }

    public EStatus getStatus() {
        return status;
    }

    public void setStatus(EStatus status) {
        this.status = status;
    }

    public double calcularServico() {
        total = 0.0;
        if (itensOS != null) {
            for (ItemOS item : itensOS) {
                total += (item.getValorServico() != 0 ? item.getValorServico() : item.getServico().getValor());
            }
        }
        return total;
    }

    public int calcularPontosFidelidade() {
        if (itensOS != null) {
            return itensOS.size() * Servico.getPontos(); // Usa pontos estático
        }
        return 0;
    }

    public void add(ItemOS item) {
        if (itensOS == null) {
            itensOS = new ArrayList<>();
        }
        itensOS.add(item);
        item.setOrdemServico(this);
    }

    public void remove(ItemOS item) {
        if (itensOS != null) {
            itensOS.remove(item);
        }
    }
}