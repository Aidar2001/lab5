package basePackage.connect;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Wither;

@Data @AllArgsConstructor
@NoArgsConstructor @Wither
public class CollectionInfo {
  private String collectionName;
  private String collectionType;
  private Date initializationDate;
  private Integer collectionSize;
}
