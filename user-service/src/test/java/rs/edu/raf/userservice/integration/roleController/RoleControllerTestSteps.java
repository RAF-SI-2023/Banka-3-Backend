package rs.edu.raf.userservice.integration.roleController;

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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
public class RoleControllerTestSteps extends RoleControllerTestsConfig{

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private EmployeeControllerTestsState employeeControllerTestsState;

    @Given("zaposleni se loguje na sistem")
    public void zaposleniSeLogujeNaSistem() {
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

    @When("korisnik zeli da pretrazi sve role")
    public void korisnikZeliDaPretraziSveRole() {
        //
    }

    @Then("sistem prikazuje sve role")
    public void sistemPrikazujeSveRole() {
        try {
            ResultActions perform = mockMvc.perform(
                    get("/api/v1/role/getAll")
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

    @When("korisnik zeli da pretrazi role po imenu")
    public void korisnikZeliDaPretraziRolePoImenu() {
        //
    }

    @Then("sistem prikazuje role sa zadatim imenom {string}")
    public void sistemPrikazujeRoleSaZadatimImenom(String roleName) {
        try {
            ResultActions perform = mockMvc.perform(
                    get("/api/v1/role/findByRoleName")
                            .param("roleName", roleName)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + employeeControllerTestsState.getJwtToken())


            ).andExpect(status().isOk());

            MvcResult mvcResult = perform.andReturn();
            String contentAsString = mvcResult.getResponse().getContentAsString();
            Role role = objectMapper.readValue(contentAsString, Role.class);
            assertTrue(role.getRoleName().equals(roleName));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
