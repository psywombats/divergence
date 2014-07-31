attribute vec4 a_position;
attribute vec4 a_color;
attribute vec2 a_texCoord0;
uniform mat4 u_projTrans;
varying vec4 v_color;
varying vec2 v_texCoords;
uniform float u_elapsed;

float rand(vec2 co){
	return fract(sin(dot(co.xy ,vec2(12.9898,78.233))) * 43758.5453);
}

void main() {
	v_color = a_color;
	v_texCoords = a_texCoord0;
	
	float offX = rand(vec2(u_elapsed, 0.0)) * 20 - 10;
	float offY = rand(vec2(u_elapsed, 1.0)) * 20 - 10;
	
	gl_Position = u_projTrans * (a_position + vec4(offX, offY, 0, 0));
} 