package basePackage.connect;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@AllArgsConstructor
@Getter
public enum RequestError {
    SOCKET_CONNECTION_ERROR(1),
    FILE_PARSE_ERROR(2);

    private static Map<Integer, RequestError> errorCodeToError = Stream.of(RequestError.values())
            .collect(Collectors.toMap(RequestError::getErrorCode, error -> error));
    private int errorCode;

    public static RequestError fromErrorNum(int errorNum) {
        RequestError requestError = errorCodeToError.get(errorNum);
        if (requestError == null) {
            throw new IllegalArgumentException("Unknown error number " + errorNum);
        }

        return requestError;
    }
}