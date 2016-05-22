package guimodule;

import processing.core.PApplet;

public class myDisplay extends PApplet{

	public void setup()
	{
		size(400,600);
		background(200,200,150);
	}
	
	public void draw() 
	{
		fill(230, 200, 0);
		ellipse(200, 200, 390, 390);
		fill (0, 0, 0);
		ellipse(120, 130, 50, 70);
		ellipse(280, 130, 50, 70);
		noFill();
		arc(200,280, 75, 75, 0, PI);
		
		//noFill();
	}
}
