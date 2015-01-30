#ifdef GL_ES
precision mediump float;
#endif

varying vec4 v_color; 
varying vec2 v_texCoords;
uniform sampler2D u_texture;
uniform sampler2D u_textureAlt;
uniform sampler2D u_light;

void main() {
	vec4 origColor = v_color * texture2D(u_texture, v_texCoords);
	vec4 altColor = texture2D(u_textureAlt, v_texCoords);
	vec4 finalColor = vec4(0.0, 0.0, 0.0, 1.0);
	float r = (1.0 - texture2D(u_light, v_texCoords)[0]);
	
	finalColor[0] = r * origColor[0] + (1.0-r) * altColor[0];
	finalColor[1] = r * origColor[1] + (1.0-r) * altColor[1];
	finalColor[2] = r * origColor[2] + (1.0-r) * altColor[2];
	finalColor[3] = r * origColor[3] + (1.0-r) * altColor[3];
	if (finalColor[3] > 1.0) {
		finalColor[3] = 1.0;
	}
	
	gl_FragColor = finalColor;
}
