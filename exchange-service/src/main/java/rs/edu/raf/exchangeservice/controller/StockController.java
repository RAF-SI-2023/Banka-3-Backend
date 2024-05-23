package rs.edu.raf.exchangeservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import feign.Param;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.edu.raf.exchangeservice.domain.dto.MakePublicStockDto;
import rs.edu.raf.exchangeservice.domain.dto.buySell.BuySellStockDto;
import rs.edu.raf.exchangeservice.domain.dto.buySell.BuyStockCompanyDto;
import rs.edu.raf.exchangeservice.domain.dto.buySell.BuyStockUserOTCDto;
import rs.edu.raf.exchangeservice.domain.dto.listing.StockDto;
import rs.edu.raf.exchangeservice.domain.dto.StockOrderDto;
import rs.edu.raf.exchangeservice.domain.model.listing.Stock;
import rs.edu.raf.exchangeservice.service.listingService.StockService;
import rs.edu.raf.exchangeservice.service.myListingService.MyStockService;
import rs.edu.raf.exchangeservice.service.orderService.StockOrderService;

import java.util.List;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/stock")
public class StockController {
    private final StockService stockService;
    private final MyStockService myStockService;
    private final StockOrderService stockOrderService;

    @GetMapping
    @Operation(description = "vraca ste Stock iz baze")
    public ResponseEntity<List<StockDto>> getAll(){
        return ResponseEntity.ok(stockService.findAll());
    }

    @GetMapping("/{ticker}")
    @Operation(description = "trazi Stock po prosledjenom Ticker")
    public ResponseEntity<StockDto> getByTicker(@PathVariable String ticker){
        return ResponseEntity.ok(this.stockService.findByTicker(ticker));
    }

    @GetMapping("/refresh")
    @Operation(description = "kada korisnik zatrazi refresh podataka")
    public ResponseEntity<List<Stock>> getAllRefreshed() throws JsonProcessingException {
        return ResponseEntity.ok(this.stockService.findAllRefreshed());
    }

    @GetMapping("/myStock/getAll")
    @Operation(description = "ova ruta se gadja ako hoces da vidis sve deonice koje ima Banka u svom vlasnistu")
    public ResponseEntity<?> getAllMyStock(){
        return ResponseEntity.ok(this.myStockService.getAll());
    }

    @GetMapping("/myStock/getAllForUser")
    @Operation(description = "ova ruta se gadja ako hoces da vidis sve deonice koje ima neki korisnik u svom vlasnistu")
    public ResponseEntity<?> getAllMyStockForUser(@Param("userId") Long userId){
        return ResponseEntity.ok(this.myStockService.getAllForUser(userId));
    }

    @GetMapping("/myStock/getAllForCompany")
    @Operation(description = "ova ruta se gadja ako hoces da vidis sve deonice koje ima neka kompanija (i banka) u svom vlasnistu")
    public ResponseEntity<?> getAllMyStockForCompany(@Param("companyId") Long companyId){
        return ResponseEntity.ok(this.myStockService.getAllForCompany(companyId));
    }

    @GetMapping("/myStock/getAllForUserOtcBuy/{userId}")
    @Operation(description = "ova ruta se gadja ako hoces da vidis sve deonice koje mozes da kupis kao korisnik")
    public ResponseEntity<?> getAllForUserOtcBuy(@PathVariable Long userId){
        return ResponseEntity.ok(this.myStockService.getAllForUserOtcBuy(userId));
    }

    @GetMapping("/myStock/getAllForCompanyOtcBuy/{companyId}")
    @Operation(description = "ova ruta se gadja ako hoces da vidis sve deonice koje mozes da kupis kao kompanija")
    public ResponseEntity<?> getAllForCompanyOtcBuy(@PathVariable Long companyId){
        return ResponseEntity.ok(this.myStockService.getAllForCompanyOtcBuy(companyId));
    }

