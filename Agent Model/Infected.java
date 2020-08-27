/**
 * 
 */
package corona;

import java.util.ArrayList;
import java.util.List;

import repast.simphony.context.Context;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.query.space.grid.GridCell;
import repast.simphony.query.space.grid.GridCellNgh;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.SpatialMath;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.NdPoint;
import repast.simphony.space.graph.Network;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;
import repast.simphony.util.ContextUtils;
import repast.simphony.util.SimUtilities;

/**
 * @author Shaon Bhatta Shuvo
 *
 */
public class Infected {
	private ContinuousSpace<Object> space;
	private Grid<Object> grid;
	private int socialIsolation=40;
	private int infectionPeriod = 30;
	private int countDays = 0;
	private boolean moved;
	private int immunity;
	private int age;
	public Infected(ContinuousSpace<Object> space, Grid<Object> grid) {
		this.space = space;
		this.grid = grid; 
		this.setAge();
		this.setImmunity();
	}
	public void setAge() {
		this.age = RandomHelper.nextIntFromTo(1, 100); 
	}
	public void setImmunity() {
		if(age<=10) {
			this.immunity = 100;
		}else if(age>=11 && age<=40) {
			this.immunity = 80;
		}else if(age>=41 && age<=50){
			this.immunity = 70;
		}else if(age>=51 && age<=60){
			this.immunity = 55;
		}else if(age>=61 && age<=80){
			this.immunity = 45;
		}else {
			this.immunity = 35;
		}
	}
	public int getSocialIsolation() {
		return socialIsolation;
	}
	public void setSocialIsolation(int socialIsolation) {
		this.socialIsolation = socialIsolation;
	}
	public int getCountDays() {
		return countDays;
	}
	public void setCountDays(int countDays) {
		this.countDays = countDays;
	}
	
	@ScheduledMethod(start=1, interval =1)
	public void step() {
		if (socialIsolation < 0) {
			if (immunity<=0) {
				death();
			}else {
				if(countDays>=infectionPeriod){
					GridPoint pt = grid.getLocation(this);
					NdPoint spacePt = space.getLocation(this);
					Context<Object> context = ContextUtils.getContext(this);
					context.remove(this);
					Recovered recovered = new Recovered(space, grid);
					context.add(recovered);
					space.moveTo(recovered, spacePt.getX(), spacePt.getY());
					grid.moveTo(recovered, pt.getX(), pt.getY());	
				}
			}
			countDays++;
			immunity--;
		} else {
			GridPoint pt = grid.getLocation(this);
			GridCellNgh<Hospital> nghCreator = new GridCellNgh<Hospital>(grid, pt, Hospital.class, 1, 1);
			List<GridCell<Hospital>> gridCells = nghCreator.getNeighborhood(true);
			SimUtilities.shuffle(gridCells, RandomHelper.getUniform());
			GridPoint pointWithMostHospitals = null;
			int maxCount = -1;
			for (GridCell<Hospital> cell : gridCells) {
				if (cell.size() > maxCount) {
					pointWithMostHospitals = cell.getPoint();
					maxCount = cell.size();
				}
			}
			moveTowards(pointWithMostHospitals);
			infect();
			recovery();
			if (immunity < 0) {
				death();
			}

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
			immunity--;
			socialIsolation--;
		}
	}
	
	public void infect() {
		GridPoint pt = grid.getLocation(this);
		List<Object> normals = new ArrayList<>();
		for(Object obj : grid.getObjectsAt(pt.getX(), pt.getY())) {
			if(obj instanceof Normal) {
				normals.add(obj);
			}
		}
		if(normals.size()>0) {
			int index = RandomHelper.nextIntFromTo(0, normals.size()-1);
			Object obj = normals.get(index);
			NdPoint spacePt = space.getLocation(obj);
			Context<Object> context = ContextUtils.getContext(obj);
			context.remove(obj);
		    Infected infected = new Infected(space,grid);
			context.add(infected);
			space.moveTo(infected, spacePt.getX(), spacePt.getY());
			grid.moveTo(infected, pt.getX(), pt.getY());
			
//			Network<Object> net = (Network<Object>) context.getProjection("infection network");
//			net.addEdge(this, infected);
		}
	}

	public void recovery() {
		GridPoint pt1 = grid.getLocation(this);
		List<Object> hospitals = new ArrayList<>();
		for (Object obj : grid.getObjectsAt(pt1.getX(), pt1.getY())) {
			if (obj instanceof Hospital) {
				hospitals.add(obj);
			}
		}
		if (hospitals.size() > 0) {
			GridPoint pt = grid.getLocation(this);
			NdPoint spacePt = space.getLocation(this);
			Context<Object> context = ContextUtils.getContext(this);
			context.remove(this);
			Recovered recovered = new Recovered(space, grid);
			context.add(recovered);
			space.moveTo(recovered, spacePt.getX(), spacePt.getY());
			grid.moveTo(recovered, pt.getX(), pt.getY());

//			Network<Object> net = (Network<Object>) context.getProjection("recovery network");
//			net.addEdge(this, recovered);

		}
	}

	public void death() {
		GridPoint pt = grid.getLocation(this);
		NdPoint spacePt = space.getLocation(this);
		Context<Object> context = ContextUtils.getContext(this);
		context.remove(this);
		Dead dead = new Dead(space, grid);
		context.add(dead);
		space.moveTo(dead, spacePt.getX(), spacePt.getY());
		grid.moveTo(dead, pt.getX(), pt.getY());

//		Network<Object> net = (Network<Object>) context.getProjection("death network");
//		net.addEdge(this, dead);

	}
}
