package com.projectzero.demo.models;

import jakarta.persistence.*;
import java.util.Set;

@Entity
@Table(name = "cities", uniqueConstraints = {@UniqueConstraint(columnNames = {"city", "country"})})
public class City {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "city", nullable = false, length = 255)
    private String city;

    @Column(name = "country", nullable = false, length = 255)
    private String country;

    @OneToMany(mappedBy = "city", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<UserCity> userCities;

    // Constructors
    public City() {}

    public City(String city, String country) {
        this.city = city;
        this.country = country;
    }

    // Getters and setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Set<UserCity> getUserCities() {
        return userCities;
    }

    public void setUserCities(Set<UserCity> userCities) {
        this.userCities = userCities;
    }
}
