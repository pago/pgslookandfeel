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
import javax.swing.event.*;
import javax.swing.plaf.*;
import javax.swing.plaf.basic.*;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.logging.Logger;


/**
 * @todo Remove rollovertab-code for Java 5.0 - their implementation is much cleaner/faster than mine.
 */
public class PgsTabbedPaneUI extends BasicTabbedPaneUI {
	private boolean paintFocus;
	private int rolloverTabIndex;
	private TabRolloverHandler tabHandler;
	private MyPropertyChangeHandler propHandler;
	private TabbedPaneMouseWheelScroller tabScrollHandler;
	private TabSelectionMouseHandler tabSelectionHandler;

	private static Logger logger = Logger.getLogger("PgsTabbedPaneUI");

	public static final String IS_SUB_TAB = "pgs.isSubTab";
	public static final String IS_BUTTON_STYLE = "pgs.isButtonStyle";
	public static final String NO_BORDER = "pgs.noBorder";

	public static ComponentUI createUI(JComponent c) {
		return new PgsTabbedPaneUI();
	}

	protected void installDefaults() {
		super.installDefaults();
		updateBackgroundOpacity();
		paintFocus = UIManager.getBoolean("TabbedPane.focusPainted");
	}

	protected void installListeners() {
		super.installListeners();
		if (tabHandler == null) {
			tabHandler = new TabRolloverHandler();
		}
		tabPane.addMouseListener(tabHandler);
		tabPane.addMouseMotionListener(tabHandler);

		if (propHandler == null) {
			propHandler = new MyPropertyChangeHandler();
		}
		tabPane.addPropertyChangeListener(propHandler);

		if (PlafOptions.isWheelTabbedPaneEnabled()) {
			if (tabScrollHandler == null) {
				tabScrollHandler = new TabbedPaneMouseWheelScroller();
			}
			tabPane.addMouseWheelListener(tabScrollHandler);
		}

		if (PlafOptions.isTabbedPaneRightClickSelectionEnabled()) {
			if (tabSelectionHandler == null) {
				tabSelectionHandler = new TabSelectionMouseHandler();
			}
			tabPane.addMouseListener(tabSelectionHandler);
		}

		if (PlafOptions.isTabReorderingEnabled()) {
			enableReordering();
		}
	}

	protected void uninstallListeners() {
		super.uninstallListeners();
		tabPane.removeMouseListener(tabHandler);
		tabPane.removeMouseMotionListener(tabHandler);
		tabPane.removePropertyChangeListener(propHandler);

		if (tabScrollHandler != null) {
			tabPane.removeMouseWheelListener(tabScrollHandler);
		}

		if (tabSelectionHandler != null) {
			tabPane.removeMouseListener(tabSelectionHandler);
		}

		disableReordering();
	}

	private void updateBackgroundOpacity() {
		if (isSubTab() || isButtonStyle()) {
			tabPane.setOpaque(true);
			if (isSubTab()) {
				tabPane.setBackground(UIManager.getColor("TabbedPane.background"));
			} else if (isButtonStyle()) {
				tabPane.setBackground(UIManager.getColor("TabbedPane.buttonStyle.background"));
			}
		} else {
			tabPane.setOpaque(false);
		}
	}

	/**
	 * This method is a hack or workaround for Jython...
	 */
	private boolean checkBooleanClientProperty(Object key) {
		Object o = tabPane.getClientProperty(key);
		if (o == null) {
			return false;
		}

		if (o instanceof Boolean) {
			return ((Boolean) o).booleanValue();
		}
		// Wow. That's unexpected...
		if (o instanceof Integer) {
			// Jython seems to use Integers instead of booleans,
			// as we know it, let's be compatible to it.
			return ((Integer) o).intValue() != 0;
		}
		// Well... we know the property has been set, but
		// it seems like the user used wrong input. We just write out something
		// and return true.
		logger.warning(
				"It seems like you've used a wrong type for '" + key + "'. It should be a boolean, but is a " + o
						.getClass().getName());
		return true;
	}

	private boolean isSubTab() {
		return checkBooleanClientProperty(IS_SUB_TAB);
	}

