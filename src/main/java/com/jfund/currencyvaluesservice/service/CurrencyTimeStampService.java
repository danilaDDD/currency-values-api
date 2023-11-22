package com.jfund.currencyvaluesservice.service;

import com.jfund.currencyvaluesservice.entity.CurrencyTimeStamp;
import com.jfund.currencyvaluesservice.repository.CurrencyTimeStampRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CurrencyTimeStampService {
    private CurrencyTimeStampRepository timeStampRepository;
    @Autowired
    public void setTimeStampRepository(CurrencyTimeStampRepository timeStampRepository) {
        this.timeStampRepository = timeStampRepository;
    }
    //FIXME: change to method repository only
    public Optional<CurrencyTimeStamp> findLastTimeStamp(){
        List<CurrencyTimeStamp> timeStampList = timeStampRepository.findByOrderByDateTimeDesc(PageRequest.of(0, 1));
        return !timeStampList.isEmpty() ? Optional.of(timeStampList.get(0)) :Optional.empty();
    }
    public void saveEntity(CurrencyTimeStamp entity){
        timeStampRepository.save(entity);
    }

    public void saveAll(Iterable<CurrencyTimeStamp> entities){
        timeStampRepository.saveAll(entities);
    }
}
