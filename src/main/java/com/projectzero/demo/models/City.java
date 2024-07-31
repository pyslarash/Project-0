package com.projectzero.demo.models;


import jakarta.persistence.*;
import java.util.Objects;


@Entity
@Table(name = "cities")
public Class City {
    @Id //makes this a primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="a_id",updatable = false)
    private int id;
@Column(nullable = false,unique = true)
private String name;

}