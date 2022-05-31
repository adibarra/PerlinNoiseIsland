import java.awt.Rectangle;
import java.awt.image.BufferedImage;

//Alec Ibarra
public class Entity {
	
	private float XPos = 100;
	private float YPos = 100;
	private float XSpawn = 100;
	private float YSpawn = 100;
	private float XVelocity = 0;
	private float YVelocity = 0;
	private float Friction = .2f;
	private float speed = 1.5f;
	private float maxSpeed = 2.5f;
	private float rotation = 0;
	private int points = 0;
	private int ID = 0;
	private int ownerID = 0;
	private int teamID = 0;
	private int health = 100;
	private int maxHealth = 100;
	private int damage = 100;
	private int money = 0;
	private int currentCooldown = 0;
	private int maxCooldown = 100;
	private String owner = "unknown";
	private String type = "unknown";
	private boolean dynamic = false;
	private boolean neutral = false;
	private boolean friendly = false;
	private boolean hostile = false;
	private BufferedImage icon = ImageLoader.getImage("noTexture");
	private int iconWidth = icon.getWidth();
	private int iconHeight = icon.getHeight();
	private Rectangle hitBox = new Rectangle();
	
	public Entity()
	{
		this.hitBox = new Rectangle((int)XPos,(int)YPos,iconWidth,iconHeight);
	}
	
	public Entity(float XPos, float YPos)
	{
		this.XPos = XPos;
		this.YPos = YPos;
		this.hitBox = new Rectangle((int)XPos,(int)YPos,iconWidth,iconHeight);
	}
	
	public Entity(float XPos, float YPos, String iconName)
	{
		this.XPos = XPos;
		this.YPos = YPos;
		this.icon = ImageLoader.getImage(iconName);
		this.iconWidth = icon.getWidth();
		this.iconHeight = icon.getHeight();
		this.hitBox = new Rectangle((int)XPos,(int)YPos,iconWidth,iconHeight);
	}
	
	public void move(double timeElapsed)
	{	
		speed = (float)Logic.worldScale/16.666f;//16.666 is a constant
		//TODO fix these equations
		if (EntityKeeper.getBackgroundOf(XPos+(iconWidth/2)*Logic.worldScale/iconWidth,
				(YPos+(iconHeight/2)*Logic.worldScale/iconHeight)) == 0)//water
		{
			speed *= .5f;//50% speed decrease in water
		}//TODO fix these equations
		else if (EntityKeeper.getBackgroundOf(XPos+(iconWidth/2)*Logic.worldScale/iconWidth,
				(YPos+(iconHeight/2)*Logic.worldScale/iconHeight)) == 1)//dirt
		{
			speed *= .7f;//30% speed decrease in dirt
		}
		
		if (Logic.left)
		{
			rotation -= 3;
		}
		
		if (Logic.right)
		{
			rotation += 3;
		}

		if (rotation > 360)
		{
			rotation -= 360;
		}
		else if (rotation < 0)
		{
			rotation += 360;
		}
		
		//if (!collisionDetect())
		{
			
			if (Logic.up)
			{
				XPos += speed * timeElapsed * Math.cos(Math.toRadians(rotation-90));
			    YPos += speed * timeElapsed * Math.sin(Math.toRadians(rotation-90));
			}
			
			if (Logic.down)
			{
				XPos -= speed * timeElapsed * Math.cos(Math.toRadians(rotation-90));
			    YPos -= speed * timeElapsed * Math.sin(Math.toRadians(rotation-90));
			}
		}
	}
	
	public void moveAI(double timeElapsed)
	{	
		if(dynamic && neutral)
		{
			int action = (int)(Math.random()*30);//choose action to do
			
			if (action > 10)
			{
				this.setXVelocity(this.getXVelocity() + (int)(Math.random()*10));
				this.setYVelocity(this.getYVelocity() + (int)(Math.random()*10));
			}
			else if (action <= 10)
			{
				int rotDir = (int)(Math.random()*2);//direction to rotate
				if (rotDir == 0)
				{
					this.setRotation(getRotation()+1);	
				}
				else if (rotDir == 1)
				{
					this.setRotation(getRotation()-1);
				}
			}
			
			if (XVelocity > 0)
			{
				XVelocity -= Friction;
			} if (XVelocity < 0) 
			{
				XVelocity += Friction;	
			}
			
			if (YVelocity > 0)
			{
				YVelocity -= Friction;
			} if (YVelocity < 0) 
			{
				YVelocity += Friction;	
			}
			
			XPos += XVelocity;
			YPos += YVelocity;
			
		}
		else if(dynamic && friendly)
		{
			
		}
		else if(dynamic && hostile)
		{
			
		}
	}
	
