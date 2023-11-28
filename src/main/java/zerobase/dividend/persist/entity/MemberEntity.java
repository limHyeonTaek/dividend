package zerobase.dividend.persist.entity;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Builder
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "MEMBER")
public class MemberEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String password;

    //복수형태의 List 사용 이유 : 사용자가 READ, WRITE 두가지 권한을 다 가질 수 있기 때문.
    //@ElementCollection은 관계형 데이터베이스 테이블이 아닌 값의 컬렉션을 매핑하기 위해 사용.
    @ElementCollection(fetch = FetchType.EAGER) // fetch 연관된 엔티티나 컬렉션을 즉시 로드
    private List<String> roles;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                //스프링 시큐리티에서 지원하고 있는 Role관련 기능을 사용하기 위해 SimpleGrantedAuthority로 매핑
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

}


