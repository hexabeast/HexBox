varying vec4 v_color;
varying vec2 v_texCoord0;

uniform sampler2D u_sampler2D;
uniform vec2 u_screenSize;
uniform vec2 u_lightOffset;
uniform vec2 u_lightArrayDim;
uniform vec2 u_camSize;
uniform sampler2D u_lights;

void main() 
{
	vec4 col = texture2D(u_sampler2D, v_texCoord0) * v_color;
	vec2 coord = vec2(((gl_FragCoord.x-u_lightOffset.x)*u_camSize.x/u_screenSize.x)/16-0.5, ((gl_FragCoord.y-u_lightOffset.y)*u_camSize.y/u_screenSize.y)/16-0.5);//*1280/16
	int supX = int(coord.x+1);
	int infX = int(coord.x);
	int supY = int(coord.y+1);
	int infY = int(coord.y);
	float xFactor = coord.x-float(infX);
	float yFactor = coord.y-float(infY);
	
	if(yFactor>0.5)yFactor*=1.5;
	else yFactor*=2.0/3.0;
	
	if(xFactor>0.5)xFactor*=1.5;
	else xFactor*=2.0/3.0;

	if(xFactor>1)xFactor=1;
	if(yFactor>1)yFactor=1;

	float lumi = texture2D(u_lights, vec2(float(infX+0.1)/u_lightArrayDim.x, float(infY+0.1)/u_lightArrayDim.y)).w*(1-xFactor +1-yFactor);//(2-(abs(infX-coord.x)+abs(infY-coord.y)));
	lumi+= texture2D(u_lights, vec2(float(supX+0.1)/(u_lightArrayDim.x), float(infY+0.1)/u_lightArrayDim.y)).w * (xFactor +1-yFactor);//(2-(abs(supX-coord.x)+abs(infY-coord.y)));
	lumi+= texture2D(u_lights, vec2(float(supX+0.1)/(u_lightArrayDim.x), float(supY+0.1)/u_lightArrayDim.y)).w * (xFactor +yFactor);//(2-(abs(supX-coord.x)+abs(supY-coord.y)));
	lumi+= texture2D(u_lights, vec2(float(infX+0.1)/(u_lightArrayDim.x), float(supY+0.1)/u_lightArrayDim.y)).w * (1-xFactor +yFactor);//(2-(abs(infX-coord.x)+abs(supY-coord.y)));
	lumi/=4;
	col.rbg*=lumi;
	gl_FragColor = col;
}
