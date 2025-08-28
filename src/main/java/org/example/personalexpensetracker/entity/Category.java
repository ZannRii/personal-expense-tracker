package org.example.personalexpensetracker.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
@ToString
@Getter
@Setter
public class Category {

    private int category_id;
    private String category_name;
    private LocalDateTime create_time;
    private LocalDateTime update_time;
    private String type;
    private String create_user;
    private String update_user;

    public Category(){

    }

}

