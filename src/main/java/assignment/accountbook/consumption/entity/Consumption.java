package assignment.accountbook.consumption.entity;

import assignment.accountbook.member.entity.Member;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Consumption {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long consumptionId;

    @Column(nullable = false)
    private LocalDate consumptionDate;

    @Column(nullable = false)
    private int consumptionPrice;

    @Column(nullable = false)
    private String consumptionMemo; //소비메모

    @Column(nullable = false, length = 1)
    private String consumptionDelete; //삭제여부

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    //===== 비즈니스 메소드 =====//
    public void update(int consumptionPrice, String consumptionMemo) {
        this.consumptionPrice = consumptionPrice;
        this.consumptionMemo = consumptionMemo;
    }

    public void delete() {
        this.consumptionDelete = "Y";
    }

    public void recover() {
        this.consumptionDelete = "N";
    }
}
