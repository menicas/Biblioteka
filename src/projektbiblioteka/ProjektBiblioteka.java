package projektbiblioteka;

public class ProjektBiblioteka {

    public static void main(String[] args) {

        Biblioteka biblioteka = new Biblioteka();
        Seed s = new Seed(biblioteka);
        s.seed();

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