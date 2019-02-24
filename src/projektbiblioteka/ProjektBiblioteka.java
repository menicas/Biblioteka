package projektbiblioteka;

public class ProjektBiblioteka {

    public static void main(String[] args) {

        Biblioteka biblioteka = new Biblioteka();

        biblioteka.wypozyczKsiazke(1);
        biblioteka.zwrocKsiazke(1);
        biblioteka.wypozyczKsiazke(1);
        biblioteka.zwrocKsiazke(1);
        biblioteka.wypozyczKsiazke(1);
        biblioteka.zwrocKsiazke(1);
        biblioteka.wypozyczKsiazke(1);
        biblioteka.zwrocKsiazke(1);
        biblioteka.wypozyczKsiazke(1);
        biblioteka.zwrocKsiazke(1);

        biblioteka.wypozyczKsiazke(2);
        biblioteka.zwrocKsiazke(2);
        biblioteka.wypozyczKsiazke(2);
        biblioteka.zwrocKsiazke(2);
        biblioteka.wypozyczKsiazke(2);
        biblioteka.zwrocKsiazke(2);
        biblioteka.wypozyczKsiazke(2);
        biblioteka.zwrocKsiazke(2);

        biblioteka.wypozyczKsiazke(3);
        biblioteka.zwrocKsiazke(3);
        biblioteka.wypozyczKsiazke(3);
        biblioteka.zwrocKsiazke(3);

        biblioteka.wypozyczKsiazke(4);
        biblioteka.zwrocKsiazke(4);
        biblioteka.wypozyczKsiazke(4);
        biblioteka.zwrocKsiazke(4);
        biblioteka.wypozyczKsiazke(4);
        biblioteka.zwrocKsiazke(4);
        biblioteka.wypozyczKsiazke(4);
        biblioteka.zwrocKsiazke(4);
        biblioteka.wypozyczKsiazke(4);
        biblioteka.zwrocKsiazke(4);

        biblioteka.wypozyczKsiazke(5);
        biblioteka.zwrocKsiazke(5);

        biblioteka.wypozyczKsiazke(0);
        biblioteka.zwrocKsiazke(0);



        System.out.println(biblioteka.zwrocIleKsiazek() + "\n\n\n");

        Menu menu = new Menu(biblioteka);
        menu.menu();

    /*
        Biblioteka biblioteka = new Biblioteka();
        //biblioteka.dodajNowaKsiazke();
        biblioteka.wyswietlListeKsiazek();

        biblioteka.wypozyczKsiazke(0);
        biblioteka.wypozyczKsiazke(1);
        biblioteka.zwrocKsiazke(1);
        biblioteka.wypozyczKsiazke(0);
        biblioteka.zwrocKsiazke(1);
        biblioteka.wyswietlSkroconaListeKsiazek();
        biblioteka.wyswietlListeKsiazek();
        */
    }
}