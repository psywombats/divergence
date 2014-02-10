#ifdef GL_ES
precision mediump float;
#endif

varying vec4 v_color; 
varying vec2 v_texCoords; 
uniform vec2 u_at;
uniform vec2 u_size;
uniform vec2 u_screensize;
uniform sampler2D u_texture;
uniform sampler2D u_mask;
uniform float u_power;
uniform float u_done;

vec2 distort(vec2 p, float amt) {
	float theta  = atan(p.y, p.x);
	float radius = length(p);
	radius += amt;
	p.y = radius * sin(theta);
	p.x = radius * cos(theta);
	return (.5 * p) + .5;
}

void main() {
	float alpha = texture2D(u_mask, v_texCoords)[3];
	vec2 off = (u_at) / u_screensize;
	float xin = (gl_FragCoord[0])/float(u_screensize[0]) + off[0];
	float yin = (gl_FragCoord[1])/float(u_screensize[1]) + off[1];
	vec2 p = vec2(2.0*xin - 1.0, 2.0*(1.0-yin) - 1.0);
	
	float pow = (sin(u_done*1.3)*.7 + .3) * u_power * alpha;
	vec2 index = distort(p, pow);
	index[0] -= off[0];
	index[1] += off[1];
	
	gl_FragColor = vec4(v_color[0], v_color[1], v_color[2], 1.0) * texture2D(u_texture, index);
}