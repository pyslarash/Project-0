package com.projectzero.demo.models;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "user_cities")
public class UserCity implements Serializable {

    @EmbeddedId
    private UserCityId id;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_user"))
    private User user;

    @ManyToOne
    @MapsId("cityId")
    @JoinColumn(name = "city_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_city"))
    private City city;

    // Constructors
    public UserCity() {}

    public UserCity(User user, City city) {
        this.user = user;
        this.city = city;
        this.id = new UserCityId(user.getId(), city.getId());
    }

    // Getters and setters
    public UserCityId getId() {
        return id;
    }

    public void setId(UserCityId id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }
}
