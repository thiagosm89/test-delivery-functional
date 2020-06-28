package br.com.delivery.test.service;

import br.com.delivery.test.model.Bill;
import br.com.delivery.test.repository.BillRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import static org.springframework.test.util.AssertionErrors.assertTrue;

public class BillServiceImplTest {
    private static BillServiceImpl service;
    private static RuleService ruleService;
    private static BillRepository repositoryMock;

    @BeforeAll
    public static void setup() {
        repositoryMock = Mockito.mock(BillRepository.class);
        ruleService = new RuleServiceImpl();
        service = new BillServiceImpl(repositoryMock, ruleService);
    }

    //Conta paga antecipadamente
    @Test
    public void save_whenAdvancePayment_thenRuleNull() {
        Bill bill = new Bill();
        bill.setDescription("Teste");
        bill.setDueDate(LocalDate.now());
        bill.setPayDate(LocalDate.now().minus(2, ChronoUnit.DAYS));
        bill.setValue(new BigDecimal(10));
        Mono<Bill> billMono = service.save(bill);

        StepVerifier.create(billMono.log())
                .assertNext((b) -> {
                    assertTrue("Rule is not null", b.getRule() == null);
                })
                .verifyComplete();
    }

    //Conta paga no dia de vencimento
    @Test
    public void save_whenNoLatePayment_thenRuleNull() {
        Bill bill = new Bill();
        bill.setDescription("Teste");
        bill.setDueDate(LocalDate.now());
        bill.setPayDate(LocalDate.now());
        bill.setValue(new BigDecimal(10));
        Mono<Bill> billMono = service.save(bill);

        StepVerifier.create(billMono.log())
                .assertNext((b) -> {
                    assertTrue("Rule is not null", b.getRule() == null);
                })
                .verifyComplete();
    }

    //Conta com 3 dias de atraso
    @Test
    public void save_whenThreeDaysOverdue_thenTwoPercenLatePenalty() {
        Bill bill = new Bill();
        bill.setDescription("Teste");
        bill.setDueDate(LocalDate.now().minus(3, ChronoUnit.DAYS));
        bill.setPayDate(LocalDate.now());
        bill.setValue(new BigDecimal(10));
        Mono<Bill> billMono = service.save(bill);

        StepVerifier.create(billMono.log())
                .assertNext((b) -> {
                    assertTrue("Rule is null", b.getRule() != null);
                    assertTrue("Rule is differente of 2", b.getRule().getLatePenalty().equals(2));
                })
                .verifyComplete();
    }

    //Conta com 4 dias de atraso
    @Test
    public void save_whenFourDaysOverdue_thenThreePercenLatePenalty() {
        Bill bill = new Bill();
        bill.setDescription("Teste");
        bill.setDueDate(LocalDate.now().minus(4, ChronoUnit.DAYS));
        bill.setPayDate(LocalDate.now());
        bill.setValue(new BigDecimal(10));
        Mono<Bill> billMono = service.save(bill);

        StepVerifier.create(billMono.log())
                .assertNext((b) -> {
                    assertTrue("Rule is null", b.getRule() != null);
                    assertTrue("Rule is differente of 3", b.getRule().getLatePenalty().equals(3));
                })
                .verifyComplete();
    }

    //Conta com 6 dias de atraso
    @Test
    public void save_whenSixDaysOverdue_thenFivePercenLatePenalty() {
        Bill bill = new Bill();
        bill.setDescription("Teste");
        bill.setDueDate(LocalDate.now().minus(6, ChronoUnit.DAYS));
        bill.setPayDate(LocalDate.now());
        bill.setValue(new BigDecimal(10));
        Mono<Bill> billMono = service.save(bill);

        StepVerifier.create(billMono.log())
                .assertNext((b) -> {
                    assertTrue("Rule is null", b.getRule() != null);
                    assertTrue("Rule is differente of 5", b.getRule().getLatePenalty().equals(5));
                })
                .verifyComplete();
    }
}
