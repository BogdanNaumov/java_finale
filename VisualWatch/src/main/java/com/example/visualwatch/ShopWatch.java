package com.example.visualwatch;

import java.util.*;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;


public class ShopWatch implements Iterable<Cl>{
    ArrayList<Cl> cls;
    transient Connection c;

    void connect()
    {
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection(
                    "jdbc:sqlite:basa.db");
            System.out.println("Opened database successfully");
        } catch (ClassNotFoundException ex) {
            //не найден драйвер
            System.out.println("Non drav");
        } catch (SQLException ex) {
            System.out.println("Can't open DB");
        }
    }

    void selectAll(){
        try {
            cls.clear();
        }catch (Exception ex){

        }
        try {
            Statement st = c.createStatement();
            ResultSet rs = st.executeQuery("select* from Watchs_tabel");

            while (rs.next()) {
                int type = rs.getInt("type");
                Cl cl =  FabricWatch.new_watch((type == 0)?Type.Clock:Type.Clock_3,rs.getString("name"),rs.getFloat("price"));
                cl.Set_time(Time.Hour,rs.getInt("hour"));
                cl.Set_time(Time.Minut,rs.getInt("min"));
                if (type == 1)
                    cl.Set_time(Time.Sec,rs.getInt("sec"));
                cl.setID(rs.getInt("ID"));
                cls.add(cl);
            }

        } catch (SQLException ex) {
            Logger.getLogger(ShopWatch.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Eror");
        } catch (IncorectData e) {
            throw new RuntimeException(e);
        } catch (InvalidType e) {
            throw new RuntimeException(e);
        }
        events();
    }
    private String title;
    public String GetTitle(){return title;}
    public void SetTitele(String title){this.title = title;}


    private String Owner;
    public String GetOwner(){return Owner;}
    public void SetOwner(String Owner){this.Owner = Owner;}

    ArrayList<IOserver> allo = new ArrayList<>();

    void events(){
        for (IOserver o: allo){
            o.event(this);
        }
    }
    public void cum(IOserver o){
        allo.add(o);
        events();
    }



    public ShopWatch(String title, String Owner) {
        cls = new ArrayList<>();
        this.title = title;
        this.Owner = Owner;
        connect();
        selectAll();
    }

    public void MaxPriceWatch(){
        ArrayList<Cl> temp = new ArrayList<>();
        float max_cen = cls.get(0).Get_price();
        Comparator<Cl> comp = new Comparator<Cl>() {
            @Override
            public int compare(Cl o1, Cl o2) {
                return Float.compare(o2.Get_price(),o1.Get_price());
            }
        };
        cls.sort(comp);
        events();
    }

    public int size(){return cls.size();}
    public void add(Cl cl){
        try {
            PreparedStatement statement = c.prepareStatement(
                    "INSERT INTO Watchs_tabel (name, price, type, hour, min, sec) VALUES (?,?,?,?,?,?)",
                    Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, cl.Get_name());
            statement.setFloat(2, cl.Get_price());
            statement.setInt(3,(cl.Get_type() == Type.Clock_3)?1:0);


            ArrayList<Integer> temp = GetTime(cl);
            statement.setInt(4,temp.get(0));
            statement.setInt(5,temp.get(1));
            if (cl.Get_type() == Type.Clock_3) {
                statement.setInt(6, temp.get(2));
            }


            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating user failed, no rows affected.");
            }

            Statement st = c.createStatement();
            ResultSet rs = st.executeQuery("SELECT last_insert_rowid()");
            if (!rs.next()) {
                System.out.println("Something is wrong...");
                return;
            }
            final Integer id = rs.getInt("last_insert_rowid()");
            cl.setID(id);

//            ResultSet generatedKeys = statement.getGeneratedKeys();
//            if (generatedKeys.next()) {
//                cl.setID((int)generatedKeys.getLong(1));
//            }
        } catch (SQLException ex) {
            Logger.getLogger(ShopWatch.class.getName()).log(Level.SEVERE, null, ex);
        }
        cls.add(cl);
        events();
    }
    public void remove(Cl cl){
        try {
            PreparedStatement statement = c.prepareStatement(
                    "DELETE FROM Watchs_tabel WHERE ID = ?");
            statement.setInt(1, cl.getID());
            statement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(ShopWatch.class.getName()).log(Level.SEVERE, null, ex);
        }
        cls.remove(cl);
        events();
    }

    ArrayList<Integer> GetTime(Cl cl){
        ArrayList<Integer> temp = new ArrayList<>();
        String s = cl.toString(),hour= new String(),min= new String(),sec= new String();
        int i = 0;
        while(s.charAt(i) != ':'){
            hour += s.charAt(i);
            i++;
        }
        i++;
        temp.add(Integer.parseInt(hour));
        while(s.length() > i && s.charAt(i) != ':'){
            min += s.charAt(i);
            i++;
        }
        i++;
        temp.add(Integer.parseInt(min));
        if (cl.Get_type() == Type.Clock_3) {
            while(s.length() > i && s.charAt(i) != ':'){
                sec += s.charAt(i);
                i++;
            }
            temp.add(Integer.parseInt(sec));;
        }
        return temp;
    }


    public Iterator<Cl> iterator(){
        return cls.iterator();
    }
    public void SetTime(Time type, int znash){
        for (Cl a: cls){
            try {
                a.Set_time(type,znash);
                ArrayList<Integer> temp = GetTime(a);
                PreparedStatement statement = c.prepareStatement(
                        "UPDATE Watchs_tabel SET hour = ?,min = ?, sec = ?  WHERE ID = ?;");
                statement.setInt(1, temp.get(0));
                statement.setInt(2, temp.get(1));
                statement.setInt(3, a.Get_type()== Type.Clock_3? temp.get(2):0);
                statement.setInt(4, a.getID());
                statement.executeUpdate();
            }catch(IncorectData error){
                System.out.println(error);
            }catch (InvalidType error){

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        events();
    }

    public void PopularName(){
        int maxi = -1; String name = "";
        Map<String,Integer> arr = new HashMap<>();
        for(Cl a: cls){
            Integer it = arr.get(a.Get_name());
            if (it == null)
                arr.put(a.Get_name(),1);
            else
                arr.put(a.Get_name(),it + 1);
        }
        Comparator<Cl> comp = new Comparator<Cl>() {
            @Override
            public int compare(Cl o1, Cl o2) {
                return Integer.compare(arr.get(o2.Get_name()),arr.get(o1.Get_name()));
            }
        };
        cls.sort(comp);
        events();
    }

    public void Name(){
        Comparator<Cl> comp = new Comparator<Cl>() {
            @Override
            public int compare(Cl o1, Cl o2) {
                return CharSequence.compare(o1.Get_name(),o2.Get_name());
            }
        };
        cls.sort(comp);
        events();
    }


}
