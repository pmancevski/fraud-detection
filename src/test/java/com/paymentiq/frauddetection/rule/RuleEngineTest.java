package com.paymentiq.frauddetection.rule;

import com.paymentiq.frauddetection.model.Transaction;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class RuleEngineTest {

    @Test
    void testRuleEngine() {
        RuleEngine engine = new RuleEngine();

        Rule highAmountRule = new HighAmountRule();
        engine.addRule(highAmountRule);

        Transaction tx = new Transaction();
        tx.setAmount(new BigDecimal("15000"));

        var result = engine.evaluate(tx);
        assertFalse(result.isEmpty());
    }
}