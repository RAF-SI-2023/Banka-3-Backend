package rs.edu.raf.exchangeservice.service.myListingService;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import rs.edu.raf.exchangeservice.domain.dto.SellOptionDto;
import rs.edu.raf.exchangeservice.domain.model.listing.Option;
import rs.edu.raf.exchangeservice.domain.model.myListing.MyOption;
import rs.edu.raf.exchangeservice.domain.model.order.OptionOrderSell;
import rs.edu.raf.exchangeservice.repository.listingRepository.OptionRepository;
import rs.edu.raf.exchangeservice.repository.myListingRepository.MyOptionRepository;
import rs.edu.raf.exchangeservice.repository.orderRepository.OptionOrderSellRepository;

import java.util.Optional;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class MyOptionService {

    private final MyOptionRepository myOptionRepository;
    private final OptionOrderSellRepository optionOrderSellRepository;
    private final OptionRepository optionRepository;

    public CopyOnWriteArrayList<OptionOrderSell> ordersToSell = new CopyOnWriteArrayList<>();

    @Transactional
    public void addAmountToMyOptions(String contractSymbol, Integer amount, String ticker) {
        Option option = null;
        Optional<Option> optionalOption = optionRepository.findByContractSymbol(contractSymbol);
        if(!optionalOption.isEmpty()){
            option = optionalOption.get();
        }else{
            return;
        }
        MyOption myOption = new MyOption();
        myOption.setBoughtPrice(option.getPrice());
        myOption.setContractSymbol(option.getContractSymbol());
        myOption.setAmount(1);
        myOption.setTicker(ticker);
        this.myOptionRepository.save(myOption);
    }

    public String sellOption(SellOptionDto sellOptionDto){
        MyOption myOption = myOptionRepository.findByTicker(sellOptionDto.getTicker());

        OptionOrderSell optionOrderSell = new OptionOrderSell();
        optionOrderSell.setEmployeeId(sellOptionDto.getEmployeeId());
        optionOrderSell.setTicker(sellOptionDto.getTicker());
        optionOrderSell.setPrice(sellOptionDto.getPrice());
        optionOrderSell.setMargin(sellOptionDto.isMargine());

        //market order
        if (sellOptionDto.getStopValue() == 0.0 && sellOptionDto.getLimitValue() == 0.0){
            optionOrderSell.setLimitValue(0.0);
            optionOrderSell.setStopValue(0.0);
            optionOrderSell.setType("MARKET");
        }

        //stop order
        if(sellOptionDto.getStopValue() != 0.0 && sellOptionDto.getLimitValue() == 0.0){
            optionOrderSell.setLimitValue(0.0);
            optionOrderSell.setStopValue(sellOptionDto.getStopValue());
            optionOrderSell.setType("STOP");
        }

        //limit order
        if(sellOptionDto.getStopValue() == 0.0 && sellOptionDto.getLimitValue() != 0.0){
            optionOrderSell.setLimitValue(sellOptionDto.getLimitValue());
            optionOrderSell.setStopValue(0.0);
            optionOrderSell.setType("LIMIT");
        }

        //stop-limit order
        if(sellOptionDto.getStopValue() != 0.0 && sellOptionDto.getLimitValue() != 0.0){
            optionOrderSell.setLimitValue(sellOptionDto.getLimitValue());
            optionOrderSell.setStopValue(sellOptionDto.getStopValue());
            optionOrderSell.setType("STOP-LIMIT");
        }

        optionOrderSell.setStatus("PROCESSING");
        this.ordersToSell.add(optionOrderSellRepository.save(optionOrderSell));
        return "UBACENO U ORDER";

    }


    @Scheduled(fixedRate = 35000)
    public void executeTask(){
        if(ordersToSell.isEmpty()){
            System.out.println("Executing task every 35 seconds, but list to sell is empty :-(");
        }
        else {
            Random rand = new Random();
            int optionNumber = rand.nextInt(ordersToSell.size());
            OptionOrderSell optionOrderSell = ordersToSell.get(optionNumber); //OptionOrder koji obradjujemo


            MyOption myOption = this.myOptionRepository.findByContractSymbol(optionOrderSell.getSymbol());
            Double currentPrice = optionOrderSell.getPrice();


            if (optionOrderSell.getType().equalsIgnoreCase("MARKET")){
                //TODO: poslati currentPrice*1 ka Transaction-service
                optionOrderSell.setStatus("FINISHED");
                ordersToSell.remove(optionNumber);    //uklanjamo ga iz liste jer je zavrsio

            }
            if (optionOrderSell.getType().equalsIgnoreCase("LIMIT")){
                if (currentPrice > optionOrderSell.getLimitValue()){
                    //TODO: poslati currentPrice*1 ka Transaction-service
                    optionOrderSell.setStatus("FINISHED");
                    ordersToSell.remove(optionNumber);    //uklanjamo ga iz liste jer je zavrsio
                }
                else {
                    optionOrderSell.setStatus("FAILED");
                    ordersToSell.remove(optionNumber);
                }
            }

            if (optionOrderSell.getType().equalsIgnoreCase("STOP")){
                if (currentPrice < optionOrderSell.getStopValue()){
                    optionOrderSell.setType("MARKET");
                }
                else {
                    System.out.println("Stop value hasn't been approved ");
                }
            }

            if (optionOrderSell.getType().equalsIgnoreCase("STOP-LIMIT")){
                if (currentPrice < optionOrderSell.getStopValue()){
                    optionOrderSell.setType("LIMIT");
                }
                else {
                    System.out.println("Stop value hasn't been approved ");
                }
            }

            this.optionOrderSellRepository.save(optionOrderSell); //cuvamo promenjene vrednosti u bazi
        }
    }

}
