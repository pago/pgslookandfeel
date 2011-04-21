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
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;
import java.awt.*;

public class PgsSplitPaneDivider extends BasicSplitPaneDivider {
	public PgsSplitPaneDivider(BasicSplitPaneUI ui) {
		super(ui);
	}

	private int[][] dotSprite = {
			{1,1,0},
			{1,0,2},
			{0,2,2}
	};
	private Color[] dotColors = {PgsLookAndFeel.getPrimaryControlShadow(),
			ColorUtils.getTranslucentColor(PgsLookAndFeel.getControlHighlight(), 50)};
	public void paint(Graphics g) {
		Dimension size = getSize();
		Color bgColor = getBackground();
		if (bgColor != null) {
			g.setColor(bgColor);
			g.fillRect(0, 0, size.width, size.height);
		}
		// paint dots
		final int numberOfDots = 5;
		final int spaceBetween = 4;
		final int dotSpace = numberOfDots * 3 + spaceBetween * (numberOfDots-1);
		if(orientation == JSplitPane.HORIZONTAL_SPLIT) {
			int startX = (size.width >> 1) - 2;
			int startY = (size.height >> 1) - (dotSpace>>1);
			for(int i = 0; i < numberOfDots; i++) {
				PgsUtils.paintSprite(g, startX, startY, dotSprite, dotColors);
				startY += 3 + spaceBetween;
			}
		} else {
			int startX = (size.width >> 1) - (dotSpace>>1);
			int startY = (size.height >> 1) - 2;
			for(int i = 0; i < numberOfDots; i++) {
				PgsUtils.paintSprite(g, startX, startY, dotSprite, dotColors);
				startX += 3 + spaceBetween;
			}
		}

		super.paint(g);
	}

	/**
	 * Creates and return an instance of JButton that can be used to
	 * collapse the left component in the metal split pane.
	 */
	protected JButton createLeftOneTouchButton() {
		JButton b = new JButton() {
			// Sprite buffer for the arrow image of the left button
			int[][] buffer = {
					{0, 0, 0, 1, 1, 0, 0, 0, 0},
					{0, 0, 1, 1, 1, 1, 0, 0, 0},
					{0, 1, 1, 1, 1, 1, 1, 0, 0},
					{1, 1, 1, 1, 1, 1, 1, 1, 0},
					{0, 0, 0, 0, 0, 0, 0, 0, 0}};

			public void setBorder(Border b) {
			}

			public void paint(Graphics g) {
				JSplitPane splitPane = getSplitPaneFromSuper();
				if (splitPane != null) {
					int oneTouchSize = getOneTouchSizeFromSuper();
					int orientation = getOrientationFromSuper();
					int blockSize = Math.min(
							getDividerSize(),
							oneTouchSize);

					// Initialize the color array
					Color[]	 colors = {
							this.getBackground(),
							PgsLookAndFeel.getPrimaryControlShadow(),
							PgsLookAndFeel.getPrimaryControlInfo(),
							PgsLookAndFeel.getPrimaryControlHighlight()};

					// Fill the background first ...
					g.setColor(this.getBackground());
					if (isOpaque()) {
						g.fillRect(
								0, 0, this.getWidth(),
								this.getHeight());
					}

					// ... then draw the arrow.
					if (getModel().isPressed()) {
						// Adjust color mapping for pressed button state
						colors[1] = colors[2];
					} else if (getModel().isRollover()) {
						colors[1] = PgsLookAndFeel.getPrimaryControlDarkShadow();
					}
					if (orientation == JSplitPane.VERTICAL_SPLIT) {
						// Draw the image for a vertical split
						for (int i = 1; i <= buffer[0].length; i++) {
							for (int j = 1; j < blockSize; j++) {
								if (buffer[j - 1][i - 1] == 0) {
									continue;
								} else {
									g.setColor(
											colors[buffer[j - 1][i - 1]]);
								}
								g.drawLine(i, j, i, j);
							}
						}
					} else {
						// Draw the image for a horizontal split
						// by simply swaping the i and j axis.
						// Except the drawLine() call this code is
						// identical to the code block above. This was done
						// in order to remove the additional orientation
						// check for each pixel.
						for (int i = 1; i <= buffer[0].length; i++) {
							for (int j = 1; j < blockSize; j++) {
								if (buffer[j - 1][i - 1] == 0) {
									// Nothing needs
									// to be drawn
									continue;
								} else {
									// Set the color from the
									// color map
									g.setColor(
											colors[buffer[j - 1][i - 1]]);
								}
								// Draw a pixel
								g.drawLine(j, i, j, i);
							}
						}
					}
				}
			}

			// Don't want the button to participate in focus traversable.
			public boolean isFocusTraversable() {
				return false;
			}
		};
		b.setRequestFocusEnabled(false);
		b.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		b.setFocusPainted(false);
		b.setBorderPainted(false);
		maybeMakeButtonOpaque(b);
		return b;
	}

