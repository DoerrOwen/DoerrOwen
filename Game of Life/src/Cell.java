public class Cell {

    private int neighbors = 0;
    private boolean isLiving = false;

    public int getNeighbors() {
        return neighbors;
    }

    public void setNeighbors(int neighbors) {
        this.neighbors = neighbors;
    }

    public boolean isLiving() {
        return isLiving;
    }

    public void setLiving(boolean living) {
        isLiving = living;
    }

    public Cell() {
        neighbors = 0;
        isLiving = false;
    }

}
