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
	float r = 1.0 - elapsed;
	gl_FragColor = vec4(current[0] * r, current[1] * r, current[2] * r, 1);
}