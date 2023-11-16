package com.openclassrooms.api.exception;

import com.openclassrooms.api.model.response.EmptyResponse;
import com.openclassrooms.api.model.response.MessageResponse;
import com.openclassrooms.api.model.response.Response;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

/**
 * Global Controller for Exception handling
 */
@Slf4j
@RestControllerAdvice
public class GlobalControllerExceptionHandler {

    /**
     * handler for error 401
     *
     * @param ex InvalidCredentialsException
     * @return Response
     */
    @ExceptionHandler(InvalidCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @Hidden
    Response handleResourceNotFound(InvalidCredentialsException ex) {
        log.error("Error 401 - Unauthorized");
        return ex.getMessage() == null ? new EmptyResponse() : new MessageResponse(ex.getMessage());
    }

    /**
     * handler for error 400
     *
     * @param ex BadRequestException
     * @return Response
     */
    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @Hidden
    Response handleResourceNotFound(BadRequestException ex) {
        log.error("Error 400 - Bad Request");
        return ex.getMessage() == null ? new EmptyResponse() : new MessageResponse(ex.getMessage());
    }

    /**
     * handler for MaxUploadSizeExceededException
     *
     * @param ex MaxUploadSizeExceededException
     * @return EmptyResponse
     */
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @Hidden
    EmptyResponse handleMaxSizeException(MaxUploadSizeExceededException ex) {
        log.error("File too large - " + ex.getMessage());
        return new EmptyResponse();
    }
}
