package clean.names;

import java.util.ArrayList;
import java.util.List;

/**
 * Som samples of clean names
 */
public class GetFlaggedCells {

  public List<int[]> theList;

  /**
   * Dificult to read as names are not meanfull
   */
  public List<int[]> getThem() {
    List<int[]> list1 = new ArrayList<>();
    for (int[] x : theList) {
      if (x[0] == 4) {
        list1.add(x);
      }
    }
    return list1;
  }

  /**
   * Same as getThem but in find minner game context
   */

  public enum CellStatus {
    FLAGGED
  }

  public enum CellField {
    STATUS
  }

  public List<int[]> getFlaggedCells(final List<int[]> gameBoard) {
    List<int[]> flaggedCells = new ArrayList<>();
    for (int[] cell : gameBoard) {
      if (cell[CellField.STATUS.ordinal()] == CellStatus.FLAGGED.ordinal()) {
        flaggedCells.add(cell);
      }
    }
    return flaggedCells;
  }

  /**
   * or even better with a cell class
   */

  public class Cell{
    int[] status;

    public boolean isFlagged(){
      return status[CellField.STATUS.ordinal()] == CellStatus.FLAGGED.ordinal();
    }
  }

  public List<Cell> getFlaggedCellsShorter(final List<Cell> gameBoard) {
    List<Cell> flaggedCells = new ArrayList<>();
    for (Cell cell : gameBoard) {
      if (cell.isFlagged()) {
        flaggedCells.add(cell);
      }
    }
    return flaggedCells;
  }

}
