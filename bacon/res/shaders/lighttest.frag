#ifdef GL_ES
precision mediump float;
#endif

varying vec4 v_color; 
varying vec2 v_texCoords;
uniform sampler2D u_texture;
uniform sampler2D u_textureAlt;
uniform sampler2D u_light;
uniform int u_black;
uniform int u_fragged;
uniform int u_eaten;

void main() {
	vec4 origColor = v_color * texture2D(u_texture, v_texCoords);
	vec4 altColor = texture2D(u_textureAlt, v_texCoords);
	vec4 res = vec4(0.0, 0.0, 0.0, 1.0);
	float r = (1.0 - texture2D(u_light, v_texCoords)[0]);
	
	res[0] = r * origColor[0] + (1.0-r) * altColor[0];
	res[1] = r * origColor[1] + (1.0-r) * altColor[1];
	res[2] = r * origColor[2] + (1.0-r) * altColor[2];
	res[3] = r * origColor[3] + (1.0-r) * altColor[3];
	if (res[3] > 1.0) {
		res[3] = 1.0;
	}
	
	if (u_black == 1) {
		gl_FragColor = vec4(0, 0, 0, 0);
	} else if (u_fragged == 1) {
		gl_FragColor = vec4(res[0] * 0.5, res[0] * 0.5, res[0] * 0.5, res[3]);
	} else if (u_eaten == 1) {
		gl_FragColor = vec4(res[0] * 0.7, res[0] * 0.1, res[0] * 0.1, res[3]);
	} else {
		gl_FragColor = res;
	}
}
