package de.axa.robin.vertragsverwaltung.modell;

public class PreisDTO {
    private double speed = 0.2;
    private double age = 0.5;
    private double faktor = 1.5;

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getAge() {
        return age;
    }

    public void setAge(double age) {
        this.age = age;
    }

    public double getFaktor() {
        return faktor;
    }

    public void setFaktor(double faktor) {
        this.faktor = faktor;
    }
}
