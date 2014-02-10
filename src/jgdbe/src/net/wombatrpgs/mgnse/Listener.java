/**
 *  InputHandler.java
 *  Created on Aug 5, 2012 11:46:31 PM for project MGNSE
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgnse;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

import net.wombatrpgs.mgnse.wizard.Wizard;


/**
 * Handles all frame input and other user actions.
 */
public class Listener implements ActionListener, TreeSelectionListener {
	
	private Logic logic;
	private MainFrame frame;
	
	/**
	 * Initializes the input. Listeners must be added elsewhere.
	 * @param logic The game logic this input thing should call.
	 */
	public Listener(MainFrame frame, Logic logic) {
		this.logic = logic;
		this.frame = frame;
	}
	
	/**
	 * Called when the user tries to close the window.
	 * @param we The event that triggered the close
	 */
	public void onWindowClose(WindowEvent we) {
		logic.onApplicationQuit();
	}

	@Override
	/**
	 * Called for every menu action performed. Must respond to all of them!
	 * @param a The action performed
	 */
	public void actionPerformed(ActionEvent ae) {
		String cmd = ae.getActionCommand();
		if (cmd.equals(Action.QUIT_APP.toString())) logic.onApplicationQuit();
		if (cmd.equals(Action.OPEN_PROJECT.toString())) logic.requestOpenProject();
		if (cmd.equals(Action.CLOSE_PROJECT.toString())) logic.closeProject();
		if (cmd.equals(Action.SAVE.toString())) logic.save();
		if (cmd.equals(Action.SAVE_ALL.toString())) logic.saveAll();
		if (cmd.equals(Action.REVERT.toString())) logic.revert();
		if (cmd.equals(Action.REVERT_ALL.toString())) logic.revertAll();
		if (cmd.equals(Action.NEW_ENTRY.toString())) logic.newEntry();
		if (cmd.equals(Action.DELETE_ENTRY.toString())) logic.deleteEntry();
		if (cmd.equals(Action.CLONE_ENTRY.toString())) logic.cloneEntry();
		
		for (Wizard w : frame.getWizards()) {
			if (cmd.equals(w.getName())) {
				w.run();
			}
		}
		
	}

	@Override
	public void valueChanged(TreeSelectionEvent e) {
		logic.loadSelectedElement();
	}

}
