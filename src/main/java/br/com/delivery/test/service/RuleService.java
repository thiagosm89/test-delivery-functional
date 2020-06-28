package br.com.delivery.test.service;

import br.com.delivery.test.model.Bill;
import br.com.delivery.test.model.Rule;

import java.util.Optional;

public interface RuleService {
    Optional<Rule> associateRule(final Bill bill);
}
