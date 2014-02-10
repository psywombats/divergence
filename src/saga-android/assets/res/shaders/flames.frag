#ifdef GL_ES
precision mediump float;
#endif

varying vec4 v_color; 
varying vec2 v_texCoords; 
uniform sampler2D u_texture;
uniform sampler2D u_mask;
uniform sampler2D u_noise1;
uniform sampler2D u_noise2;
uniform sampler2D u_noise3;
uniform float u_elapsed;
uniform float u_restrict;

// comes in centered at .5, .5
vec2 perturb(vec2 p, float power) {
	p.x = (p.x*2.0) - 1.0;
	p.y = (p.y*2.0) - 1.0;
	float theta  = atan(p.y, p.x);
	float radius = length(p) + power;
	p.y = radius * sin(theta);
	p.x = radius * cos(theta);
	return 0.5 * (p + 1.0);
}

void main() {

	float dx = v_texCoords[0] - .5;
	float dy = v_texCoords[1] - .5;
	float dist = sqrt(dx*dx + dy*dy);
	
	vec2 indexp1 = vec2(v_texCoords[0], v_texCoords[1] + u_elapsed*1.5);
	vec2 indexp2 = vec2(v_texCoords[0], v_texCoords[1] + u_elapsed*1.0);
	vec2 indexp3 = vec2(v_texCoords[0], v_texCoords[1] + u_elapsed*0.5);
	
	float off = 0.0;
	off += (1.0 - texture2D(u_noise1, indexp1)[0]*2.0);
	off += (1.0 - texture2D(u_noise2, indexp2)[0]*1.75);
	off += (1.0 - texture2D(u_noise3, indexp3)[0]*1.5);
	off *= dist;
	
	float alpha = texture2D(u_mask, perturb(v_texCoords, off))[3];
	if (alpha > u_restrict) alpha = u_restrict;
	
	gl_FragColor = vec4(v_color[0], v_color[1], v_color[2], alpha) * texture2D(u_texture, perturb(v_texCoords, off));
}