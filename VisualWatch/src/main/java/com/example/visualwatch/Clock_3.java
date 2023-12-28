package com.example.visualwatch;

public class Clock_3 extends Clock {
    private int sec;
    public void  Set_sec(int sec){this.sec = sec;}
    public int Get_sec(){return sec;}
    public Clock_3(String name, float price){
        super(name,price);
        super.type = Type.Clock_3;
    }

    @Override
    public String toString(){
        return "" + hour + " " + minut + " " + sec;
    }


    @Override
    public void Set_time(Time type, int znach) throws IncorectData,InvalidType{
        if (znach < 0)
            throw new IncorectData();
        try {
            super.Set_time(type, znach);
        }catch (InvalidType temp){
            switch (type){
                case Sec:
                    if (znach > 60)
                        throw new IncorectData();
                    this.sec = znach;
                    break;
                default:
                    throw new InvalidType();
            }
        }
    }
    @Override
    public void Perev(Time type, int znach) throws IncorectData, InvalidType{
        if (znach < 0)
            throw new IncorectData();
        try {
            super.Perev(type, znach);
        }catch (InvalidType temp){
            switch (type){
                case Sec:
                    this.sec += znach;
                    super.Perev(Time.Minut,sec / 60);
                    sec %= 60;
                    break;
                default:
                    throw new InvalidType();
            }
        }
    }

}
