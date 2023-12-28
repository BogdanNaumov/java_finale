package com.example.visualwatch;

public class ClockFactory {
    static public Cl new_watch(Type type, String name, float price)  {
        switch (type) {
            case Clock:
                return new Clock(name, price);
            case Clock_3:
                return new Clock_3(name, price);
        }
        return null;
    }
}
