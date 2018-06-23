package org.kusmp.api.dao;

import org.kusmp.api.MorphiaHandler;
import org.kusmp.api.model.Course;
import org.kusmp.api.model.Student;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.aggregation.Accumulator;
import org.mongodb.morphia.aggregation.Sort;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;

import java.util.Iterator;

public class StudentDAO {
    final static Datastore datastore = MorphiaHandler.getInstance().getDatastore();

    public static long generateStudentIndex() {

        Query<AutoIncrement> query = datastore.createQuery(AutoIncrement.class).filter("colectionName", "Student");
        UpdateOperations<AutoIncrement> updateOperations = datastore.createUpdateOperations(AutoIncrement.class).inc("actual");
        AutoIncrement autoIncrement = datastore.findAndModify(query, updateOperations);
        if (autoIncrement == null) {
            System.out.println("Wszedlem do ifa");
            autoIncrement = new AutoIncrement(1, "Student");
            datastore.save(autoIncrement);

        }
        System.out.println("FUNCT Przyznany indeks studenta: " + autoIncrement.getActual());
        return autoIncrement.getActual();


//        final Query<Student> studQuery = datastore.createQuery(Student.class);
//        long index = studQuery.asList().size();
//        index++;
//        System.out.println("Przyznany indeks studenta: " + index);
//        return index;
    }

    public static long generateCourseId(){
        final Query<Course> courseQuery = datastore.createQuery(Course.class);
        long index = courseQuery.asList().size();
        index++;
        System.out.println("Przyznany indeks kursu: " + index);
        return index;
    }

    public static int generateCourseIndex(){
        try {
            Query<AutoIncrement> query = datastore.createQuery(AutoIncrement.class).filter("colectionName","Course");
            UpdateOperations<AutoIncrement> updateOperations = datastore.createUpdateOperations(AutoIncrement.class).inc("actual");
            AutoIncrement autoIncrement = datastore.findAndModify(query, updateOperations);
            if (autoIncrement == null) {
                autoIncrement = new AutoIncrement(1, "Course");
                datastore.save(autoIncrement);
            }
            System.out.println("ActualValue " + autoIncrement.getActual());
            return autoIncrement.getActual();
        }catch (Exception e){
            e.printStackTrace();
            return 0;
        }

    }

}
