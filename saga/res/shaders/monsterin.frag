#ifdef GL_ES
precision mediump float;
#endif

varying vec4 v_color; 
varying vec2 v_texCoords; 
uniform sampler2D u_texture;
uniform float u_elapsedRatio;
uniform float u_middleY;
uniform float u_portraitHeight;

void main() {
	
	float skip = .2;
	float start = .5;
	float fromMid = (v_texCoords[1] + (u_middleY - .5)) / u_portraitHeight;
	if (fromMid < 0.0) {
		fromMid = fromMid * -1.0;
	}
	
	float offY = 0.0;
	float offX = sin(v_texCoords[1] * 24.0 + u_elapsedRatio * 8.0) / 28.0;
	
	if (u_elapsedRatio > start) {
		if (fromMid < u_elapsedRatio - start) {
			offX = 0.0;
		}
	}
	
	if (mod(gl_FragCoord[1], 4.0) >= 2.0) {
		offX = offX * -1.0;
	}
	
	if (u_elapsedRatio < start && fromMid > u_elapsedRatio) {
		gl_FragColor = vec4(1.0, 1.0, 1.0, 0.0);
	} else {
		gl_FragColor = v_color * texture2D(u_texture, v_texCoords + vec2(offX, offY));
	}
}