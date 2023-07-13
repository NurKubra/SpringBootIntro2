package com.tpe.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
//@RequiredArgsConstructor  //final keyword ile kullanilan fieldlar icin constructor olusturur yalnizca
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //@Getter  // class yerine field seviyesiden eklemek istersek
    private Long id;

    @NotNull(message = "first name can not be null")
    @NotBlank(message = "first name can not be white space")
    @Size(min=2, max=25, message = "first name ${validatedValue} muss be between {min} and {max} long")
    @Column(nullable = false, length = 25)
    private /*final*/String name;  //eger @RequiredArgsConstructor kullanirsal final ile isaretleyip constructor uretebilirm

    @Column(nullable = false, length = 25)
    private String lastName;

    private Integer grade;

    @Email(message = "Provide valid email")
    @Column(nullable = false, unique = true)
    private String email;

    private String phoneNumber;

    //@Setter(AccessLevel.NONE) //class seviyesinde ekledikten sonra setter kullanilmasin istersek ekleriz
    private LocalDateTime createDate = LocalDateTime.now();

    //bir ogrenci birden fazla kitap alcagi icin kitaplari bir list olarak yazdik
    @OneToMany(mappedBy = "student")
    private List<Book> book = new ArrayList<>();



}
