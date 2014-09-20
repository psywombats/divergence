#ifdef GL_ES
precision mediump float;
#endif

varying vec4 v_color;
varying vec2 v_texCoords;
uniform sampler2D u_texture;
uniform vec3 u_black;
uniform vec3 u_dgray;
uniform vec3 u_lgray;
uniform vec3 u_white;
uniform vec3 u_blackOut;
uniform vec3 u_dgrayOut;
uniform vec3 u_lgrayOut;
uniform vec3 u_whiteOut;
uniform float u_margin;
uniform float u_height;
uniform float elapsed;

float color_d(vec4 c1, vec3 c2) {
	return abs(c1[0] - c2[0]) + abs(c1[1] - c2[1]) + abs(c1[2] - c2[2]);
}

vec4 vec3to4(vec3 vec) {
	return vec4(vec[0], vec[1], vec[2], 1);
}

void main() {
	vec4 current = v_color * texture2D(u_texture, v_texCoords);
	current[0] *= current[3];
	current[1] *= current[3];
	current[2] *= current[3];
	float y = gl_FragCoord[1];
	float off = elapsed;
	if (y < u_margin) off += .6;
	if (y < u_margin*2.0) off += .4;
	if (y > u_height-u_margin) off += .4;
	if (y > u_height-u_margin*2.0) off += .6;
	current[0] += off;
	current[1] += off;
	current[2] += off;
	
	float dBlack = color_d(current, u_black);
	float dDgray = color_d(current, u_dgray);
	float dLgray = color_d(current, u_lgray);
	float dWhite = color_d(current, u_white);
	
	if (current[3] == 0.0) {
		gl_FragColor = v_color * texture2D(u_texture, v_texCoords);
	} else {
		if (dBlack <= dDgray && dBlack <= dLgray && dBlack <= dWhite) {
			gl_FragColor = vec3to4(u_blackOut);
		}
		if (dDgray <= dBlack && dDgray <= dLgray && dDgray <= dWhite) {
			gl_FragColor = vec3to4(u_dgrayOut);
		}
		if (dLgray <= dBlack && dLgray <= dDgray && dLgray <= dWhite) {
			gl_FragColor = vec3to4(u_lgrayOut);
		}
		if (dWhite <= dBlack && dWhite <= dDgray && dWhite <= dLgray) {
			gl_FragColor = vec3to4(u_whiteOut);
		}
	}
}