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
import javax.swing.plaf.metal.*;
import java.awt.*;

public class PgsToggleButtonUI extends MetalToggleButtonUI {
	private static PgsToggleButtonUI INSTANCE = new PgsToggleButtonUI();

	public static ComponentUI createUI(JComponent c) {
		if (c.getParent() instanceof JToolBar) {
			return ToolBarToggleButtonUI.createUI(c);
		}
		return INSTANCE;
	}

	public void installDefaults(AbstractButton b) {
		super.installDefaults(b);
		b.setFocusPainted(false);
		b.putClientProperty("rolloverBackground", UIManager.getColor("ToggleButton.rolloverBackground"));
		b.putClientProperty("pgs.isFlat", UIManager.get("ToggleButton.isFlat"));
		b.putClientProperty("gradientStart", UIManager.get("ToggleButton.gradientStart"));
		b.putClientProperty("gradientEnd", UIManager.get("ToggleButton.gradientEnd"));
		b.putClientProperty("rollover.gradientStart", UIManager.get("ToggleButton.rolloverGradientStart"));
		b.putClientProperty("rollover.gradientEnd", UIManager.get("ToggleButton.rolloverGradientEnd"));
		b.putClientProperty("selected.gradientStart", UIManager.get("ToggleButton.selectedGradientStart"));
		b.putClientProperty("selected.gradientEnd", UIManager.get("ToggleButton.selectedGradientEnd"));
	}

	protected BasicButtonListener createButtonListener(AbstractButton b) {
		return new RolloverButtonListener(b);
	}

	protected void paintFocus(
			Graphics g, AbstractButton b, Rectangle viewRect,
			Rectangle textRect, Rectangle iconRect) {

		int topLeftInset = 3;
		int width = b.getWidth() - topLeftInset * 2;
		int height = b.getHeight() - topLeftInset * 2;

		g.setColor(getFocusColor());
		PgsUtils.drawRoundRect(g, topLeftInset, topLeftInset, width - 1, height - 1, 3, 3);
	}

	public void update(Graphics g, JComponent c) {
		AbstractButton b = (AbstractButton) c;
		if (c.isOpaque()) {
			if (b.isContentAreaFilled()) {
				if (Boolean.TRUE.equals(c.getClientProperty("pgs.isFlat")) || !b.isEnabled()) {
					g.setColor(
							c.isEnabled() && b.getModel().isRollover() ? (Color) c
									.getClientProperty("rolloverBackground") : c.getBackground());
					g.fillRect(0, 0, c.getWidth(), c.getHeight());
				} else {
					if (c.isEnabled() && b.getModel().isRollover()) {
						PgsUtils.drawGradient(g, c, "rollover");
					} else {
						PgsUtils.drawGradient(g, c);
					}
				}
			}
		}
		super.paint(g, c);
	}

	protected void paintButtonPressed(Graphics g, AbstractButton b) {
		if (b.isContentAreaFilled()) {
			if (Boolean.TRUE.equals(b.getClientProperty("pgs.isFlat"))) {
				g.setColor(getSelectColor());
				g.fillRect(0, 0, b.getWidth(), b.getHeight());
			} else {
				PgsUtils.drawGradient(g, b, "selected");
			}
		}
	}
}
