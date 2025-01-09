package assignments;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
// Add your documentation below:

public class Ex2Sheet implements Sheet {
    private Cell[][] table;

    public Ex2Sheet(int x, int y) {
        table = new SCell[x][y];
        for(int i=0;i<x;i=i+1) {
            for(int j=0;j<y;j=j+1) {
                table[i][j] = new SCell(Ex2Utils.EMPTY_CELL);
            }
        }
        eval();
    }
    public Ex2Sheet() {
        this(Ex2Utils.WIDTH, Ex2Utils.HEIGHT);
    }


    @Override
    public String value(int x, int y) {
        String ans = Ex2Utils.EMPTY_CELL;

        Cell c = get(x,y);
        if(c!=null) {
            ans = c.toString();
        }
        return ans;
    }

    @Override
    public Cell get(int x, int y) {
        return table[x][y];
    }

    @Override
    public Cell get(String cords) {
        Cell ans = null;

        int x = getXFromReference(cords);
        int y = getYFromReference(cords);

        if(isIn(x,y)){
            return get(x,y);
        }
        return ans;
    }

    @Override
    public int width() {
        return table.length;
    }
    @Override
    public int height() {
        return table[0].length;
    }
    @Override
    public void set(int x, int y, String s) {
        Cell c = new SCell(s);
        table[x][y] = c;

        if(MyCell.isForm(s)){
            String computedVal = MyCell.computeWithError(s);
            if(computedVal != null){
                c.setData(computedVal);
            }
        }
        eval();
    }


    @Override
    public void eval() {
        int[][] dd = depth();

        for(int x=0; x<width();x++){
            for(int y=0;y<height();y++){
                Cell cell = get(x,y);

                if(cell !=null && MyCell.isForm(cell.getData())){
                    String formula = cell.getData();
                    String result = MyCell.computeWithError(formula);

                   cell.setData(result);
                    }
                }
                    }
                }



    @Override
    public boolean isIn(int xx, int yy) {
        boolean ans = xx>=0 && yy>=0 && xx<width() && yy<height();

        if(ans){
            Cell cell = get(xx,yy);
            if(cell==null){
                ans = false;
            }
        }
        return ans;
    }

    @Override
    public int[][] depth() {
        int[][] depth = new int[width()][height()];

        for (int i = 0; i < width(); i++) {
            for (int j = 0; j < height(); j++) {
                depth[i][j] = -1;
            }
        }
        int currentDepth = 0;
        boolean anyUpdated = true;
        int cellsProcessed = 0;
        int totalCells = width() * height();

        while (cellsProcessed < totalCells && anyUpdated) {
            anyUpdated = false;


            for (int x = 0; x < width(); x++) {
                for (int y = 0; y < height(); y++) {
                    if (depth[x][y] == -1 && canCompute(x, y, depth)) {
                        depth[x][y] = currentDepth;
                        cellsProcessed++;
                        anyUpdated = true;
                    }
                }

            }
            currentDepth++;
        }
        return depth;
    }

    private boolean canCompute(int x, int y, int [][]depth){
        Cell cell = get(x, y);

        if(cell!=null && MyCell.isForm(cell.getData())){
            String formula = cell.getData();

            for(String ref: getReferences(formula)){
                int refX = getXFromReference(ref);
                int refY = getYFromReference(ref);

                if(depth[refX][refY] ==-1){
                    return false;
                }
            }
        }
        return true;
        }

private List<String> getReferences(String formula){
        List<String> references = new ArrayList<>();

        int index = 0;
        while(index<formula.length()){
            if(Character.isLetter(formula.charAt(index))) {
                StringBuilder ref = new StringBuilder();
                while (index < formula.length() && (Character.isLetter(formula.charAt(index)) || Character.isDigit(formula.charAt(index)))) {
                    ref.append(formula.charAt(index));
                    index++;
                }
                references.add(ref.toString());
            }else {
                index++;
            }
            }
        return references;
        }
private int getXFromReference(String ref){
        int column = 0;
        for(int i = 0; i<ref.length();i++){
            column = column * 26 +(ref.charAt(i) - 'A' + 1);
        }
        return column - 1;
}
private int getYFromReference(String ref){
        return Integer.parseInt(ref.substring(1))-1;
}

    @Override
    public void load(String fileName) throws IOException {
        FileReader fileReader = new FileReader(fileName);
        BufferedReader reader = new BufferedReader(fileReader);

        String line = reader.readLine();
        int row = 0;

        while(line != null){
            String[] cells = line.split(",");

            for(int column = 0; column< cells.length; column++){
                set(row,column,cells[column]);
            }
            line = reader.readLine();
            row++;
        }
        reader.close();
    }

    @Override
    public void save(String fileName) throws IOException {
        FileWriter fileWriter = new FileWriter(fileName);
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

        try{
            for(int x = 0; x<width();x++){
                for(int y = 0;y<height();y++){
                    Cell cell = get(x,y);
                    if(cell!=null){
                        bufferedWriter.write(cell.getData());
                    }
                    bufferedWriter.write("\t");
                }
                bufferedWriter.newLine();

            }
        }catch (IOException exception){
            System.out.println("Error saving the file: " + exception.getMessage());
            throw exception;
        }finally {
            bufferedWriter.close();
        }

    }

    @Override
    public String eval(int x, int y) {
        String ans = null;
        if(get(x,y)!=null) {
            ans = get(x,y).toString();
        }
        if(ans !=null && MyCell.isForm(ans)){
            ans = MyCell.computeWithError(ans);
        }
        return ans;
        }
}
