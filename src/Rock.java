
//Alec Ibarra
public class Rock extends Entity{
	
	public Rock()
	{
		setDynamic(false);
		setIcon(ImageLoader.getImage("objectRock"));
		updateHitBox();
	}
	
	public Rock(float XPos, float YPos)
	{
		setDynamic(false);
		setXPos(XPos);
		setYPos(YPos);
		setIcon(ImageLoader.getImage("objectRock"));
		updateHitBox();
	}
	
	public Rock(float XPos, float YPos, int ID)
	{
		setDynamic(false);
		setXPos(XPos);
		setYPos(YPos);
		setID(ID);
		setIcon(ImageLoader.getImage("objectRock"));
		updateHitBox();
	}

}
