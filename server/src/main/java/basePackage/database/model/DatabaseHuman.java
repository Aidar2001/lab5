package basePackage.database.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Wither;

import java.util.List;

@Data
@Wither
@AllArgsConstructor
@NoArgsConstructor
public class DatabaseHuman {
    private int id;
    private String name;
    private List<String> actionNames;
    private List<String> professions;
    private String locationName;
    private String creationTime;
}
