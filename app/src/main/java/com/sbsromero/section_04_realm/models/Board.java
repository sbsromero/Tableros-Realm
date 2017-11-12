package com.sbsromero.section_04_realm.models;

import com.sbsromero.section_04_realm.app.MyApplication;

import java.util.Date;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * Created by USERPC on 11/11/2017.
 */

public class Board extends RealmObject {

    @PrimaryKey
    private int id;
    @Required
    private String title;
    @Required
    private Date createAt;

    private RealmList<Note> notes;

    public Board(String title) {
        this.id = MyApplication.BoardId.incrementAndGet();
        this.title = title;
        this.createAt = new Date();
        this.notes = new RealmList<Note>();
    }

    public Board() {
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public RealmList<Note> getNotes() {
        return notes;
    }
}
