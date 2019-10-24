package basePackage.database.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Wither;

@Data @Wither
@AllArgsConstructor @NoArgsConstructor
public class Association {
    private String username;
    private int humanId;
}
