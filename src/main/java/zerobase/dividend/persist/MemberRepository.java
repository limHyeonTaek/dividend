package zerobase.dividend.persist;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import zerobase.dividend.persist.entity.MemberEntity;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<MemberEntity, Long> {

    //id를 기준으로 회원 정보 찾기
    Optional<MemberEntity> findByUsername(String username);

    //회원가입시 이미 존재하는 ID 인지 여부 확인.
    boolean existsByUsername(String username);

}
