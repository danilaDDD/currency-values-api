package com.jfund.currencyvaluesservice;

import com.jfund.currencyvaluesservice.saver.NewCurrencyValueSaver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.core.env.ConfigurableEnvironment;

@SpringBootTest
class CurrencyValuesServiceApplicationTests {
    @Value("${spring.profiles.active}")
    private String profile;

    @Autowired
    private ConfigurableEnvironment env;
    private NewCurrencyValueSaver newCurrencyValueSaver;

    @Test
    void contextLoads() {
        assertSame("test", profile);
    }

    @Qualifier("newCurrencyValueSaver")
    @Autowired
    public void setNewCurrencyValueSaver(NewCurrencyValueSaver newCurrencyValueSaver) {
        this.newCurrencyValueSaver = newCurrencyValueSaver;
    }
}
