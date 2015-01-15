#ifdef GL_ES
precision mediump float;
#endif

varying vec4 v_color; 
varying vec2 v_texCoords;
uniform sampler2D u_texture;
uniform sampler2D u_light;

void main() {
	vec4 origColor = v_color * texture2D(u_texture, v_texCoords);
	float r = (texture2D(u_light, v_texCoords)[0]);
	origColor[0] *= r;
	origColor[1] *= r;
	origColor[2] *= r;
	gl_FragColor = origColor;
}