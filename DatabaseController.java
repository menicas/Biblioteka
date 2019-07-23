package projektbiblioteka;

import java.sql.*;
import java.util.ArrayList;

//todo wywalic returny z finally blockow

public class DatabaseController {

    private final String DB_USER = "****";
    private final String DB_PASS = "****";
    private final String DB_URL = "****?autoReconnect=true&useSSL=false";
    Connection conn;
    Statement stmt;

    public boolean dodajKsiazkeDoBazy(Ksiazka ksiazka) {
        try{
            String query = "INSERT INTO `ksiazki`(tytul, imiona_autora, nazwisko_autora, rok, kategorie, " +
                    "czy_wypozyczona, liczba_wypozyczen) VALUES (?, ?, ?, ?, ?, ?, ?)";
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, ksiazka.zwrocTytul());
            ps.setString(2, ksiazka.zwrocImionaAutora());
            ps.setString(3, ksiazka.zwrocNazwiskoAutora());
            ps.setInt(4, ksiazka.zwrocRok());
            ps.setString(5, ksiazka.zwrocKategorie());
            ps.setBoolean(6, ksiazka.zwrocCzyWypozyczona());
            ps.setInt(7, ksiazka.zwrocLiczbeWypozyczen());
            ps.executeUpdate();
        } catch (Exception e) {
            System.err.println("Database error");
            e.printStackTrace();
            return false;
        } finally {
            try{
                conn.close();
            } catch (SQLException e){
                System.err.println("Database error");
                e.printStackTrace();
            }
        }
        return true;
    }

    public Ksiazka odczytajKsiazkeZBazy(int id){
        Ksiazka result = null;
        try{
            String query = "SELECT * FROM ksiazki WHERE id=?";
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if(rs == null) return null;
            else {
                rs.next();
                result = new Ksiazka(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4),
                        rs.getInt(5), rs.getString(6), rs.getBoolean(7),
                        rs.getInt(8));
            }
        } catch (SQLException e){
            System.err.println("Database error");
            e.printStackTrace();
        } finally {
            try{
                conn.close();
            } catch (SQLException e){
                System.err.println("Database error");
                e.printStackTrace();
            }
        }
        return result;
    }

    public ArrayList<Ksiazka> odczytajWszystkieKsiazkiZBazy(){
        ArrayList<Ksiazka> result = new ArrayList<>();
        Ksiazka tmp;
        try{
            String query = "SELECT * FROM ksiazki";
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            if(rs == null) return null;
            else {
                while(rs.next()){
                    tmp = new Ksiazka(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4),
                            rs.getInt(5), rs.getString(6), rs.getBoolean(7),
                            rs.getInt(8));
                    result.add(tmp);
                }
            }
        } catch (SQLException e){
            System.err.println("Database error");
            e.printStackTrace();
        } finally {
            try{
                conn.close();
            } catch (SQLException e){
                System.err.println("Database error");
                e.printStackTrace();
            }
        }
        return result;
    }

    public int zwrocLiczbeKsiazek(){
        int result = -1;
        try{
            String query = "SELECT COUNT(*) FROM ksiazki";
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            rs.next();
            result = rs.getInt(1);
        } catch (SQLException e){
            System.err.println("Database error");
            e.printStackTrace();
        } finally {
            try{
                conn.close();
            } catch (SQLException e){
                System.err.println("Database error");
                e.printStackTrace();
            }
        }
        return result;
    }

    public boolean wypozyczKsiazke(int id){
        try{
            String query = "UPDATE ksiazki SET czy_wypozyczona=1 WHERE id=?";
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e){
            System.err.println("Database error");
            e.printStackTrace();
            return false;
        } finally {
            try{
                conn.close();
            } catch (SQLException e){
                System.err.println("Database error");
                e.printStackTrace();
            }
        }
        return true;
    }

    public boolean zwrocKsiazke(int id){
        try{
            String query = "UPDATE ksiazki SET czy_wypozyczona=0, liczba_wypozyczen=liczba_wypozyczen+1 WHERE id=?";
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e){
            System.err.println("Database error");
            e.printStackTrace();
            return false;
        } finally {
            try{
                conn.close();
            } catch (SQLException e){
                System.err.println("Database error");
                e.printStackTrace();
            }
        }
        return true;
    }

    public int zwrocIleObecnieWypozyczonych(){
        int result = -1;
        try{
            String query = "SELECT SUM(czy_wypozyczona) FROM ksiazki";
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            rs.next();
            result = rs.getInt(1);
        } catch (SQLException e){
            System.err.println("Database error");
            e.printStackTrace();
        } finally {
            try{
                conn.close();
            } catch (SQLException e){
                System.err.println("Database error");
                e.printStackTrace();
            }
        }
        return result;
    }

    public int zwrocIleWszystkichWypozyczen() {
        int result = -1;
        try{
            String query = "SELECT SUM(liczba_wypozyczen) FROM ksiazki";
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            rs.next();
            result = rs.getInt(1);
        } catch (SQLException e){
            System.err.println("Database error");
            e.printStackTrace();
        } finally {
            try{
                conn.close();
            } catch (SQLException e){
                System.err.println("Database error");
                e.printStackTrace();
            }
        }
        return result;
    }

    public boolean edytujKsiazke(Ksiazka k){
        try{
            String query = "UPDATE ksiazki SET imiona_autora=?, nazwisko_autora=?, tytul=?, rok=?, kategorie=? WHERE id=?";
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, k.zwrocImionaAutora());
            ps.setString(2, k.zwrocNazwiskoAutora());
            ps.setString(3, k.zwrocTytul());
            ps.setInt(4, k.zwrocRok());
            ps.setString(5, k.zwrocKategorie());
            ps.setInt(6, k.zwrocId());
            ps.executeUpdate();
        } catch (SQLException e){
            System.err.println("Database error");
            e.printStackTrace();
            return false;
        } finally {
            try{
                conn.close();
            } catch (SQLException e){
                System.err.println("Database error");
                e.printStackTrace();
            }
        }
        return true;
    }
}
