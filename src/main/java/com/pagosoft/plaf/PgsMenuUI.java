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


public class PgsMenuUI extends BasicMenuUI {
	public static ComponentUI createUI(JComponent c) {
		return new PgsMenuUI();
	}

	protected void installListeners() {
		super.installListeners();
		menuItem.addMouseListener(createButtonListener(menuItem));
	}

	protected void uninstallListeners() {
		super.uninstallListeners();
	}

	protected BasicButtonListener createButtonListener(AbstractButton b) {
		return new RolloverButtonListener(b);
	}

	protected void paintBackground(Graphics g, JMenuItem menuItem, Color bgColor) {
		ButtonModel model = menuItem.getModel();
		Color oldColor = g.getColor();
		Dimension size = menuItem.getSize();

		if (menuItem.getParent() instanceof JMenuBar) {
			if (menuItem.isEnabled() && (model.isArmed() || (menuItem instanceof JMenu && model.isSelected()) || model
					.isRollover())) {
				if (PgsUtils.isFlat("MenuBarMenu")) {
					if (model.isSelected()) {
						g.setColor(
								UIManager.getColor(
										"MenuBarMenu.selectedBackground"));
					} else {
						g.setColor(
								UIManager.getColor(
										"MenuBarMenu.rolloverBackground"));
					}
					g.fillRect(0, 0, size.width, size.height);
				} else if (PlafOptions.isVistaStyle()) {
					if (model.isRollover() && !model.isSelected()) {
						PgsUtils.drawVistaBackground(g, menuItem, "MenuBarMenu.rolloverBackground");
					} else {
						PgsUtils.drawVistaBackground(g, menuItem, "MenuBarMenu.selectedBackground");
					}
				} else {
					if (model.isRollover() && !model.isSelected()) {
						PgsUtils.drawGradient(
								g, menuItem.getWidth(), menuItem.getHeight(),
								(Color) UIManager.getColor("MenuBarMenu.rolloverBackgroundGradientStart"),
								(Color) UIManager.getColor("MenuBarMenu.rolloverBackgroundGradientEnd"));
					} else {
						PgsUtils.drawGradient(
								g, menuItem.getWidth(), menuItem.getHeight(),
								(Color) UIManager.getColor("MenuBarMenu.selectedBackgroundGradientStart"),
								(Color) UIManager.getColor("MenuBarMenu.selectedBackgroundGradientEnd"));
					}
				}
				if (model.isRollover() && !model.isSelected()) {
					// draw a border if the menu is hovered but not selected
					g.setColor(UIManager.getColor("MenuBarMenu.rolloverBorderColor"));
					g.drawRect(0, 0, size.width - 1, size.height - 1);
				} else {
					g.setColor(UIManager.getColor("MenuBarMenu.selectedBorderColor"));
					g.drawLine(0, 0, 0, size.height - 1);
					g.drawLine(size.width - 1, 0, size.width - 1, size.height - 1);
					g.drawLine(0, 0, size.width - 1, 0);
				}
			} else {
				if (PgsUtils.isFlat("MenuBar")) {
					g.setColor(UIManager.getColor("MenuBar.background"));
					g.fillRect(0, 0, size.width, size.height);
				} else if (PlafOptions.isVistaStyle()) {
					PgsUtils.drawVistaBackground(g, menuItem, "MenuBar");
				} else {
					PgsUtils.drawGradient(
							g, menuItem.getWidth(), menuItem.getHeight(),
							(Color) UIManager.getColor("MenuBar.gradientStart"),
							(Color) UIManager.getColor("MenuBar.gradientEnd"));
				}
			}
		} else {
			if (menuItem.getIcon() == null) {
				menuItem.setIcon(PgsIconFactory.getEmptyIcon());
			}
			PgsUtils.paintMenuItemBackground(g, menuItem, bgColor, getPropertyPrefix());
		}
		g.setColor(oldColor);
		//}
	}

	protected void paintText(Graphics g, JMenuItem c, Rectangle textRect, String text) {
		PgsUtils.installAntialiasing(g);
		if (c.getParent() instanceof JMenuBar) {
			c.setForeground(UIManager.getColor("MenuBarMenu.foreground"));
			selectionForeground = UIManager.getColor("MenuBarMenu.foreground");
		}
		super.paintText(g, c, textRect, text);
		PgsUtils.uninstallAntialiasing(g);
	}
}
