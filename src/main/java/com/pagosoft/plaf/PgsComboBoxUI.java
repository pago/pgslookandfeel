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
import javax.swing.event.*;
import javax.swing.plaf.*;
import javax.swing.plaf.basic.*;
import javax.swing.plaf.metal.*;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * It tasts bad, that I had to copy most of MetalComboBoxUIs Code
 * just to change a little things of it's behaviour...
 */
public class PgsComboBoxUI extends MetalComboBoxUI {
	private boolean iconPaintPressed = false;
	private PopupHandler popupHandler;
	private FocusHandler focusHandler;

	public static ComponentUI createUI(JComponent c) {
		return new PgsComboBoxUI();
	}

	public void paintCurrentValue(Graphics g, Rectangle bounds, boolean hasFocus) {
		ListCellRenderer renderer = comboBox.getRenderer();
		Component c;

		if (hasFocus && !isPopupVisible(comboBox)) {
			c = renderer.getListCellRendererComponent(
					listBox,
					comboBox.getSelectedItem(),
					-1,
					true,
					false);
		} else {
			c = renderer.getListCellRendererComponent(
					listBox,
					comboBox.getSelectedItem(),
					-1,
					false,
					false);
			c.setBackground(UIManager.getColor("ComboBox.background"));
		}
		c.setFont(comboBox.getFont());
		if (comboBox.isEnabled()) {
			c.setForeground(comboBox.getForeground());
			c.setBackground(comboBox.getBackground());
		} else {
			c.setForeground(UIManager.getColor("ComboBox.disabledForeground"));
			c.setBackground(UIManager.getColor("ComboBox.disabledBackground"));
		}

		// Fix for 4238829: should lay out the JPanel.
		boolean shouldValidate = false;
		if (c instanceof JPanel) {
			shouldValidate = true;
		}

		currentValuePane.paintComponent(
				g, c, comboBox, bounds.x, bounds.y,
				bounds.width, bounds.height, shouldValidate);

		/*if(hasFocus && !isPopupVisible(comboBox)) {
			g.setColor(listBox.getSelectionBackground());
			PgsUtils.drawRoundRect(g, bounds.x + 2, bounds.y + 2, bounds.width-4, bounds.height-4);
		}*/
	}

	public void paintCurrentValueBackground(
			Graphics g, Rectangle bounds,
			boolean hasFocus) {
		/*g.setColor(MetalLookAndFeel.getControlDarkShadow());
		g.drawRect(bounds.x, bounds.y, bounds.width, bounds.height - 1);
		g.setColor(MetalLookAndFeel.getControlShadow());
		g.drawRect(bounds.x + 1, bounds.y + 1, bounds.width - 2,
				   bounds.height - 3);*/
	}

	protected void installListeners() {
		super.installListeners();
		if (popup instanceof BasicComboPopup) {
			popupHandler = new PopupHandler();
			((BasicComboPopup) popup).addPopupMenuListener(popupHandler);
			comboBox.addFocusListener(focusHandler = new FocusHandler());
		}
	}

	protected void uninstallListeners() {
		super.uninstallListeners();
		if (popup instanceof BasicComboPopup) {
			((BasicComboPopup) popup).removePopupMenuListener(popupHandler);
			comboBox.removeFocusListener(focusHandler);
		}
	}

	public void paint(Graphics g, JComponent c) {
		hasFocus = comboBox.hasFocus();
		if (comboBox.isEditable()) {
			Color old = g.getColor();
			g.setColor(UIManager.getColor("TextField.background"));
			g.fillRect(0, 0, c.getWidth()+1, c.getHeight()+1);
			g.setColor(old);
		} else {
			Rectangle r = rectangleForCurrentValue();
			paintCurrentValueBackground(g, r, hasFocus);
			paintCurrentValue(g, r, hasFocus);
		}
	}

