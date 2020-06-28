package br.com.delivery.test.service;

import br.com.delivery.test.model.Bill;
import br.com.delivery.test.model.Rule;
import io.vavr.Function1;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import static br.com.delivery.test.model.RuleJavascript.*;

@Service
public class RuleServiceImpl implements RuleService {

    private final static Logger LOGGER = LoggerFactory.getLogger(RuleServiceImpl.class);

    //Mais informações sobre nashorn: https://docs.oracle.com/javase/10/nashorn/introduction.htm#JSNUG136
    private static final ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");

    private final List<Rule> rulesPipeline;

    //Define o predicate de forma dinâmica, permitindo que esteja salvo em banco a lógica funcional
    private final static Function1<String, Predicate<Long>> RULE_PREDICATE_FN = (javascriptFn) -> {
        String predicateName = Predicate.class.getName();
        Try<Predicate<Long>> predicateTry = Try.of(() -> (Predicate<Long>) engine.eval(String.format("new %s(%s)", predicateName, javascriptFn)));
        if(predicateTry.isFailure()) {
            LOGGER.error("Function Javascript is invalid.", predicateTry.getCause());
            return null;
        }
        return predicateTry.get();
    };

    public RuleServiceImpl() {
        rulesPipeline = new ArrayList<>();
        definePipeline();
    }

    private void definePipeline() {
        //A ordem será levada em consideração
        rulesPipeline.add(Rule.of(2, 0.1, RULE_PREDICATE_FN.apply(LTE_THREE.getFunc())));
        rulesPipeline.add(Rule.of(3, 0.2, RULE_PREDICATE_FN.apply(LTE_FIVE.getFunc())));
        rulesPipeline.add(Rule.of(5, 0.3, RULE_PREDICATE_FN.apply(GT_FIVE.getFunc())));
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
