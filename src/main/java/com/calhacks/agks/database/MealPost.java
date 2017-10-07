package com.calhacks.agks.database;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.sql.Date;

@XmlRootElement
public class MealPost {
    @XmlElement(name = "name")
    private String name;
    @XmlElement(name = "NBDno")
    private String NBDno;
    //@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @XmlElement(name = "date")
    private String date;

    public MealPost() {}

    public MealPost(String name, String NBDno, String date) {
        this.name = name;
        this.NBDno = NBDno;
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNBDno() {
        return NBDno;
    }

    public void setNBDno(String NBDno) {
        this.NBDno = NBDno;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