	protected ComboBoxEditor createEditor() {
		// Using the Metal-version of it introduces problems with height and border
		BasicComboBoxEditor.UIResource editor = new BasicComboBoxEditor.UIResource();
		Component ec = editor.getEditorComponent();
		if(ec instanceof JTextComponent) {
			ec.addFocusListener(new FocusListener() {
				public void focusGained(FocusEvent e) {
					comboBox.repaint();
				}

				public void focusLost(FocusEvent e) {
					comboBox.repaint();
				}
			});
		}
		return editor;
	}

	protected JButton createArrowButton() {
		JButton button = new PgsComboBoxButtonUI(
				comboBox, new PgsComboBoxIcon(),
				comboBox.isEditable(),
				currentValuePane, listBox);
		button.setMargin(new Insets(1, 1, 1, 1));
		button.setFocusPainted(comboBox.isEditable());
		return button;
	}

	protected ComboPopup createPopup() {
		// we don't want the visual clutter of having two borders directly
		// below each other so we move the popup one pixel higher
		BasicComboPopup basiccombopopup = new BasicComboPopup(comboBox) {
			public void show(Component invoker, int x, int y) {
				super.show(invoker, x, y-1);
			}
		};
		basiccombopopup.setBorder(UIManager.getBorder("ComboBox.popup.border"));
		basiccombopopup.applyComponentOrientation(comboBox.getComponentOrientation());
		return basiccombopopup;
	}

