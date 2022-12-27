package assignment.accountbook.consumption.service;

import assignment.accountbook.consumption.dto.ConsumptionDTO;
import assignment.accountbook.consumption.dto.ConsumptionSaveDTO;
import assignment.accountbook.consumption.dto.ConsumptionUpdateDTO;
import assignment.accountbook.consumption.entity.Consumption;
import assignment.accountbook.consumption.repository.ConsumptionRepository;
import assignment.accountbook.exception.consumption.DoesNotExitsConsumption;
import assignment.accountbook.exception.member.DoesNotExistMember;
import assignment.accountbook.exception.member.NotAuthentication;
import assignment.accountbook.member.entity.Member;
import assignment.accountbook.member.repository.MemberRepository;
import assignment.accountbook.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConsumptionService {

    private final ConsumptionRepository consumptionRepository;
    private final MemberRepository memberRepository;

    /**
     * 소비내역 저장
     */
    @Transactional
    public ConsumptionDTO save(ConsumptionSaveDTO consumptionSaveDTO) {
        //Security Context에 저장된 member의 정보를 가져온다.
        Member member = getMember();

        //데이터 저장시 오늘날짜 구하기
        LocalDate now = LocalDate.now();
        log.info(now.toString());

        //데이터 저장을 위해 Entity 빌더
        Consumption consumption = Consumption.builder()
                .consumptionDate(now)
                .consumptionPrice(consumptionSaveDTO.getConsumptionPrice())
                .consumptionMemo(consumptionSaveDTO.getConsumptionMemo())
                .consumptionDelete("N")
                .member(member)
                .build();

        //소비내역 저장
        Consumption saveConsumption = consumptionRepository.save(consumption);

        return toDTO(saveConsumption);

    }

    /**
     * 소비내역 상세조회
     */
    @Transactional(readOnly = true)
    public ConsumptionDTO findById(Long consumptionId) {
        Optional<Consumption> findConsumption = consumptionRepository.findById(consumptionId);

        if (findConsumption.isEmpty()) {
            throw new DoesNotExitsConsumption("존재하지 않는 소비내역입니다.");
        }

        return this.toDTO(findConsumption.get());
    }

    /**
     * 유저가 등록한 소비내역 전체조회
     */
    @Transactional(readOnly = true)
    public List<ConsumptionDTO> findAll() {
        //Security Context에 저장된 member의 정보를 가져온다.
        Member member = getMember();

        //유저ID로 저장한 소비내역 조회
        List<Consumption> findConsumptions = consumptionRepository.findAll(member.getMemberId());

        //결과를 담은 DTO 컬렉션 생성
        List<ConsumptionDTO> resultList = new ArrayList<>();

        //삭제여부가 N인 목록 조회 후 DTO 변환
        findConsumptions.stream()
                .filter(c -> "N".equals(c.getConsumptionDelete()))
                .forEach(c -> resultList.add(this.toDTO(c)));

        return resultList;
    }

    /**
     * 유저가 등록한 복구가능내역 조회
     */
    @Transactional(readOnly = true)
    public List<ConsumptionDTO> findAllRecover() {
        //Security Context에 저장된 member의 정보를 가져온다.
        Member member = getMember();

        //유저ID로 저장한 소비내역 조회
        List<Consumption> findConsumptions = consumptionRepository.findAll(member.getMemberId());

        //결과를 담은 DTO 컬렉션 생성
        List<ConsumptionDTO> resultList = new ArrayList<>();

        //삭제여부가 Y인 목록 조회 후 DTO 변환
        findConsumptions.stream()
                .filter(c -> "Y".equals(c.getConsumptionDelete()))
                .forEach(c -> resultList.add(this.toDTO(c)));

        return resultList;
    }


    /**
     * 소비내역 수정
     */
    @Transactional
    public ConsumptionDTO update(Long consumptionId, ConsumptionUpdateDTO consumptionUpdateDTO) {
        //존재하는 소비내역인지 우선 조회
        this.findById(consumptionId);

        //변경DTO와 변경할 데이터의 PK값을 같이 보낸다.
        Consumption updateConsumption = consumptionRepository.update(consumptionId, consumptionUpdateDTO);

        return this.toDTO(updateConsumption);
    }

    /**
     * 소비내역 삭제
     * 삭제이지만 실제 데이터행을 삭제하지 않고, 삭제여부 필드의 값을 변경한다.
     * 목록조회시 삭제여부 필드값이 Y인 데이터는 보이지 않는다.
     */
    @Transactional
    public ConsumptionDTO delete(Long consumptionId) {
        //존재하는 소비내역인지 우선 조회
        this.findById(consumptionId);

        //삭제요청
        Consumption deleteConsumption = consumptionRepository.delete(consumptionId);

        return this.toDTO(deleteConsumption);
    }

    /**
     * 소비내역 복구
     * 삭제여부 필드값 Y를 N으로 복구한다.
     * 목록조회시 삭제여부 필드값이 N인 데이터는 정상적으로 조회된다.
     */
    @Transactional
    public ConsumptionDTO recover(Long consumptionId) {
        //존재하는 소비내역인지 우선 조회
        this.findById(consumptionId);

        //복구 요청
        Consumption recoverConsumption = consumptionRepository.recover(consumptionId);

        return this.toDTO(recoverConsumption);
    }

    /**
     * ConsumptionDTO 생성 중복 코드 리팩토링
     */
    public ConsumptionDTO toDTO(Consumption consumption) {
        return new ConsumptionDTO(consumption.getConsumptionId(),
                consumption.getConsumptionDate(),
                consumption.getConsumptionPrice(),
                consumption.getConsumptionMemo(),
                consumption.getConsumptionDelete());
    }

    /**
     * Security Context에 저장된 member 정보를 가져오는 메서드
     */
    private Member getMember() {
        //Security Context에서 memberEmail을 가져오기
        Optional<String> currentMemberEmail = SecurityUtil.getCurrentMemberEmail();

        if (currentMemberEmail.isEmpty()) {
            throw new NotAuthentication("인증 되지 않은 회원입니다.");
        }

        //memberEmail을 통해 member_id값을 조회
        Optional<Member> member = memberRepository.findByEmail(currentMemberEmail.get());

        if (member.isEmpty()) {
            throw new DoesNotExistMember("존재하지 않는 회원입니다.");
        }

        return member.get();
    }

}
