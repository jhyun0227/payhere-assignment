package assignment.accountbook.consumption.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConsumptionSaveDTO {

    @Positive(message = "금액을 입력해주세요.")
    private int consumptionPrice;

    @NotBlank(message = "메모를 입력해주세요.")
    private String consumptionMemo;

}
