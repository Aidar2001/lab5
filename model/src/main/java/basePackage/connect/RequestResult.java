package basePackage.connect;

import basePackage.objectModel.Human;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Wither;

@Data
@Wither
@AllArgsConstructor
@NoArgsConstructor
public class RequestResult<T> {
    private static ObjectMapper mapper = new ObjectMapper();

    private Boolean success;

    private T result;

    private RequestError error;
    private Exception exception;

    @JsonIgnore
    public Human getHumanResult() {
        return mapper.convertValue(result, Human.class);
    }

    @JsonIgnore
    public List<Human> getHumansListResult() {
        return mapper.convertValue(result, new TypeReference<List<Human>>(){});
    }

    @JsonIgnore
    public CollectionInfo getCollectionInfoResult() {
        return mapper.convertValue(result, CollectionInfo.class);
    }

    public static <T> RequestResult<T> createSuccessResultWith(T result) {
        return new RequestResult<T>()
                .withSuccess(true)
                .withResult(result);
    }

    public static <T> RequestResult<T> createSuccessResult() {
        return new RequestResult<T>()
                .withSuccess(true)
                .withResult(null);
    }

    public static <T> RequestResult<T> createFailResult(Exception ex, RequestError error) {
        return new RequestResult<T>()
                .withSuccess(false)
                .withException(ex)
                .withError(error);
    }

    public static <T> RequestResult<T> createFailResult(Exception ex) {
        return new RequestResult<T>()
                .withSuccess(false)
                .withException(ex)
                .withResult(null);
    }

    public static <T> RequestResult<T> createFailResult(RequestError errorCode) {
        return new RequestResult<T>()
                .withError(errorCode)
                .withSuccess(false);
    }
}