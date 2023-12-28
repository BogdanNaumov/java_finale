package com.example.visualwatch;

public class FabricWatch {
    static public Cl new_watch(Type typ, String name, float price)  {
        switch (typ) {
            case Clock:
                return new Clock(name, price);
            case Clock_3:
                return new Clock_3(name, price);
        }
        return null;
    }
}
