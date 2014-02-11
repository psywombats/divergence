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

vec2 distort(vec2 p) {
	float theta  = atan(p.y, p.x);
	float radius = length(p);
	radius = pow(radius, u_power);
	p.y = radius * sin(theta);
	p.x = radius * cos(theta);
	float middle = sin((p.y*.5)*(3.14/2.0) + (3.14/2.0));
	float off = -cos(2.0*u_done*3.14);
	p.x += -sin(off*middle*3.14)*.05;
	return 0.5 * (p + 1.0);
}

void main() {
	float alpha = texture2D(u_mask, v_texCoords)[3];
	float xin = (gl_FragCoord[0])/float(u_screensize[0]);
	float yin = (gl_FragCoord[1])/float(u_screensize[1]);
	vec2 p = vec2(2.0*xin - 1.0, 2.0*(1.0-yin) - 1.0);
	vec2 index = distort(p);
	gl_FragColor = vec4(v_color[0], v_color[1], v_color[2], alpha) * texture2D(u_texture, index);
}