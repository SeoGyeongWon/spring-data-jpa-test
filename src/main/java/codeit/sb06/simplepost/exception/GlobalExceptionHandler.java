package codeit.sb06.simplepost.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(PostNotFoundException.class)
    protected ResponseEntity<ErrorResponse> handlePostNotFoundException(PostNotFoundException e) {
        log.error("handlePostNotFoundException", e);
        final ErrorCode errorCode = ErrorCode.POST_NOT_FOUND;
        final ErrorResponse response = ErrorResponse.of(errorCode);
        return new ResponseEntity<>(response, errorCode.getStatus());
    }

    @ExceptionHandler(InvalidPasswordException.class)
    protected ResponseEntity<ErrorResponse> handleInvalidPasswordException(InvalidPasswordException e) {
        log.error("handleInvalidPasswordException", e);
        final ErrorCode errorCode = ErrorCode.INVALID_PASSWORD;
        final ErrorResponse response = ErrorResponse.of(errorCode);
        return new ResponseEntity<>(response, errorCode.getStatus());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error("handleMethodArgumentNotValidException", e);
        final ErrorCode errorCode = ErrorCode.INVALID_INPUT_VALUE;
        final ErrorResponse response = ErrorResponse.of(errorCode);
        // NOTE: e.getBindingResult()를 통해 어떤 필드에서 어떤 에러가 발생했는지 상세 정보 로깅 및 확인 가능
        return new ResponseEntity<>(response, errorCode.getStatus());
    }
}