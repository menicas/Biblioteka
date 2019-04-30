package projektbiblioteka;

import java.util.Scanner;

public class Walidacja {

	public static Scanner sc = new Scanner(System.in);

	public static String wproadzSciezke() {
		String s = null;
		do {
			if (s != null) {
				System.out.println("Wprowadzono błędne dane! Spróbuj ponownie!");
			}
			s = sc.nextLine();
		} while (s.matches("^[A-Za-z0-9\\\\_\\-!@#$%&\\*\\(\\)`~=\\+\\[\\]{}\\/:]+$"));
		return s;
	}

	public static String wprowadzTytul(){
		String s = null;
		do{
			if (s != null) {
				System.out.println("Wprowadzono błędne dane! Spróbuj ponownie!");
			}
			s = sc.nextLine();
		} while(!s.matches("^[A-Za-z0-9]{1}[A-Za-z0-9\\-\\.!_@#$%&~`'\\*\\(\\) ]*$"));
		return s;
	}

	public static String wprowadzDane(){
		String s = null;
		do{
			if (s != null) {
				System.out.println("Wprowadzono błędne dane! Spróbuj ponownie!");
			}
			s = sc.nextLine();
		} while(!s.matches("^[A-Za-z]{1}[A-Za-z0-9\\- ]+$"));
		return s;
	}

	public static String wprowadzKategorie(){
		String s = null;
		do {
			if(s != null){
				System.out.println("Wprowadzono błędne dane! Spróbuj ponownie!");
			}
			s = sc.nextLine();
		} while (!s.matches("^([a-z]{3,};)*[a-z]+$"));
		return s;
	}

	//todo regexp
	public static int sprawdzInt(int min, int max) {
		String num = null;
		do {
			if (num != null) {
				System.out.println("Wprowadzono błędne dane, wprowadź ponownie.");
			}
			num = sc.nextLine();
		} while (!isInteger(num) || (Integer.parseInt(num) > max || Integer.parseInt(num) < min));
		return Integer.parseInt(num);
	}

	static boolean isInteger(String s) {
		try {
			Integer.parseInt(s);
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}
}
