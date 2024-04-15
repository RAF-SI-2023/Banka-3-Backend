
package rs.edu.raf.exchangeservice.service.orderService;


import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import rs.edu.raf.exchangeservice.client.BankServiceClient;
import rs.edu.raf.exchangeservice.domain.dto.BuyOptionDto;
import rs.edu.raf.exchangeservice.domain.dto.OptionOrderDto;
import rs.edu.raf.exchangeservice.domain.dto.StockTransactionDto;
import rs.edu.raf.exchangeservice.domain.mappers.OptionMapper;
import rs.edu.raf.exchangeservice.domain.mappers.StockMapper;
import rs.edu.raf.exchangeservice.domain.model.enums.StockOrderStatus;
import rs.edu.raf.exchangeservice.domain.model.enums.StockOrderType;
import rs.edu.raf.exchangeservice.domain.model.listing.Option;
import rs.edu.raf.exchangeservice.domain.model.listing.Stock;
import rs.edu.raf.exchangeservice.domain.model.order.OptionOrder;
import rs.edu.raf.exchangeservice.domain.model.order.StockOrder;
import rs.edu.raf.exchangeservice.repository.ActuaryRepository;
import rs.edu.raf.exchangeservice.repository.listingRepository.OptionRepository;
import rs.edu.raf.exchangeservice.repository.orderRepository.OptionOrderRepository;
import rs.edu.raf.exchangeservice.service.myListingService.MyOptionService;
import rs.edu.raf.exchangeservice.service.myListingService.MyStockService;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
@RequiredArgsConstructor
public class OptionOrderService {



    private final OptionOrderRepository optionOrderRepository;

    private final OptionRepository optionRepository;

    private final ActuaryRepository actuaryRepository;

    private final MyOptionService myOptionService;
    private final BankServiceClient bankServiceClient;

    public CopyOnWriteArrayList<OptionOrder> ordersToBuy = new CopyOnWriteArrayList<>();
    public CopyOnWriteArrayList<OptionOrder> ordersToApprove = new CopyOnWriteArrayList<>();

    public List<OptionOrder> findAll(){
        return optionOrderRepository.findAll();
    }

    public OptionOrder approveOptionOrder(Long id, boolean approved){
        OptionOrder optionOrder = optionOrderRepository.findByOptionOrderId(id);
        if (approved){
            optionOrder.setStatus("PROCESSING");
            this.ordersToApprove.remove(optionOrder);
            this.ordersToBuy.add(optionOrder);
        }else {
            optionOrder.setStatus("REJECTED");
            this.ordersToApprove.remove(optionOrder);
        }
        return this.optionOrderRepository.save(optionOrder);
    }

    public OptionOrderDto buyOption(BuyOptionDto buyOptionDto){

        OptionOrder optionOrder = new OptionOrder();
        optionOrder.setEmployeeId(buyOptionDto.getEmployeeId());
        optionOrder.setSymbol(buyOptionDto.getContractSymbol());
        optionOrder.setPrice(buyOptionDto.getPrice());
        optionOrder.setTicker(buyOptionDto.getTicker());


        if (actuaryRepository.findByEmployeeId(buyOptionDto.getEmployeeId()).isOrderRequest()){
            optionOrder.setStatus("WAITING");
        }else {
            optionOrder.setStatus("PROCESSING");
        }

        if(buyOptionDto.getStopValue() == null)
            optionOrder.setStopValue(0.0);
        else
            optionOrder.setStopValue(buyOptionDto.getStopValue());

        if(buyOptionDto.getLimitValue() == null)
            optionOrder.setLimitValue(0.0);
        else
            optionOrder.setLimitValue(buyOptionDto.getLimitValue());

        //market order
        if (optionOrder.getStopValue() == 0.0 && optionOrder.getLimitValue() == 0.0)
            optionOrder.setType("MARKET");

        //stop order
        if(optionOrder.getStopValue() != 0.0 && optionOrder.getLimitValue() == 0.0)
            optionOrder.setType("STOP");

        //limit order
        if(optionOrder.getStopValue() == 0.0 && optionOrder.getLimitValue() != 0.0)
            optionOrder.setType("LIMIT");

        //stop-limit order
        if(optionOrder.getStopValue() != 0.0 && optionOrder.getLimitValue() != 0.0)
            optionOrder.setType("STOP_LIMIT");

        optionOrder.setMargin(buyOptionDto.isMargine());

        if (optionOrder.getStatus().equalsIgnoreCase("PROCESSING")){
            this.ordersToBuy.add(this.optionOrderRepository.save(optionOrder));
        }else {
            this.ordersToApprove.add(this.optionOrderRepository.save(optionOrder));
        }

        return OptionMapper.INSTANCE.optionOrderToOptionOrderDto(optionOrder);

    }

    @Scheduled(fixedRate = 35000)
    public void executeTask() {

        if (ordersToBuy.isEmpty()) {
            System.out.println("Executing task every 35 seconds, but list to buy is empty :-(");
        } else {
            Random rand = new Random();
            int stockNumber = rand.nextInt(ordersToBuy.size());
            OptionOrder optionOrder = ordersToBuy.get(stockNumber);   //StockOrder koji obradjujemo

            Option option = null;
            Optional<Option> optionalOption = this.optionRepository.findByContractSymbol(optionOrder.getSymbol());
//            Double currentPrice = optionOrder.getPrice();

            if(optionalOption.isPresent())
                option = optionalOption.get();
            else {
                System.out.println("No option available with symbol "+optionOrder.getSymbol()+", restarting in 35 seconds...");
                return;
            }

            double currentPrice = option.getAsk();   //trenutna cena po kojoj kupujemo

            if (optionOrder.getType().equals("MARKET")) {
//                bankServiceClient.startStockTransaction(stockTransactionDto);
                printer(1, currentPrice);
                optionOrder.setStatus("FINISHED");
                ordersToBuy.remove(stockNumber);    //uklanjamo ga iz liste jer je zavrsio

                myOptionService.addAmountToMyOptions(option.getContractSymbol(), 1, optionOrder.getTicker());    //dodajemo kolicinu kupljenih deonica u vlasnistvo banke
            }

            if (optionOrder.getType().equals("LIMIT")){
                if (currentPrice < optionOrder.getLimitValue()){
                    myOptionService.addAmountToMyOptions(optionOrder.getSymbol(), 1, optionOrder.getTicker());    //dodajemo kolicinu kupljenih deonica u vlasnistvo banke
                    optionOrder.setStatus("FINISHED");
                    ordersToBuy.remove(stockNumber);    //uklanjamo ga iz liste jer je zavrsio
                } else {
                    optionOrder.setStatus("FAILED");
                    ordersToBuy.remove(stockNumber);
                }
            }

            if (optionOrder.getType().equals("STOP")){
                if (currentPrice > optionOrder.getStopValue()){
                    optionOrder.setType("MARKET");
                } else {
                    System.out.println("Stop value hasn't been approved ");
                }
            }

            if (optionOrder.getType().equals("STOP_LIMIT")){
                if (currentPrice > optionOrder.getStopValue()){
                    optionOrder.setType("LIMIT");
                } else {
                    System.out.println("Stop value hasn't been approved ");
                }
            }

            this.optionOrderRepository.save(optionOrder); //cuvamo promenjene vrednosti u bazi
        }
    }

    private void printer(int amountToBuy, double currentPrice) {
        System.out.println("Kupljeno: " + amountToBuy + " OPTION, po ceni: " + currentPrice*amountToBuy);
    }
}
