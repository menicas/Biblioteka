package projektbiblioteka;

public class Ksiazka implements Comparable {

    private static int id = 0;

    private final int bookId;
    private String tytul;
    private String nazwiskoAutora;
    private String imionaAutora;
    private int rok;
    private String kategorie;
    private boolean czyWypozyczona;
    private int liczbaWypozyczen;


    public Ksiazka(String tytul, String imionaAutora, String nazwiskoAutora, int rok, String kategorie, boolean czyWypozyczona, int liczbaWypozyczen) {
        this.bookId = id;
        this.tytul = tytul;
        this.nazwiskoAutora = nazwiskoAutora;
        this.imionaAutora = imionaAutora;
        this.rok = rok;
        this.kategorie = kategorie.toLowerCase();
        this.czyWypozyczona = czyWypozyczona;
        this.liczbaWypozyczen = liczbaWypozyczen;
        id++;
    }

    public int zwrocId() {
        return this.bookId;
    }

    public String zwrocImionaAutora() {
        return this.imionaAutora;
    }

    public String zwrocInicjałyImionAutora() {
        String[] inicjalyTab = this.zwrocImionaAutora().split(" ");
        String inicjaly = "";
        for (String s : inicjalyTab) inicjaly += s.charAt(0) + ". ";
        return inicjaly;
    }

    public void ustawImionaAutora(String imionaAutora) {
        this.imionaAutora = imionaAutora;
    }

    public String zwrocNazwiskoAutora() {
        return this.nazwiskoAutora;
    }

    public void ustawNazwiskoAutora(String nazwiskoAutora) {
        this.nazwiskoAutora = nazwiskoAutora;
    }

    public String zwrocTytul() {
        return this.tytul;
    }

    public void ustawTytul(String tytul) {
        this.tytul = tytul;
    }

    public int zwrocRok() {
        return this.rok;
    }

    public void ustawRok(int rok) {
        this.rok = rok;
    }

    public void ustawKategorie(String kategorie) {
        this.kategorie = kategorie;
    }

    public String zwrocKategorie() {
        return this.kategorie;
    }

    public void wypozycz() {
        this.czyWypozyczona = true;
    }

    public void zwroc() {
        this.czyWypozyczona = false;
        liczbaWypozyczen++;
    }

    public boolean zwrocCzyWypozyczona() {
        return this.czyWypozyczona;
    }

    public int zwrocLiczbeWypozyczen() {
        return liczbaWypozyczen;
    }

    @Override
    public int compareTo(Object k) {
        int wypozyczenia = ((Ksiazka) k).zwrocLiczbeWypozyczen();
        return wypozyczenia - this.liczbaWypozyczen;
    }
}
