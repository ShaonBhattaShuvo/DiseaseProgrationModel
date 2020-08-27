/**
 * 
 */
package corona;

import java.util.List;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.engine.watcher.Watch;
import repast.simphony.engine.watcher.WatcherTriggerSchedule;
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
public class Normal {
	private ContinuousSpace<Object> space;
	private Grid<Object> grid;
	public int socialIsolation=40;
	private int immunity, startingImmunity;
	private int age;
	public Normal(ContinuousSpace<Object> space, Grid<Object> grid) {
		this.space = space;
		this.grid  = grid;
		setAge();
		setImmunity();
	}
	
	public void setAge() {
		age = RandomHelper.nextIntFromTo(1, 100); 
	}
	
	public void setImmunity() {
		if(age<10) {
			immunity = startingImmunity = 30;
		}else if(age>=10 && age<=40) {
			immunity = startingImmunity = 100;
		}else{
			immunity = startingImmunity = 50;
		}
	}
	public int getSocialIsolation() {
		return socialIsolation;
	}
	public void setSocialIsolation(int socialIsolation) {
		this.socialIsolation = socialIsolation;
	}
	@ScheduledMethod(start=1, interval =1)
	public void move() {
		if (socialIsolation > 0) {
			GridPoint pt = grid.getLocation(this);
			GridCellNgh nghCreator = new GridCellNgh<Hospital>(grid, pt, Hospital.class, 1, 1);
			List<GridCell<Hospital>> gridCells = nghCreator.getNeighborhood(true);
			SimUtilities.shuffle(gridCells, RandomHelper.getUniform());
			GridPoint pointWithLeastHospitals = null;
			int minCount = Integer.MAX_VALUE;
			for (GridCell<Hospital> cell : gridCells) {
				if (cell.size() < minCount) {
					pointWithLeastHospitals = cell.getPoint();
					minCount = cell.size();
				}
			}
			if (immunity > 0) {
				moveTowards(pointWithLeastHospitals);
			} else {
				immunity = startingImmunity;
			}
		}
	}
	
	public void moveTowards(GridPoint pt) {
		if(!pt.equals(grid.getLocation(this))) {
			NdPoint myPoint  = space.getLocation(this);
			NdPoint otherPoint = new NdPoint(pt.getX(), pt.getY());
			double angle = SpatialMath.calcAngleFor2DMovement(space,myPoint,otherPoint);
			space.moveByVector(this, 1, angle, 0);
			myPoint  = space.getLocation(this);
			grid.moveTo(this, (int)myPoint.getX(), (int)myPoint.getY());
			immunity--;
			socialIsolation--;
		}
		
	}
		

}
