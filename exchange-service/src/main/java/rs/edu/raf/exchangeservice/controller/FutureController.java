package rs.edu.raf.exchangeservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.edu.raf.exchangeservice.domain.dto.buySell.BuyFutureCompanyDto;
import rs.edu.raf.exchangeservice.domain.dto.buySell.BuyFutureDto;
import rs.edu.raf.exchangeservice.domain.dto.listing.FutureDto;
import rs.edu.raf.exchangeservice.domain.model.listing.Future;
import rs.edu.raf.exchangeservice.service.listingService.FutureService;
import rs.edu.raf.exchangeservice.service.myListingService.MyFutureSerivce;
import rs.edu.raf.exchangeservice.service.orderService.FuturOrderService;

import java.util.List;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/future")
public class FutureController {
    private final FutureService futureService;
    private final MyFutureSerivce myFutureService;
    private final FuturOrderService futureOrderService;

    @GetMapping
    @Operation(description = "vracamo sve Future iz baze")
    public ResponseEntity<List<Future>> getAll(){
        return ResponseEntity.ok(futureService.findAll());
    }

    @GetMapping("/{contractName}")
    @Operation(description = "trazi Future po prosledjenom Contractu")
    public ResponseEntity<FutureDto> getByContractName(@PathVariable String contractName){
        return ResponseEntity.ok(this.futureService.findByContractName(contractName));
    }
    @PostMapping("/buyFuture")
    @Operation(description = "ruta koja se gadja prilikom kupovine Future-a")
    public ResponseEntity buyFuture(@RequestBody BuyFutureDto buyFutureDto){
        return ResponseEntity.ok(this.futureOrderService.buyFuture(buyFutureDto));
    }
    @PostMapping("/sellFuture")
    @Operation(description = "ruta koja se gadja prilikom prodaje Future-a")
    public ResponseEntity sellFuture(@RequestBody BuyFutureDto sellFutureDto){
        return ResponseEntity.ok(this.myFutureService.sellMyFuture(sellFutureDto));
    }
    @GetMapping("/myFuture/getAllForCompany/{companyId}")
    @Operation(description = "ova ruta se gadja ako hoces da vidis sve future koje ima neka kompanija u svom vlasnistvu")
    public ResponseEntity<?> getAllMyFutureForCompany(@PathVariable Long companyId){
        return ResponseEntity.ok(this.myFutureService.findAllByCompanyId(companyId));
    }
    @GetMapping("/myFuture/getAllForOtcBuy/{companyId}")
    @Operation(description = "ova ruta se gadja ako hoces da vidis sve future koje mozes da kupis")
    public ResponseEntity<?> getAllForOtcBuy(@PathVariable Long companyId){
        return ResponseEntity.ok(this.myFutureService.findAllForOtcBuy(companyId));
    }
    @PutMapping("/myFuture/makePublic/{myFutureId}")
    @Operation(description = "ova ruta se gadja ako hoces da postavis svoj future kao public")
    public ResponseEntity<?> makeFuturePublic(@PathVariable Long myFutureId){
        return ResponseEntity.ok(this.myFutureService.makeFuturePublic(myFutureId));
    }
    @PutMapping("/myFuture/makePrivate/{myFutureId}")
    @Operation(description = "ova ruta se gadja ako hoces da postavis svoj future kao private")
    public ResponseEntity<?> makeFuturePrivate(@PathVariable Long myFutureId){
        return ResponseEntity.ok(this.myFutureService.makeFuturePrivate(myFutureId));
    }
    @GetMapping("/futureOrder/getAll")
    @Operation(description = "odavde dobijas sve FutureOrders koji su ikada napravljeni")
    private ResponseEntity<?> getAllFutureOrder(){
        return ResponseEntity.ok(futureOrderService.findAll());
    }

    @PostMapping("/buyFutureOtc")
    @Operation(description = "ruta koja se gadja prilikom kreiranja otc ugovora za future")
    public ResponseEntity buyFutureOtc(@RequestBody BuyFutureCompanyDto buyFutureCompanyDto){
        return ResponseEntity.ok(this.futureService.requestToBuyFutureByCompany(buyFutureCompanyDto));
    }
//    @GetMapping("/myFuture/getAll")
//    @Operation(description = "ova ruta se gadja ako hoces da vidis sve future koje ima Banka u svom vlasnistu")
//    public ResponseEntity<?> getAllMyFuture(){
//        return ResponseEntity.ok(this.myFutureService.getAll());
//    }
//
//
//    @GetMapping("/futureOrder/getAll/{id}")
//    @Operation(description = "odavde dobijas sve FutureOrders koji je napravio neki employee, saljes employeeId")
//    private ResponseEntity<?> getAllFutureOrderByEmployee(@PathVariable Long id){
//        return ResponseEntity.ok(futureOrderService.findAllByEmployee(id));
//    }
//
//    @GetMapping("/futuresToApprove/getAll")
//    @Operation(description = "odavde se dobija lista svih future-a koje SUPERVISOR treba da odobri/odbije")
//    private ResponseEntity<?> getAllOrdersToApprove(){
//        return ResponseEntity.ok(futureOrderService.getAllOrdersToApprove());
//    }
//
//    @GetMapping("/futuresToApprove/approve/{id}")
//    @Operation(description = "salje se id FutureOrdera i boolean da li je odobren ili ne")
//    private ResponseEntity<?> approveFutureOrder(@PathVariable Long id, @Param("approved") String approved){
//        if (approved.equalsIgnoreCase("true")){
//            return ResponseEntity.ok(futureOrderService.approveFutureOrder(id));
//        }
//        if (approved.equalsIgnoreCase("false")){
//            return ResponseEntity.ok(futureOrderService.approveFutureOrder(id));
//        }
//        return ResponseEntity.badRequest().body("nije dobar boolean");
//    }
//

}
