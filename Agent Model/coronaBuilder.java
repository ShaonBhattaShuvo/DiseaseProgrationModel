/**
 * 
 */
package corona;

import repast.simphony.context.Context;
import repast.simphony.context.space.continuous.ContinuousSpaceFactory;
import repast.simphony.context.space.continuous.ContinuousSpaceFactoryFinder;
import repast.simphony.context.space.graph.NetworkBuilder;
import repast.simphony.context.space.grid.GridFactory;
import repast.simphony.context.space.grid.GridFactoryFinder;
import repast.simphony.dataLoader.ContextBuilder;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.parameter.Parameters;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.NdPoint;
import repast.simphony.space.continuous.RandomCartesianAdder;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridBuilderParameters;
import repast.simphony.space.grid.SimpleGridAdder;
import repast.simphony.space.grid.WrapAroundBorders;

/**
 * @author Shaon Bhatta Shuvo
 *
 */
public class coronaBuilder implements ContextBuilder<Object> {
	@Override
	public Context build(Context<Object> context) {
		NetworkBuilder<Object> netBuilder = new NetworkBuilder<>("infection network", context, true);
		netBuilder.buildNetwork();
		context.setId("corona");
		ContinuousSpaceFactory spaceFactory = ContinuousSpaceFactoryFinder.createContinuousSpaceFactory(null);
		ContinuousSpace<Object> space = spaceFactory.createContinuousSpace("space", context, 
				new RandomCartesianAdder<Object>(), 
				new repast.simphony.space.continuous.WrapAroundBorders(), 50,50);
		
		GridFactory gridFactory = GridFactoryFinder.createGridFactory(null);
		Grid<Object> grid  = gridFactory.createGrid("grid", context, 
				new GridBuilderParameters<Object>( new WrapAroundBorders(),
						new SimpleGridAdder<Object>(),
						true, 50,50));
		
		Parameters params = RunEnvironment.getInstance().getParameters();
		int normalCount = (Integer)params.getValue("normal_count");
		for(int i = 0; i<normalCount; i++) {
			context.add(new Normal(space,grid));
		}
		
		int infectedCount = (Integer)params.getValue("infected_count");
		for(int i=0;i<infectedCount;i++) {
			context.add(new Infected(space,grid));
		}
		
		int hospitalCount = (Integer)params.getValue("hospital_count");
		for(int i=0;i<hospitalCount;i++) {
			context.add(new Hospital(space,grid));
		}
		
		for(Object obj : context) {
			NdPoint nt = space.getLocation(obj);
			grid.moveTo(obj, (int)nt.getX(), (int)nt.getY());
		}
		return context;
	}
}
