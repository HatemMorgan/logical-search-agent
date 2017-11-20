import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Random;




public class HelpR2D2 {
	
	private ArrayList<Position> pressurePadsPostions;
	private ArrayList<Position> rocksPostions;
	private ArrayList<Position> obstaclePositions;
	private Position teleportalPos;
	private Position agentPos;
	private  int xDim;
	private int yDim;
	
	public HelpR2D2 (){
		pressurePadsPostions  = new ArrayList<Position>();
		rocksPostions = new ArrayList<Position>();
		obstaclePositions = new ArrayList<Position>();
	}
	
	/**
	 * Creates two files :
	 * 
	 * grid.pl which contains all the facts representing knowledge base of the agent 
	 * query.pl which contains query used to by prolog to find set of actions to reach the goal
	 * 
	 * @throws IOException
	 */
	public void writeLogicalFactsAndQueryToFiles() throws IOException{
		Path path = Paths.get("Logic-Agent/grid.pl");
		
		if(Files.exists(path,LinkOption.NOFOLLOW_LINKS))
			Files.delete(path);
		
		// write knowledge base that represents generated grid in form of facts
		// ex. agent(0,0,s0).  pressurePad(1,1).
		ArrayList<String> kowledgeBaseLines = generateKnowledgeBase();
		Files.write(path, kowledgeBaseLines,Charset.defaultCharset());
		
		path = Paths.get("Logic-Agent/query.pl");
		if(Files.exists(path, LinkOption.NOFOLLOW_LINKS))
			Files.delete(path);
		
		// generate query after GenGrid() is called to make prolog able to logically search to reach the query that represents 
		// the goal
		ArrayList<String> queryLines = generateQuery();
		Files.write(path, queryLines,Charset.defaultCharset());
	}
	
