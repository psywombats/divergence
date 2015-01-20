#ifdef GL_ES
precision mediump float;
#endif

varying vec4 v_color; 
varying vec2 v_texCoords;
uniform sampler2D u_texture;
uniform sampler2D u_light;

void main() {
	vec4 origColor = v_color * texture2D(u_texture, v_texCoords);
	origColor[0] *= (1.0 - texture2D(u_light, v_texCoords)[0]);
	origColor[1] *= (1.0 - texture2D(u_light, v_texCoords)[1]);
	origColor[2] *= (1.0 - texture2D(u_light, v_texCoords)[2]);
	gl_FragColor = origColor;
}
