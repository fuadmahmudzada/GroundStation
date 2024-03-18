package org.example.groundstationdemo;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;

public class RollingWindow<T> {
    private final Deque<T> window;
    private final int maxSize;

    public RollingWindow(int maxSize) {
        this.maxSize = maxSize;
        this.window = new LinkedList<>();
    }

    public void add(T item) {
        if (window.size() == maxSize) {
            window.removeFirst();
        }
        window.addLast(item);
    }

    public ObservableList<T> getWindowData() {
        return FXCollections.observableList(new ArrayList<>(window));
    }
}