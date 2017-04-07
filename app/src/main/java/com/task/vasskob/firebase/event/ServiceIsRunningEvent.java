package com.task.vasskob.firebase.event;

public class ServiceIsRunningEvent {

    private final boolean isRunning;

    public ServiceIsRunningEvent(boolean isRunning) {
        this.isRunning = isRunning;
    }

    public boolean isRunning() {
        return isRunning;
    }
}
