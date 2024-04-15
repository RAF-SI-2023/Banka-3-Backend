package rs.edu.raf.userservice.integration.cardController;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.Given;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import rs.edu.raf.userservice.domains.dto.login.LoginRequest;
import rs.edu.raf.userservice.integration.LoginResponseForm;
import rs.edu.raf.userservice.integration.accountTypeController.AccountTypeControllerTestsState;

import static org.junit.jupiter.api.Assertions.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CardControllerTestSteps extends CardControllerTestsConfig {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AccountTypeControllerTestsState accountTypeControllerTestsState;

    @Given("Ulogovani smo kao admin")
    public void ulogovaniSmoKaoAdmin() {
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
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

}
