#ifdef GL_ES
precision mediump float;
#endif

varying vec4 v_color; 
varying vec2 v_texCoords; 
uniform sampler2D u_texture;
uniform ivec2 u_tilesize;
uniform ivec2 u_mapsize;
uniform vec2 u_offset;
uniform sampler2D u_visibility;

int passable(int tileX, int tileY) {
	if (tileX < 0 || tileY < 0 || tileX >= u_mapsize.x || tileY >= u_mapsize.y) {
		return 0;
	}
	ivec2 index = ivec2(tileX, tileY);
	if (texelFetch(u_visibility, index, 0).r == 1.0) {
		return 0;
	} else {
		return 1;
	}
}

void main() {
	float alpha = 0.0;
	float screenX = (gl_FragCoord[0]);
	float screenY = (gl_FragCoord[1]);
	int tileX = int(floor((u_offset.x + screenX) / float(u_tilesize.x)));
	int tileY = int(floor((u_offset.y + screenY) / float(u_tilesize.y)));
	float spareX = (u_offset.x + screenX) - float(tileX * u_tilesize.x);
	float spareY = (u_offset.y + screenY) - float(tileY * u_tilesize.y);
	if (tileX < 0 || tileY < 0 || tileX >= u_mapsize.x || tileY >= u_mapsize.y) {
		alpha = 0.0;
	} else if (passable(tileX, tileY) == 0) {
		alpha = 1.0;
	}
	gl_FragColor = vec4(v_color[0], v_color[1], v_color[2], alpha) * texture2D(u_texture, v_texCoords);
	//gl_FragColor = vec4(v_color[0], v_color[1]+woof, v_color[2], 1.0) * texture2D(u_texture, v_texCoords);
	//gl_FragColor = v_color * texture2D(u_texture, v_texCoords);
}