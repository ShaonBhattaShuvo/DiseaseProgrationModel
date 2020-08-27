/**
 * 
 */
package corona;

import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.grid.Grid;

/**
 * @author Shaon
 *
 */
public class Hospital {
	private ContinuousSpace<Object> space;
	private Grid<Object> grid;
		public Hospital(ContinuousSpace<Object> space, Grid<Object> grid) {
		this.space = space;
		this.grid = grid; 
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
