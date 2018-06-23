"use strict";

var ServerURL = "http://localhost:8000/";

var apiSerwer = "http://localhost:8000/";

class Student {
    constructor(data = {name: "", surname: "", birthday: ""},sub = true){
        this.name = new ko.observable(data.name);
        this.surname = new ko.observable(data.surname);
        this.birthday = new ko.observable(data.birthday);
        this.index = new ko.observable(data.index);
        if(sub) {
                        this.addSubscribe();
                    }
    }

    addSubscribe(){
        this.name.subscribe(this.PUT.bind(this));
        this.surname.subscribe(this.PUT.bind(this));
        this.birthday.subscribe(this.PUT.bind(this));
        this.index.subscribe(this.PUT.bind(this));
    }

    getData(){
        return ko.toJSON({
            name: this.name,
            surname: this.surname,
            birthday: this.birthday
        });
    }

    PUT(){
        console.log(this.getData());
        $.ajax({
            url: "http://localhost:8000/students/" + ko.toJS(this.index),
            method: "PUT",
            data: this.getData(),
            async: false,
            headers: {
                Accept: "application/json",
                "Content-Type": "application/json"
            }
        });
    }

    DELETE(){
        $.ajax({
            url: "http://localhost:8000/students/" + ko.toJS(this.index),
            method: "DELETE",
            async: false,
            headers: {
                Accept: "application/json",
                "Content-Type": "application/json"
            }
        });
    }

    POST(){
        $.ajax({
            url: "http://localhost:8000/students/",
            method: "POST",
            async: false,
            data: this.getData(),
            headers: {
                Accept: "application/json",
                "Content-Type": "application/json"
            }
        });
    }
}
class Course {
    constructor(data = {name: "", lecturer: "", id: ""}){
        this.name = new ko.observable(data.name);
        this.lecturer = new ko.observable(data.lecturer);
        this.id = new ko.observable(data.id);
        this.addSubscribe();
    }

    addSubscribe(){
        this.name.subscribe(this.PUT.bind(this));
        this.lecturer.subscribe(this.PUT.bind(this));
        this.id.subscribe(this.PUT.bind(this));
    }

    getData(){
        return ko.toJSON({
            name: this.name,
            lecturer: this.lecturer
        });
    }

    PUT(){
        $.ajax({
            url: "http://localhost:8000/courses/" + ko.toJS(this.id),
            method: "PUT",
            data: this.getData(),
            async: false,
            headers: {
                Accept: "application/json",
                "Content-Type": "application/json"
            }
        });
    }

    DELETE(){
        $.ajax({
            url: "http://localhost:8000/courses/" + ko.toJS(this.id),
            method: "DELETE",
            async: false,
            headers: {
                Accept: "application/json",
                "Content-Type": "application/json"
            }
        });
    }

    POST(){
        $.ajax({
            url: "http://localhost:8000/courses/",
            method: "POST",
            async: false,
            data: this.getData(),
            headers: {
                Accept: "application/json",
                "Content-Type": "application/json"
            }
        });
    }
}



class Grade{
    constructor(data = {id: "", value: "", course: "", date: ""},student = 0){
        this.student = student;
        this.id = new ko.observable(data.id);
        this.value = new ko.observable(data.value);
        this.course = new ko.observable(data.course.id);
       // this.course = new ko.observable(data.course);
        this.date = new ko.observable(data.date);
        this.addSubscribe();
    }

    addSubscribe(){
        this.id.subscribe(this.PUT.bind(this));
        this.value.subscribe(this.PUT.bind(this));
        this.course.subscribe(this.PUT.bind(this));
        this.date.subscribe(this.PUT.bind(this));
    }

    getGrades(){
        return ko.toJSON({
            value: this.value,
            course: {id: this.course},
            date: this.date
        });
    }

    PUT(){
        console.log(this.getGrades());
        $.ajax({
            url: "http://localhost:8000/students/" + this.student + "/grades/" + ko.toJS(this.id),
            method: "PUT",
            data: this.getGrades(),
            async: false,
            headers: {
                Accept: "application/json",
                "Content-Type": "application/json"
            }
        });
    }

    POST(){
        console.log(this.getGrades());
        $.ajax({
            url: "http://localhost:8000/students/" + this.student + "/grades/",
            method: "POST",
            async: false,
            data: this.getGrades(),
            headers: {
                Accept: "application/json",
                "Content-Type": "application/json"
            }
        });
    }

    DELETE(){
        $.ajax({
            url: "http://localhost:8000/students/" + this.student + "/grades/" + ko.toJS(this.id),
            method: "DELETE",
            async: false,
            headers: {
                Accept: "application/json",
                "Content-Type": "application/json"
            }
        });
    }
}

class StudentSearch extends Student{
    constructor(observableArray ,data = {name: "", surname: "", birthday: "", index: ""},sub = true){
                super(data,sub);
                this.studentsObservableArray = observableArray;
            }

