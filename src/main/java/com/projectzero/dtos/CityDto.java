package com.projectzero.dtos;

import lombok.*;

import java.util.Set;

@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CityDto {
    private Integer id;
    private String city;
    private String country;
    private Set<UserDto> users; // Include this if you need to return associated users

    // Add this constructor if needed
    public CityDto(Integer id, String city, String country) {
        this.id = id;
        this.city = city;
        this.country = country;
    }
}