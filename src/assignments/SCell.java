package assignments;// Add your documentation below:

public class SCell implements Cell {
    private String line;
    private int type;
    public static final int TYPE_NUMBER = 0;
    public static final int TYPE_TEXT = 1;
    public static final int TYPE_FORMULA = 2;

    public SCell(String s) {
        // Add your code here
        setData(s);
    }

    @Override
    public int getOrder() {
        // Add your code here

        return 0;
        // ///////////////////
    }

    //@Override
    @Override
    public String toString() {
        return getData();
    }

    @Override
public void setData(String s) {
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
