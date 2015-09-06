package com.hexabeast.sandbox;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Blending;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class Shaders {
	public static Shaders instance;
	//public SpriteBatch shaderBatch;
	public ShaderProgram shadow;
	public ShaderProgram shadow2;
	public ShaderProgram meshader;
	public ShaderProgram red;
	public ShaderProgram white;
	public ShaderProgram outline;
	public ShaderProgram basic;
	public int minX;
	public int maxX;
	public int minY;
	public int maxY;
	public int size;
	public int size2;
	public Vector2 offset = new Vector2();
	FrameBuffer fbo;
	Texture lightmap;
	Pixmap pixmap;
	public Shaders()
	{
		pixmap = new Pixmap(128,128,Pixmap.Format.RGB888);
		Pixmap.setBlending(Blending.None);
		ShaderProgram.pedantic = false;
		basic = new ShaderProgram(Gdx.files.internal("shaders/passthrough.vsh"),Gdx.files.internal("shaders/passthrough.fsh"));
		meshader = new ShaderProgram(Gdx.files.internal("shaders/image.vsh"),Gdx.files.internal("shaders/image.fsh"));
		shadow = new ShaderProgram(Gdx.files.internal("shaders/shadow2.vsh"),Gdx.files.internal("shaders/shadow2.fsh"));
		shadow2 = new ShaderProgram(Gdx.files.internal("shaders/shadow2.vsh"),Gdx.files.internal("shaders/shadow3.fsh"));
		red = new ShaderProgram(Gdx.files.internal("shaders/shadow2.vsh"),Gdx.files.internal("shaders/red.fsh"));
		white = new ShaderProgram(Gdx.files.internal("shaders/shadow2.vsh"),Gdx.files.internal("shaders/white.fsh"));
		outline = new ShaderProgram(Gdx.files.internal("shaders/shadow2.vsh"),Gdx.files.internal("shaders/textoutline.fsh"));
		
		
		if(!basic.isCompiled())
		{
			System.out.println(basic.getLog());
			basic = new ShaderProgram(Gdx.files.internal("shaders/passthrough.vsh"),Gdx.files.internal("shaders/passthrough2.fsh"));
		}
		if(!shadow.isCompiled())System.out.println("shadow : "+shadow.getLog());
		if(!shadow2.isCompiled())System.out.println("shadow2 : "+shadow2.getLog());
		if(!red.isCompiled())System.out.println("red : "+red.getLog());
		if(!white.isCompiled())System.out.println("white : "+white.getLog());
		if(!meshader.isCompiled())System.out.println("meshader : "+meshader.getLog());
		if(!outline.isCompiled())System.out.println("outline : "+outline.getLog());
	}
	
	public void setShadowShader()
	{
		if(Parameters.i.fullBright && Parameters.i.shader)
		{
			if(Parameters.i.HQ == 4)
				Main.batch.setShader(Shaders.instance.shadow);
			else
				Main.batch.setShader(Shaders.instance.shadow2);
		}
		else setDefaultShader();
	}
	
	public void setShadowShaderFBO()
	{
		if(Parameters.i.fullBright && Parameters.i.shader)
		{
			if(Parameters.i.HQ == 4)
				Main.batch.setShader(Shaders.instance.shadow);
			else
				Main.batch.setShader(Shaders.instance.shadow2);
		}
		else setDefaultShader();
	}
	
	public void setDefaultShader()
	{
		Main.batch.setShader(Shaders.instance.basic);
	}
	
	public void setRedShader()
	{
		Main.batch.setShader(Shaders.instance.red);
	}
	
	public void setWhiteShader()
	{
		Main.batch.setShader(Shaders.instance.white);
	}
	
	public void setOutlineShader()
	{
		Main.batch.setShader(Shaders.instance.outline);
	}
	
	public void update()
	{
		
	}
	
	public void updateOutline(float w, float h, float r, float g, float b)
	{
		outline.setUniformf("u_invScreenSize", 1f/w, 1f/h);
		outline.setUniformf("u_color", r, g, b);
		
	}
	
	public void updateShadows()
	{
		computeBounds();
		//fbo.getColorBufferTexture().bind(2000);
		ShaderProgram current = shadow;
		if(Parameters.i.HQ == 5)
		{
			current = shadow2;
		}
		current.begin();
		lightmap.bind(1);
		current.setUniformi("u_lights", 1);
		current.setUniformf("u_lightOffset", offset);
		int RGB = 0;
		if(Parameters.i.RGB)RGB = 1;
		current.setUniformi("u_RGB", RGB);
		current.setUniformf("u_lightArrayDim",size,size2);
		current.setUniformf("u_camSize", GameScreen.camera.viewportWidth,GameScreen.camera.viewportHeight);
		current.setUniformf("u_screenSize", Main.windowWidth,Main.windowHeight);
		current.end();
		Gdx.gl20.glActiveTexture(GL20.GL_TEXTURE0);
	}
	
	public void computeBounds()
	{
		minX = Tools.floor((GameScreen.camera.position.x - GameScreen.camera.zoom*GameScreen.camera.viewportWidth/2 - 32)/16);
		minY = Tools.floor((GameScreen.camera.position.y - GameScreen.camera.zoom*GameScreen.camera.viewportHeight/2 - 32)/16);
		maxX = Tools.floor((GameScreen.camera.position.x + GameScreen.camera.zoom*GameScreen.camera.viewportWidth/2 + 32)/16);
		maxY = Tools.floor((GameScreen.camera.position.y + GameScreen.camera.zoom*GameScreen.camera.viewportHeight/2 + 32)/16);
		
		if(!GameScreen.noLimit)
		{
			if(minX<0)minX = 0;
			if(maxX>Map.instance.width-1)maxX = Map.instance.width-1;
		}
		
		if(minY<1)minY = 1;
		if(maxY>Map.instance.height-2)maxY = Map.instance.height-2;
		size = (maxX-minX);
		size2 = (maxY-minY);
		
		
		if(Parameters.i.RGB)
		{
			if(pixmap == null || pixmap.getFormat() != Pixmap.Format.RGB888 || pixmap.getWidth() != size || pixmap.getHeight() != size2)
			{
				pixmap.dispose();
				pixmap = new Pixmap(size,size2,Pixmap.Format.RGB888);
			}
		}
		else
		{
			if(pixmap == null || pixmap.getFormat() != Pixmap.Format.Alpha || pixmap.getWidth() != size || pixmap.getHeight() != size2)
			{
				pixmap.dispose();
				pixmap = new Pixmap(size,size2,Pixmap.Format.Alpha);
			}
		}
		if(Parameters.i.RGB)
		{
			for(int i = 0; i<size; i++)
			{
				for(int j = 0; j<size2; j++)
				{
					Vector3 color = Map.instance.lights.getLight(i+minX,j+minY);
					//Color col = new Color();
					//Color.rgba8888ToColor(col, pixmap.getPixel(i, j));
					//if(color.x != col.r && color.y != col.g && color.z != col.b)
					pixmap.drawPixel(i,j,Color.rgba8888(color.x/4,color.z/4,color.y/4,1));
				}
			}
		}
		else
		{
			for(int i = 0; i<size; i++)
			{
				for(int j = 0; j<size2; j++)
				{
					float color = Map.instance.lights.getLight(i+minX,j+minY,0);
					//Color col = new Color();
					//Color.rgba8888ToColor(col, pixmap.getPixel(i, j));
					//if(color != col.a)
					pixmap.drawPixel(i,j,Color.alpha(color/4));
				}
			}
		}
				/*float c = Map.instance.lights.getLight(i+minX,j+minY);
				float r = Map.instance.lights.getLight(i+minX+1,j+minY);
				float l = Map.instance.lights.getLight(i+minX-1,j+minY);
				float u = Map.instance.lights.getLight(i+minX,j+minY+1);
				float d = Map.instance.lights.getLight(i+minX,j+minY-1);
				pixmap.drawPixel(i*2,j*2,Color.rgba8888(1,1,1,(c*2+l+d)/4));
				pixmap.drawPixel(i*2+1,j*2,Color.rgba8888(1,1,1,(c*2+r+d)/4));
				pixmap.drawPixel(i*2,j*2+1,Color.rgba8888(1,1,1,(c*2+l+u)/4));
				pixmap.drawPixel(i*2+1,j*2+1,Color.rgba8888(1,1,1,(c*2+r+u)/4));*/
		
		
		offset = Tools.getScreenPos(minX*16, minY*16);
		if(lightmap != null)lightmap.dispose();
		lightmap = new Texture(pixmap);	
		/*fbo.begin();
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		shaderBatch.begin();
		shaderBatch.draw(new Texture(pixmap),0,0,1024,1024);
		shaderBatch.end();
		fbo.end();*/
	}
}
