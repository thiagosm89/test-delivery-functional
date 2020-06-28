package br.com.delivery.test.service;

import br.com.delivery.test.model.Bill;
import br.com.delivery.test.model.Rule;
import io.vavr.Function1;
import org.springframework.stereotype.Service;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

@Service
public class RuleServiceImpl implements RuleService {
    //Mais informações sobre nashorn: https://docs.oracle.com/javase/10/nashorn/introduction.htm#JSNUG136
    private static final ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");

    private final List<Rule> rulesPipeline;

    //Pode vir de banco de dados, são funções javascripts que serão executadas de forma dinâmica no código
    private final static String LTE_THREE = "function(delayDays) delayDays > 0 && delayDays <= 3";
    private final static String LTE_FIVE = "function(delayDays) delayDays > 0 && delayDays <= 5";
    private final static String GT_FIVE = "function(delayDays) delayDays > 5";

    //Define o predicate de forma dinâmica, permitindo que esteja salvo em banco a lógica funcional
    private final static Function1<String, Predicate<Long>> RULE_PREDICATE_FN = (javascriptFn) -> {
        try {
            String predicateName = Predicate.class.getName();
            return (Predicate<Long>) engine.eval(String.format("new %s(%s)", predicateName, javascriptFn));
        } catch (ScriptException e) {
            e.printStackTrace();
        }
        return null;
    };

    public RuleServiceImpl() {
        rulesPipeline = new ArrayList<>();
        definePipeline();
    }

    private void definePipeline() {
        //A ordem será levada em consideração
        rulesPipeline.add(Rule.of(2, 0.1, RULE_PREDICATE_FN.apply(LTE_THREE)));
        rulesPipeline.add(Rule.of(3, 0.2, RULE_PREDICATE_FN.apply(LTE_FIVE)));
        rulesPipeline.add(Rule.of(5, 0.3, RULE_PREDICATE_FN.apply(GT_FIVE)));
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
