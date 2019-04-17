package projektbiblioteka;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

// Komentarze przed metodami oznaczają miejsce, w którym zostały wykorzystane w menu (plik Menu.java)

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

    // ---------------------------------------------- 1 ----------------------------------------------

    public void wyswietlListeKsiazek() {
        System.out.format("%-5s %-15s %-17s %-40s %-12s %-40s %-12s %-4s \n",
                "ID", "imię autora", "nazwisko autora", "tytuł", "rok wydania", "kategorie", "wypożyczona", "liczba wypożyczeń");
        for (Ksiazka k : this.ksiazki) {
            this.wyswietlKsiazke(k.zwrocId());
        }
        System.out.println();
    }

    // ---------------------------------------------- 2 ----------------------------------------------

    public void wyswietlSkroconaListeKsiazek() {
        System.out.format("%-5s %-40s %-35s %-3s \n",
                "ID", "tytuł", "autor", "wypożyczona");
        for (Ksiazka k : this.ksiazki) {
            this.wyswietlSkroconaKsiazke(k.zwrocId());
        }
        System.out.println();
    }

    // ---------------------------------------------- 3 ----------------------------------------------

    public void dodajNowaKsiazke() {
        this.zapisDoPliku(this.dodajKsiazke());
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

    // ---------------------------------------------- 4 ----------------------------------------------

    //todo refactor - zapis do pliku
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

    // ---------------------------------------------- 5 ----------------------------------------------

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

    // ---------------------------------------------- 6 ----------------------------------------------

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

    // ---------------------------------------------- 7 ----------------------------------------------
    // 7.1

    public void szukajNazwisko(String szukane) {
        boolean znaleziono = false;
        for (Ksiazka k : ksiazki) {
            if (k.zwrocNazwiskoAutora().toLowerCase().contains(szukane.toLowerCase())) {
                wyswietlKsiazke(k.zwrocId());
                znaleziono = true;
            }
        }
        if(!znaleziono){
            System.out.println("Nie udało się znaleźć książki pasującej do podanego autora.");
        }
    }

    // 7.2

    public void szukajTytul(String szukane) {
        boolean znaleziono = false;
        for (Ksiazka k : ksiazki) {
            if (k.zwrocTytul().toLowerCase().contains(szukane.toLowerCase())) {
                wyswietlKsiazke(k.zwrocId());
                znaleziono = true;
            }
        }
        if(!znaleziono){
            System.out.println("Nie udało się znaleźć książki pasującej do podanego tytułu.");
        }
    }

    // 7.3

    public void szukajKategorie(String szukane) {
        String maleSzukane = szukane.toLowerCase();
        String kategorie;
        boolean znaleziono = false;
        for (Ksiazka k : ksiazki) {
            kategorie = k.zwrocKategorie();
            String[] tabKategorie = kategorie.split(";");
            for (String kategoria : tabKategorie) {
                if (kategoria.equals(maleSzukane)) {
                    this.wyswietlKsiazke(k.zwrocId());
                    znaleziono = true;
                    break;
                }
            }
        }
        if(!znaleziono){
            System.out.println("Nie udało się znaleźć żadnej książki w podanej kategorii");
        }
    }

    // ---------------------------------------------- 8 ----------------------------------------------
    // 8.1

    public int zwrocIleKsiazek() {
        return ileKsiazek;
    }

    public int zwrocIleObecnieWypozyczonych() {
        return this.ileObecnieWypozyczonych;
    }

    public int zwrocIleWszystkichWypozyczen() {
        return this.ileWszystkichWypozyczen;
    }

    // 8.2

    public void wyswietl5NajczesciejWypozyczanych() {
        Ksiazka[] tablicaKsiazek = this.sortujWgLiczbyWypozyczen();
        int wypozyczeniaNajKsiazki = tablicaKsiazek[0].zwrocLiczbeWypozyczen();
        if(wypozyczeniaNajKsiazki != 0){
            boolean piecPierwszychRowne = true;
            for(int i = 0; i < 5; i++){
                if(wypozyczeniaNajKsiazki != tablicaKsiazek[i].zwrocLiczbeWypozyczen()){
                    piecPierwszychRowne = false;
                }
            }
            if(piecPierwszychRowne){
                int liczbaWyswietlanychKsiazek = 0;
                System.out.println("Więcej niż 5 książek ma taką samą liczbę wypożyczeń:");
                while(wypozyczeniaNajKsiazki == tablicaKsiazek[liczbaWyswietlanychKsiazek].zwrocLiczbeWypozyczen()){
                    this.wyswietlKsiazke(tablicaKsiazek[liczbaWyswietlanychKsiazek].zwrocId());
                    liczbaWyswietlanychKsiazek++;
                }
            } else {
                for(int i = 0; i < 5; i++){
                    if(tablicaKsiazek[i].zwrocLiczbeWypozyczen() != 0){
                        this.wyswietlKsiazke(tablicaKsiazek[i].zwrocId());
                    }
                }
            }
        } else {
            System.out.println("Żadna książka nie została jeszcze wypożyczona.");
        }
     }

    // 8.3

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
            if (listaKsiazek.size() == 0){
                cnt = 0;
                System.out.println("Nie wypożyczono jeszcze żadnej książki z tej kategorii.");
            }
            else if (listaKsiazek.size() < 5) cnt = listaKsiazek.size();
            else cnt = 5;
            for (int i = 0; i < cnt; i++) {
                this.wyswietlKsiazke(listaKsiazek.get(i).zwrocId());
            }
        }
    }

    // 8.4

    //todo refactor - max 5 wynikow > 0
    public void wyswietl5NajpopularniejszychAutorow(){
        Map<String, Integer> autorzy = new TreeMap<String, Integer>();
        String imionaAutora;
        String nazwiskoAutora;
        String klucz;
        for(int i = 0; i < this.ksiazki.size(); i++){
            imionaAutora = this.ksiazki.get(i).zwrocImionaAutora();
            nazwiskoAutora = this.ksiazki.get(i).zwrocNazwiskoAutora();
            klucz = imionaAutora + " " + nazwiskoAutora;
            if(autorzy.containsKey(klucz)){
                autorzy.put(klucz, autorzy.get(klucz) + this.ksiazki.get(i).zwrocLiczbeWypozyczen());
            } else {
                autorzy.put(klucz, this.ksiazki.get(i).zwrocLiczbeWypozyczen());
            }
        }
        Map<String, Integer> posortowane = autorzy.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

        for(Map.Entry<String, Integer> entry : posortowane.entrySet()){
            System.out.println("autor = " + entry.getKey() + ", liczba wypożyczeń: " + entry.getValue());
        }
    }

    // ---------------------------------------------- 9 ----------------------------------------------

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

    // ---------------------------------------------- 0 ----------------------------------------------
    // 0.1

    public Ksiazka[] sortujWgNaziwskaAutora() {
        Ksiazka[] sortowanieNazwiska = this.ksiazki.toArray(new Ksiazka[ksiazki.size()]);
        Ksiazka tmp;
        for (int i = 0; i < sortowanieNazwiska.length - 1; i++) {
            for (int j = 0; j < sortowanieNazwiska.length - 1; j++) {
                if (sortowanieNazwiska[j].zwrocNazwiskoAutora().compareTo(sortowanieNazwiska[j + 1].zwrocNazwiskoAutora()) > 0) {
                    tmp = sortowanieNazwiska[j];
                    sortowanieNazwiska[j] = sortowanieNazwiska[j + 1];
                    sortowanieNazwiska[j + 1] = tmp;
                }
            }
        }
        return sortowanieNazwiska;
    }

    // 0.2

    public Ksiazka[] sortujWgRokuWydania() {
        Ksiazka[] sortowanieRokWydania = this.ksiazki.toArray(new Ksiazka[ksiazki.size()]);
        Ksiazka tmp;
        for (int i = 0; i < sortowanieRokWydania.length - 1; i++) {
            for (int j = 0; j < sortowanieRokWydania.length - 1; j++) {
                if (sortowanieRokWydania[j].zwrocRok() < sortowanieRokWydania[j + 1].zwrocRok()) {
                    tmp = sortowanieRokWydania[j];
                    sortowanieRokWydania[j] = sortowanieRokWydania[j + 1];
                    sortowanieRokWydania[j + 1] = tmp;
                }
            }
        }
        return sortowanieRokWydania;
    }

    // 0.3

    public Ksiazka[] sortujWgLiczbyWypozyczen() {
        Ksiazka[] sortowanieLiczbaWypozyczen = this.ksiazki.toArray(new Ksiazka[ksiazki.size()]);
        Ksiazka tmp;
        for (int i = 0; i < sortowanieLiczbaWypozyczen.length - 1; i++) {
            for (int j = 0; j < sortowanieLiczbaWypozyczen.length - 1; j++) {
                if (sortowanieLiczbaWypozyczen[j].zwrocLiczbeWypozyczen() < sortowanieLiczbaWypozyczen[j + 1].zwrocLiczbeWypozyczen()) {
                    tmp = sortowanieLiczbaWypozyczen[j];
                    sortowanieLiczbaWypozyczen[j] = sortowanieLiczbaWypozyczen[j + 1];
                    sortowanieLiczbaWypozyczen[j + 1] = tmp;
                }
            }
        }
        return sortowanieLiczbaWypozyczen;
    }

    // 0.4

    public Ksiazka[] sortujWgTytulu() {
        Ksiazka[] sortowanieTytul = this.ksiazki.toArray(new Ksiazka[ksiazki.size()]);
        Ksiazka tmp;
        for (int i = 0; i < sortowanieTytul.length - 1; i++) {
            for (int j = 0; j < sortowanieTytul.length - 1; j++) {
                if (sortowanieTytul[j].zwrocTytul().compareTo(sortowanieTytul[j + 1].zwrocTytul()) > 0) {
                    tmp = sortowanieTytul[j];
                    sortowanieTytul[j] = sortowanieTytul[j + 1];
                    sortowanieTytul[j + 1] = tmp;
                }
            }
        }
        return sortowanieTytul;
    }

    // ---------------------------------------------- METODY POMOCNICZE ----------------------------------------------

    public void wyswietlTabKsiazek(Ksiazka[] tablicaKsiazek){
        for(Ksiazka k : tablicaKsiazek){
            this.wyswietlKsiazke(k.zwrocId());
        }
    }

    // WYŚWIETLANIE KSIĄŻEK W KONSOLI
    private void wyswietlKsiazke(int id) {
        Ksiazka k = ksiazki.get(id);
        System.out.format("%-5d %-15s %-17s %-40s %-12d %-40s %-12s %-4d \n",
                k.zwrocId(), k.zwrocImionaAutora(), k.zwrocNazwiskoAutora(), k.zwrocTytul(),
                k.zwrocRok(), k.zwrocKategorie(), (k.zwrocCzyWypozyczona()) ? "tak" : "nie", k.zwrocLiczbeWypozyczen());
    }

    private void wyswietlSkroconaKsiazke(int id) {
        Ksiazka k = ksiazki.get(id);
        System.out.format("%-5d %-40s %-35s %-3s\n",
                k.zwrocId(), k.zwrocTytul(), (k.zwrocInicjałyImionAutora() + k.zwrocNazwiskoAutora()), (k.zwrocCzyWypozyczona()) ? "tak" : "nie");
    }

    // OPERACJE NA PLIKU Z DANYMI
    //todo refactor - zrobić coś z zapisywaniem danych, aby nadpisanie zajmowało taką samą liczbę bajtów (np. tablica charów)
    private void zamianaTekstu(int idKsiazki, String coZamienic, String naCoZamienic) {
        try {
            raf = new RandomAccessFile(this.sciezkaDoPlikuZDanymi, "rw");
            raf.seek(0);
            for (int i = 0; i < idKsiazki; i++) {
                raf.readLine();
            }
            long pozycjaLinii = raf.getFilePointer();
            String line = raf.readLine();
            line = line.replace("; " + coZamienic, "; " + naCoZamienic);
            raf.seek(pozycjaLinii);
            raf.writeBytes(line);
            raf.close();
        } catch (IOException e) {
            System.out.println("Nie udało się zapisać do pliku: " + this.sciezkaDoPlikuZDanymi);
            e.printStackTrace();
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
        if (data.length > 4) {
            czyWypozyczona = data[4].equals("tak");
        } else {
            czyWypozyczona = false;
        }
        int liczbaWypozyczen;
        if (data.length > 5) {
            liczbaWypozyczen = Integer.parseInt(data[5]);
        } else {
            liczbaWypozyczen = 0;
        }
        if (czyWypozyczona) this.ileObecnieWypozyczonych++;
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
}