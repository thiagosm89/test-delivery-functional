package br.com.delivery.test.model;

//Pode vir de banco de dados, são funções javascripts que serão executadas de forma dinâmica no código
public enum RuleJavascript {
    LTE_THREE("function(delayDays) delayDays > 0 && delayDays <= 3"),
    LTE_FIVE("function(delayDays) delayDays > 0 && delayDays <= 5"),
    GT_FIVE("function(delayDays) delayDays > 5");

    private String func;

    RuleJavascript(String func) {
        this.func = func;
    }

    public String getFunc() {
        return func;
    }
}
