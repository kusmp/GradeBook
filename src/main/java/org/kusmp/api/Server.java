package org.kusmp.api;

import org.kusmp.api.model.*;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.List;


@Path("/")
public class Server {

    private final List<Student> students = mockModel.getStudentsList();
    private final List<Course> courses = mockModel.getCourses();
    private final GradebookDataService dataService = GradebookDataService.getInstance();
    final Datastore datastore = MorphiaHandler.getInstance().getDatastore();
    final Query<Course> courseQuery = datastore.createQuery(Course.class);
    final Query<Student> studQuery = datastore.createQuery(Student.class);

    // private final List<Grade> grades = mockModel.getGrades();

    ///////////////
    @GET
    @Path("/students")
    @Produces({MediaType.APPLICATION_JSON})
    public List<Student> getStudentsList() {
        List<Student> listOfStudents = studQuery.asList();
        return listOfStudents;
    }

    @POST
    @Path("/students")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response addStudent(Student student) {

        Student a = new Student(student.getName(), student.getSurname(), student.getDayOfBirth(), student.getGrades());
        datastore.save(a);

//        Student createdIndex = dataService.addStudent(student);
//        Student a = new Student(student.getName(), student.getSurname(), student.getDayOfBirth(), student.getGrades(), createdIndex.getIndex());
//        if (students.add(a))
            return Response.created(URI.create("/students/" + student.getIndex())).build();
//        else return Response.status(Response.Status.BAD_REQUEST).build();
    }
    ///////////////

    @GET
    @Path("/students/{index}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Object getStudent(@PathParam("index") long index) {
        final Query<Student> query = datastore.createQuery(Student.class);
        Query<Student> studentQuery = query.field("index").equal(index);
//        for (Student student : students) {
//            if (student.getIndex() == index) {
//                return student;
//            }
//        }
            return studentQuery.asList();
       // return Response.status(Response.Status.NOT_FOUND).build();
    }

    @PUT
    @Path("/students/{index}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response updateStudent(@PathParam("index") long index, Student student) {

        Student a = new Student(student.getName(), student.getSurname(), student.getDayOfBirth(), student.getGrades());
        Student selectedStudent = studQuery.field("index").equal(index).get();
        UpdateOperations ops = datastore
                .createUpdateOperations(Student.class)
                .set("name", a.getName())
                .set("surname", a.getSurname())
                .set("dayOfBirth", a.getDayOfBirth())
                .set("grades", a.getGrades())
                .set("index", index);
        datastore.update(selectedStudent, ops);
        return Response.status(Response.Status.OK).build();


//        Student a = new Student(student.getName(), student.getSurname(), student.getDayOfBirth(), student.getGrades());
//        List<Student> students = studQuery.asList();
//        for(Student stud : students){
//            if(stud.getIndex() == index){
//                a.setIndex(index);
//                int position = students.indexOf(stud);
//                students.set(position, a);
//               datastore.merge(students);
//               return Response.status(Response.Status.OK).build();
//            }
//        }

//        for (Student stud : students) {
//            if (stud.getIndex() == index) {
//                a.setIndex(index);
//                int position;
//                position = students.indexOf(stud);
//                students.set(position, a);
//                return Response.status(Response.Status.OK).build();
//            }
//        }
      //  return Response.status(Response.Status.NOT_FOUND).build();
    }

    @DELETE
    @Path("/students/{index}")
    public Response deleteStudent(@PathParam("index") long index) {
        Query<Student> removeStudent = studQuery.field("index").equal(index);
        datastore.delete(removeStudent);

       // Iterator<Student> iterator = students.iterator();
      //  while (iterator.hasNext()) {
      //      Student next = iterator.next();
       //     if (next.getIndex() == index) {
      //          iterator.remove();
      //          return Response.status(Response.Status.OK).build();
         //   }
      //  }
        return Response.status(Response.Status.OK).build();
    }
    ///////////////

    @GET
    @Path("/students/{index}/grades")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Grade> getGrades(@PathParam("index") long index) {
        Student selectedStudent = studQuery.field("index").equal(index).get();
        List<Grade> grades = selectedStudent.getGrades();
//        for (Student student : students) {
//            if (student.getIndex() == index) {
//                return student.getGrades();
//            }
//        }
        return grades;
    }

    @POST
    @Path("/students/{index}/grades")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response addGradeToStudent(@PathParam("index") long index, Grade grade) {

        Student student = studQuery.field("index").equal(index).get();
        List<Grade> grades = student.getGrades();
        Grade a = new Grade(grade.getDate(), grade.getValue(), grade.getCourse());
        grades.add(a);
        UpdateOperations<Student> updateOps;
        updateOps = datastore.createUpdateOperations(Student.class).set("grades", grades);
        datastore.update(student, updateOps);
        return Response.status(Response.Status.CREATED).build();
//        for (Student student : students) {
//            if (student.getIndex() == index) {
//                List<Grade> grades = student.getGrades();
//                grades.add(grade);
//                return Response.status(Response.Status.CREATED).build();
//            }
//        }
//        return null;
    }

