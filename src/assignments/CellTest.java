package assignments;

import org.junit.jupiter.api.Test;

import static org.junit.Assert.*;

public class CellTest {
    @Test
    public void testIsNumber_ValidNumbers(){
        assertTrue(MyCell.isNumber("123"));
        assertTrue(MyCell.isNumber("-0.5"));
        assertTrue(MyCell.isNumber("0"));
        assertTrue(MyCell.isNumber("325.75"));
        assertTrue(MyCell.isNumber("-66"));
    }
    @Test
    public void testIsNumber_InvalidNumbers(){
        assertFalse(MyCell.isNumber("12a"));
        assertFalse(MyCell.isNumber("3.4.6"));
        assertFalse(MyCell.isNumber(""));
        assertFalse(MyCell.isNumber("12*3"));
    }

    @Test
    public void testIsText_ValidTexts(){
        assertTrue(MyCell.isText("hello"));
        assertTrue(MyCell.isText("123abc"));
    }

    @Test
    public void testIsText_InvalidTexts(){
        assertFalse(MyCell.isText("=123"));
        assertFalse(MyCell.isText("123"));
    }
    @Test
    public void testIsForm_Valid(){
        assertTrue(MyCell.isForm("=123"));
        assertTrue(MyCell.isForm("=(12+3)*4"));
        assertTrue(MyCell.isForm("=(12+78)"));
        assertTrue(MyCell.isForm("=8/(6-4)"));
    }

    @Test
    public void testIsForm_Invalid(){
        assertFalse(MyCell.isForm("123"));
        assertFalse(MyCell.isForm("1+5"));
        assertFalse(MyCell.isForm("=12+5)"));
        assertFalse(MyCell.isForm("=64+"));
        assertFalse(MyCell.isForm("=35--3"));
    }

    @Test
    public void testComputeForm_Valid(){
        assertEquals(123.0, MyCell.computeForm("=123"),0.0001);
        assertEquals(579.0, MyCell.computeForm("=212+367"), 0.0001);
        assertEquals(-3, MyCell.computeForm("=(1-4)"), 0.0001);
        assertEquals(6.0, MyCell.computeForm("=2*3"), 0.0001);
        assertEquals(4, MyCell.computeForm("=8/2"), 0.0001);
        assertEquals(-1, MyCell.computeForm("=((1+2)-(3+1))"), 0.0001);
        assertEquals(20.0, MyCell.computeForm("=((2*5)+10)"), 0.0001);

    }

}
