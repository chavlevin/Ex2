
public class Cell {

    //This method checks if the String in cell is a number

    boolean isNumber(String n) {
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

    boolean isText (String t); {

    }
}