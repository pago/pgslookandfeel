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
import javax.swing.plaf.metal.*;
import java.awt.*;

public class PgsScrollBarUI extends MetalScrollBarUI {
	private static Color shadowColor;
	private static Color highlightColor;
	private static Color darkShadowColor;
	private static Color thumbColor;
	private static Color thumbShadow;
	private static Color thumbHighlightColor;

	public static ComponentUI createUI(JComponent c) {
		return new PgsScrollBarUI();
	}

	protected void configureScrollBarColors() {
		super.configureScrollBarColors();
		shadowColor = UIManager.getColor("ScrollBar.shadow");
		highlightColor = UIManager.getColor("ScrollBar.highlight");
		darkShadowColor = UIManager.getColor("ScrollBar.darkShadow");
		thumbColor = UIManager.getColor("ScrollBar.thumb");
		thumbShadow = UIManager.getColor("ScrollBar.thumbShadow");
		thumbHighlightColor = UIManager.getColor("ScrollBar.thumbHighlight");
		trackColor = UIManager.getColor("ScrollBar.track");
		trackHighlightColor = UIManager.getColor("ScrollBar.trackHighlight");
	}

	protected void paintTrack(Graphics g, JComponent c, Rectangle r) {
		super.paintTrack(g, c, r);
		g.translate(r.x, r.y);
		if (((JScrollBar) c).getOrientation() == SwingConstants.VERTICAL) {
			Graphics2D gfx = (Graphics2D) g;
			gfx.setPaint(new GradientPaint(0, 0, trackColor, r.width, 0, trackHighlightColor, true));
			gfx.fillRect(0, 0, r.width, r.height);

			g.setColor(thumbShadow);
			if(PgsUtils.isLeftToRight(c)) {
				g.drawLine(0,0,0, r.height-1); // left border
			} else {
				g.drawLine(r.width-1,0,r.width-1, r.height-1); // right border
			}
		} else {
			Graphics2D gfx = (Graphics2D) g;
			gfx.setPaint(new GradientPaint(0, 0, trackColor, 0, r.height, trackHighlightColor, true));
			gfx.fillRect(0, 0, r.width, r.height);

			// top border
			g.setColor(thumbShadow);
			g.drawLine(0, 0, r.width-1, 0);
		}
		g.translate(-r.x, -r.y);
	}

