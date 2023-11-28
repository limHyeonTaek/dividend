package zerobase.dividend.web;

import lombok.AllArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import zerobase.dividend.model.Company;
import zerobase.dividend.model.constants.CacheKey;
import zerobase.dividend.persist.entity.CompanyEntity;
import zerobase.dividend.service.CompanyService;

import java.util.List;

@RestController
@RequestMapping("/company")
@AllArgsConstructor
public class CompanyController {

    private final CompanyService companyService;

    private final CacheManager redisCacheManager;

    //배당금 검색 : 자동완성
    @GetMapping("/autocomplete")
    public ResponseEntity<?> autocomplete(@RequestParam String keyword) {
        List<String> result = companyService.autoComplete(keyword);
        //아파치 trie 라이브러리 자동완성 사용 : 서버 메모리에 추가 저장
//        List<String> result = companyService.getCompanyNamesByKeyword(keyword);
// like 연산자 자동완성 사용 : DB에서 데이터 연산 - 데이터 양과 연산이 많으면 부하가니까 사용 X
        return ResponseEntity.ok(result);
    }

    //회사 리스트 조회
    @GetMapping                             //final : page 값 변하는 것 방지.
    @PreAuthorize("hasRole('READ')")
    public ResponseEntity<?> searchCompany(final Pageable pageable) {
        Page<CompanyEntity> companies = companyService.getAllCompany(pageable);
        return ResponseEntity.ok(companies);
    }

    //회사 데이터 저장
    @PostMapping
    @PreAuthorize("hasRole('WRITE')")
    public ResponseEntity<?> addCompany(@RequestBody Company request) {
        String ticker = request.getTicker().trim();
        if (ObjectUtils.isEmpty(ticker)) {
            throw new RuntimeException("ticker is empty");
        }

        Company company = companyService.save(ticker);
        companyService.addAutocompleteKeyword(company.getName());

        return ResponseEntity.ok(company);
    }

    //회사 삭제
    @DeleteMapping("/{ticker}")
    @PreAuthorize("hasRole('WRITE')") //관리권한을 가진 계정
    public ResponseEntity<?> deleteCompany(@PathVariable String ticker) {
        String companyName = companyService.deleteCompany(ticker);
        clearFinanceCache(companyName);
        return ResponseEntity.ok(companyName);
    }

    //저장된 캐시 정보 삭제.
    public void clearFinanceCache(String companyName) {
        //캐시 키, 이름 가져옴                           //evict : 특정 키와 연관된 값을 제거하는 함수
        redisCacheManager.getCache(CacheKey.KEY_FINANCE ).evict(companyName);
    }

}
