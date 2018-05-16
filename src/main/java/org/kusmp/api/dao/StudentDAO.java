package org.kusmp.api.dao;

import org.kusmp.api.MorphiaHandler;
import org.kusmp.api.model.Course;
import org.kusmp.api.model.Student;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.aggregation.Accumulator;
import org.mongodb.morphia.aggregation.Sort;
import org.mongodb.morphia.query.Query;

import java.util.Iterator;

public class StudentDAO {
    final static Datastore datastore = MorphiaHandler.getInstance().getDatastore();

    public static long generateStudentIndex() {

        final Query<Student> studQuery = datastore.createQuery(Student.class);
        long index = studQuery.asList().size();
        index++;
        System.out.println("Przyznany indeks studenta: " + index);
        return index;
    }

    public static long generateCourseId(){
        final Query<Course> courseQuery = datastore.createQuery(Course.class);
        long index = courseQuery.asList().size();
        index++;
        System.out.println("Przyznany indeks kursu: " + index);
        return index;
    }

}
