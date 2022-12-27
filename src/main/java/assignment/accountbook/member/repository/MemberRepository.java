package assignment.accountbook.member.repository;

import assignment.accountbook.member.entity.Member;

import java.util.Optional;

public interface MemberRepository {

    public Member save(Member member);

    public Optional<Member> findByEmail(String memberEmail);

}
