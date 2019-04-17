package projektbiblioteka;

import java.util.Scanner;

/*
    Schemat menu:

    1 - wyświetl listę książek
    2 - wyświetl krótką listę książek
    3 - dodaj książkę
    4 - edytuj książkę(OSTROŻNIE)
    5 - wypożycz książkę
    6 - zwróć książkę
    7 - wyszukaj książkę
        1 - nazwiska autora
        2 - tytułu
        3 - kategorii tematycznej
    8 - inne
        1 - statystyki wypożyczeń
        2 - 5 najczęściej wypożyczanych
        3 - 5 najbardziej poczytnych z każdej kategorii
        4 - 5 najbardziej poczytnych autorów
    9 - import danych z pliku tekstowego
    0 - posortowana lista książek
        1 - według nazwiska autora
        2 - według roku wydania
        3 - według liczby wypozyczeń
        4 - według tytułu
*/

public class Menu {

    private Scanner sc = new Scanner(System.in);
    Biblioteka biblioteka;

    public Menu(Biblioteka biblioteka) {
        this.biblioteka = biblioteka;
    }

    public void wyswietlMenu() {

        int wybor;

        while (true) {

            System.out.println("Co chcesz zrobić?");
            System.out.print("1 - wyświetl listę książek\n2 - wyświetl krótką listę książek\n3 - dodaj książkę\n4 - edytuj książkę(OSTROŻNIE)" +
                    "\n5 - wypożycz książkę\n6 - zwróć książkę\n7 - wyszukaj książkę\n8 - inne\n9 - import danych z pliku tekstowego" +
                    "\n0 - posortowana lista książek\nWpisz -1 aby zakończyć program\n");

            wybor = Walidacja.sprawdzInt(-1, 9);

            if (wybor == -1) break;

            switch (wybor) {
                case 1:
                    biblioteka.wyswietlListeKsiazek();
                    break;
                case 2:
                    biblioteka.wyswietlSkroconaListeKsiazek();
                    break;
                case 3:
                    biblioteka.dodajNowaKsiazke();
                    break;
                case 4:
                    System.out.println("Podaj ID książki do edycji:");
                    biblioteka.edytujKsiazke(sc.nextInt());
                    break;
                case 5:
                    System.out.println("Podaj ID książki do wypożyczenia:");
                    biblioteka.wypozyczKsiazke(sc.nextInt());
                    break;
                case 6:
                    System.out.println("Podaj ID książki do zwrotu:");
                    biblioteka.zwrocKsiazke(sc.nextInt());
                    break;
                case 7:
                    System.out.println("Wyszukiwanie według:\n1 - nazwiska autora\n2 - tytułu\n3 - kategorii tematycznej\n4 - powrót");
                    wybor = Walidacja.sprawdzInt(1, 4);

                    switch (wybor) {
                        case 1:
                            String szukane;
                            System.out.println("Wpisz szukane nazwisko:");
                            szukane = sc.nextLine();
                            biblioteka.szukajNazwisko(szukane);
                            break;
                        case 2:
                            System.out.println("Wpisz szukany tytuł:");
                            szukane = sc.nextLine();
                            biblioteka.szukajTytul(szukane);
                            break;
                        case 3:
                            System.out.println("Wpisz szukaną kategorię:");
                            szukane = sc.nextLine();
                            biblioteka.szukajKategorie(szukane);
                            break;
                    }
                    break;
                case 8:
                    System.out.println("Inne opcje:\n1 - statystyki wypożyczeń\n2 - 5 najczęściej wypożyczanych\n3 - 5 najbardziej poczytnych z każdej kategorii" +
                            "\n4 - 5 najbardziej poczytnych autorów\n5 - powrót");
                    wybor = Walidacja.sprawdzInt(1, 5);

                    switch (wybor) {
                        case 1:
                            System.out.println("Liczba wszystkich książek w bibliotece: " + biblioteka.zwrocIleKsiazek());
                            System.out.println("Liczba obecnie wypożyczonych książek: " + biblioteka.zwrocIleObecnieWypozyczonych());
                            System.out.println("Liczba wszystkich wypożyczeń: " + biblioteka.zwrocIleWszystkichWypozyczen());
                            break;
                        case 2:
                            biblioteka.wyswietl5NajczesciejWypozyczanych();
                            break;
                        case 3:
                            System.out.println();
                            biblioteka.wyswietl5NajpopularniejszychWKategorii();
                            break;
                        case 4:
                            biblioteka.wyswietl5NajpopularniejszychAutorow();
                            break;
                    }
                    break;
                case 9:
                    System.out.println("Podaj ścieżkę do pliku");
                    String sciezkaDoPliku = Walidacja.wprowadzString();
                    biblioteka.importZPliku(sciezkaDoPliku);
                    break;
                case 0:
                    System.out.println("Sortowanie:\n1 - według nazwiska autora\n2 - według roku wydania\n" +
                            "3 - według liczby wypozyczeń\n4 - według tytułu\n5 - powrót");
                    wybor = Walidacja.sprawdzInt(1, 5);
                    switch (wybor) {
                        case 1:
                            biblioteka.wyswietlTabKsiazek(biblioteka.sortujWgNaziwskaAutora());
                            break;
                        case 2:
                            biblioteka.wyswietlTabKsiazek(biblioteka.sortujWgRokuWydania());
                            break;
                        case 3:
                            biblioteka.wyswietlTabKsiazek(biblioteka.sortujWgLiczbyWypozyczen());
                            break;
                        case 4:
                            biblioteka.wyswietlTabKsiazek(biblioteka.sortujWgTytulu());
                            break;
                    }
                    break;
            }
        }
    }
}