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
import rs.edu.raf.userservice.domain.dto.employee.*;
import rs.edu.raf.userservice.domain.dto.login.LoginRequest;
import rs.edu.raf.userservice.domain.model.Role;
import rs.edu.raf.userservice.domain.model.enums.RoleName;
import rs.edu.raf.userservice.integration.LoginResponseForm;
import rs.edu.raf.userservice.repository.RoleRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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


    EmployeeSetPasswordDto employeeSetPasswordDto = new EmployeeSetPasswordDto();
    EmployeeDto employeeDtoPreeSet;


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
        EmployeeCreateDto employeeCreateDto = new EmployeeCreateDto();
        employeeCreateDto.setFirstName(ime);
        employeeCreateDto.setLastName(prezime);
        employeeCreateDto.setEmail(email);
        employeeCreateDto.setUsername(username);
        employeeCreateDto.setJmbg(jmbg);
        employeeCreateDto.setDateOfBirth(Long.parseLong(dateOfBirth));
        employeeCreateDto.setAddress(address);
        employeeCreateDto.setPhoneNumber(phoneNumber);
        employeeCreateDto.setGender(gender);
        Optional<Role> role1 = roleRepository.findByRoleName(RoleName.valueOf(role));
        employeeCreateDto.setRole(role1.get());

        try{
            String employeeCreateDtoJson = objectMapper.writeValueAsString(employeeCreateDto);

            System.out.println(employeeCreateDtoJson);

            ResultActions resultActions = mockMvc.perform(
                    post("/api/v1/employee")
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + employeeControllerTestsState.getJwtToken())
                            .content(employeeCreateDtoJson)
            ).andExpect(status().isOk());

            MvcResult mvcResult = resultActions.andReturn();

            String jsonEmployee = mvcResult.getResponse().getContentAsString();
            EmployeeDto employeeDto = objectMapper.readValue(jsonEmployee, EmployeeDto.class);
            assertEquals(ime, employeeDto.getFirstName());
            assertEquals(prezime, employeeDto.getLastName());
            assertEquals(email, employeeDto.getEmail());
        }catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Then("povlacimo zaposlenog iz baze i proveravamo da li je kreiran, pretraga po emailu {string}")
    public void povlacimoZaposlenogIzBazeIProveravamoDaLiJeKreiranPretragaPoEmailu(String email) {
        try{
            ResultActions resultActions = mockMvc.perform(
                    get("/api/v1/employee/findByEmail/" + email)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + employeeControllerTestsState.getJwtToken())
            ).andExpect(status().isOk());

            MvcResult mvcResult = resultActions.andReturn();

            String jsonEmployee = mvcResult.getResponse().getContentAsString();
            EmployeeDto employeeDto = objectMapper.readValue(jsonEmployee, EmployeeDto.class);
            assertEquals(email, employeeDto.getEmail());
        }catch (Exception e) {
            fail(e.getMessage());
        }
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


    @When("setujemo sifru zaposlenom sa emailom {string}")
    public void setujemoSifruZaposlenomSaIdEm(String email) {
        employeeSetPasswordDto.setEmail(email);
        employeeSetPasswordDto.setPassword("damir123");

        try {
            ResultActions resultActions = mockMvc.perform(
                    post("/api/v1/employee/setPassword")
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + employeeControllerTestsState.getJwtToken())
                            .content(objectMapper.writeValueAsString(employeeSetPasswordDto))
            ).andExpect(status().isOk());

            MvcResult mvcResult = resultActions.andReturn();
            String jsonEmployee = mvcResult.getResponse().getContentAsString();
            System.out.println(jsonEmployee);
        }catch (Exception e) {
            fail(e.getMessage());
        }

    }