	private boolean isButtonStyle() {
		return checkBooleanClientProperty(IS_BUTTON_STYLE);
	}

	private void mySetRolloverTab(int x, int y) {
		mySetRolloverTab(myTabForCoordinate(tabPane, x, y));
	}

	private int myTabForCoordinate(JTabbedPane pane, int x, int y) {
		Point p = new Point(x, y);
		int tabCount = tabPane.getTabCount();
		for (int i = 0; i < tabCount; i++) {
			if (rects[i].contains(p.x, p.y)) {
				return i;
			}
		}
		return -1;
	}

	protected void mySetRolloverTab(int index) {
		if(index >= 0) {
			if(rolloverTabIndex != -1) {
				tabPane.repaint(rects[rolloverTabIndex]);
			}
			tabPane.repaint(rects[index]);
		} else if(rolloverTabIndex != -1 && rolloverTabIndex < rects.length) {
			tabPane.repaint(rects[rolloverTabIndex]);
		}
		rolloverTabIndex = index;
	}

	protected int getRolloverTab() {
		return rolloverTabIndex;
	}

	protected void paintTabBackground(
			Graphics g, int tabPlacement, int tabIndex,
			int x, int y, int w, int h, boolean isSelected) {
		if (isButtonStyle()) {
			if (isSelected) {
				g.setColor(UIManager.getColor("TabbedPane.buttonStyle.selectedBackground"));
				g.fillRect(x + 2, y + 2, w - 4, h - 4);
			}
			if (tabIndex == getRolloverTab() && tabPane.isEnabledAt(tabIndex)) {
				g.setColor(UIManager.getColor("TabbedPane.buttonStyle.rolloverBackground"));
				if (isSelected) {
					g.fillRect(x + 2, y + 2, w - 4, h - 4);
				} else {
					g.fillRect(x, y, w, h);
				}
			}
			return;
		}
		if (isSubTab() && !isSelected) {
			return;
		}
		Graphics2D gfx = (Graphics2D) g;
		if (isSelected) {
			gfx.setColor(UIManager.getColor("TabbedPane.selected"));
		} else {
			Color a = UIManager.getColor("TabbedPane.tabGradientStart");
			Color b = UIManager.getColor("TabbedPane.tabGradientEnd");
			gfx.setPaint(new GradientPaint(0, y + 1, a, 0, y + 1 + h, b));
		}

		switch (tabPlacement) {
			case LEFT:
				gfx.fill(new Rectangle(x, y, w + 2, h));
				break;
			case RIGHT:
				gfx.fill(new Rectangle(x - 2, y, w + 2, h));
				break;
			case BOTTOM:
				gfx.fill(new Rectangle(x, y - 2, w, h + 2));
				break;
			case TOP:
			default:
				gfx.fill(new Rectangle(x, y, w - 2, h + 2));
				break;
		}

		// shall we paint a rollover-effect as well?
		if (!isSelected && tabIndex == getRolloverTab() && tabPane.isEnabledAt(tabIndex)) {
			RenderingHints oldHints = gfx.getRenderingHints();
			gfx.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			gfx.setColor(PgsLookAndFeel.getGlow());
			Stroke oldStroke = gfx.getStroke();
			gfx.setStroke(PgsUtils.rolloverBorderStroke);
			switch (tabPlacement) {
				case LEFT:
					gfx.drawRoundRect(x + 1, y + 1, w + 1, h - 3, 5, 5);
					break;
				case RIGHT:
					gfx.drawRoundRect(x - 2, y + 1, w + 1, h - 2, 5, 5);
					break;
				case BOTTOM:
					gfx.drawRoundRect(x + 1, y - 2, w - 3, h, 5, 5);
					break;
				case TOP:
				default:
					gfx.drawRoundRect(x + 1, y + 1, w - 4, h + 1, 5, 5);
					break;
			}
			gfx.setStroke(oldStroke);
			gfx.setRenderingHint(RenderingHints.KEY_ANTIALIASING, oldHints.get(RenderingHints.KEY_ANTIALIASING));
			//gfx.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
		}
	}

