package com.example.emailservice.controller;


import com.example.emailservice.dto.CodeSenderDto;
import com.example.emailservice.dto.CodeSenderEntityDto;
import com.example.emailservice.model.CodeSender;
import com.example.emailservice.service.CodeSenderService;
import com.example.emailservice.service.impl.CodeSenderServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@CrossOrigin()
@RequestMapping("/user")
public class CodeSenderController {

    @Autowired
    private CodeSenderService codeSenderService;
    @PostMapping(value = "/activateUser", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> activateUser(@RequestBody CodeSenderDto codeSenderDto) {
        return codeSenderService.activateUser(codeSenderDto); //feignClient
        //return codeSenderService.activateUserOkHttp(codeSenderDto); //OkHttp
    }
    
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "/findByCode/{code}")
    public CodeSenderEntityDto findCodeSenderByCode(@PathVariable("code") Integer code){
        return codeSenderService.findCodeSenderByCode(code);
    }
}
