package assignments;// Add your documentation below:

import java.util.ArrayList;
import java.util.List;


public class SCell implements Cell {
    private String line;
    private int type;
    private String data;
    private int order;
    private Ex2Sheet sheet;

    public static final int TYPE_NUMBER = 0;
    public static final int TYPE_TEXT = 1;
    public static final int TYPE_FORMULA = 2;


    public SCell(String data, Ex2Sheet sheet) {
        this.sheet = sheet;
        this.order = 0;
        setData(data);
    }

    @Override
    public int getOrder() {
        if (type != TYPE_FORMULA) {
            return 0;
        }
        return order;
    }

    private int computeOrder() {
        return computeOrder(new ArrayList<>(), new ArrayList<>());
    }

    private int computeOrder(List<String> visited, List<String> processing) {
        if(processing.contains(line)){
            throw new IllegalArgumentException("Circular dependency");
        }

        if (visited.contains(line)) {
            return 0;
        }

        processing.add(line);
        visited.add(line);

        List<String> dependencies = extractCellRefs(line);
        if (dependencies.isEmpty()) {
            processing.remove(line);
            return 1;
        }
        int maxOrder = 0;
        for (String cellRef : dependencies) {
            Cell dependentCell = sheet.get(cellRef);
            if(dependentCell==null){
                throw new IllegalArgumentException("Cell does not exist");
            }

            int dependentOrder = dependentCell.getOrder();
                if(dependentOrder==0) {
                    dependentOrder = ((SCell) dependentCell).computeOrder(visited,processing);
                }
                maxOrder = Math.max(maxOrder, dependentOrder);
        }
        processing.remove(line);
        return maxOrder + 1;
    }



    private List<String> extractCellRefs(String formula) {
        List<String> cellRefs = new ArrayList<>();
        if(formula==null|| formula.isEmpty()){
            return cellRefs;
        }
        StringBuilder currentRef = new StringBuilder();


        for (int i = 0; i < formula.length(); i++) {
            char c = formula.charAt(i);
            if (Character.isLetter(c)) {
                currentRef.append(c);
            }else if(Character.isDigit(c)) {
                currentRef.append(c);
            }else{
                if(isValidCellRef(currentRef.toString())) {
                    cellRefs.add(currentRef.toString());
                }
                currentRef.setLength(0);
                }
            }
        if(isValidCellRef(currentRef.toString())){
            cellRefs.add(currentRef.toString());
        }
        return cellRefs;
    }

private boolean isValidCellRef(String ref) {
    if (ref.isEmpty()) {
        return false;
    }
    int i = 0;
    while (i < ref.length()  && Character.isLetter(ref.charAt(i))){
        i++;
    }
    if (i == 0 || i == ref.length()) {
        return false;
    }
    while (i < ref.length()) {
        if (!Character.isDigit(ref.charAt(i))) {
            return false;
        }
        i++;
    }
    return true;
}


    //@Override
    @Override
    public String toString() {
        return getValue();
    }

    @Override
    public void setData(String s) {
        if (s == null || s.trim().isEmpty()) {
            line = "";
            type = TYPE_TEXT;
            data = "";
            order = 0;
            return;
        }
        line = s.trim();

        if (MyCell.isForm(line)) {
            type = TYPE_FORMULA;
            data = line;
            order = computeOrder();
        } else if (MyCell.isNumber(line)) {
            type = TYPE_NUMBER;
            data = line;
        } else {
            type = TYPE_TEXT;
            data = line;
        }
    }

    @Override
    public String getData() {
        return data;
    }

    @Override
    public int getType() {
        if (line == null || line.trim().isEmpty()) {
            return TYPE_TEXT;
        }
        return type;
    }

    @Override
    public void setType(int t) {
        type = t;
    }

    @Override
    public void setOrder(int t) {
        if (type == TYPE_FORMULA) {
            this.order = t;
        } else {
            this.order = 0;
        }
    }

    public String getValue() {
        if(data == null||data.isEmpty()){
            return "";
        }
        if(type==TYPE_NUMBER){
            return data;
        }
        if(type==TYPE_FORMULA) {
            if(data.length()<=1){
                return "ERR_FORM";
            }
            String formula = data.substring(1);
            Double result = MyCell.computeForm("=" + formula);
            if(result==null){
                return "ERR_FORM";
            }
            return result.toString();
        }
        return data;
    }

    public String evaluateForm(List<String> visited, List<String> processing) {
        if (type != TYPE_FORMULA) {
            return data;
        }

        try{
        String formula = data.substring(1);

        if(processing.contains(data)){
            setData(Ex2Utils.ERR_CYCLE);
            return Ex2Utils.ERR_CYCLE;
        }
        processing.add(data);

        List<String> cellRefs = extractCellRefs(formula);
        for(String cellRef: cellRefs) {
            Cell refCell = sheet.get(cellRef);

            if (refCell == null) {
                setData(Ex2Utils.ERR_FORM);
                return Ex2Utils.ERR_FORM;
            }
            if (!visited.contains(cellRef)) {
                if (refCell instanceof SCell) {
                    ((SCell) refCell).evaluateForm(visited, processing);
                }
                visited.add(cellRef);
            }
            String refValue = refCell.getData();
            if (!MyCell.isNumber(refValue)) {
                setData(Ex2Utils.ERR_FORM);
                return Ex2Utils.ERR_FORM;
            }
            formula = formula.replaceAll("\\b" + cellRef + "\\b", refValue);
        }

        String result = MyCell.computeWithError("=" + formula);
        setData(result);
        processing.remove(data);
        return result;
    }catch (Exception e) {
            setData(Ex2Utils.ERR_FORM);
            return Ex2Utils.ERR_FORM;
        }
        }

    private boolean hasCycle(String formula, List<String> visited, List<String> processing) {


        return hasCycleHelper(formula, visited, processing);
    }

    private boolean hasCycleHelper(String formula, List<String> visited, List<String> processing) {

        List<String> cellRefs = extractCellRefs(formula);
        for (String ref : cellRefs) {
            if(processing.contains(ref)){
                return true;
            }
            if(!visited.contains(ref)){
                Cell refCell = sheet.get(ref);
                if (refCell != null) {
                    String refValue = ((SCell) refCell).getData();
                    if (hasCycle(refValue, visited, processing)) {
                        return true;
            }


                }
            }
        }
        return false;
    }
}










