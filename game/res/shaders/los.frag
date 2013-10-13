#ifdef GL_ES
precision mediump float;
#endif

varying vec4 v_color; 
varying vec2 v_texCoords; 
uniform sampler2D u_texture;
uniform int u_colorcomp;
uniform ivec2 u_tilesize;
uniform ivec2 u_mapsize;
uniform vec2 u_offset;
uniform sampler2D u_visibility;

int visible(int tileX, int tileY) {
	if (tileX < 0 || tileY < 0 || tileX >= u_mapsize.x || tileY >= u_mapsize.y) {
		return 0;
	}
	vec2 index = vec2((2.0*float(tileX) + 1.0)/(2.0*float(u_mapsize.x)), (2.0*float(tileY) + 1.0)/(2.0*float(u_mapsize.y)));
	if (texture2D(u_visibility, index)[u_colorcomp] == 1.0) {
		return 1;
	} else {
		return 0;
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
		alpha = 1.0;
	} else if (visible(tileX, tileY) == 0) {
		alpha = 1.0;
		float mod = 0.0;
		int left = visible(tileX-1, tileY);
		int right = visible(tileX+1, tileY);
		int down = visible(tileX, tileY-1);
		int up = visible(tileX, tileY+1);
		if (left == 1) {
			if (spareX < 16.0) alpha *= spareX/16.0;
		}
		if (right == 1) {
			if (spareX > 16.0) alpha *= 1.0-(spareX-16.0)/16.0;
		}
		if (down == 1) {
			if (spareY < 16.0) alpha *= spareY/16.0;
		}
		if (up == 1) {
			if (spareY > 16.0) alpha *= 1.0-(spareY-16.0)/16.0;
		}
		if (left==0 && down==0 && visible(tileX-1, tileY-1)==1) {
			mod = sqrt(spareX*spareX+spareY*spareY)/16.0;
			if (mod < 1.0 && mod > 0.0) alpha *= mod;
		}
		if (left==0 && up==0 && visible(tileX-1, tileY+1)==1) {
			mod = sqrt(spareX*spareX+(32.0-spareY)*(32.0-spareY))/16.0;
			if (mod < 1.0 && mod > 0.0) alpha *= mod;
		}
		if (right==0 && down==0 && visible(tileX+1, tileY-1)==1) {
			mod = sqrt((32.0-spareX)*(32.0-spareX)+spareY*spareY)/16.0;
			if (mod < 1.0 && mod > 0.0) alpha *= mod;
		}
		if (right==0 && up==0 && visible(tileX+1, tileY+1)==1) {
			mod = sqrt((32.0-spareX)*(32.0-spareX)+(32.0-spareY)*(32.0-spareY))/16.0;
			if (mod < 1.0 && mod > 0.0) alpha *= mod;
		}
	}
	gl_FragColor = vec4(v_color[0], v_color[1], v_color[2], alpha) * texture2D(u_texture, v_texCoords);
	//gl_FragColor = vec4(v_color[0], v_color[1]+woof, v_color[2], 1.0) * texture2D(u_texture, v_texCoords);
	//gl_FragColor = v_color * texture2D(u_texture, v_texCoords);
}