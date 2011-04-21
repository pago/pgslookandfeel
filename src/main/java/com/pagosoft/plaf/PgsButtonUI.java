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

import com.pagosoft.swing.ColorUtils;

import javax.swing.*;
import javax.swing.plaf.*;
import javax.swing.plaf.basic.*;
import javax.swing.plaf.metal.*;
import java.awt.*;
import java.awt.event.*;
import java.lang.ref.WeakReference;

public class PgsButtonUI extends MetalButtonUI implements ActionListener {
	private static PgsButtonUI INSTANCE = new PgsButtonUI();

	private Timer defaultButtonTimer;
	private WeakReference defaultButtonRef;
	private int defaultButtonAlpha;
	private boolean defaultButtonAlphaDir;

	public static ComponentUI createUI(JComponent c) {
		if (c.getParent() instanceof JToolBar) {
			return ToolBarButtonUI.createUI(c);
		}
		return INSTANCE;
	}

	public PgsButtonUI() {
		defaultButtonTimer = new Timer(50, this);
	}

	public void installDefaults(AbstractButton b) {
		super.installDefaults(b);
		b.putClientProperty("rolloverBackground", UIManager.getColor("Button.rolloverBackground"));
		b.putClientProperty("pgs.isFlat", UIManager.get("Button.isFlat"));
		b.putClientProperty("gradientStart", UIManager.get("Button.gradientStart"));
		b.putClientProperty("gradientEnd", UIManager.get("Button.gradientEnd"));
		b.putClientProperty("rollover.gradientStart", UIManager.get("Button.rolloverGradientStart"));
		b.putClientProperty("rollover.gradientEnd", UIManager.get("Button.rolloverGradientEnd"));
		b.putClientProperty("selected.gradientStart", UIManager.get("Button.selectedGradientStart"));
		b.putClientProperty("selected.gradientEnd", UIManager.get("Button.selectedGradientEnd"));
	}

	protected BasicButtonListener createButtonListener(AbstractButton b) {
		return new RolloverButtonListener(b);
	}

	protected void paintFocus(
			Graphics g, AbstractButton b, Rectangle viewRect,
			Rectangle textRect, Rectangle iconRect) {

		if (b.isFocusPainted() && !(b instanceof PgsComboBoxButtonUI)) {
			int topLeftInset = 3;
			int width = b.getWidth() - topLeftInset * 2;
			int height = b.getHeight() - topLeftInset * 2;

			g.setColor(getFocusColor());
			PgsUtils.drawRoundRect(g, topLeftInset, topLeftInset, width - 1, height - 1, 3, 3);
		}
	}

	public void update(Graphics g, JComponent c) {
		JButton b = (JButton) c;
		if (c.isOpaque() && b.isContentAreaFilled()) {
			if (Boolean.TRUE.equals(c.getClientProperty("pgs.isFlat")) || !b.isEnabled()) {
				g.setColor(
						c.isEnabled() && b.getModel().isRollover() ? (Color) c.getClientProperty("rolloverBackground")
								: c.getBackground());
				g.fillRect(0, 0, c.getWidth(), c.getHeight());
			} else {
				g.setColor(
						c.isEnabled() && b.getModel().isRollover() ? (Color) c.getClientProperty("rolloverBackground")
								: c.getBackground());
				g.fillRect(0, 0, c.getWidth(), c.getHeight());
				if (c.isEnabled() && b.getModel().isRollover()) {
					PgsUtils.drawGradient(g, c, "rollover");
				} else {
					PgsUtils.drawGradient(g, c);
				}
			}
			if (b.isDefaultButton() && PgsUtils.hasFocus(b.getTopLevelAncestor())) {
				JButton defaultButton;
				if (defaultButtonRef == null) {
					defaultButton = null;
				} else {
					defaultButton = (JButton) defaultButtonRef.get();
				}
				// We need to re-apply our defaultButton-setup
				if (b != defaultButton) {
					// we had no defaultButton before
					if (defaultButton == null) {
						defaultButton = b;
						defaultButtonRef = new WeakReference(b);
						defaultButtonTimer.start();
					} else {
						JButton temp = defaultButton;
						defaultButton = b;
						defaultButtonRef = new WeakReference(b);
						temp.repaint();
					}
					defaultButtonAlpha = 10;
					defaultButtonAlphaDir = true;
				}
				// let's paint the button
				PgsUtils.drawButtonBorder(
						g,
						1, 1, b.getWidth() - 3, b.getHeight() - 3,
						PgsUtils.rolloverBorderStroke,
						ColorUtils.getTranslucentColor(
								PgsLookAndFeel.getPrimaryControlShadow(),
								defaultButtonAlpha));
			}
		}
		super.paint(g, c);
	}

	protected void paintButtonPressed(Graphics g, AbstractButton b) {
		if (b.isOpaque() && b.isContentAreaFilled()) {
			if (Boolean.TRUE.equals(b.getClientProperty("pgs.isFlat"))) {
				g.setColor(getSelectColor());
				g.fillRect(0, 0, b.getWidth(), b.getHeight());
			} else {
				PgsUtils.drawGradient(g, b, "selected");
			}
		}
	}

	protected void paintText(Graphics g, JComponent c, Rectangle textRect, String text) {
		PgsUtils.installAntialiasing(g);
		super.paintText(g, c, textRect, text);
		PgsUtils.uninstallAntialiasing(g);
	}

	public void actionPerformed(ActionEvent e) {
		JButton defaultButton = (JButton) defaultButtonRef.get();
		// no default button, thus stop running
		if (defaultButton == null && defaultButtonTimer.isRunning()) {
			defaultButtonTimer.stop();
			defaultButtonRef = null;
			return;
		}
		defaultButtonAlpha += (defaultButtonAlphaDir) ? 10 : -10;
		if (defaultButtonAlpha == 200 || defaultButtonAlpha == 10) {
			defaultButtonAlphaDir = !defaultButtonAlphaDir;
		}
		defaultButton.repaint();
	}
}