	protected void paintTabBorder(
			Graphics g, int tabPlacement,
			int tabIndex,
			int x, int y, int w, int h,
			boolean isSelected) {
		if (isButtonStyle()) {
			if (isSelected) {
				g.setColor(UIManager.getColor("TabbedPane.buttonStyle.selectedBorder"));
				g.drawRect(x + 2, y + 2, w - 4, h - 4);
			}
			if (tabIndex == getRolloverTab()) {
				g.setColor(UIManager.getColor("TabbedPane.buttonStyle.rolloverBorder"));
				if (isSelected) {
					g.drawRect(x + 2, y + 2, w - 4, h - 4);
				} else {
					g.drawRect(x, y, w, h);
				}
			}
			return;
		}
		if (isSubTab() && !isSelected) {
			g.setColor(PgsLookAndFeel.getControlDarkShadow());
			g.drawLine(x + w - 1, y, x + w - 1, y + h);
			return;
		}
		Graphics2D gfx = (Graphics2D) g;
		gfx.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		gfx.setColor(PgsLookAndFeel.getControlDarkShadow());
		if (isSubTab()) {
			switch (tabPlacement) {
				case LEFT:
					gfx.drawRect(x, y, w + 2, h);
					break;
				case RIGHT:
					gfx.drawRect(x - 2, y, w + 2, h);
					break;
				case BOTTOM:
					gfx.drawRect(x, y - 2, w, h + 2);
					break;
				case TOP:
				default:
					gfx.drawRect(x, y, w - 2, h + 2);
					break;
			}
		} else {
			switch (tabPlacement) {
				case LEFT:
					gfx.drawRoundRect(x, y, w + 2, h, 5, 5);
					break;
				case RIGHT:
					gfx.drawRoundRect(x - 2, y, w + 2, h, 5, 5);
					break;
				case BOTTOM:
					gfx.drawRoundRect(x, y - 2, w, h + 2, 5, 5);
					break;
				case TOP:
				default:
					gfx.drawRoundRect(x, y, w - 2, h + 2, 5, 5);
					break;
			}
		}
		gfx.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
	}

	protected void paintFocusIndicator(
			Graphics g, int tabPlacement,
			Rectangle[] rects, int tabIndex,
			Rectangle iconRect, Rectangle textRect,
			boolean isSelected) {
		if (paintFocus) {
			super.paintFocusIndicator(
					g, tabPlacement,
					rects, tabIndex,
					iconRect, textRect,
					isSelected);
		}
	}

