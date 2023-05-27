package dmit2015.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ProgrammingLanguageTest {

    @Test
    public void powerOperator_returnsCorrectResults() {
//        assertEquals( 8, 2^3 );
        assertEquals( 8, Math.pow(2, 3));
    }

    @Test
    public void divideOperator_integerDivision_returnsCorrectResults() {
//        assertEquals( 0.80, 4 / 5);
        assertEquals( 0.80, 1.0 * 4 / 5, 0);
        assertEquals( 0.80, 4.0 / 5, 0);
    }

    @Test
    public void equalsOperator_stringCompare_returnsCorrectResults() {
        assertTrue("dmit2015" == "dmit2015");
        assertTrue( "dmit2015".equals("dmit2015"));
        assertTrue( "dmit2015".equalsIgnoreCase("DMIT2015"));
    }

    @Test
    public void divideOperator_divisionByZero_throwsArithmeticException() {
//        assertEquals( 0, 3 / 0);
        Exception ex = assertThrows(ArithmeticException.class, () -> {
            assertEquals( 0, 3 / 0);
        });
        assertEquals("/ by zero", ex.getMessage());
    }
}
