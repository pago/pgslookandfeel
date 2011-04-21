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
import javax.swing.plaf.UIResource;
import java.awt.*;
import java.io.Serializable;

public class PgsIconFactory {
	private static Icon emptyIcon;

	public static Icon getEmptyIcon() {
		if (emptyIcon == null) {
			emptyIcon = new EmptyIcon(PlafOptions.getDefaultMenuItemIconSize());
		}
		return emptyIcon;
	}

	private static class EmptyIcon implements Icon, UIResource, Serializable {
		private Dimension size;

		public EmptyIcon(Dimension dim) {
			size = dim;
		}

		public void paintIcon(Component c, Graphics g, int x, int y) {
			return;
		}

		public int getIconWidth() {
			return size.width;
		}

		public int getIconHeight() {
			return size.height;
		}
	}

	protected static void drawCheck(Component c, Graphics g, int x, int y) {
		g.translate(x, y);
		g.drawLine(3, 5, 3, 5);
		g.fillRect(3, 6, 2, 2);
		g.drawLine(4, 8, 9, 3);
		g.drawLine(5, 8, 9, 4);
		g.drawLine(5, 9, 9, 5);
		g.translate(-x, -y);
	}

	private static Icon checkBoxIcon;

	public static Icon getCheckBoxIcon() {
		if (checkBoxIcon == null) {
			checkBoxIcon = new CheckBoxIcon();
		}
		return checkBoxIcon;
	}

	private static class CheckBoxIcon implements Icon, UIResource, Serializable {
		protected int getControlSize() {
			return 13;
		}

		public void paintIcon(Component c, Graphics g, int x, int y) {
			JCheckBox cb = (JCheckBox) c;
			ButtonModel model = cb.getModel();
			int controlSize = getControlSize();

			boolean drawCheck = model.isSelected();

			if (model.isEnabled()) {
				// draw background
				g.setColor(PgsLookAndFeel.getControlHighlight());
				g.fillRect(x, y, controlSize, controlSize);
				// slight gradient from center towards bottom right corner
				PgsUtils.drawGradient(g, x, y,
						controlSize, controlSize,
						ColorUtils.getTranslucentColor(PgsLookAndFeel.getControlShadow(), 0),
						ColorUtils.getTranslucentColor(PgsLookAndFeel.getControlShadow(), 50));

				if (model.isRollover()) {
					g.setColor(PgsLookAndFeel.getGlow());
					g.drawRect(x + 1, y + 1, controlSize - 3, controlSize - 3);
				}
				g.setColor(PgsLookAndFeel.getControlDarkShadow());
				g.drawRect(x, y, controlSize - 1, controlSize - 1);
				if (model.isPressed() && model.isArmed()) {
					g.setColor(PgsLookAndFeel.getControlShadow());
					g.fillRect(x + 1, y + 1, controlSize - 2, controlSize - 2);
				}
				g.setColor(PgsLookAndFeel.getControlInfo());
			} else {
				g.setColor(PgsLookAndFeel.getControlShadow());
				g.drawRect(x, y, controlSize - 1, controlSize - 1);
			}


			if (drawCheck) {
				//x++;
				g.setColor(
						model.isEnabled() ? PgsLookAndFeel.getPrimaryControlDarkShadow()
								: PgsLookAndFeel.getControlShadow());
				drawCheck(c, g, x, y);
			}
		}

		public int getIconWidth() {
			return getControlSize();
		}

		public int getIconHeight() {
			return getControlSize();
		}
	}

	private static Icon checkBoxMenuItemIcon;

	public static Icon getCheckBoxMenuItemIcon() {
		if (checkBoxMenuItemIcon == null) {
			checkBoxMenuItemIcon = new CheckBoxMenuItemIcon();
		}
		return checkBoxMenuItemIcon;
	}

	/**
	 * @todo Redo this one... it is ABSOLUTLY the same as CheckBoxIcon...
	 */
	private static class CheckBoxMenuItemIcon implements Icon, UIResource, Serializable {
		protected int getControlSize() {
			return 13;
		}

