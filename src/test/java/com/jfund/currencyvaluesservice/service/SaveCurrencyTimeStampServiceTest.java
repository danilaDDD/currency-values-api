package com.jfund.currencyvaluesservice.service;

import com.jfund.currencyvaluesservice.repository.CurrencyTimeStampRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@ActiveProfiles("test")
class SaveCurrencyTimeStampServiceTest {
    @Autowired
    private CurrencyTimeStampRepository repository;

    @Autowired
    private SaveCurrencyTimeStampService service;

    @BeforeEach
    public void setUp(){
        repository.deleteAll().block();
    }


    @Test
    void testSave_WhenNotLastTimeStamp_ThenInsertInputTimeStamp() {
    }

    @Test
    void testSave_WhenLastTimeStampWithAllDifferentValues_ThenInsertTimeStampWithAllValues(){

    }

    @Test
    void testSave_WhenLastTimeStampWithSomeDifferentValues_ThenInsertTimeStampWithDifferentValues(){

    }

    @Test
    void testSave_WhenLastTimeStampWithNotDifferentValues_ThenNotInsert(){

    }

    @Test
    void testSaveTimeStampWithEmptyValues_ShouldThrowSaveNewValuesException(){

    }

    @Test
    void testSaveTimeStampWithDateTimeLessThanExist_ShouldThrowSaveNewValuesException(){

    }
}