	/**
	 * Creates and return an instance of JButton that can be used to
	 * collapse the right component in the metal split pane.
	 */
	protected JButton createRightOneTouchButton() {
		JButton b = new JButton() {
			// Sprite buffer for the arrow image of the right button
			int[][] buffer = {
					{1, 1, 1, 1, 1, 1, 1, 1},
					{0, 1, 1, 1, 1, 1, 1, 0},
					{0, 0, 1, 1, 1, 1, 0, 0},
					{0, 0, 0, 1, 1, 0, 0, 0},
					{0, 0, 0, 0, 0, 0, 0, 0}};

			public void setBorder(Border border) {
			}

			public void paint(Graphics g) {
				JSplitPane splitPane = getSplitPaneFromSuper();
				if (splitPane != null) {
					int oneTouchSize = getOneTouchSizeFromSuper();
					int orientation = getOrientationFromSuper();
					int blockSize = Math.min(
							getDividerSize(),
							oneTouchSize);

					// Initialize the color array
					Color[]	 colors = {
							this.getBackground(),
							PgsLookAndFeel.getPrimaryControlShadow(),
							PgsLookAndFeel.getPrimaryControlInfo(),
							PgsLookAndFeel.getPrimaryControlHighlight()};

					// Fill the background first ...
					g.setColor(this.getBackground());
					if (isOpaque()) {
						g.fillRect(
								0, 0, this.getWidth(),
								this.getHeight());
					}

					// ... then draw the arrow.
					if (getModel().isPressed()) {
						// Adjust color mapping for pressed button state
						colors[1] = colors[2];
					} else if (getModel().isRollover()) {
						colors[1] = PgsLookAndFeel.getPrimaryControlDarkShadow();
					}
					if (orientation == JSplitPane.VERTICAL_SPLIT) {
						// Draw the image for a vertical split
						for (int i = 1; i <= buffer[0].length; i++) {
							for (int j = 1; j < blockSize; j++) {
								if (buffer[j - 1][i - 1] == 0) {
									continue;
								} else {
									g.setColor(
											colors[buffer[j - 1][i - 1]]);
								}
								g.drawLine(i, j, i, j);
							}
						}
					} else {
						// Draw the image for a horizontal split
						// by simply swaping the i and j axis.
						// Except the drawLine() call this code is
						// identical to the code block above. This was done
						// in order to remove the additional orientation
						// check for each pixel.
						for (int i = 1; i <= buffer[0].length; i++) {
							for (int j = 1; j < blockSize; j++) {
								if (buffer[j - 1][i - 1] == 0) {
									// Nothing needs
									// to be drawn
									continue;
								} else {
									// Set the color from the
									// color map
									g.setColor(
											colors[buffer[j - 1][i - 1]]);
								}
								// Draw a pixel
								g.drawLine(j, i, j, i);
							}
						}
					}
				}
			}

			// Don't want the button to participate in focus traversable.
			public boolean isFocusTraversable() {
				return false;
			}
		};
		b.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		b.setFocusPainted(false);
		b.setBorderPainted(false);
		b.setRequestFocusEnabled(false);
		maybeMakeButtonOpaque(b);
		return b;
	}

	private void maybeMakeButtonOpaque(JComponent c) {
		Object opaque = UIManager.get("SplitPane.oneTouchButtonsOpaque");
		if (opaque != null) {
			c.setOpaque(((Boolean) opaque).booleanValue());
		}
	}

	int getOneTouchSizeFromSuper() {
		return super.ONE_TOUCH_SIZE;
	}

	int getOneTouchOffsetFromSuper() {
		return super.ONE_TOUCH_OFFSET;
	}

	int getOrientationFromSuper() {
		return super.orientation;
	}

	JSplitPane getSplitPaneFromSuper() {
		return super.splitPane;
	}

	JButton getLeftButtonFromSuper() {
		return super.leftButton;
	}

	JButton getRightButtonFromSuper() {
		return super.rightButton;
	}
}
