#ifdef GL_ES
precision mediump float;
#endif

varying vec4 v_color; 
varying vec2 v_texCoords; 
uniform sampler2D u_texture;
uniform float elapsed;

vec4 vec3to4(vec3 vec) {
	return vec4(vec[0], vec[1], vec[2], 1);
}

void main() {
	vec4 current = v_color * texture2D(u_texture, v_texCoords);
	float off = 0.0;
	float y = v_texCoords[1];
	if (y < .1) off += .6;
	if (y < .2) off += .4;
	if (y > .8) off += .4;
	if (y > .9) off += .6;
	off -= elapsed;
	off *= -1;
	gl_FragColor = vec4(current[0] - off, current[1]  - off, current[2]  - off, 1);
}