package rs.edu.raf.userservice.integration.accountTypeController;

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
import rs.edu.raf.userservice.domains.dto.login.LoginRequest;
import rs.edu.raf.userservice.domains.model.AccountType;
import rs.edu.raf.userservice.integration.LoginResponseForm;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AccountTypeControllerTestSteps extends AccountTypeControllerTestsConfig{

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AccountTypeControllerTestsState accountTypeControllerTestsState;

    @Given("Korisnik je ulogovan")
    public void korisnikJeUlogovan() {
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
            accountTypeControllerTestsState.setJwtToken(loginResponseForm.getJwt());
            System.out.println("JWT: " + loginResponseForm.getJwt());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
    @When("Korisnik zatrazi sve tipove naloga")
    public void korisnikZatraziSveTipoveNaloga() {
        try {
            ResultActions resultActions = mockMvc.perform(
                    get("/api/v1/account-types/getAll")
                            .header("Authorization", "Bearer " + accountTypeControllerTestsState.getJwtToken())

            ).andExpect(status().isOk());

        } catch (Exception e) {
            fail(e.getMessage());
        }
    }


    @Then("Sistem vraca sve tipove naloga")
    public void sistemVracaSveTipoveNaloga() {
        try {
            ResultActions resultActions = mockMvc.perform(
                    get("/api/v1/account-types/getAll")
                            .header("Authorization", "Bearer " + accountTypeControllerTestsState.getJwtToken())
            ).andExpect(status().isOk());

            MvcResult mvcResult = resultActions.andReturn();
            String contentAsString = mvcResult.getResponse().getContentAsString();
            List<AccountType> accountTypes = objectMapper.readValue(contentAsString, new TypeReference<List<AccountType>>(){});
            assertFalse(accountTypes.isEmpty(), "The list of account types should not be empty");
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
}
