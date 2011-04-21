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

public class PgsRadioButtonMenuItemUI extends BasicRadioButtonMenuItemUI {
	public static ComponentUI createUI(JComponent c) {
		return new PgsRadioButtonMenuItemUI();
	}

	protected void paintBackground(Graphics g, JMenuItem menuItem, Color bgColor) {
		PgsUtils.paintMenuItemBackground(g, menuItem, bgColor, getPropertyPrefix());
	}

	protected void paintText(Graphics g, JMenuItem c, Rectangle textRect, String text) {
		PgsUtils.installAntialiasing(g);
		super.paintText(g, c, textRect, text);
		PgsUtils.uninstallAntialiasing(g);
	}
}
