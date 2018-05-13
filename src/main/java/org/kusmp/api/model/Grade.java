package org.kusmp.api.model;


import com.fasterxml.jackson.annotation.JsonProperty;
import org.glassfish.jersey.linking.InjectLink;
import org.glassfish.jersey.linking.InjectLinks;
import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Reference;

import javax.ws.rs.core.Link;
import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

@Embedded
@XmlRootElement(name = "grade")
public class Grade {

   // private UUID uuid = UUID.randomUUID();
    private long id;
    private Date date;
    private float value;
   // @Reference
    private Course course;
    static final AtomicLong count = new AtomicLong(0);

//    @InjectLinks({
//            @InjectLink(value = "students/{index}/grades", rel = "parent"),
//            @InjectLink(value = "students/{index}/grades/{id}", rel = "self")
//    })
//    @XmlElement(name = "link")
//    @XmlElementWrapper(name = "links")
//    @JsonProperty("links")
//    @XmlJavaTypeAdapter(Link.JaxbAdapter.class)
//    private List<Link> links;
//    private long studentOwnerIndex;

    public Grade() {
    }

    public Grade(Date date, float value, Course course) {
        this.date = date;
        this.value = value;
        this.course = course;
        this.id = count.incrementAndGet();
    }

    @XmlElement
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = count.incrementAndGet();
    }

    public void rewriteId(long id){
        this.id = id;
    }

    @XmlElement
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @XmlElement
    public float getValue() {
        return value;
    }

    public void setValue(float value) {

        if((value%0.5f == 0 && value>=3.0f && value<=5) || value == 2.0f) {
            this.value = value;
        }
        else this.value = 2.0f;
    }

    @XmlElement
    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }
}
