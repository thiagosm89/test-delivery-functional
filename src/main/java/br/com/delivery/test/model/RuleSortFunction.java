package br.com.delivery.test.model;

import io.vavr.Function1;

import java.util.Optional;

public class RuleSortFunction {
    public final static Function1<Bill, Optional<Rule>> LTE_THREE = (bill) -> Rule
            .of(2, 0.1, (delayedDays) -> delayedDays > 0 && delayedDays <= 3)
            .combineRule(bill);

    public final static Function1<Bill, Optional<Rule>> LTE_FIVE = (bill) -> Rule
            .of(3, 0.2, (delayedDays) -> delayedDays > 0 && delayedDays <= 5)
            .combineRule(bill);

    public final static Function1<Bill, Optional<Rule>> GT_FIVE = (bill) -> Rule
            .of(5, 0.3, (delayedDays) -> delayedDays > 5)
            .combineRule(bill);
}
