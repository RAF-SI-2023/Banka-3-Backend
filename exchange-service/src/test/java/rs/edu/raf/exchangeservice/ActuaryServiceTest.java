package rs.edu.raf.exchangeservice;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import rs.edu.raf.exchangeservice.client.UserServiceClient;
import rs.edu.raf.exchangeservice.domain.dto.ActuaryDto;
import rs.edu.raf.exchangeservice.domain.model.Actuary;
import rs.edu.raf.exchangeservice.repository.ActuaryRepository;
import rs.edu.raf.exchangeservice.service.ActuaryService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ActuaryServiceTest {

    @Mock
    private UserServiceClient userServiceClient;

    @Mock
    private ActuaryRepository actuaryRepository;

    @InjectMocks
    private ActuaryService actuaryService;

    @Test
    public void testLoadActuaryAgent() {
        
        ActuaryDto actuaryDto = new ActuaryDto();
        actuaryDto.setEmployeeId(1L);
        actuaryDto.setRole("AGENT");
        actuaryDto.setEmail("test@test.com");
        when(userServiceClient.getEmployees()).thenReturn(ResponseEntity.ok(Arrays.asList(actuaryDto)));
        
        actuaryService.loadActuary();

        verify(actuaryRepository, times(1)).save(any(Actuary.class));
    }

    @Test
    public void testLoadActuaryNonAgent() {

        ActuaryDto actuaryDto = new ActuaryDto();
        actuaryDto.setEmployeeId(1L);
        actuaryDto.setRole("A");
        actuaryDto.setEmail("test@test.com");
        when(userServiceClient.getEmployees()).thenReturn(ResponseEntity.ok(Arrays.asList(actuaryDto)));

        actuaryService.loadActuary();

        verify(actuaryRepository, times(1)).save(any(Actuary.class));
    }

    @Test
    public void testFindAllAgents() {
        
        when(actuaryRepository.findByRole("ROLE_AGENT")).thenReturn(Arrays.asList(new Actuary()));

        actuaryService.findAllAgents();

        verify(actuaryRepository, times(1)).findByRole("ROLE_AGENT");
    }

    @Test
    public void testRestartLimitUsed() {
        
        Actuary actuary = new Actuary();
        when(actuaryRepository.findById(anyLong())).thenReturn(Optional.of(actuary));

        actuaryService.restartLimitUsed(1L);

        verify(actuaryRepository, times(1)).save(any(Actuary.class));
    }

    @Test
    public void testSetLimit() {
        
        Actuary actuary = new Actuary();
        when(actuaryRepository.findById(anyLong())).thenReturn(Optional.of(actuary));

        actuaryService.setLimit(1L, 1000.0);

        verify(actuaryRepository, times(1)).save(any(Actuary.class));
    }

    @Test
    public void testSetOrderRequest() {
        
        Actuary actuary = new Actuary();
        when(actuaryRepository.findById(anyLong())).thenReturn(Optional.of(actuary));

        actuaryService.setOrderRequest(1L, true);

        verify(actuaryRepository, times(1)).save(any(Actuary.class));
    }

    @Test
    public void testMyScheduledFunction() {
        
        actuaryService.myScheduledFunction();

        verify(actuaryRepository, times(1)).updateLimitToZeroForAllEmployees();
    }
}
