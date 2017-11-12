package com.sbsromero.section_04_realm.models;

import com.sbsromero.section_04_realm.app.MyApplication;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * Created by USERPC on 11/11/2017.
 */

public class Note extends RealmObject {

    @PrimaryKey
    private int id;
    @Required
    private String description;
    @Required
    private Date createAt;

    public Note(String description){
        this.id = MyApplication.NoteId.incrementAndGet();
        this.description = description;
        this.createAt = new Date();
    }

    public Note() {
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreateAt() {
        return createAt;
    }
}
