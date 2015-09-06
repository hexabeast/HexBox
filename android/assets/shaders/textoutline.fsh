varying vec4 v_color;
varying vec2 v_texCoord0;
uniform vec2 u_invScreenSize;
uniform vec3 u_color;

uniform sampler2D u_sampler2D;

void main() 
{
	vec4 col = texture2D(u_sampler2D, v_texCoord0) * v_color;
	vec4 red = vec4(u_color.x,u_color.y,u_color.z,col.w);
	
	/*vec2 T = v_texCoord0;

	vec4 col1 = texture2D(u_sampler2D, vec2(T.x+u_invScreenSize.x,T.y));
	vec4 col2 = texture2D(u_sampler2D, vec2(T.x-u_invScreenSize.x,T.y));
	vec4 col3 = texture2D(u_sampler2D, vec2(T.x,T.y+u_invScreenSize.y));
	vec4 col4 = texture2D(u_sampler2D, vec2(T.x,T.y-u_invScreenSize.y));

	if(col1.w==0 || col2.w==0 || col3.w==0 || col4.w==0)
	{
		red.x = 0;
		red.y = 1;
	}*/
	
	if(col.w>0) 
	gl_FragColor = red;
	else
	gl_FragColor = vec4(0,0,0,0);
}