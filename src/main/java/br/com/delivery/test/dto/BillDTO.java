package br.com.delivery.test.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

class BillDTO {
    private String description;
    private BigDecimal value;
    private LocalDate payDate;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public LocalDate getPayDate() {
        return payDate;
    }

    public void setPayDate(LocalDate payDate) {
        this.payDate = payDate;
    }
}
