package com.hexabeast.sandbox;
 
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class Converter {
	static OrthographicCamera cam = new OrthographicCamera();

	public Converter() {
	}

	/*public static Vector2 screenToMapCoordsT3(float x,float y)
	{
		Vector3 worldCoord = new Vector3(x,y, 0);
		cam.position.set(GameScreen.camPos);
		cam.zoom = GameScreen.gameZoom;
		cam.viewportWidth = GameScreen.windowWidth;
		cam.viewportHeight = GameScreen.windowHeight;
		cam.update();
		cam.unproject(worldCoord);
		return new Vector2((int)(worldCoord.x/Map.instance.mainLayer.getTileWidth()), (int)(worldCoord.y/Map.instance.mainLayer.getTileHeight()));
	}*/
	
	public static Vector2 screenToMapCoords(float x,float y)
	{
		Vector3 worldCoord = new Vector3(x,y, 0);
		GameScreen.camera.unproject(worldCoord);
		return new Vector2((int)(worldCoord.x/Map.instance.mainLayer.getTileWidth()), (int)(worldCoord.y/Map.instance.mainLayer.getTileHeight()));
	}
}
