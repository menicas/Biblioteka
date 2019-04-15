package projektbiblioteka;

import java.util.Scanner;

public class Walidacja {

    static Scanner sc;

    public Walidacja(){
        sc = new Scanner(System.in);
    };

    public static String wprowadzString(){
        String s = null;
        do{
            if(s != null){
                System.out.println("Wprowadzono błędne dane! Spróbuj ponownie!");
            }
            s = sc.nextLine();
        } while(s.length() == 0 || s == null);
        return s;
    }

    public static int sprawdzInt(int min, int max){
        String num = null;
        do{
            if(num != null){
                System.out.println("Wprowadzono błędne dane, wprowadź ponownie.");
            }
            num = sc.nextLine();
        } while(isInteger(num) && (Integer.parseInt(num) > max || Integer.parseInt(num) < min));
        return Integer.parseInt(num);
    }

    public static boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
        } catch(NumberFormatException e) {
            return false;
        } catch(NullPointerException e) {
            return false;
        }
        return true;
    }
}
