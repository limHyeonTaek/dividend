package zerobase.dividend.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import zerobase.dividend.exception.impl.NoCompanyException;
import zerobase.dividend.model.Company;
import zerobase.dividend.model.Dividend;
import zerobase.dividend.model.ScrapedResult;
import zerobase.dividend.model.constants.CacheKey;
import zerobase.dividend.persist.CompanyRepository;
import zerobase.dividend.persist.DividendRepository;
import zerobase.dividend.persist.entity.CompanyEntity;
import zerobase.dividend.persist.entity.DividendEntity;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class FinanceService {

    private final CompanyRepository companyRepository;
    private final DividendRepository dividendRepository;

    //특정 회사 해당하는 정보와 배당금 조회
    @Cacheable(key = "#companyName", value = CacheKey.KEY_FINANCE)
    public ScrapedResult getDividendByCompanyName(String companyName) {
        log.info("search company -> " + companyName);
        //1. 회사명을 기준으로 회사 정보를 조회
        CompanyEntity company = companyRepository.findByName(companyName)
                                                    .orElseThrow(NoCompanyException::new);
        //2. 조회된 회사 ID로 배당금 정보 조회
        List<DividendEntity> dividendEntities =
                dividendRepository.findAllByCompanyId(company.getId());

        //3. 결과 조합 후 반환.
        List<Dividend> dividends = dividendEntities.stream()
                                            .map(e -> new Dividend(e.getDate(), e.getDividend()))
                                            .collect(Collectors.toList());

        //ScrapedResult에 Entity정보 X -> 매핑 작업
        return new ScrapedResult(new Company(company.getTicker(), company.getName()),
                                    dividends);
    }
}
