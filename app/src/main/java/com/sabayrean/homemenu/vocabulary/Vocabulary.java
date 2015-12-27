package com.sabayrean.homemenu.vocabulary;

/**
 * Created by LAYLeangsros on 05/06/2015.
 */
public class Vocabulary {
    public int id;
    public String kh, en, fr, voice;

    public Vocabulary() {
    }

    public Vocabulary(int id, String voice, String kh, String fr, String en) {
        this.id = id;
        this.voice = voice;
        this.kh = kh;
        this.fr = fr;
        this.en = en;

    }

    public void setId(int id) {
        this.id = id;
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

    public void setVoice(String voice) {
        this.voice = voice;
    }
}