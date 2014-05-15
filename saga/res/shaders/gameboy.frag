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

float color_d(vec4 c1, vec3 c2) {
	return abs(c1[0] - c2[0]) + abs(c1[1] - c2[1]) + abs(c1[2] - c2[2]);
}

vec4 vec3to4(vec3 vec) {
	return vec4(vec[0], vec[1], vec[2], 1);
}

void main() {
	vec4 current = texture2D(u_texture, v_texCoords);
	
	float dBlack = color_d(current, u_black);
	float dDgray = color_d(current, u_dgray);
	float dLgray = color_d(current, u_lgray);
	float dWhite = color_d(current, u_white);
	
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