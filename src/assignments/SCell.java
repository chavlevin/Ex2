package assignments;// Add your documentation below:

import java.util.ArrayList;
import java.util.List;


public class SCell implements Cell {
    private String line;
    private int type;
    private Ex2Sheet parentSheet;
    public static final int TYPE_NUMBER = 0;
    public static final int TYPE_TEXT = 1;
    public static final int TYPE_FORMULA = 2;


    public SCell(String s, Ex2Sheet parent) {
        parentSheet = parent;
        if (s == null || s.trim().isEmpty()) {
            line = "";
            type = TYPE_TEXT;
        } else {
            setData(s);
        }
    }
    @Override
    public int getOrder() {
        if(type != TYPE_FORMULA){
            return 0;
        }
       return computeOrder();
    }

    private int computeOrder(){
        if(parentSheet==null){
            throw new IllegalArgumentException("Parent sheet is not set for this cell.");
        }

        List<String> dependencies = extractCellRefs(line);
        if(dependencies.isEmpty()){
            return 1;
        }
        int maxOrder = 0;
        for(String cellRef : dependencies){
            Cell dependentCell = parentSheet.get(cellRef);
            if(dependentCell != null) {
                int dependentOrder = dependentCell.getOrder();
                maxOrder = Math.max(maxOrder, dependentOrder);
            }else{
                return -1;
            }
        }
        return maxOrder + 1;
    }

    private List<String> extractCellRefs(String formula) {
        List<String> refs = new ArrayList<>();
        int i = 0;

        while (i < formula.length()) {
            char c = formula.charAt(i);
            if (Character.isLetter(c)) {
                int start = i;
                i++;

                while (i < formula.length() && Character.isLetter(formula.charAt(i))) {
                    i++;
                }
                if (i < formula.length() && Character.isDigit(formula.charAt(i))) {
                    int startR = i;
                    while (i < formula.length() && Character.isDigit(formula.charAt(i))) {
                        i++;
                    }
                    refs.add(formula.substring(start, i));
                }
            } else {
                i++;
            }
        }
        return refs;
    }



    private int parseColumn(String cellRef){
        int colIndex = 0;
        int length = cellRef.length();
        for(int i = 0; i < length; i++){
            char c = cellRef.charAt(i);
            colIndex = colIndex * 26 + (c - 'A' +1);
        }
        return colIndex - 1;
    }

    private int parseRow(String cellRef){
        String rowStr = cellRef.replaceAll("[^0-9]", "");
        return Integer.parseInt(rowStr) -1;
    }


    //@Override
    @Override
    public String toString() {
        return getData();
    }

    @Override
public void setData(String s) {
        if(s==null||s.trim().isEmpty()){
            line = "";
            type = TYPE_TEXT;
            return;
        }
        line = s.trim();

        if(MyCell.isNumber(line)){
            type = TYPE_NUMBER;
        } else if(MyCell.isForm(line)){
            type = TYPE_FORMULA;
        }else{
            type = TYPE_TEXT;
        }
    }

    @Override
    public String getData() {
        return line;
    }

    @Override
    public int getType() {
        return type;
    }

    @Override
    public void setType(int t) {
        type = t;
    }

    @Override
    public void setOrder(int t) {
        // Add your code here

    }
}
