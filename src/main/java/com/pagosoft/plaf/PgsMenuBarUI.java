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

public class PgsMenuBarUI extends BasicMenuBarUI {
	public static ComponentUI createUI(JComponent c) {
		return new PgsMenuBarUI();
	}

	public void paint(Graphics g, JComponent b) {
		if (b.isOpaque()) {
			if (PgsUtils.isFlat("MenuBar")) {
				g.setColor(b.getBackground());
				g.fillRect(0, 0, b.getWidth(), b.getHeight());
			} else if (PlafOptions.isVistaStyle()) {
				PgsUtils.drawVistaBackground(g, b, "MenuBar");
			} else {
				PgsUtils.drawGradient(
						g, 0, 0, b.getWidth(), b.getHeight(),
						UIManager.getColor("MenuBar.gradientStart"),
						UIManager.getColor("MenuBar.gradientEnd"));
			}
		}
	}
}
