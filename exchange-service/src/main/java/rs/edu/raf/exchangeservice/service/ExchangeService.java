package rs.edu.raf.exchangeservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import rs.edu.raf.exchangeservice.domain.model.Exchange;
import rs.edu.raf.exchangeservice.repository.ExchangeRepository;
import rs.edu.raf.exchangeservice.service.listingService.TickerService;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExchangeService {
    private final ExchangeRepository exchangeRepository;
    private final TickerService tickerService;
    private final String pathToFile = "exchange-service/src/main/resources/data/exchange_data.csv";

    public List<Exchange> findAll(){
        return this.exchangeRepository.findAll();
    }

    //vraca objekat Exchange na osnovu njegove oznake
    public Exchange findByExchange(String exchange){
        return exchangeRepository.findByExchange(exchange);
    }

    @PostConstruct
    private void loadData(){
        try (BufferedReader br = new BufferedReader(new FileReader(pathToFile))) {
            String line;
            // Skip the header line
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                Exchange exchange = new Exchange();
                exchange.setExchangeName(parts[0]);
                exchange.setExchangeAcronym(parts[1]);
                exchange.setExchange(parts[2]);
                exchange.setCountry(parts[3]);
                String currency = parts[4];
                if (currency.contains("Dollar")){
                    exchange.setCurrency("USD");
                }
                else if (currency.equalsIgnoreCase("Euro")){
                    exchange.setCurrency("EUR");
                }else if (currency.equalsIgnoreCase("BRITISH POUND STERLING")){
                    exchange.setCurrency("GBR");
                }else {
                    exchange.setCurrency("RSD");
                }

                exchange.setTimeZone(parts[5]);

                String[] openTimePool = parts[6].split(":");
                String hoursOpen = openTimePool[0].replace(" ", "");
                LocalTime openTime = LocalTime.of(Integer.parseInt(hoursOpen.replace(String.valueOf(" 0"), "").replace(" ", "")), Integer.parseInt(openTimePool[1]));
                exchange.setOpenTime(timeByBelgrade(exchange.getTimeZone(), openTime));

                String[] closeTimePool = parts[7].split(":");
                Integer hoursClose = Integer.parseInt(closeTimePool[0].replace(" ", ""));
                Integer minutesClose = Integer.parseInt(closeTimePool[1].replace(" ", ""));
                LocalTime closeTime = LocalTime.of(hoursClose, minutesClose);
                exchange.setCloseTime(timeByBelgrade(exchange.getTimeZone(),closeTime));

                this.exchangeRepository.save(exchange); //save in DB
            }

            tickerService.loadData();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //ne dirati ovu lepotu
    private Long timeByBelgrade(String timeZone, LocalTime localTime ){
        ZoneId newYorkZone = ZoneId.of(timeZone);
        ZonedDateTime newYorkDateTime = ZonedDateTime.of(ZonedDateTime.now().toLocalDate(), localTime, newYorkZone);

        ZoneId belgradeZone = ZoneId.of("Europe/Paris");
        ZonedDateTime belgradeDateTime = newYorkDateTime.withZoneSameInstant(belgradeZone);
        return belgradeDateTime.toInstant().toEpochMilli();
    }
}
