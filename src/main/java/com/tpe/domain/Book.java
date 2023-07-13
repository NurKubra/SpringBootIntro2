package com.tpe.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Setter
@NoArgsConstructor
public class Book {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonProperty("bookName")  //sadece JSON ciktidaki key degerini degistirir!! Java ve DB dekini degistirmez!!
    private String name;

    //bir ogrenci birden fazla kitap alabilsin seklinde bir iliski kuruyorum.
    @JsonIgnore  //book objemi JSON a maplerken student ı alma
    @ManyToOne
    @JoinColumn(name="student_id")  //yazmak zorunda degilz ama kod okunurlugnu arttirmak icin yazdim
    private Student student;

    //getter
    public Long getId(){
        return id;
    }
    public String getName(){
        return name;
    }
    public Student getStudent(){
        return student;
    }
}
