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
import javax.swing.plaf.*;
import javax.swing.plaf.basic.*;
import java.awt.*;
import java.awt.geom.*;

public class PgsProgressBarUI extends BasicProgressBarUI {
	private Rectangle boxRect;

	public static ComponentUI createUI(JComponent x) {
		return new PgsProgressBarUI();
	}

	public void paint(Graphics g, JComponent c) {
		JProgressBar prog = (JProgressBar) c;
		Graphics2D gfx = (Graphics2D) g;
		Dimension size = c.getSize();
		if (true) {
			gfx.setColor(c.getBackground());
		} else if (prog.getOrientation() == JProgressBar.HORIZONTAL) {
			gfx.setPaint(
					new GradientPaint(
							0, 0, UIManager.getColor("ProgressBar.gradientStart"), 0, size.height / 2,
							UIManager.getColor("ProgressBar.gradientEnd"), true));
		} else {
			gfx.setPaint(
					new GradientPaint(
							0, 0, UIManager.getColor("ProgressBar.gradientStart"), size.width / 2, 0,
							UIManager.getColor("ProgressBar.gradientEnd"), true));
		}
		gfx.fill(new Rectangle(0, 0, size.width, size.height));
		super.paint(g, c);
	}

	/**
	 * All purpose paint method that should do the right thing for almost
	 * all linear, determinate progress bars. By setting a few values in
	 * the defaults
	 * table, things should work just fine to paint your progress bar.
	 * Naturally, override this if you are making a circular or
	 * semi-circular progress bar.
	 *
	 * @see #paintIndeterminate
	 * @since 1.4
	 */
	protected void paintDeterminate(Graphics g, JComponent c) {
		if (!(g instanceof Graphics2D)) {
			return;
		}

		Insets b = progressBar.getInsets(); // area for border
		int barRectWidth = progressBar.getWidth() - (b.right + b.left);
		int barRectHeight = progressBar.getHeight() - (b.top + b.bottom);

		// amount of progress to draw
		int amountFull = getAmountFull(b, barRectWidth, barRectHeight);

		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(progressBar.getForeground());

		if (progressBar.getOrientation() == JProgressBar.HORIZONTAL) {
			if (PgsUtils.isLeftToRight(c)) {
				if (progressBar.isEnabled()) {
					g2.setPaint(
							new GradientPaint(
									0, 0,
									UIManager.getColor(
											"ProgressBar.innerGradientStart"),
									0, (barRectHeight / 2),
									UIManager.getColor(
											"ProgressBar.innerGradientEnd"),
									true));
				} else {
					g2.setPaint(
							new GradientPaint(
									0, 0,
									UIManager.getColor(
											"ProgressBar.innerDisabledGradientStart"),
									0, (barRectHeight / 2),
									UIManager.getColor(
											"ProgressBar.innerDisabledGradientEnd"),
									true));
				}
				g2.fillRect(
						b.left + 2, b.top + 2,
						amountFull + b.left - b.right - 4, barRectHeight - 4);
				g2.setColor(
						UIManager.getColor(
								progressBar.isEnabled()
										? "ProgressBar.innerBorderColor"
										: "ProgressBar.innerDisabledBorderColor"));
				g2.drawRect(
						b.left + 1, b.top + 1,
						amountFull + b.left - b.right - 3, barRectHeight - 3);
			} else {
				if (progressBar.isEnabled()) {
					g2.setPaint(
							new GradientPaint(
									0, 0,
									UIManager.getColor(
											"ProgressBar.innerGradientStart"),
									0, (barRectHeight / 2),
									UIManager.getColor(
											"ProgressBar.innerGradientEnd"),
									true));
				} else {
					g2.setPaint(
							new GradientPaint(
									0, 0,
									UIManager.getColor(
											"ProgressBar.innerDisabledGradientStart"),
									0, (barRectHeight / 2),
									UIManager.getColor(
											"ProgressBar.innerDisabledGradientEnd"),
									true));
				}
				g2.fillRect(
						b.left + barRectWidth - amountFull + 2, b.top + 2,
						amountFull + b.left - b.right - 4, barRectHeight - 4);
				g2.setColor(
						UIManager.getColor(
								progressBar.isEnabled()
										? "ProgressBar.innerBorderColor"
										: "ProgressBar.innerDisabledBorderColor"));
				g2.drawRect(
						(b.left + barRectWidth - amountFull) - 1,
						b.top + 1,
						amountFull + b.left - b.right - 3,
						barRectHeight - 3);
			}

		} else { // VERTICAL
			if (progressBar.isEnabled()) {
				g2.setPaint(
						new GradientPaint(
								0, 0,
								UIManager.getColor(
										"ProgressBar.innerGradientStart"),
								(barRectWidth / 2), 0,
								UIManager.getColor(
										"ProgressBar.innerGradientEnd"),
								true));
			} else {
				g2.setPaint(
						new GradientPaint(
								0, 0,
								UIManager.getColor(
										"ProgressBar.innerDisabledGradientStart"),
								(barRectWidth / 2), 0,
								UIManager.getColor(
										"ProgressBar.innerDisabledGradientEnd"),
								true));
			}
			g2.fillRect(
					b.left + 2,
					b.top + barRectHeight - b.bottom - amountFull - 3,
					barRectWidth - b.left - 3,
					amountFull + 2);
			g2.setColor(
					UIManager.getColor(
							progressBar.isEnabled()
									? "ProgressBar.innerBorderColor"
									: "ProgressBar.innerDisabledBorderColor"));
			g2.drawRect(
					b.left + 1,
					b.top + barRectHeight - b.bottom - amountFull - 4,
					barRectWidth - b.left - 2,
					amountFull + 3);
		}

		// Deal with possible text painting
		if (progressBar.isStringPainted()) {
			paintString(
					g, b.left, b.top,
					barRectWidth, barRectHeight,
					amountFull, b);
		}
	}

