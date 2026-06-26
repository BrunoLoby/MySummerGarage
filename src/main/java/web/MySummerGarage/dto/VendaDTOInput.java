package web.MySummerGarage.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Min;
import java.math.BigDecimal;

public class VendaDTOInput {

    @NotNull(message = "O anúncio é obrigatório")
    private Long codigoAnuncio;

    @NotNull(message = "O valor final é obrigatório")
    @DecimalMin(value = "0.01", message = "O valor deve ser maior que zero")
    private BigDecimal valorFinal;

    @NotBlank(message = "A forma de pagamento é obrigatória")
    private String tipoPagamento;

    @NotNull(message = "O número de parcelas é obrigatório")
    @Min(value = 1, message = "Mínimo de 1 parcela")
    private Integer parcelas;

    public Long getCodigoAnuncio() {
        return codigoAnuncio;
    }

    public void setCodigoAnuncio(Long codigoAnuncio) {
        this.codigoAnuncio = codigoAnuncio;
    }

    public BigDecimal getValorFinal() {
        return valorFinal;
    }

    public void setValorFinal(BigDecimal valorFinal) {
        this.valorFinal = valorFinal;
    }

    public String getTipoPagamento() {
        return tipoPagamento;
    }

    public void setTipoPagamento(String tipoPagamento) {
        this.tipoPagamento = tipoPagamento;
    }

    public Integer getParcelas() {
        return parcelas;
    }

    public void setParcelas(Integer parcelas) {
        this.parcelas = parcelas;
    }
}