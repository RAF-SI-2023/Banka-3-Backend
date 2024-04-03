package rs.edu.raf.exchangeservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.edu.raf.exchangeservice.domain.model.Option;
import rs.edu.raf.exchangeservice.service.OptionService;

import java.util.List;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/option")
public class OptionController {
    private final OptionService optionService;

    @GetMapping("/calls/{ticker}")
    public ResponseEntity<List<Option>> getAllCalls(@PathVariable String ticker){
        return ResponseEntity.ok(optionService.findCalls(ticker));
    }

    @GetMapping("/puts/{ticker}")
    public ResponseEntity<List<Option>> getAllPutss(@PathVariable String ticker){
        return ResponseEntity.ok(optionService.findPuts(ticker));
    }

    @GetMapping("/refresh")
    public ResponseEntity<List<Option>> getAllRefreshed() throws JsonProcessingException {
        return ResponseEntity.ok(this.optionService.findAllRefreshed());
    }
}
