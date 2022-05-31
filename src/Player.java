
//Alec Ibarra
public class Player extends Entity{
	
	public Player()
	{
		setDynamic(true);
		setIcon(ImageLoader.getImage("player"));
		updateHitBox();
	}
	
	public Player(float XPos, float YPos)
	{
		setDynamic(true);
		setXPos(XPos);
		setYPos(YPos);
		setIcon(ImageLoader.getImage("player"));
		updateHitBox();
	}
	
	public Player(float XPos, float YPos, int ID)
	{
		setDynamic(true);
		setXPos(XPos);
		setYPos(YPos);
		setID(ID);
		setIcon(ImageLoader.getImage("player"));
		updateHitBox();
	}
	
	public Player(float XPos, float YPos, int ID, int teamID)
	{
		setDynamic(true);
		setXPos(XPos);
		setYPos(YPos);
		setID(ID);
		setTeamID(teamID);
		setIcon(ImageLoader.getImage("player"));
		updateHitBox();
	}

}

