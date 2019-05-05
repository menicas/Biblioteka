package projektbiblioteka;

import java.util.InputMismatchException;
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
		} while (!s.matches("^([a-z]{3,};)*[a-z]{3,}$"));
		return s;
	}

	public static int wprowadzInt(int min, int max){
		System.out.print("Wprowadź liczbę(" + min + "-" + max + "):");
		Scanner read = new Scanner(System.in);
		int liczba = 0;
		try{
			liczba = read.nextInt();
			if(liczba > max || liczba < min) throw new InputMismatchException();
		} catch (InputMismatchException e){
			System.out.println("Błędne dane, spróbuj ponownie:");
			return wprowadzInt(min, max);
		}
		return liczba;
	}

	private static boolean isInteger(String num){
		try {
			Integer.parseInt(num);
		} catch (NumberFormatException e){
			return false;
		}
		return true;
	}
}
