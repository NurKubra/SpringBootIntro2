package com.tpe.repository;

import com.tpe.domain.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository //opsiyonel
public interface StudentRepository extends JpaRepository<Student,Long> {


    boolean existsByEmail(String email);

    List<Student> findByLastName(String lastName);

    //JPQL ile
    @Query("SELECT s FROM Student s WHERE s.grade=:pGrade")  //grade degeri pGrade olan tum student'lari getir demis olduk
    List<Student> findAllEqualGrade(@Param("pGrade") Integer grade);  //SpringFramework bu methodun nasil caliscagini bilmioyr cunku biz olusturudk bu yuzden nasil bir sorgu kullnamsi gerektigini @Query icine yaziyoruz
    //Parametreyi sorguda kullanabilmek icin @Param annatation kullanirim.


    //SQL ile
    @Query(value = "SELECT * FROM Student s WHERE s.grade=:pGrade",nativeQuery = true)
    List<Student> findAllEqualGradeWithSQL(@Param("pGrade") Integer grade);




    //JPQL mucizesi ile POJO-DTO dönüsümü
    //@Query("SELECT * FROM Student s WHERE s.id=:id") --> bu sekilde SQL sorgusu ile yaparsam bana Student objesi
    //döner ama ben DTO istiyorum bu yuzden JPQL kullanmaliyim !!!
    //POJO yu DTO ya cevirmek istiyoruz
    //JPQL sayesinde otomatik mapplme yapmis olduk
    @Query("SELECT new com.tpe.dto.StudentDto(s) FROM Student s WHERE s.id=:id")  //db den aldigimiz student objesini sorguda adresiyle belirtigimiz clasin objesine maplemis olduk
                                                                                 //bu islemi daha once StuentDto da olustudugumuz paramtreli constructor sayesinde yaptik
    Optional<Object> findStudentDtoById(@Param("id")Long id);  //custem bir method olausturduk ve bunun Query'sini yazdik
    //JPQL'in ne gibi bir avantaji var ?


}
