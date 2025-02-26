package com.skoy.bootcamp_microservices.model;

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