    addSubscribe(){
                this.name.subscribe(this.search.bind(this));
                this.surname.subscribe(this.search.bind(this));
                this.birthday.subscribe(this.search.bind(this));
                this.index.subscribe(this.search.bind(this));
           }

    search(){
                var mapping = {
                        create: function(options) {
                            return new Student(options.data);
                        }
                };
                var students = JSON.parse($.ajax({
                        url: "http://localhost:8000/students/?" + this.getSearchParametersString(),
                        method: "GET",
                        async: false,
                        headers: {
                            Accept: "application/json",
                                "Content-Type": "application/json"
                        },
                }).responseText);
                ko.mapping.fromJS(students,mapping,this.studentsObservableArray);
                console.log("http://localhost:8000/students/?" + this.getSearchParametersString());
            }

    getSearchParametersString(){
                return "index="+ko.toJS(this.index)+"&name="+ko.toJS(this.name)+"&surname="+ko.toJS(this.surname)+"&birthday="+ko.toJS(this.birthday)
                }
}

class CourseSearch extends Course {
    constructor(observableArray, data) {
        super(data);
        this.coursesObservableArray = observableArray;
    }

    addSubscribe() {
        this.name.subscribe(this.search.bind(this));
        this.lecturer.subscribe(this.search.bind(this));
        this.id.subscribe(this.search.bind(this));
    }

    search() {
        var mapping = {
            create: function (options) {
                return new Course(options.data);
            }
        };
        var courses = JSON.parse($.ajax({
            url: "http://localhost:8000/courses/?" + this.getSearchParametersString(),
            method: "GET",
            async: false,
            headers: {
                Accept: "application/json",
                "Content-Type": "application/json"
            },
        }).responseText);
        console.log(courses);
        ko.mapping.fromJS(courses, mapping, this.coursesObservableArray);
        console.log("http://localhost:8000/courses/?" + this.getSearchParametersString());
    }

    getSearchParametersString() {
        return "id=" + ko.toJS(this.id) + "&name=" + ko.toJS(this.name) + "&lecturer=" + ko.toJS(this.lecturer);
    }
}

class GradeSearch extends Grade{
    constructor(observableArray ,data,student){
                super(data,student);
               this.gradesObservableArray = observableArray;
            }

    addSubscribe(){
                this.id.subscribe(this.search.bind(this));
                this.value.subscribe(this.search.bind(this));
                this.course.subscribe(this.search.bind(this));
                this.date.subscribe(this.search.bind(this));
            }

    search(){
                var mapping = {
                       create: function(options) {
                            return new Grade(options.data,this.student);
                        }
                };
                var grades = JSON.parse($.ajax({
                        url: "http://localhost:8000/students/"+this.student+"/grades/?" + this.getSearchParametersString(),
                        method: "GET",
                        async: false,
                       headers: {
                            Accept: "application/json",
                                "Content-Type": "application/json"
                        },
                }).responseText);
                console.log(grades);
                ko.mapping.fromJS(grades,mapping,this.gradesObservableArray);
                console.log("value="+ko.toJS(this.value)+"&id="+ko.toJS(this.id)+"&date="+ko.toJS(this.date)+"&course="+ko.toJS(this.course));
            }

    getSearchParametersString(){
                return "value="+ko.toJS(this.value)+"&id="+ko.toJS(this.id)+"&date="+ko.toJS(this.date)+"&course="+ko.toJS(this.course) }
}

