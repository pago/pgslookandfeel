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
import java.awt.*;

public class ToolBarToggleButtonUI extends PgsToggleButtonUI {
	private static ToolBarToggleButtonUI INSTANCE = new ToolBarToggleButtonUI();

	public static ComponentUI createUI(JComponent c) {
		return INSTANCE;
	}

	public void installDefaults(AbstractButton b) {
		super.installDefaults(b);
		// Just a hack, but makes life much easier
		b.setBorderPainted(false);
		b.setOpaque(false);
		b.setMargin(UIManager.getInsets("ToolBarButton.margin"));
		b.putClientProperty("rolloverBackground", UIManager.getColor("ToolBarButton.rolloverBackground"));
		b.putClientProperty("rolloverBorderColor", UIManager.get("ToolBarButton.rolloverBorderColor"));
		b.putClientProperty("pgs.isFlat", UIManager.get("ToolBarButton.isFlat"));
		b.putClientProperty("gradientStart", UIManager.get("ToolBarButton.gradientStart"));
		b.putClientProperty("gradientEnd", UIManager.get("ToolBarButton.gradientEnd"));
		b.putClientProperty("rollover.gradientStart", UIManager.get("ToolBarButton.rolloverGradientStart"));
		b.putClientProperty("rollover.gradientEnd", UIManager.get("ToolBarButton.rolloverGradientEnd"));
		b.putClientProperty("selected.gradientStart", UIManager.get("ToolBarButton.selectedGradientStart"));
		b.putClientProperty("selected.gradientEnd", UIManager.get("ToolBarButton.selectedGradientEnd"));

		Icon ico = b.getIcon();
		if (ico != null && (ico instanceof ImageIcon)) {
			Image img = ((ImageIcon) ico).getImage();
			if (b.getDisabledIcon() == null) {
				b.setDisabledIcon(PgsUtils.getDisabledButtonIcon(img));
			}
			if (b.getDisabledSelectedIcon() == null) {
				b.setDisabledSelectedIcon(PgsUtils.getDisabledButtonIcon(img));
			}
			if (b.getRolloverIcon() == null) {
				b.setRolloverIcon(PgsUtils.getToolBarIcon(img));
			}
			if (b.getRolloverSelectedIcon() == null) {
				b.setRolloverSelectedIcon(PgsUtils.getToolBarIcon(img));
			}
		}
	}

	public void update(Graphics g, JComponent c) {
		AbstractButton b = (AbstractButton) c;

		// Rollover ?
		if (c.isEnabled() && b.getModel().isRollover()) {
			if (Boolean.TRUE.equals(c.getClientProperty("pgs.isFlat"))) {
				g.setColor((Color) c.getClientProperty("rolloverBackground"));
				g.fillRect(0, 0, b.getWidth() - 1, b.getHeight() - 1);
				g.setColor((Color) c.getClientProperty("rolloverBorderColor"));
				g.drawRect(0, 0, b.getWidth() - 1, b.getHeight() - 1);
			} else {
				PgsUtils.drawGradient(g, c, "rollover", 1, 1, b.getWidth()-1, b.getHeight()-1);
				PgsUtils.drawButtonBorder(g, 0, 0, b.getWidth()-1, b.getHeight()-1,
						(Color)c.getClientProperty("rolloverBorderColor"));
			}
		}
		super.paint(g, c);
	}

	protected void paintButtonPressed(Graphics g, AbstractButton b) {
		if (Boolean.TRUE.equals(b.getClientProperty("pgs.isFlat"))) {
			g.setColor(getSelectColor());
			g.fillRect(0, 0, b.getWidth(), b.getHeight());
		} else {
			PgsUtils.drawGradient(g, b, "selected", 1, 1, b.getWidth()-1, b.getHeight()-1);
		}
		super.paintButtonPressed(g, b);
		PgsUtils.drawButtonBorder(g, 0, 0, b.getWidth()-1, b.getHeight()-1,
						(Color) b.getClientProperty("rolloverBorderColor"));
	}
}
