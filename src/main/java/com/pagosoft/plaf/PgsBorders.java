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
import javax.swing.text.JTextComponent;
import javax.swing.border.*;
import javax.swing.plaf.*;
import javax.swing.plaf.basic.*;
import javax.swing.plaf.metal.*;
import java.awt.*;

public class PgsBorders {
	public static class Generic extends AbstractBorder implements UIResource {
		private Insets insets;
		private Color color;
		public Generic(Insets insets, Color color) {
			this.insets = insets;
			this.color = color;
		}

		public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
			g.setColor(color);
			if(insets.left != 0) {
				g.fillRect(x, y, x+insets.left, y+height);
			}
			if(insets.right != 0) {
				g.fillRect(x+width-insets.right, y, x+width, y+height);
			}
			if(insets.top != 0) {
				g.fillRect(x, y, x+width, x+insets.top);
			}
			if(insets.bottom != 0) {
				g.fillRect(x, y+height-insets.bottom, x+width, y+height);
			}
		}

		public Insets getBorderInsets(Component c) {
			return insets;
		}

		public Insets getBorderInsets(Component c, Insets insets) {
			return insets;
		}
	}

	private static Border buttonBorder;

	public static Border getButtonBorder() {
		if (buttonBorder == null) {
			buttonBorder = new BorderUIResource.CompoundBorderUIResource(
					new ButtonBorder(),
					new BasicBorders.MarginBorder());
		}
		return buttonBorder;
	}

	private static class ButtonBorder extends AbstractBorder implements UIResource {
		protected static final Insets INSETS = new Insets(3, 4, 3, 4);
		protected static final Insets NO_INSETS = new Insets(0, 0, 0, 0);

		public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
			AbstractButton button = (AbstractButton) c;
			ButtonModel model = button.getModel();

			if (model.isEnabled()) {
				if (model.isRollover() && PlafOptions.isPaintRolloverButtonBorder()) {
					PgsUtils.drawRolloverButtonBorder(g, x, y, w, h);
				} else {
					PgsUtils.drawButtonBorder(g, x, y, w, h);
				}
			} else { // disabled state
				PgsUtils.drawDisabledBorder(g, x, y, w - 1, h - 1);
			}
		}

		public Insets getBorderInsets(Component c) {
			if (((AbstractButton) c).isBorderPainted()) {
				return INSETS;
			}
			return NO_INSETS;
		}

		public Insets getBorderInsets(Component c, Insets newInsets) {
			if (((AbstractButton) c).isBorderPainted()) {
				return getBorderInsets(c, newInsets, INSETS);
			}
			return getBorderInsets(c, newInsets, NO_INSETS);
		}

		public Insets getBorderInsets(Component c, Insets newInsets, Insets oldInsets) {
			newInsets.top = oldInsets.top;
			newInsets.left = oldInsets.left;
			newInsets.bottom = oldInsets.bottom;
			newInsets.right = oldInsets.right;
			return newInsets;
		}
	}

	private static Border textFieldBorder;

	public static Border getTextFieldBorder() {
		if (textFieldBorder == null) {
			textFieldBorder = new BorderUIResource.CompoundBorderUIResource(
					new TextFieldBorder(),
					new BasicBorders.MarginBorder());
		}
		return textFieldBorder;
	}

	private static class TextFieldBorder extends AbstractBorder {
		private static final Insets INSETS = new Insets(1, 1, 1, 1);

		//private static final Insets INSETS = new Insets(0, 0, 0, 0);
		public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
			if (c.isEnabled()) {
				//PgsUtils.drawButtonBorder(g, x, y, w, h);
				boolean isEditable = (c instanceof JTextComponent && ((JTextComponent)c).isEditable()) || (c instanceof JComboBox && ((JComboBox)c).isEditable());
				boolean hasFocus = c.hasFocus();
				if(c instanceof JComboBox) {
					JComboBox comboBox = ((JComboBox) c);
					hasFocus = comboBox.isEditable() && comboBox.getEditor().getEditorComponent().hasFocus();
				}
				PgsUtils.drawButtonBorder(g, x, y, w - 1, h - 1, 2, 2, hasFocus && isEditable ? PgsLookAndFeel.getPrimaryControlDarkShadow() : PgsLookAndFeel.getControlDarkShadow());
			} else {
				PgsUtils.drawDisabledBorder(g, x, y, w - 1, h - 1, 2, 2);
			}

		}

		public Insets getBorderInsets(Component c) {
			return INSETS;
		}

		public Insets getBorderInsets(Component c, Insets newInsets) {
			newInsets.top = INSETS.top;
			newInsets.left = INSETS.left;
			newInsets.bottom = INSETS.bottom;
			newInsets.right = INSETS.right;
			return newInsets;
		}
	}

	private static Border componentBorder;

	public static Border getComponentBorder() {
		if (componentBorder == null) {
			componentBorder = new BorderUIResource.CompoundBorderUIResource(
					new ComponentBorder(),
					new BasicBorders.MarginBorder());
		}
		return componentBorder;
	}

	private static class ComponentBorder extends AbstractBorder {
		private static final Insets INSETS = new Insets(1, 1, 1, 1);

		public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
			if (c.isEnabled()) {
				PgsUtils.drawButtonBorder(g, x, y, w, h);
			} else {
				PgsUtils.drawDisabledBorder(g, x, y, w - 1, h - 1);
			}
		}

		public Insets getBorderInsets(Component c) {
			return INSETS;
		}

		public Insets getBorderInsets(Component c, Insets newInsets) {
			newInsets.top = INSETS.top;
			newInsets.left = INSETS.left;
			newInsets.bottom = INSETS.bottom;
			newInsets.right = INSETS.right;
			return newInsets;
		}
	}

	private static Border toolBarBorder;

	public static Border getToolBarBorder() {
		if (toolBarBorder == null) {
			toolBarBorder = new BorderUIResource(new ToolBarBorder());
		}
		return toolBarBorder;
	}

	public static class ToolBarBorder extends AbstractBorder implements UIResource, SwingConstants {
		public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
			g.translate(x, y);

			if (((JToolBar) c).isFloatable()) {
				if (((JToolBar) c).getOrientation() == HORIZONTAL) {
					PgsUtils.drawVerticalBumps(g, 2, 4, c.getHeight() - 6);
				} else {
					PgsUtils.drawHorizontalBumps(g, 4, 2, c.getWidth() - 6);
				}
			}

			if (((JToolBar) c).getOrientation() == HORIZONTAL) {
				g.setColor(MetalLookAndFeel.getControlShadow());
				//g.drawLine(0, h - 2, w, h - 2);
				g.setColor(UIManager.getColor("ToolBar.borderColor"));
				g.drawLine(0, h - 1, w, h - 1);
			}

			g.translate(-x, -y);
		}

		public Insets getBorderInsets(Component c) {
			return getBorderInsets(c, new Insets(0, 0, 0, 0));
		}

		public Insets getBorderInsets(Component c, Insets newInsets) {
			newInsets.left = 2;
			newInsets.top = 1;
			newInsets.bottom = 2;
			newInsets.right = 2;

			if (((JToolBar) c).isFloatable()) {
				if (((JToolBar) c).getOrientation() == HORIZONTAL) {
					if (c.getComponentOrientation().isLeftToRight()) {
						newInsets.left = 16;
					} else {
						newInsets.right = 16;
					}
				} else {
					newInsets.top = 16;
				}
			}

			Insets margin = ((JToolBar) c).getMargin();

			if (margin != null) {
				newInsets.left += margin.left;
				newInsets.top += margin.top;
				newInsets.right += margin.right;
				newInsets.bottom += margin.bottom;
			}

			return newInsets;
		}
	}
}
