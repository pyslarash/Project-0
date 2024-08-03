package com.projectzero.models;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "user_cities")
public class UserCities {

    @EmbeddedId
    private UserCitiesId id;

    // Getters and Setters for user and city
    @Setter
    @Getter
    @MapsId("userId")
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Setter
    @Getter
    @MapsId("cityId")
    @ManyToOne
    @JoinColumn(name = "city_id")
    private City city;

    // Constructors, Getters, and Setters
    public UserCities() {}

    @Getter
    @Setter
    @EqualsAndHashCode
    @Embeddable
    public static class UserCitiesId implements Serializable {

        @Column(name = "user_id")
        private Integer userId; // Changed to Integer to match the User ID type

        @Column(name = "city_id")
        private Integer cityId; // Changed to Integer to match the City ID type

        // Default constructor
        public UserCitiesId() {}

        public UserCitiesId(Integer userId, Integer cityId) {
            this.userId = userId;
            this.cityId = cityId;
        }
    }
}