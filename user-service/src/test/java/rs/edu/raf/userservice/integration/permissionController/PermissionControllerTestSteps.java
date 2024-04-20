package rs.edu.raf.userservice.integration.permissionController;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import rs.edu.raf.userservice.domain.dto.login.LoginRequest;
import rs.edu.raf.userservice.domain.model.Role;
import rs.edu.raf.userservice.integration.LoginResponseForm;
import rs.edu.raf.userservice.integration.employeeController.EmployeeControllerTestsState;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class PermissionControllerTestSteps extends PermissionControllerTestsConfing {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private EmployeeControllerTestsState employeeControllerTestsState;

    @Given("radnik se loguje na sistem")
    public void radnikSeLogujeNaSistem() {
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
            employeeControllerTestsState.setJwtToken(loginResponseForm.getJwt());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @When("korisnik zeli da pretrazi svih permisija")
    public void korisnikZeliDaPretraziSvihPermisija() {

    }

    @Then("sistem prikazuje sve permisije")
    public void sistemPrikazujeSvePermisije() {
        try {
            ResultActions perform = mockMvc.perform(
                    get("/api/v1/permission/getAll")
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + employeeControllerTestsState.getJwtToken())

            ).andExpect(status().isOk());

            MvcResult mvcResult = perform.andReturn();
            String contentAsString = mvcResult.getResponse().getContentAsString();
            Role[] roles = objectMapper.readValue(contentAsString, Role[].class);
            assertTrue(roles.length > 0);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @When("korisnik zeli da pretrazi permmisije po imenu")
    public void korisnikZeliDaPretraziPermmisijePoImenu() {

    }

    @Then("sistem prikazuje permisije sa zadatim imenom {string}")
    public void sistemPrikazujePermisijeSaZadatimImenom(String permissionName) {
        try {
            ResultActions perform = mockMvc.perform(
                    get("/api/v1/permission/findByPermissionName")
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + employeeControllerTestsState.getJwtToken())
                            .param("permissionName", permissionName)

            ).andExpect(status().isOk());

            MvcResult mvcResult = perform.andReturn();
            String contentAsString = mvcResult.getResponse().getContentAsString();
            Role role = objectMapper.readValue(contentAsString, Role.class);
            assertTrue(role != null);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
