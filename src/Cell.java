
public class Cell {

    //This method checks if the String in cell is a number

    public static boolean isNumber(String n) {
        boolean ans = false;//Starting by assuming the string isn't a number

        boolean allDigits = true; //Checks if string n consists of only digits.

        for (int i = 0; i < n.length(); i++) {
                if (!Character.isDigit(n.charAt(i))) {//Check for non-digit characters
                    allDigits = false;
                    break;


                }
            if (!allDigits) {//If the string is made up of characters other than digits, this ensures that it's either a negative sign or a decimal point
                for (int j = 0; j < n.length(); j++) {
                    char c = n.charAt(j);
                    if (c == '-' && j!=0) {//negative sign can only be in the beginning of the number
                        return ans;
                    }
                    if (c == '.' && n.indexOf('.') != j) {//There cannot appear more than one decimal point in a number
                        return ans;
                    }
                    if (c != '-' && c!= '.' && !Character.isDigit(c)) { //If there's any other character
                        return ans;
                    }

                    return ans;//Returns false if the string is not a number
                }
            }




        }
        ans = true;
        return true;//If all the checks are passed, then it's a number
    }


    //This method will check whether a string is text.

   public static boolean isText (String t) {
       if (isNumber(t)) {
           return false;//Return false if the string is a number because then it isn't a text
       }
       if (isForm(t)) {
           return false;//Return false if the string is a valid formula because then it isn't a text
       }
       return true;//Otherwise, it is a text
    }

    //This method will check whether a string is a formula.

    public static boolean isForm (String f) {
        if (!f.startsWith("=")) {//A valid formula must start with an equals sign
            return false;
        }
        f = f.substring(1); //To validate the rest of the string, we look at everything after the equals sign.

        if (isNumber(f)) {
            return true;//A standalone number is a valid formula
        }

        if (!balancedPar(f)) {//If there are parentheses in the formula, they must be balanced.
            return false;
        }

        if (f.startsWith("(") && f.endsWith(")")) {
            String content = f.substring(1, f.length() - 1); //If the entire formula string is inside parentheses, remove them to validate its contents.
            return isForm("=" + content);
        }

        for (int i = 0; i < f.length(); i++) {
            char o = f.charAt(i);

            if (o == '+' || o == '-' || o == '*' || o == '/') { //Checking for valid operators in formula
                String leftSide = f.substring(0, i);//Left side of the operator
                String rightSide = f.substring(i, i + 1);//Right side of the operator

                if (isForm("=" + leftSide) && isForm("=" + rightSide)) {
                    return true;
                }
            }

            if (!Character.isDigit(o) && o != '+' && o != '-' && o != '*' && o != '/' && o != '(' && o != ')') {
                return false;//Any other character would be invalid
            }
        }
        return false;
    }

    private static boolean balancedPar (String f) {//Checks if amount of parentheses are balanced
        int balance = 0;

        for(int i = 0; i<f.length();i++) {
            char p = f.charAt(i);
            if (p == '(') {
                balance++;//Each time there is an open parentheses the balance goes up.
            } else if (p == ')') {
                balance--;//Each time there is a close parentheses the balance goes down.
            }
            if (balance < 0) {//If the balance is negative then the parentheses are not balanced.
                return false;
            }
        }
        return balance ==0;//Balance must be zero if the parentheses are balanced
            }
        }










