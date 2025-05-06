package com.amit_g.repository.BASE;

public class TaskResult<T> {
    private final T result;

    public TaskResult(T result) {
        this.result = result;
    }

    public T getResult() {
        return result;
    }
}