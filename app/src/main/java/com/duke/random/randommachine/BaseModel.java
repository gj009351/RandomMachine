package com.duke.random.randommachine;

import java.io.Serializable;

public class BaseModel implements Serializable {
    public String text;
    public int code;//1:checked

    public BaseModel() {
    }

    public BaseModel(String text, int code) {
        this.text = text;
        this.code = code;
    }
}
