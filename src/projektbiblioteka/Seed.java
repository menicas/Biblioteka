package projektbiblioteka;

public class Seed {

    private Biblioteka biblioteka;

    public Seed(Biblioteka b){
        this.biblioteka = b;
    }

    public void seed(){
        biblioteka.wypozyczKsiazke(1);
        biblioteka.zwrocKsiazke(1);
        biblioteka.wypozyczKsiazke(1);
        biblioteka.zwrocKsiazke(1);
        biblioteka.wypozyczKsiazke(1);
        biblioteka.zwrocKsiazke(1);
        biblioteka.wypozyczKsiazke(1);
        biblioteka.zwrocKsiazke(1);
        biblioteka.wypozyczKsiazke(1);
        biblioteka.zwrocKsiazke(1);

        biblioteka.wypozyczKsiazke(2);
        biblioteka.zwrocKsiazke(2);
        biblioteka.wypozyczKsiazke(2);
        biblioteka.zwrocKsiazke(2);
        biblioteka.wypozyczKsiazke(2);
        biblioteka.zwrocKsiazke(2);
        biblioteka.wypozyczKsiazke(2);
        biblioteka.zwrocKsiazke(2);

        biblioteka.wypozyczKsiazke(3);
        biblioteka.zwrocKsiazke(3);
        biblioteka.wypozyczKsiazke(3);
        biblioteka.zwrocKsiazke(3);

        biblioteka.wypozyczKsiazke(4);
        biblioteka.zwrocKsiazke(4);
        biblioteka.wypozyczKsiazke(4);
        biblioteka.zwrocKsiazke(4);
        biblioteka.wypozyczKsiazke(4);
        biblioteka.zwrocKsiazke(4);
        biblioteka.wypozyczKsiazke(4);
        biblioteka.zwrocKsiazke(4);
        biblioteka.wypozyczKsiazke(4);
        biblioteka.zwrocKsiazke(4);

        biblioteka.wypozyczKsiazke(5);
        biblioteka.zwrocKsiazke(5);

        biblioteka.wypozyczKsiazke(0);
        biblioteka.zwrocKsiazke(0);
    }
}
