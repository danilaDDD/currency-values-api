package com.jfund.currencyvaluesservice.service;

import com.jfund.currencyvaluesservice.repository.CurrencyKeyRepository;
import com.jfund.currencyvaluesservice.entity.CurrencyKey;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;


@SpringBootTest
@ActiveProfiles("test")
class CurrencyKeyCRUDServiceTest {
    private final static String[] KEYS = {"EURUSD", "EURGBP", "EURJPY", "USDJPY", "USDGBP", "GBPJPY"};
    private final static Comparator<CurrencyKey> KEY_COMPARATOR = Comparator.comparing(CurrencyKey::getKey);


    @Autowired
    private CurrencyKeyRepository currencyKeyRepository;
    @Autowired
    private CurrencyKeyCRUDService crudService;

    @BeforeEach
    void setUp() {
        currencyKeyRepository.deleteAll().block();
    }

    @Test
    void testUpdate_WithNotEmptyExistAndNewEqualsKeys_ShouldNotInsert() {
        List<CurrencyKey> currencyKeyList = buildKeysObjects();
        currencyKeyRepository.saveAll(currencyKeyList).blockLast();

        crudService.updateCurrencyKeys(Flux.fromIterable(currencyKeyList)).block();

        StepVerifier.create(findAllSorted())
                .expectNextSequence(currencyKeyList)
                .verifyComplete();
    }

    @Test
    void testUpdate_WithNotEmptyExistAndNewDifferentKeys_ShouldInsertAllDifferent() {
        List<CurrencyKey> currencyKeyList = buildKeysObjects();
        currencyKeyRepository.saveAll(currencyKeyList).blockLast();

        List<CurrencyKey> updatedCurrencyKeyList = currencyKeyList.stream()
                .map(currencyKey -> new CurrencyKey(currencyKey.getId(), currencyKey.getKey() + "_updated"))
                .toList();

        crudService.updateCurrencyKeys(Flux.fromIterable(updatedCurrencyKeyList)).block();

        currencyKeyList.addAll(updatedCurrencyKeyList);
        List<CurrencyKey> expected = sortedList(currencyKeyList);
        StepVerifier.create(findAllSorted())
                .expectNextSequence(expected)
                .verifyComplete();
    }

    @Test
    void testUpdate_WithEmptyExist_ShouldInsertAll() {
        List<CurrencyKey> currencyKeyList = buildKeysObjects();

        crudService.updateCurrencyKeys(Flux.fromIterable(currencyKeyList)).block();


        StepVerifier.create(crudService.loadCurrencyKeys().sort(KEY_COMPARATOR))
                .expectNextSequence(currencyKeyList)
                .verifyComplete();
    }

    @Test
    void testLoad_WithNotEmpty_ShouldSuccessfully() {
        List<CurrencyKey> currencyKeyList = buildKeysObjects();
        currencyKeyRepository.saveAll(currencyKeyList).blockLast();

        StepVerifier.create(crudService.loadCurrencyKeys().sort(KEY_COMPARATOR))
                .expectNextSequence(currencyKeyList)
                .verifyComplete();
    }

    @Test
    void testLoad_WithEmpty_ShouldThrowException() {
        StepVerifier.create(crudService.loadCurrencyKeys().sort(KEY_COMPARATOR))
                .expectError(RuntimeException.class)
                .verify();
    }

    private List<CurrencyKey> buildKeysObjects() {
        return Stream.of(KEYS)
                .map(CurrencyKey::new)
                .sorted(KEY_COMPARATOR)
                .toList();
    }

    private Flux<CurrencyKey> findAllSorted() {
        return currencyKeyRepository.findAll(Sort.by(Sort.Direction.ASC,"key"));
    }

    private List<CurrencyKey> sortedList(List<CurrencyKey> currencyKeyList) {
        return currencyKeyList.stream()
                .sorted(KEY_COMPARATOR)
                .toList();
    }


}