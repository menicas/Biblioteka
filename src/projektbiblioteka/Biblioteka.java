package projektbiblioteka;

import com.sun.xml.internal.ws.spi.db.RepeatedElementBridge;

import java.io.*;
import java.security.KeyStore;
import java.util.*;

import static java.util.Map.Entry.comparingByValue;
import static java.util.stream.Collectors.toMap;

// Komentarze przed metodami oznaczają miejsce, w którym zostały wykorzystane w menu (plik Menu.java)

public class Biblioteka {

	private final ArrayList<Ksiazka> ksiazki = new ArrayList<>();
	private final ArrayList<String> istniejaceKategorie = new ArrayList<>();
	private int ileKsiazek;
	private int ileObecnieWypozyczonych = 0;
	private int ileWszystkichWypozyczen = 0;
	private String sciezkaDoPlikuZDanymi = "/home/pawel/Projects/Java/ProjektBiblioteka/biblioteka.dat";
	private Scanner sc = new Scanner(System.in);
	private Scanner reader = null;

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
		String tytul = Walidacja.wprowadzTytul();
		System.out.println("Podaj imiona autora");
		String imionaAutora = Walidacja.wprowadzDane();
		System.out.println("Podaj nazwisko autora:");
		String nazwiskoAutora = Walidacja.wprowadzDane();
		System.out.println("Podaj kategorie (oddzielone średnikami (;))");
		String kategorie = Walidacja.wprowadzKategorie();
		System.out.println("Podaj rok wydania (1970-2012)");
		int rok = Walidacja.wprowadzInt(1970, 2012);
		Ksiazka dodanaKsiazka = new Ksiazka(tytul, imionaAutora, nazwiskoAutora, rok, kategorie, false, 0);
		ksiazki.add(dodanaKsiazka);
		ileKsiazek = ksiazki.size();
		return dodanaKsiazka;
	}

	// ---------------------------------------------- 4 ----------------------------------------------

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
				nowaWartosc = Walidacja.wprowadzDane();
				k.ustawImionaAutora(nowaWartosc);
				break;
			case 1:
				nowaWartosc = Walidacja.wprowadzDane();
				k.ustawNazwiskoAutora(nowaWartosc);
				break;
			case 2:
				nowaWartosc = Walidacja.wprowadzTytul();
				k.ustawTytul(nowaWartosc);
				break;
			case 3:
				rok = Walidacja.wprowadzInt(1970, 2012);
				k.ustawRok(rok);
				break;
			case 4:
				nowaWartosc = Walidacja.wprowadzKategorie();
				k.ustawKategorie(nowaWartosc);
				break;
			default:
		}
		this.zapisDoPliku(k);
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
			this.zapisDoPliku(k);
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
			this.zapisDoPliku(k);
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
		if (!znaleziono) {
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
		if (!znaleziono) {
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
		if (!znaleziono) {
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
		if (wypozyczeniaNajKsiazki != 0) {
			boolean piecPierwszychRowne = true;
			for (int i = 0; i < 5; i++) {
				if (wypozyczeniaNajKsiazki != tablicaKsiazek[i].zwrocLiczbeWypozyczen()) {
					piecPierwszychRowne = false;
				}
			}
			if (piecPierwszychRowne) {
				int liczbaWyswietlanychKsiazek = 0;
				System.out.println("Więcej niż 5 książek ma taką samą liczbę wypożyczeń:");
				while (wypozyczeniaNajKsiazki == tablicaKsiazek[liczbaWyswietlanychKsiazek].zwrocLiczbeWypozyczen()) {
					this.wyswietlKsiazke(tablicaKsiazek[liczbaWyswietlanychKsiazek].zwrocId());
					liczbaWyswietlanychKsiazek++;
				}
			} else {
				for (int i = 0; i < 5; i++) {
					if (tablicaKsiazek[i].zwrocLiczbeWypozyczen() != 0) {
						this.wyswietlKsiazke(tablicaKsiazek[i].zwrocId());
					}
				}
			}
		} else {
			System.out.println("Żadna książka nie została jeszcze wypożyczona.");
		}
	}

	// 8.3
	public void wyswietl5NajpopularniejszychWKategorii() {
		// ta część metody odpowiada za pozbycie się powtórzeń książek i zsumowanie ich liczby wypożyczeń
		ArrayList<Ksiazka> listaKsiazek = new ArrayList<>();
		try {
			for (Ksiazka k : this.ksiazki) {
				if(!listaKsiazek.contains(k)) {
					listaKsiazek.add((Ksiazka) k.clone());
				}
			}
		} catch (CloneNotSupportedException e) {
			System.out.println("Klonowanie nie powiodlo sie");
			e.printStackTrace();
		}

		Iterator<Ksiazka> it;

		for(it = listaKsiazek.iterator(); it.hasNext(); ){
			Ksiazka k1 = it.next();
			for(Iterator<Ksiazka> itt = this.ksiazki.iterator(); itt.hasNext(); ){
				Ksiazka k2 = itt.next();
				if (k1.equals(k2) && !k1.toString().equals(k2.toString())){
					k1.ustawLiczbeWypozyczen(k1.zwrocLiczbeWypozyczen() + k2.zwrocLiczbeWypozyczen());
				}
			}
		}


		// algorytm wyświetlania książek dla każdej kategorii
		Collections.sort(listaKsiazek);
		ArrayList<Ksiazka> ksiazkiWKategori;
		for (String kategoria : this.istniejaceKategorie) {
			ksiazkiWKategori = new ArrayList<>();
			System.out.println("\nKategoria: " + kategoria);
			for (Ksiazka k : listaKsiazek) {
				if (k.zwrocKategorie().contains(kategoria) && k.zwrocLiczbeWypozyczen() > 0) {
					ksiazkiWKategori.add(k);
				}
			}
			int cnt;
			if (ksiazkiWKategori.size() == 0) {
				cnt = 0;
				System.out.println("Żadna książka z tej kategorii nie została jeszcze wypożyczona");
			} else if (ksiazkiWKategori.size() < 5) {
				cnt = ksiazkiWKategori.size();
			} else {
				cnt = 5;
			}

			// ponieważ kod operuje na skopiowanych książkach (które mają takie sam id jak oryginały)
			// wyświetlanie zostało zaimplementowane w następujący sposób, a nie przy użyciu metody wyswietlKsiazka(id)
			for (int i = 0; i < cnt; i++) {
				System.out.println(ksiazkiWKategori.get(i).toString());
			}
			System.out.println();
		}
	}

	// 8.4

	public void wyswietl5NajpopularniejszychAutorow() {
		ArrayList<String> autorzy = this.zwrocListeAutorow();
		int[] wypozyczenia = this.zwrocTabliceWypozyczenAutorow(autorzy);

		int tmp;
		String autorTmp;
		//sortowanie obu struktur wg liczby wypozyczen;
		for (int i = 0; i < autorzy.size() - 1; i++) {
			for (int j = 0; j < autorzy.size() - 1; j++) {
				if (wypozyczenia[j] < wypozyczenia[j + 1]) {
					tmp = wypozyczenia[j];
					wypozyczenia[j] = wypozyczenia[j + 1];
					wypozyczenia[j + 1] = tmp;
					autorTmp = autorzy.get(j);
					autorzy.set(j, autorzy.get(j + 1));
					autorzy.set(j + 1, autorTmp);
				}
			}
		}

		int cnt =  autorzy.size() > 5 ? 5 : autorzy.size();
		for (int i = 0; i < cnt; i++) {
			if (wypozyczenia[i] > 0){
				String autor = autorzy.get(i);
				System.out.println(autor + ", liczba wypożyczeń: " + wypozyczenia[i] + ", najpopularniejsza książka - \"" +
						this.zwrocNajpoupularniejszaKsiazkeAutora(autor).zwrocTytul() + "\"");
			}
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
			this.zapisDoPliku(odczytajZPliku(importFile));
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

	public void wyswietlTabKsiazek(Ksiazka[] tablicaKsiazek) {
		for (Ksiazka k : tablicaKsiazek) {
			this.wyswietlKsiazke(k.zwrocId());
		}
	}

	// WYŚWIETLANIE KSIĄŻEK W KONSOLI
	private void wyswietlKsiazke(int id) {
		Ksiazka k = ksiazki.get(id);
		System.out.println(k.toString());
	}

	private void wyswietlSkroconaKsiazke(int id) {
		Ksiazka k = ksiazki.get(id);
		System.out.println(k.toShortString());
	}

	private ArrayList<String> zwrocListeAutorow(){
		ArrayList<String> autorzy = new ArrayList<>(this.ksiazki.size());
		for (Ksiazka k : this.ksiazki) {
			String autor = k.zwrocImionaAutora() + " " + k.zwrocNazwiskoAutora();
			if (!autorzy.contains(autor)) {
				autorzy.add(autor);
			}
		}
		return autorzy;
	}

	private int[] zwrocTabliceWypozyczenAutorow(ArrayList<String> autorzy){
		int[] wypozyczenia = new int[autorzy.size()];
		for (int i = 0; i < autorzy.size(); i++) {
			for (Ksiazka k : this.ksiazki) {
				String autorKsiazki = k.zwrocImionaAutora() + " " + k.zwrocNazwiskoAutora();
				if (autorzy.get(i).equals(autorKsiazki)) {
					wypozyczenia[i] += k.zwrocLiczbeWypozyczen();
				}
			}
		}
		return wypozyczenia;
	}

	private Ksiazka zwrocNajpoupularniejszaKsiazkeAutora(String autor){
		Ksiazka najpopularniejsza = null;
		for(Ksiazka k : this.ksiazki){
			if(autor.equals(k.zwrocImionaAutora() + " " + k.zwrocNazwiskoAutora())){
				if(najpopularniejsza == null){
					najpopularniejsza = k;
				} else if ( najpopularniejsza.zwrocLiczbeWypozyczen() < k.zwrocLiczbeWypozyczen()){
					najpopularniejsza = k;
				}
			}
		}
		return najpopularniejsza;
	}

	// OPERACJE NA PLIKU Z DANYMI

	private void zapisDoPliku(Ksiazka k){

		String dane = this.pobierzDaneDoZapisu(k);
		try{
			RandomAccessFile raf = new RandomAccessFile(sciezkaDoPlikuZDanymi, "rw");

			char[] c = new char[200];
			Arrays.fill(c, ' ');

			for(int i = 0; i < k.zwrocId(); i++){
				raf.readLine();
			}

			for(int i = 0; i < c.length; i++){
				if(i < dane.length()) c[i] = dane.charAt(i);
				raf.write(c[i]);

			}
			if(k.zwrocId() == this.ksiazki.size() - 1) raf.writeChar('\n');
			raf.close();
		} catch (IOException e) {
			System.err.println("Błąd I/O");
			e.printStackTrace();
		}
	}

	private Ksiazka odczytajZPliku(Scanner read) {

		String[] data = read.nextLine().split("; ");
		String imiona = data[0].split(", ")[0];
		String nazwisko = data[0].split(", ")[1];
		String tytul = data[1];
		int rok = Integer.parseInt(data[2]);
		String kategorie = data[3].replaceAll(", ", ";");
		boolean czyWypozyczona;
		if(data.length >= 5){
			czyWypozyczona = data[4].equals("tak");
		} else {
			czyWypozyczona = false;
		}
		int liczbaWypozyczen;
		if(data.length >= 6){
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

	private String pobierzDaneDoZapisu(Ksiazka k){
		String[] tabKategorii = k.zwrocKategorie().split(";");
		for (String kategoria : tabKategorii) {
			if (!istniejaceKategorie.contains(kategoria)) {
				istniejaceKategorie.add(kategoria);
			}
		}

		String dane = String.format("%s, %s; %s; %d; ", k.zwrocImionaAutora(), k.zwrocNazwiskoAutora(), k.zwrocTytul(),
				k.zwrocRok());
		for (String s : tabKategorii) {
			dane += s + ", ";
		}
		dane = dane.substring(0, dane.length() - 2);
		dane = String.format(dane + "; %s; %d;", (k.zwrocCzyWypozyczona()) ? "tak" : "nie", k.zwrocLiczbeWypozyczen());
		return dane;
	}

}