package com.idocnet.realmdbdemo.model;

public class SaveRespone {
    public boolean isSuccess;
    public String message;

    public SaveRespone(){}

    public SaveRespone(boolean isSuccess, String message){
        this.isSuccess = isSuccess;
        this.message = message;
    }
}
