package com.cachiama.springbootwebsocket.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class UserDto {

    @NotNull
    @Length(min = 3)
    private String name;

}
