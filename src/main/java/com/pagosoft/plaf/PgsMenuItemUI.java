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

import javax.swing.*;
import javax.swing.plaf.*;
import javax.swing.plaf.basic.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;


public class PgsMenuItemUI extends BasicMenuItemUI {
	public static ComponentUI createUI(JComponent c) {
		return new PgsMenuItemUI();
	}

	private PropertyChangeListener menuItemIconChangeListener;

	protected void installDefaults() {
		super.installDefaults();
		if (menuItem.getIcon() == null) {
			menuItem.setIcon(PgsIconFactory.getEmptyIcon());
		}
	}

	protected void installListeners() {
		super.installListeners();
		if (menuItemIconChangeListener == null) {
			menuItemIconChangeListener = new IconChangeListener(menuItem);
		}
		menuItem.addPropertyChangeListener(JMenuItem.ICON_CHANGED_PROPERTY, menuItemIconChangeListener);
	}

	protected void uninstallListeners() {
		super.uninstallListeners();
		menuItem.removePropertyChangeListener(JMenuItem.ICON_CHANGED_PROPERTY, menuItemIconChangeListener);
	}

	protected void paintBackground(Graphics g, JMenuItem menuItem, Color bgColor) {
		PgsUtils.paintMenuItemBackground(g, menuItem, bgColor, getPropertyPrefix());
	}

	protected void paintText(Graphics g, JMenuItem c, Rectangle textRect, String text) {
		PgsUtils.installAntialiasing(g);
		super.paintText(g, c, textRect, text);
		PgsUtils.uninstallAntialiasing(g);
	}

	public static class IconChangeListener implements PropertyChangeListener {
		private AbstractButton button;

		public IconChangeListener(AbstractButton item) {
			button = item;
		}

		public void propertyChange(PropertyChangeEvent evt) {
			if (button.getClientProperty("allow.no.icon") == null && button.getIcon() == null) {
				button.setIcon(PgsIconFactory.getEmptyIcon());
			}
		}
	}
}
