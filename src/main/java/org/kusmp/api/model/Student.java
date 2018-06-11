package org.kusmp.api.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.bson.types.ObjectId;
import org.glassfish.jersey.linking.InjectLink;
import org.glassfish.jersey.linking.InjectLinks;
import org.kusmp.api.ObjectIdJaxbAdapter;
import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Indexed;

import javax.ws.rs.core.Link;
import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Entity
@XmlRootElement(name = "student")
public class Student {

    private static AtomicLong counter = new AtomicLong(100);
    @Indexed(name = "index", unique = true)
    private long index;
    private static long IDtemp =1;
    private String name;
    private String surname;
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd", timezone="CET")
    private Date birthday;
    @Embedded
    private List<Grade> grades;
    @XmlTransient
    @Id
    @XmlJavaTypeAdapter(ObjectIdJaxbAdapter.class)
    ObjectId id;

   static final AtomicLong count = new AtomicLong(100);

    @InjectLinks({
            @InjectLink(value = "/students/{index}", rel = "self"),
            @InjectLink(value = "/students", rel = "parent"),
            @InjectLink(value = "/students/{index}/grades", rel = "grades")
    })

@XmlElement(name = "link")
@XmlElementWrapper(name = "links")
@JsonProperty("links")
@XmlJavaTypeAdapter(Link.JaxbAdapter.class)
    private List<Link> links;

    public Student() {
    }

    public Student(String name, String surname, Date birthday, List<Grade> grades, long index) {
        index = IDtemp++;
        this.name = name;
        this.surname = surname;
        this.birthday = birthday;
        this.grades = grades;
        this.index = count.getAndIncrement();
    }


    public Student(String name, String surname, Date birthday, List<Grade> grades) {
       // index = IDtemp++;
        this.name = name;
        this.surname = surname;
        this.birthday = birthday;
        this.grades = grades;
        this.index = count.getAndIncrement();
    }

    @XmlAttribute
    public long getIndex() {
        return index;
    }

//    public void setIndex(long index) {
//        this.index = index;
//    }

    @XmlElement
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlElement
    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    @XmlElement
    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    @XmlElement
    public List<Grade> getGrades() {
        return grades;
    }

    public void setGrades(List<Grade> grades) {
        this.grades = grades;
    }

    public void setIndex(long index) {
        this.index = index;
    }

    @XmlTransient
    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public static AtomicLong getCounter() {
        return counter;
    }

    public static void setCounter(AtomicLong counter) {
        Student.counter = counter;
    }

    public static long getIDtemp() {
        return IDtemp;
    }

    public static void setIDtemp(long IDtemp) {
        Student.IDtemp = IDtemp;
    }

    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }
}

