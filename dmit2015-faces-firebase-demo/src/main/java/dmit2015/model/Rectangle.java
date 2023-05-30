package dmit2015.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
public class Rectangle {

    @Getter @Setter
    private double length;

    @Getter @Setter
    private double width;

    public Rectangle() {
        length = 1;
        width = 1;
    }

    public double area() {
        return length * width;
    }

    public double perimeter() {
        return 2 * (length + width);
    }

}
