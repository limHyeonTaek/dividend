package zerobase.dividend.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor //Data에 포함 X
public class ScrapedResult {

    //스크래핑 정보 클래스.
    private Company company;            //스크래핑한 회사 정보
    private List<Dividend> dividends;   //회사의 배당금 리스트(한 회사는 여러개의 배당금 정보.)

    public ScrapedResult() {
        this.dividends = new ArrayList<>();
    }
}
