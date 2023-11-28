package zerobase.dividend.scraper;

import zerobase.dividend.model.Company;
import zerobase.dividend.model.ScrapedResult;

public interface Scraper {
    Company scrapCompanyByTicker(String ticker);
    ScrapedResult scrap(Company company);
}
