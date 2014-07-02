package test;

import java.io.IOException;

/**
 * Created by hasithad on 5/16/14.
 */

public class StudentManager {
    private Student[] students;
    private String json;

    public StudentManager() {
        students = new Student[2];

        students[0] = new Student(1234,"Amal", "De Silva");
        students[1] = new Student(4321, "John", "Carter");

        try {
            json = JSONConverter.convertToJSON(students[0]);
        } catch (IOException e) {
            json="{error}";
        }
    }

    public Student[] getStudents() {
        return students;
    }


    public String getJson() {
        return json;
    }
}