		public void paintIcon(Component c, Graphics g, int x, int y) {
			JMenuItem cb = (JMenuItem) c;
			ButtonModel model = cb.getModel();
			int controlSize = getControlSize();

			boolean drawCheck = model.isSelected();

			if (model.isEnabled()) {
				// draw background
				g.setColor(PgsLookAndFeel.getControlHighlight());
				g.fillRect(x, y, controlSize, controlSize);
				if (model.isRollover()) {
					g.setColor(PgsLookAndFeel.getPrimaryControl());
					g.drawRect(x + 1, y + 1, controlSize - 3, controlSize - 3);
				}
				g.setColor(PgsLookAndFeel.getControlDarkShadow());
				g.drawRect(x, y, controlSize - 1, controlSize - 1);
				if (model.isPressed() && model.isArmed()) {
					g.setColor(PgsLookAndFeel.getControlShadow());
					g.fillRect(x + 1, y + 1, controlSize - 2, controlSize - 2);
				}
				g.setColor(PgsLookAndFeel.getControlInfo());
			} else {
				g.setColor(PgsLookAndFeel.getControlShadow());
				g.drawRect(x, y, controlSize - 1, controlSize - 1);
			}


			if (drawCheck) {
				g.setColor(
						model.isEnabled() ? PgsLookAndFeel.getPrimaryControlDarkShadow()
								: PgsLookAndFeel.getControlShadow());
				drawCheck(c, g, x, y);
			}
		}

		public int getIconWidth() {
			return getControlSize();
		}

		public int getIconHeight() {
			return getControlSize();
		}
	}

	private static Icon radioButtonIcon;

	public static Icon getRadioButtonIcon() {
		if (radioButtonIcon == null) {
			radioButtonIcon = new RadioButtonIcon();
		}
		return radioButtonIcon;
	}

	private static Icon radioMenuItemIcon;

	public static Icon getRadioButtonMenuItemIcon() {
		if (radioMenuItemIcon == null) {
			radioMenuItemIcon = new RadioButtonIcon();
		}
		return radioMenuItemIcon;
	}

	private static class RadioButtonIcon implements Icon, UIResource, Serializable {
		protected int getControlSize() {
			return 13;
		}

		public void paintIcon(Component c, Graphics g, int x, int y) {
			AbstractButton cb = (AbstractButton) c;
			ButtonModel model = cb.getModel();
			int controlSize = getControlSize();

			boolean drawDot = model.isSelected();

			Graphics2D gfx = (Graphics2D) g;
			gfx.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			if (model.isEnabled()) {
				// draw background
				gfx.setColor(PgsLookAndFeel.getControlHighlight());
				gfx.fillOval(x, y, controlSize, controlSize);
				if (model.isRollover()) {
					gfx.setColor(PgsLookAndFeel.getGlow());
					gfx.drawOval(x + 1, y + 1, controlSize - 3, controlSize - 3);
				}
				gfx.setColor(PgsLookAndFeel.getControlDarkShadow());
				gfx.drawOval(x, y, controlSize - 1, controlSize - 1);
				if (model.isPressed() && model.isArmed()) {
					gfx.setColor(PgsLookAndFeel.getControlShadow());
					gfx.fillOval(x + 1, y + 1, controlSize - 2, controlSize - 2);
				}
				gfx.setColor(PgsLookAndFeel.getControlInfo());
			} else {
				gfx.setColor(PgsLookAndFeel.getControlShadow());
				gfx.drawOval(x, y, controlSize - 1, controlSize - 1);
			}


			if (drawDot) {
				if(model.isEnabled()) {
					//gfx.setPaint(new GradientPaint(x + 3, y + 3, PgsLookAndFeel.getPrimaryControlShadow(), controlSize - 6, controlSize - 6, PgsLookAndFeel.getPrimaryControlDarkShadow()));
					gfx.setColor(PgsLookAndFeel.getPrimaryControlDarkShadow());
				} else {
				gfx.setColor(
						model.isEnabled() ? PgsLookAndFeel.getPrimaryControlDarkShadow()
								: PgsLookAndFeel.getControlShadow());
				}
				gfx.fillOval(x + 3, y + 3, controlSize - 6, controlSize - 6);
			}
			gfx.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
		}

		public int getIconWidth() {
			return getControlSize();
		}

		public int getIconHeight() {
			return getControlSize();
		}
	}
}
