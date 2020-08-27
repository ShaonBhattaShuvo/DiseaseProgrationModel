/**
 * 
 */
package corona;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.grid.Grid;
/**
 * @author Shaon Bhatta Shuvo
 *
 */
public class Dead {
	private ContinuousSpace<Object> space;
	private Grid<Object> grid;
	public Dead(ContinuousSpace<Object> space, Grid<Object> grid) {
		this.space = space;
		this.grid  = grid;
	}
	public ContinuousSpace<Object> getSpace() {
		return space;
	}
	public void setSpace(ContinuousSpace<Object> space) {
		this.space = space;
	}
	public Grid<Object> getGrid() {
		return grid;
	}
	public void setGrid(Grid<Object> grid) {
		this.grid = grid;
	}
	
}
