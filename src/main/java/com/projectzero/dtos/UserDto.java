package com.projectzero.dtos;

import lombok.*;

import java.util.Set;

@Data
@Setter
@Getter
@NoArgsConstructor
public class UserDto {
    private Integer id;
    private String login;
    private String email;
    private Set<CityDto> cities;

    public UserDto(Integer id, String login, String email, Set<CityDto> cities) {
        this.id = id;
        this.login = login;
        this.email = email;
        this.cities = cities;
    }
}
