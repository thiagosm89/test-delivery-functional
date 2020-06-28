package br.com.delivery.test.service;

import br.com.delivery.test.model.Bill;
import br.com.delivery.test.model.Rule;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static br.com.delivery.test.function.RuleSortFn.*;

@Service
public class RuleServiceImpl implements RuleService {
    private final List<Rule> rulesPipeline;

    public RuleServiceImpl() {
        rulesPipeline = new ArrayList<>();
        definePipeline();
    }

    private void definePipeline() {
        //A ordem será levada em consideração
        rulesPipeline.add(RULE_01);
        rulesPipeline.add(RULE_02);
        rulesPipeline.add(RULE_03);
    }

    @Override
    public Optional<Rule> associateRule(final Bill bill) {
        return rulesPipeline
                .stream()
                .map(rule -> rule.combineRule(bill))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .findFirst();
    }
}
