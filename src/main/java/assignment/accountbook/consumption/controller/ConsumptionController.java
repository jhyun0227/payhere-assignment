package assignment.accountbook.consumption.controller;

import assignment.accountbook.consumption.dto.ConsumptionDTO;
import assignment.accountbook.consumption.dto.ConsumptionSaveDTO;
import assignment.accountbook.consumption.dto.ConsumptionUpdateDTO;
import assignment.accountbook.consumption.service.ConsumptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/consumption")
public class ConsumptionController {

    private final ConsumptionService consumptionService;

    /**
     * 소비내역 저장
     */
    @PostMapping("/save")
    public ResponseEntity<Map<String, String>> save(@Validated @RequestBody ConsumptionSaveDTO consumptionSaveDTO) {
        ConsumptionDTO saveConsumption = consumptionService.save(consumptionSaveDTO);

        Map<String, String> result = new HashMap<>();
        result.put("message", "소비내역이 정상적으로 저장되었습니다.");
        result.put("consumptionId", String.valueOf(saveConsumption.getConsumptionId()));

        return ResponseEntity.ok().body(result);
    }

    /**
     * 소비내역 상세조회
     */
    @GetMapping("/{consumptionId}")
    public ConsumptionDTO findById(@PathVariable Long consumptionId) {
        return consumptionService.findById(consumptionId);
    }

    /**
     * 유저가 등록한 소비내역 전체조회
     */
    @GetMapping("/findAll")
    public List<ConsumptionDTO> findAll() {
        return consumptionService.findAll();
    }

    /**
     * 유저가 등록한 복구가능내역 조회
     */
    @GetMapping("/findAll/recover")
    public List<ConsumptionDTO> findAllRecover() {
        return consumptionService.findAllRecover();
    }

    /**
     * 소비내역 수정
     */
    @PatchMapping("/update/{consumptionId}")
    public ResponseEntity<Map<String, String>> update(@PathVariable Long consumptionId, @Validated @RequestBody ConsumptionUpdateDTO consumptionUpdateDTO) {
        ConsumptionDTO updateConsumption = consumptionService.update(consumptionId, consumptionUpdateDTO);

        Map<String, String> result = new HashMap<>();
        result.put("message", "소비내역이 정상적으로 수정되었습니다.");
        result.put("consumptionId", String.valueOf(updateConsumption.getConsumptionId()));

        return ResponseEntity.ok().body(result);
    }

    /**
     * 소비내역 삭제
     * 삭제이지만 실제 데이터행을 삭제하지 않고, 삭제여부 필드의 값을 변경한다.
     * 목록조회시 삭제여부 필드값이 Y인 데이터는 보이지 않는다.
     */
    @PatchMapping("/delete/{consumptionId}")
    public ResponseEntity<Map<String, String>> delete(@PathVariable Long consumptionId) {
        ConsumptionDTO deleteConsumption = consumptionService.delete(consumptionId);

        Map<String, String> result = new HashMap<>();
        result.put("message", "소비내역이 삭제되었습니다.");
        result.put("consumptionId", String.valueOf(deleteConsumption.getConsumptionId()));

        return ResponseEntity.ok().body(result);
    }

    /**
     * 소비내역 복구
     * 삭제여부 필드값 Y를 N으로 복구한다.
     * 목록조회시 삭제여부 필드값이 N인 데이터는 정상적으로 조회된다.
     */
    @PatchMapping("/recover/{consumptionId}")
    public ResponseEntity<Map<String, String>> recover(@PathVariable Long consumptionId) {
        ConsumptionDTO recoverConsumption = consumptionService.recover(consumptionId);

        Map<String, String> result = new HashMap<>();
        result.put("message", "소비내역이 복구되었습니다.");
        result.put("consumptionId", String.valueOf(recoverConsumption.getConsumptionId()));

        return ResponseEntity.ok().body(result);
    }


}
