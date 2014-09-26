#ifdef GL_ES
precision mediump float;
#endif

varying vec4 v_color; 
varying vec2 v_texCoords; 
uniform sampler2D u_texture;
uniform float u_elapsed;

void main() {
	gl_FragColor = v_color * texture2D(u_texture, v_texCoords);
	gl_FragColor[0] += u_elapsed;
	gl_FragColor[1] += u_elapsed;
	gl_FragColor[2] += u_elapsed;
}