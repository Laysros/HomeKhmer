package com.sabayrean.homemenu.vocabulary;

/**
 * Created by LAYLeangsros on 05/06/2015.
 */
public class Category {
    public int id;
    public String icon, kh,en, fr;

    public Category() {
    }

    public Category(int id, String kh, String fr, String en, String icon) {
        this.id = id;
        this.kh = kh;
        this.en = en;
        this.fr = fr;
        this.icon = icon;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public void setKh(String kh) {
        this.kh = kh;
    }

    public void setEn(String en) {
        this.en = en;
    }

    public void setFr(String fr) {
        this.fr = fr;
    }
}