	public boolean collisionDetect()
	{	
		for(int k = 0; k < EntityKeeper.everything.size(); k++)
		{
			for(int j = 0; j < EntityKeeper.everything.get(k).size(); j++)
			{
				if(this.getHitBox().intersects(((Entity)EntityKeeper.everything.get(k).get(j)).getHitBox())
						                  && !(((Entity)EntityKeeper.everything.get(k).get(j)).getID() == this.getID()))
				{
					EntityKeeper.everything.get(k).remove(j);
					return true;//if current Entity collides with anything (excluding itself) then return true
				}
			}	
		}
		return false;
	}
	
	public void updateHitBox(){
		this.hitBox = new Rectangle((int)XPos,(int)YPos,(int)Logic.worldScale,(int)Logic.worldScale);
	}

	public float getXPos() {
		return XPos;
	}

	public void setXPos(float xPos) {
		XPos = xPos;
	}

	public float getYPos() {
		return YPos;
	}

	public void setYPos(float yPos) {
		YPos = yPos;
	}

	public float getXSpawn() {
		return XSpawn;
	}

	public void setXSpawn(float xSpawn) {
		XSpawn = xSpawn;
	}

	public float getYSpawn() {
		return YSpawn;
	}

	public void setYSpawn(float ySpawn) {
		YSpawn = ySpawn;
	}

	public float getXVelocity() {
		return XVelocity;
	}

	public void setXVelocity(float xVelocity) {
		XVelocity = xVelocity;
	}

	public float getYVelocity() {
		return YVelocity;
	}

	public void setYVelocity(float yVelocity) {
		YVelocity = yVelocity;
	}

	public float getFriction() {
		return Friction;
	}

	public void setFriction(float friction) {
		Friction = friction;
	}

	public float getSpeed() {
		return speed;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}
	
	public float getMaxSpeed() {
		return maxSpeed;
	}

	public void setMaxSpeed(float maxSpeed) {
		this.maxSpeed = maxSpeed;
	}
	
	public float getRotation() {
		return rotation;
	}

	public void setRotation(float rotation) {
		this.rotation = rotation;
	}

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}
	
	public int getOwnerID() {
		return ownerID;
	}

	public void setOwnerID(int ownerID) {
		this.ownerID = ownerID;
	}

	public int getTeamID() {
		return teamID;
	}

	public void setTeamID(int teamID) {
		this.teamID = teamID;
	}

	public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
		this.health = health;
	}

	public int getMaxHealth() {
		return maxHealth;
	}

	public void setMaxHealth(int maxHealth) {
		this.maxHealth = maxHealth;
	}

	public int getDamage() {
		return damage;
	}

	public void setDamage(int damage) {
		this.damage = damage;
	}

	public int getMoney() {
		return money;
	}

	public void setMoney(int money) {
		this.money = money;
	}

	public int getCurrentCooldown() {
		return currentCooldown;
	}

	public void setCurrentCooldown(int currentCooldown) {
		this.currentCooldown = currentCooldown;
	}

	public int getMaxCooldown() {
		return maxCooldown;
	}

	public void setMaxCooldown(int maxCooldown) {
		this.maxCooldown = maxCooldown;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public boolean isDynamic() {
		return dynamic;
	}

	public void setDynamic(boolean dynamic) {
		this.dynamic = dynamic;
	}

	public boolean isNeutral() {
		return neutral;
	}

	public void setNeutral(boolean neutral) {
		this.neutral = neutral;
	}

	public boolean isFriendly() {
		return friendly;
	}

	public void setFriendly(boolean friendly) {
		this.friendly = friendly;
	}

	public boolean isHostile() {
		return hostile;
	}

	public void setHostile(boolean hostile) {
		this.hostile = hostile;
	}

	public BufferedImage getIcon() {
		return icon;
	}

	public void setIcon(BufferedImage icon) {
		this.icon = icon;
	}

	public int getIconWidth() {
		return iconWidth;
	}

	public void setIconWidth(int iconWidth) {
		this.iconWidth = iconWidth;
	}

	public int getIconHeight() {
		return iconHeight;
	}

	public void setIconHeight(int iconHeight) {
		this.iconHeight = iconHeight;
	}

	public Rectangle getHitBox() {
		updateHitBox();
		return hitBox;
	}

	public void setHitBox(Rectangle hitBox) {
		this.hitBox = hitBox;
	}
	
	

}
