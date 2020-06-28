package br.com.delivery.test.service;

import br.com.delivery.test.model.Bill;
import br.com.delivery.test.model.Rule;
import io.vavr.Function1;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RuleServiceImpl implements RuleService {
    private final List<Function1<Bill, Optional<Rule>>> rulesPipeline;

    private final static Function1<Bill, Optional<Rule>> LTE_THREE = (bill) -> Rule
            .of(2, 0.1, (delayedDays) -> delayedDays > 0 && delayedDays <= 3)
            .combineRule(bill);

    private final static Function1<Bill, Optional<Rule>> LTE_FIVE = (bill) -> Rule
            .of(3, 0.2, (delayedDays) -> delayedDays > 0 && delayedDays <= 5)
            .combineRule(bill);

    private final static Function1<Bill, Optional<Rule>> GT_FIVE = (bill) -> Rule
            .of(5, 0.3, (delayedDays) -> delayedDays > 5)
            .combineRule(bill);

    public RuleServiceImpl() {
        rulesPipeline = new ArrayList<>();
        definePipeline();
    }

    private void definePipeline() {
        rulesPipeline.add(LTE_THREE);
        rulesPipeline.add(LTE_FIVE);
        rulesPipeline.add(GT_FIVE);
    }

    private Optional<Rule> executePipeline(final Bill bill, final Function1<Bill, Optional<Rule>> ruleFn) {
        return ruleFn.apply(bill);
    }

    @Override
    public Optional<Rule> associateRule(final Bill bill) {
        return rulesPipeline
                .stream()
                .map(fn -> executePipeline(bill, fn))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .findFirst();
    }
}
