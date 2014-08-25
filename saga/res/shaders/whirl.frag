#ifdef GL_ES
precision mediump float;
#endif

varying vec4 v_color; 
varying vec2 v_texCoords; 
uniform sampler2D u_texture;
uniform float u_elapsed;

void main() {
	float offX = sin(v_texCoords[1] * 32.0 + u_elapsed * 7.0) / 48.0;
	gl_FragColor = v_color * texture2D(u_texture, v_texCoords + vec2(offX, 0));
}