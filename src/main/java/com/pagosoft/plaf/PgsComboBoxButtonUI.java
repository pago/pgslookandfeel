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
import java.awt.*;

public class PgsComboBoxButtonUI extends JButton {
	protected JComboBox comboBox;
	protected JList listBox;
	protected CellRendererPane rendererPane;
	protected Icon comboIcon;
	protected boolean iconOnly = false;

	public final JComboBox getComboBox() {
		return comboBox;
	}

	public final void setComboBox(JComboBox cb) {
		comboBox = cb;
	}

	public final Icon getComboIcon() {
		return comboIcon;
	}

	public final void setComboIcon(Icon i) {
		comboIcon = i;
	}

	public final boolean isIconOnly() {
		return iconOnly;
	}

	public final void setIconOnly(boolean isIconOnly) {
		iconOnly = isIconOnly;
	}

	public PgsComboBoxButtonUI() {
		super("");
		DefaultButtonModel model = new DefaultButtonModel() {
			public void setArmed(boolean armed) {
				super.setArmed(isPressed() ? true : armed);
			}
		};
		setModel(model);
	}

	public PgsComboBoxButtonUI(
			JComboBox cb, Icon i,
			CellRendererPane pane, JList list) {
		this();
		comboBox = cb;
		comboIcon = i;
		rendererPane = pane;
		listBox = list;
		setEnabled(comboBox.isEnabled());
	}

	public PgsComboBoxButtonUI(
			JComboBox cb, Icon i, boolean onlyIcon,
			CellRendererPane pane, JList list) {
		this(cb, i, pane, list);
		iconOnly = onlyIcon;
	}

	public boolean isFocusTraversable() {
		return false;
	}

	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);

		// Set the background and foreground to the combobox colors.
		if (enabled) {
			setBackground(comboBox.getBackground());
			setForeground(comboBox.getForeground());
		} else {
			setBackground(UIManager.getColor("ComboBox.disabledBackground"));
			setForeground(UIManager.getColor("ComboBox.disabledForeground"));
		}
	}

	public void paintComponent(Graphics g) {
		boolean leftToRight = PgsUtils.isLeftToRight(comboBox);

		// Paint the button as usual
		super.paintComponent(g);

		Insets insets = getInsets();

		int width = getWidth() - (insets.left + insets.right);
		int height = getHeight() - (insets.top + insets.bottom);

		if (height <= 0 || width <= 0) {
			return;
		}

		int left = insets.left;
		int top = insets.top;
		int right = left + (width - 1);
		int bottom = top + (height - 1);

		int iconWidth = 0;
		int iconLeft = (leftToRight) ? right : left;

		// Paint the icon
		if (comboIcon != null) {
			iconWidth = comboIcon.getIconWidth();
			int iconHeight = comboIcon.getIconHeight();
			int iconTop = 0;

			if (iconOnly) {
				iconLeft = (getWidth() / 2) - (iconWidth / 2);
				iconTop = (getHeight() / 2) - (iconHeight / 2) - 1;
			} else {
				if (leftToRight) {
					//iconLeft = (left + (width - 1)) - iconWidth;
					iconLeft = (getWidth() / 2) - (iconWidth / 2);
				} else {
					iconLeft = left;
				}
				iconTop = (top + ((bottom - top) / 2)) - (iconHeight / 2);
			}

			comboIcon.paintIcon(this, g, iconLeft, iconTop);
		}
	}
}
