#ifdef GL_ES
precision mediump float;
#endif

varying vec4 v_color; 
varying vec2 v_texCoords; 
uniform sampler2D u_texture;
uniform float u_elapsedRatio;

void main() {
	float prime = 104729.0;
	float r = mod(v_texCoords[1] * prime, 100.0);
	if (r < u_elapsedRatio * 100.0) {
		gl_FragColor = vec4(1.0, 1.0, 1.0, 0.0);
	} else {
		gl_FragColor = v_color * texture2D(u_texture, v_texCoords);
	}
}