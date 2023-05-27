package dmit2015.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

class CircleTest {

    @Test
    void area() {
        Circle currentCircle = new Circle(15);
        assertEquals(706.86, currentCircle.area(), 0.005);
    }

    @Test
    void perimeter() {
        Circle currentCircle = new Circle();
        currentCircle.setRadius(25);
        assertEquals(50, currentCircle.perimeter());
    }

    @Test
    void circumference() {
        var currentCircle = new Circle(35);
        assertEquals(219.91, currentCircle.circumference(), 0.005);
    }

    @ParameterizedTest(name = "radius = {0}, expected area = {1} ")
    @CsvSource({
            "1.0, 3.14",
            "25.0, 1963.50",
            "100.0, 31415.93",
            "125.0, 49087.39",
    })
    void area_DifferentRadius_ReturnsCorrectResults(double radius, double expectedArea) {
// Arrange
        Circle circle1 = new Circle();
// Act
        circle1.setRadius(radius);
// Assert
        assertEquals(expectedArea, circle1.area(), 0.005);
    }
}