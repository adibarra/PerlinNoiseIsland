
//Alec Ibarra
public class Tree extends Entity{
	
	public Tree()
	{
		setDynamic(false);
		setIcon(ImageLoader.getImage("objectTree"));
		updateHitBox();
	}
	
	public Tree(float XPos, float YPos)
	{
		setDynamic(false);
		setXPos(XPos);
		setYPos(YPos);
		setIcon(ImageLoader.getImage("objectTree"));
		updateHitBox();
	}
	
	public Tree(float XPos, float YPos, int ID)
	{
		setDynamic(false);
		setXPos(XPos);
		setYPos(YPos);
		setID(ID);
		setIcon(ImageLoader.getImage("objectTree"));
		updateHitBox();
	}

}
