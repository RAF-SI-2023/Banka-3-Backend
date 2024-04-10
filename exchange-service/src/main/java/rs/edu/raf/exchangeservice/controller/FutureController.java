package rs.edu.raf.exchangeservice.controller;

import feign.Param;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.edu.raf.exchangeservice.domain.dto.*;
import rs.edu.raf.exchangeservice.domain.model.listing.Future;
import rs.edu.raf.exchangeservice.service.listingService.FutureService;
import rs.edu.raf.exchangeservice.service.myListingService.MyFutureService;
import rs.edu.raf.exchangeservice.service.myListingService.MyStockService;
import rs.edu.raf.exchangeservice.service.orderService.FutureOrderService;
import rs.edu.raf.exchangeservice.service.orderService.StockOrderService;

import java.util.List;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/future")
public class FutureController {
    private final FutureService futureService;
    private final MyFutureService myFutureService;
    private final FutureOrderService futureOrderService;

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

    @GetMapping("/myFuture/getAll")
    @Operation(description = "ova ruta se gadja ako hoces da vidis sve future koje ima Banka u svom vlasnistu")
    public ResponseEntity<?> getAllMyFuture(){
        return ResponseEntity.ok(this.myFutureService.getAll());
    }

    @GetMapping("/futureOrder/getAll")
    @Operation(description = "odavde dobijas sve FutureOrders koji su ikada napravljeni")
    private ResponseEntity<?> getAllFutureOrder(){
        return ResponseEntity.ok(futureOrderService.findAll());
    }

    @GetMapping("/futureOrder/getAll/{id}")
    @Operation(description = "odavde dobijas sve FutureOrders koji je napravio neki employee, saljes employeeId")
    private ResponseEntity<?> getAllFutureOrderByEmployee(@PathVariable Long id){
        return ResponseEntity.ok(futureOrderService.findAllByEmployee(id));
    }

    @GetMapping("/futuresToApprove/getAll")
    @Operation(description = "odavde se dobija lista svih future-a koje SUPERVISOR treba da odobri/odbije")
    private ResponseEntity<?> getAllOrdersToApprove(){
        return ResponseEntity.ok(futureOrderService.getAllOrdersToApprove());
    }

    @GetMapping("/futuresToApprove/approve/{id}")
    @Operation(description = "salje se id FutureOrdera i boolean da li je odobren ili ne")
    private ResponseEntity<?> approveFutureOrder(@PathVariable Long id, @Param("approved") String approved){
        if (approved.equalsIgnoreCase("true")){
            return ResponseEntity.ok(futureOrderService.approveFutureOrder(id, true));
        }
        if (approved.equalsIgnoreCase("false")){
            return ResponseEntity.ok(futureOrderService.approveFutureOrder(id, false));
        }
        return ResponseEntity.badRequest().body("nije dobar boolean");
    }

    @PostMapping("/buyFuture")
    @Operation(description = "ruta koja se gadja prilikom kupovine Future-a")
    public ResponseEntity buyFuture(@RequestBody BuyFutureDto buyFutureDto){
        return ResponseEntity.ok(futureOrderService.buyFuture(buyFutureDto));
    }

    @PostMapping("/sellFuture")
    @Operation(description = "ruta koja se gadja prilikom prodaje Future-a")
    public ResponseEntity sellFuture(@RequestBody SellFutureDto sellFutureDto){
        sellFutureDto.toString();
        return ResponseEntity.ok(this.myFutureService.sellFuture(sellFutureDto));
    }


}
