package web.MySummerGarage.config;

import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;

import java.beans.PropertyEditorSupport;
import java.math.BigDecimal;

/**
 * Conversores aplicados a TODOS os controllers, para aceitar valores vindos
 * de campos com máscara brasileira (ex.: "3.000,00" para dinheiro).
 */
@ControllerAdvice
public class GlobalBindingAdvice {

    @InitBinder
    public void configurarConversores(WebDataBinder binder) {
        // BigDecimal: remove separador de milhar (.) e troca a vírgula decimal por ponto
        binder.registerCustomEditor(BigDecimal.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) {
                if (text == null || text.isBlank()) {
                    setValue(null);
                    return;
                }
                String limpo = text.trim().replace(".", "").replace(",", ".");
                setValue(new BigDecimal(limpo));
            }

            @Override
            public String getAsText() {
                Object valor = getValue();
                return valor == null ? "" : valor.toString();
            }
        });

        // Integer: mantém apenas os dígitos (remove o separador de milhar)
        binder.registerCustomEditor(Integer.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) {
                if (text == null) {
                    setValue(null);
                    return;
                }
                String somenteDigitos = text.replaceAll("\\D", "");
                setValue(somenteDigitos.isEmpty() ? null : Integer.valueOf(somenteDigitos));
            }

            @Override
            public String getAsText() {
                Object valor = getValue();
                return valor == null ? "" : valor.toString();
            }
        });
    }
}
