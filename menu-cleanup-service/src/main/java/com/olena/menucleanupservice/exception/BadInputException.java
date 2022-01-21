package com.olena.menucleanupservice.exception;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class BadInputException extends Exception {
    private String errMsg;

    public BadInputException(String msg, Exception e) {
        super(msg, e);
        this.errMsg = msg;
    }
}
