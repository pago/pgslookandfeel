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
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicHTML;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.plaf.metal.MetalToolTipUI;
import javax.swing.text.View;
import java.awt.*;

public class PgsToolTipUI extends MetalToolTipUI {
	public static ComponentUI createUI(JComponent c) {
		return new PgsToolTipUI();
	}

	private Font smallFont;

	public void installUI(JComponent c) {
		super.installUI(c);

		Font f = c.getFont();
		smallFont = new Font( f.getName(), f.getStyle(), f.getSize() - 2 );

		c.putClientProperty("gradientStart", UIManager.getColor("Tooltip.gradientStart"));
		c.putClientProperty("gradientEnd", UIManager.getColor("Tooltip.gradientEnd"));
	}

	public final void paint(Graphics g, JComponent c) {
		PgsUtils.installAntialiasing(g);
		JToolTip tip = (JToolTip)c;
		Dimension size = c.getSize();

		super.paint(g, c);

		PgsUtils.drawGradient(g, c);

		Font font = c.getFont();
		FontMetrics metrics = c.getFontMetrics(font);
		String keyText = getAcceleratorString();
		String tipText = tip.getTipText();
		if (tipText == null) {
			tipText = "";
		}
		g.setColor(c.getForeground());
		Insets insets = c.getInsets();
		Rectangle paintTextR = new Rectangle(
				insets.left,
				insets.top,
				size.width - (insets.left + insets.right),
				size.height - (insets.top + insets.bottom));
		View v = (View) c.getClientProperty(BasicHTML.propertyKey);
		if (v != null) {
			v.paint(g, paintTextR);
		} else {
			g.drawString(tipText, paintTextR.x + 3,paintTextR.y + metrics.getAscent());
		}
		if (! (keyText.equals(""))) {  // only draw control key if there is one
			g.setFont(smallFont);
			g.setColor( MetalLookAndFeel.getBlack() );
			g.drawString(keyText,
					metrics.stringWidth(tipText) + padSpaceBetweenStrings,
					2 + metrics.getAscent());
		}
		PgsUtils.uninstallAntialiasing(g);
	}
}
