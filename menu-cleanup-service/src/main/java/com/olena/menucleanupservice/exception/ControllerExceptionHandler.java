package com.olena.menucleanupservice.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ServerWebInputException;

@ControllerAdvice
@Slf4j
public class ControllerExceptionHandler {

    @ExceptionHandler(value = ServerWebInputException.class)
    public ResponseEntity<Object> exception(ServerWebInputException e) {
        String msg = "";
        if (e.getRootCause() != null)
            msg = e.getRootCause().getMessage();
        if (msg == null || msg.isEmpty())
            msg = e.getMessage();
        return new ResponseEntity<>("Bad input(s) - " + msg, e.getStatus());
    }

    @ExceptionHandler(value = NumberFormatException.class)
    public ResponseEntity<Object> exception(NumberFormatException exception) {
        return new ResponseEntity<>("Bad input(s).", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = IllegalArgumentException.class)
    public ResponseEntity<Object> exception(IllegalArgumentException e) {
        return new ResponseEntity<>("Bad input(s) - " + e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = BadInputException.class)
    public ResponseEntity<Object> exception(BadInputException e) {
        return new ResponseEntity<>(e.getErrMsg(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = ServiceException.class)
    public ResponseEntity<Object> exception(ServiceException e) {
        return new ResponseEntity<>(e.getErrMsg(), HttpStatus.EXPECTATION_FAILED);
    }


    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<Object> exception(Exception e) {
        String msg = "Unexpected exception occured in the service.";
        log.error(msg, e);
        return new ResponseEntity<>(msg, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
