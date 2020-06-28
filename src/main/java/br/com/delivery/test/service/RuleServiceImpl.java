package br.com.delivery.test.service;

import br.com.delivery.test.function.RuleSortFunction;
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

    public RuleServiceImpl() {
        rulesPipeline = new ArrayList<>();
        definePipeline();
    }

    private void definePipeline() {
        rulesPipeline.add(RuleSortFunction.LTE_THREE);
        rulesPipeline.add(RuleSortFunction.LTE_FIVE);
        rulesPipeline.add(RuleSortFunction.GT_FIVE);
    }

    @Override
    public Optional<Rule> associateRule(final Bill bill) {
        return rulesPipeline
                .stream()
                .map(fn -> fn.apply(bill))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .findFirst();
    }
}
