//package rs.edu.raf.userservice.integration.userController;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import io.cucumber.java.en.Given;
//import io.cucumber.java.en.Then;
//import io.cucumber.java.en.When;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.MvcResult;
//import org.springframework.test.web.servlet.ResultActions;
//import rs.edu.raf.userservice.domain.dto.login.LoginRequest;
//import rs.edu.raf.userservice.domain.dto.user.*;
//import rs.edu.raf.userservice.integration.LoginResponseForm;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//public class UserControllerTestSteps extends UserControllerTestsConfig{
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @Autowired
//    private UserControllerTestState userControllerTestState;
//
//    @Given("logovali smo se kao admin da bi kreirali korisnika")
//    public void logovaliSmoSeKaoAdminDaBiKreiraliKorisnika() {
//        LoginRequest loginRequest = new LoginRequest();
//        loginRequest.setEmail("admin@admin.com");
//        loginRequest.setPassword("admin1234");
//
//        try {
//            String loginRequestJson = objectMapper.writeValueAsString(loginRequest);
//
//            ResultActions resultActions = mockMvc.perform(
//                    post("/api/v1/employee/auth/login")
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .accept(MediaType.APPLICATION_JSON)
//                            .content(loginRequestJson)
//
//            ).andExpect(status().isOk());
//
//            MvcResult result = resultActions.andReturn();
//            String loginResponse = result.getResponse().getContentAsString();
//            LoginResponseForm loginResponseForm = objectMapper.readValue(loginResponse, LoginResponseForm.class);
//            userControllerTestState.setJwtTokenEmpoyee(loginResponseForm.getJwt());
//        } catch (Exception e) {
//            fail(e.getMessage());
//        }
//    }
//
//    @When("kreiramo korsisnika sa imenom {string} i prezimenom {string} i emailom {string} i lozinkom {string}, jmbg {string}")
//    public void kreiramoKorsisnikaSaImenomIPrezimenomIEmailomILozinkomJmbg(String ime, String prezime, String email, String password, String jmbg) {
//        UserPostPutDto userPostPutDto = new UserPostPutDto();
//        userPostPutDto.setFirstName(ime);
//        userPostPutDto.setLastName(prezime);
//        userPostPutDto.setEmail(email);
//        userPostPutDto.setJmbg(jmbg);
//        userPostPutDto.setAddress("Adresa 1");
//        userPostPutDto.setDateOfBirth("123124124213");
//        userPostPutDto.setGender("M");
//        userPostPutDto.setPhoneNumber("1231241512");
//
//        try {
//            String createUserDtoJson = objectMapper.writeValueAsString(userPostPutDto);
//            ResultActions resultActions = mockMvc.perform(
//                    post("/api/v1/user/register")
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .accept(MediaType.APPLICATION_JSON)
//                            .header("Authorization", "Bearer " + userControllerTestState.getJwtTokenEmpoyee())
//                            .content(createUserDtoJson)
//
//            ).andExpect(status().isOk());
//        } catch (Exception e) {
//            fail(e.getMessage());
//        }
//    }
//
//    @Then("povlacimo korisnika sa emailom {string}")
//    public void povlacimoKorisnikaSaEmailom(String email) {
//        try {
//            ResultActions resultActions = mockMvc.perform(
//                    get("/api/v1/user/findByEmail/" + email)
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .accept(MediaType.APPLICATION_JSON)
//                            .header("Authorization", "Bearer " + userControllerTestState.getJwtTokenEmpoyee())
//
//            ).andExpect(status().isOk());
//        }catch (Exception e) {
//            fail(e.getMessage());
//        }
//
//    }
//
//
//    @When("zaposleni banke brise korisnika sa id-em {string}")
//    public void zaposleniBankeBriseKorisnikaSaIdEm(String id) {
//       try {
//           ResultActions resultActions = mockMvc.perform(
//                   delete("/api/v1/user/" +  id)
//                           .header("Authorization", "Bearer " + userControllerTestState.getJwtTokenEmpoyee())
//           ).andExpect(status().isOk());
//       } catch (Exception e) {
//           fail(e.getMessage());
//       }
//
//    }
//
//    @Then("korisnik sa id-em {string} nije aktivan")
//    public void korisnikSaIdEmNijeAktivan(String id) {
//        try {
//            ResultActions resultActions = mockMvc.perform(
//                    get("/api/v1/user/" + id)
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .accept(MediaType.APPLICATION_JSON)
//                            .header("Authorization", "Bearer " + userControllerTestState.getJwtTokenEmpoyee())
//
//            ).andExpect(status().isOk());
//
//            MvcResult result = resultActions.andReturn();
//
//            String response = result.getResponse().getContentAsString();
//            UserDto userDto = objectMapper.readValue(response, UserDto.class);
//
//
//            //TODO: odkomentarisati kada se opsosobi email service
//            ResultActions resultActions1 = mockMvc.perform(
//                    get("/api/v1/user/isUserActive/" + userDto.getEmail())
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .accept(MediaType.APPLICATION_JSON)
//                            .header("Authorization", "Bearer " + userControllerTestState.getJwtTokenEmpoyee())
//
//            ).andExpect(status().isOk());
//
//            MvcResult result1 = resultActions1.andReturn();
//            String responseIsActive = result1.getResponse().getContentAsString();
//            UserDto userDtoIsActive = objectMapper.readValue(responseIsActive, UserDto.class);
//            assertEquals(false, userDto.isActive());
//        }catch (Exception e) {
//            fail(e.getMessage());
//        }
//    }
//
//
//    @Given("korisnik sa emailom {string} i lozinkom {string}")
//    public void korisnikSaEmailomILozinkom(String email, String password) {
//        try{
//            LoginRequest loginRequest = new LoginRequest();
//            loginRequest.setEmail(email);
//            loginRequest.setPassword(password);
//
//            String loginRequestJson = objectMapper.writeValueAsString(loginRequest);
//
//            ResultActions resultActions = mockMvc.perform(
//                    post("/api/v1/user/auth/login")
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .accept(MediaType.APPLICATION_JSON)
//                            .content(loginRequestJson)
//
//            ).andExpect(status().isOk());
//        } catch (Exception e) {
//            fail(e.getMessage());
//        }
//    }
//
//    @When("korisnik setuje sifru {string} i email {string}")
//    public void korisnikSetujeSifruIEmail(String password, String email) {
//        UserSetPasswordDto userSetPasswordDTO = new UserSetPasswordDto();
//        userSetPasswordDTO.setPassword(password);
//        userSetPasswordDTO.setEmail(email);
//
//        try {
//            String setPasswordDTOJson = objectMapper.writeValueAsString(userSetPasswordDTO);
//            ResultActions resultActions = mockMvc.perform(
//                    post("/api/v1/user/setPassword")
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .accept(MediaType.APPLICATION_JSON)
//                            .header("Authorization", "Bearer " + userControllerTestState.getJwtTokenEmpoyee())
//                            .content(setPasswordDTOJson)
//
//            ).andExpect(status().isOk());
//        } catch (Exception e) {
//            fail(e.getMessage());
//        }
//    }
//
//
//    @When("korisnik resetuje sifru {string} i email {string}")
//    public void korisnikResetujeSifruIEmail(String sifra, String email) {
//        ResetUserPasswordDTO resetPasswordDTO = new ResetUserPasswordDTO();
//        resetPasswordDTO.setPassword(sifra);
//        resetPasswordDTO.setEmail(email);
//
//        try {
//            String resetPasswordDTOJson = objectMapper.writeValueAsString(resetPasswordDTO);
//            ResultActions resultActions = mockMvc.perform(
//                    post("/api/v1/user/resetPassword")
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .accept(MediaType.APPLICATION_JSON)
//                            .header("Authorization", "Bearer " + userControllerTestState.getJwtTokenEmpoyee())
//                            .content(resetPasswordDTOJson)
//
//            ).andExpect(status().isOk());
//        } catch (Exception e) {
//            fail(e.getMessage());
//        }
//
//    }
//
//
//    @Then("korisnik se opet loguje {string} i lozinkom {string}")
//    public void korisnikSeOpetLogujeILozinkom(String email, String sifra) {
//        try{
//            LoginRequest loginRequest = new LoginRequest();
//            loginRequest.setEmail(email);
//            loginRequest.setPassword(sifra);
//
//            String loginRequestJson = objectMapper.writeValueAsString(loginRequest);
//
//            ResultActions resultActions = mockMvc.perform(
//                    post("/api/v1/user/auth/login")
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .accept(MediaType.APPLICATION_JSON)
//                            .content(loginRequestJson)
//
//            ).andExpect(status().isOk());
//        } catch (Exception e) {
//            fail(e.getMessage());
//        }
//    }
//
//
//    @When("promena adrese korisniku sa id-em {string} u {string}")
//    public void promenaAdreseKorisnikuSaIdEmU(String id, String address) {
//        UserUpdateDto userUpdateDto = new UserUpdateDto();
//        userUpdateDto.setAddress(address);
//        try {
//            String updateUserDtoJSON = objectMapper.writeValueAsString(userUpdateDto);
//            ResultActions resultActions = mockMvc.perform(
//                    put("/api/v1/user/" + id)
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .accept(MediaType.APPLICATION_JSON)
//                            .header("Authorization", "Bearer " + userControllerTestState.getJwtTokenEmpoyee())
//                            .content(updateUserDtoJSON)
//            ).andExpect(status().isOk());
//        } catch (Exception e) {
//            fail(e.getMessage());
//        }
//
//    }
//
//    @Then("korisnik sa id-em {string} ima adresu {string}")
//    public void korisnikSaIdEmImaAdresu(String id, String address) {
//        try {
//            ResultActions resultActions = mockMvc.perform(
//                    get("/api/v1/user/" + id)
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .accept(MediaType.APPLICATION_JSON)
//                            .header("Authorization", "Bearer " + userControllerTestState.getJwtTokenEmpoyee())
//
//            ).andExpect(status().isOk());
//
//            MvcResult result = resultActions.andReturn();
//
//            String response = result.getResponse().getContentAsString();
//            UserDto userDto = objectMapper.readValue(response, UserDto.class);
//
//            assertEquals(address, userDto.getAddress());
//        }catch (Exception e) {
//            fail(e.getMessage());
//        }
//    }
//
//    @Then("korisnik sa emailom {string} ima adresu {string}")
//    public void korisnikSaEmailomImaAdresu(String email, String address) {
//        try {
//            ResultActions resultActions = mockMvc.perform(
//                    get("/api/v1/user/findByEmail/" + email)
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .accept(MediaType.APPLICATION_JSON)
//                            .header("Authorization", "Bearer " + userControllerTestState.getJwtTokenEmpoyee())
//
//            ).andExpect(status().isOk());
//
//            MvcResult result = resultActions.andReturn();
//
//            String response = result.getResponse().getContentAsString();
//            UserDto userDto = objectMapper.readValue(response, UserDto.class);
//
//            assertEquals(address, userDto.getAddress());
//        }catch (Exception e) {
//            fail(e.getMessage());
//        }
//    }
//
//    @When("zaposleni zeli da vidi sve korisnike")
//    public void zaposleniZeliDaVidiSveKorisnike() {
//        //
//    }
//
//    @Then("zaposleni vidi sve korisnike")
//    public void zaposleniVidiSveKorisnike() {
//        try {
//            ResultActions resultActions = mockMvc.perform(
//                    get("/api/v1/user/getAll")
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .accept(MediaType.APPLICATION_JSON)
//                            .header("Authorization", "Bearer " + userControllerTestState.getJwtTokenEmpoyee())
//
//            ).andExpect(status().isOk());
//
//            MvcResult result = resultActions.andReturn();
//
//            String response = result.getResponse().getContentAsString();
//            UserDto[] userDtos = objectMapper.readValue(response, UserDto[].class);
//
//            assertTrue(userDtos.length > 0);
//        }catch (Exception e) {
//            fail(e.getMessage());
//        }
//    }
//
//
//    @When("zaposleni zeli da vidi korisnika sa odrednjenim vrednostima")
//    public void zaposleniZeliDaVidiKorisnikaSaOdrednjenimVrednostima() {
//
//    }
//
//    @Then("zaposleni pretrazuje korisnika po odredjenim vrednostima")
//    public void zaposleniPretrazujeKorisnikaPoOdredjenimVrednostima() {
//        try {
//            ResultActions resultActions = mockMvc.perform(
//                    get("/api/v1/user/search")
//                            .param("firstName", "Pera")
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .accept(MediaType.APPLICATION_JSON)
//                            .header("Authorization", "Bearer " + userControllerTestState.getJwtTokenEmpoyee())
//
//            ).andExpect(status().isOk());
//
//            MvcResult result = resultActions.andReturn();
//
//            String response = result.getResponse().getContentAsString();
//            UserDto[] userDtos = objectMapper.readValue(response, UserDto[].class);
//
//            assertTrue(userDtos.length > 0);
//        }catch (Exception e) {
//            fail(e.getMessage());
//        }
//    }
//
//    @When("korisnik se registruje sa emailom {string}")
//    public void korisnikSeRegistrujeSaEmailom(String email) {
//        UserPostPutDto userPostPutDto = new UserPostPutDto();
//        userPostPutDto.setFirstName("Pera");
//        userPostPutDto.setLastName("Peric");
//        userPostPutDto.setEmail(email);
//        userPostPutDto.setJmbg("1234567891234");
//        userPostPutDto.setAddress("Adresa 1");
//        userPostPutDto.setDateOfBirth("123124124213");
//        userPostPutDto.setGender("M");
//        userPostPutDto.setPhoneNumber("1231241512");
//
//        try {
//            String createUserDtoJson = objectMapper.writeValueAsString(userPostPutDto);
//            ResultActions resultActions = mockMvc.perform(
//                    post("/api/v1/user")
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .accept(MediaType.APPLICATION_JSON)
//                            .content(createUserDtoJson)
//                            .header("Authorization", "Bearer " + userControllerTestState.getJwtTokenEmpoyee())
//            ).andExpect(status().isOk());
//        } catch (Exception e) {
//            fail(e.getMessage());
//        }
//
//    }
//
//    @Then("neuspesno logovanje korisika")
//    public void neuspesnoLogovanjeKorisika() {
//        try{
//            LoginRequest loginRequest = new LoginRequest();
//            loginRequest.setEmail("dasdas@dasd.com");
//            loginRequest.setPassword("dasdasdas");
//
//            String loginRequestJson = objectMapper.writeValueAsString(loginRequest);
//
//            ResultActions resultActions = mockMvc.perform(
//                    post("/api/v1/user/auth/login")
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .accept(MediaType.APPLICATION_JSON)
//                            .content(loginRequestJson)
//            ).andExpect(status().isUnauthorized());
//        } catch (Exception e) {
//            fail(e.getMessage());
//        }
//    }
//}
