package com.example.emailservice.service.impl;

import com.example.emailservice.client.UserServiceClientUser;
import com.example.emailservice.dto.CodeSenderDto;
import com.example.emailservice.dto.CodeSenderEntityDto;
import com.example.emailservice.dto.SetPasswordDTO;
import com.example.emailservice.model.CodeSender;
import com.example.emailservice.repository.CodeSenderRepository;
import com.example.emailservice.service.CodeSenderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class CodeSenderServiceImpl implements CodeSenderService {

    private final CodeSenderRepository codeSenderRepository;
    private final UserServiceClientUser userServiceClientUser;
    private final PasswordEncoder passwordEncoder;

    public CodeSenderServiceImpl(CodeSenderRepository codeSenderRepository, UserServiceClientUser userServiceClientUser, PasswordEncoder passwordEncoder) {
        this.codeSenderRepository = codeSenderRepository;
        this.userServiceClientUser = userServiceClientUser;
        this.passwordEncoder = passwordEncoder;
    }


    /**
     *
     * Ova klasa prima, u obliku codeSenderDto, e-mail, kod za aktivaciju, password i confirmPassword iz kontrolera/
     * Proverava da li se sifre podudaraju, pa zatim trazi da li u bazi postoji taj kod.
     * Ako postoji proverava da li je proslo 5 minuta (300000 milisek) i ako nije dozvoljava aktivaciju naloga, a u suprotnom salje 401.
     * Aktivacija naloga podrazumeva hesiranje sifre, a zatim setovanje iste na user service koristeci OkHttp biblioteku.
     * Nakon zavrsetka posla salje 200 OK response.
     */
    public ResponseEntity<String> activateUserOkHttp(CodeSenderDto codeSenderDto){

        if(!codeSenderDto.getPassword().equals(codeSenderDto.getConfirmPassword()))
            return ResponseEntity.status(400).body("Password and confirm password do not match!");

        CodeSender cs = codeSenderRepository.findCodeSenderByCode(codeSenderDto.getCode()).get();

        if(cs.getCodeSenderID() == null)
            return ResponseEntity.status(400).body("Code not valid.");

        if(System.currentTimeMillis() - cs.getDate() > 300000)
            return ResponseEntity.status(401).body("5 minutes have passed");

        SetPasswordDTO setPasswordDto = new SetPasswordDTO(codeSenderDto.getEmail(), codeSenderDto.getPassword());
        //setPasswordDto.setPassword(passwordEncoder.encode(setPasswordDto.getPassword()));

        // Convert DTO to JSON string using Jackson
        ObjectMapper mapper = new ObjectMapper();
        String jsonBody = null;
        try {
            jsonBody = mapper.writeValueAsString(setPasswordDto);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Password not set.");
        }
        RequestBody requestBody = RequestBody.create(jsonBody, MediaType.parse("application/json"));


        OkHttpClient httpClient = new OkHttpClient();

        String url = "localhost:8080/api/v1/user/setPassword";
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();

        try {
            Response response = httpClient.newCall(request).execute();


            if (!response.isSuccessful()) {
                System.out.println(response.body().string());
                return ResponseEntity.status(500).body("Password not set.");
            }

            // Close response
            response.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return ResponseEntity.ok("Success.");
    }

    /**
     *
     * Ova klasa prima, u obliku codeSenderDto, e-mail, kod za aktivaciju, password i confirmPassword iz kontrolera/
     * Proverava da li se sifre podudaraju, pa zatim trazi da li u bazi postoji taj kod.
     * Ako postoji proverava da li je proslo 5 minuta (300000 milisek) i ako nije dozvoljava aktivaciju naloga, a u suprotnom salje 401.
     * Aktivacija naloga podrazumeva setovanje sifre na user service koristeci FeignClient framework.
     * Nakon zavrsetka posla salje 200 OK response.
     */
    public ResponseEntity<String> activateUser(CodeSenderDto codeSenderDto){


        if(!codeSenderDto.getPassword().equals(codeSenderDto.getConfirmPassword()))
            return ResponseEntity.status(400).body("Password and confirm password do not match!");

        CodeSender cs = codeSenderRepository.findCodeSenderByCode(codeSenderDto.getCode()).get();

        if(cs.getCodeSenderID() == null)
            return ResponseEntity.status(400).body("Code not valid.");

        if(System.currentTimeMillis() - cs.getDate() > 300000)
            return ResponseEntity.status(401).body("5 minutes have passed");

        SetPasswordDTO setPasswordDto = new SetPasswordDTO(codeSenderDto.getPassword(), codeSenderDto.getEmail());
        //setPasswordDto.setPassword(passwordEncoder.encode(setPasswordDto.getPassword()));
        userServiceClientUser.setPassword(setPasswordDto);

        return ResponseEntity.ok("Success.");
    }

    @Override
    public CodeSenderEntityDto findCodeSenderByCode(Integer code) {

        CodeSender codeSender = codeSenderRepository.findCodeSenderByCode(code).get();
        return CodeSenderEntityDto.builder()
                .email(codeSender.getEmail())
                .code(codeSender.getCode())
                .date(codeSender.getDate())
                .active(codeSender.getActive())
                .build();
    }


}