	protected void paintThumb(Graphics g, JComponent c, Rectangle r) {
		super.paintThumb(g, c, r);
		if (!c.isEnabled()) {
			return;
		}
		g.translate(r.x, r.y);

		if (((JScrollBar) c).getOrientation() == SwingConstants.VERTICAL) {
			// Paints the thumbbackground
			Graphics2D gfx = (Graphics2D) g;
			if (PlafOptions.isOfficeScrollBarEnabled()) {
				gfx.setPaint(new GradientPaint(0, 0, thumbHighlightColor, 0, (r.height / 2) + 1, thumbColor, true));
				gfx.fillRect(0, 0, r.width, r.height);
			} else {
				gfx.setPaint(new GradientPaint(0, 0, thumbHighlightColor, (r.width / 2) + 1, 0, thumbColor, true));
				gfx.fillRect(0, 0, r.width, r.height);
			}

			g.setColor(thumbShadow);
			if(PgsUtils.isLeftToRight(scrollbar)) {
				g.drawLine(0,0,0, r.height-1); // left border
			} else {
				g.drawLine(r.width-1, 0, r.width-1, r.height-1); // right border
			}
			if(scrollbar.getValue() != scrollbar.getMaximum()-scrollbar.getModel().getExtent()) {
				g.drawLine(0,r.height-1,r.width-1,r.height-1); // bottom border
			}
			if(scrollbar.getValue() != 0) {
				g.drawLine(0,0,r.width,0); // top border
				// highlight needs to be painted one pixel earlier
				g.setColor(thumbHighlightColor);
				g.drawLine(1, 1, r.width - 1, 1);
			} else {
				g.setColor(thumbHighlightColor);
				g.drawLine(1, 0, r.width - 1, 0);
			}
			g.drawLine(1, 1, 1, r.height - 2);

			int x = (int) r.getWidth() / 2;
			int y = (int) r.getHeight() / 2;

			// one line = 4px; five lines = 20px
			boolean isSmallThumb = r.getHeight() <= 20;

			g.setColor(ColorUtils.getTranslucentColor(thumbShadow, 150));
			if (!isSmallThumb) {
				g.drawLine(x - 3, y - 8, x + 3, y - 8);
			}
			g.drawLine(x - 3, y - 4, x + 3, y - 4);
			g.drawLine(x - 3, y, x + 3, y);
			g.drawLine(x - 3, y + 4, x + 3, y + 4);
			if (!isSmallThumb) {
				g.drawLine(x - 3, y + 8, x + 3, y + 8);
			}

			g.setColor(ColorUtils.getTranslucentColor(thumbHighlightColor, 150));
			if (!isSmallThumb) {
				g.drawLine(x - 2, y - 7, x + 4, y - 7);
			}
			g.drawLine(x - 2, y - 3, x + 4, y - 3);
			g.drawLine(x - 2, y + 1, x + 4, y + 1);
			g.drawLine(x - 2, y + 5, x + 4, y + 5);
			if (!isSmallThumb) {
				g.drawLine(x - 2, y + 9, x + 4, y + 9);
			}
		} else {
			// Paints the thumbbackground
			Graphics2D gfx = (Graphics2D) g;
			if (PlafOptions.isOfficeScrollBarEnabled()) {
				gfx.setPaint(new GradientPaint(0, 0, thumbHighlightColor, (r.width / 2) + 1, 0, thumbColor, true));
				gfx.fillRect(0, 0, r.width, r.height);
			} else {
				gfx.setPaint(new GradientPaint(0, 0, thumbHighlightColor, 0, (r.height / 2) + 1, thumbColor, true));
				gfx.fillRect(0, 0, r.width, r.height);
			}

			g.setColor(thumbShadow);
			g.drawLine(0,0,r.width-1,0); // top border
			if(scrollbar.getValue() != scrollbar.getMaximum()-scrollbar.getModel().getExtent()) {
				g.drawLine(r.width-1, 0, r.width-1, r.height-1); // right border
			}
			if(scrollbar.getValue() != 0) {
				g.drawLine(0,0,0, r.height-1); // left border
				g.setColor(thumbHighlightColor);
				g.drawLine(1, 1, 1, r.height - 1);
			} else {
				// highlight needs to be painted one pixel earlier
				g.setColor(thumbHighlightColor);
				g.drawLine(0, 1, 0, r.height - 1);
			}
			g.drawLine(1, 1, r.width - 2, 1);


			int x = (int) r.getWidth() / 2;
			int y = (int) r.getHeight() / 2;

			// one line = 4px; five lines = 20px
			boolean isSmallThumb = r.getWidth() <= 20;

			g.setColor(ColorUtils.getTranslucentColor(thumbShadow, 200));
			if (!isSmallThumb) {
				g.drawLine(x - 8, y - 3, x - 8, y + 3);
			}
			g.drawLine(x - 4, y - 3, x - 4, y + 3);
			g.drawLine(x, y - 3, x, y + 3);
			g.drawLine(x + 4, y - 3, x + 4, y + 3);
			if (!isSmallThumb) {
				g.drawLine(x + 8, y - 3, x + 8, y + 3);
			}

			g.setColor(ColorUtils.getTranslucentColor(thumbHighlightColor, 200));
			if (!isSmallThumb) {
				g.drawLine(x - 7, y - 2, x - 7, y + 4);
			}
			g.drawLine(x - 3, y - 2, x - 3, y + 4);
			g.drawLine(x + 1, y - 2, x + 1, y + 4);
			g.drawLine(x + 5, y - 2, x + 5, y + 4);
			if (!isSmallThumb) {
				g.drawLine(x + 9, y - 2, x + 9, y + 4);
			}
		}
	}

	protected JButton createDecreaseButton(int orientation) {
		decreaseButton = new PgsScrollBarButton(orientation, scrollBarWidth, isFreeStanding);
		return decreaseButton;
	}

	protected JButton createIncreaseButton(int orientation) {
		increaseButton = new PgsScrollBarButton(orientation, scrollBarWidth, isFreeStanding);
		return increaseButton;
	}
}
