package assignment.accountbook.consumption.repository;

import assignment.accountbook.consumption.dto.ConsumptionUpdateDTO;
import assignment.accountbook.consumption.entity.Consumption;

import java.util.List;
import java.util.Optional;

public interface ConsumptionRepository {

    public Consumption save(Consumption consumption);

    public List<Consumption> findAll(Long memberId);

    public Optional<Consumption> findById(Long consumptionId);

    public Consumption update(Long consumptionId, ConsumptionUpdateDTO consumptionUpdateDTO);

    public Consumption delete(Long consumptionId);

    public Consumption recover(Long consumptionId);
}
