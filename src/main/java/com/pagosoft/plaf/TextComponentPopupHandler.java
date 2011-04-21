/*
 * Copyright 2005 Patrick Gotthardt
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.pagosoft.plaf;

import com.pagosoft.OS;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.event.*;
import java.util.ResourceBundle;

/**
 * @author Patrick Gotthardt
 */
public class TextComponentPopupHandler extends MouseAdapter {
	private static TextComponentPopupHandler INSTANCE;

	public static TextComponentPopupHandler getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new TextComponentPopupHandler();
		}
		return INSTANCE;
	}


	private ResourceBundle bundle;

	private Action cutAction;
	private Action copyAction;
	private Action pasteAction;
	private Action deleteAction;
	private Action selectAllAction;

	private JPopupMenu popupMenu;
	private JTextComponent comp;

	private TextComponentPopupHandler() {
		bundle = ResourceBundle.getBundle("com.pagosoft.plaf.Bundle");

		cutAction = new CutAction();
		copyAction = new CopyAction();
		pasteAction = new PasteAction();
		deleteAction = new DeleteAction();
		selectAllAction = new SelectAllAction();


	}

	public ImageIcon getIcon(String name) {
		try {
			return new ImageIcon(TextComponentPopupHandler.class.getResource("/com/pagosoft/plaf/icons/" + name));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public void mousePressed(MouseEvent e) {
		if ((!e.isPopupTrigger()
				// isPopupTrigger() doesn't work for me, so check if we're not on Mac and look for the right mouse button
				&& !(!OS.isMacOsX() && e.getButton() == MouseEvent.BUTTON3))
				// does the user himself take care about the menu?
				|| MenuSelectionManager.defaultManager()
				.getSelectedPath().length > 0) {
			return;
		}

		if(!(e.getComponent() instanceof JTextComponent)) {
			return;
		}

		comp = (JTextComponent) e.getComponent();

		popupMenu = new JPopupMenu();
		popupMenu.add(cutAction);
		popupMenu.add(copyAction);
		popupMenu.add(pasteAction);
		popupMenu.add(deleteAction);
		popupMenu.addSeparator();
		popupMenu.add(selectAllAction);

		// show the menu
		popupMenu.show(comp, e.getX(), e.getY());
	}

	private class CutAction extends AbstractAction {
		public CutAction() {
			super(bundle.getString("textcomponent.cut"), getIcon("editcut.png"));
			putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control X"));
		}

		public void actionPerformed(ActionEvent e) {
			comp.cut();
		}

		public boolean isEnabled() {
			return comp.isEditable()
					&& comp.isEnabled()
					&& comp.getSelectedText() != null;
		}
	}

	private class CopyAction extends AbstractAction {
		public CopyAction() {
			super(bundle.getString("textcomponent.copy"), getIcon("editcopy.png"));
			putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control C"));
		}

		public void actionPerformed(ActionEvent e) {
			comp.copy();
		}

		public boolean isEnabled() {
			return comp.isEnabled() && comp.getSelectedText() != null;
		}
	}

	private class PasteAction extends AbstractAction {
		public PasteAction() {
			super(bundle.getString("textcomponent.paste"), getIcon("editpaste.png"));
			putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control V"));
		}

		public void actionPerformed(ActionEvent e) {
			comp.paste();
		}

		public boolean isEnabled() {
			if (comp.isEditable() && comp.isEnabled()) {
				Transferable contents = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(this);
				return contents.isDataFlavorSupported(DataFlavor.stringFlavor);
			} else {
				return false;
			}
		}
	}

	private class DeleteAction extends AbstractAction {
		public DeleteAction() {
			super(bundle.getString("textcomponent.delete"), getIcon("editdelete.png"));
		}

		public void actionPerformed(ActionEvent e) {
			comp.replaceSelection(null);
		}

		public boolean isEnabled() {
			return comp.isEditable()
					&& comp.isEnabled()
					&& comp.getSelectedText() != null;
		}
	}

	private class SelectAllAction extends AbstractAction {
		public SelectAllAction() {
			super(bundle.getString("textcomponent.selectall"));
			putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control A"));
		}

		public void actionPerformed(ActionEvent e) {
			comp.selectAll();
		}

		public boolean isEnabled() {
			return comp.isEnabled() && comp.getText().length() > 0;
		}
	}
}
