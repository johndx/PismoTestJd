package com.pismo.core.accounts.exceptions;

public class BadRequestAlertException extends Throwable {
    public BadRequestAlertException(String s, String entityName, String idExists) {
        System.out.println("\n A bad URL request was made for Entity :" + entityName + " and Id :" + idExists);
    }
}
