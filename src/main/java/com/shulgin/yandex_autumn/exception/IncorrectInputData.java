package com.shulgin.yandex_autumn.exception;

public class IncorrectInputData {
    private int code;
    private String message;

    public IncorrectInputData(String info, int code) {
        this.message = info;
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}