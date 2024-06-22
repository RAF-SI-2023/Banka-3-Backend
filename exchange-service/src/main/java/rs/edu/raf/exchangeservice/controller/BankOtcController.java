package rs.edu.raf.exchangeservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.edu.raf.exchangeservice.domain.dto.offer.FrontendOfferDto;
import rs.edu.raf.exchangeservice.domain.dto.offer.MyStockDto;
import rs.edu.raf.exchangeservice.domain.dto.offer.OfferDto;
import rs.edu.raf.exchangeservice.domain.model.listing.BankOTCStock;
import rs.edu.raf.exchangeservice.domain.model.offer.MyOffer;
import rs.edu.raf.exchangeservice.domain.model.offer.Offer;
import rs.edu.raf.exchangeservice.service.BankOtcService;

import java.util.List;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/otcTrade")
public class BankOtcController {
    private final BankOtcService bankOtcService;
    ////

    @GetMapping("/getOurStocks")
    @Operation(description = "dohvata stockove nase banke")
    public ResponseEntity<List<MyStockDto>> getMyStocks(){
        return ResponseEntity.ok(this.bankOtcService.findAllStocks());
    }

    @PostMapping("/sendOffer/bank1")
    @Operation(description = "primamo ponude od banke 1")
    public ResponseEntity<Offer> receiveOfferBank1(@RequestBody OfferDto dto){
        return ResponseEntity.ok(bankOtcService.receiveOffer(dto,1));
    }

    @PostMapping("/sendOffer/bank2")
    @Operation(description = "primamo ponude od banke 2")
    public ResponseEntity<Offer> receiveOfferBank2(@RequestBody OfferDto dto){
        return ResponseEntity.ok(bankOtcService.receiveOffer(dto,2));
    }

    @PostMapping("/sendOffer/bank4")
    @Operation(description = "primamo ponude od banke 4")
    public ResponseEntity<Offer> receiveOfferBank4(@RequestBody OfferDto dto){
        return ResponseEntity.ok(bankOtcService.receiveOffer(dto,4));
    }

    @PostMapping("/sendOffer/bank5")
    @Operation(description = "primamo ponude od banke 5")
    public ResponseEntity<Offer> receiveOfferBank5(@RequestBody OfferDto dto){
        return ResponseEntity.ok(bankOtcService.receiveOffer(dto,5));
    }

