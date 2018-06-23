package org.kusmp.api;

import jersey.repackaged.com.google.common.collect.Lists;
import org.kusmp.api.dao.StudentDAO;
import org.kusmp.api.model.*;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;

import javax.ws.rs.*;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;


@Path("/")
public class Server {

    private final List<Student> students = mockModel.getStudentsList();
    private final List<Course> courses = mockModel.getCourses();
   // private final GradebookDataService dataService = GradebookDataService.getInstance();
    final Datastore datastore = MorphiaHandler.getInstance().getDatastore();
    final Query<Course> courseQuery = datastore.createQuery(Course.class);
    final Query<Student> studQuery = datastore.createQuery(Student.class);

    // private final List<Grade> grades = mockModel.getGrades();


    ///////////////
    @GET
    @Path("/students")
    @Produces({MediaType.APPLICATION_JSON})

    public List<Student> getStudentsList(@QueryParam("name") String name,
                                         @QueryParam("surname") String surname,
                                         @QueryParam("birthday") String birthday,
                                         @QueryParam("dateRelation") String dateRelation,
                                         @QueryParam("index") Long index
    ) {

        Query<Student> query = datastore.createQuery(Student.class);
        if (index != null) {
            query.field("index").equal(index);
                       }
        //filtering by name
        if(name != null){

            query = query.field("name").containsIgnoreCase(name);
        }

        //filtering by surname
        if(surname != null){
            query = query.field("surname").containsIgnoreCase(surname);
        }

        if(birthday != null && !birthday.equals("")){
            try {
                query.field("birthday").equal(dateFromString(birthday));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        //filtering by date
        if (birthday != null && dateRelation != null) {
            switch (dateRelation.toLowerCase()) {
                case "equal":
                    query.filter("birthday ==", birthday);
                    break;
                case "after":
                    query.filter("birthday >", birthday);
                    break;
                case "before":
                    query.filter("birthday <", birthday);
                    break;
            }
        }


        return query.asList();
    }

    private Date dateFromString(String date) throws ParseException {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date d = null;
        d = format.parse(date);

        return d;
    }

    @POST
    @Path("/students")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response addStudent(Student student) {

        Student a = new Student(student.getName(), student.getSurname(), student.getBirthday(), student.getGrades());
        a.setIndex(StudentDAO.generateStudentIndex());
        datastore.save(a);
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

        return studentQuery.asList();
        // return Response.status(Response.Status.NOT_FOUND).build();
    }

    @PUT
    @Path("/students/{index}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response updateStudent(@PathParam("index") long index, Student student) {


        Student a = new Student(student.getName(), student.getSurname(), student.getBirthday(), student.getGrades());
        Student selectedStudent = studQuery.field("index").equal(index).get();

        UpdateOperations ops = datastore
                .createUpdateOperations(Student.class)
                .set("name", a.getName())
                .set("surname", a.getSurname())
               // .set("birthday", a.getBirthday())
               // .set("grades", selectedStudent.getGrades())
                .set("index", index);
        if(a.getBirthday() == null){
            ops.unset("birthday");
        } else ops.set("birthday", a.getBirthday());
        datastore.update(selectedStudent, ops);
        return Response.status(Response.Status.OK).build();

    }

    @DELETE
    @Path("/students/{index}")
    public Response deleteStudent(@PathParam("index") long index) {
        Query<Student> removeStudent = studQuery.field("index").equal(index);
        datastore.delete(removeStudent);

        return Response.status(Response.Status.OK).build();
    }
    ///////////////

    @GET
    @Path("/students/{index}/grades")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Object getGradesOfStudent(
            @PathParam("index") int index,
            @QueryParam("course") String course,
            @QueryParam("greaterGrade") Double graeterGrade,
            @QueryParam("lessGrade") Double lessGrade,
            @QueryParam("value") Double value,
            @QueryParam("id") Integer id,
            @QueryParam("date") String date
    ) throws ParseException {
        List<Grade> grades =datastore.createQuery(Student.class).filter("index", index).get().getGrades();

        Iterator<Grade> iterator = grades.iterator();
        while (iterator.hasNext()) {
            Grade grade = iterator.next();
            if (course != null && !grade.getCourse().getName().contains(course)) {
                iterator.remove();
                continue;
            }
            if (graeterGrade != null && grade.getValue() <= graeterGrade) {
                iterator.remove();
                continue;
            }
            if (lessGrade != null && grade.getValue() >= lessGrade) {
                iterator.remove();
                continue;
            }
            if (value != null && grade.getValue() != value) {
                iterator.remove();
                continue;
            }
            if (id != null && grade.getId() != id) {
                iterator.remove();
                continue;
            }
            if (date != null && !date.equals("") && !grade.getDate().equals(dateFromString(date))) {
                iterator.remove();
                continue;
            }
        }

        //if(grades != null && grades.size() > 0){
        return new GenericEntity<List<Grade>>(grades) {
        };
//        }else{
//            return Response.status(Response.Status.NOT_FOUND).build();
//        }
    }



//    public List<Grade> getGrades(@PathParam("index") long index,
//                                 @QueryParam("course") String course,
//                                 @QueryParam("value") String value,
//                                 @QueryParam("valueRelation") String valueRelation) {
//        Student selectedStudent = studQuery.field("index").equal(index).get();
//        List<Grade> grades = new ArrayList<>();
//               grades = selectedStudent.getGrades();
//        if (course != null) {
//            grades = grades.stream().filter(gr -> gr.getCourse().getName().equals(course)).collect(Collectors.toList());
//        }
//
//        if (value != null && valueRelation != null) {
//            switch (valueRelation.toLowerCase()) {
//                case "greater":
//                    grades = grades.stream().filter(gr -> gr.getValue() > Float.valueOf(value).floatValue()).collect(Collectors.toList());
//                    break;
//                case "lower":
//                    grades = grades.stream().filter(gr -> gr.getValue() < Float.valueOf(value).floatValue()).collect(Collectors.toList());
//                    break;
//            }
//        }
//
//
//
//        return grades;
//    }

    @POST
    @Path("/students/{index}/grades")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response addGradeToStudent(@PathParam("index") long index, Grade grade) {

        Course selectedCourse = courseQuery.field("id").equal(grade.getCourse().getId()).get();
        // System.out.println(selectedCourse.getId());
        List<Grade> grades;
        if(selectedCourse!=null){
            Student student = studQuery.field("index").equal(index).get();
            if(student.getGrades() == null){
                grades = new ArrayList<>();
            }
            else grades = student.getGrades();
            Grade a = new Grade(grade.getDate(), grade.getValue());
            a.setCourse(selectedCourse);
            System.out.println(a.getCourse().getName());
            grades.add(a);
            UpdateOperations<Student> updateOps;
            updateOps = datastore.createUpdateOperations(Student.class).set("grades", grades);
            datastore.update(student, updateOps);
            return Response.status(Response.Status.CREATED).build();
        } else return Response.status(Response.Status.NO_CONTENT).entity("No courses to assign").build();


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
        Course selectedCourse = courseQuery.field("id").equal(grade.getCourse().getId()).get();
        Grade a = new Grade(grade.getDate(), grade.getValue());
        Student student = studQuery.field("index").equal(index).get();
        System.out.println("Course ID: " + grade.getCourse().getId());
        List<Grade> grades = new ArrayList<>();
        grades = student.getGrades();
        for(Grade grd : grades){
            if(grd.getId() == id){
                int position = grades.indexOf(grd);

                a.rewriteId(id);
                a.setCourse(selectedCourse);
                System.out.println(grades.get(position).getCourse());
                grades.set(position, a);

                UpdateOperations<Student> updateOps;
                updateOps = datastore.createUpdateOperations(Student.class).set("grades", grades);
                datastore.update(student, updateOps);
                return Response.status(Response.Status.OK).build();
            }
        }


        return Response.status(Response.Status.NOT_FOUND).build();
    }
    ///////////////

    @GET
    @Path("/courses")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Course> returnCourses(@QueryParam("lecturer") String lecturer,
                                      @QueryParam("name") String name,
                                      @QueryParam("id") int id
    ) {

        final Query<Course> query = datastore.createQuery(Course.class);
        if (lecturer != null)
            query.field("lecturer").containsIgnoreCase(lecturer);

        if(name != null){
                            query.field("name").containsIgnoreCase(name);
                        }
                    if(id != 0){
                            query.field("id").equal(id);
                       }
        System.out.println("GET COURSE");
        return query.asList();


        //  return courses;
        //return courseQuery.asList();
    }

    @POST
    @Path("/courses")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response addCourse(Course course) {

        Course a = new Course(course.getName(), course.getLecturer());
      //  a.setId(StudentDAO.generateCourseId());
       // a.setId(StudentDAO.generateCourseIndex());
        datastore.save(a);

        return Response.status(Response.Status.CREATED).build();
    }

    @GET
    @Path("/courses/{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Course getCourseById(@PathParam("id") long id) {
        Course course = courseQuery.field("id").equal(id).get();

        return course;
    }

    @DELETE
    @Path("/courses/{id}")
    public Response deleteCourse(@PathParam("id") long id) {

        Query<Course> query = datastore.createQuery(Course.class).filter("id", id);
        Query<Student> query2 = datastore.createQuery(Student.class);

        Course course = query.get();

        if(course == null){
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        try{
            for(Student student : query2.asList()){
                Iterator<Grade> iterator = student.getGrades().iterator();
                while(iterator.hasNext()) {
                    Grade grade = iterator.next();
                    if (grade.getCourse().getId() == course.getId()) {
                        iterator.remove();
                    }
                }
                datastore.save(student);
            }
        }catch(Exception e){
            e.printStackTrace();
        }

        datastore.delete(query);
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



        return Response.status(Response.Status.OK).build();
    }

}