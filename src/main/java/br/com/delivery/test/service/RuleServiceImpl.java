package br.com.delivery.test.service;

import br.com.delivery.test.function.RuleSortFn;
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
        //A ordem será levada em consideração
        rulesPipeline.add(RuleSortFn.LTE_THREE);
        rulesPipeline.add(RuleSortFn.LTE_FIVE);
        rulesPipeline.add(RuleSortFn.GT_FIVE);
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
