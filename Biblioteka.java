package projektbiblioteka;

import java.util.*;

// Komentarze przed metodami oznaczają miejsce, w którym zostały wykorzystane w menu (plik Menu.java)

public class Biblioteka {

	private ArrayList<Ksiazka> ksiazki = new ArrayList<>();
	private final ArrayList<String> istniejaceKategorie = new ArrayList<>();
	private DatabaseController database = new DatabaseController();

	public Biblioteka() {
		/* todo zastanowc sie czy nie bedzie lepszym rozwiazaniem pobranie z bazy raz na poczatku dzialania programu
		liczby wypozyczonych ksiazek i liczby wszystkich wypozyczen, inkrementowac/dekrementowac
		(3 metody beda dzialac szybciej)
		 */
	}

	// ---------------------------------------------- 1 ----------------------------------------------

	public void wyswietlListeKsiazek() {
		System.out.format("%-5s %-15s %-17s %-40s %-12s %-40s %-12s %-4s \n",
				"ID", "imię autora", "nazwisko autora", "tytuł", "rok wydania", "kategorie", "wypożyczona", "liczba wypożyczeń");
		ArrayList<Ksiazka> res = database.odczytajWszystkieKsiazkiZBazy();
		for (Ksiazka k : res) {
			this.wyswietlKsiazke(k);
		}
		System.out.println();
	}

	// ---------------------------------------------- 2 ----------------------------------------------

	public void wyswietlSkroconaListeKsiazek() {
		System.out.format("%-5s %-40s %-35s %-3s \n",
				"ID", "tytuł", "autor", "wypożyczona");
		ArrayList<Ksiazka> res = database.odczytajWszystkieKsiazkiZBazy();
		for (Ksiazka k : res) {
			this.wyswietlSkroconaKsiazke(k);
		}
		System.out.println();
	}

	// ---------------------------------------------- 3 ----------------------------------------------

	public void dodajKsiazke() {
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
		database.dodajKsiazkeDoBazy(dodanaKsiazka);
	}

	// ---------------------------------------------- 4 ----------------------------------------------

	public void edytujKsiazke(int id) { //zmienic wyswietlanie na pelna a nie skrocona
		Ksiazka k = database.odczytajKsiazkeZBazy(id);
		System.out.println("Wybrałeś następującą książkę do edycji:");
		wyswietlKsiazke(k);
		System.out.println("Co chcesz edytować?\n0 - Imiona autora\n1 - Nazwisko autora\n2 - Tytuł\n3 - Rok wydania\n"
				+ "4 - Kategorie");
		int wybor = Walidacja.wprowadzInt(0, 4);
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
		database.edytujKsiazke(k);
	}

	// ---------------------------------------------- 5 ----------------------------------------------

	public void wypozyczKsiazke(int id) {
		Ksiazka k = database.odczytajKsiazkeZBazy(id);
		String daneKsiazki = k.zwrocInicjałyImionAutora() + k.zwrocNazwiskoAutora() + ", " + k.zwrocTytul() + ", " + k.zwrocRok();
		if (k.zwrocCzyWypozyczona()) {
			System.out.println("Nie można wypożyczyć książki - jest już wypożyczona (" + daneKsiazki + ")");
		} else {
			k.wypozycz();
			System.out.println("Wypożyczono książkę (" + daneKsiazki + ")");
		}
	}

	// ---------------------------------------------- 6 ----------------------------------------------

