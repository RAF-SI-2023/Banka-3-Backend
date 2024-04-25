package rs.edu.raf.exchangeservice;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import rs.edu.raf.exchangeservice.domain.dto.listing.FutureDto;
import rs.edu.raf.exchangeservice.domain.model.listing.Future;
import rs.edu.raf.exchangeservice.repository.listingRepository.FutureRepository;
import rs.edu.raf.exchangeservice.service.listingService.FutureService;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class FutureServiceTests {

    @Mock
    private FutureRepository futureRepository;

    @InjectMocks
    private FutureService futureService;

    @Test
    public void testFindAll() {

        List<Future> futures = createDummyFutures();

        given(futureRepository.findAll()).willReturn(futures);

        List<Future> futureList = futureService.findAll();
        for(Future future: futureList) {
            boolean found = false;
            for(Future future1: futures) {
                if(future.getFutureId().equals(future1.getFutureId())) {
                    found = true;
                    break;
                }
            }

            if(!found)
                fail("Future not found!");
        }
    }

    @Test
    public void testFindByContractName() {
        String contractName = "contract";
        Future future = createDummyFuture();
        future.setContractName(contractName);

        given(futureRepository.findByContractName(contractName)).willReturn(Optional.of(future));

        FutureDto futureDto = futureService.findByContractName(contractName);

        assertEquals(future.getContractName(), futureDto.getContractName());
    }

    private Future createDummyFuture() {
        Future future = new Future();
        future.setFutureId(1L);
        return future;
    }

    private List<Future> createDummyFutures() {
        Future future1 = new Future();
        future1.setFutureId(1L);

        Future future2 = new Future();
        future2.setFutureId(2L);

        Future future3 = new Future();
        future3.setFutureId(3L);

        return List.of(future1, future2, future3);
    }
}
