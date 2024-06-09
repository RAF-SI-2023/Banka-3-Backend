package rs.edu.raf.exchangeservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;
import rs.edu.raf.exchangeservice.domain.dto.offer.FrontendOfferDto;
import rs.edu.raf.exchangeservice.domain.dto.offer.MyOfferDto;
import rs.edu.raf.exchangeservice.domain.dto.offer.MyStockDto;
import rs.edu.raf.exchangeservice.domain.dto.offer.OfferDto;
import rs.edu.raf.exchangeservice.domain.model.offer.MyOffer;
import rs.edu.raf.exchangeservice.domain.model.offer.Offer;
import rs.edu.raf.exchangeservice.service.Banka4OtcService;

import javax.websocket.server.PathParam;
import java.util.List;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/otcTrade")
public class Banka4OtcController {

    //https://banka-3.si.raf.edu.rs/api/v1/otcTrade

    private final Banka4OtcService banka4OtcService;

    //radi testirano (janko i damir
    @GetMapping("/getStocks")
    @Operation(description = "dohvata stockove nase banke")
    public ResponseEntity<List<MyStockDto>> getMyStocks(){
        return ResponseEntity.ok(this.banka4OtcService.findAllStocks());
    }

    //radi radi testirano (janko i damir)
    @PostMapping("/sendOffer")
    @Operation(description = "primamo ponude od banke 4")
    public ResponseEntity<Offer> receiveOffer(@RequestBody OfferDto dto){
//        this.banka4OtcService.receiveOffer(dto);
        return ResponseEntity.ok(this.banka4OtcService.receiveOffer(dto));

    }

    //radi testirano (janko i damir) FRONTEND NAS
    @GetMapping("/getOffers")
    @Operation(description = "dohvata sve ponude koje smo dobili")
    public ResponseEntity<List<Offer>> getOffers(){
        return ResponseEntity.ok(this.banka4OtcService.findAllOffers());
    }

    //radi testirano (janko i damir) FRONTEND NAS
    @GetMapping("/getBank4Stocks")
    @Operation(description = "fortend dohvata stockove od banke 4")
    public ResponseEntity<List<MyStockDto>> getBank4Stocks(){
        return ResponseEntity.ok(this.banka4OtcService.getAllStocksForBank4());
    }

    //radi testirano (janko i damir) FRONTEND NAS
    @PostMapping("/acceptOffer/{id}")
    @Operation(description = "prihvatamo ponudu")
    public ResponseEntity<Offer> acceptOffer(@PathVariable Long id){
        return ResponseEntity.ok(this.banka4OtcService.acceptOffer(id));
    }

    //radi testirano (janko i damir) FRONTEND NAS
    @PostMapping("/declineOffer/{id}")
    @Operation(description = "odbijamo ponudu")
    public ResponseEntity<Offer> declineOffer(@PathVariable Long id){
        return ResponseEntity.ok(this.banka4OtcService.declineOffer(id));
    }

    //radi testirano (janko i damir) FRONTEND NAS
    @PostMapping("/makeOffer")
    @Operation(description = "sa frontenda stize ponuda koju treba proslediti banci 4")
    public ResponseEntity<MyOffer> makeOffer(@RequestBody FrontendOfferDto frontendOfferDto){
        return ResponseEntity.ok(this.banka4OtcService.makeOffer(frontendOfferDto));
    }

    @PostMapping("/offerAccepted/{id}")
    @Operation(description = "od banke 4 stize poruka da su nam prihvatili ponudu")
    public ResponseEntity<MyOffer> offerAccepted(@PathVariable Long id){
        return ResponseEntity.ok(this.banka4OtcService.offerAccepted(id));
    }

    @PostMapping("/offerDeclined/{id}")
    @Operation(description = "od banke 4 stize poruka da su nam odbili ponudu")
    public ResponseEntity<MyOffer> offerDeclined(@PathVariable Long id){
        return ResponseEntity.ok(this.banka4OtcService.offerDeclined(id));
    }

    // radi testirano (janko i damir) FRONTEND NAS
    @GetMapping("/getMyOffers")
    @Operation(description = "dohvata sve ponude koje smo poslali banci 4")
    public ResponseEntity<List<MyOffer>> getMyOffers(){
        return ResponseEntity.ok(this.banka4OtcService.getMyOffers());
    }
}
