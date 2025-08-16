package org.example.personalexpensetracker;

public interface ApplicationLauncher {

    static void main(String... args){
        new PersonalExpenseTracker(args);
    }
}
