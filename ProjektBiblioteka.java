package projektbiblioteka;

public class ProjektBiblioteka {

	public static void main(String[] args) {
		Biblioteka biblioteka = new Biblioteka();
		Menu menu = new Menu(biblioteka);
		menu.wyswietlMenu();
	}
}