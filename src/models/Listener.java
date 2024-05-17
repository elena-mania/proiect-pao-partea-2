package models;

public class Listener extends User {
    private static final double monthlySubscription = 12.99; // valoarea lunara pe care trebuie sa o plateasca fiecare utilizator
    private boolean hasPaid;
    public Listener(){}
    public Listener(String name, String email, String password, int id) {
        super(name, email, password, id);
        this.hasPaid = false;
    }
    public boolean getHasPaid() {return hasPaid;}

    public void setHasPaid(boolean hasPaid) {this.hasPaid = hasPaid;}
    public static double getMonthlySubscription() {return monthlySubscription;}

}