var gradesSystemModel = function(){
    var self = this;

    this.students = ko.observableArray([]);
    this.studentToAdd = new Student();
    this.studentSearch = new StudentSearch(this.students);
    this.courses = ko.observableArray([]);
    this.courseToAdd = new Course();
    this.courseSearch = new CourseSearch(this.courses);
    this.grades = ko.observableArray([]);
    this.gradeToAdd = new Grade();
    this.gradeSearch = new GradeSearch(this.grades);
    this.currentStudent = new Student({firstname: "", lastname: "", birthday: ""},false);


    this.getStudents = function() {
        var mapping = {
            create: function(options) {
                return new Student(options.data);
            }
        };
        var studnets = JSON.parse($.ajax({
            url: apiSerwer + 'students',
            async: false,
            headers: {
                Accept: "application/json",
            }
        }).responseText);
        ko.mapping.fromJS(studnets,mapping,self.students);
    }
    this.getStudents();
    this.addStudent = function(){
        self.studentToAdd.POST();
        self.getStudents();
        ko.mapping.fromJS(new Student,{},self.studentToAdd);
    }
    this.deleteStudent = function(student){
        student.DELETE();
        self.getStudents();
    }

    this.getCourses = function() {
        var mapping = {
            create: function(options) {
                return new Course(options.data);
            }
        };
        var courses = JSON.parse($.ajax({
            url: apiSerwer + 'courses',
            async: false,
            headers: {
                Accept: "application/json",
            }
        }).responseText);
        ko.mapping.fromJS(courses,mapping,self.courses);
    }
    this.getCourses();
    this.addCourse= function(){
        self.courseToAdd.POST();
        self.getCourses();
        ko.mapping.fromJS(new Course,{},self.courseToAdd);
    }
    this.deleteCourse = function(course){
        course.DELETE();
        self.getCourses();
    }


    this.getGradesForStudent = function(index){
        var mapping = {
            create: function(options) {
                return new Grade(options.data, index);
            }
        };
        var grades = JSON.parse($.ajax({
            url: apiSerwer + 'students/'+index+'/grades',
            async: false,
            headers: {
                Accept: "application/json"
            }
        }).responseText);
        ko.mapping.fromJS(grades,mapping,self.grades);
        self.gradeToAdd.student = index;
    }
    this.addGrade = function(){
        self.gradeToAdd.POST();
        self.getGradesForStudent(self.gradeToAdd.student);
        //ko.mapping.fromJS(new Grade,{},self.gradeToAdd);
    }
    this.deleteGrade = function(grade){
        grade.DELETE();
        self.getGradesForStudent(self.gradeToAdd.student);
    }


    self.onClickGrades = function(data) {
        var dataJS = ko.toJS(data);
        ko.mapping.fromJS(dataJS,{},self.currentStudent);
        self.gradeSearch.student = dataJS.index;
        if(typeof dataJS.index !== 'undefined'){
            self.getGradesForStudent(dataJS.index);
        }
        window.location = "#grades";
    }
}

// var gradesSystemModel = function(){
//     var self = this;
//
//     this.students = ko.observableArray([]);
//     this.studentToAdd = new Student();
//     this.courses = ko.observableArray([]);
//     this.courseToAdd = new Course();
//     this.grades = ko.observableArray([]);
//     this.studentSearch = new StudentSearch(this.students);
//     this.courseSearch = new CourseSearch(this.courses);
//     this.gradeSearch = new GradeSearch(this.grades);
//     this.gradeToAdd = new Grade();
//     this.currentStudent = new Student({name: "", surname: "", birthday: ""},false);
//
//
//         this.getStudents = function() {
//             var mapping = {
//                 create: function(options) {
//                     return new Student(options.data);
//                 }
//             };
//             var studnets = JSON.parse($.ajax({
//                 url: apiSerwer + 'students',
//                 async: false,
//                 headers: {
//                     Accept: "application/json",
//                 }
//             }).responseText);
//             ko.mapping.fromJS(studnets,mapping,self.students);
//         }
//     this.getStudents();
//     this.addStudent = function(){
//         self.studentToAdd.POST();
//         self.getStudents();
//         ko.mapping.fromJS(new Student,{},self.studentToAdd);
//     }
//     this.deleteStudent = function(student){
//         student.DELETE();
//         self.getStudents();
//     }
//
//     this.getCourses = function() {
//         var mapping = {
//             create: function(options) {
//                 return new Course(options.data);
//             }
//         };
//         var courses = JSON.parse($.ajax({
//             url: apiSerwer + 'courses',
//             async: false,
//             headers: {
//                 Accept: "application/json",
//             }
//         }).responseText);
//         ko.mapping.fromJS(courses,mapping,self.courses);
//     }
//     this.getCourses();
//     this.addCourse= function(){
//         self.courseToAdd.POST();
//         self.getCourses();
//         ko.mapping.fromJS(new Course,{},self.courseToAdd);
//     }
//     this.deleteCourse = function(course){
//         course.DELETE();
//         self.getCourses();
//     }
//
//
//     this.getGradesForStudent = function(index){
//         var mapping = {
//             create: function(options) {
//                 return new Grade(options.data, index);
//             }
//         };
//         var grades = JSON.parse($.ajax({
//             url: apiSerwer + 'students/'+index+'/grades',
//             async: false,
//             headers: {
//                 Accept: "application/json"
//             }
//         }).responseText);
//         ko.mapping.fromJS(grades,mapping,self.grades);
//         self.gradeToAdd.student = index;
//     }
//     this.addGrade = function(){
//         self.gradeToAdd.POST();
//         self.getGradesForStudent(self.gradeToAdd.student);
//         ko.mapping.fromJS(new Grade,{},self.gradeToAdd);
//     }
//     this.deleteGrade = function(grade){
//         grade.DELETE();
//         self.getGradesForStudent(self.gradeToAdd.student);
//     }
//
//
//     self.onClickGrades = function(data) {
//         var dataJS = ko.toJS(data);
//         ko.mapping.fromJS(dataJS,{},self.currentStudent);
//         self.gradeSearch.student = dataJS.index;
//         if(typeof dataJS.index !== 'undefined'){
//             self.getGradesForStudent(dataJS.index);
//         }
//         window.location = "#grades";
//     }



var viewModel = new gradesSystemModel();

ko.applyBindings(viewModel);
