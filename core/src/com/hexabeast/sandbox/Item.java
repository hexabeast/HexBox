package com.hexabeast.sandbox;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class Item extends Entity {
	
	private Vector2 velocity = new Vector2();
	private float speed = 2 * 60, gravity = 200*6f;
	private float time = 0;
	public float life = 300;
	public int id;
	public Sprite spr;
	public int number = 1;
	
	public Item(TextureRegion region,int idd, float x, float y) {
		spr = new Sprite(region);
		isDead = false;
		id = idd;
		spr.setSize(32, 32);
		spr.setScale(0.7f);
		setX(x-spr.getWidth()/2);
		setY(y-spr.getHeight()/2);
	}

	public void update(float delta)
	{
		time+=delta;
		if(time>life){
			isDead = true;
		}
		if(delta>0.05f)delta = 0.05f;
		
		
		float oldY = getY(), tileWidth = Map.instance.mainLayer.getTileWidth(), tileHeight = Map.instance.mainLayer.getTileHeight();
		
		float xDistance = getX()-(GameScreen.player.PNJ.x+GameScreen.player.PNJ.width/2);
		float yDistance = getY()-(GameScreen.player.PNJ.y+GameScreen.player.PNJ.height/2);
		
		int xOffset;
		if(xDistance>=0)xOffset = 60;
		else xOffset = -60;
		
		int yOffset;
		if(yDistance>=0)yOffset = 60;
		else yOffset = -60;
		
		if(Math.abs(xDistance)<100 && Math.abs(yDistance)<100  && !GameScreen.inventory.isFull(id))
		{
			if(Math.abs(xDistance)<15 && Math.abs(yDistance)<38 && !GameScreen.inventory.isTransit && number>0)
			{
				SoundManager.instance.PickUp.play();
				GameScreen.inventory.PutItem(id);
			    Tools.checkItems();
			    number--;
				while(number>0 && !GameScreen.inventory.isFull(id))
				{
					GameScreen.inventory.PutItem(id);
				    Tools.checkItems();
				    number--;    
				}	
			}
			velocity = new Vector2(velocity.x-(1/(xDistance+xOffset))*300*300*delta,velocity.y-(1/(yDistance+yOffset))*200*300*delta);
			velocity.x*=1-1f*delta;
			velocity.y*=1-1f*delta;
			setX(getX()+velocity.x * delta);
			setY(getY()+velocity.y * delta);
		}
		else
		{
			velocity.y -= gravity*delta;
			velocity.x *= 0.9f;
			if(velocity.y>speed*2)
				velocity.y = speed*2;
			else if(velocity.y<-speed*2)
				velocity.y = -speed*2;
			
			setX(getX()+velocity.x * delta);
			setY(getY()+velocity.y * delta);
			
			if(TestYDownCollisions(Map.instance.mainLayer,getX(),getY(),spr.getWidth(),spr.getHeight(),velocity,-13,tileWidth,tileHeight))
			{
				setY(oldY);
				velocity.y = 0;
			}
		}
		
		if(number<=0)isDead = true;
		

	}
	
	public boolean TestYDownCollisions(MapLayer colayer, float posX, float posY,float width, float height, Vector2 velo, float DO,float tileW,float tileH){
		boolean colliding = false;
		if(velo.y<0){
			colliding = isCollide(colayer.getBloc(Tools.floor((posX+spr.getWidth()/2)/tileW), Tools.floor((posY-DO)/tileH)));
		}
		return colliding;
	}
	
	public boolean TestYUpCollisions(MapLayer colayer, float posX, float posY,float width, float height, Vector2 velo, float DO,float tileW,float tileH){
		boolean colliding = false;
		if(velo.y>0){
			colliding = isCollide(colayer.getBloc(Tools.floor((posX+width/2)/tileW), Tools.floor((posY+height/2-DO)/tileH)));
		}
		return colliding;
	}
	
	public boolean TestXRightCollisions(MapLayer colayer, float posX, float posY,float width, float height, Vector2 velo, float DO,float tileW,float tileH){
		boolean colliding = false;
		if(velo.y<0){
			colliding = isCollide(colayer.getBloc(Tools.floor((posX+spr.getWidth()+DO)/tileW), Tools.floor((posY+height/2)/tileH)));
		}
		return colliding;
	}
	
	public boolean TestXLeftCollisions(MapLayer colayer, float posX, float posY,float width, float height, Vector2 velo, float DO,float tileW,float tileH){
		boolean colliding = false;
		if(velo.y<0){
			colliding = isCollide(colayer.getBloc(Tools.floor((posX-DO)/tileW), Tools.floor((posY+height/2)/tileH)));
		}
		return colliding;
	}
	
	private boolean isCollide(BlocType cell)
	{
		return cell.collide;
	}
	
	@Override
	 public void draw(SpriteBatch batch)
	 {
		super.draw(batch);
		if(Main.delta<1)update(Main.delta);
		Vector3 color = Tools.getShadowColor((int) ((getX()+spr.getWidth()/2)/16),(int) ((getY()+spr.getHeight()/2)/16));
		spr.setColor(color.x,color.y,color.z,1);
		spr.draw(batch);	
	 }
	 
	 @Override
	 public float getX()
		{
			return spr.getX();
		}
	 @Override
	 public float getY()
		{
			return spr.getY();
		}
	 @Override
	 public void setX(float xii)
		{
		 spr.setX(xii);
		}
	 @Override
	 public void setY(float xii)
		{
		 spr.setY(xii);
		}

}
