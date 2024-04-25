package rs.edu.raf.exchangeservice;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import rs.edu.raf.exchangeservice.client.UserServiceClient;
import rs.edu.raf.exchangeservice.domain.dto.ActuaryDto;
import rs.edu.raf.exchangeservice.domain.model.Actuary;
import rs.edu.raf.exchangeservice.repository.ActuaryRepository;
import rs.edu.raf.exchangeservice.service.ActuaryService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class ActuaryServiceTests {


    @Mock
    private UserServiceClient userServiceClient;

    @Mock
    private ActuaryRepository actuaryRepository;

    @InjectMocks
    private ActuaryService actuaryService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    public void findAllAgents() {
        Actuary actuary = new Actuary();
        actuary.setRole("ROLE_AGENT");
        when(actuaryRepository.findByRole("ROLE_AGENT")).thenReturn(Arrays.asList(actuary));

        List<Actuary> result = actuaryService.findAllAgents();

        assertEquals(1, result.size());
        assertEquals("ROLE_AGENT", result.get(0).getRole());
    }

    @Test
    public void restartLimitUsed() {
        Actuary actuary = new Actuary();
        actuary.setLimitUsed(100.0);
        when(actuaryRepository.findById(anyLong())).thenReturn(Optional.of(actuary));
        when(actuaryRepository.save(any(Actuary.class))).thenAnswer(i -> i.getArguments()[0]);//vrati taj isti objekat koji si save-ovo

        Actuary result = actuaryService.restartLimitUsed(1L);

        assertEquals(0.0, result.getLimitUsed());
    }

    @Test
    public void setLimit() {
        Actuary actuary = new Actuary();
        actuary.setLimitValue(100.0);
        actuary.setLimitUsed(0.0);
        when(actuaryRepository.findById(anyLong())).thenReturn(Optional.of(actuary));
        when(actuaryRepository.save(any(Actuary.class))).thenAnswer(i -> i.getArguments()[0]);

        Actuary result = actuaryService.setLimit(1L, 200.0);

        assertEquals(200.0, result.getLimitValue());
    }

    @Test
    public void setOrderRequest() {
        Actuary actuary = new Actuary();
        actuary.setOrderRequest(false);
        when(actuaryRepository.findById(anyLong())).thenReturn(Optional.of(actuary));
        when(actuaryRepository.save(any(Actuary.class))).thenAnswer(i -> i.getArguments()[0]);

        Actuary result = actuaryService.setOrderRequest(1L, true);

        assertEquals(true, result.isOrderRequest());
    }

    @Test
    public void addActuary() {
        ActuaryDto actuaryDto = new ActuaryDto();
        actuaryDto.setRole("ROLE_AGENT");
        when(actuaryRepository.save(any(Actuary.class))).thenAnswer(i -> i.getArguments()[0]);

        actuaryService.addActuary(actuaryDto);

        verify(actuaryRepository, times(1)).save(any(Actuary.class));
    }
}