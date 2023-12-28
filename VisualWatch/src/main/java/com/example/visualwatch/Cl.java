package com.example.visualwatch;

public interface Cl {
    public float Get_price();
    public String Get_name();
    public Type Get_type();
    public void  setID(int ID);
    public int getID();

    void Set_time(Time type, int znach)throws IncorectData, InvalidType;
    void Perev(Time type, int znach)throws IncorectData, InvalidType;

}
