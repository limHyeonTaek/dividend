package zerobase.dividend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor //역직렬화가 필요한 객체
public class Company {

    private String ticker;
    private String name;

}
