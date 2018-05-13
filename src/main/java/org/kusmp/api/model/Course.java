package org.kusmp.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.bson.types.ObjectId;
import org.glassfish.jersey.linking.InjectLink;
import org.glassfish.jersey.linking.InjectLinks;
import org.kusmp.api.ObjectIdJaxbAdapter;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

import javax.ws.rs.core.Link;
import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Entity
@XmlRootElement(name = "course")
public class Course {

    private long id;
    private static long increment =1;
    private String name;
    private String lecturer;
   // private static final AtomicLong counter = new AtomicLong(100);
    @XmlTransient
    @Id
    @XmlJavaTypeAdapter(ObjectIdJaxbAdapter.class)
    ObjectId ID;

    static final AtomicLong count = new AtomicLong(0);

    @InjectLinks({
            @InjectLink(value = "/courses/{id}", rel = "self"),
            @InjectLink(value = "/courses", rel = "parent")
    })
    @XmlElement(name = "link")
    @XmlElementWrapper(name = "links")
    @JsonProperty("links")
    @XmlJavaTypeAdapter(Link.JaxbAdapter.class)
    private List<Link> links;

    public Course() {
    }

    public Course(String name, String lecturer) {
        id = count.incrementAndGet();
        this.name = name;
        this.lecturer = lecturer;
    }

    public Course(long id, String name, String lecturer) {
        this.id = count.incrementAndGet();
        this.name = name;
        this.lecturer = lecturer;
    }


    @XmlElement
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    @XmlElement
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlElement
    public String getLecturer() {
        return lecturer;
    }

    public void setLecturer(String lecturer) {
        this.lecturer = lecturer;
    }

    @XmlTransient
    public ObjectId getID2() {
        return ID;
    }
    public void setID2(ObjectId ID) {
        this.ID = ID;
    }
}

