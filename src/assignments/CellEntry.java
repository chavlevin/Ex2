package assignments;
// Add your documentation below:

public class CellEntry  implements Index2D {
    private final int x;
    private final int y;

    public CellEntry(int x, int y){
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean isValid() {
        return false;
    }

    @Override
    public int getX() {return Ex2Utils.ERR;}

    @Override
    public int getY() {return Ex2Utils.ERR;}
}
