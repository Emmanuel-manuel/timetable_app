/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package timetable_app;

/**
 *
 * @author Lenovo-x130
 */
public class TimetableCell {

    private final String placeholder; // e.g., "A1", "B2"
    private String learningArea;      // e.g., "Mathematics"

    public TimetableCell(String placeholder) {
        this.placeholder = placeholder;
        this.learningArea = null; // Initially unassigned
    }

    // Getters and setters
    public String getPlaceholder() {
        return placeholder;
    }

    public String getLearningArea() {
        return learningArea;
    }

    public void setLearningArea(String learningArea) {
        this.learningArea = learningArea;
    }
}
