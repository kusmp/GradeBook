package org.kusmp.api.model;

import org.kusmp.api.MorphiaHandler;
import org.mongodb.morphia.Datastore;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class mockModel {

    public static final List<Student> students = new ArrayList<>();
    public static final List<Course> courses = new ArrayList<>();

    public static Calendar toCalendar(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal;
    }

    static {
        Datastore ds = MorphiaHandler.getInstance().getDatastore();
        List<Grade> grades = new ArrayList<>();
        Grade tempGrade = new Grade();

        courses.add(new Course( "Matematyka", "Jan Kowalski"));
        courses.add((new Course("Fizyka", "Jan Nowak")));
        courses.add(new Course( "Chemia", "Piotr Kwiatkowski"));


        /////////// student 1
        tempGrade.setId(10);
        tempGrade.setValue(4);
        tempGrade.setDate(new Date("2018/4/9"));
        tempGrade.setCourse(courses.get(0));
        grades.add(tempGrade);
        tempGrade = new Grade();
        //
        tempGrade.setId(14);
        tempGrade.setValue(3);
        tempGrade.setDate(new Date("2018/4/7"));
        tempGrade.setCourse(courses.get(1));
        grades.add(tempGrade);
        tempGrade = new Grade();
        //
        tempGrade.setId(12);
        tempGrade.setValue(5);
        tempGrade.setDate(new Date("2018/4/8"));
        tempGrade.setCourse(courses.get(2));
        grades.add(tempGrade);
        tempGrade = new Grade();
        //
        Student student = new Student( "Patryk", "Kuśmierkiewicz", new Date("1995/2/15"), grades);
        students.add(student);
        grades =  new ArrayList<>();
        /////////// student 2
        tempGrade.setId(1);
        tempGrade.setValue(4.5f);
        tempGrade.setDate(new Date("2018/4/9"));
        tempGrade.setCourse(courses.get(0));
        grades.add(tempGrade);
        tempGrade = new Grade();
        //
        tempGrade.setId(8);
        tempGrade.setValue(3.5f);
        tempGrade.setDate(new Date("2018/4/7"));
        tempGrade.setCourse(courses.get(1));
        grades.add(tempGrade);
        tempGrade = new Grade();
        //
        tempGrade.setId(12);
        tempGrade.setValue(5);
        tempGrade.setDate(new Date("2018/4/8"));
        tempGrade.setCourse(courses.get(2));
        grades.add(tempGrade);
        tempGrade = new Grade();
        //
        student = new Student("Piotr", "Wróbel", new Date("1995/8/16"), grades);
        students.add(student);
        grades =  new ArrayList<>();
        /////////// student 3
        tempGrade.setId(6);
        tempGrade.setValue(3.5f);
        tempGrade.setDate(new Date("2018/4/9"));
        tempGrade.setCourse(courses.get(0));
        grades.add(tempGrade);
        tempGrade = new Grade();
        //
        tempGrade.setId(80);
        tempGrade.setValue(2.0f);
        tempGrade.setDate(new Date("2018/4/7"));
        tempGrade.setCourse(courses.get(1));
        grades.add(tempGrade);
        tempGrade = new Grade();
        //
        tempGrade.setId(13);
        tempGrade.setValue(5);
        tempGrade.setDate(new Date("2018/4/8"));
        tempGrade.setCourse(courses.get(2));
        grades.add(tempGrade);
        tempGrade = new Grade();
        //
        student = new Student("Maciej", "Wiśniewski", new Date("1994/9/18"), grades);
        students.add(student);
        grades =  new ArrayList<>();
        ds.save(courses);
        ds.save(students);


        //ds.save(grades);
       // ds.save(courses);
    }

    public mockModel() {
    }

    public static List<Student> getStudentsList() {
        return students;
    }

    public static List<Course> getCourses() {
        return courses;
    }
}
