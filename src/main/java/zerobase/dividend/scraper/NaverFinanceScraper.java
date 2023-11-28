package zerobase.dividend.scraper;

import zerobase.dividend.model.Company;
import zerobase.dividend.model.ScrapedResult;

public class NaverFinanceScraper implements Scraper{
    //만약 네이버에서 스크랩 할려고 하면 오버라이드된 메소드 만들면됨. 코드 변화 최소화 -> 확장
    @Override
    public Company scrapCompanyByTicker(String ticker) {
        return null;
    }

    @Override
    public ScrapedResult scrap(Company company) {
        return null;
    }
}
