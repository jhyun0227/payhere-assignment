package assignment.accountbook.consumption.repository;

import assignment.accountbook.consumption.dto.ConsumptionUpdateDTO;
import assignment.accountbook.consumption.entity.Consumption;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class JpaConsumptionRepositoryImpl implements ConsumptionRepository {

    private final EntityManager em;

    @Override
    public Consumption save(Consumption consumption) {
        em.persist(consumption);
        return consumption;
    }

    @Override
    public List<Consumption> findAll(Long memberId) {
        String jpql = "select c from Consumption c where c.member.memberId = :memberId";

        return em.createQuery(jpql, Consumption.class)
                .setParameter("memberId", memberId)
                .getResultList();
    }

    @Override
    public Optional<Consumption> findById(Long consumptionId) {
        //Optional로 반환 받기 위해 아래 코드 사용
        /*
        Consumption consumption = em.find(Consumption.class, consumptionId);
        */

        String jpql = "select c from Consumption c where c.consumptionId = :consumptionId";

        List<Consumption> consumption = em.createQuery(jpql, Consumption.class)
                .setParameter("consumptionId", consumptionId)
                .getResultList();

        return consumption.stream().findAny();
    }

    @Override
    public Consumption update(Long consumptionId, ConsumptionUpdateDTO consumptionUpdateDTO) {
        //존재하는 거래내역의 여부를 Service에서 조회하기 때문에, Optional로 받을 필요 없음
        Consumption consumption = em.find(Consumption.class, consumptionId);

        //수정내역을 저장한다.
        consumption.update(consumptionUpdateDTO.getConsumptionPrice(),
                                consumptionUpdateDTO.getConsumptionMemo());

        return consumption;
    }

    @Override
    public Consumption delete(Long consumptionId) {
        //삭제할 거래내역을 조회
        //존재하는 거래내역의 여부를 Service에서 조회하기 때문에, Optional로 받을 필요 없음
        Consumption consumption = em.find(Consumption.class, consumptionId);

        //삭제여부의 값을 N -> Y로 변경한다.
        consumption.delete();

        return consumption;
    }

    @Override
    public Consumption recover(Long consumptionId) {
        //복구할 거래내역을 조회
        //존재하는 거래내역의 여부를 Service에서 조회하기 때문에, Optional로 받을 필요 없음
        Consumption consumption = em.find(Consumption.class, consumptionId);

        //삭제여부의 값을 Y -> N으로 변경한다.
        consumption.recover();

        return consumption;
    }
}
