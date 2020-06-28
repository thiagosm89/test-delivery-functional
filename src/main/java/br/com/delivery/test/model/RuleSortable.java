package br.com.delivery.test.model;

import java.math.BigDecimal;

public interface RuleSortable {
    boolean combine();

    BigDecimal correctedValue();
}