	protected void paintContentBorder(Graphics g, int tabPlacement, int selectedIndex) {
		int width = tabPane.getWidth();
		int height = tabPane.getHeight();
		Insets insets = tabPane.getInsets();
		Insets tabAreaInsets = getTabAreaInsets(tabPlacement);

		int x = insets.left;
		int y = insets.top;
		int w = width - insets.right - insets.left;
		int h = height - insets.top - insets.bottom;

		boolean tabsOverlapBorder = true;
		int tabAreaWidth = 0, tabAreaHeight = 0;

		switch (tabPlacement) {
			case LEFT:
				tabAreaWidth = calculateTabAreaWidth(tabPlacement, runCount, maxTabWidth);
				x += tabAreaWidth;
				if (tabsOverlapBorder) {
					x -= tabAreaInsets.right;
				}
				w -= (x - insets.left);
				break;
			case RIGHT:
				tabAreaWidth = calculateTabAreaWidth(tabPlacement, runCount, maxTabWidth);
				w -= tabAreaWidth;
				if (tabsOverlapBorder) {
					w += tabAreaInsets.left;
				}
				break;
			case BOTTOM:
				tabAreaHeight = calculateTabAreaHeight(tabPlacement, runCount, maxTabHeight);
				h -= tabAreaHeight;
				if (tabsOverlapBorder) {
					h += tabAreaInsets.top;
				}
				break;
			case TOP:
			default:
				tabAreaHeight = calculateTabAreaHeight(tabPlacement, runCount, maxTabHeight);
				y += tabAreaHeight;
				if (tabsOverlapBorder) {
					y -= tabAreaInsets.bottom;
				}
				h -= (y - insets.top);
		}

		// Fill region behind content area
		Color color = UIManager.getColor("TabbedPane.contentAreaColor");
		Color selectedColor = UIManager.getColor("TabbedPane.selected");
		if (color != null) {
			g.setColor(color);
		} else if (selectedColor == null) {
			g.setColor(tabPane.getBackground());
		} else {
			g.setColor(selectedColor);
		}
		g.fillRect(x, y, w, h);

		if (checkBooleanClientProperty(NO_BORDER)) {
			return;
		}

		g.setColor(PgsLookAndFeel.getControlDarkShadow());
		PgsUtils.drawRoundRect(g, x, y, w - 1, h - 1);

		// stupid hack... however, it works...
		if (selectedIndex > -1 && !isButtonStyle()) {
			Rectangle selRect = getTabBounds(selectedIndex, calcRect);
			g.setColor(UIManager.getColor("TabbedPane.selected"));
			switch (tabPlacement) {
				case JTabbedPane.TOP:
					g.fillRect(selRect.x + 1, selRect.y + selRect.height - 2, selRect.width - 2, 1);
					if (selectedIndex == 0) {
						g.setColor(PgsLookAndFeel.getControlDarkShadow());
						g.drawLine(x, selRect.y + selRect.height - 2, x, selRect.y + selRect.height + 2);
					}
					break;
				case JTabbedPane.BOTTOM:
					g.fillRect(selRect.x + 1, selRect.y + 1, selRect.width - 1, 1);
					if (selectedIndex == 0) {
						g.setColor(PgsLookAndFeel.getControlDarkShadow());
						g.drawLine(x, selRect.y - 2, x, selRect.y + 2);
					}
					break;
				case JTabbedPane.LEFT:
					g.fillRect(selRect.x + selRect.width - 2, selRect.y + 1, 1, selRect.height - 1);
					if (selectedIndex == 0) {
						g.setColor(PgsLookAndFeel.getControlDarkShadow());
						g.drawLine(selRect.x + selRect.width - 2, selRect.y, selRect.x + selRect.width + 2, selRect.y);
					}
					break;
				case JTabbedPane.RIGHT:
					g.fillRect(selRect.x + 1, selRect.y + 1, 1, selRect.height - 1);
					if (selectedIndex == 0) {
						g.setColor(PgsLookAndFeel.getControlDarkShadow());
						g.drawLine(selRect.x - 2, selRect.y, selRect.x + 2, selRect.y);
					}
					break;
			}
		}
	}

	protected void paintText(
			Graphics g, int tabPlacement,
			Font font, FontMetrics metrics, int tabIndex,
			String title, Rectangle textRect,
			boolean isSelected) {
		PgsUtils.installAntialiasing(g);
		super.paintText(g, tabPlacement, font, metrics, tabIndex, title, textRect, isSelected);
		PgsUtils.uninstallAntialiasing(g);
	}

	protected class TabRolloverHandler implements MouseListener, MouseMotionListener {
		public void mouseClicked(MouseEvent e) {
		}

		public void mouseReleased(MouseEvent e) {
		}

		public void mouseEntered(MouseEvent e) {
			mySetRolloverTab(e.getX(), e.getY());
		}

		public void mouseExited(MouseEvent e) {
			mySetRolloverTab(-1);
		}

		public void mousePressed(MouseEvent e) {
		}

		public void mouseDragged(MouseEvent e) {
		}

		public void mouseMoved(MouseEvent e) {
			mySetRolloverTab(e.getX(), e.getY());
		}
	}