	/**
	 * This​ ​method​ ​is​ ​responsible​ ​for​ ​generating​ ​the​ ​random​ ​grid​ ​of​ ​the​ ​search​ ​problem,  when​ ​initializing​ ​the​ ​search​ ​problem​
	 *  ​the​ ​constructor​ ​call​ ​this​ ​method​ ​to​ ​generate the​ ​grid​ ​randomly.​ ​At​ ​the​ ​beginning​ ​the​ ​dimensions​ ​of​ ​the​ ​2D​ ​grid​ ​is​ 
	 *  ​generated randomly​ ​using​ ​a​ ​random​ ​function​ ​which​ ​generate​ ​a​ ​random​ ​number​ ​between​ ​two preset​ ​integers.​ ​Then​ 
	 *  ​the​ ​number​ ​of​ ​free​ ​spaces,​ ​rocks,​ ​pressure​ ​pads​ ​and obstacles​ ​are​ ​generated​ ​randomly.​ ​Then​ ​the​ ​positions​ ​of​ ​these​ 
	 *  ​different​ ​types​ ​of cells​ ​are​ ​set​ ​randomly​ ​in​ ​the​ ​2D​ ​grid​ ​and​ ​finally​ ​the​ ​2D​ ​grid​ ​of​ ​the​ ​search​ ​problem​ ​is initialized. 
	 */
	public void GenRandomGrid() {

		int m =3;
		int n =3;
		
		xDim = n;
		yDim = m;
		
		GridObjects[][] grid = new GridObjects[n][m];
//		stateSpaceLookup = new TreeNode[n][m];
		int totalCells = (m * n) - 2;
		int Teleportal = 1; // check if correct
		int FreeSpaces = RandomNumber(1, totalCells - 3 + 1);
		totalCells -= FreeSpaces;
		int Pressure = RandomNumber(1, (totalCells - 1) / 2 + 1);
	    int Rocks = Pressure;
//	    RocksNum = Rocks;
		totalCells -= Pressure * 2;
		int Obstacles = RandomNumber(1, totalCells + 1);
		FreeSpaces += totalCells - Obstacles;
		int Agent = 1;

		ArrayList<GridObjects> gridObjects = new ArrayList<GridObjects>();
		gridObjects.add(GridObjects.PressurePad);
		gridObjects.add(GridObjects.FreeSpace);
		gridObjects.add(GridObjects.Teleportal);
		gridObjects.add(GridObjects.Agent);
		gridObjects.add(GridObjects.Rock);
		gridObjects.add(GridObjects.Obstacle);
		
		Position pos;
		
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < m; j++) {
				int Object = RandomNumber(0, gridObjects.size());
				grid[i][j] = gridObjects.get(Object);
				switch (gridObjects.get(Object)) {
				case PressurePad:
					Pressure--;
					pressurePadsPostions.add(new Position(i, j));
					if (Pressure == 0)
						gridObjects.remove(GridObjects.PressurePad);
					
					break;
				
				case FreeSpace:
					FreeSpaces--;
					if (FreeSpaces == 0)
						gridObjects.remove(GridObjects.FreeSpace);
					
					break;
					
				case Rock:
					Rocks--;
					if (Rocks == 0)
						gridObjects.remove(GridObjects.Rock);
					
					rocksPostions.add(new Position(i, j));
					break;
					
				case Obstacle:
					Obstacles--;
					if (Obstacles == 0)
						gridObjects.remove(GridObjects.Obstacle);
					
					obstaclePositions.add(new Position(i, j));
					break;
					
				case Agent:
					Agent--;
					if (Agent == 0) 
						gridObjects.remove(GridObjects.Agent);
					
					agentPos = new Position(i, j);
					break;
				case Teleportal:
					Teleportal--;
					if (Teleportal == 0) 
						gridObjects.remove(GridObjects.Teleportal);
					
					teleportalPos = new Position(i, j);
					break;
				}
			}
		}
	}
	
	private ArrayList<String> generateKnowledgeBase(){
		ArrayList<String> lines = new ArrayList<String>();
		
		lines.add(writeFact(GridObjects.Agent, agentPos));
		lines.add(writeFact(GridObjects.Teleportal, teleportalPos));

		
		for(Position pos : rocksPostions)
			lines.add(writeFact(GridObjects.Rock, pos));
		
		for(Position pos : obstaclePositions)
			lines.add(writeFact(GridObjects.Obstacle, pos));
		
		for(Position pos : pressurePadsPostions)
			lines.add(writeFact(GridObjects.PressurePad, pos));

		lines.add("dim("+xDim+","+yDim+").");
		return lines;
		
	}
	
	private ArrayList<String> generateQuery(){
		ArrayList<String> lines = new ArrayList<String>();
		lines.add(":- [grid].");
		lines.add(":- [main]. \n");
		
		lines.add("search() :- ");
		
		StringBuilder queryBuilder = new StringBuilder();
		// add query for checking that all rocks are on all pressure pads
		// each rock exist  on only one pressure pad
		for(Position pos : pressurePadsPostions){
			queryBuilder.append("				rock("+pos.getX()+","+pos.getY()+",S), \n");
		}
		
		queryBuilder.append("					agent("+teleportalPos.getX()+","+teleportalPos.getY()+",S)");
		
		String query = queryBuilder.toString();
		
		lines.add("solve( \n ("+query+"\n ),1,R).");
		
		return lines;
	}
	
	private int RandomNumber(int low, int high) {
		Random r = new Random();
		return (r.nextInt(high - low) + low);
	}
	
	private String writeFact(GridObjects type, Position pos){
		
		return (type == GridObjects.Rock || type == GridObjects.Agent)?
					type.getFactName()+"("+pos.getX()+","+pos.getY()+","+"s0).":
					type.getFactName()+"("+pos.getX()+","+pos.getY()+").";
	}
	
	/**
	 * Populate rocks positions list, pressurePads positions list, obstacles positions list , set agent positions 
	 * and teleportal positions to be able to generate knowledge base and query that will be used by prolog 
	 * to perform logical searching to find actions needed to reach goal.
	 * 
	 * @param  grid
	 * 		hard coded grid
	 */
	private void genHardCodedGrid(GridObjects[][] grid){
		xDim = grid.length;
		yDim = grid[0].length;
		
		for(int i=0; i<grid.length; i++){
			for(int j=0; j<grid[0].length; j++){
				
				switch (grid[i][j] ) {
				case Agent:  agentPos =  new Position(i, j); break;
				case Obstacle:  	obstaclePositions.add(new Position(i, j)); break;
				case PressurePad:  pressurePadsPostions.add(new Position(i, j)); break;
				case  Rock: rocksPostions.add(new Position(i, j)); break;
				case Teleportal: teleportalPos = new Position(i, j); break;
				default:  break;

				}
					
			}
		}
	}
	
	public static void main(String[] args) throws IOException {
		HelpR2D2 helpR2D2 = new HelpR2D2();
	
//		helpR2D2.GenRandomGrid();
	
//		GridObjects[][] g = new GridObjects[][]{{GridObjects.Agent,GridObjects.Rock,GridObjects.PressurePad},
//				   {GridObjects.FreeSpace,GridObjects.Rock,GridObjects.Obstacle},
//				   {GridObjects.Teleportal,GridObjects.PressurePad,GridObjects.FreeSpace}};		
		
//		GridObjects[][] g = new GridObjects[][]{{GridObjects.Agent,GridObjects.Rock,GridObjects.PressurePad},
//				   {GridObjects.Obstacle,GridObjects.FreeSpace,GridObjects.FreeSpace },
//				   {GridObjects.FreeSpace,GridObjects.Teleportal,GridObjects.FreeSpace}};	
		
//		GridObjects[][] g = new GridObjects[][]{{GridObjects.FreeSpace,GridObjects.Teleportal,GridObjects.Obstacle},
//				   {GridObjects.PressurePad,GridObjects.Rock,GridObjects.Agent }};
					
		GridObjects[][] g = new GridObjects[][]{{GridObjects.Agent,GridObjects.Rock,GridObjects.PressurePad},
				   {GridObjects.Obstacle,GridObjects.Teleportal,GridObjects.FreeSpace }};
		
		helpR2D2.genHardCodedGrid(g);
		
		
		// write logical facts and query into files
		helpR2D2.writeLogicalFactsAndQueryToFiles();

	}
}
