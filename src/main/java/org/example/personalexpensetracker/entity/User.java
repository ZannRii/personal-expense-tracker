package org.example.personalexpensetracker.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
@ToString
@Getter
@Setter
public class User {

    private int id;
    private String name;
    private String password;
    private String email;
    private LocalDateTime create_time;
    private LocalDateTime update_time;
    private String create_user;
    private String update_user;
    public User(){

    }
}
