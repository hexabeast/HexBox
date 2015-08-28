varying vec4 v_color;
varying vec2 v_texCoord0;

uniform sampler2D u_sampler2D;

void main() 
{
	vec4 col = texture2D(u_sampler2D, v_texCoord0) * v_color;

	gl_FragColor = vec4(min(col.x+0.1,1),col.y,col.z,col.w);
}