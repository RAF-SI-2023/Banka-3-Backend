package rs.edu.raf.userservice.integration.contactController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import rs.edu.raf.userservice.domain.dto.contact.ContactDto;
import rs.edu.raf.userservice.domain.dto.login.LoginRequest;
import rs.edu.raf.userservice.integration.LoginResponseForm;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ContactControllerTestSteps extends ContactControllerTestsConfig{

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private ContactControllerTestsState contactControllerTestsState;


    @When("Korisnik kreira kontakt userId {string}, myName {string}, name {string}, accountNumber {string}")
    public void korisnikKreiraKontaktUserIdMyNameNameAccountNumber
            (String userId, String myName, String name, String accNumber) {
        ContactCreateDto ccDto = new ContactCreateDto();
        ccDto.setMyName(myName);
        ccDto.setName(name);
        ccDto.setAccountNumber(accNumber);

        try {
            String ccDtoJson = objectMapper.writeValueAsString(ccDto);
            ResultActions resultActions = mockMvc.perform(
                    post("/api/v1/contact/" + userId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .content(ccDtoJson)
            ).andExpect(status().isOk());
            MvcResult result = resultActions.andReturn();

            String contactResponse = result.getResponse().getContentAsString();
            ContactDto contactDto = objectMapper.readValue(contactResponse, ContactDto.class);
            assertEquals(contactDto.getMyName(), myName);
            assertEquals(contactDto.getName(), name);
            assertEquals(contactDto.getAccountNumber(), accNumber);
        } catch (JsonProcessingException e) {
            fail(e.getMessage());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Given("Korisnik logovan")
    public void korisnikLogovan() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("sljubicic7120rn@raf.rs");
        loginRequest.setPassword("strahinja1234");

        try{
            String loginRequestJson = objectMapper.writeValueAsString(loginRequest);

            ResultActions resultActions = mockMvc.perform(
                post("/api/v1/user/auth/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .content(loginRequestJson)
            ).andExpect(status().isOk());

            MvcResult result = resultActions.andReturn();
            String loginResponse = result.getResponse().getContentAsString();
            LoginResponseForm loginResponseForm = objectMapper.readValue(loginResponse, LoginResponseForm.class);
            contactControllerTestsState.setJwtToken(loginResponseForm.getJwt());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }


    @Then("Korisniku userId {string} se vracaju svi njegovi kontakti, zajedno sa ovim novim")
    public void korisnikuUserIdSeVracajuSviNjegoviKontaktiZajednoSaOvimNovim(String userId) {
        try{
            ResultActions resultActions = mockMvc.perform(
                get("/api/v1/contact/" + userId)
                    .header("Authorization", "Bearer " + contactControllerTestsState.getJwtToken())
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
            ).andExpect(status().isOk());

            MvcResult result = resultActions.andReturn();
            String contactsResponse = result.getResponse().getContentAsString();
            ContactDto[] contactDtos = objectMapper.readValue(contactsResponse, ContactDto[].class);

            for(ContactDto cdto: contactDtos){
                if(cdto.getMyName().equals("Placanje teretane")){
                    assertEquals(cdto.getName(), "Zikica Zikic");
                    assertEquals(cdto.getAccountNumber(), "33333333333");
                }
            }

        } catch (Exception e) {
            fail(e.getMessage());
        }
    }


    @Given("Admin logovan")
    public void adminLogovan() {
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
            contactControllerTestsState.setJwtToken(loginResponseForm.getJwt());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @When("Admin zatrazi sve kontakte")
    public void adminZatraziSveKontakte() {

    }

    @When("Admin zatrazi sve kontakte i jedan obrise onaj sa id {string}, a drugi id {string} promeni myName {string}")
    public void adminZatraziSveKontakteIJedanObriseOnajSaIdADrugiIdPromeniMyName
            (String idZaBrisanje, String idZaMenjanje, String myName) {
        try{
            ResultActions resultActions = mockMvc.perform(
                    get("/api/v1/contact/getAll")
                            .header("Authorization", "Bearer " + contactControllerTestsState.getJwtToken())
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
            ).andExpect(status().isOk());

            MvcResult result = resultActions.andReturn();
            String contactsResponse = result.getResponse().getContentAsString();
            ContactDto[] contactDtos = objectMapper.readValue(contactsResponse, ContactDto[].class);

            //r compose up -dassertEquals(true, contactDtos.length > 0);

            for(ContactDto cdto: contactDtos){
                if(cdto.getId().equals(Long.valueOf(idZaBrisanje))){
                    mockMvc.perform(
                            delete("/api/v1/contact/" + cdto.getId())
                                    .header("Authorization", "Bearer " + contactControllerTestsState.getJwtToken())
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .accept(MediaType.APPLICATION_JSON)
                    ).andExpect(status().isOk());
                }
                if(cdto.getId().equals(Long.valueOf(idZaMenjanje))){
                    ContactCreateDto ccDto = new ContactCreateDto();
                    ccDto.setMyName(myName);
                    ccDto.setName(cdto.getName());
                    ccDto.setAccountNumber(cdto.getAccountNumber());

                    String ccDtoJson = objectMapper.writeValueAsString(ccDto);
                    ResultActions resultActions1 = mockMvc.perform(
                            put("/api/v1/contact/" + cdto.getId())
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .accept(MediaType.APPLICATION_JSON)
                                    .content(ccDtoJson)
                    ).andExpect(status().isOk());
                    MvcResult result1 = resultActions1.andReturn();

                    String contactResponse = result1.getResponse().getContentAsString();
                    ContactDto contactDto = objectMapper.readValue(contactResponse, ContactDto.class);
                    assertEquals(contactDto.getMyName(), myName);
                    assertEquals(contactDto.getName(), cdto.getName());
                    assertEquals(contactDto.getAccountNumber(), cdto.getAccountNumber());
                }
            }
        }catch (Exception e){
            fail(e.getMessage());
        }
    }

    @Then("Adminu se vracaju svi kontakti, osim onog obrisanog sa id {string}, i izmenjeni kontakt")
    public void adminuSeVracajuSviKontaktiOsimOnogObrisanogSaIdIIzmenjeniKontakt(String idObrisanog) {
        try {
            ResultActions resultActions = mockMvc.perform(
                    get("/api/v1/contact/getAll")
                            .header("Authorization", "Bearer " + contactControllerTestsState.getJwtToken())
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
            ).andExpect(status().isOk());

            MvcResult result = resultActions.andReturn();
            String contactsResponse = result.getResponse().getContentAsString();
            ContactDto[] contactDtos = objectMapper.readValue(contactsResponse, ContactDto[].class);

            for(ContactDto dto: contactDtos){
                assertEquals(false, dto.getId().equals(Long.valueOf(idObrisanog)));
            }
        }catch (Exception e){
            fail(e.getMessage());
        }
    }
}
