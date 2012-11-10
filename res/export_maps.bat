cd ..
del game\res\maps\*.* /s /q
java -classpath res/libs/gdx-natives.jar;res/libs/gdx-backend-jogl-natives.jar;res/libs/gdx-backend-jogl.jar;res/libs/gdx.jar;res/libs/gdx-tools.jar;res/libs/gdx-tiled-preprocessor.jar com.badlogic.gdx.tiledmappacker.TiledMapPacker res/maps game/res/maps
PAUSE