class Circle {
    private double radius;

    // Constructor
    public Circle(double radius) {
        this.radius = radius;
    }

    // Method to calculate and return area
    public double getArea() {
        return Math.PI * radius * radius;
    }

    // Getters and Setters
    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }
}

public class Encapsulation {
    public static void main(String[] args) {
        Circle circle = new Circle(5.0);
        System.out.println("Area of circle = " + circle.getArea());
    }
}
