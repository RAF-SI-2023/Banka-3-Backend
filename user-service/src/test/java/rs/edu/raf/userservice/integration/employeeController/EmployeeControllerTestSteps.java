package rs.edu.raf.userservice.integration.employeeController;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import rs.edu.raf.userservice.domains.dto.employee.EmployeeCreateDto;
import rs.edu.raf.userservice.domains.dto.employee.EmployeeDto;
import rs.edu.raf.userservice.domains.dto.login.LoginRequest;
import rs.edu.raf.userservice.integration.LoginResponseForm;
import rs.edu.raf.userservice.repositories.RoleRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class EmployeeControllerTestSteps extends EmployeeControllerTestsConfig{

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private EmployeeControllerTestsState employeeControllerTestsState;

    @Autowired
    private RoleRepository roleRepository;

    @Given("logovali smo se kao administrator")
    public void logovali_smo_se_kao_administrator() {
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

    @When("kreiramo zaposlenog sa imenom {string} i prezimenom {string}, emailom {string}, username {string}, jmbg {string}, datum rodjenja {string}, adresa {string}, telefon {string},pol {string}, rola {string}")
    public void kreiramoZaposlenogSaImenomIPrezimenomEmailomUsernameJmbgDatumRodjenjaAdresaTelefonPolRola(String ime, String prezime, String email, String username, String jmbg, String dateOfBirth, String address, String phoneNumber, String gender, String role) {
//        EmployeeCreateDto employeeCreateDto = new EmployeeCreateDto();
//        employeeCreateDto.setFirstName(ime);
//        employeeCreateDto.setLastName(prezime);
//        employeeCreateDto.setEmail(email);
//        employeeCreateDto.setUsername(username);
//        employeeCreateDto.setJmbg(jmbg);
//        employeeCreateDto.setDateOfBirth(Long.parseLong(dateOfBirth));
//        employeeCreateDto.setAddress(address);
//        employeeCreateDto.setPhoneNumber(phoneNumber);
//        employeeCreateDto.setGender(gender);
//        employeeCreateDto.setRole(roleRepository.findRoleByRoleId(Long.valueOf(role)));
//
//
//
//
//        try{
//            String employeeCreateDtoJson = objectMapper.writeValueAsString(employeeCreateDto);
//
//            ResultActions resultActions = mockMvc.perform(
//                    post("/api/v1/employee")
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .accept(MediaType.APPLICATION_JSON)
//                            .header("Authorization", "Bearer " + employeeControllerTestsState.getJwtToken())
//                            .content(employeeCreateDtoJson)
//            ).andExpect(status().isOk());
//
//            MvcResult mvcResult = resultActions.andReturn();
//
//            String jsonEmployee = mvcResult.getResponse().getContentAsString();
//            EmployeeDto employeeDto = objectMapper.readValue(jsonEmployee, EmployeeDto.class);
//            assertEquals(ime, employeeDto.getFirstName());
//            assertEquals(prezime, employeeDto.getLastName());
//            assertEquals(email, employeeDto.getEmail());
//        }catch (Exception e) {
//            fail(e.getMessage());
//        }
    }

    @Then("povlacimo zaposlenog iz baze i proveravamo da li je kreiran, pretraga po emailu {string}")
    public void povlacimoZaposlenogIzBazeIProveravamoDaLiJeKreiranPretragaPoEmailu(String email) {
//        try{
//            ResultActions resultActions = mockMvc.perform(
//                    get("/api/v1/employee/findByEmail/" + email)
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .accept(MediaType.APPLICATION_JSON)
//                            .header("Authorization", "Bearer " + employeeControllerTestsState.getJwtToken())
//            ).andExpect(status().isOk());
//
//            MvcResult mvcResult = resultActions.andReturn();
//
//            String jsonEmployee = mvcResult.getResponse().getContentAsString();
//            EmployeeDto employeeDto = objectMapper.readValue(jsonEmployee, EmployeeDto.class);
//            assertEquals(email, employeeDto.getEmail());
//        }catch (Exception e) {
//            fail(e.getMessage());
//        }
    }

    @When("obrisemo korisnika sa id-em {string}")
    public void obrisemoKorisnikaSaIdEm(String id){
        try {
            ResultActions resultActions = mockMvc.perform(
                    delete("/api/v1/employee/{id}", id)
                            .header("Authorization", "Bearer " + employeeControllerTestsState.getJwtToken())
            ).andExpect(status().isOk());
        }catch (Exception e){
            fail(e.getMessage());
        }

    }

    @Then("korisnik sa id-em {string} je obrisan")
    public void korisnikSaIdEmJeObrisan(String id) {
        try{
            ResultActions resultActions = mockMvc.perform(
                    get("/api/v1/employee/findById/{id}", id)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + employeeControllerTestsState.getJwtToken())
            ).andExpect(status().isOk());

            MvcResult result = resultActions.andReturn();

            String response = result.getResponse().getContentAsString();
            EmployeeDto employeeDto = objectMapper.readValue(response, EmployeeDto.class);
            assertFalse(employeeDto.getIsActive());

        }catch (Exception e){
            fail(e.getMessage());
        }


    }

}
