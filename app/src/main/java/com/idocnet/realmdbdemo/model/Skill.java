package com.idocnet.realmdbdemo.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class Skill extends RealmObject {
    public static final String PROPERTY_SKILL = "skill";

    @PrimaryKey
    @Required
    public String skill;
}
