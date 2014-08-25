attribute vec4 a_position;
attribute vec4 a_color;
attribute vec2 a_texCoord0;
uniform mat4 u_projTrans;
varying vec4 v_color;
varying vec2 v_texCoords;
uniform float u_elapsed;
uniform float u_offX;
uniform float u_offY;

float rand(vec2 co){
	return fract(sin(dot(co.xy ,vec2(12.9898, 78.233))) * 43758.5453);
}

void main() {
	v_color = a_color;
	v_texCoords = a_texCoord0;
	
	float offX = rand(vec2(u_elapsed, 0.0));
	float offY = rand(vec2(u_elapsed, 1.0));
	
	if (u_offX > 0.0) {
		offX = (offX * u_offX) - (u_offX / 2.0);
	} else if (u_offX == -1.0) {
		offX = 0.0;
	} else {
		offX = offX * 20.0 - 10.0;
	}
	
	if (u_offY > 0.0) {
		offY = (offY * u_offY) - (u_offY / 2.0);
	} else if (u_offY == -1.0) {
		offY = 0.0;
	} else {
		offY = offY * 20.0 - 10.0;
	}
	
	gl_Position = u_projTrans * (a_position + vec4(offX, offY, 0, 0));
} 