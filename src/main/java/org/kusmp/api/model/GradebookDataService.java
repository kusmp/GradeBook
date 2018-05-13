package org.kusmp.api.model;

import org.kusmp.api.MorphiaHandler;
import org.mongodb.morphia.Datastore;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class GradebookDataService {

    private static final AtomicLong gradeIdCounter = new AtomicLong();
    private static final GradebookDataService ourInstance = new GradebookDataService();
    private static final AtomicLong studentIdCounter = new AtomicLong();
    private static final AtomicLong courseIdCounter = new AtomicLong();
//    private final List<Student> studentsList = new ArrayList<>();
    private final List<Student> studentsList = mockModel.getStudentsList();
//    private final List<Course> coursesList = new ArrayList<>();
    private final List<Course> coursesList = mockModel.getCourses();
    public static GradebookDataService getInstance() {
        return ourInstance;
    }

    private Datastore datastore = MorphiaHandler.getDatastore();

    public GradebookDataService(Datastore datastore) {
        this.datastore = datastore;
    }

    public GradebookDataService() {
    }

    public Student addStudent(Student student){
        long index = studentIdCounter.getAndIncrement();
        for(Student existingStudent : studentsList){
            if(existingStudent.getIndex() == index){
                return addStudent(student);
            }
        }
        student.setIndex(index);
        for(Grade grade : student.getGrades()){
            long id = gradeIdCounter.getAndIncrement();
            grade.setId(id);
            boolean isExisting = false;
            for(Course existingCourse : coursesList){
                if(existingCourse.getName().equals(grade.getCourse().getName())){
                    grade.getCourse().setId(existingCourse.getId());
                    isExisting = true;
                }
            }
            if(!isExisting){
                grade.getCourse().setId(courseIdCounter.getAndIncrement());
            }

        }
        studentsList.add(student);
        datastore.save(student);
        return student;
    }

    public Long addGrade(long index, Grade grade){
        long id = gradeIdCounter.getAndIncrement();
        for(Student student : studentsList){
            if(student.getIndex() == index){
                grade.setId(id);
                student.getGrades().add(grade);
                return id;
            }
        }
        return null;
    }

    public long addCourse(Course course){
        for(Course existingCourse : coursesList){
            if(existingCourse.getName().equals(course.getName())){
                return existingCourse.getId();
            }
        }
        long id = courseIdCounter.getAndIncrement();
        course.setId(id);
        coursesList.add(course);
        return id;
    }

}