	public class MyPropertyChangeHandler implements PropertyChangeListener {
		public void propertyChange(PropertyChangeEvent e) {
			if (IS_SUB_TAB.equals(e.getPropertyName()) || IS_BUTTON_STYLE.equals(e.getPropertyName())) {
				updateBackgroundOpacity();
			} else if ("wheelScrolling".equals(e.getPropertyName())) {
				if (checkBooleanClientProperty("wheelScrolling")) {
					if (tabScrollHandler == null) {
						tabScrollHandler = new TabbedPaneMouseWheelScroller();
					}
					tabPane.addMouseWheelListener(tabScrollHandler);
				} else {
					if (tabScrollHandler != null) {
						tabPane.removeMouseWheelListener(tabScrollHandler);
					}
				}
			} else if ("rightClickSelection".equals(e.getPropertyName())) {
				if (checkBooleanClientProperty("rightClickSelection")) {
					if (tabSelectionHandler == null) {
						tabSelectionHandler = new TabSelectionMouseHandler();
					}
					tabPane.addMouseListener(tabSelectionHandler);
				} else {
					if (tabSelectionHandler != null) {
						tabPane.removeMouseListener(tabSelectionHandler);
					}
				}
			} else if ("tabReordering".equals(e.getPropertyName())) {
				if (checkBooleanClientProperty("tabReordering")) {
					enableReordering();
				} else {
					disableReordering();
				}
			}
		}
	}

	protected JButton createScrollButton(int direction) {
		if (direction != SOUTH && direction != NORTH && direction != EAST &&
				direction != WEST) {
			throw new IllegalArgumentException(
					"Direction must be one of: " +
							"SOUTH, NORTH, EAST or WEST");
		}
		return new ScrollableTabButton(direction);
	}

	private class ScrollableTabButton extends BasicArrowButton implements UIResource,
			SwingConstants {
		public ScrollableTabButton(int direction) {
			super(
					direction,
					UIManager.getColor("TabbedPane.selected"),
					UIManager.getColor("TabbedPane.shadow"),
					UIManager.getColor("TabbedPane.darkShadow"),
					UIManager.getColor("TabbedPane.highlight"));
			setOpaque(false);
		}

		public void paint(Graphics g) {
			Color origColor;
			boolean isPressed, isEnabled;
			int w, h, size;

			w = getSize().width;
			h = getSize().height;
			origColor = g.getColor();
			isPressed = getModel().isPressed();
			isEnabled = isEnabled();

			if (isEnabled && getModel().isRollover()) {
				g.setColor(
						isPressed ? PgsLookAndFeel.getPrimaryControlShadow()
								: UIManager.getColor("ToolBarButton.rolloverBackground"));
				g.fillRect(0, 0, w - 2, h - 2);
				g.setColor(UIManager.getColor("ToolBarButton.rolloverBorderColor"));
				g.drawRect(0, 0, w - 2, h - 2);
			}

			// If there's no room to draw arrow, bail
			if (h < 5 || w < 5) {
				g.setColor(origColor);
				return;
			}

			if (isPressed) {
				g.translate(1, 1);
			}

			// Draw the arrow
			size = Math.min((h - 4) / 3, (w - 4) / 3);
			size = Math.max(size, 2);
			paintTriangle(
					g, (w - size) / 2, (h - size) / 2,
					size, direction, isEnabled);

			// Reset the Graphics back to it's original settings
			if (isPressed) {
				g.translate(-1, -1);
			}
			g.setColor(origColor);
		}
	}

	private TabReorderHandler tabReorderingHandler;

	public void enableReordering() {
		if (tabReorderingHandler == null) {
			tabReorderingHandler = new TabReorderHandler();
		}
		tabPane.addMouseListener(tabReorderingHandler);
		tabPane.addMouseMotionListener(tabReorderingHandler);
	}

	public void disableReordering() {
		tabPane.removeMouseListener(tabReorderingHandler);
		tabPane.removeMouseMotionListener(tabReorderingHandler);
	}

	public class TabReorderHandler extends MouseInputAdapter {
		private int draggedTabIndex;

		protected TabReorderHandler() {
			draggedTabIndex = -1;
		}

		public void mouseReleased(MouseEvent e) {
			draggedTabIndex = -1;
		}

		public void mouseDragged(MouseEvent e) {
			if(draggedTabIndex == -1) {
				return;
			}

			int targetTabIndex = tabPane.getUI().tabForCoordinate(tabPane,
																  e.getX(), e.getY());
			if(targetTabIndex != -1 && targetTabIndex != draggedTabIndex) {
				boolean isForwardDrag = targetTabIndex > draggedTabIndex;
				tabPane.insertTab(tabPane.getTitleAt(draggedTabIndex),
								  tabPane.getIconAt(draggedTabIndex),
								  tabPane.getComponentAt(draggedTabIndex),
								  tabPane.getToolTipTextAt(draggedTabIndex),
								  isForwardDrag ? targetTabIndex+1 : targetTabIndex);
				draggedTabIndex = targetTabIndex;
				tabPane.setSelectedIndex(draggedTabIndex);
			}
		}