    @GET
    @Path("/students/{index}/grades/{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Grade getGradyById(@PathParam("index") long index, @PathParam("id") int id) {
        Student selectedStudent = studQuery.field("index").equal(index).get();
        List<Grade> grades = selectedStudent.getGrades();
        for(Grade grd : grades){
            if(grd.getId()==id){
                return grd;
            }
        }
//        for (Student student : students) {
//            if (student.getIndex() == index) {
//                for (Grade grade : student.getGrades()) {
//                    if (grade.getId() == id) {
//                        return grade;
//                    }
//                }
//            }
//        }
        return null;
    }

    @DELETE
    @Path("/students/{index}/grades/{id}")
    public Response deleteStudentGrade(@PathParam("index") long index, @PathParam("id") long id) {

        Student student = studQuery.field("index").equal(index).get();
        List<Grade> studGrades = student.getGrades();
        for(Grade grd : studGrades){
            if(grd.getId() == id){
                studGrades.remove(grd);
                UpdateOperations<Student> updateOps;
                updateOps = datastore.createUpdateOperations(Student.class).set("grades", studGrades);
                datastore.update(student, updateOps);
                return Response.status(Response.Status.OK).build();
            }
        }

        return Response.status(Response.Status.NO_CONTENT).build();
    }

    @PUT
    @Path("/students/{index}/grades/{id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response updateStudentGrade(@PathParam("index") long index, @PathParam("id") long id, Grade grade) {
        Grade a = new Grade(grade.getDate(), grade.getValue(), grade.getCourse());
        Student student = studQuery.field("index").equal(index).get();
       List<Grade> grades = student.getGrades();
       for(Grade grd : grades){
           if(grd.getId() == id){
               a.rewriteId(id);
               int position = grades.indexOf(grd);
               grades.set(position, a);
               UpdateOperations<Student> updateOps;
               updateOps = datastore.createUpdateOperations(Student.class).set("grades", grades);
               datastore.update(student, updateOps);
               return Response.status(Response.Status.OK).build();
           }
       }

//        for (Student student : students) {
//            if (student.getIndex() == index) {
//                List<Grade> upgrad = student.getGrades();
//                for (Grade grd : upgrad) {
//                    if (grd.getId() == id) {
//                        a.setId(id);
//                        int position;
//                        position = upgrad.indexOf(grd);
//                        upgrad.set(position, a);
//                        student.setGrades(upgrad);
//                        return Response.status(Response.Status.OK).build();
//                    }
//                }
//            }
//        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }
    ///////////////

    @GET
    @Path("/courses")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Course> returnCourses() {
      //  return courses;
        return courseQuery.asList();
    }

    @POST
    @Path("/courses")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response addCourse(Course course) {

        Course a = new Course(course.getName(), course.getLecturer());
        datastore.save(a);
//        Long createdCourseId = dataService.addCourse(course);
//        course.setId(createdCourseId);
//        datastore.save(course);
       // courses.add(course);
        return Response.status(Response.Status.CREATED).build();
    }

    @GET
    @Path("/courses/{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Course getCourseById(@PathParam("id") long id) {
        Course course = courseQuery.field("id").equal(id).get();
//        for (Course course : courses) {
//            if (course.getId() == id) {
//                return course;
//            }
//        }
        return course;
    }

    @DELETE
    @Path("/courses/{id}")
    public Response deleteCourse(@PathParam("id") long id) {

        Query<Course> deleteCourse = courseQuery.field("id").equal(id);
        datastore.delete(deleteCourse);

//        Iterator<Course> iterator = courses.iterator();
//        while (iterator.hasNext()) {
//            Course next = iterator.next();
//            if (next.getId() == id) {
//                iterator.remove();
//                return Response.status(Response.Status.OK).build();
//            }
//        }
        return Response.status(Response.Status.OK).build();
    }

    @PUT
    @Path("/courses/{id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response updateCourse(@PathParam("id") long id, Course course) {
        Course a = new Course(course.getName(), course.getLecturer());

        Course selectedCourse = courseQuery.field("id").equal(id).get();
        UpdateOperations ops = datastore
                .createUpdateOperations(Course.class)
                .set("name", course.getName())
                .set("lecturer", course.getLecturer())
                .set("id", id);
        datastore.update(selectedCourse, ops);


//        for (Course cour : courses) {
//            if (cour.getId() == id) {
//                a.setId(id);
//                int position;
//                position = courses.indexOf(cour);
//                courses.set(position, a);
//                return Response.status(Response.Status.OK).build();
//            }
//        }
        return Response.status(Response.Status.OK).build();
    }

}
