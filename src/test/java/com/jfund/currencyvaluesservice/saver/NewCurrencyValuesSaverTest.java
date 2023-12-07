package com.jfund.currencyvaluesservice.saver;

import com.jfund.currencyvaluesservice.entity.ChangeCurrencyValuesEvent;
import com.jfund.currencyvaluesservice.entity.CurrencyTimeStamp;
import com.jfund.currencyvaluesservice.repository.CurrencyTimeStampRepository;
import com.jfund.currencyvaluesservice.service.ChangeCurrencyValuesService;
import com.jfund.currencyvaluesservice.testutils.CustomerTestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
@SpringBootTest
public class NewCurrencyValuesSaverTest {
    private NewCurrencyValueSaver newCurrencyValueSaver;
    private CustomerTestUtils customerTestUtils;
    private CurrencyTimeStampRepository currencyTimeStampRepository;
    private ChangeCurrencyValuesService changeCurrencyValuesService;

    @Autowired
    public void setNewCurrencyValueSaver(NewCurrencyValueSaver newCurrencyValueSaver) {
        this.newCurrencyValueSaver = newCurrencyValueSaver;
    }

    @Autowired
    public void setCustomerTestUtils(CustomerTestUtils customerTestUtils) {
        this.customerTestUtils = customerTestUtils;
    }
    @Autowired
    public void setCurrencyTimeStampRepository(CurrencyTimeStampRepository currencyTimeStampRepository) {
        this.currencyTimeStampRepository = currencyTimeStampRepository;
    }
    @Autowired
    public void setChangeCurrencyValuesService(ChangeCurrencyValuesService changeCurrencyValuesService) {
        this.changeCurrencyValuesService = changeCurrencyValuesService;
    }

    @BeforeEach
    public void beforeEach(){
        customerTestUtils.dropCurrencyValuesTable();
    }

    @Test
    public void shouldSuccessWhenOneLaunch(){
        Map<String, Float> currencyKeyValueMap = customerTestUtils.generateRandomCurrencyKeyValue();
        newCurrencyValueSaver.save(currencyKeyValueMap);
    }

    @Test
    public void shouldSuccessWhenParallelLaunch() throws ExecutionException, InterruptedException {
            Map<String, Float> currencyKeyValueMap = customerTestUtils.generateRandomCurrencyKeyValue();

            CompletableFuture<Void> task = CompletableFuture.runAsync(() -> {
                System.out.println("start");
                try {
                    newCurrencyValueSaver.save(currencyKeyValueMap);
                    Thread.sleep(500);
                }catch (InterruptedException e){
                    throw new RuntimeException(e);
                }
                System.out.println("end");
            });

            CompletableFuture.allOf(task, task, task, task, task).get();

    }
    @Test
    public void shouldSave3TimeStampWhen3DifferentInputData() throws InterruptedException {
        save3TimeStamp();

        List<CurrencyTimeStamp> timeStampList = this.currencyTimeStampRepository.findAll();
        assertEquals(3, timeStampList.size());
    }

    @Test
    public void shouldSave3ChangedCurrencyValuesEntityWhen3DifferentInputData() throws InterruptedException {
        save3TimeStamp();

        List<ChangeCurrencyValuesEvent> changeCurrencyValuesListSize3 = this.changeCurrencyValuesService.findAll()
                .stream().filter(changeCurrencyValues -> changeCurrencyValues.getChangedValues().size() > 1).toList();

        assertEquals(3, changeCurrencyValuesListSize3.size());
    }

    private void save3TimeStamp() throws InterruptedException {
        Map<String, Float> inputData1 = customerTestUtils.generateRandomCurrencyKeyValue();
        newCurrencyValueSaver.save(inputData1);
        Thread.sleep(20);

        Map<String, Float> inputData2 = customerTestUtils.generateRandomCurrencyKeyValue();
        newCurrencyValueSaver.save(inputData2);
        Thread.sleep(20);

        Map<String, Float> inputData3 = customerTestUtils.generateRandomCurrencyKeyValue();
        newCurrencyValueSaver.save(inputData3);
        Thread.sleep(20);
    }
}
