package rs.edu.raf.userservice.e2e.byStoks;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.client.RestTemplate;

import rs.edu.raf.userservice.domain.dto.login.LoginRequest;
import rs.edu.raf.userservice.domain.dto.user.UserDto;
import rs.edu.raf.userservice.e2e.byStoks.helper.*;
import rs.edu.raf.userservice.integration.LoginResponseForm;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class BuyStoksTestsSteps extends BuyStoksTestsConfig{

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private String jwtToken;

    private UserDto userDto;
    private String adminJwtToken;
    private UserAccountDto userAccountDtoBeforeCredit;
    @Given("Korisnik se loguje na aplikaciju sa email {string} i sifrom {string}")
    public void korisnikSeLogujeNaAplikacijuSaEmailISifrom(String email, String password) {
        try{
            LoginRequest loginRequest = new LoginRequest();
            loginRequest.setEmail(email);
            loginRequest.setPassword(password);

            LoginRequest adminRequest = new LoginRequest();
            adminRequest.setEmail("admin@admin.com");
            adminRequest.setPassword("admin1234");

            String adminLoginRequestJson = objectMapper.writeValueAsString(adminRequest);

            ResultActions adminActions = mockMvc.perform(
                    post("/api/v1/employee/auth/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .content(adminLoginRequestJson)

            ).andExpect(status().isOk());

            MvcResult adminResult = adminActions.andReturn();
            String loginResponse = adminResult.getResponse().getContentAsString();
            LoginResponseForm loginResponseForm = objectMapper.readValue(loginResponse, LoginResponseForm.class);
            adminJwtToken = loginResponseForm.getJwt();

            String loginRequestJson = objectMapper.writeValueAsString(loginRequest);

            ResultActions resultActions = mockMvc.perform(
                    post("/api/v1/user/auth/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .content(loginRequestJson)

            ).andExpect(status().isOk());

            ResultActions findbyEmail = mockMvc.perform(
                    get("/api/v1/user/findByEmail/" + email)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + adminJwtToken)
            ).andExpect(status().isOk());

            MvcResult result = findbyEmail.andReturn();
            String response = result.getResponse().getContentAsString();
            userDto = objectMapper.readValue(response, UserDto.class);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }


    @And("korisnik cekira balans na svom {string} racunu")
    public void korisnikCekiraBalansNaSvomRacunu(String mark) throws JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:8082/api/v1/account/getByUser/" + userDto.getId();

        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            String responseBody = response.getBody();
            List<UserAccountDto> accountDtos = objectMapper.readValue(responseBody, new TypeReference<List<UserAccountDto>>(){});
            for (UserAccountDto userAccountDto: accountDtos) {
                if (userAccountDto.getCurrency().getMark().equals(mark)) {
                    userAccountDtoBeforeCredit = userAccountDto;
                }
            }
        } else {
            fail(response.getStatusCode().toString());
        }
    }

    @When("korisnik salje zahtev za kredit")
    public void korisnikSaljeZahtevZaKredit() {
        CreditRequestCreateDto creditRequestCreateDto = new CreditRequestCreateDto();
        creditRequestCreateDto.setAmount(1000.0);
        creditRequestCreateDto.setPaymentPeriod(12);
        creditRequestCreateDto.setUserId(userDto.getId());
        creditRequestCreateDto.setAccountNumber(userAccountDtoBeforeCredit.getAccountNumber());
        creditRequestCreateDto.setName("Kes kredit");
        creditRequestCreateDto.setEmployed(true);
        creditRequestCreateDto.setApplianceReason("Kredit za stan");
        creditRequestCreateDto.setCurrencyMark(userAccountDtoBeforeCredit.getCurrency().getMark());
        creditRequestCreateDto.setDateOfEmployment(System.currentTimeMillis());

        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:8082/api/v1/credit-request";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<CreditRequestCreateDto> requestEntity = new HttpEntity<>(creditRequestCreateDto, headers);
        System.out.println(requestEntity.getBody().toString());

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            System.out.println(response.getBody());
        } else {
            fail(response.getStatusCode().toString());
        }
    }

    @Then("Zaposleni se loguje na aplikaciju i odobrava zahtev")
    public void zaposleniSeLogujeNaAplikacijuIOdobravaZahtev() throws JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:8082/api/v1/credit-request";

        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            String responseBody = response.getBody();
            CreditRequestDto creditReqDto;
            List<CreditRequestDto> creditRequestCreateDtos = objectMapper.readValue(responseBody, new TypeReference<List<CreditRequestDto>>(){});
            creditReqDto = creditRequestCreateDtos.stream().max((c1, c2) -> c1.getCreditRequestId().compareTo(c2.getCreditRequestId())).get();


            ProcessCreditRequestDto processCreditRequestDto = new ProcessCreditRequestDto();
            processCreditRequestDto.setAccepted(true);
            processCreditRequestDto.setCreditRequestId(creditReqDto.getCreditRequestId());
            processCreditRequestDto.setEmployeeId(1l);


            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<ProcessCreditRequestDto> requestEntity = new HttpEntity<>(processCreditRequestDto, headers);
            System.out.println(requestEntity.getBody().toString());

            ResponseEntity<String> responseCredit = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, String.class);



        } else {
            fail(response.getStatusCode().toString());
        }
    }

    @And("korisnik cekira balans")
    public void korisnikCekiraBalans() throws JsonProcessingException, InterruptedException {
        Thread.sleep(35000);
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:8082/api/v1/account/getByUser/" + userDto.getId();

        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            String responseBody = response.getBody();
            UserAccountDto userAccountAfterCredit = null;
            List<UserAccountDto> accountDtos = objectMapper.readValue(responseBody, new TypeReference<List<UserAccountDto>>(){});
            for (UserAccountDto userAccountDto: accountDtos) {
                if (userAccountDto.getCurrency().getMark().equals("EUR")) {
                    userAccountAfterCredit = userAccountDto;
                }
            }

            assertTrue(userAccountDtoBeforeCredit.getAvailableBalance().compareTo(userAccountAfterCredit.getAvailableBalance()) == -1);
        } else {
            fail(response.getStatusCode().toString());
        }

    }


    private UserDto userCardDto;
    private UserAccountDto userAccountDtoBeforeWithdraw;
    private CardDto cardDto;
    @Given("korisnik se loguje {string} i lozinkom {string}")
    public void korisnikSeLogujeILozinkom(String email, String password) {
        try{
            LoginRequest loginRequest = new LoginRequest();
            loginRequest.setEmail(email);
            loginRequest.setPassword(password);

            LoginRequest adminRequest = new LoginRequest();
            adminRequest.setEmail("admin@admin.com");
            adminRequest.setPassword("admin1234");

            String adminLoginRequestJson = objectMapper.writeValueAsString(adminRequest);

            ResultActions adminActions = mockMvc.perform(
                    post("/api/v1/employee/auth/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .content(adminLoginRequestJson)

            ).andExpect(status().isOk());

            MvcResult adminResult = adminActions.andReturn();
            String loginResponse = adminResult.getResponse().getContentAsString();
            LoginResponseForm loginResponseForm = objectMapper.readValue(loginResponse, LoginResponseForm.class);
            adminJwtToken = loginResponseForm.getJwt();

            String loginRequestJson = objectMapper.writeValueAsString(loginRequest);

            ResultActions resultActions = mockMvc.perform(
                    post("/api/v1/user/auth/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .content(loginRequestJson)

            ).andExpect(status().isOk());

            ResultActions findbyEmail = mockMvc.perform(
                    get("/api/v1/user/findByEmail/" + email)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + adminJwtToken)
            ).andExpect(status().isOk());

            MvcResult result = findbyEmail.andReturn();
            String response = result.getResponse().getContentAsString();
            userCardDto = objectMapper.readValue(response, UserDto.class);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @And("korisnik cekira balans na dinarskom racunu")
    public void korisnikCekiraBalansNaDinarskomRacunu() throws JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:8082/api/v1/account/getByUser/" + userCardDto.getId();

        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            String responseBody = response.getBody();
            List<UserAccountDto> accountDtos = objectMapper.readValue(responseBody, new TypeReference<List<UserAccountDto>>(){});
            for (UserAccountDto userAccountDto: accountDtos) {
                if (userAccountDto.getCurrency().getMark().equals("RSD")) {
                    userAccountDtoBeforeWithdraw = userAccountDto;
                }
            }
            System.out.println(userAccountDtoBeforeWithdraw);
        } else {
            fail(response.getStatusCode().toString());
        }
    }


    @When("korisnik se loguje preko kartice")
    public void korisnikSeLogujePrekoKartice() throws JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:8082/api/v1/card/getAllByUser/" + userCardDto.getId();

        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            String responseBody = response.getBody();
            List<CardDto> cardDtos = objectMapper.readValue(responseBody, new TypeReference<List<CardDto>>(){});
            for (CardDto cardDto1: cardDtos) {
                if (cardDto1.getAccountNumber().equals(userAccountDtoBeforeWithdraw.getAccountNumber())) {
                    cardDto = cardDto1;
                }
            }
            System.out.println(cardDto);

            CardLoginDto cardLoginDto = new CardLoginDto(cardDto.getCardNumber(), cardDto.getCVV());

            RestTemplate restTemplateLogin = new RestTemplate();
            String urlLogin = "http://localhost:8082/api/v1/card/cardLogin";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<CardLoginDto> requestEntity = new HttpEntity<>(cardLoginDto, headers);
            System.out.println(requestEntity.getBody().toString());

            ResponseEntity<String> responseLogin = restTemplate.exchange(urlLogin, HttpMethod.POST, requestEntity, String.class);

            if (responseLogin.getStatusCode() == HttpStatus.OK) {
                System.out.println(responseLogin.getBody());
            } else {
                fail(responseLogin.getStatusCode().toString());
            }

        } else {
            fail(response.getStatusCode().toString());
        }
    }

    @And("korisnik podize pare sa kartice")
    public void korisnikPodizePareSaKartice() {

        WithdrawFundsDto withdrawFundsDto = new WithdrawFundsDto(cardDto.getAccountNumber(), 100.0);
        RestTemplate restTemplate = new RestTemplate();
        String urlLogin = "http://localhost:8082/api/v1/card/withdraw";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<WithdrawFundsDto> requestEntity = new HttpEntity<>(withdrawFundsDto, headers);
        System.out.println(requestEntity.getBody().toString());

        ResponseEntity<String> responseLogin = restTemplate.exchange(urlLogin, HttpMethod.POST, requestEntity, String.class);

        if (responseLogin.getStatusCode() == HttpStatus.OK) {
            System.out.println(responseLogin.getBody());
        } else {
            fail(responseLogin.getStatusCode().toString());
        }
    }

    @Then("korisnik cekira balans na dinarskom racunu nakon podizanja")
    public void korisnikCekiraBalansNaDinarskomRacunuNakonPodizanja() throws JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:8082/api/v1/account/getByUser/" + userCardDto.getId();

        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        UserAccountDto userAccountDtoAfterWithdraw = null;

        if (response.getStatusCode() == HttpStatus.OK) {
            String responseBody = response.getBody();
            List<UserAccountDto> accountDtos = objectMapper.readValue(responseBody, new TypeReference<List<UserAccountDto>>(){});
            for (UserAccountDto userAccountDto: accountDtos) {
                if (userAccountDto.getCurrency().getMark().equals("RSD")) {
                    userAccountDtoAfterWithdraw = userAccountDto;
                }
            }
           assertTrue(userAccountDtoBeforeWithdraw.getAvailableBalance().compareTo(userAccountDtoAfterWithdraw.getAvailableBalance()) == 1);
        } else {
            fail(response.getStatusCode().toString());
        }

    }


    private UserAccountDto userAccountRSD;
    private UserAccountDto userAccountEUR;

    private CompanyAccountDto bankAccountRSD;
    private CompanyAccountDto bankAccountEUR;

    @And("korisnik cekira dinarski racun")
    public void korisnikCekiraDinarskiRacun() throws JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:8082/api/v1/account/getByUser/" + userCardDto.getId();

        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            String responseBody = response.getBody();
            List<UserAccountDto> accountDtos = objectMapper.readValue(responseBody, new TypeReference<List<UserAccountDto>>(){});
            for (UserAccountDto userAccountDto: accountDtos) {
                if (userAccountDto.getCurrency().getMark().equals("RSD")) {
                    userAccountRSD = userAccountDto;
                }
            }
            System.out.println(userAccountRSD);
        } else {
            fail(response.getStatusCode().toString());
        }
    }

    @And("korisnik cekira euro racun")
    public void korisnikCekiraEuroRacun() throws JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:8082/api/v1/account/getByUser/" + userCardDto.getId();

        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            String responseBody = response.getBody();
            List<UserAccountDto> accountDtos = objectMapper.readValue(responseBody, new TypeReference<List<UserAccountDto>>(){});
            for (UserAccountDto userAccountDto: accountDtos) {
                if (userAccountDto.getCurrency().getMark().equals("EUR")) {
                    userAccountEUR = userAccountDto;
                }
            }
            System.out.println(userAccountEUR);
        } else {
            fail(response.getStatusCode().toString());
        }
    }

    @And("provera stanja dinarskog i euro racuna banke")
    public void proveraStanjaDinarskogIEuroRacunaBanke() throws JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:8082/api/v1/companyAccount/getAll";

        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            String responseBody = response.getBody();
            List<CompanyAccountDto> accountDtos = objectMapper.readValue(responseBody, new TypeReference<List<CompanyAccountDto>>(){});
            for (CompanyAccountDto companyAccountDto: accountDtos) {
               if(companyAccountDto.getCurrency().getMark().equals("RSD")) {
                   bankAccountRSD = companyAccountDto;
               } else if (companyAccountDto.getCurrency().getMark().equals("EUR")) {
                   bankAccountEUR = companyAccountDto;
               }
            }
            System.out.println(bankAccountRSD);
            System.out.println(bankAccountEUR);
        } else {
            fail(response.getStatusCode().toString());
        }
    }

    @When("korisnik izvrsava konverziju novca")
    public void korisnikIzvrsavaKonverzijuNovca() {
        CurrencyExchangeDto currencyExchangeDto = new CurrencyExchangeDto();
        currencyExchangeDto.setAccountFrom(userAccountEUR.getAccountNumber());
        currencyExchangeDto.setAccountTo(userAccountRSD.getAccountNumber());
        currencyExchangeDto.setAmount(100.0);

        RestTemplate restTemplate = new RestTemplate();
        String urlLogin = "http://localhost:8082/api/v1/currencyExchange";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<CurrencyExchangeDto> requestEntity = new HttpEntity<>(currencyExchangeDto, headers);
        System.out.println(requestEntity.getBody().toString());

        ResponseEntity<String> responseLogin = restTemplate.exchange(urlLogin, HttpMethod.POST, requestEntity, String.class);

        if (responseLogin.getStatusCode() == HttpStatus.OK) {
            System.out.println(responseLogin.getBody());
        } else {
            fail(responseLogin.getStatusCode().toString());
        }
    }

    @Then("provera stanja dinarskog i euro racuna banke nako konverzije")
    public void proveraStanjaDinarskogIEuroRacunaBankeNakoKonverzije() throws JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:8082/api/v1/companyAccount/getAll";

        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        CompanyAccountDto bankAccountRSDAfter = null;
        CompanyAccountDto bankAccountEURAfter = null;
        if (response.getStatusCode() == HttpStatus.OK) {
            String responseBody = response.getBody();
            List<CompanyAccountDto> accountDtos = objectMapper.readValue(responseBody, new TypeReference<List<CompanyAccountDto>>(){});
            for (CompanyAccountDto companyAccountDto: accountDtos) {
                if(companyAccountDto.getCurrency().getMark().equals("RSD")) {
                    bankAccountRSDAfter = companyAccountDto;
                } else if (companyAccountDto.getCurrency().getMark().equals("EUR")) {
                    bankAccountEURAfter = companyAccountDto;
                }
            }
            System.out.println(bankAccountRSDAfter);
            System.out.println(bankAccountEURAfter);
            assertTrue(bankAccountRSD.getAvailableBalance().compareTo(bankAccountRSDAfter.getAvailableBalance()) != 0);
            assertTrue(bankAccountEUR.getAvailableBalance().compareTo(bankAccountEURAfter.getAvailableBalance()) != 0);
        } else {
            fail(response.getStatusCode().toString());
        }
    }

    @And("korisnik cekira balans na euro i dinarskom racunu")
    public void korisnikCekiraBalansNaEuroIDinarskomRacunu() throws JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:8082/api/v1/account/getByUser/" + userCardDto.getId();

        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        UserAccountDto userAccountRSDAfter = null;
        UserAccountDto userAccountEURAfter = null;
        if (response.getStatusCode() == HttpStatus.OK) {
            String responseBody = response.getBody();
            List<UserAccountDto> accountDtos = objectMapper.readValue(responseBody, new TypeReference<List<UserAccountDto>>(){});
            for (UserAccountDto userAccountDto: accountDtos) {
                if(userAccountDto.getCurrency().getMark().equals("RSD")) {
                    userAccountRSDAfter = userAccountDto;
                } else if (userAccountDto.getCurrency().getMark().equals("EUR")) {
                    userAccountEURAfter = userAccountDto;
                }
            }
            assertTrue(bankAccountRSD.getAvailableBalance().compareTo(userAccountRSDAfter.getAvailableBalance()) != 0);
            assertTrue(bankAccountEUR.getAvailableBalance().compareTo(userAccountEURAfter.getAvailableBalance()) != 0);
        } else {
            fail(response.getStatusCode().toString());
        }
    }
}
