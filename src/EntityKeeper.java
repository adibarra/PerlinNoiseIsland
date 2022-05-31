import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Area;
import java.util.ArrayList;
import java.util.Random;

//Alec Ibarra
public class EntityKeeper {
	
	@SuppressWarnings("rawtypes")
	static ArrayList<ArrayList> everything = new ArrayList<ArrayList>();
	static ArrayList<Player> players = new ArrayList<Player>();
	static ArrayList<Entity> objects = new ArrayList<Entity>();
	static ArrayList<Point> objectPoints = new ArrayList<Point>();
	
	static Area noZone = new Area();
	static Rectangle bigScreen = new Rectangle(-100,-100,1100,800);
	static Rectangle screen = new Rectangle(0,21,990,650);
	static int height = (Logic.screenSizeY-21)/Logic.worldScale;
	static int width = Logic.screenSizeX/Logic.worldScale;
	static int[][] world = new int[width][height];
	static int seed = (int) (Math.random() * 999999);
	static Random r = new Random(seed);
	static int objectsToPlace = 40;

	
	static Rectangle tempRec = new Rectangle();
	static Area tempArea = new Area();
	//static Wall tempWall = new Wall();
	
	public static void prepare()
	{
		everything.add(objects);
		everything.add(players);
	}

	public static void mapGen()
	{
		height = (Logic.screenSizeY-21)/Logic.worldScale;
		width = Logic.screenSizeX/Logic.worldScale;
		world = new int[width][height];
		
		// Setup points in the 4 corners of the map.
		world[0][0] = 128;
		world[width-1][0] = 128;
		world[width-1][height-1] = 128;
		world[0][height-1] = 128;
		// Do the midpoint
		midpoint(0,0,width-1,height-1);
	}

	public static boolean midpoint(int x1,int y1, int x2, int y2 ){
		//  If this is pointing at just on pixel, Exit because
		//  it doesn't need doing
		if(x2-x1<2 && y2-y1<2) return false;

		// Find distance between points and
		// use when generating a random number.
		int dist=(x2-x1+y2-y1);
		int hdist=dist / 2;
		// Find Middle Point
		int midx=(x1+x2) / 2;
		int midy=(y1+y2) / 2;
		// Get pixel colors of corners
		int c1=world[x1][y1];
		int c2=world[x2][y1];
		int c3=world[x2][y2];
		int c4=world[x1][y2];

		// If Not already defined, work out the midpoints of the corners of
		// the rectangle by means of an average plus a random number.
		if(world[midx][y1]==0) world[midx][y1]=((c1+c2+r.nextInt(dist)-hdist) / 2);
		if(world[midx][y2]==0) world[midx][y2]=((c4+c3+r.nextInt(dist)-hdist) / 2);
		if(world[x1][midy]==0) world[x1][midy]=((c1+c4+r.nextInt(dist)-hdist) / 2);
		if(world[x2][midy]==0) world[x2][midy]=((c2+c3+r.nextInt(dist)-hdist) / 2);

		// Work out the middle point...
		world[midx][midy] = ((c1+c2+c3+c4+r.nextInt(dist)-hdist) / 4);

		// Now divide this rectangle into 4, And call again For Each smaller
		// rectangle
		midpoint(x1,y1,midx,midy);
		midpoint(midx,y1,x2,midy);
		midpoint(x1,midy,midx,y2);
		midpoint(midx,midy,x2,y2);

		return true;
	}
	
	public static void generateWorld()
	{
		mapGen();
		
		for(int k = 0; k < EntityKeeper.height; k++)//rows
			for(int j = 0; j < EntityKeeper.width; j++)//columns
				if(world[j][k] <= 123)//water
				{
					world[j][k] = 0;
				}
				else if(world[j][k] > 123 && world[j][k] <= 126)//dirt
				{
					world[j][k] = 1;
				}
				else if(world[j][k] >= 126)//grass
				{
					world[j][k] = 2;
				}
		//TODO make new method for keeping track of object positions(base it on background "block" locations)
		objectPoints.clear();
		objects.clear();
		boolean playerPlaced = false;
		int counter = 0;
        for(int k = 0; k < objectsToPlace; k++)
        {
        	int type = (int)(Math.random()*2);
        	int x = (int)((Math.random()*(width-2))+1)*Logic.worldScale;
        	int y = ((int)((Math.random()*(height-2))+1)*Logic.worldScale)+21;
        	int treeChance = 0;
        	int rockChance = 0;
        	
        	if(world[x/Logic.worldScale][y/Logic.worldScale] == 1)//dirt
        	{
        		treeChance = 8;//lower is better
        		rockChance = 2;//lower is better
        	}
        	else if (world[x/Logic.worldScale][y/Logic.worldScale] == 2)//grass
        	{
        		treeChance = 2;//lower is better
        		rockChance = 8;//lower is better
        	}
        	        	
        	int treeChance2 = (int)(Math.random()*treeChance);
        	int rockChance2 = (int)(Math.random()*rockChance);
        	
        	if(!objectPoints.contains(new Point(x,y)) && world[x/Logic.worldScale][(y/Logic.worldScale)] != 0)
        	{
        		if(!playerPlaced)
        		{
        			objectPoints.add(new Point((int)Logic.player.getXPos(),(int)Logic.player.getYPos()));
        			Logic.player.setXPos(x);
        			Logic.player.setYPos(y);
        			playerPlaced = true;
        			k--;
        		}
        		else
        		{
        			objectPoints.add(new Point(x,y));
        			
        			if (type % 2 == 0 && treeChance2 == 0)
        				objects.add(new Tree(x,y));
        			else if(rockChance2 == 0)
        				objects.add(new Rock(x,y));
        			else 
        				k--;
        		}
        	}
        	else
        	{
        		k--;
        		counter++;
        		if (counter > 2000)
        		{
        			break;
        		}
        	}
        }
	}
	
	public static void reloadWorld()
	{
		mapGen();
	}
	
	public static int getBackgroundOf(double XPos,double YPos)
	{	
		return world[(int)Math.floor((Math.floor(XPos)/Logic.worldScale)-1)][(int)Math.floor((Math.floor(YPos)/Logic.worldScale)-1)];
	}
	
	public static void buildNoZone()//call whenever a wall is placed
	{
		noZone = new Area();
		
		//creates barrier around screen
		tempArea = new Area(bigScreen);
		noZone.add(tempArea);
		tempArea = new Area(screen);
		noZone.subtract(tempArea);
		
		/*//adds walls to nozone
		for (int k = 0; k < EntityKeeper.walls.size(); k++)
        {
			tempWall.clone(EntityKeeper.walls.get(k));
			tempRec = tempWall.getBoundingBox();
			tempArea = new Area(tempRec);
			noZone.add(tempArea);
        }
        */
	}

}
