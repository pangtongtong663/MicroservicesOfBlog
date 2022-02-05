package com.pt.accountservice.exception;

import java.util.ArrayList;

public class DuplicateInfoException extends RuntimeException{
    private ArrayList<String> duplicateInfos = new ArrayList<>();

    public DuplicateInfoException() {
        super();
    }

    public void addDuplicateInfoField(String info) {
        this.duplicateInfos.add(info);
    }

    public ArrayList<String> getDuplicateInfos() {
        return this.duplicateInfos;
    }
}
