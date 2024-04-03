package rs.edu.raf.exchangeservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rs.edu.raf.exchangeservice.domain.model.Forex;
import rs.edu.raf.exchangeservice.service.ForexService;

import java.util.List;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/forex")
public class ForexController {
    private final ForexService forexService;

    @GetMapping()
    public ResponseEntity<List<Forex>> getAll(){
        return ResponseEntity.ok(this.forexService.findAll());
    }

    //kada korisnik inicira refresh
    @GetMapping("/refresh")
    public ResponseEntity<List<Forex>> getAllRefreshed() throws JsonProcessingException {
        return ResponseEntity.ok(this.forexService.findAllRefreshed());
    }
}
