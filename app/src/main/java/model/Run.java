package model;

import java.io.Serializable;
import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Run extends RealmObject implements Serializable  {


    Float length;
    Date date;
    Integer duration;
    @PrimaryKey
    Integer hash;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    String id;

    public Run() {
        length = -1f;
        date = null;
        duration = -1;
        hash = this.hashCode();

    }

    public void setHash(Integer hash) {
        this.hash = hash;
    }

    public Float getLength() {
        return length;
    }

    public void setLength(float length) {
        this.length = length;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public Integer getHash() {
        return hash;
    }

    public Run(float length, Date date, int duration) {
        this.length = length;
        this.date = date;
        this.duration = duration;
        this.hash = this.hashCode();
    }
}
