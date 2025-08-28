package org.example.personalexpensetracker.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
@ToString
@Getter
@Setter
public class Expense {

    private int income_id;
    private int user_id;
    private int category_id;
    private int payment_method_id;
    private Double amount;
    private LocalDateTime date;
    private LocalDateTime create_time;
    private LocalDateTime update_time;
    private String create_user;
    private String update_user;
    public Expense(){

    }

}
