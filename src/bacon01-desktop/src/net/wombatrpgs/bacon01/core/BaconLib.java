/**
 *  BaconLib.java
 *  Created on Feb 6, 2015 5:39:14 AM for project bacon01-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.bacon01.core;

import net.wombatrpgs.bacon01.screens.ScreenOutro;
import net.wombatrpgs.mgne.core.MGlobal;
import net.wombatrpgs.mgne.core.interfaces.FinishListener;
import net.wombatrpgs.mgne.scenes.SceneCommand;
import net.wombatrpgs.mgne.scenes.SceneLib;
import net.wombatrpgs.mgne.screen.Screen;

import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.TwoArgFunction;
import org.luaj.vm2.lib.ZeroArgFunction;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;

/**
 * goddamn bacon
 */
public class BaconLib extends TwoArgFunction {

	@Override public LuaValue call(LuaValue modname, LuaValue env) {
		LuaValue library = tableOf();
		
		env.set("hasItem", new OneArgFunction() {
			@Override public LuaValue call(LuaValue arg) {
				String itemName = arg.checkjstring();
				if (BGlobal.items.contains(itemName)) {
					return LuaValue.TRUE;
				} else {
					return LuaValue.FALSE;
				}
			}
		});
		
		env.set("itemCount", new ZeroArgFunction() {
			@Override public LuaValue call() {
				return CoerceJavaToLua.coerce(BGlobal.items.countPages() == 18);
			}
		});
		
		env.set("outro", new ZeroArgFunction() {
			@Override public LuaValue call() {
				SceneLib.addFunction(new SceneCommand() {
					@Override protected void internalRun() {
						final Screen outScreen = new ScreenOutro();
						MGlobal.assets.loadAsset(outScreen, "outro screen");
						MGlobal.levelManager.getTele().getPre().run();
						MGlobal.levelManager.getTele().getPre().addListener(new FinishListener() {
							@Override public void onFinish() {
								outScreen.getTint().r = 0;
								outScreen.getTint().g = 0;
								outScreen.getTint().b = 0;
								MGlobal.screens.push(outScreen);
								MGlobal.levelManager.getTele().getPost().run();
							};
						});
					}
					@Override protected boolean shouldFinish() {
						return true;
					}
				});
				return LuaValue.NIL;
			}
		});

		env.set("baconlib", library);
		return library;
	}
	
}
