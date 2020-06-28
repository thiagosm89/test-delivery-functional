package br.com.delivery.test.dto;

import java.math.BigDecimal;

public class BillListDTO extends BillDTO {
    private BigDecimal correctedValue;

    public BigDecimal getCorrectedValue() {
        return correctedValue;
    }

    public void setCorrectedValue(BigDecimal correctedValue) {
        this.correctedValue = correctedValue;
    }
}
