varying vec4 v_color;
varying vec2 v_texCoord0;

uniform sampler2D u_sampler2D;


void main() {
	vec4 textureColor = texture2D(u_sampler2D, v_texCoord0)*v_color;

	textureColor.a *= (255.0/254.0);

    gl_FragColor = textureColor;

}
