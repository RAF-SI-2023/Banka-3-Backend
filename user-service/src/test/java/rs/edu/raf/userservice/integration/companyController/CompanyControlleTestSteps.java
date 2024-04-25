package rs.edu.raf.userservice.integration.companyController;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import rs.edu.raf.userservice.domain.dto.company.CompanyCreateDto;
import rs.edu.raf.userservice.domain.dto.company.CompanyDto;
import rs.edu.raf.userservice.domain.dto.login.LoginRequest;
import rs.edu.raf.userservice.domain.dto.user.UserSetPasswordDto;
import rs.edu.raf.userservice.integration.LoginResponseForm;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CompanyControlleTestSteps extends CompanyControllerTestsConfig{

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private CompanyControllerTestState companyControllerTestState;

    LoginRequest loginRequest = new LoginRequest();
    @Given("kompanija se loguje sa sa emailom {string} i lozinkom {string}")
    public void kompanijaSeLogujeSaSaEmailomILozinkom(String email, String password) {
        loginRequest.setEmail(email);
        loginRequest.setPassword(password);

        try {
            String loginRequestJson = objectMapper.writeValueAsString(loginRequest);

            ResultActions resultActions = mockMvc.perform(
                    post("/api/v1/company/auth/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .content(loginRequestJson)
            ).andExpect(status().isOk());
        }catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @When("kompanija zeli da promeni lozinku u {string}")
    public void kompanijaZeliDaPromeniLozinkuU(String password) {
        UserSetPasswordDto userSetPasswordDto = new UserSetPasswordDto();
        userSetPasswordDto.setPassword(password);
        userSetPasswordDto.setEmail("exchange@gmail.com");

        try {
            String userSetPasswordDtoJson = objectMapper.writeValueAsString(userSetPasswordDto);

            ResultActions resultActions = mockMvc.perform(
                    post("/api/v1/company/setPassword")
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .content(userSetPasswordDtoJson)
            ).andExpect(status().isOk());
        }catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Then("kompanija se ponovo loguje")
    public void kompanijaSePonovoLoguje() {

        try {
            String loginRequestJson = objectMapper.writeValueAsString(loginRequest);

            ResultActions resultActions = mockMvc.perform(
                    post("/api/v1/company/auth/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .content(loginRequestJson)
            ).andExpect(status().isOk());
        }catch (Exception e) {
            fail(e.getMessage());
        }
    }


    @Given("admin se loguje sa emailom {string} i lozinkom {string}")
    public void adminSeLogujeSaEmailomILozinkom(String email, String password) {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(email);
        loginRequest.setPassword(password);

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
            companyControllerTestState.setJwtToken(loginResponseForm.getJwt());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @When("admin kreira novu kompaniju sa emailom {string} i lozinkom {string}")
    public void adminKreiraNovuKompanijuSaEmailomILozinkom(String email, String password) {
        CompanyCreateDto companyCreateDto = new CompanyCreateDto();
        companyCreateDto.setEmail(email);
        companyCreateDto.setTitle("Company");
        companyCreateDto.setSifraDelatnosti(4123);
        companyCreateDto.setMaticniBroj(100000032);
        companyCreateDto.setNumber("123456789");

        try {
            String companyCreateDtoJson = objectMapper.writeValueAsString(companyCreateDto);

            ResultActions resultActions = mockMvc.perform(
                    post("/api/v1/company")
                            .header("Authorization", "Bearer " + companyControllerTestState.getJwtToken())
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .content(companyCreateDtoJson)
            ).andExpect(status().isOk());

            MvcResult result = resultActions.andReturn();
            String company = result.getResponse().getContentAsString();
            CompanyDto companyDto = objectMapper.readValue(company, CompanyDto.class);
            companyControllerTestState.setCompanyId(companyDto.getId());
            companyControllerTestState.setEmail(companyDto.getEmail());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Then("nova kompanija je aktivana")
    public void novaKompanijaJeAktivana() {
        try {

            ResultActions resultActions = mockMvc.perform(
                    put("/api/v1/company/activate/" + companyControllerTestState.getCompanyId())
                            .header("Authorization", "Bearer " + companyControllerTestState.getJwtToken())
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
            ).andExpect(status().isOk());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @When("admin dohvati sve kompanije")
    public void adminDohvatiSveKompanije() {
        try {
            ResultActions resultActions = mockMvc.perform(
                    get("/api/v1/company/getAll")
                            .header("Authorization", "Bearer " + companyControllerTestState.getJwtToken())
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
            ).andExpect(status().isOk());

            MvcResult result = resultActions.andReturn();
            String companies = result.getResponse().getContentAsString();
            CompanyDto[] companyDtos = objectMapper.readValue(companies, CompanyDto[].class);
            assertTrue(companyDtos.length > 0);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Then("admin provaerava da li je kompanija aktivana")
    public void adminProvaeravaDaLiJeKompanijaAktivana() {
        try {

            ResultActions resultActions = mockMvc.perform(
                    get("/api/v1/company/isCompanyActive/" + companyControllerTestState.getEmail())
                            .header("Authorization", "Bearer " + companyControllerTestState.getJwtToken())
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
            ).andExpect(status().isOk());

            MvcResult result = resultActions.andReturn();
            String isActive = result.getResponse().getContentAsString();
            assertFalse(Boolean.parseBoolean(isActive));
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @When("admin dohvati kompaniju po id-u")
    public void adminDohvatiKompanijuPoIdU() {
        try {
            ResultActions resultActions = mockMvc.perform(
                    get("/api/v1/company/getByCompany/" + companyControllerTestState.getCompanyId())
                            .header("Authorization", "Bearer " + companyControllerTestState.getJwtToken())
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
            ).andExpect(status().isOk());

            MvcResult result = resultActions.andReturn();
            String company = result.getResponse().getContentAsString();
            CompanyDto companyDto = objectMapper.readValue(company, CompanyDto.class);
            assertTrue(companyDto.getId().equals(companyControllerTestState.getCompanyId()));
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }


    @Then("admin setuje da je kompanije neaktivna")
    public void adminSetujeDaJeKompanijeNeaktivna() {
        try {
            ResultActions resultActions = mockMvc.perform(
                    put("/api/v1/company/deactivate/" + companyControllerTestState.getCompanyId())
                            .header("Authorization", "Bearer " + companyControllerTestState.getJwtToken())
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
            ).andExpect(status().isOk());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
}
