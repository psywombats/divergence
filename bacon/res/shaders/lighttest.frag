#ifdef GL_ES
precision mediump float;
#endif

varying vec4 v_color; 
varying vec2 v_texCoords;
uniform sampler2D u_texture;
uniform sampler2D u_light;

void main() {
	vec4 origColor = v_color * texture2D(u_texture, v_texCoords);
	vec4 finalColor = origColor;
	float lum = texture2D(u_light, v_texCoords)[0];
	finalColor[0] *= lum;
	finalColor[1] *= lum;
	finalColor[2] *= lum;
	
	float thresh = .85;
	if (lum > thresh) {
		float add = (lum - thresh) * 10.0;
		finalColor[0] += (1.0 - origColor[0]) * add;
		finalColor[1] += (1.0 - origColor[1]) * add;
		finalColor[2] += (1.0 - origColor[2]) * add;
	}
	
	gl_FragColor = finalColor;
}
