package ru.pvs.task;

public class TestSwitches {
    public int method(int value) {
        int a;
        switch (value) {
            case 1:
                a = 10;
                break;
            case 2:
                a = 20;
                break;
            default:
                a = 100;
                break;
        }
        return a;
    }
}
