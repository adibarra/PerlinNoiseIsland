import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

//Alec Ibarra
public class ImageLoader {
	
	static BufferedImage splashScreen, dirt, grass, water, guiBack, guiPause, guiCheckedCheckbox, guiCheckbox;
	static BufferedImage effectFire, effectSmoke, player, noTexture, rock, tree;
	
	public ImageLoader()
	{
		try {//load images to use
        	
			player = ImageIO.read(getClass().getClassLoader().getResource("Player.png"));
			rock = ImageIO.read(getClass().getClassLoader().getResource("Rock.png"));
			tree = ImageIO.read(getClass().getClassLoader().getResource("Tree.png"));
			
        	splashScreen = ImageIO.read(getClass().getClassLoader().getResource("SplashScreen.png"));
        	dirt = ImageIO.read(getClass().getClassLoader().getResource("BGDirt.png"));
        	grass = ImageIO.read(getClass().getClassLoader().getResource("BGGrass.png"));
        	water = ImageIO.read(getClass().getClassLoader().getResource("BGWater.png"));
        	
        	guiBack = ImageIO.read(getClass().getClassLoader().getResource("GUIBack.png"));
        	guiPause = ImageIO.read(getClass().getClassLoader().getResource("GUIPause.png"));
        	guiCheckedCheckbox = ImageIO.read(getClass().getClassLoader().getResource("GUICheckedCheckbox.png"));
        	guiCheckbox = ImageIO.read(getClass().getClassLoader().getResource("GUICheckbox.png"));

        	effectFire = ImageIO.read(getClass().getClassLoader().getResource("EffectFire.png"));
        	effectSmoke = ImageIO.read(getClass().getClassLoader().getResource("EffectSmoke.png"));
        	noTexture = ImageIO.read(getClass().getClassLoader().getResource("NoTexture.png"));
        	
        } catch (IOException e) {
        	e.printStackTrace();
        }
	}
	
	public static BufferedImage getImage(String imageName)
	{	
		//Game elements
		if(imageName.equals("dirt"))
			return dirt;
		
		if(imageName.equals("grass"))
			return grass;
		
		if(imageName.equals("water"))
			return water;
		
		if(imageName.equals("objectRock"))
			return rock;
		
		if(imageName.equals("objectTree"))
			return tree;
		
		if(imageName.equals("player"))
			return player;
		
		if(imageName.equals("effectFire"))
			return effectFire;
		
		if(imageName.equals("effectSmoke"))
			return effectSmoke;
		
		//GUI elements
		if(imageName.equals("splashScreen"))
			return splashScreen;
		
		if(imageName.equals("guiBack"))
			return guiBack;
		
		if(imageName.equals("guiPause"))
			return guiPause;
		
		if(imageName.equals("guiCheckedCheckbox"))
			return guiCheckedCheckbox;
		
		if(imageName.equals("guiCheckbox"))
			return guiCheckbox;

		return noTexture;
	}

}
