package org.example.personalexpensetracker.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
@ToString
@Getter
@Setter
public class Transaction {

    private String type;   // "INCOME" or "EXPENSE"
    private double amount;
    private String category_name;
    private LocalDateTime date;
    private int id;
    private int category_id;
    private Category category;
}
