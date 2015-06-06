varying vec4 v_color;
varying vec2 v_texCoord0;

uniform sampler2D u_sampler2D;
uniform vec2 u_screenSize;
uniform vec2 u_lightOffset;
uniform vec2 u_lightArrayDim;
uniform vec2 u_camSize;
uniform int u_RGB;
uniform sampler2D u_lights;

void main() 
{
	vec4 color2 = v_color;
	color2.a *= (255.0/254.0);
	vec4 col = texture2D(u_sampler2D, v_texCoord0) * color2;
	vec2 coord = vec2(((gl_FragCoord.x-u_lightOffset.x)*u_camSize.x/u_screenSize.x)/16.0, ((gl_FragCoord.y-u_lightOffset.y)*u_camSize.y/u_screenSize.y)/16.0);
	int infX = int(coord.x);
	int infY = int(coord.y);
	int useX = -1;
	int useY = -1;
	float factorX = coord.x-float(infX)-0.5;
	float factorY = coord.y-float(infY)-0.5;
	if(factorX >0.0)
	{
		useX = 1;
	}
	else
	{
		factorX*=-1.0;
	}
	
	if(factorY >0.0)
	{
		useY = 1;
	}
	else
	{
		factorY*=-1.0;
	}

	if(u_RGB == 1)
	{
		vec3 lumi = texture2D(u_lights, vec2((float(infX)+0.1)/u_lightArrayDim.x, (float(infY)+0.1)/u_lightArrayDim.y)).xyz*(1.0-factorX-factorY);
		lumi+= texture2D(u_lights, vec2((float(infX+useX)+0.1)/u_lightArrayDim.x, (float(infY)+0.1)/u_lightArrayDim.y)).xyz*factorX;
		lumi+= texture2D(u_lights, vec2((float(infX)+0.1)/u_lightArrayDim.x, (float(infY+useY)+0.1)/u_lightArrayDim.y)).xyz*factorY;
		col.rbg*=lumi*4.0;
	}
	else
	{
		float lumi = texture2D(u_lights, vec2((float(infX)+0.1)/u_lightArrayDim.x, (float(infY)+0.1)/u_lightArrayDim.y)).w*(1.0-factorX-factorY);
		lumi+= texture2D(u_lights, vec2((float(infX+useX)+0.1)/u_lightArrayDim.x, (float(infY)+0.1)/u_lightArrayDim.y)).w*factorX;
		lumi+= texture2D(u_lights, vec2((float(infX)+0.1)/u_lightArrayDim.x, (float(infY+useY)+0.1)/u_lightArrayDim.y)).w*factorY;
		col.rbg*=lumi*4.0;
	}

	if(col.x>1.0)col.x = 1.0;
	if(col.y>1.0)col.y = 1.0;
	if(col.z>1.0)col.z = 1.0;
	
	
	gl_FragColor = col;
}
