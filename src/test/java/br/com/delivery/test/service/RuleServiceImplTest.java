package br.com.delivery.test.service;

import br.com.delivery.test.model.Bill;
import br.com.delivery.test.model.Rule;
import br.com.delivery.test.service.RuleServiceImpl;
import io.vavr.control.Either;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import static org.springframework.test.util.AssertionErrors.assertEquals;
import static org.springframework.test.util.AssertionErrors.assertTrue;

/**
 * Testes para verificar se esta classificando corretamente a Rule para uma Bill.
 */
public class RuleServiceImplTest {
    private static RuleServiceImpl ruleService;

    @BeforeAll
    public static void setup() {
        ruleService = new RuleServiceImpl();
    }

    //Conta paga antecipadamente
    @Test
    public void associateRule_whenAdvancePayment_thenOptionalEmpty() {
        Bill bill = new Bill();
        bill.setDescription("Teste");
        bill.setDueDate(LocalDate.now());
        bill.setPayDate(LocalDate.now().minus(2, ChronoUnit.DAYS));
        bill.setValue(new BigDecimal(10));
        Optional<Rule> rule = ruleService.associateRule(bill);
        assertTrue("Not optional empty", !rule.isPresent());
    }

    //Conta sem atraso
    @Test
    public void associateRule_whenNoLatePayment_thenOptionalEmpty() {
        Bill bill = new Bill();
        bill.setDescription("Teste");
        bill.setDueDate(LocalDate.now());
        bill.setPayDate(LocalDate.now());
        bill.setValue(new BigDecimal(10));
        Optional<Rule> rule = ruleService.associateRule(bill);
        assertTrue("Not optional empty", !rule.isPresent());
    }

    //Conta com 3 dias de atraso
    @Test
    public void associateRule_whenThreeDaysOverdue_thenTwoPercenLatePenalty() {
        Bill bill = new Bill();
        bill.setDescription("Teste");
        bill.setDueDate(LocalDate.now().minus(3, ChronoUnit.DAYS));
        bill.setPayDate(LocalDate.now());
        bill.setValue(new BigDecimal(10));
        Optional<Rule> rule = ruleService.associateRule(bill);
        assertEquals("Not 2 percent late penalty", 2, rule.get().getLatePenalty());
    }

    //Conta com 4 dias de atraso
    @Test
    public void associateRule_whenFourDaysOverdue_thenThreePercenLatePenalty() {
        Bill bill = new Bill();
        bill.setDescription("Teste");
        bill.setDueDate(LocalDate.now().minus(4, ChronoUnit.DAYS));
        bill.setPayDate(LocalDate.now());
        bill.setValue(new BigDecimal(10));
        Optional<Rule> rule = ruleService.associateRule(bill);
        assertEquals("Not 3 percent late penalty", 3, rule.get().getLatePenalty());
    }

    //Conta com 6 dias de atraso
    @Test
    public void associateRule_whenSixDaysOverdue_thenFivePercenLatePenalty() {
        Bill bill = new Bill();
        bill.setDescription("Teste");
        bill.setDueDate(LocalDate.now().minus(6, ChronoUnit.DAYS));
        bill.setPayDate(LocalDate.now());
        bill.setValue(new BigDecimal(10));
        Optional<Rule> rule = ruleService.associateRule(bill);
        assertEquals("Not 2 percent late penalty", 5, rule.get().getLatePenalty());
    }
}
