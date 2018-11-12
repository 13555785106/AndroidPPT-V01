package com.telecom.sqlitedemo;

/**
 * Created by xiaojf on 18/2/8.
 */

public class Contact {
    private int id = -1;
    private String name;
    private String phoneNums;

    public Contact(String name, String phoneNums) {
        this.name = name;
        this.phoneNums = phoneNums;
    }

    public Contact(int id, String name, String phoneNums) {
        this(name, phoneNums);
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNums() {
        return phoneNums;
    }

    public void setPhoneNums(String phoneNums) {
        this.phoneNums = phoneNums;
    }

    @Override
    public String toString() {
        return "Contact{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", phoneNums='" + phoneNums + '\'' +
                '}';
    }
}