//    @When("resetovanje sifre zaposlenom sa emailom {string}")
//    public void resetovanjeSifreZaposlenomSaEmailom(String email) {
//        EmployeeSetPasswordDto resetPasswordDTO = new EmployeeSetPasswordDto();
//        resetPasswordDTO.setEmail(email);
//        resetPasswordDTO.setPassword("damir1234");
//
//        try {
//            ResultActions resultActions = mockMvc.perform(
//                    post("/api/v1/employee/resetPassword")
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .accept(MediaType.APPLICATION_JSON)
//                            .header("Authorization", "Bearer " + employeeControllerTestsState.getJwtToken())
//                            .content(objectMapper.writeValueAsString(resetPasswordDTO))
//            ).andExpect(status().isOk());
//
//            MvcResult mvcResult = resultActions.andReturn();
//            String jsonEmployee = mvcResult.getResponse().getContentAsString();
//            System.out.println(jsonEmployee);
//        }catch (Exception e) {
//            fail(e.getMessage());
//        }
//    }
//
    @Then("proveravamo logovanje zaposlenog sa novom sifrom")
    public void proveravamoLogovanjeZaposlenogSaNovomSifrom() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("cehd1234@gmail.com");
        loginRequest.setPassword("damir123");

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
            assertNotEquals(loginResponseForm.getJwt(), null);
        } catch (Exception e) {
            fail(e.getMessage());
        }

    }

    @When("promena adrese zaposlenom sa id-em {string} u {string}")
    public void promenaAdreseZaposlenomSaIdEmU(String id, String address) {
        EmployeeUpdateDto employeeUpdateDto = new EmployeeUpdateDto();
        employeeUpdateDto.setAddress(address);
        employeeUpdateDto.setIsActive(true);

        try {
            ResultActions resultActions = mockMvc.perform(
                    put("/api/v1/employee/{id}", id)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + employeeControllerTestsState.getJwtToken())
                            .content(objectMapper.writeValueAsString(employeeUpdateDto))
            ).andExpect(status().isOk());

            MvcResult mvcResult = resultActions.andReturn();
            String jsonEmployee = mvcResult.getResponse().getContentAsString();
            System.out.println(jsonEmployee);
            employeeDtoPreeSet = objectMapper.readValue(jsonEmployee, EmployeeDto.class);
            assertEquals(address, employeeDtoPreeSet.getAddress());
        }catch (Exception e) {
            fail(e.getMessage());

        }
    }

    @Then("proveravamo zaposlenog po emailu da li mu je adresa {string}")
    public void proveravamoZaposlenogPoEmailuDaLiMuJeAdresa(String address) {
        try{
            ResultActions resultActions = mockMvc.perform(
                    get("/api/v1/employee/findByEmail/" + employeeDtoPreeSet.getEmail())
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + employeeControllerTestsState.getJwtToken())
            ).andExpect(status().isOk());

            MvcResult mvcResult = resultActions.andReturn();

            String jsonEmployee = mvcResult.getResponse().getContentAsString();
            EmployeeDto employeeDto = objectMapper.readValue(jsonEmployee, EmployeeDto.class);
            assertEquals(address, employeeDto.getAddress());
            assertEquals(employeeDto.getEmail(), employeeDtoPreeSet.getEmail());
        }catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Then("proveravamo zaposlenog po username-u da li mu je adresa {string}")
    public void proveravamoZaposlenogPoUsernameUDaLiMuJeAdresa(String address) {
        try{
            ResultActions resultActions = mockMvc.perform(
                    get("/api/v1/employee/findByUsername/" + employeeDtoPreeSet.getUsername())
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + employeeControllerTestsState.getJwtToken())
            ).andExpect(status().isOk());

            MvcResult mvcResult = resultActions.andReturn();

            String jsonEmployee = mvcResult.getResponse().getContentAsString();
            EmployeeDto employeeDto = objectMapper.readValue(jsonEmployee, EmployeeDto.class);
            assertEquals(address, employeeDto.getAddress());
            assertEquals(employeeDto.getUsername(), employeeDtoPreeSet.getUsername());
        }catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Then("proveravamo zaposlenog po search-u da li mu je adresa {string}")
    public void proveravamoZaposlenogPoSearchUDaLiMuJeAdresa(String address) {
        System.out.println(employeeDtoPreeSet.getEmail() + employeeDtoPreeSet.getFirstName()+ employeeDtoPreeSet.getLastName()+ employeeDtoPreeSet.getRole().toString());

        try{
            ResultActions resultActions = mockMvc.perform(
                    get("/api/v1/employee/search")
                            .param("firstName", employeeDtoPreeSet.getFirstName())
                            .param("lastName", employeeDtoPreeSet.getLastName())
                            .param("email", employeeDtoPreeSet.getEmail())
                            .param("role", employeeDtoPreeSet.getRole().getRoleName().toString())
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + employeeControllerTestsState.getJwtToken())
            ).andExpect(status().isOk());

            MvcResult mvcResult = resultActions.andReturn();

            String jsonEmployee = mvcResult.getResponse().getContentAsString();
            EmployeeDto[] employeeDtos = objectMapper.readValue(jsonEmployee, EmployeeDto[].class);
            for(EmployeeDto employeeDto : employeeDtos){
                if(employeeDto.getEmail().equals(employeeDtoPreeSet.getEmail())){
                    assertEquals(address, employeeDto.getAddress());
                }
            }
        }catch (Exception e) {
            fail(e.getMessage());
        }
    }


    @When("zahtev za pregled korisnika")
    public void zahtevZaPregledKorisnika() {
        //Zaposleni zeli da vidi sve korisnike
    }

    @Then("zaposleni dobija listu svih korisnika")
    public void zaposleniDobijaListuSvihKorisnika() {
        try{
            ResultActions resultActions = mockMvc.perform(
                    get("/api/v1/employee/getAll")
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + employeeControllerTestsState.getJwtToken())
            ).andExpect(status().isOk());

            MvcResult mvcResult = resultActions.andReturn();

            String jsonEmployee = mvcResult.getResponse().getContentAsString();
            EmployeeDto[] employeeDtos = objectMapper.readValue(jsonEmployee, EmployeeDto[].class);
            assertTrue(employeeDtos.length > 0);
        }catch (Exception e) {
            fail(e.getMessage());
        }
    }

    //TODO: Otkomentarisati ovde i u employeeController.feature kada se uradi rebase
