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
import javax.swing.plaf.metal.*;
import java.awt.*;
import java.awt.event.*;

public class PgsToolBarUI extends MetalToolBarUI {
	public static ComponentUI createUI(JComponent c) {
		return new PgsToolBarUI();
	}

	public void installUI(JComponent c) {
		super.installUI(c);
		c.putClientProperty("gradientStart", UIManager.get("ToolBar.gradientStart"));
		c.putClientProperty("gradientEnd", UIManager.get("ToolBar.gradientEnd"));

		int limit = c.getComponentCount();
		for (int i = 0; i < limit; i++) {
			setToolBarUI((JComponent) c.getComponent(i));
		}
	}

	public void paint(Graphics g, JComponent b) {
		if (b.isOpaque()) {
			if (PlafOptions.getStyle(PlafOptions.TOOLBAR) == PlafOptions.FLAT_STYLE) {
				g.setColor(b.getBackground());
				g.drawRect(0, 0, b.getWidth(), b.getHeight());
			} else {
				int orient = ((JToolBar) b).getOrientation();
				if (orient == JToolBar.HORIZONTAL) {
					PgsUtils.drawGradient(g, b);
				} else {
					PgsUtils.drawGradient(
							g, 0, 0, b.getWidth(), b.getHeight(),
							(Color) b.getClientProperty("gradientStart"),
							(Color) b.getClientProperty("gradientEnd"));
				}
			}
		}
		super.paint(g, b);
	}

	protected ContainerListener createContainerListener() {
		return new ToolBarContListener();
	}

	protected void setToolBarUI(JComponent c) {
		if (c instanceof JButton) {
			((AbstractButton) c).setUI((ToolBarButtonUI) ToolBarButtonUI.createUI(c));
		} else if (c instanceof JToggleButton) {
			((AbstractButton) c).setUI((ToolBarToggleButtonUI) ToolBarToggleButtonUI.createUI(c));
		}
	}

	protected class ToolBarContListener implements ContainerListener {
		public void componentAdded(ContainerEvent e) {
			setToolBarUI((JComponent) e.getChild());
		}

		public void componentRemoved(ContainerEvent e) {
			JComponent c = (JComponent) e.getChild();

			if (c instanceof JButton || c instanceof JToggleButton) {
				((AbstractButton) c).setUI((ButtonUI) UIManager.getUI(c));
			}
		}
	}
}
