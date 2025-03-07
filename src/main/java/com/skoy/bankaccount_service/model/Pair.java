package com.skoy.bankaccount_service.model;

import lombok.Data;

@Data
public class Pair {
    private int id;
    private String name;

    public Pair() {
    }

    public Pair(int id, String name) {
        this.id = id;
        this.name = name;
    }
}
