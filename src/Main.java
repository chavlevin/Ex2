//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
            System.out.println("Test 1: " + Cell.computeForm("=123")); // Expected: 123.0
            System.out.println("Test 2: " + Cell.computeForm("=212+367")); // Expected: 579.0
            System.out.println("Test 3: " + Cell.computeForm("=(1-4)")); // Expected: -3.0
            System.out.println("Test 4: " + Cell.computeForm("=2*3")); // Expected: 6.0
            System.out.println("Test 5: " + Cell.computeForm("=8/2")); // Expected: 4.0
            System.out.println("Test 6: " + Cell.computeForm("=((1+2)-(3+1))")); // Expected: -1.0
            System.out.println("Test 7: " + Cell.computeForm("=((2*5)+10)")); // Expected: 20.0
        }


    }

