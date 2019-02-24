package projektbiblioteka;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Biblioteka {

    private final ArrayList<Ksiazka> ksiazki = new ArrayList<>();
    private final ArrayList<String> istniejaceKategorie = new ArrayList<>();
    private int ileKsiazek;
    private int ileObecnieWypozyczonych = 0;
    private int ileWszystkichWypozyczen = 0;
    private String sciezkaDoPlikuZDanymi = "/home/pawel/Projects/Java/ProjektBiblioteka/biblioteka.dat";
    private FileWriter file;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private Scanner sc = new Scanner(System.in);
    private Scanner reader;

    public Biblioteka() {
        try{
            reader = new Scanner(new File(sciezkaDoPlikuZDanymi));
        } catch (IOException e){
            System.out.println("Nie udało się otworzyć pliku biblioteka.dat");
            e.printStackTrace();
        }

        while(reader.hasNext()){
            odczytajZPliku(reader);
        }
        ileKsiazek = ksiazki.size();
        reader.close();

    }
    
    private Ksiazka dodajKsiazke(){
        System.out.println("Podaj tytuł:");
        String tytul = sc.nextLine();
        System.out.println("Podaj imiona autora");
        String imionaAutora = sc.nextLine();
        System.out.println("Podaj nazwisko autora:");
        String nazwiskoAutora = sc.nextLine();
        System.out.println("Podaj kategorie");
        String kategorie = sc.nextLine();
        System.out.println("Podaj rok wydania");
        int rok = sc.nextInt();
        sc.nextLine();
        Ksiazka dodanaKsiazka = new Ksiazka(tytul, imionaAutora, nazwiskoAutora, rok, kategorie);
        ksiazki.add(dodanaKsiazka);
        ileKsiazek = ksiazki.size();
        return dodanaKsiazka;
    }

    public void dodajNowaKsiazke(){
        this.zapisDoPliku(this.dodajKsiazke());
    }
    
    public void edytujKsiazke(int id){ //zmienic wyswietlanie na pelna a nie skrocona
        Ksiazka k = ksiazki.get(id);
        System.out.println("Wybrałeś następującą książkę do edycji:");
        wyswietlKsiazke(id);
        System.out.println("Co chcesz edytować?\n0 - Imiona autora\n1 - Nazwisko autora\n2 - Tytuł\n3 - Rok wydania\n"
                + "4 - Kategorie");
        int wybor = sc.nextInt();
        sc.nextLine();
        System.out.println("Wprowadź nową wartość:");
        String nowaWartosc;
        int rok;
        switch(wybor){
            case 0:
                nowaWartosc = sc.nextLine();
                k.ustawImionaAutora(nowaWartosc);
                break;
            case 1:
                nowaWartosc = sc.nextLine();
                k.ustawNazwiskoAutora(nowaWartosc);
                break;
            case 2:
                nowaWartosc = sc.nextLine();
                k.ustawTytul(nowaWartosc);
                break;
            case 3:
                rok = sc.nextInt();
                k.ustawRok(rok);
                break;
            case 4:
                nowaWartosc = sc.nextLine();
                k.ustawKategorie(nowaWartosc);
                break;
            default:
        }

    }
    
    private void wyswietlKsiazke(int id){
        Ksiazka k = ksiazki.get(id);
        System.out.format("%-5d %-15s %-17s %-25s %-12d %-30s %-12s %-4d \n", 
                    k.zwrocId(), k.zwrocImionaAutora(), k.zwrocNazwiskoAutora(), k.zwrocTytul(), 
                    k.zwrocRok(), k.zwrocKategorie(), (k.czyWypozyczona()) ? "tak" : "nie", k.zwrocLiczbeWypozyczen());
    }

    private void wyswietlSkroconaKsiazke(int id){
        Ksiazka k = ksiazki.get(id);
        System.out.format("%-5d %-30s %-35s %-3s \n",
                k.zwrocId(), k.zwrocTytul(), (k.zwrocInicjałyImionAutora() + k.zwrocNazwiskoAutora()), (k.czyWypozyczona()) ? "tak" : "nie");
    }
    
    public void wyswietlSkroconaListeKsiazek(){
        System.out.format("%-5s %-30s %-35s %-3s \n",
                "ID", "tytuł", "autor", "wypożyczona");
        for(Ksiazka k : ksiazki){
            this.wyswietlSkroconaKsiazke(k.zwrocId());
        }
        System.out.println();
    }
    
    public void wyswietlListeKsiazek(){
        System.out.format("%-5s %-15s %-17s %-25s %-12s %-30s %-12s %-4s \n",
                "ID", "imię autora", "nazwisko autora", "tytuł", "rok wydania", "kategorie", "wypożyczona", "liczba wypożyczeń");
        for(Ksiazka k : ksiazki){
            this.wyswietlKsiazke(k.zwrocId());
        }
        System.out.println();
    }

    public void wyswietl5NajczesciejWypozyczanych(){
        Ksiazka[] najczesciejWypozyczane = new Ksiazka[5];
        for(int i = 0; i < najczesciejWypozyczane.length; i++){
            najczesciejWypozyczane[i] = ksiazki.get(0);
        }
        for(Ksiazka k : ksiazki){
            Ksiazka temp = k;
            for (int i = 0; i < najczesciejWypozyczane.length; i++){
                Ksiazka  temp1;
                if(temp.zwrocLiczbeWypozyczen() > najczesciejWypozyczane[i].zwrocLiczbeWypozyczen()){
                    temp1 = najczesciejWypozyczane[i];
                    najczesciejWypozyczane[i] = temp;
                    temp = temp1;
                }
            }
        }
        for(Ksiazka k : najczesciejWypozyczane){
            this.wyswietlSkroconaKsiazke(k.zwrocId());
        }
    }

    public void wyswietl5NajpopularniejszychKategorie(){

    }

    public void wypozyczKsiazke(int id){
        Ksiazka k = ksiazki.get(id);
        String daneKsiazki = k.zwrocInicjałyImionAutora() + k.zwrocNazwiskoAutora() + ", " + k.zwrocTytul() + ", " + k.zwrocRok();
        if(k.czyWypozyczona()){
            System.out.println("Nie można wypożyczyć książki - jest już wypożyczona (" + daneKsiazki + ")");
        } else{
            k.wypozycz();
            System.out.println("Wypożyczono książkę (" + daneKsiazki + ")");
            this.ileObecnieWypozyczonych++;
        }
    }

    public void zwrocKsiazke(int id){
        Ksiazka k = ksiazki.get(id);
        String daneKsiazki = k.zwrocInicjałyImionAutora() + k.zwrocNazwiskoAutora() + ", " + k.zwrocTytul() + ", " + k.zwrocRok();
        if(k.czyWypozyczona()){
            System.out.println("Zwrócono książkę (" + daneKsiazki + ")");
            this.ileObecnieWypozyczonych--;
            this.ileWszystkichWypozyczen++;
            k.zwroc();
        } else {
            System.out.println("Książka nie jest wypożyczona - nie można zwrócić (" + daneKsiazki + ")");
        }
    }
    
    public int zwrocIleKsiazek(){
        return ileKsiazek;
    }

    public int zwrocIleObecnieWypozyczonych(){ return this.ileObecnieWypozyczonych;}

    public int zwrocIleWszystkichWypozyczen(){ return this.ileWszystkichWypozyczen; }

    public void szukajNazwisko(String szukane){
        for(Ksiazka k : ksiazki){
            if(k.zwrocNazwiskoAutora().toLowerCase().contains(szukane.toLowerCase())){
                wyswietlKsiazke(k.zwrocId());
            }
        }
    }

    public void szukajTytul(String szukane){
        for(Ksiazka k : ksiazki){
            if(k.zwrocTytul().toLowerCase().contains(szukane.toLowerCase())){
                wyswietlKsiazke(k.zwrocId());
            }
        }
    }

    public void szukajKategorie(String szukane){
        String maleSzukane = szukane.toLowerCase();
        String kategorie;
        for(Ksiazka k : ksiazki){
            kategorie = k.zwrocKategorie();
            String[] tabKategorie = kategorie.split(";");
            for(String kategoria : tabKategorie){
                if(kategoria.equals(maleSzukane)){
                    this.wyswietlKsiazke(k.zwrocId());
                    break;
                }

            }
        }
    }

    private void zapisDoPliku(Ksiazka k){
        String[] tabKategori = k.zwrocKategorie().split(";");
        for(String kategoria : tabKategori){
            if(!istniejaceKategorie.contains(kategoria)){
                istniejaceKategorie.add(kategoria);
            }
        }
        String dane = String.format("%s, %s; %s; %d; ", k.zwrocImionaAutora(), k.zwrocNazwiskoAutora(), k.zwrocTytul(), k.zwrocRok());
        for(String s : tabKategori){
            dane += s + ", ";
        }
        try{
            file = new FileWriter(this.sciezkaDoPlikuZDanymi, true);
            bufferedWriter = new BufferedWriter(file);
            bufferedWriter.write(dane.substring(0, dane.length() - 2));
            bufferedWriter.newLine();
            bufferedWriter.close();
        } catch (IOException e) {
            System.out.println("Could not write to file - biblioteka.dat");
            e.printStackTrace();
        }        reader.close();
    }

    private void odczytajZPliku(Scanner read){
        String[] data = read.nextLine().split("; ");
        String imiona = data[0].split(", ")[0];
        String nazwisko = data[0].split(", ")[1];
        String tytul = data[1];
        int rok = Integer.parseInt(data[2]);
        String kategorie = data[3].replaceAll(", ", ";");
        ksiazki.add(new Ksiazka(tytul, imiona, nazwisko, rok, kategorie));
        //dodaj kategorie do tablicy istniejaceKategorie
        String[] tabKategori = kategorie.split(";");
        for(String k : tabKategori){
            System.out.println(k);
        }
        for(String kategoria : tabKategori){
            if(!istniejaceKategorie.contains(kategoria)){
                istniejaceKategorie.add(kategoria);
            }
        }
    }
}