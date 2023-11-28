package zerobase.dividend.security;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import zerobase.dividend.service.MemberService;

import java.util.Date;
import java.util.List;

@Component
@RequiredArgsConstructor
public class TokenProvider {

    //토큰 만료 시간
    private static final long TOKEN_EXPIRE_TIME = 1000 * 60 * 60; // 1hour ms, 초, 분 ex)5시간 1000 * 60 * 60 * 5

    //회원가입 권한 역할
    private static final String KEY_ROLES = "roles";

    private final MemberService memberService;

    //토큰값 ubuntu cmd에서 echo 'dayone-spring-boot-dividend-project-tutorial-jwt-secret-key' | base64로 생성
    @Value("${spring.jwt.secret}")
    private String secretKey;

    /**
     * 토큰 생성(발급)
     * @param username
     * @param roles
     * @return
     */
    //토큰값 문자열 : Auth(로그인한 사용자 이름, 역할) 참조
    public String generateToken(String username, List<String> roles) {
        //사용자 권한 정보 저장
        Claims claims = Jwts.claims().setSubject(username);
        //claims데이터는 Key, Value  형태로 저장되어야함.
        claims.put(KEY_ROLES, roles);

        //토큰 생성시간 및 만료 시간 설정
        var now = new Date();
        var expiredDate = new Date(now.getTime() + TOKEN_EXPIRE_TIME);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)   // 토큰 생성 시간
                .setExpiration(expiredDate) //토큰 만료 시간
                .signWith(SignatureAlgorithm.HS512, secretKey) //사용할 암호화 알고리즘, 비밀키
                .compact();
    }

    //토큰 인증 정보 : 사용자의 정보와 권한 정보 포함.
    public Authentication getAuthentication(String jwt) {
        UserDetails userDetails = memberService.loadUserByUsername(getUsername(jwt));
        //스프링에서 지원하는 형태의 토큰으로 변환.
        return new UsernamePasswordAuthenticationToken(
                userDetails, "", userDetails.getAuthorities());
    }


    public String getUsername(String token) {
        //generateToken메소드에서 넣어준 setSubject(username)값 반환
        return parseClaims(token).getSubject();
    }

    public boolean validateToken(String token) {
        if (!StringUtils.hasText(token)) return false;
        var claims = parseClaims(token);
        //토큰의 만료시간이 현재의 시간인지 아닌지로 비교.
        return !claims.getExpiration().before(new Date());
    }

    private Claims parseClaims(String token) {
        try {
            return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }
}
