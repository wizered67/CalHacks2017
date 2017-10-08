package com.calhacks.agks.database;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class MealMatch {
    private String name;
    private String id;

    public MealMatch() {}

    public MealMatch(String name, String id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
