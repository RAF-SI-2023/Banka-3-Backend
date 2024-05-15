package rs.edu.raf.userservice.e2e.byStoks.helper;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class Currency implements Serializable {


    private Long currencyId;

    private String name;

    private String mark;
}
