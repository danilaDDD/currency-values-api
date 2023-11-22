package com.jfund.currencyvaluesservice.saver;

import com.jfund.currencyvaluesservice.testutils.CustomerTestUtils;
import com.jfund.jfundclilib.UpdateOrCreateData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
@SpringBootTest
public class NewCurrencyValuesSaverTest {
    private NewCurrencyValueSaver newCurrencyValueSaver;
    private CustomerTestUtils customerTestUtils;
    @Autowired
    public void setNewCurrencyValueSaver(NewCurrencyValueSaver newCurrencyValueSaver) {
        this.newCurrencyValueSaver = newCurrencyValueSaver;
    }

    @Autowired
    public void setCustomerTestUtils(CustomerTestUtils customerTestUtils) {
        this.customerTestUtils = customerTestUtils;
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
    public void shouldSuccessWhenDifferentInputData() throws ExecutionException, InterruptedException {
        CompletableFuture<Void> taskWithInputData1 = CompletableFuture.runAsync(() -> {
            Map<String, Float> inputData = customerTestUtils.generateCurrencyKeys(100);
            newCurrencyValueSaver.save(inputData);
        });

        CompletableFuture<Void> taskWithInputData2 = CompletableFuture.runAsync(() -> {
            Map<String, Float> inputData = customerTestUtils.generateCurrencyKeys(200);
            newCurrencyValueSaver.save(inputData);
        });

        CompletableFuture<Void> taskWithInputData3 = CompletableFuture.runAsync(() -> {
            Map<String, Float> inputData = customerTestUtils.generateCurrencyKeys(100);
            newCurrencyValueSaver.save(inputData);
        });

        CompletableFuture.allOf(taskWithInputData1, taskWithInputData2, taskWithInputData3).get();
    }
}
