varying vec4 v_color;
varying vec2 v_texCoord0;

uniform sampler2D u_sampler2D;

void main() 
{
	vec4 col = texture2D(u_sampler2D, v_texCoord0) * v_color;

	gl_FragColor = vec4(0.8,0,0,col.w);
}