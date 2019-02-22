package projektbiblioteka;

public class ProjektBiblioteka {

    public static void main(String[] args) {

        Menu menu = new Menu(new Biblioteka());
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