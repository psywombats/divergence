#ifdef GL_ES
precision mediump float;
#endif

varying vec4 v_color; 
varying vec2 v_texCoords; 
uniform sampler2D u_texture;
uniform float u_elapsed;
uniform float u_thresh;

float rand(vec2 co) {
	return fract(sin(dot(co.xy, vec2(12.9898, 78.233))) * 43758.5453);
}

void main() {

	vec4 texColor = v_color * texture2D(u_texture, v_texCoords);
	if (texColor[3] == 0) {
		gl_FragColor = vec4(0, 0, 0, 0);
	} else {
	
		vec2 randCoords = vec2(gl_FragCoord[0], gl_FragCoord[1]);
		float chunk = 2.0;
		chunk += rand(vec2(v_texCoords[1], u_elapsed)) * 4.0;
		randCoords[0] -= mod(randCoords[0], int(chunk));
		randCoords[1] -= mod(randCoords[1], 2);

		float r = (u_elapsed * 4.0) + texColor[0];
		r += rand(randCoords);
		float remainder = r - int(r);
		
		float alpha = texColor[3];
		if (remainder < u_thresh) {
			if (remainder < u_thresh * .2) {
				alpha -= .9;
			} else {
				alpha = 0.0;
			}
		}
		if (alpha < 0.0) {
			alpha = 0.0;
		}
		vec4 finalColor = vec4(texColor[0], texColor[1], texColor[2], alpha);
		
		gl_FragColor = v_color * finalColor;
	}
}