    @PutMapping("/myStock/makeCompanyStockPublic")
    @Operation(description = "ova ruta postavlja deo donica kompanije da budu javne, ostala kolicina postaje privatna")
    public ResponseEntity<?> makeCompanyStockPublic(@RequestBody MakePublicStockDto makePublicStockDto){
        return ResponseEntity.ok(this.myStockService.makeCompanyStockPublic(makePublicStockDto));
    }

    @PutMapping("/myStock/makeUserStockPublic")
    @Operation(description = "ova ruta postavlja deo donica korisnika da budu javne, ostala kolicina postaje privatna")
    public ResponseEntity<?> makeUserStockPublic(@RequestBody MakePublicStockDto makePublicStockDto){
        return ResponseEntity.ok(this.myStockService.makeUserStockPublic(makePublicStockDto));
    }

    @GetMapping("/stockOrder/getAll")
    @Operation(description = "odavde dobijas sve StockOrders koji su ikada napravljeni")
    private ResponseEntity<?> getAllStockOrder(){
        return ResponseEntity.ok(stockOrderService.findAll());
    }

    @GetMapping("/stockOrder/getAll/{id}")
    @Operation(description = "odavde dobijas sve StockOrders koji je napravio neki employee, saljes employeeId")
    private ResponseEntity<?> getAllStockOrderByEmployee(@PathVariable Long id){
        return ResponseEntity.ok(stockOrderService.findAllByEmployee(id));
    }

    @GetMapping("/ordersToApprove/getAll")
    @Operation(description = "odavde se dobija lista svih ordera koje SUPERVISOR treba da odobri/odbije")
    private ResponseEntity<?> getAllOrdersToApprove(){
        return ResponseEntity.ok(stockOrderService.getAllOrdersToApprove());
    }

    @GetMapping("/ordersToApprove/approve/{id}")
    @Operation(description = "salje se id StockOrdera i boolean da li je odobren ili ne")
    private ResponseEntity<?> approveStockOrder(@PathVariable Long id, @Param("approved") String approved){
        if (approved.equalsIgnoreCase("true")){
            return ResponseEntity.ok(stockOrderService.approveStockOrder(id, true));
        }
        if (approved.equalsIgnoreCase("false")){
            return ResponseEntity.ok(stockOrderService.approveStockOrder(id, false));
        }
        return ResponseEntity.badRequest().body("nije dobar boolean");
    }

    @PostMapping("/buyStock")
    @Operation(description = "ruta koja se gadja prilikom kupovine Stocks")
    public ResponseEntity<StockOrderDto> buyStock(@RequestBody BuySellStockDto buySellStockDto){
        return ResponseEntity.ok(stockOrderService.buyStock(buySellStockDto));
    }

    @PostMapping("/buyCompanyStockOtc")
    @Operation(description = "Firma salje zahtev drugoj firmi za kupovinu stock-a")
    public ResponseEntity<?> buyCompanyStockOtc(@RequestBody BuyStockCompanyDto buyStockCompanyDto) {
        if(this.stockOrderService.buyCompanyStockOtc(buyStockCompanyDto)){
            return ResponseEntity.ok().build();
        }else{
            //nema para
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
        }
    }

    @PostMapping("/buyUserStockOtc")
    @Operation(description = "Korisnik salje zahtev drugom korisniku za kupovinu stock-a")
    public ResponseEntity<?> buyUserStockOtc(@RequestBody BuyStockUserOTCDto buyStockUserOTCDto) {
        if(this.stockOrderService.buyUserStockOtc(buyStockUserOTCDto)){
            return ResponseEntity.ok().build();
        }else{
            //nema para
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
        }
    }


    @PostMapping("/sellStock")
    @Operation(description = "ruta koja se gadja prilikom prodaje Stocks")
    public ResponseEntity<?> sellStock(@RequestBody BuySellStockDto sellStockDto){
        this.myStockService.sellStock(sellStockDto);
        return ResponseEntity.ok().build();
    }
}
