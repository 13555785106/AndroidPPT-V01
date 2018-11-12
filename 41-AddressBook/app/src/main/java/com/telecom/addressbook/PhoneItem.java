package com.telecom.addressbook;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiaojf on 18/1/27.
 */

public class PhoneItem {
    long id;
    Bitmap thumb;
    String name;
    List<String> phoneNums = new ArrayList<>();

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Bitmap getThumb() {
        return thumb;
    }

    public void setThumb(Bitmap thumb) {
        this.thumb = thumb;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getPhoneNums() {
        return phoneNums;
    }

    public void setPhoneNums(List<String> phoneNums) {
        this.phoneNums = phoneNums;
    }
}
