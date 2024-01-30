package rest.api.aop;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import rest.api.exceptions.ExceptionResponse;
import rest.api.exceptions.ProjectException;

@ControllerAdvice
public class ExceptionsHandler {


    @ExceptionHandler
    public ResponseEntity<ExceptionResponse> globalExceptionHandler(Exception exception) {
        ExceptionResponse exceptionResponse = new ExceptionResponse();
        exceptionResponse.setInfo(exception.getMessage());

        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ExceptionResponse> projectExceptionHandler(ProjectException projectException) {
        ExceptionResponse exceptionResponse = new ExceptionResponse();
        exceptionResponse.setInfo(projectException.getMessage());

        return new ResponseEntity<>(exceptionResponse, HttpStatus.NOT_FOUND);
    }
}