	protected ListCellRenderer createRenderer() {
		return new BasicComboBoxRenderer.UIResource() {
			public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
				Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
				c.setComponentOrientation(comboBox.getComponentOrientation());
				return c;
			}
		};
	}

	public void layoutComboBox(Container parent, MetalComboBoxLayoutManager manager) {
		if (arrowButton != null) {
			if (arrowButton instanceof PgsComboBoxButtonUI) {
				Icon icon = ((PgsComboBoxButtonUI) arrowButton).getComboIcon();
				Insets buttonInsets = arrowButton.getInsets();
				Insets insets = comboBox.getInsets();
				int buttonWidth = icon.getIconWidth() + buttonInsets.left +
						buttonInsets.right;
				arrowButton.setBounds(
						PgsUtils.isLeftToRight(comboBox)
								? (comboBox.getWidth() - insets.right - buttonWidth)
								: insets.left+2,
						insets.top + 2, buttonWidth - 2,
						comboBox.getHeight() - insets.top - insets.bottom - 4);
			} else {
				Insets insets = comboBox.getInsets();
				int width = comboBox.getWidth();
				int height = comboBox.getHeight();
				arrowButton.setBounds(
						insets.left, insets.top,
						width - (insets.left + insets.right),
						height - (insets.top + insets.bottom));
			}
		}

		if (editor != null) {
			Rectangle cvb = rectangleForCurrentValue();
			editor.setBounds(cvb);
		}
	}

	protected Rectangle rectangleForCurrentValue() {
		int width = comboBox.getWidth();
		int height = comboBox.getHeight();
		Insets insets = getInsets();
		int buttonSize = height - (insets.top + insets.bottom);
		if (arrowButton != null) {
			if (arrowButton instanceof PgsComboBoxButtonUI) {
				Icon icon = ((PgsComboBoxButtonUI) arrowButton).getComboIcon();
				Insets buttonInsets = arrowButton.getInsets();
				buttonSize = icon.getIconWidth() + buttonInsets.left +
						buttonInsets.right;
			} else {
				buttonSize = arrowButton.getWidth();
			}
		}
		if(PgsUtils.isLeftToRight(comboBox)) {
			return new Rectangle(insets.left+2, insets.top+1,
								 width - (insets.left + insets.right + buttonSize + 4),
								 height - (insets.top + insets.bottom)-2);
		} else {
			return new Rectangle(insets.left + buttonSize + 2, insets.top+1,
								 width - (insets.left + insets.right + buttonSize + 4),
								 height - (insets.top + insets.bottom)-2);
		}
	}

	public Dimension getMinimumSize(JComponent c) {
		if (!isMinimumSizeDirty) {
			return new Dimension(cachedMinimumSize);
		}

		Dimension size;

		if (!comboBox.isEditable() &&
				arrowButton != null &&
				arrowButton instanceof PgsComboBoxButtonUI) {

			PgsComboBoxButtonUI button = (PgsComboBoxButtonUI) arrowButton;
			Insets buttonInsets = button.getInsets();
			Insets insets = comboBox.getInsets();

			size = getDisplaySize();
			size.width += insets.left + insets.right;
			size.width += buttonInsets.left + buttonInsets.right;
			size.width += buttonInsets.right + button.getComboIcon().getIconWidth();
			size.height += insets.top + insets.bottom;
			size.height += buttonInsets.top + buttonInsets.bottom;
			size.height -= 6; // that's what the size is off after the previous calculation
		} else if (comboBox.isEditable() &&
				arrowButton != null &&
				editor != null) {
			size = super.getMinimumSize(c);
			Insets margin = arrowButton.getMargin();
			size.height += margin.top + margin.bottom - 2;
			size.width += margin.left + margin.right;
		} else {
			size = super.getMinimumSize(c);
		}

		cachedMinimumSize.setSize(size.width, size.height);
		isMinimumSizeDirty = false;

		return new Dimension(cachedMinimumSize);
	}

	public PropertyChangeListener createPropertyChangeListener() {
		return new PgsPropertyChangeListener();
	}


	public class PgsPropertyChangeListener extends BasicComboBoxUI.PropertyChangeHandler {
		public void propertyChange(PropertyChangeEvent e) {
			super.propertyChange(e);
			String propertyName = e.getPropertyName();

			if ("editable".equals(propertyName)) {
				PgsComboBoxButtonUI button = (PgsComboBoxButtonUI) arrowButton;
				button.setIconOnly(comboBox.isEditable());
				comboBox.repaint();
				button.setFocusPainted(comboBox.isEditable());
			} else if ("background".equals(propertyName)) {
				Color color = (Color) e.getNewValue();
				arrowButton.setBackground(color);
				listBox.setBackground(color);

			} else if ("foreground".equals(propertyName)) {
				Color color = (Color) e.getNewValue();
				arrowButton.setForeground(color);
				listBox.setForeground(color);
			}
		}
	}

	private class PgsComboBoxIcon implements Icon {
		public void paintIcon(Component component, Graphics g, int x, int y) {
			JButton c = (JButton) component;
			int iconWidth = getIconWidth();

			g.translate(x, y);

			g.setColor(c.isEnabled() ? PgsLookAndFeel.getControlInfo() : PgsLookAndFeel.getControlShadow());
			if (iconPaintPressed) {
				g.drawLine(1, 3, 1 + iconWidth - 3, 3);
				g.drawLine(2, 2, 2 + iconWidth - 5, 2);
				g.drawLine(3, 1, 3 + iconWidth - 7, 1);
				g.drawLine(4, 0, 4 + iconWidth - 9, 0);
			} else {
				g.drawLine(1, 1, 1 + (iconWidth - 3), 1);
				g.drawLine(2, 2, 2 + (iconWidth - 5), 2);
				g.drawLine(3, 3, 3 + (iconWidth - 7), 3);
				g.drawLine(4, 4, 4 + (iconWidth - 9), 4);
			}

			g.translate(-x, -y);
		}

		/**
		 * Created a stub to satisfy the interface.
		 */
		public int getIconWidth() {
			return 10;
		}

		/**
		 * Created a stub to satisfy the interface.
		 */
		public int getIconHeight() {
			return 4;
		}
	}

	private class PopupHandler implements PopupMenuListener {
		public void popupMenuCanceled(PopupMenuEvent e) {
		}

		public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
			iconPaintPressed = false;
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					comboBox.repaint();
				}
			});
		}

		public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
			iconPaintPressed = true;
		}
	}

	// it seems like the PopupMenuListener is not notified if the
	// popup closes through a focus loss
	private class FocusHandler implements FocusListener {
		public void focusGained(FocusEvent e) {
		}

		public void focusLost(FocusEvent e) {
			iconPaintPressed = false;
			comboBox.repaint();
		}
	}
}