	/**
	 * All purpose paint method that should do the right thing for all
	 * linear bouncing-box progress bars.
	 * Override this if you are making another kind of
	 * progress bar.
	 *
	 * @see #paintDeterminate
	 * @since 1.4
	 */
	protected void paintIndeterminate(Graphics g, JComponent c) {
		if (!(g instanceof Graphics2D)) {
			return;
		}

		Insets b = progressBar.getInsets(); // area for border
		int barRectWidth = progressBar.getWidth() - (b.right + b.left);
		int barRectHeight = progressBar.getHeight() - (b.top + b.bottom);

		Graphics2D g2 = (Graphics2D) g;

		// Paint the bouncing box.
		boxRect = getBox(boxRect);
		if (boxRect != null) {
			if (progressBar.getOrientation() == JProgressBar.HORIZONTAL) {
				g2.setPaint(
						new GradientPaint(
								0, 0,
								UIManager.getColor(
										"ProgressBar.innerGradientStart"),
								0, (barRectHeight / 2),
								UIManager.getColor(
										"ProgressBar.innerGradientEnd"),
								true));
				g2.fillRect(
						boxRect.x + b.left + 2, boxRect.y + 2,
						boxRect.width + b.left - b.right - 4,
						boxRect.height - 4);
				g2.setColor(UIManager.getColor("ProgressBar.innerBorderColor"));
				g2.drawRect(
						boxRect.x + b.left + 1, boxRect.y + 1,
						boxRect.width + b.left - b.right - 3,
						boxRect.height - 3);
			} else {
				g2.setPaint(
						new GradientPaint(
								0, 0,
								UIManager.getColor(
										"ProgressBar.innerGradientStart"),
								(barRectWidth / 2), 0,
								UIManager.getColor(
										"ProgressBar.innerGradientEnd"),
								true));
				g2.fillRect(
						boxRect.x + b.left + 1,
						boxRect.y + b.top + 1,
						boxRect.width + b.left - b.right - 4,
						boxRect.height - 2 - b.bottom);
				g2.setColor(UIManager.getColor("ProgressBar.innerBorderColor"));
				g2.drawRect(
						boxRect.x + b.left,
						boxRect.y + b.top,
						boxRect.width + b.left - b.right - 3,
						boxRect.height - 1 - b.bottom);
			}
		}

		// Deal with possible text painting
		if (progressBar.isStringPainted()) {
			if (progressBar.getOrientation() == JProgressBar.HORIZONTAL) {
				paintString(
						g2, b.left, b.top,
						barRectWidth, barRectHeight,
						boxRect.x, boxRect.width, b);
			} else {
				paintString(
						g2, b.left, b.top,
						barRectWidth, barRectHeight,
						boxRect.y, boxRect.height, b);
			}
		}
	}

	/**
	 * Paints the progress string.
	 *
	 * @param g		  Graphics used for drawing.
	 * @param x		  x location of bounding box
	 * @param y		  y location of bounding box
	 * @param width	  width of bounding box
	 * @param height	 height of bounding box
	 * @param fillStart  start location, in x or y depending on orientation,
	 *                   of the filled portion of the progress bar.
	 * @param amountFull size of the fill region, either width or height
	 *                   depending upon orientation.
	 * @param b		  Insets of the progress bar.
	 */
	private void paintString(
			Graphics g, int x, int y, int width, int height,
			int fillStart, int amountFull, Insets b) {
		if (!(g instanceof Graphics2D)) {
			return;
		}

		PgsUtils.installAntialiasing(g);

		Graphics2D g2 = (Graphics2D) g;
		String progressString = progressBar.getString();
		g2.setFont(progressBar.getFont());
		Point renderLocation = getStringPlacement(
				g2, progressString,
				x, y, width, height);
		Rectangle oldClip = g2.getClipBounds();

		if (progressBar.getOrientation() == JProgressBar.HORIZONTAL) {
			g2.setColor(getSelectionBackground());
			g2.drawString(progressString, renderLocation.x, renderLocation.y);
			g2.setColor(getSelectionForeground());
			g2.clipRect(fillStart, y, amountFull, height);
			g.drawString(progressString, renderLocation.x, renderLocation.y);
		} else { // VERTICAL
			g2.setColor(getSelectionBackground());
			AffineTransform rotate =
					AffineTransform.getRotateInstance(Math.PI / 2);
			g2.setFont(progressBar.getFont().deriveFont(rotate));
			renderLocation = getStringPlacement(
					g2, progressString,
					x, y, width, height);
			g2.drawString(progressString, renderLocation.x, renderLocation.y);
			g2.setColor(getSelectionForeground());
			g2.clipRect(x, fillStart, width, amountFull);
			g2.drawString(progressString, renderLocation.x, renderLocation.y);
		}
		g2.setClip(oldClip);

		PgsUtils.uninstallAntialiasing(g);
	}
}
