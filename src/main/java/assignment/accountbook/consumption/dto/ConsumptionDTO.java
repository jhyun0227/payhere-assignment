package assignment.accountbook.consumption.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConsumptionDTO {

    private Long consumptionId;
    private LocalDate consumptionDate;
    private int consumptionPrice;
    private String consumptionMemo;
    private String consumptionDelete;

}
