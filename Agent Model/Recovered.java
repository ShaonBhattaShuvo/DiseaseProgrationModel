/**
 * 
 */
package corona;

import java.util.List;

import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.query.space.grid.GridCell;
import repast.simphony.query.space.grid.GridCellNgh;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.SpatialMath;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.NdPoint;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;
import repast.simphony.util.SimUtilities;

/**
 * @author Shaon Bhatta Shuvo
 *
 */
public class Recovered {
	private ContinuousSpace<Object> space;
	private Grid<Object> grid;
	private boolean moved;
	public int socialIsolation=10;
	//private int immunity, startingImmunity;
	public Recovered(ContinuousSpace<Object> space, Grid<Object> grid) {
		this.space = space;
		this.grid = grid; 
	}
	public int getSocialIsolation() {
		return socialIsolation;
	}
	public void setSocialIsolation(int socialIsolation) {
		this.socialIsolation = socialIsolation;
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
	public boolean isMoved() {
		return moved;
	}
	public void setMoved(boolean moved) {
		this.moved = moved;
	}
	@ScheduledMethod(start=1, interval =1)
	public void step() {
		if (socialIsolation > 0) {
			GridPoint pt = grid.getLocation(this);
			GridCellNgh<Normal> nghCreator = new GridCellNgh<Normal>(grid, pt, Normal.class, 1, 1);
			List<GridCell<Normal>> gridCells = nghCreator.getNeighborhood(true);
			SimUtilities.shuffle(gridCells, RandomHelper.getUniform());
			GridPoint pointWithMostNormals = null;
			int maxCount = -1;
			for (GridCell<Normal> cell : gridCells) {
				if (cell.size() > maxCount) {
					pointWithMostNormals = cell.getPoint();
					maxCount = cell.size();
				}
			}
			moveTowards(pointWithMostNormals);
		}
	}
	public void moveTowards(GridPoint pt) {
		if(!pt.equals(grid.getLocation(this))) {
			NdPoint myPoint = space.getLocation(this);
			NdPoint otherPoint = new NdPoint(pt.getX(),pt.getY());
			double angle = SpatialMath.calcAngleFor2DMovement(space, myPoint, otherPoint);
			space.moveByVector(this, 1, angle, 0);
			myPoint = space.getLocation(this);
			grid.moveTo(this, (int)myPoint.getX(), (int)myPoint.getY());
			moved = true;
		}
	}
}
