// src/main/java/net/amaha/learnmod/data/Task.java
package net.amaha.learnmod.data;

public class Task {
    private String name;
    private boolean completed;

    public Task(String name, boolean completed) {
        this.name = name;
        this.completed = completed;
    }

    public String getName() {
        return name;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}