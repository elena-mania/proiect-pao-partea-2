package models;

public class Artist extends User {
    private String genre;
    private double monthlyEarnings;
    public Artist(){}
    public Artist(String name, String email, String password, String genre, int id) {
        super(name, email, password,id);
        this.genre = genre;
        this.monthlyEarnings = 0;
    }

    public String getGenre() {return genre;}

    public void setGenre(String genre) {this.genre = genre;}

    public double getMonthlyEarnings() {return monthlyEarnings;}

    public void setMonthlyEarnings(double monthlyEarnings) {this.monthlyEarnings = monthlyEarnings;}

}