	public void zwrocKsiazke(int id) {
		Ksiazka k = database.odczytajKsiazkeZBazy(id);
		String daneKsiazki = k.zwrocInicjałyImionAutora() + k.zwrocNazwiskoAutora() + ", " + k.zwrocTytul() + ", " + k.zwrocRok();
		if (k.zwrocCzyWypozyczona()) {
			System.out.println("Zwrócono książkę (" + daneKsiazki + ")");
			k.zwroc();
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
				wyswietlKsiazke(k);
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
				wyswietlKsiazke(k);
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
					this.wyswietlKsiazke(k);
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
		return database.zwrocLiczbeKsiazek();
	}

	public int zwrocIleObecnieWypozyczonych() {
		return database.zwrocIleObecnieWypozyczonych();
	}

	public int zwrocIleWszystkichWypozyczen() {
		return database.zwrocIleWszystkichWypozyczen();
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
					this.wyswietlKsiazke(tablicaKsiazek[liczbaWyswietlanychKsiazek]);
					liczbaWyswietlanychKsiazek++;
				}
			} else {
				for (int i = 0; i < 5; i++) {
					if (tablicaKsiazek[i].zwrocLiczbeWypozyczen() != 0) {
						this.wyswietlKsiazke(tablicaKsiazek[i]);
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
				if (!listaKsiazek.contains(k)) {
					listaKsiazek.add((Ksiazka) k.clone());
				}
			}
		} catch (CloneNotSupportedException e) {
			System.out.println("Klonowanie nie powiodlo sie");
			e.printStackTrace();
		}

		Iterator<Ksiazka> it;

		for (it = listaKsiazek.iterator(); it.hasNext(); ) {
			Ksiazka k1 = it.next();
			for (Iterator<Ksiazka> itt = this.ksiazki.iterator(); itt.hasNext(); ) {
				Ksiazka k2 = itt.next();
				if (k1.equals(k2) && !k1.toString().equals(k2.toString())) {
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

		int cnt = autorzy.size() > 5 ? 5 : autorzy.size();
		for (int i = 0; i < cnt; i++) {
			if (wypozyczenia[i] > 0) {
				String autor = autorzy.get(i);
				System.out.println(autor + ", liczba wypożyczeń: " + wypozyczenia[i] + ", najpopularniejsza książka - \"" +
						this.zwrocNajpoupularniejszaKsiazkeAutora(autor).zwrocTytul() + "\"");
			}
		}
	}

	// ---------------------------------------------- 9 ----------------------------------------------
	// 9.1

	public Ksiazka[] sortujWgNaziwskaAutora() {
		Ksiazka[] posortowane = database.odczytajWszystkieKsiazkiZBazy().toArray(new Ksiazka[database.zwrocLiczbeKsiazek()]);
		Arrays.sort(posortowane, Comparator.comparing(Ksiazka::zwrocNazwiskoAutora));
		return posortowane;
	}

	// 9.2

	public Ksiazka[] sortujWgRokuWydania() {
		Ksiazka[] posortowane = database.odczytajWszystkieKsiazkiZBazy().toArray(new Ksiazka[database.zwrocLiczbeKsiazek()]);
		Arrays.sort(posortowane, (k1, k2) -> k2.zwrocRok() - k1.zwrocRok());
		return posortowane;
	}

	// 9.3

	public Ksiazka[] sortujWgLiczbyWypozyczen() {
		Ksiazka[] posortowane = database.odczytajWszystkieKsiazkiZBazy().toArray(new Ksiazka[database.zwrocLiczbeKsiazek()]);
		Arrays.sort(posortowane, (k1, k2) -> k2.zwrocLiczbeWypozyczen() - k1.zwrocLiczbeWypozyczen());
		return posortowane;
	}

	// 9.4

	public Ksiazka[] sortujWgTytulu() {
		Ksiazka[] posortowane = database.odczytajWszystkieKsiazkiZBazy().toArray(new Ksiazka[database.zwrocLiczbeKsiazek()]);
		Arrays.sort(posortowane, Comparator.comparing(Ksiazka::zwrocTytul));
		return posortowane;
	}

	// ---------------------------------------------- METODY POMOCNICZE ----------------------------------------------

	public void wyswietlTabKsiazek(Ksiazka[] tablicaKsiazek) {
		for (Ksiazka k : tablicaKsiazek) {
			this.wyswietlKsiazke(k);
		}
	}

	// WYŚWIETLANIE KSIĄŻEK W KONSOLI

	private void wyswietlKsiazke(Ksiazka k) {
		System.out.println(k.toString());
	}

	private void wyswietlSkroconaKsiazke(Ksiazka k) {
		System.out.println(k.toShortString());
	}

	private ArrayList<String> zwrocListeAutorow() {
		ArrayList<String> autorzy = new ArrayList<>(this.ksiazki.size());
		for (Ksiazka k : this.ksiazki) {
			String autor = k.zwrocImionaAutora() + " " + k.zwrocNazwiskoAutora();
			if (!autorzy.contains(autor)) {
				autorzy.add(autor);
			}
		}
		return autorzy;
	}

	private int[] zwrocTabliceWypozyczenAutorow(ArrayList<String> autorzy) {
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

	private Ksiazka zwrocNajpoupularniejszaKsiazkeAutora(String autor) {
		Ksiazka najpopularniejsza = null;
		for (Ksiazka k : this.ksiazki) {
			if (autor.equals(k.zwrocImionaAutora() + " " + k.zwrocNazwiskoAutora())) {
				if (najpopularniejsza == null) {
					najpopularniejsza = k;
				} else if (najpopularniejsza.zwrocLiczbeWypozyczen() < k.zwrocLiczbeWypozyczen()) {
					najpopularniejsza = k;
				}
			}
		}
		return najpopularniejsza;
	}
}