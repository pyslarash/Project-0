package com.projectzero.models;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "user_cities")
public class UserCities {

    @EmbeddedId
    private UserCitiesId id;

    @MapsId("userId")
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @MapsId("cityId")
    @ManyToOne
    @JoinColumn(name = "city_id")
    private City city;

    public UserCities() {}

    @Getter
    @Setter
    @EqualsAndHashCode
    @Embeddable
    public static class UserCitiesId implements Serializable {

        private Long userId;
        private Long cityId;

        // Default constructor
        public UserCitiesId() {}

        public UserCitiesId(Long userId, Long cityId) {
            this.userId = userId;
            this.cityId = cityId;
        }
    }

}
