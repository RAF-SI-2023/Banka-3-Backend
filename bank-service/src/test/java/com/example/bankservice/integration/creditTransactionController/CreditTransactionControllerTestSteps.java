//package com.example.bankservice.integration.creditTransactionController;
//
//import com.example.bankservice.domains.dto.CreditTransactionDto;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import io.cucumber.java.en.Then;
//import io.cucumber.java.en.When;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.ResultActions;
//
//import java.time.LocalDate;
//import java.util.ArrayList;
//import java.util.List;
//import static org.junit.jupiter.api.Assertions.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//
//public class CreditTransactionControllerTestSteps extends CreditTransactionControllerTestsConfig{
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//
//    List<CreditTransactionDto> creditTransactionDtos = new ArrayList<>();
//
//    @When("Korisnik kreira kredit transakciju")
//    public void korisnikKreiraKreditTransakciju() {
//        CreditTransactionDto creditTransactionDto = new CreditTransactionDto();
//        creditTransactionDto.setAmount(1000.0);
//        creditTransactionDto.setCurrencyMark("RSD");
//        creditTransactionDto.setAccountFrom("1258963400124583");
//        creditTransactionDto.setCreditId(1L);
//        creditTransactionDto.setDate(12321412421l);
//
//        creditTransactionDtos.add(creditTransactionDto);
//    }
//
//    @Then("Kredit transakcija je kreirana")
//    public void kreditTransakcijaJeKreirana() {
//        try{
//            String json = objectMapper.writeValueAsString(creditTransactionDtos);
//            System.out.println(json);
//
//            ResultActions resultActions = mockMvc.perform(
//                    post("/api/v1/creditTransaction")
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .accept(MediaType.APPLICATION_JSON)
//                            .content(json)
//            ).andExpect(status().isOk());
//
//        } catch (Exception e) {
//            fail(e.getMessage());
//        }
//    }
//}
