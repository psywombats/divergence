#ifdef GL_ES
precision mediump float;
#endif

varying vec4 v_color; 
varying vec2 v_texCoords; 
uniform sampler2D u_texture;
uniform vec2 u_tilesize;
uniform vec2 u_mapsize;
uniform vec2 u_offset;
uniform float u_visibility[2400];

int visible(float tileX, float tileY) {
	if (tileX < 0.0 || tileY < 0.0 || tileX >= u_mapsize.x || tileY >= u_mapsize.y) {
		return 0;
	}
	if (u_visibility[int(u_mapsize.x*tileY + tileX)] == 1.0) {
		return 1;
	} else {
		return 0;
	}
}

void main() {
	float alpha = 0.0;
	float screenX = (gl_FragCoord[0]);
	float screenY = (gl_FragCoord[1]);
	float tileX = floor((u_offset.x + screenX) / u_tilesize.x);
	float tileY = floor((u_offset.y + screenY) / u_tilesize.y);
	float spareX = (u_offset.x + screenX) - (tileX * u_tilesize.x);
	float spareY = (u_offset.y + screenY) - (tileY * u_tilesize.y);
	if (tileX < 0.0 || tileY < 0.0 || tileX >= u_mapsize.x || tileY >= u_mapsize.y) {
		alpha = 1.0;
	} else if (visible(tileX, tileY) == 0) {
		alpha = 1.0;
		float mod;
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
			if (spareX + spareY < 16.0) alpha *= (spareX+spareY)/16.0;
		}
		if (left==0 && up==0 && visible(tileX-1, tileY+1)==1) {
			if (spareX + (32.0-spareY) < 16.0) alpha *= (spareX+(32.0-spareY))/16.0;
		}
		if (right==0 && down==0 && visible(tileX+1, tileY-1)==1) {
			if ((32.0-spareX) + spareY < 16.0) alpha *= ((32.0-spareX)+spareY)/16.0;
		}
		if (right==0 && up==0 && visible(tileX+1, tileY+1)==1) {
			if ((32.0-spareX) + (32.0-spareY) < 16.0) alpha *= ((32.0-spareX)+(32.0-spareY))/16.0;
		}
	}
	gl_FragColor = vec4(v_color[0], v_color[1], v_color[2], alpha) * texture2D(u_texture, v_texCoords);
	//gl_FragColor = vec4(v_color[0], alpha, .0, 1.0);
}