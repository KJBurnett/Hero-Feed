package com.burntech.kyler.comicrss;

/**
 * Created by Kyler J. Burnett on 4/10/2015.
 */
public class Publisher {

    private String id;
    private String name;

    public Publisher(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Publisher{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
