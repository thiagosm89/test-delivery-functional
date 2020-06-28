package br.com.delivery.test.function;

import br.com.delivery.test.model.Rule;
import io.vavr.Function1;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.function.Predicate;

/**
 * Novas regras são adicionadas aqui.
 */
public class RuleSortFn {
    //Pode estar armazenado no banco de dados
    private final static String LTE_THREE = "function(x) x > 0 && x <= 3";
    private final static String LTE_FIVE = "function(x) x > 0 && x <= 5";
    private final static String GT_FIVE = "function(x) x > 5";

    private static final ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");

    private final static Function1<String, Predicate<Long>> PREDICATE = (stringFn) -> {
        try {
            String predicateName = Predicate.class.getName();
            return (Predicate<Long>) engine.eval(String.format("new %s(%s)", predicateName, stringFn));
        } catch (ScriptException e) {
            e.printStackTrace();
        }
        return null;
    };

    public final static Rule RULE_01 = Rule.of(2, 0.1, PREDICATE.apply(LTE_THREE));
    public final static Rule RULE_02 = Rule.of(3, 0.2, PREDICATE.apply(LTE_FIVE));
    public final static Rule RULE_03 = Rule.of(5, 0.3, PREDICATE.apply(GT_FIVE));
}
