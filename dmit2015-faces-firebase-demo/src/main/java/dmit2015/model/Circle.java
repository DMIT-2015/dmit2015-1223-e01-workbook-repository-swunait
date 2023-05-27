package dmit2015.model;

public class Circle {

    private double radius;

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public Circle() {
        radius = 1;
    }

    public Circle(double newRadius) {
        this.radius = newRadius;
    }

    public double area() {
        return Math.PI * radius * radius;
    }

    public double perimeter() {
        return 2 * radius;
    }

    public double circumference() {
        return 2 * Math.PI * getRadius();
    }
}
