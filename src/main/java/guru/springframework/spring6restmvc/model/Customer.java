package guru.springframework.spring6restmvc.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Data
public class Customer {

    private UUID id;
    private String customerName;
    private int version;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiededDate;
}
