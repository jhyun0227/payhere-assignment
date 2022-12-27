package assignment.accountbook.member.repository;

import assignment.accountbook.member.entity.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class JpaMemberRepositoryImpl implements MemberRepository {

    private final EntityManager em;

    @Override
    public Member save(Member member) {
        em.persist(member);
        return member;
    }

    @Override
    public Optional<Member> findByEmail(String memberEmail) {
        String jpql = "select m from Member m where m.memberEmail = :memberEmail";

        //Optional로 반환받기 위해 위의 코드 사용하지 않음.
        /*
        Member member = em.createQuery(jpql, Member.class)
                .setParameter("memberEmail", memberEmail)
                .getSingleResult();
        */

        List<Member> findMember = em.createQuery(jpql, Member.class)
                .setParameter("memberEmail", memberEmail)
                .getResultList();

        return findMember.stream().findAny();
    }
}
