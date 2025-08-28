package org.example.personalexpensetracker.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
@ToString
@Getter
@Setter
public class PaymentMethod {
    private int payment_method_id;
    private String  method_name;
    private LocalDateTime create_time;
    private LocalDateTime update_time;
    private String create_user;
    private String update_user;
    public PaymentMethod(){

    }

}

