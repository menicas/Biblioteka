package projektbiblioteka;

public class ProjektBiblioteka {

    public static void main(String[] args) {

        Biblioteka biblioteka = new Biblioteka();
        Seed s = new Seed(biblioteka);
        s.seed();


        Menu menu = new Menu(biblioteka);
        menu.wyswietlMenu();
    }
}