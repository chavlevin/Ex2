import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CellTest {
    @Test
    public void testIsNumber_ValidNumbers(){
        assertTrue(Cell.isNumber("123"));
        assertTrue(Cell.isNumber("-0.5"));
        assertTrue(Cell.isNumber("0"));
        assertTrue(Cell.isNumber("325.75"));
        assertTrue(Cell.isNumber("-66"));
    }
    @Test
    public void testIsNumber_InvalidNumbers(){
        assertFalse(Cell.isNumber("12a"));
        assertFalse(Cell.isNumber("3.4.6"));
        assertFalse(Cell.isNumber(""));
        assertFalse(Cell.isNumber("12*3"));
    }

    @Test
    public void testIsText_ValidTexts(){
        assertTrue(Cell.isText("hello"));
        assertTrue(Cell.isText("123abc"));
    }

    @Test
    public void testIsText_InvalidTexts(){
        assertFalse(Cell.isText("=123"));
        assertFalse(Cell.isText("123"));
    }
    @Test
    public void testIsForm_Valid(){
        assertTrue(Cell.isForm("=123"));
        assertTrue(Cell.isForm("=(12+3)*4"));
        assertTrue(Cell.isForm("=(12+78)"));
        assertTrue(Cell.isForm("=8/(6-4)"));
    }

    @Test
    public void testIsForm_Invalid(){
        assertFalse(Cell.isForm("123"));
        assertFalse(Cell.isForm("1+5"));
        assertFalse(Cell.isForm("=12+5)"));
        assertFalse(Cell.isForm("=64+"));
        assertFalse(Cell.isForm("=35--3"));
    }

    @Test
    public void testComputeForm_Valid(){
        assertEquals(123.0,Cell.computeForm("=123"),0.0001);
        assertEquals(579.0, Cell.computeForm("=212+367"), 0.0001);
        assertEquals(-3, Cell.computeForm("=(1-4)"), 0.0001);
        assertEquals(6.0, Cell.computeForm("=2*3"), 0.0001);
        assertEquals(4, Cell.computeForm("=8/2"), 0.0001);
        assertEquals(-1, Cell.computeForm("=((1+2)-(3+1))"), 0.0001);
        assertEquals(20.0, Cell.computeForm("=((2*5)+10)"), 0.0001);

    }

}
