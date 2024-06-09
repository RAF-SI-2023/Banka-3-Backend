package rs.edu.raf.exchangeservice.client;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import rs.edu.raf.exchangeservice.domain.dto.bank.BankTransactionDto;
import rs.edu.raf.exchangeservice.domain.dto.bank.CheckAccountBalanceDto;
import rs.edu.raf.exchangeservice.domain.dto.bank.CompanyOtcDto;
import rs.edu.raf.exchangeservice.domain.dto.bank.UserOtcDto;

@FeignClient(name = "bankServiceClient", url = "${bankServiceLocation}")
public interface BankServiceClient {

    @PostMapping(value = "/transaction/stockBuyTransaction",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "kada banka kupuje nesto od Exchange")
    ResponseEntity<?> stockBuyTransaction (@RequestBody BankTransactionDto bankTransactionDto);

    @PostMapping(value = "/transaction/stockSellTransaction",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "kada banka prodaje nesto Exchange-u")
    ResponseEntity<?> stockSellTransaction (@RequestBody BankTransactionDto bankTransactionDto);

    @GetMapping("/getByCompany/{companyId}")
    @Operation(description = "Uzimamo firmu po id")
    ResponseEntity<?> getByCompanyId(@PathVariable Long companyId);

    @PostMapping(value = "/transaction/otcUserTransaction",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "user kupuje ili prodaje od drugog user-a")
    ResponseEntity<?> otcUserTransaction (@RequestBody UserOtcDto userOtcDto);

    @PostMapping(value = "/transaction/otcCompanyTransaction",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "kompanija kupuje ili prodaje od kompanije")
    ResponseEntity<?> otcBankTransaction (@RequestBody CompanyOtcDto companyOtcDto);

    @PostMapping(value = "/account/checkAccountBalanceUser",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Provera stanja racuna racuna korisnika")
    ResponseEntity<?> checkAccountBalanceUser (@RequestBody CheckAccountBalanceDto checkAccountBalanceDto);

    @PostMapping(value = "/account/checkAccountBalanceCompany",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Provera stanja racuna racuna kompanije")
    ResponseEntity<?> checkAccountBalanceCompany (@RequestBody CheckAccountBalanceDto checkAccountBalanceDto);
}