		public void mousePressed(MouseEvent e) {
			draggedTabIndex = tabPane.getUI().tabForCoordinate(tabPane, e.getX(), e.getY());
		}
	}
	/*public class TabReorderHandler extends MouseInputAdapter {
		private int draggedTabIndex;
		private int dragStartX;

		protected TabReorderHandler() {
			draggedTabIndex = -1;
		}

		public void mouseReleased(MouseEvent e) {
			draggedTabIndex = -1;
		}

		public void mouseDragged(MouseEvent e) {
			if (draggedTabIndex == -1) {
				return;
			}

			int targetTabIndex = myTabForCoordinate(tabPane, e.getX(), e.getY());
			if (targetTabIndex != -1 && targetTabIndex != draggedTabIndex) {
				boolean isForwardDrag = e.getX() > dragStartX;
				tabPane.insertTab(
						tabPane.getTitleAt(draggedTabIndex),
						tabPane.getIconAt(draggedTabIndex),
						tabPane.getComponentAt(draggedTabIndex),
						tabPane.getToolTipTextAt(draggedTabIndex),
						isForwardDrag ? targetTabIndex + 1 : targetTabIndex);
				draggedTabIndex = targetTabIndex;
				dragStartX = e.getX();
				tabPane.setSelectedIndex(draggedTabIndex);
			}
		}

		public void mousePressed(MouseEvent e) {
			dragStartX = e.getX();
			draggedTabIndex = myTabForCoordinate(tabPane, dragStartX, e.getY());
		}
	}*/

	public class TabSelectionMouseHandler extends MouseAdapter {
		public void mouseClicked(MouseEvent e) {
			// we only look at the right button
			if (SwingUtilities.isRightMouseButton(e)) {
				JTabbedPane tabPane = (JTabbedPane) e.getSource();
				JPopupMenu menu = new JPopupMenu();

				int tabCount = tabPane.getTabCount();
				for (int i = 0; i < tabCount; i++) {
					menu.add(new SelectTabAction(tabPane, i));
				}

				menu.show(tabPane, e.getX(), e.getY());
			}
		}
	}

	public class SelectTabAction extends AbstractAction {
		private JTabbedPane tabPane;
		private int index;

		public SelectTabAction(JTabbedPane tabPane, int index) {
			super(tabPane.getTitleAt(index), tabPane.getIconAt(index));

			this.tabPane = tabPane;
			this.index = index;
		}

		public void actionPerformed(ActionEvent e) {
			tabPane.setSelectedIndex(index);
		}
	}

	public class TabbedPaneMouseWheelScroller implements MouseWheelListener {
		public void mouseWheelMoved(MouseWheelEvent e) {
			JTabbedPane tabPane = (JTabbedPane) e.getSource();
			int dir = e.getWheelRotation();
			int selIndex = tabPane.getSelectedIndex();
			int maxIndex = tabPane.getTabCount() - 1;
			if ((selIndex == 0 && dir < 0) || (selIndex == maxIndex && dir > 0)) {
				selIndex = maxIndex - selIndex;
			} else {
				selIndex += dir;
			}
			tabPane.setSelectedIndex(selIndex);
			/*int dir = e.getWheelRotation();
			int selIndex = tabPane.getSelectedIndex();
			// previous tab
			if(dir < 0) {
				// is it already the first index?
				if(selIndex == 0) {
					// select the last tab
					selIndex = tabPane.getTabCount()-1;
				} else {
					// select the previous tab
					selIndex--;
				}
			// next tab
			} else {
				// is it already the last index?
				if(selIndex == tabPane.getTabCount()-1) {
					// select the first tab
					selIndex = 0;
				} else {
					// select the next tab
					selIndex++;
				}
			}
			tabPane.setSelectedIndex(selIndex);*/
		}
	}
}
