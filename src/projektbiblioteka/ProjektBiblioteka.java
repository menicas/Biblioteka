package projektbiblioteka;

public class ProjektBiblioteka {

    public static void main(String[] args) {
        System.out.println(Walidacja.isInteger("e"));
        Biblioteka biblioteka = new Biblioteka();
        Menu menu = new Menu(biblioteka);
        menu.wyswietlMenu();
    }
}