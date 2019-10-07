package basePackage.connect;

import basePackage.objectModel.Human;
import java.io.Serializable;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Wither;

@Data
@Wither
@AllArgsConstructor
@NoArgsConstructor
public class Request implements Serializable {
    private String signature;
    private List<Human> data;
}
