package zerobase.dividend.persist.entity; //DB와 직접적으로 매핑 되기 위한 클래스.

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import zerobase.dividend.model.Company;

import javax.persistence.*;

@Entity(name = "COMPANY")
@Getter
@ToString
@NoArgsConstructor
public class CompanyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //auto_increment.
    private Long id;

    @Column(unique = true)  //중복제거
    private String ticker;

    private String name;    //회사명

    public CompanyEntity(Company company) {
        this.ticker = company.getTicker();
        this.name = company.getName();
    }
}
