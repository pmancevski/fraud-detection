package com.paymentiq.frauddetection.rule;

import com.paymentiq.frauddetection.model.Transaction;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class RuleEngine {

    private static final Logger log = LoggerFactory.getLogger(RuleEngine.class);

    private final List<Rule> rules = new ArrayList<>();

    @PostConstruct
    public void init() {
        addRule(new HighAmountRule());
        addRule(new VelocityRule());
        addRule(new LocationRule());
        log.info("Rules registered: {}", rules.size());
    }

    public void addRule(Rule rule) {
        rules.add(rule);
        log.info("Rule added: {}", rule.getRuleName());
    }

    public List<Rule> evaluate(Transaction transaction) {
        List<Rule> triggeredRules = new ArrayList<>();

        for (Rule rule : rules) {
            if (rule.evaluate(transaction)) {
                triggeredRules.add(rule);

                log.warn("Rule triggered: {} - {}", rule.getRuleName(), rule.getDescription());
            }
        }

        return triggeredRules;
    }
}
