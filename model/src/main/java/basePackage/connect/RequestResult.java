package basePackage.connect;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Wither;

@Data
@Wither
@AllArgsConstructor
@NoArgsConstructor
public class RequestResult<T> {
    private boolean success;
    private T result;
    private RequestError error;
    private Exception exception;

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

    public static <T> RequestResult<T> createFailResult(Exception ex, int errorCode) {
        return new RequestResult<T>()
                .withSuccess(false)
                .withException(ex)
                .withError(RequestError.fromErrorNum(errorCode));
    }

    public static <T> RequestResult<T> createFailResult(Exception ex) {
        return new RequestResult<T>()
                .withSuccess(false)
                .withResult(null);
    }

    public static <T> RequestResult<T> createFailResult(int errorCode) {
        return new RequestResult<T>()
                .withError(RequestError.fromErrorNum(errorCode))
                .withSuccess(false);
    }
}