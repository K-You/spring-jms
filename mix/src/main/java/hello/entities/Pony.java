package hello.entities;

import java.io.Serializable;

public class Pony implements Serializable {

    private String name;

    public Pony(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Pony{" +
                "name='" + name + '\'' +
                '}';
    }
}
