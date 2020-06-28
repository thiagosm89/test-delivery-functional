package br.com.delivery.test.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
public class Bill {
    @Id
    @GeneratedValue
    private Long id;
    private String description;
    private BigDecimal value;
    private LocalDate dueDate;
    private LocalDate payDate;
    private BigDecimal correctedValue;
    private Rule rule;

    @Column(name = "DESCRIPTION")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(name = "VALUE")
    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    @Column(name = "DUE_DATE")
    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    @Column(name = "PAY_DATE")
    public LocalDate getPayDate() {
        return payDate;
    }

    public void setPayDate(LocalDate payDate) {
        this.payDate = payDate;
    }

    @Column(name = "CORRECT_VALUE")
    public BigDecimal getCorrectedValue() {
        return correctedValue;
    }

    public void setCorrectedValue(BigDecimal correctedValue) {
        this.correctedValue = correctedValue;
    }

    public Rule getRule() {
        return rule;
    }

    public void setRule(Rule rule) {
        this.rule = rule;
    }
}
