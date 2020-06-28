package br.com.delivery.test.service;

import br.com.delivery.test.model.Bill;
import br.com.delivery.test.model.Rule;
import br.com.delivery.test.repository.BillRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Optional;
import java.util.function.Function;

@Service
public class BillServiceImpl implements BillService {

    private final BillRepository repository;
    private final RuleService ruleService;

    public BillServiceImpl(BillRepository repository, RuleService ruleService) {
        this.repository = repository;
        this.ruleService = ruleService;
    }

    @Override
    public Mono<Bill> save(Bill bill) {
        return Mono.just(bill)
                .map(associateRuleFn());
    }

    private Function<Bill, Bill> associateRuleFn() {
        return bill -> {
            Optional<Rule> ruleMaybe = ruleService.associateRule(bill);
            ruleMaybe.ifPresent(bill::setRule);
            return bill;
        };
    }

    @Override
    public Mono<Bill> list() {
        return null;
    }
}
