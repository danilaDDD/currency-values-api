package com.jfund.currencyvaluesservice.saver;

import com.jfund.currencyvaluesservice.entity.CurrencyTimeStamp;
import com.jfund.currencyvaluesservice.repository.CurrencyTimeStampRepository;
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
    @BeforeEach
    public void beforeEach(){
        customerTestUtils.dropCurrencyValuesTable();
    }

    @Test
    public void shouldSuccessWhenOneLaunch(){
        Map<String, Float> currencyKeyValueMap = customerTestUtils.generateCurrencyKeys(100);
        newCurrencyValueSaver.save(currencyKeyValueMap);
    }

    @Test
    public void shouldSuccessWhenParallelLaunch() throws ExecutionException, InterruptedException {
            Map<String, Float> currencyKeyValueMap = customerTestUtils.generateCurrencyKeys(100);

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
    public void shouldSave3TimeStampWhenDifferentInputData() throws ExecutionException, InterruptedException {

        Map<String, Float> inputData1 = customerTestUtils.generateCurrencyKeys(100);
        newCurrencyValueSaver.save(inputData1);
        Thread.sleep(20);

        Map<String, Float> inputData2 = customerTestUtils.generateCurrencyKeys(100);
        newCurrencyValueSaver.save(inputData2);
        Thread.sleep(20);

        Map<String, Float> inputData3 = customerTestUtils.generateCurrencyKeys(100);
        newCurrencyValueSaver.save(inputData3);
        Thread.sleep(20);


        List<CurrencyTimeStamp> timeStampList = this.currencyTimeStampRepository.findAll();
        assertEquals(3, timeStampList.size());

    }
}
