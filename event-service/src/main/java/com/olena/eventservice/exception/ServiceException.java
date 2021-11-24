package com.olena.eventservice.exception;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class ServiceException extends Exception {
    private String errMsg;

    public ServiceException(String msg, Exception e) {
        super(msg, e);
        this.errMsg = msg;
    }
}
