package rs.edu.raf.userservice.integration.companyAccountController;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import rs.edu.raf.userservice.domain.dto.account.CheckEnoughBalanceDto;
import rs.edu.raf.userservice.domain.dto.account.RebalanceAccountDto;
import rs.edu.raf.userservice.domain.dto.companyaccount.CompanyAccountCreateDto;
import rs.edu.raf.userservice.domain.dto.companyaccount.CompanyAccountDto;
import rs.edu.raf.userservice.domain.dto.login.LoginRequest;
import rs.edu.raf.userservice.integration.LoginResponseForm;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CompanyAccountControllerTestSteps extends CompanyAccountControllerTestsConfig {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CompanyAccountControllerTestsState companyAccountControllerTestsState;

    @Given("Admin je ulogovan")
    public void admin_je_ulogovan() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("admin@admin.com");
        loginRequest.setPassword("admin1234");

        try {
            String loginRequestJson = objectMapper.writeValueAsString(loginRequest);

            ResultActions resultActions = mockMvc.perform(
                    post("/api/v1/employee/auth/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .content(loginRequestJson)

            ).andExpect(status().isOk());

            MvcResult result = resultActions.andReturn();
            String loginResponse = result.getResponse().getContentAsString();
            LoginResponseForm loginResponseForm = objectMapper.readValue(loginResponse, LoginResponseForm.class);
            companyAccountControllerTestsState.setJwtToken(loginResponseForm.getJwt());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @When("Admin salje zahtev za sve company account-e")
    public void admin_salje_zahtev_za_sve_company_account_e() {

        try {
            ResultActions resultActions = mockMvc.perform(
                    get("/api/v1/companyAccount/getAll")
                            .header("Authorization", "Bearer " + companyAccountControllerTestsState.getJwtToken())
            ).andExpect(status().isOk());
            System.out.println("JWT: " + companyAccountControllerTestsState.getJwtToken());
        }catch (Exception e){
            fail(e.getMessage());
        }
    }

    @Then("Sistem vraca sve company account-e")
    public void sistem_vraca_sve_company_account_e() {
        try {
            ResultActions resultActions = mockMvc.perform(
                    get("/api/v1/companyAccount/getAll")
                            .header("Authorization", "Bearer " + companyAccountControllerTestsState.getJwtToken())
            ).andExpect(status().isOk());

            MvcResult mvcResult = resultActions.andReturn();
            String contentAsString = mvcResult.getResponse().getContentAsString();
            // Assuming that the response is a list of account types
            List<CompanyAccountDto> companyAccountDtos = objectMapper.readValue(contentAsString, new TypeReference<List<CompanyAccountDto>>(){});
            // Add your assertions here. For example:
            assertFalse(companyAccountDtos.isEmpty(), "The list of company accounts should not be empty");
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @When("Admin salje zahtev za company account sa companyId-jem {string}")
    public void adminSaljeZahtevZaCompanyAccountSaCompanyIdJem(String companyId) {
        try {
            ResultActions resultActions = mockMvc.perform(
                    get("/api/v1/companyAccount/getByCompany/{companyId}", companyId)
                            .header("Authorization", "Bearer " + companyAccountControllerTestsState.getJwtToken())
            ).andExpect(status().isOk());
        }catch (Exception e){
            fail(e.getMessage());
        }
    }

    @Then("Sistem vraca company account sa companyId-jem {string}")
    public void sistemVracaCompanyAccountSaCompanyIdJem(String companyId) {

        try {
            ResultActions resultActions = mockMvc.perform(
                    get("/api/v1/companyAccount/getByCompany/{companyId}", companyId)
                            .header("Authorization", "Bearer " + companyAccountControllerTestsState.getJwtToken())
            ).andExpect(status().isOk());

            MvcResult mvcResult = resultActions.andReturn();
            String contentAsString = mvcResult.getResponse().getContentAsString();
            // Assuming that the response is a list of account types
            List<CompanyAccountDto> companyAccountDto = objectMapper.readValue(contentAsString, new TypeReference<>() {
            });
            // Add your assertions here. For example:
            assertTrue(companyAccountDto.isEmpty(), "No accounts found for company with id " + companyId);
        } catch (Exception e) {
            fail(e.getMessage());
        }

        //TODO puca ali u postman-u zahtev vraca praznu listu
    }


//    @When("Admin salje zahtev za company account sa brojem {string}")
//    public void adminSaljeZahtevZaCompanyAccountSaIdJem(String accNum) {
//        try {
//            ResultActions resultActions = mockMvc.perform(
//                    get("/api/v1/companyAccount/getByAccountNumber/{accNum}", accNum)
//                            .header("Authorization", "Bearer " + companyAccountControllerTestsState.getJwtToken())
//            ).andExpect(status().isOk());
//        }catch (Exception e){
//            fail(e.getMessage());
//        }
//
//    }
//
//    @Then("Sistem vraca company account sa brojem {string}")
//    public void sistemVracaCompanyAccountSabrojem(String accNum) {
//        try {
//            ResultActions resultActions = mockMvc.perform(
//                    get("/api/v1/companyAccount/getByAccountNumber/{accNum}", accNum)
//                            .header("Authorization", "Bearer " + companyAccountControllerTestsState.getJwtToken())
//            ).andExpect(status().isOk());
//
//            MvcResult mvcResult = resultActions.andReturn();
//            String contentAsString = mvcResult.getResponse().getContentAsString();
//            // Assuming that the response is a list of account types
//            CompanyAccountDto companyAccountDto = objectMapper.readValue(contentAsString, CompanyAccountDto.class);
//            // Add your assertions here. For example:
//            assertNotEquals(null, companyAccountDto, "No accounts with number " + accNum);
//        } catch (Exception e) {
//            fail(e.getMessage());
//        }
//    }


//    @When("Kreiramo novi company nalog id {string}")
//    public void kreiramoNoviCompanyNalog(String id) {
//
//        CompanyAccountCreateDto companyAccountDto = new CompanyAccountCreateDto();
//        companyAccountDto.setBalance(new BigDecimal("10000000000000000.0"));
//        Long idl = Long.parseLong(id);
//        companyAccountDto.setCompanyId(idl);
//
//        try {
//            String companyAccountDtoJson = objectMapper.writeValueAsString(companyAccountDto);
//
//            ResultActions resultActions = mockMvc.perform(
//                    post("/api/v1/companyAccount/{companyId}", idl)
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .accept(MediaType.APPLICATION_JSON)
//                            .content(companyAccountDtoJson)
//                            .header("Authorization", "Bearer " + companyAccountControllerTestsState.getJwtToken())
//            ).andExpect(status().isOk());
//
//            MvcResult mvcResult = resultActions.andReturn();
//
//            String jsonCompanyAccount = mvcResult.getResponse().getContentAsString();
//            CompanyAccountCreateDto companyAccountCreateDto = objectMapper.readValue(jsonCompanyAccount, CompanyAccountCreateDto.class);
//            assertEquals(new BigDecimal("10000000000000000.0"), companyAccountCreateDto.getBalance());
//        }catch (Exception e){
//            fail(e.getMessage());
//        }
//    }

//    @Then("Povlacimo nalog {string} iz baze i proveravamo da li je kreiran po companyId-ju")
//    public void povlacimoNalogIzBazeIProveravamoDaLiJeKreiran(String id) {
////        try {
////            ResultActions resultActions = mockMvc.perform(
////                    post("/api/v1/companyAccount/getByCompany/{companyId}", id)
////                            .header("Authorization", "Bearer " + companyAccountControllerTestsState.getJwtToken())
////            ).andExpect(status().isOk());
////
////            MvcResult mvcResult = resultActions.andReturn();
////
////            String jsonCompanyAccount = mvcResult.getResponse().getContentAsString();
////            List<CompanyAccountCreateDto> companyAccountCreateDtos = objectMapper.readValue(jsonCompanyAccount,  new TypeReference<>() {
////            });
////            assertFalse(companyAccountCreateDtos.isEmpty(), "No accounts found for company with id " + id);
////        }catch (Exception e){
////            fail(e.getMessage());
////        }
//    }


    @When("Admin salje zahtev za brisanje company account-a sa id-jem {string}")
    public void adminSaljeZahtevZaBrisanjeCompanyAccountASaIdJem(String id) {

        try {
            mockMvc.perform(
                    delete("/api/v1/companyAccount/{id}", id)
                            .header("Authorization", "Bearer " + companyAccountControllerTestsState.getJwtToken())
            ).andExpect(status().isOk());
        }catch (Exception e){
            fail(e.getMessage());
        }
    }

    @Then("Sistem brise company account sa brojem {string}")
    public void sistemBriseCompanyAccountSaIdJem(String accNum) {
        try{
            ResultActions resultActions = mockMvc.perform(
                    get("/api/v1/companyAccount/getByAccountNumber/{id}", accNum)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + companyAccountControllerTestsState.getJwtToken())
            ).andExpect(status().isOk());

        }catch (Exception e){
            fail(e.getMessage());
        }
    }


    @When("Slanje zahteva za proveru balansa")
    public void slanjeZahtevaZaProveruBalansa() {
        CheckEnoughBalanceDto checkEnoughBalanceDto = new CheckEnoughBalanceDto();
        checkEnoughBalanceDto.setAccountNumber("1010101010101010");
        checkEnoughBalanceDto.setAmount(1000.0);
        checkEnoughBalanceDto.setCurrencyMark("RSD");


        try {
            String json = objectMapper.writeValueAsString(checkEnoughBalanceDto);
            ResultActions resultActions = mockMvc.perform(
                    post("/api/v1/companyAccount/checkCompanyBalance")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json)
                            .header("Authorization", "Bearer " + companyAccountControllerTestsState.getJwtToken())
            ).andExpect(status().isOk());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }


    @Then("Sistem vraca odgovor u zavisnosti od toga da li ima dovoljno sredstava")
    public void sistemVracaOdgovorUZavisnostiOdTogaDaLiImaDovoljnoSredstava() {

        CheckEnoughBalanceDto checkEnoughBalanceDto = new CheckEnoughBalanceDto();
        checkEnoughBalanceDto.setAccountNumber("1010101010101010");
        checkEnoughBalanceDto.setAmount(1000.0);
        checkEnoughBalanceDto.setCurrencyMark("RSD");


        try {
            String json = objectMapper.writeValueAsString(checkEnoughBalanceDto);
            ResultActions resultActions = mockMvc.perform(
                    post("/api/v1/companyAccount/checkCompanyBalance")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json)
                            .header("Authorization", "Bearer " + companyAccountControllerTestsState.getJwtToken())
            ).andExpect(status().isOk());

            assertEquals(200, resultActions.andReturn().getResponse().getStatus());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @When("Slanje zahteva za rezervaciju novca")
    public void slanjeZahtevaZaRezervacijuNovca() {
        RebalanceAccountDto rebalanceAccountDto = new RebalanceAccountDto();
        rebalanceAccountDto.setAccountNumber("1010101010101010");
        rebalanceAccountDto.setAmount(1000.0);
        rebalanceAccountDto.setCurrencyMark("RSD");

        try {
            String json = objectMapper.writeValueAsString(rebalanceAccountDto);
            ResultActions resultActions = mockMvc.perform(
                    post("/api/v1/companyAccount/reserveCompanyMoney")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json)
                            .header("Authorization", "Bearer " + companyAccountControllerTestsState.getJwtToken())
            ).andExpect(status().isOk());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }


    @Then("Sistem vraca odgovor u zavisnosti od toga da li je rezervacija uspesna")
    public void sistemVracaOdgovorUZavisnostiOdTogaDaLiJeRezervacijaUspesna() {
        RebalanceAccountDto rebalanceAccountDto = new RebalanceAccountDto();
        rebalanceAccountDto.setAccountNumber("1010101010101010");
        rebalanceAccountDto.setAmount(1000.0);
        rebalanceAccountDto.setCurrencyMark("RSD");

        try {
            String json = objectMapper.writeValueAsString(rebalanceAccountDto);
            ResultActions resultActions = mockMvc.perform(
                    post("/api/v1/companyAccount/reserveCompanyMoney")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json)
                            .header("Authorization", "Bearer " + companyAccountControllerTestsState.getJwtToken())
            ).andExpect(status().isOk());

            assertEquals(200, resultActions.andReturn().getResponse().getStatus());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @When("Slanje zahteva za uklanjanje rezervacije novca")
    public void slanjeZahtevaZaUklanjanjeRezervacijeNovca() {
        RebalanceAccountDto rebalanceAccountDto = new RebalanceAccountDto();
        rebalanceAccountDto.setAccountNumber("1010101010101010");
        rebalanceAccountDto.setAmount(1000.0);
        rebalanceAccountDto.setCurrencyMark("RSD");

        try {
            String json = objectMapper.writeValueAsString(rebalanceAccountDto);
            ResultActions resultActions = mockMvc.perform(
                    post("/api/v1/companyAccount/unreserveCompanyMoney")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json)
                            .header("Authorization", "Bearer " + companyAccountControllerTestsState.getJwtToken())
            ).andExpect(status().isOk());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Then("Sistem vraca odgovor u zavisnosti od toga da li je uklanjanje rezervacije uspesno")
    public void sistemVracaOdgovorUZavisnostiOdTogaDaLiJeUklanjanjeRezervacijeUspesno() {
        RebalanceAccountDto rebalanceAccountDto = new RebalanceAccountDto();
        rebalanceAccountDto.setAccountNumber("1010101010101010");
        rebalanceAccountDto.setAmount(1000.0);
        rebalanceAccountDto.setCurrencyMark("RSD");

        try {
            String json = objectMapper.writeValueAsString(rebalanceAccountDto);
            ResultActions resultActions = mockMvc.perform(
                    post("/api/v1/companyAccount/unreserveCompanyMoney")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json)
                            .header("Authorization", "Bearer " + companyAccountControllerTestsState.getJwtToken())
            ).andExpect(status().isOk());

            assertEquals(200, resultActions.andReturn().getResponse().getStatus());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @When("Slanje zahteva za dodavanje novca")
    public void slanjeZahtevaZaDodavanjeNovca() {
        RebalanceAccountDto rebalanceAccountDto = new RebalanceAccountDto();
        rebalanceAccountDto.setAccountNumber("1010101010101010");
        rebalanceAccountDto.setAmount(1000.0);
        rebalanceAccountDto.setCurrencyMark("RSD");

        try {
            String json = objectMapper.writeValueAsString(rebalanceAccountDto);
            ResultActions resultActions = mockMvc.perform(
                    post("/api/v1/companyAccount/addMoneyToCompanyAccount")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json)
                            .header("Authorization", "Bearer " + companyAccountControllerTestsState.getJwtToken())
            ).andExpect(status().isOk());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }


    @Then("Sistem vraca odgovor u zavisnosti od toga da li je dodavanje uspesno")
    public void sistemVracaOdgovorUZavisnostiOdTogaDaLiJeDodavanjeUspesno() {
        RebalanceAccountDto rebalanceAccountDto = new RebalanceAccountDto();
        rebalanceAccountDto.setAccountNumber("1010101010101010");
        rebalanceAccountDto.setAmount(1000.0);
        rebalanceAccountDto.setCurrencyMark("RSD");

        try {
            String json = objectMapper.writeValueAsString(rebalanceAccountDto);
            ResultActions resultActions = mockMvc.perform(
                    post("/api/v1/companyAccount/addMoneyToCompanyAccount")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json)
                            .header("Authorization", "Bearer " + companyAccountControllerTestsState.getJwtToken())
            ).andExpect(status().isOk());

            assertEquals(200, resultActions.andReturn().getResponse().getStatus());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @When("Slanje zahteva za uklanjanje uzimanje novca")
    public void slanjeZahtevaZaUklanjanjeUzimanjeNovca() {
        RebalanceAccountDto rebalanceAccountDto = new RebalanceAccountDto();
        rebalanceAccountDto.setAccountNumber("1010101010101010");
        rebalanceAccountDto.setAmount(1000.0);
        rebalanceAccountDto.setCurrencyMark("RSD");

        try {
            String json = objectMapper.writeValueAsString(rebalanceAccountDto);
            ResultActions resultActions = mockMvc.perform(
                    post("/api/v1/companyAccount/takeMoneyFromCompanyAccount")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json)
                            .header("Authorization", "Bearer " + companyAccountControllerTestsState.getJwtToken())
            ).andExpect(status().isOk());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Then("Sistem vraca odgovor u zavisnosti od toga da li je uzimanje uspesno")
    public void sistemVracaOdgovorUZavisnostiOdTogaDaLiJeUzimanjeUspesno() {
        RebalanceAccountDto rebalanceAccountDto = new RebalanceAccountDto();
        rebalanceAccountDto.setAccountNumber("1010101010101010");
        rebalanceAccountDto.setAmount(1000.0);
        rebalanceAccountDto.setCurrencyMark("RSD");

        try {
            String json = objectMapper.writeValueAsString(rebalanceAccountDto);
            ResultActions resultActions = mockMvc.perform(
                    post("/api/v1/companyAccount/takeMoneyFromCompanyAccount")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json)
                            .header("Authorization", "Bearer " + companyAccountControllerTestsState.getJwtToken())
            ).andExpect(status().isOk());

            assertEquals(200, resultActions.andReturn().getResponse().getStatus());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
}
