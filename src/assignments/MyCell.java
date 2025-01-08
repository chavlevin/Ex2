package assignments;

public class MyCell {

    //This method checks if the String in cell is a number

    public static boolean isNumber(String n) {
        if (n == null || n.isEmpty()) return false;

        boolean allDigits = true; //Checks if string n consists of only digits.

        for (int i = 0; i < n.length(); i++) {
            char c = n.charAt(i);
            if (!Character.isDigit(c)) {
                allDigits = false;
                break;
            }
        }
        if (!allDigits) {//If the string is made up of characters other than digits, this ensures that it's either a negative sign or a decimal point
            int decimalCount = 0;
            for (int j = 0; j < n.length(); j++) {
                char c = n.charAt(j);

                if (c == '-') {
                    if (j != 0) return false;//negative sign can only be in the beginning of the number
                } else if (c == '.') {
                    decimalCount++;
                    if (decimalCount > 1) return false;//There cannot be more than one decimal point in a number
                } else if (!Character.isDigit(c)) {
                    return false;//Any other non-digit character is invalid.
                }
            }
        }
        return true;
    }


    //This method will check whether a string is text.

    public static boolean isText(String t) {
        if (isNumber(t)) {
            return false;//Return false if the string is a number because then it isn't a text
        }
        if (isForm(t)) {
            return false;//Return false if the string is a valid formula because then it isn't a text
        }
        return true;//Otherwise, it is a text
    }

    //This method will check whether a string is a formula.

    public static boolean isForm(String f) {
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

        boolean lastCharOp = true;//Checks if last character of string is an operator
        for (int i = 0; i < f.length(); i++) {
            char o = f.charAt(i);

            if (o == '+' || o == '-' || o == '*' || o == '/') { //Checking for valid operators in formula
                if (lastCharOp) {
                    return false;//Two operators in a row is invalid
                }
                lastCharOp = true;
                } else if (Character.isDigit(o) || o == '(' || o == ')') {
                    lastCharOp = false;
                } else {
                    return false;
                }
            }
            if(lastCharOp) {
                return false;//Ensures the last character isn't an operator
            }
            int mainOpIndex = findMainOp(f);
            if(mainOpIndex==-1) {
                return false;//No valid operator found
            }

            String leftSide = f.substring(0, mainOpIndex);//Left side of the operator
            String rightSide = f.substring(mainOpIndex + 1);//Everything to the right of the operator

            return !leftSide.isEmpty() && !rightSide.isEmpty() && isForm("=" + leftSide) && isForm("="+ rightSide);
            }





            private static boolean balancedPar(String f) {//Checks if amount of parentheses are balanced
        int balance = 0;

        for (int i = 0; i < f.length(); i++) {
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
        return balance == 0;//Balance must be zero if the parentheses are balanced
    }


    private static int findMainOp(String form) {
        int balance = 0;
        int mainOpIndex = -1;

        for (int i = 0; i < form.length(); i++) {
            char c = form.charAt(i);
            if (c == '(') balance++;
            else if (c == ')') balance--;
            else if (balance == 0) {//Only consider operators with highest precedence
                if (c == '*' || c == '/') {
                    mainOpIndex = i;
                }else if ((c == '+' || c == '-') && mainOpIndex == -1) {
                    if(i==0||form.charAt(i-1)=='(' || form.charAt(i-1)=='+' || form.charAt(i-1)=='-'){
                        continue;
                    }
                    mainOpIndex = i;
                }
            }
        }
        return mainOpIndex;

    }


    public static Double computeForm(String form) {

        if (form.startsWith("=") && form.length()>1) {
            form = form.substring(1);//Remove the equals sign for evaluation
        }

        form = form.trim();


        while(form.contains("(")){
            int startIndex = form.lastIndexOf("(");//Find the innermost opening parentheses
            if(startIndex==-1){
                return null;
            }
            int endIndex = form.indexOf(")", startIndex);//Find the pair
            if(endIndex==-1 || endIndex<=startIndex){
                return null;
            }
            String innerForm = form.substring(startIndex + 1, endIndex).trim();//Content inside the parentheses

            if(innerForm.isEmpty()){
                return null;//Empty parentheses
            }
            Double innerResult = computeForm(innerForm);
            if(innerResult == null){
                return null;//If the inner formula is invalid, return null.
            }

            form = form.substring(0,startIndex) + innerResult + form.substring(endIndex +1).trim();

            if (form.startsWith("(") && form.endsWith(")") && balancedPar(form)) {
                return computeForm(form.substring(1, form.length() - 1));//Remove parentheses to evaluate content inside them
            }


        }


        if(form.startsWith("-")) {
            String subForm = form.substring(1).trim();
            if (isNumber(subForm)) {
                return -Double.parseDouble(subForm);//If form is just a negative number, return it as a number
            }
            Double subResult = computeForm(subForm);
            if(subResult==null){
                return null;
            }
            return -subResult;//If it's a negative expression, evaluate recursively
        }

        if (isNumber(form)) {
            return Double.parseDouble(form);//If it's just a number then return its value
        }



        int mainOpIndex = findMainOp(form);
        if (mainOpIndex == -1) {
            return null;//No valid operator
        }

        String leftSide = form.substring(0, mainOpIndex).trim();
        String rightSide = form.substring(mainOpIndex + 1).trim();


        if(isNumber(leftSide) && isNumber(rightSide)){//If left and right side are simple numbers, no need to computeForm again on them from beginning
            Double leftValue = Double.parseDouble(leftSide);
            Double rightValue = Double.parseDouble(rightSide);

            char op = form.charAt(mainOpIndex);
            switch (op) {
                case '+':
                    return leftValue + rightValue;
                case '-':
                    return leftValue - rightValue;
                case '*':
                    return leftValue * rightValue;
                case '/':
                    if(rightValue==0){
                        return null;//Cannot divide by zero.
                    }
                    return leftValue / rightValue;
                default:
                    return null;
            }
        }


        if(leftSide.isEmpty() || rightSide.isEmpty() || !isForm("=" + leftSide) || !isForm(rightSide)){
            return null;//A formula missing operands is invalid.
        }

//Recursively compute left and right side if they are not numbers
     Double leftValue = computeForm(leftSide);
        Double rightValue = computeForm(rightSide);
if(leftValue == null || rightValue == null){
    return null;//Return null if either side is invalid.
}

        char op = form.charAt(mainOpIndex);
        switch (op) {
            case '+':
                return leftValue + rightValue;
            case '-':
                return leftValue - rightValue;
            case '*':
                return leftValue * rightValue;
            case '/':
                if(rightValue==0){
                    return null;//Cannot divide by zero.
                }
                return leftValue / rightValue;
            default:
                return null;
        }
    }


public static Double computeWithError(String form) {
    Double result = computeForm(form);
    if (result == null) {
        System.out.println("ERR_FORM");
    }
    return result;
}
}
