    @PostMapping("/offerAccepted/bank1/{id}")
    @Operation(description = "od banke 1 stize poruka da su nam prihvatili ponudu")
    public ResponseEntity<MyOffer> offerAcceptedBank1(@PathVariable Long id){
        if (bankOtcService.offerAccepted(id)){
            return ResponseEntity.ok().build();
        }else {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/offerDeclined/bank1/{id}")
    @Operation(description = "od banke 1 stize poruka da su nam odbili ponudu")
    public ResponseEntity<MyOffer> offerDeclinedBank1(@PathVariable Long id){
        if (bankOtcService.offerDeclined(id)){
            return ResponseEntity.ok().build();
        }else {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/offerAccepted/bank2/{id}")
    @Operation(description = "od banke 2 stize poruka da su nam prihvatili ponudu")
    public ResponseEntity<MyOffer> offerAcceptedBank2(@PathVariable Long id){
        if (bankOtcService.offerAccepted(id)){
            return ResponseEntity.ok().build();
        }else {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/offerDeclined/bank2/{id}")
    @Operation(description = "od banke 2 stize poruka da su nam odbili ponudu")
    public ResponseEntity<MyOffer> offerDeclinedBank2(@PathVariable Long id){
        if (bankOtcService.offerDeclined(id)){
            return ResponseEntity.ok().build();
        }else {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/offerAccepted/bank4/{id}")
    @Operation(description = "od banke 4 stize poruka da su nam prihvatili ponudu")
    public ResponseEntity<MyOffer> offerAcceptedBank4(@PathVariable Long id){
        if (bankOtcService.offerAccepted(id)){
            return ResponseEntity.ok().build();
        }else {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/offerDeclined/bank4/{id}")
    @Operation(description = "od banke 4 stize poruka da su nam odbili ponudu")
    public ResponseEntity<MyOffer> offerDeclinedBank4(@PathVariable Long id){
        if (bankOtcService.offerDeclined(id)){
            return ResponseEntity.ok().build();
        }else {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/offerAccepted/bank5/{id}")
    @Operation(description = "od banke 5 stize poruka da su nam prihvatili ponudu")
    public ResponseEntity<MyOffer> offerAcceptedBank5(@PathVariable Long id){
        if (bankOtcService.offerAccepted(id)){
            return ResponseEntity.ok().build();
        }else {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/offerDeclined/bank5/{id}")
    @Operation(description = "od banke 5 stize poruka da su nam odbili ponudu")
    public ResponseEntity<MyOffer> offerDeclinedBank5(@PathVariable Long id){
        if (bankOtcService.offerDeclined(id)){
            return ResponseEntity.ok().build();
        }else {
            return ResponseEntity.badRequest().build();
        }
    }

    ///////////////////////FRONTEND/////////////////////////////////////////////////

    @GetMapping("/getBanksStocks")
    @Operation(description = "frontend dohvata sve stocks koje su u ponudi za OTC od drugih banaka")
    public ResponseEntity<List<BankOTCStock>> getBanksStocks(){
        return ResponseEntity.ok(this.bankOtcService.getAllStocksForBanks());
    }

    @GetMapping("/getOffers")
    @Operation(description = "frontend dohvata sve ponude od svih banaka koje smo dobili")
    public ResponseEntity<List<Offer>> getOffers(){
        return ResponseEntity.ok(this.bankOtcService.findAllOffers());
    }

    @GetMapping("/getOurOffers")
    @Operation(description = "dohvata sve ponude koje smo poslali bankama")
    public ResponseEntity<List<MyOffer>> getMyOffers(){
        return ResponseEntity.ok(this.bankOtcService.getMyOffers());
    }

    @PutMapping("/refresh")
    @Operation(description = "sa frontenda, da se osveze Stock-ovi drugih banaka za OTC")
    public ResponseEntity<Offer> refreshOTC(){
        this.bankOtcService.getBankStocks();
        return ResponseEntity.ok().build();
    }

    @PostMapping("/makeOffer")
    @Operation(description = "sa frontenda stize ponuda koju treba proslediti odgovarajucoj banci")
    public ResponseEntity<MyOffer> makeOffer(@RequestBody FrontendOfferDto frontendOfferDto){
        if (this.bankOtcService.makeOffer(frontendOfferDto)){
            return ResponseEntity.ok().build();
        }else {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/acceptOffer/{id}")
    @Operation(description = "sa frontenda nam stize koju ponudu prihvatamo, id je offer u nasoj bazi")
    public ResponseEntity<Offer> acceptOffer(@PathVariable Long id){
        if (this.bankOtcService.acceptOffer(id)){
            return ResponseEntity.ok().build();
        }else {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/declineOffer/{id}")
    @Operation(description = "sa frontenda nam stize koju ponudu odbijamo, id je offer u nasoj bazi")
    public ResponseEntity<Offer> declineOffer(@PathVariable Long id){
        if (this.bankOtcService.declineOffer(id)){
            return ResponseEntity.ok().build();
        }else {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/deleteMyOffer/{id}")
    @Operation(description = "sa fronta nam kaze da treba da obrisemo neku ponudu koju smo mi poslali, id je iz nase baze")
    public ResponseEntity<?> deleteMyOffer(@PathVariable Long id){
        if (this.bankOtcService.deleteMyOffer(id)){
            return ResponseEntity.ok().build();
        }else {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/deleteOffer/{id}")
    @Operation(description = "sa fronta nam kaze da treba da obrisemo neku ponudu koja nam je stigla, id je iz nase baze")
    public ResponseEntity<?> deleteOffer(@PathVariable Long id){
        if (this.bankOtcService.deleteOffer(id)){
            return ResponseEntity.ok().build();
        }else {
            return ResponseEntity.badRequest().build();
        }
    }
}
