package com.calhacks.agks.authentication;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by Sabrina on 10/7/2017.
 */
@XmlRootElement
public class RegistrationData {
    private String username;
    private String password;
    private int age;
    private String sex;

    public RegistrationData(String username, String password, int age, String sex) {
        this.username = username;
        this.password = password;
        this.age = age;
        this.sex = sex;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }
}
