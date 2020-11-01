package modern.java;

import lombok.Getter;
import lombok.Setter;

/**
 * lombokでセッターゲッターは自動生成
 */
@Getter
@Setter
public class Person {
    private String id;
    private String name;

    public Person() {};

    public Person(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String toString() {
        return " ID: " +id + " NAME: " + name;
    }
}