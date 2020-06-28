package br.com.delivery.test.model;

import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.function.Predicate;

public class Rule {
    private Double interestLatePayment;
    private Integer latePenalty;
    private Predicate<Long> delayedDaysFn;

    public Rule() {
    }

    private Rule(int latePenalty, double interestLatePayment, Predicate<Long> delayedDaysFn) {
        this.interestLatePayment = interestLatePayment;
        this.latePenalty = latePenalty;
        this.delayedDaysFn = delayedDaysFn;
    }

    public static Rule of(int latePenalty, double interestLatePayment, Predicate<Long> delayedDaysFn) {
        return new Rule(latePenalty, interestLatePayment, delayedDaysFn);
    }

    public Double getInterestLatePayment() {
        return interestLatePayment;
    }

    public void setInterestLatePayment(Double interestLatePayment) {
        this.interestLatePayment = interestLatePayment;
    }

    public Integer getLatePenalty() {
        return latePenalty;
    }

    public void setLatePenalty(Integer latePenalty) {
        this.latePenalty = latePenalty;
    }

    public Optional<Rule> combineRule(Bill bill) {
        long delayedDays = ChronoUnit.DAYS.between(bill.getDueDate(), bill.getPayDate());
        return delayedDaysFn.test(delayedDays) ? Optional.of(this) : Optional.empty();
    }
}
