package com.appperfect.searchengine.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

// represents a database table
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "cataloger")
public class Cataloger {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

//    @OneToMany(
//            mappedBy = "cataloger",
//            cascade = CascadeType.ALL,
//            orphanRemoval = true
//    )
//    private List<Document> documents = new ArrayList<>();


}
