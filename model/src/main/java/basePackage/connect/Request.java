package basePackage.connect;

import basePackage.objectModel.Human;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Wither;

import java.io.Serializable;
import java.util.List;

@Data
@Wither
@AllArgsConstructor
@NoArgsConstructor
public class Request implements Serializable {
    private String signature;
    private List<Human> data;
}
