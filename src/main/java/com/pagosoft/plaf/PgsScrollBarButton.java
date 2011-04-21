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
import javax.swing.plaf.metal.*;
import java.awt.*;
import java.awt.event.*;

public class PgsScrollBarButton extends MetalScrollButton {
	private static Color shadowColor;
	private static Color highlightColor;
	private boolean isFreeStanding = false;

	public PgsScrollBarButton(int direction, int width, boolean freeStanding) {
		super(direction, width, freeStanding);

		shadowColor = UIManager.getColor("ScrollBar.darkShadow");
		highlightColor = UIManager.getColor("ScrollBar.highlight");
		isFreeStanding = freeStanding;

		putClientProperty("rolloverBackground", UIManager.getColor("Button.rolloverBackground"));
		putClientProperty("pgs.isFlat", UIManager.get("Button.isFlat"));
		putClientProperty("gradientStart", UIManager.get("Button.gradientStart"));
		putClientProperty("gradientEnd", UIManager.get("Button.gradientEnd"));
		putClientProperty("rollover.gradientStart", UIManager.get("Button.rolloverGradientStart"));
		putClientProperty("rollover.gradientEnd", UIManager.get("Button.rolloverGradientEnd"));

		setBorder(null);
	}

	public void setFreeStanding(boolean freeStanding) {
		super.setFreeStanding(freeStanding);
		isFreeStanding = freeStanding;
	}

	public void paint(Graphics g) {
		boolean leftToRight = PgsUtils.isLeftToRight(this);
		boolean isEnabled = getParent().isEnabled();
		boolean isPressed = getModel().isPressed();

		Color arrowColor = isEnabled
				? PgsLookAndFeel.getPrimaryControlDarkShadow()
				: PgsLookAndFeel.getControlDisabled();
		int width = getWidth();
		int height = getHeight();
		int arrowHeight = (height + 1) >> 2;

		// @todo The background is not completly painted
		if (isPressed) {
			PgsUtils.drawGradient(
					g, -1, 0, getWidth(), getHeight(), UIManager.getColor("Button.select"),
					UIManager.getColor("Button.select").brighter());
		} else {
			PgsUtils.drawGradient(g, this);
		}

		// paint the arrow
		Graphics2D gfx = (Graphics2D) g;
		Stroke s = gfx.getStroke();
		gfx.setStroke(new BasicStroke(1.3f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
		g.setColor(arrowColor);
		gfx.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		if (getDirection() == NORTH) {
			int startY = ((height + 2) - arrowHeight) / 2 - 1;
			int startX = ((width+1) / 2) - 1;
			gfx.drawLine(startX, startY, startX-3, startY+3);
			gfx.drawLine(startX, startY, startX+3, startY+3);
		} else if (getDirection() == SOUTH) {
			int startY = ((height + 2) - arrowHeight) / 2 - 1;
			int startX = ((width+1) / 2) - 1;
			gfx.drawLine(startX-3, startY, startX, startY+3);
			gfx.drawLine(startX+3, startY, startX, startY+3);
		} else if (getDirection() == EAST) {
			int startX = (((width + 1) - arrowHeight) / 2);
			int startY = (height / 2);
			gfx.drawLine(startX, startY-3, startX+3, startY);
			gfx.drawLine(startX, startY+3, startX+3, startY);
		} else if (getDirection() == WEST) {
			int startX = (((width + 1) - arrowHeight) / 2) - 1;
			int startY = (height / 2);
			gfx.drawLine(startX, startY, startX+3, startY-3);
			gfx.drawLine(startX, startY, startX+3, startY+3);
		}
		gfx.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_DEFAULT);
		gfx.setStroke(s);

		if (getModel().isEnabled() && getModel().isRollover()) {
			if(!UIManager.getBoolean("Button.rolloverVistaStyle")) {
				s = gfx.getStroke();
				gfx.setStroke(new BasicStroke(2.4f, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER));
				g.setColor(PgsLookAndFeel.getGlow());
				if (getDirection() == NORTH) {
					g.drawRect(2, 1, width - 3, height - 3);
				} else if (getDirection() == SOUTH) {
					g.drawRect(2, 2, width - 3, height - 3);
				} else if (getDirection() == EAST) {
					g.drawRect(2, 2, width - 4, height - 3);
				} else if (getDirection() == WEST) {
					g.drawRect(1, 2, width - 3, height - 3);
				}
				gfx.setStroke(s);
			}
		}
		g.setColor(isEnabled ? shadowColor : PgsLookAndFeel.getControlShadow());
		// border painting is relative to the position. A "NORTH"-button will
		// only paint right and bottom borders, for example.
		if (getDirection() == NORTH) {
			if(PgsUtils.isLeftToRight(this.getParent())) {
				g.drawLine(0, 0, 0, height-1); // border left
			} else {
				g.drawLine(width-1, 0, width-1, height-1); // border right
			}
			g.drawLine(0, height-1, width-1, height-1); // border bottom
			// paint the top border if there is a column header (indicates a JTable)
			Container c = getParent().getParent();
			if(c instanceof JScrollPane) {
				JScrollPane sc = (JScrollPane)c;
				if(sc.getColumnHeader() != null) {
					g.drawLine(0, 0, width-1, 0); // border top
				}
			}
		} else if (getDirection() == SOUTH) {
			g.drawLine(0, 0, width-1, 0); // border top
			if(PgsUtils.isLeftToRight(this.getParent())) {
				g.drawLine(0, 0, 0, height-1); // border left
			} else {
				g.drawLine(width-1, 0, width-1, height-1); // border right
			}
			Container c = getParent().getParent();
			if(c instanceof JScrollPane) {
				JScrollPane sc = (JScrollPane)c;
				if(sc.getHorizontalScrollBar() != null && sc.getHorizontalScrollBar().isVisible()) {
					g.drawLine(0, height-1, width-1, height-1); // border bottom
				}
			} else {
				g.drawLine(0, height-1, width-1, height-1); // border bottom
			}
		} else if (getDirection() == EAST) {
			g.drawLine(0, 0, width-1, 0); // border top
			g.drawLine(0, 0, 0, height-1); // border left
			Container c = getParent().getParent();
			if(c instanceof JScrollPane) {
				JScrollPane sc = (JScrollPane)c;
				if(sc.getVerticalScrollBar() != null && sc.getVerticalScrollBar().isVisible()) {
					g.drawLine(width-1, 0, width-1, height-1); // border right
				}
			} else {
				g.drawLine(width-1, 0, width-1, height-1); // border right
			}
		} else if (getDirection() == WEST) {
			g.drawLine(0, 0, width-1, 0); // border top
			g.drawLine(width-1, 0, width-1, height-1); // border right
		}
	}

	protected void paintBorder(Graphics g) {
		// do nothing at all
	}
}
