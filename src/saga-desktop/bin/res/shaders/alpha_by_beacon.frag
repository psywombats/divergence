#ifdef GL_ES
precision mediump float;
#endif

varying vec4 v_color; 
varying vec2 v_texCoords; 
uniform sampler2D u_texture;
uniform vec2 u_beacons[50];
uniform int u_beaconCount;
uniform float u_atX;
uniform float u_atY;
uniform float u_exp;
uniform float u_factor;
uniform int u_radius;

void main() {
	float r = float(u_radius);
	float minDist = float(r);
	for (int i = 0; i < u_beaconCount; i++) {
		vec2 beacon = u_beacons[i];
		float screenX = (gl_FragCoord[0]);
		float screenY = (gl_FragCoord[1]);
		float a = u_atX + u_atY + float(u_beaconCount);
		float dx = screenX - (beacon.x - u_atX);
		float dy = screenY - (beacon.y - u_atY);
		float dist = sqrt(dx*dx + dy*dy);
		if (dist < minDist) {
			minDist = dist;
		}
	}
	float alpha = v_color[3] * pow(minDist / r, u_exp) * u_factor + (1.0-u_factor);
	gl_FragColor = vec4(v_color[0], v_color[1], v_color[2], alpha) * texture2D(u_texture, v_texCoords);
	//gl_FragColor = vec4(0.0, minDist*minDist * v_color[3], 0.0, 1.0);
	//gl_FragColor = vec4(v_color[0], v_color[1], v_color[2], alpha);
}