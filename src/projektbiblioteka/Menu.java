package projektbiblioteka;

import java.util.Scanner;

public class Menu {

    private Scanner sc = new Scanner(System.in);
    Biblioteka biblioteka;

    public Menu(Biblioteka biblioteka) {
        this.biblioteka = biblioteka;
    }

    public void menu() {

        int wybor;
        String szukane;

        while (true) {

            System.out.println("Co chcesz zrobić?");
            System.out.print("1 - wyświetl listę książek\n2 - wyświetl krótką listę książek\n3 - dodaj książkę\n4 - edytuj książkę(OSTROŻNIE)" +
                    "\n5 - wypożycz książkę\n6 - zwróć książkę\n7 - wyszukaj książkę\n8 - inne\n9 - import danych z pliku tekstowego" +
                    "\n0 - posortowana lista książek\nWpisz -1 aby zakończyć program\n");

            wybor = sc.nextInt();
            if (wybor == -1) break;
            else if (wybor < -1 || wybor > 9) System.out.println("Podano niepoprawną opcję, wybierz jeszcze raz.");

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
                    wybor = sc.nextInt();
                    sc.nextLine();

                    switch (wybor) {
                        case 1:
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
                    wybor = sc.nextInt();
                    sc.nextLine();

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

                            break;
                    }
                    break;
                case 9:

                    break;
                case 0:

                    break;
            }
        }
    }
}