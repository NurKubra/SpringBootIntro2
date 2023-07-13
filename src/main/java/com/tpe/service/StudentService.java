package com.tpe.service;

import com.tpe.domain.Student;
import com.tpe.dto.StudentDto;
import com.tpe.exception.ConflictException;
import com.tpe.exception.ResourceNotFoundException;
import com.tpe.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;


    public List<Student> getAll() {

    return  studentRepository.findAll();
    }

    public void createStudent(Student student) {
        if(studentRepository.existsByEmail(student.getEmail())){
         throw new ConflictException("Email is already exist!!");
        }
        studentRepository.save(student);

    }

    public Student findStudent(Long id) {

       return studentRepository.findById(id).orElseThrow(
               ()-> new ResourceNotFoundException("Student not found with id= "+id));
    }

    public void deleteStudent(Long id) {
        Student student = findStudent(id);  //ogrencinin varligini kontrol etmek icn yukardaki methodu kullandik
        studentRepository.delete(student);  //delete() methodunda direk icine bir entity yazmmami istiyor bu yuzden oncelikle bu id li ogrenci var mi diye bakmam lazim
    }

    public void updateStudent(Long id, StudentDto studentDto) {
        //3 kontrol yapmaliyim
        //1-email db de var mi
        boolean emailExist = studentRepository.existsByEmail(studentDto.getEmail()); //studentDTO ile gelen emaile var mi diye bakiyoruz --> boolen doncek

        //2-istenilen id de student var mi
        Student student= findStudent(id);  //POJO daki objeyi doncek

        //3-girilen maili kabul edebilir miyim edemez miyim ona bakiyorum
        if(emailExist && !studentDto.getEmail().equals(student.getEmail())){
            throw new ConflictException("Email is already exist");
        }
        //burda onemli nokta senaryonun if icine girmemesini saglamak --> bu sayede update islemi basarili gerceklesir.
        //ayrica && mukemmelliyetci olmasi nedeniyle tek bir false if bloguna girisi önler!!
        /*
       senaryo 1 = kendi mailim mrc, mrc girdim db de var (update olur)
       senaryo 2 = kendi mailim mrc, ahmt girdim ve db de var (exception)
       senaryo 3= kendi mailim mrc, mhmt girdim db de yok (update olur)
         */

        //DTO objesini POJO ya ceviriyorum -->
        student.setName(studentDto.getFirstName());  //field'in ismini degistirdigim icin burda setliyorum
        student.setLastName(studentDto.getLastName()); //diger tum field'leri de Pojo ya setliyorum
        student.setGrade(studentDto.getGrade());
        student.setEmail(studentDto.getEmail());
        student.setPhoneNumber(studentDto.getPhoneNumber());
        //not burda createDate 'i set edemem cunku basta otomatik olusturdum

        studentRepository.save(student);
    }

    public Page<Student> getAllWithPage(Pageable pageable) {

       return studentRepository.findAll(pageable);
    }


    public List<Student> findStudent(String lastName){
        return studentRepository.findByLastName(lastName);
    }

    public List<Student> findAllEqualGrade(Integer grade) {

        return studentRepository.findAllEqualGrade(grade);
        //return studentRepository.findAllEqualGradeWithSQL(grade);  burda SQL ile yazdigimiz methodu da denedik ayni sekilde casisti (postman de)
    }

    public StudentDto findStudentDtById(Long id) {
        //normalde DTO yu burda dönüstürüyorduk ama bu defa repoda yapacagiz
        //methodumu kendimiz yapiyoruz --> id ile ilgili bir method oldugundan OPTIONAL bir yapi donmek istiyorum o yuzden burda Optional oldugunu belirtiyprum
        //orElseThrow varsa optional'dir !!!

        return (StudentDto) studentRepository.findStudentDtoById(id).
                orElseThrow(()-> new ResourceNotFoundException("Student not found with id: "+ id));

                //orElseThrow(()-> new ResourceNotFoundException("Student not found with id: "+ id));

    }
}


