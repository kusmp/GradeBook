package org.kusmp.api.dao;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.query.Query;

@Entity
public class AutoIncrement {
    @Id
    private ObjectId id;
    private Integer actual;
    private String colectionName;

    public AutoIncrement(){

    }

    public AutoIncrement(Integer actual, String colectionName) {
        this.actual = actual;
        this.colectionName = colectionName;
    }

    public Integer getActual() {
        return actual;
    }

    public String getColectionName() {
        return colectionName;
    }

    public void setColectionName(String colectionName) {
        this.colectionName = colectionName;
    }

}