//    @Then("zaposleni dobija listu svih Exchange zaposlenih")
//    public void zaposleniDobijaListuSvihExchangeZaposlenih() {
//        try{
//            ResultActions resultActions = mockMvc.perform(
//                    get("/api/v1/employee/getExchangeEmployees")
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .accept(MediaType.APPLICATION_JSON)
//                            .header("Authorization", "Bearer " + employeeControllerTestsState.getJwtToken())
//            ).andExpect(status().isOk());
//
//            MvcResult mvcResult = resultActions.andReturn();
//
//            String jsonEmployee = mvcResult.getResponse().getContentAsString();
//            ExchangeEmployeeDto[] exchangeEmployeeDtos = objectMapper.readValue(jsonEmployee, ExchangeEmployeeDto[].class);
//            assertTrue(exchangeEmployeeDtos.length > 0);
//        }catch (Exception e) {
//            fail(e.getMessage());
//        }
//    }

    @When("zaposleni se registruje sa emailom {string}")
    public void zaposleniSeRegistrujeSaEmailom(String email) {

    }

    @Then("neuspesno logovanje i neuspesno brisanje")
    public void neuspesnoLogovanjeINeuspesnoBrnisanje() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("dada@dad.com");
        loginRequest.setPassword("dam");

        try {
            String loginRequestJson = objectMapper.writeValueAsString(loginRequest);

            ResultActions resultActions = mockMvc.perform(
                    post("/api/v1/employee/auth/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .content(loginRequestJson)

            ).andExpect(status().isUnauthorized());

            ResultActions resultActions1 = mockMvc.perform(
                    delete("/api/v1/employee/{id}", 9999)
                            .header("Authorization", "Bearer " + employeeControllerTestsState.getJwtToken())
            ).andExpect(status().isBadRequest());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

}
