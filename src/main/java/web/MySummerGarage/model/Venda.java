package web.MySummerGarage.model;

//IMPORTS
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "venda")
public class Venda implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "geradorVenda", sequenceName = "venda_codigo_seq", allocationSize = 1)
    @GeneratedValue(generator = "geradorVenda", strategy = GenerationType.SEQUENCE)
    private Long codigo;

    @Column(name = "data_venda")
    private LocalDate dataVenda;

    @Column(name = "valor_final")
    private BigDecimal valorFinal;

    @Enumerated(EnumType.STRING)
    private StatusVenda status = StatusVenda.PENDENTE;

    @ManyToOne
    @JoinColumn(name = "codigo_anuncio")
    private AnuncioCarro anuncio;

    @ManyToOne
    @JoinColumn(name = "codigo_comprador")
    private Usuario comprador;

    public Long getCodigo() {
        return codigo;
    }

    public void setCodigo(Long codigo) {
        this.codigo = codigo;
    }

    public LocalDate getDataVenda() {
        return dataVenda;
    }

    public void setDataVenda(LocalDate dataVenda) {
        this.dataVenda = dataVenda;
    }

    public BigDecimal getValorFinal() {
        return valorFinal;
    }

    public void setValorFinal(BigDecimal valorFinal) {
        this.valorFinal = valorFinal;
    }

    public StatusVenda getStatus() {
        return status;
    }

    public void setStatus(StatusVenda status) {
        this.status = status;
    }

    public AnuncioCarro getAnuncio() {
        return anuncio;
    }

    public void setAnuncio(AnuncioCarro anuncio) {
        this.anuncio = anuncio;
    }

    public Usuario getComprador() {
        return comprador;
    }

    public void setComprador(Usuario comprador) {
        this.comprador = comprador;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((codigo == null) ? 0 : codigo.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Venda other = (Venda) obj;
        if (codigo == null) {
            if (other.codigo != null)
                return false;
        } else if (!codigo.equals(other.codigo))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Venda [codigo=" + codigo + ", dataVenda=" + dataVenda + ", valorFinal=" + valorFinal + ", status="
                + status + "]";
    }
}