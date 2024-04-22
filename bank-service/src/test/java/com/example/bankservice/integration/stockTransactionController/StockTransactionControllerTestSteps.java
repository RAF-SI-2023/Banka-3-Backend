//package com.example.bankservice.integration.stockTransactionController;
//
//import com.example.bankservice.domains.dto.StockTransactionDto;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import io.cucumber.java.en.Then;
//import io.cucumber.java.en.When;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.ResultActions;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//public class StockTransactionControllerTestSteps extends StockTransactionControllerTestsConfig{
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    StockTransactionDto stockTransactionDto;
//    @When("Zaposleni kreira kreira stock transakciju")
//    public void zaposleniKreiraKreiraStockTransakciju() {
//        stockTransactionDto = new StockTransactionDto();
//        stockTransactionDto.setAccountFrom("3030303030303030");
//        stockTransactionDto.setAmount(100.0);
//        stockTransactionDto.setAccountTo("3333333333333333");
//
//    }
//
//
//    @Then("Stock transakcija je kreiran")
//    public void stockTransakcijaJeKreiran() {
//        try{
//            String json = objectMapper.writeValueAsString(stockTransactionDto);
//            ResultActions resultActions = mockMvc.perform(
//                    post("/api/v1/stockTransaction/start")
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
