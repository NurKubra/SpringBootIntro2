package com.tpe.controller;

import com.tpe.domain.Student;
import com.tpe.dto.StudentDto;
import com.tpe.service.StudentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/student") //http://localhost:8080/students
public class StudentController {


    Logger loger = LoggerFactory.getLogger(StudentController.class);  //logger objesi olusturdum

    @Autowired
    private StudentService studentService;

    //Butun ogrencileri getirelim
    @GetMapping //http://localhost:8080/student +GET
    public ResponseEntity<List<Student>> getAll(){

        List<Student> students = studentService.getAll();

        return ResponseEntity.ok(students);  //List<Student> +HTTP.Status code :200
    }

    //Create new student

    @PostMapping //http://localhost:8080/student + POST +JSON
    public ResponseEntity<Map<String,String>> createStudent(@Valid @RequestBody Student student){
        studentService.createStudent(student); //requesten gelen mapplenmis datayi methodum ile service e gönderiyorum.

        Map<String,String> map = new HashMap<>(); //sadece burda kullancagimiz ve baska yerde ihtiyacimiz olmadiigndan new'leme yaptik
        map.put("message","Student is created succesfully");
        map.put("status","true");

        return new ResponseEntity<>(map, HttpStatus.CREATED);  //201 bu defa yukardakinden farkli bir yapi kullandik new'leme yaptik


    }

    //Belli bir id li ogrenciyi getirme --Get a student by id via RequestParam
    @GetMapping("/query")  //http://localhost:8080/student/query?id=1     //burdaki id RequestParamdaki id ile ayni
    public ResponseEntity<Student> getStudent(@RequestParam("id") Long id){  //@requestParam ile aldigim id yi Long tipindeki id ile mappledim,
        Student student = studentService.findStudent(id);
        return ResponseEntity.ok(student);  //ResponseEntity<>(map, HttpStatus.OK) ile ayni ama daha kisa hali
    }

    //--Get a student by id via PathVariable

    @GetMapping("{id}")   //method ici yukardaki ile ayni yalnizca bu defa PathVarible kullandik
    public ResponseEntity<Student> getStudentWithPath(@PathVariable("id") Long id){
        Student student = studentService.findStudent(id);
        return ResponseEntity.ok(student);
    }

    //delete Student with id
    @DeleteMapping("{id}")
    public ResponseEntity<Map<String,String>> deleteStudent(@PathVariable("id") Long id){

        studentService.deleteStudent(id);

        //Controller'in diger gorevi olan gelen requesti, kullaniciya responce olarak donme-> bu kismi responce icin yaptik !!
        //bu yapi yani key-value seklinde olmak zorunda degil, farkli yapilar kullanilabilir.
        Map<String,String> map = new HashMap<>(); //sadece burda kullancagimiz ve baska yerde ihtiyacimiz olmadiigndan new'leme yaptik
        map.put("message","Student is deleted succesfully");
        map.put("status","true");

        return new ResponseEntity<>(map,HttpStatus.OK);  //ya da return ResponceEntity.ok(map);

    }


    //update Student
    @PutMapping("{id}") //http://localhost:8080/student/1 --> endpoint +id + JSON +HTTP Method //not gelcek olan id mi @PathVarible ile gelcek olan JSON i ise @RequestBody ile alamm lazim
    public ResponseEntity<Map<String,String>> updateStudent(@PathVariable("id") Long id, @RequestBody StudentDto studentDto ){
        studentService.updateStudent(id,studentDto);

        Map<String,String> map = new HashMap<>();
        map.put("message","Student is updated succesfully");
        map.put("status","true");

        return new ResponseEntity<>(map,HttpStatus.OK);

    }

    //pageable
    @GetMapping("/page") //http://localhost:8080/student/page?page=0&size=2&sort=name&direction=ASC            //page yapisiyla tum datalarimi getir demis oldum
    public ResponseEntity<Page<Student>> getAllWithPage(
            //her bir parametreyi RequestParam ile aliyorum ,3 ve 4 opsiyonel!!
            @RequestParam("page") int page, //kacinci sayfa gelsin
            @RequestParam("size") int size, //sayfa basi kac urun
            @RequestParam("sort") String prop, //hangi fielda gore siralancak
            @RequestParam("direction") Sort.Direction direction  //siralama turu
    ){
        //bu obje olusurken ihtiyac duydugu paramtrelri yukardan verdik
        Pageable pageable = PageRequest.of(page,size,Sort.by(direction,prop));  //pagebale objesi olusturoyorum -
        Page<Student> studentPage= studentService.getAllWithPage(pageable); //pageable objemi icine gonderdim, donen data tipi de Page yapida olcak

        return ResponseEntity.ok(studentPage);

    }
        //not :http://localhost:8080/student/page?page=0&size=2&sort=name&direction=ASC  örnek endpoint bu sekilde yazilmali !!


    //get by lastName --> ögrenciyi soyadiyla getirme
    @GetMapping("/querylastname")
    public ResponseEntity<List<Student>> getStudentByLastName(@RequestParam("lastName") String lastName){  //endpointle bir lastName gelcek onu String bir degiskenle mappliyoruz

       List<Student> list = studentService.findStudent(lastName);

        return ResponseEntity.ok(list);
    }


    //get All Student By Grade -- grade puanina gore ogrenci getirme (JPQL hazir method degil kendimiz sorgu yazacagiz)
    @GetMapping("/grade/{grade}")  //http://localhost:8080/student/grade/75 +GET
    public  ResponseEntity<List<Student>> getStudentEqualsGrade(@PathVariable("grade") Integer grade){
        List<Student> list= studentService.findAllEqualGrade(grade);

        return ResponseEntity.ok(list);

    }

    //db den direk DTO olark datami almak istersem  --> normalde dönüsümümü service yaparim bu sefer repoda yapacagim
    //1 id li ogrenciyi getircem db den dto olarak getircem
    @GetMapping("/query/dto")  //http://localhost:8080/student/query/dto?id=1
    public ResponseEntity<StudentDto> getStudentDto(@RequestParam("id") Long id){
        StudentDto studentDto= studentService.findStudentDtById(id);

        return ResponseEntity.ok(studentDto);
    }

    //view
    @GetMapping("/welcome")  //http://localhost:8080/student/welcome +GET
    public String welcome(HttpServletRequest request){

        loger.warn("-----------------Welcome{}",request.getServletPath());

        return "Welcome to Student Controller";
    }


}
