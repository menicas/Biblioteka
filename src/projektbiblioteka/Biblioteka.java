package projektbiblioteka;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class Biblioteka {

    private final ArrayList<Ksiazka> ksiazki = new ArrayList<>();
    private final ArrayList<String> istniejaceKategorie = new ArrayList<>();
    private int ileKsiazek;
    private int ileObecnieWypozyczonych = 0;
    private int ileWszystkichWypozyczen = 0;
    private String sciezkaDoPlikuZDanymi = "/home/pawel/Projects/Java/ProjektBiblioteka/biblioteka.dat";
    private FileWriter file = null;
    private BufferedWriter bufferedWriter = null;
    private Scanner sc = new Scanner(System.in);
    private Scanner reader = null;
    private RandomAccessFile raf = null;

    public Biblioteka() {
        try {
            reader = new Scanner(new File(sciezkaDoPlikuZDanymi));
        } catch (IOException e) {
            System.out.println("Nie udało się otworzyć pliku biblioteka.dat");
            e.printStackTrace();
        }

        while (reader.hasNext()) {
            odczytajZPliku(reader);
        }
        ileKsiazek = ksiazki.size();
        reader.close();
    }

    private Ksiazka dodajKsiazke() {
        System.out.println("Podaj tytuł:");
        String tytul = Walidacja.wprowadzString();
        System.out.println("Podaj imiona autora");
        String imionaAutora = Walidacja.wprowadzString();
        System.out.println("Podaj nazwisko autora:");
        String nazwiskoAutora = Walidacja.wprowadzString();
        System.out.println("Podaj kategorie (oddzielone średnikami (;))");
        String kategorie = Walidacja.wprowadzString();
        System.out.println("Podaj rok wydania (1970-2012)");
        int rok = Walidacja.sprawdzInt(1970, 2012);
        Ksiazka dodanaKsiazka = new Ksiazka(tytul, imionaAutora, nazwiskoAutora, rok, kategorie, false, 0);
        ksiazki.add(dodanaKsiazka);
        ileKsiazek = ksiazki.size();
        return dodanaKsiazka;
    }

    public void dodajNowaKsiazke() {
        this.zapisDoPliku(this.dodajKsiazke());
    }

    public void edytujKsiazke(int id) { //zmienic wyswietlanie na pelna a nie skrocona
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
        switch (wybor) {
            case 0:
                nowaWartosc = Walidacja.wprowadzString();
                k.ustawImionaAutora(nowaWartosc);
                break;
            case 1:
                nowaWartosc = Walidacja.wprowadzString();
                k.ustawNazwiskoAutora(nowaWartosc);
                break;
            case 2:
                nowaWartosc = Walidacja.wprowadzString();
                k.ustawTytul(nowaWartosc);
                break;
            case 3:
                rok = Walidacja.sprawdzInt(1970, 2012);
                k.ustawRok(rok);
                break;
            case 4:
                nowaWartosc = Walidacja.wprowadzString();
                k.ustawKategorie(nowaWartosc);
                break;
            default:
        }

    }

    private void wyswietlKsiazke(int id) {
        Ksiazka k = ksiazki.get(id);
        System.out.format("%-5d %-15s %-17s %-25s %-12d %-30s %-12s %-4d \n",
                k.zwrocId(), k.zwrocImionaAutora(), k.zwrocNazwiskoAutora(), k.zwrocTytul(),
                k.zwrocRok(), k.zwrocKategorie(), (k.zwrocCzyWypozyczona()) ? "tak" : "nie", k.zwrocLiczbeWypozyczen());
    }

    private void wyswietlSkroconaKsiazke(int id) {
        Ksiazka k = ksiazki.get(id);
        System.out.format("%-5d %-30s %-35s %-3s\n",
                k.zwrocId(), k.zwrocTytul(), (k.zwrocInicjałyImionAutora() + k.zwrocNazwiskoAutora()), (k.zwrocCzyWypozyczona()) ? "tak" : "nie");
    }

    public void wyswietlSkroconaListeKsiazek() {
        System.out.format("%-5s %-30s %-35s %-3s \n",
                "ID", "tytuł", "autor", "wypożyczona");
        for (Ksiazka k : ksiazki) {
            this.wyswietlSkroconaKsiazke(k.zwrocId());
        }
        System.out.println();
    }

    public void wyswietlListeKsiazek() {
        System.out.format("%-5s %-15s %-17s %-25s %-12s %-30s %-12s %-4s \n",
                "ID", "imię autora", "nazwisko autora", "tytuł", "rok wydania", "kategorie", "wypożyczona", "liczba wypożyczeń");
        for (Ksiazka k : ksiazki) {
            this.wyswietlKsiazke(k.zwrocId());
        }
        System.out.println();
    }

    //todo NAPRAWIĆ GDY MNIEJ NIZ 5 KSIAZEK MA WIECEJ NIZ 0 WYPOZYCZEN, 15.04 - w ogole przstalo dzialac
    public void wyswietl5NajczesciejWypozyczanych() {
        ArrayList<Ksiazka> najczesciejWypozyczane = new ArrayList<>();
        for (Ksiazka k : this.ksiazki) {
            if (k.zwrocLiczbeWypozyczen() > 0) najczesciejWypozyczane.add(k);
        }
        Collections.sort(najczesciejWypozyczane);
        boolean piecPierwszychRowne = true;
        int liczbaWypozyczenPierwszejKsiazki = najczesciejWypozyczane.get(0).zwrocLiczbeWypozyczen();
        for (int i = 1; i <= 5; i++) {
            if (liczbaWypozyczenPierwszejKsiazki != najczesciejWypozyczane.get(i).zwrocLiczbeWypozyczen()) {
                piecPierwszychRowne = false;
                break;
            }
        }
        int i = 0;
        if (piecPierwszychRowne) {
            while (i < najczesciejWypozyczane.size() && liczbaWypozyczenPierwszejKsiazki == najczesciejWypozyczane.get(i).zwrocLiczbeWypozyczen()) {
                this.wyswietlKsiazke(najczesciejWypozyczane.get(i).zwrocId());
                i++;
            }
        } else {
            for (i = 0; i < 5; i++) {
                this.wyswietlKsiazke(najczesciejWypozyczane.get(i).zwrocId());
            }
        }
    }

    //todo REFACTOR ----------------------------------------------------------------------------------------------

    public void wyswietl5NajpopularniejszychWKategorii() {
        ArrayList<Ksiazka> listaKsiazek;
        int cnt;
        for (String kategoria : this.istniejaceKategorie) {
            listaKsiazek = new ArrayList<>();
            System.out.println("\nKategoria: " + kategoria);
            for (Ksiazka k : this.ksiazki) {
                if (k.zwrocKategorie().contains(kategoria) && k.zwrocLiczbeWypozyczen() > 0) {
                    listaKsiazek.add(ksiazki.get(k.zwrocId()));
                }
            }
            Collections.sort(listaKsiazek);
            if (listaKsiazek.size() < 5) cnt = listaKsiazek.size();
            else cnt = 5;
            for (int i = 0; i < cnt; i++) {
                this.wyswietlKsiazke(listaKsiazek.get(i).zwrocId());
            }
        }
    }

    public void wypozyczKsiazke(int id) {
        Ksiazka k = ksiazki.get(id);
        String daneKsiazki = k.zwrocInicjałyImionAutora() + k.zwrocNazwiskoAutora() + ", " + k.zwrocTytul() + ", " + k.zwrocRok();
        if (k.zwrocCzyWypozyczona()) {
            System.out.println("Nie można wypożyczyć książki - jest już wypożyczona (" + daneKsiazki + ")");
        } else {
            k.wypozycz();
            System.out.println("Wypożyczono książkę (" + daneKsiazki + ")");
            this.zamianaTekstu(id, "nie", "tak");
            this.ileObecnieWypozyczonych++;
        }
    }

    public void zwrocKsiazke(int id) {
        Ksiazka k = ksiazki.get(id);
        String daneKsiazki = k.zwrocInicjałyImionAutora() + k.zwrocNazwiskoAutora() + ", " + k.zwrocTytul() + ", " + k.zwrocRok();
        if (k.zwrocCzyWypozyczona()) {
            System.out.println("Zwrócono książkę (" + daneKsiazki + ")");
            this.ileObecnieWypozyczonych--;
            this.ileWszystkichWypozyczen++;
            k.zwroc();
            this.zamianaTekstu(id, "tak", "nie");
            this.zamianaTekstu(id, String.valueOf(k.zwrocLiczbeWypozyczen() - 1), String.valueOf(k.zwrocLiczbeWypozyczen()));
        } else {
            System.out.println("Książka nie jest wypożyczona - nie można zwrócić (" + daneKsiazki + ")");
        }
    }

    private void zamianaTekstu(int idKsiazki, String coZamienic, String naCoZamienic){
        try{
            raf = new RandomAccessFile(this.sciezkaDoPlikuZDanymi, "rw");
            raf.seek(0);
            for(int i = 0; i < idKsiazki; i++){
                raf.readLine();
            }
            long pozycjaLinii = raf.getFilePointer();
            String line = raf.readLine();
            line = line.replace("; " + coZamienic, "; " + naCoZamienic);
            raf.seek(pozycjaLinii);
            raf.writeBytes(line);
            raf.close();
        } catch (IOException e){
            System.out.println("Nie udało się zapisać do pliku: " + this.sciezkaDoPlikuZDanymi);
            e.printStackTrace();
        }
    }

    public int zwrocIleKsiazek() {
        return ileKsiazek;
    }

    public int zwrocIleObecnieWypozyczonych() {
        return this.ileObecnieWypozyczonych;
    }

    public int zwrocIleWszystkichWypozyczen() {
        return this.ileWszystkichWypozyczen;
    }

    public void szukajNazwisko(String szukane) {
        for (Ksiazka k : ksiazki) {
            if (k.zwrocNazwiskoAutora().toLowerCase().contains(szukane.toLowerCase())) {
                wyswietlKsiazke(k.zwrocId());
            }
        }
    }

    public void szukajTytul(String szukane) {
        for (Ksiazka k : ksiazki) {
            if (k.zwrocTytul().toLowerCase().contains(szukane.toLowerCase())) {
                wyswietlKsiazke(k.zwrocId());
            }
        }
    }

    public void szukajKategorie(String szukane) {
        String maleSzukane = szukane.toLowerCase();
        String kategorie;
        for (Ksiazka k : ksiazki) {
            kategorie = k.zwrocKategorie();
            String[] tabKategorie = kategorie.split(";");
            for (String kategoria : tabKategorie) {
                if (kategoria.equals(maleSzukane)) {
                    this.wyswietlKsiazke(k.zwrocId());
                    break;
                }
            }
        }
    }

    public void sortujWgNaziwskaAutora(){
        Ksiazka[] sortowanieNazwiska = this.ksiazki.toArray(new Ksiazka[ksiazki.size()]);
        Ksiazka tmp;
        for(int i = 0; i < sortowanieNazwiska.length - 1; i++){
            for(int j = 0; j < sortowanieNazwiska.length - 1; j++){
                if(sortowanieNazwiska[j].zwrocNazwiskoAutora().compareTo(sortowanieNazwiska[j + 1].zwrocNazwiskoAutora()) > 0){
                    tmp = sortowanieNazwiska[j];
                    sortowanieNazwiska[j] = sortowanieNazwiska[j + 1];
                    sortowanieNazwiska[j + 1] = tmp;
                }
            }
        }
        for(Ksiazka k : sortowanieNazwiska){
            this.wyswietlKsiazke(k.zwrocId());
        }
    }

    public void sortujWgRokuWydania(){
        Ksiazka[] sortowanieRokWydania = this.ksiazki.toArray(new Ksiazka[ksiazki.size()]);
        Ksiazka tmp;
        for(int i = 0; i < sortowanieRokWydania.length - 1; i++){
            for(int j = 0; j < sortowanieRokWydania.length - 1; j++){
                if(sortowanieRokWydania[j].zwrocRok() < sortowanieRokWydania[j + 1].zwrocRok()){
                    tmp = sortowanieRokWydania[j];
                    sortowanieRokWydania[j] = sortowanieRokWydania[j + 1];
                    sortowanieRokWydania [j + 1] = tmp;
                }
            }
        }
        for(Ksiazka k : sortowanieRokWydania){
            this.wyswietlKsiazke(k.zwrocId());
        }
    }

    public void sortujWgLiczbyWypozyczen(){
        Ksiazka[] sortowanieLiczbaWypozyczen = this.ksiazki.toArray(new Ksiazka[ksiazki.size()]);
        Ksiazka tmp;
        for(int i = 0; i < sortowanieLiczbaWypozyczen.length - 1; i++){
            for(int j = 0; j < sortowanieLiczbaWypozyczen.length - 1; j++){
                if(sortowanieLiczbaWypozyczen[j].zwrocLiczbeWypozyczen() < sortowanieLiczbaWypozyczen[j + 1].zwrocLiczbeWypozyczen()){
                    tmp = sortowanieLiczbaWypozyczen[j];
                    sortowanieLiczbaWypozyczen[j] = sortowanieLiczbaWypozyczen[j + 1];
                    sortowanieLiczbaWypozyczen [j + 1] = tmp;
                }
            }
        }
        for(Ksiazka k : sortowanieLiczbaWypozyczen){
            this.wyswietlKsiazke(k.zwrocId());
        }
    }

    public void sortujWgTytulu(){
        Ksiazka[] sortowanieTytul = this.ksiazki.toArray(new Ksiazka[ksiazki.size()]);
        Ksiazka tmp;
        for(int i = 0; i < sortowanieTytul.length - 1; i++){
            for(int j = 0; j < sortowanieTytul.length - 1; j++){
                if(sortowanieTytul[j].zwrocTytul().compareTo(sortowanieTytul[j + 1].zwrocTytul()) > 0){
                    tmp = sortowanieTytul[j];
                    sortowanieTytul[j] = sortowanieTytul[j + 1];
                    sortowanieTytul[j + 1] = tmp;
                }
            }
        }
        for(Ksiazka k : sortowanieTytul){
            this.wyswietlKsiazke(k.zwrocId());
        }
    }

    private void zapisDoPliku(Ksiazka k) {
        String[] tabKategorii = k.zwrocKategorie().split(";");
        for (String kategoria : tabKategorii) {
            if (!istniejaceKategorie.contains(kategoria)) {
                istniejaceKategorie.add(kategoria);
            }
        }
        String dane = String.format("%s, %s; %s; %d; ", k.zwrocImionaAutora(), k.zwrocNazwiskoAutora(), k.zwrocTytul(), k.zwrocRok());
        for (String s : tabKategorii) {
            dane += s + ", ";
        }
        dane = dane.substring(0, dane.length() - 2);
        dane = String.format(dane + "; %s; %d", (k.zwrocCzyWypozyczona()) ? "tak" : "nie", k.zwrocLiczbeWypozyczen());
        try {
            file = new FileWriter(this.sciezkaDoPlikuZDanymi, true);
            bufferedWriter = new BufferedWriter(file);
            bufferedWriter.write(dane);
            bufferedWriter.newLine();
            bufferedWriter.close();
        } catch (IOException e) {
            System.out.println("Nie udało się zapisać do pliku - biblioteka.dat");
            e.printStackTrace();
        }
        reader.close();
    }

    private Ksiazka odczytajZPliku(Scanner read) {
        String[] data = read.nextLine().split("; ");
        String imiona = data[0].split(", ")[0];
        String nazwisko = data[0].split(", ")[1];
        String tytul = data[1];
        int rok = Integer.parseInt(data[2]);
        String kategorie = data[3].replaceAll(", ", ";");
        boolean czyWypozyczona;
        if(data.length > 4){
            czyWypozyczona = data[4].equals("tak");
        } else {
            czyWypozyczona = false;
        }
        int liczbaWypozyczen;
        if(data.length > 5){
            liczbaWypozyczen = Integer.parseInt(data[5]);
        } else {
            liczbaWypozyczen = 0;
        }
        if(czyWypozyczona) this.ileObecnieWypozyczonych++;
        this.ileWszystkichWypozyczen += liczbaWypozyczen;
        Ksiazka odczytanaKsiazka = new Ksiazka(tytul, imiona, nazwisko, rok, kategorie, czyWypozyczona, liczbaWypozyczen);
        ksiazki.add(odczytanaKsiazka);
        //dodaj kategorie do tablicy istniejaceKategorie
        String[] tabKategorii = kategorie.split(";");
        for (String kategoria : tabKategorii) {
            if (!istniejaceKategorie.contains(kategoria)) {
                istniejaceKategorie.add(kategoria);
            }
        }
        return odczytanaKsiazka;
    }

    // /home/pawel/Projects/Java/ProjektBiblioteka/bib.txt
    public void importZPliku(String sciezkaDoPliku) {
        Scanner importFile = null;
        try {
            importFile = new Scanner(new File(sciezkaDoPliku));
        } catch (FileNotFoundException e) {
            System.out.println("Nie znaleziono pliku: " + sciezkaDoPliku);
            e.printStackTrace();
        }
        while (importFile.hasNext()) {
            zapisDoPliku(odczytajZPliku(importFile));
        }
        importFile.close();
        System.out.println("Pomyślnie dokonano importu z " + sciezkaDoPliku);
    }
}