package assignments;


import java.io.*;
import java.util.ArrayList;
import java.util.List;
// Add your documentation below:

public class Ex2Sheet implements Sheet {
    private final int rows;
    private final int cols;
    private String[][] data;
    private int[][] order;
    private Cell[][] table;





    public Ex2Sheet(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.data = new String [rows][cols];
        this.order = new int[rows][cols];
        this.table = new Cell[rows][cols];

        for(int r=0;r<rows;r++) {
            for(int c=0;c<cols;c++) {
                data[r][c] = Ex2Utils.EMPTY_CELL;
                order[r][c] = 0;
                table[r][c] = new SCell(Ex2Utils.EMPTY_CELL, this);
            }
        }
    }

    public Ex2Sheet() {
        this(Ex2Utils.WIDTH, Ex2Utils.HEIGHT);
    }
    public void setCellContent(String position, String content) {
        int[] rc = fromCellName(position);
        if(!isIn(rc[0],rc[1])){
            throw new IllegalArgumentException("Invalid cell position");
        }
        data[rc[0]][rc[1]] = content;
        table[rc[0]][rc[1]] = new SCell(content, this);
        eval();
    }

    public String getCellContent(String position) {
        int[] rc = fromCellName(position);
        Cell cell = table[rc[0]][rc[1]];

        if (cell instanceof SCell) {
            return ((SCell) cell).getValue();

    }
    return Ex2Utils.EMPTY_CELL;
    }

    private int[] fromCellName(String pos){
        int col = pos.toUpperCase().charAt(0)-'A';
        int row = Integer.parseInt(pos.substring(1))-1;
        return new int[]{row,col};
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
        if(x>=0 && x<rows && y>=0 && y<cols){
            return table[x][y];
        }
        return null;
    }

    @Override
    public Cell get(String cords) {

        int x = getXFromReference(cords);
        int y = getYFromReference(cords);

        if(isIn(x,y)){
            return get(x,y);
        }
        return null;
    }

    @Override
    public int width() {
        return rows;
    }
    @Override
    public int height() {
        return cols;
    }

    @Override
    public void set(int x, int y, String s) {
        table[x][y] = new SCell(s, this);
        eval();
    }



    @Override
    public void eval() {
        int[][] depDepth = depth();
        for(int d = 0;d<=rows * cols;d++){
        for(int r=0;r<rows;r++) {
            for (int c = 0; c < cols; c++) {
                if (depDepth[r][c] == d) {
                    Cell cell = get(r, c);
                    if (cell instanceof SCell && MyCell.isForm(cell.getData())) {
                        try {
                            ((SCell) cell).evaluateForm(new ArrayList<>(), new ArrayList<>());
                        }catch(Exception e){
                            cell.setData(Ex2Utils.ERR_FORM);
                        }
                    }
                }
            }
        }
            }
        }



    @Override
    public boolean isIn(int xx, int yy) {
        boolean ans = xx>=0 && yy>=0 && xx<rows && yy<cols;

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
        int[][] depth = new int[rows][cols];

        for(int x = 0; x<rows;x++){
            for(int y = 0;y<cols;y++){
                depth[x][y] = -1;
            }
        }
        boolean updated;
        int currentDepth = 0;

        do {
            updated = false;
            for (int x = 0; x < rows; x++) {
                for (int y = 0; y < cols; y++) {
                    if (depth[x][y] == -1){
                        List<String> visited = new ArrayList<>();
                    if (canCompute(x, y, depth,visited)) {
                        depth[x][y] = currentDepth;
                        updated = true;
                    }
                    }
                }
            }
            currentDepth++;
        }while(updated);
        return depth;
        }



    private boolean canCompute(int x, int y, int [][]depth, List<String> visited){
        Cell cell = get(x, y);

        if (cell == null || !MyCell.isForm(cell.getData())) {
            return true;
        }

        String formula = cell.getData();

        for(String ref: getReferences(formula)){
            if (visited.contains(ref)) {
                return false;
            }
            int refX = getXFromReference(ref);
            int refY = getYFromReference(ref);

            if(!isIn(refX,refY) || depth[refX][refY] ==-1){
                    return false;
                }
            }
        visited.add(cell.getData());
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
                String reference = ref.toString();
                if(getXFromReference(reference)<0 || getYFromReference(reference)<0){
                    throw new IllegalArgumentException("Invalid cell reference");
                }
                references.add(reference);
            }else {
                index++;
            }
            }
        return references;
        }




private int getXFromReference(String ref) {
    int column = 0;
    int i = 0;

    while (i < ref.length() && Character.isLetter(ref.charAt(i))) {
        column = column * 26 + (Character.toUpperCase(ref.charAt(i)) - 'A' + 1);
        i++;
    }
    return column - 1;
}

private int getYFromReference(String ref){
        return Integer.parseInt(ref.replaceAll("[^0-9]", "")) -1;
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

        try {
            for (int x = 0; x < rows; x++) {
                for (int y = 0; y < cols; y++) {
                    Cell cell = get(x, y);
                    if (cell != null) {
                        bufferedWriter.write(cell.getData());
                    }
                    if (y < cols - 1) {
                        bufferedWriter.write(",");
                    }
                }
                bufferedWriter.newLine();
            }
            } finally{
                bufferedWriter.close();
            }
        }




    @Override
    public String eval(int x, int y) {
        Cell cell = get(x,y);
        if(cell!=null){
            String data = cell.getData();
            if(MyCell.isForm(data)) {
                try{
                    return MyCell.computeWithError("=" +data);
                }catch(Exception e){
                    return Ex2Utils.EMPTY_CELL;
                }
            }else{
                return data;
            }
        }else{
            return Ex2Utils.EMPTY_CELL;

        }

        }
}
