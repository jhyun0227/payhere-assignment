package assignment.accountbook.consumption.service;

import assignment.accountbook.config.WithMockCustomUser;
import assignment.accountbook.consumption.dto.ConsumptionDTO;
import assignment.accountbook.consumption.dto.ConsumptionSaveDTO;
import assignment.accountbook.consumption.dto.ConsumptionUpdateDTO;
import assignment.accountbook.exception.consumption.DoesNotExitsConsumption;
import assignment.accountbook.member.dto.MemberDTO;
import assignment.accountbook.member.dto.MemberSaveDTO;
import assignment.accountbook.member.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class ConsumptionServiceTest {

    @Autowired
    MemberService memberService;
    @Autowired
    ConsumptionService consumptionService;

    @BeforeEach
    void memberJoin() {
        //Consumption 정보 등록을 하기 위해, memberId 값이 필수로 입력이 필요
        //WithMockCustomUser를 통해 Security Context에 인증정보가 있지만, 인증정보에 memberId 값은 포함되지 않는다.
        //Service에서 memberEmail 이용해 member 정보를 받아오는 메서드가 존재하기 때문에 테스트 전 DB에 정보를 넣어주어야 한다.
        MemberSaveDTO memberSaveDTO = new MemberSaveDTO("12345@12345.com", "12345");
        assertThat(memberService.join(memberSaveDTO)).isInstanceOf(MemberDTO.class);
    }

    @Test
    @WithMockCustomUser
    void 소비내역저장() {
        // 1-1.소비내역 저장
        ConsumptionSaveDTO saveDTO = new ConsumptionSaveDTO(10000, "테스트");

        // 1-2.정상적으로 소비내역이 저장이 되면 ConsumptionDTO를 반환한다.
        assertThat(consumptionService.save(saveDTO)).isInstanceOf(ConsumptionDTO.class);
    }

    @Test
    @WithMockCustomUser
    void 소비내역상세조회() {
        // 정상적으로 소비내역이 조회되는 경우

        // 1-1.소비내역 저장 후, consumptionId 확인
        ConsumptionSaveDTO saveDTO = new ConsumptionSaveDTO(10000, "테스트");
        ConsumptionDTO savedConsumption = consumptionService.save(saveDTO);
        Long consumptionId = savedConsumption.getConsumptionId();

        // 1-2.정상적으로 소비내역이 조회되었을 경우 ConsumptionDTO를 반환한다.
        assertThat(consumptionService.findById(consumptionId)).isInstanceOf(ConsumptionDTO.class);

        //=====================================================================================//

        // 정상적으로 소비내역이 조회되지 않는 경우 예외를 발생한다.
        Long wrongConsumptionId = consumptionId + 1L;
        assertThatThrownBy(() -> consumptionService.findById(wrongConsumptionId))
                .isInstanceOf(DoesNotExitsConsumption.class);
    }

    @Test
    @WithMockCustomUser
    void 소비내역목록조회() {
        // 정상적으로 소비내역 목록 조회 (삭제여부가 'N'인 목록만을 조회한다.)
        // 1-1.소비내역 목록 2개 저장
        ConsumptionSaveDTO saveDTO1 = new ConsumptionSaveDTO(10000, "테스트1");
        ConsumptionSaveDTO saveDTO2 = new ConsumptionSaveDTO(20000, "테스트2");
        consumptionService.save(saveDTO1);
        consumptionService.save(saveDTO2);

        // 1-2.소비내역 조회
        List<ConsumptionDTO> consumptions = consumptionService.findAll();

        // 1-3. 정상적으로 소비내역이 조회되는 경우 2개의 목록을 반환한다.
        assertThat(consumptions.size()).isEqualTo(2);
    }

    @Test
    @WithMockCustomUser
    void 복구가능소비내역목록조회() {
        // 정상적으로 복구가능 소비내역 목록 조회 (삭제여부가 'Y'인 목록만을 조회한다.)
        // 1-1.3개의 소비내역 저장하고, consumptionId를 확인한다.
        ConsumptionSaveDTO saveDTO1 = new ConsumptionSaveDTO(10000, "테스트1");
        ConsumptionSaveDTO saveDTO2 = new ConsumptionSaveDTO(20000, "테스트2");
        ConsumptionSaveDTO saveDTO3 = new ConsumptionSaveDTO(30000, "테스트3");

        consumptionService.save(saveDTO1);
        ConsumptionDTO savedConsumption2 = consumptionService.save(saveDTO2);
        ConsumptionDTO savedConsumption3 = consumptionService.save(saveDTO3);

        // 1-2.3개의 소비내역 중 2개의 소비내역을 삭제
        consumptionService.delete(savedConsumption2.getConsumptionId());
        consumptionService.delete(savedConsumption3.getConsumptionId());

        // 1-2.복구가능한 소비내역 조회
        List<ConsumptionDTO> consumptions = consumptionService.findAllRecover();

        // 1-3. 정상적으로 소비내역이 조회되는 경우 2개의 목록을 반환한다.
        assertThat(consumptions.size()).isEqualTo(2);
    }

    @Test
    @WithMockCustomUser
    void 소비내역수정() {
        // 정상적으로 소비내역이 수정
        // 1-1.소비내역 저장 후, consumptionId 확인
        ConsumptionSaveDTO saveDTO = new ConsumptionSaveDTO(10000, "테스트");

        ConsumptionDTO savedConsumption = consumptionService.save(saveDTO);
        Long consumptionId = savedConsumption.getConsumptionId();

        // 1-2.수정할 소비내역 객체 생성 후 수정
        ConsumptionUpdateDTO updateDTO = new ConsumptionUpdateDTO(20000, "수정 테스트");
        ConsumptionDTO updatedConsumption = consumptionService.update(consumptionId, updateDTO);

        // 1-2.정상적으로 소비내역이 수정되었을 경우 반환하는 DTO의 Id, Price, Memo 필드 값은 파라미터의 필드와 같다.
        assertThat(consumptionId).isEqualTo(updatedConsumption.getConsumptionId());
        assertThat(updateDTO.getConsumptionPrice()).isEqualTo(updatedConsumption.getConsumptionPrice());
        assertThat(updateDTO.getConsumptionMemo()).isEqualTo(updatedConsumption.getConsumptionMemo());
    }

    @Test
    @WithMockCustomUser
    void 소비내역삭제() {
        // 정상적으로 소비내역이 삭제
        // 1-1.소비내역 저장 후, consumptionId 확인
        ConsumptionSaveDTO saveDTO = new ConsumptionSaveDTO(10000, "테스트");
        ConsumptionDTO savedConsumption = consumptionService.save(saveDTO);
        Long consumptionId = savedConsumption.getConsumptionId();

        // 1-2.consumptionId를 통해 소비내역 삭제
        ConsumptionDTO deletedConsumption = consumptionService.delete(consumptionId);

        // 1-3.정상적으로 소비내역이 삭제되었을 경우 반환하는 DTO의 Id값이 파라미터와 일치하고, 삭제여부가 'Y'가 된다.
        assertThat(consumptionId).isEqualTo(deletedConsumption.getConsumptionId());
        assertThat("Y").isEqualTo(deletedConsumption.getConsumptionDelete());
    }

    @Test
    @WithMockCustomUser
    void 소비내역복구() {
        // 정상적으로 소비내역이 복구
        // 1-1.소비내역 저장 후, consumptionId 확인
        ConsumptionSaveDTO saveDTO = new ConsumptionSaveDTO(10000, "테스트");
        ConsumptionDTO savedConsumption = consumptionService.save(saveDTO);
        Long consumptionId = savedConsumption.getConsumptionId();

        // 1-2.consumptionId를 통해 소비내역 삭제
        ConsumptionDTO deletedConsumption = consumptionService.delete(consumptionId);

        // 1-3.정상적으로 소비내역이 삭제되었을 경우 반환하는 DTO의 Id값이 파라미터와 일치하고, 삭제여부가 'Y'가 된다.
        assertThat(consumptionId).isEqualTo(deletedConsumption.getConsumptionId());
        assertThat("Y").isEqualTo(deletedConsumption.getConsumptionDelete());

        // 1-4.삭제된 소비내역을 다시 복구
        ConsumptionDTO recoveredConsumption = consumptionService.recover(consumptionId);

        // 1-5.정상적으로 소비내역이 복구되었을 경우 반환하는 DTO의 Id값이 파라미터와 일치하고, 삭제여부가 'N'이 된다.
        assertThat(consumptionId).isEqualTo(recoveredConsumption.getConsumptionId());
        assertThat("N").isEqualTo(recoveredConsumption.getConsumptionDelete());
    }
}