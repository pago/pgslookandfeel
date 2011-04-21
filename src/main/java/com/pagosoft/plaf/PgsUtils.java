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
import javax.swing.text.*;
import javax.swing.text.html.*;
import java.awt.*;
import java.awt.image.*;
import java.util.HashMap;
import java.util.Map;

public class PgsUtils {
	/**
	 * The given text is surrounded by a HTML document with a specific style. Implemented elements are h1, body and p.
	 * Every text should be surrounded within a <code>p</code>-Element and a tooltip should have a single <code>h1</code>
	 * at the beginning of the document.
	 *
	 * Example:
	 * <pre><code>comp.setToolTipText(PgsUtils.createHtmlToolTip(new StringBuilder()
				.append("&lt;h1&gt;Tooltips changed, too&lt;/h1&gt;")
				.append("&lt;p&gt;Office 2007 and Windows Vista have introduced a new kind&lt;br&gt;")
				.append("of UI for tooltips. To make it short: They should look better now.&lt;/p&gt;")
				.append("&lt;p&gt;Therefore Tooltips in PgsLookAndFeel now feature a background gradient&lt;br&gt;")
				.append("as well as a default style for HTML text within them. This is available through&lt;br&gt;")
				.append("a utility method in PgsUtils.&lt;/p&gt;")
				.toString()));</code></pre>
	 * @param text
	 */
	public static String createHtmlToolTip(String text) {
		Font defaultFont = UIManager.getFont("Button.font");
		return new StringBuffer()
				.append("<html><head><style type='text/css'>")
				.append("body {padding: 5px}")
				.append("h1 {font-size: ").append(defaultFont.getSize()).append("pt; margin: 0 0 2px 0; padding:0; color: ")
						.append(ColorUtils.toString(PgsLookAndFeel.getPrimaryControlDarkShadow())).append("}")
				.append("p {margin-left: 15px}")
				.append("</style></head><body>")
				.append(text)
				.append("</body>")
				.toString();
	}

	public static boolean isFlat(JComponent b) {
		return Boolean.TRUE.equals(b.getClientProperty("pgs.isFlat"));
	}

	public static boolean isFlat(String id) {
		return Boolean.TRUE.equals(UIManager.get(id + ".isFlat"));
	}

	/**
	 * Paint the given sprite. The value of the sprite array is the color to pick from the colors
	 * array. If it is 0, nothing is painted. The first color (index 0 in colors) is qualified as 1.
	 * @param g
	 * @param x
	 * @param y
	 * @param sprite
	 * @param colors
	 */
	public static void paintSprite(Graphics g, int x, int y, int[][] sprite, Color[] colors) {
		for(int i=0; i < sprite.length; i++) {
			for(int j=0; j < sprite[i].length; j++) {
				if(sprite[i][j] == 0) continue;
				g.setColor(colors[sprite[i][j]-1]);
				g.drawLine(x+i,y+j,x+i,y+j);
			}
		}
	}

	/**
	 * This code is taken from the "TabContainer" of NetBeans
	 */
	/*
	 *                 Sun Public License Notice
	 *
	 * The contents of this file are subject to the Sun Public License
	 * Version 1.0 (the "License"). You may not use this file except in
	 * compliance with the License. A copy of the License is available at
	 * http://www.sun.com/
	 *
	 * The Original Code is NetBeans. The Initial Developer of the Original
	 * Code is Sun Microsystems, Inc. Portions Copyright 1997-2003 Sun
	 * Microsystems, Inc. All Rights Reserved.
	 */
	private static HashMap gpCache;

	public static GradientPaint getGradientPaint(int x, int y, int width, int height, Color from, Color to) {
		if (gpCache == null) {
			gpCache = new HashMap(20);
		}

		//Generate a hash code for looking up an existing paint
		long bits = Double.doubleToLongBits(x)
				+ Double.doubleToLongBits(y) * 37 + Double.doubleToLongBits(
				width) * 43 + Double.doubleToLongBits(height) * 47;
		int hash = ((((int) bits) ^ ((int) (bits >> 32)))
				^ from.hashCode() ^ (to.hashCode() * 17)) * 31;

		Object key = new Integer(hash);
		GradientPaint result = (GradientPaint) gpCache.get(key);
		if (result == null) {
			result = new GradientPaint(0, 0, from, 0, height, to, true);
			if (gpCache.size() > 40) {
				gpCache.clear();
			}
			gpCache.put(key, result);
		}
		return result;
	}
	/* End of code */

	public static void drawGradient(Graphics g, int width, int height, Color from, Color to) {
		drawGradient(g, 0, 0, width, height, from, to);
	}

	public static void drawGradient(Graphics g, int x, int y, int width, int height, Color from, Color to) {
		Graphics2D gfx = (Graphics2D) g;
		gfx.setPaint(getGradientPaint(x, y, width, height, from, to));
		gfx.fill(new Rectangle(x, y, width, height));
	}

	public static void drawGradient(Graphics g, JComponent c, String prefix, int x, int y, int width, int height) {
		drawGradient(g, x, y, width, height,
				(Color) c.getClientProperty(prefix + ".gradientStart"),
				(Color) c.getClientProperty(prefix + ".gradientEnd"));
	}

	public static void drawGradient(Graphics g, JComponent c, String prefix) {
		drawGradient(
				g, c.getWidth(), c.getHeight(),
				(Color) c.getClientProperty(prefix + ".gradientStart"),
				(Color) c.getClientProperty(prefix + ".gradientEnd"));
	}

	public static void drawGradient(Graphics g, JComponent c, String prefix1, String prefix2) {
		drawGradient(
				g, c.getWidth(), c.getHeight(),
				(Color) c.getClientProperty(prefix1 + ".gradientStart"),
				(Color) c.getClientProperty(prefix2 + ".gradientEnd"));
	}

	public static void drawGradient(Graphics g, JComponent c) {
		drawGradient(
				g, c.getWidth(), c.getHeight(),
				(Color) c.getClientProperty("gradientStart"),
				(Color) c.getClientProperty("gradientEnd"));
	}

	public static void drawVistaBackground(Graphics g, Component b, String prefix) {
		drawVistaBackground(
				g, 0, 0, b.getWidth(), b.getHeight(),
				UIManager.getColor(prefix + ".gradientStart"),
				UIManager.getColor(prefix + ".gradientMiddle"),
				UIManager.getColor(prefix + ".gradientEnd"));
	}

	public static void drawVistaBackground(Graphics g, Component b, Color start, Color mid, Color end) {
		drawVistaBackground(g, 0, 0, b.getWidth(), b.getHeight(), start, mid, end);
	}

	public static void drawVistaBackground(
			Graphics g, int x, int y, int width, int height, Color start, Color mid, Color end) {
		g.setColor(start);
		g.fillRect(x, y, width, height / 2);
		g.setColor(end);
		g.fillRect(x, y + height / 2, width, height / 2 + 1);
		g.setColor(mid);
		g.drawLine(x, y + height / 2, width, y + height / 2);
	}

	protected static void paintMenuItemBackground(Graphics g, AbstractButton menuItem, Color bgColor, String prefix) {
		ButtonModel model = menuItem.getModel();
		Color oldColor = g.getColor();
		Dimension size = menuItem.getSize();
		Insets ins = UIManager.getInsets(prefix + ".selectedBorderMargin");
		Rectangle rect = new Rectangle(
				ins.left,
				ins.top,
				size.width - ins.right - ins.left,
				size.height - ins.top - ins.bottom - 2);

		if (menuItem.isOpaque()) {
			g.setColor(menuItem.getBackground());
			g.fillRect(0, 0, size.width, size.height);
			if (model.isArmed() ||
					(menuItem instanceof JMenu && model.isSelected())) {
				g.setColor(bgColor);
				if (PgsUtils.isFlat("MenuItem")) {
					g.fillRect(
							(int) rect.getX(), (int) rect.getY(),
							(int) rect.getWidth(), (int) rect.getHeight());
				} else {
					PgsUtils.drawGradient(
							g, (int) rect.getX(), (int) rect.getY(),
							(int) rect.getWidth(), (int) rect.getHeight(),
							UIManager.getColor(prefix + ".gradientStart"),
							UIManager.getColor(prefix + ".gradientEnd"));
				}
				g.setColor(UIManager.getColor(prefix + ".selectedBorderColor"));
				g.drawRect(
						(int) rect.getX(), (int) rect.getY(),
						(int) rect.getWidth(), (int) rect.getHeight());
			}
			g.setColor(oldColor);
		}
	}

	public static boolean isLeftToRight(Component c) {
		return c.getComponentOrientation().isLeftToRight();
	}

	// Toolbar icons specific
	public static Icon getToolBarIcon(Image i) {
		if (!PlafOptions.isToolBarIconUsed()) {
			return null;
		}
		return new IconUIResource(new ShadowedIcon(new ImageIcon(i)));
		/*ImageProducer prod = new FilteredImageSource(i.getSource(),
				new ToolBarImageFilter());
		return new IconUIResource(new ImageIcon(
				Toolkit.getDefaultToolkit().createImage(prod)));*/
	}

	public static Icon getDisabledButtonIcon(Image image) {
		if (!PlafOptions.isDisabledIconUsed()) {
			return null;
		}
		return new IconUIResource(new ImageIcon(GrayFilter.createDisabledImage(image)));
	}

	// http://jroller.com/page/santhosh/20050521#beautify_swing_applications_toolbar_with
	public static class ShadowedIcon implements Icon {
		private int shadowWidth = 2;
		private int shadowHeight = 2;
		private Icon icon, shadow;

		public ShadowedIcon(Icon icon) {
			this.icon = icon;
			shadow = new ImageIcon(GrayFilter.createDisabledImage(((ImageIcon) icon).getImage()));
		}

		public ShadowedIcon(Icon icon, int shadowWidth, int shadowHeight) {
			this(icon);
			this.shadowWidth = shadowWidth;
			this.shadowHeight = shadowHeight;
		}

		public int getIconHeight() {
			return icon.getIconWidth() + shadowWidth;
		}

		public int getIconWidth() {
			return icon.getIconHeight() + shadowHeight;
		}

		public void paintIcon(Component c, Graphics g, int x, int y) {
			shadow.paintIcon(c, g, x + shadowWidth, y + shadowHeight);
			icon.paintIcon(c, g, x, y);
		}
	}

	private static class ToolBarImageFilter extends RGBImageFilter {
		public ToolBarImageFilter() {
			canFilterIndexColorModel = true;
		}

		public int filterRGB(int x, int y, int rgb) {
			int r = ((rgb >> 16) & 0xff);
			int g = ((rgb >> 8) & 0xff);
			int b = (rgb & 0xff);
			int gray = Math.max(Math.max(r, g), b);
			return (rgb & 0xff000000) | (gray << 16) | (gray << 8) |
					(gray << 0);
		}
	}

	public static void drawVerticalBumps(Graphics g, int x, int y, int height) {
		int loops = height / 6;
		for (int i = 0; i < loops; i++) {
			g.setColor(PgsLookAndFeel.getControlShadow());
			g.fillRect(x, y + (i * 6), 2, 2);
			g.fillRect(x + 3, 3 + y + (i * 6), 2, 2);

			g.setColor(ColorUtils.getTranslucentColor(PgsLookAndFeel.getControl(), 180));
			g.fillRect(x + 1, 1 + y + (i * 6), 2, 2);
			g.fillRect(x + 4, 4 + y + (i * 6), 2, 2);
		}
	}

	public static void drawHorizontalBumps(Graphics g, int x, int y, int width) {
		int loops = width / 6;
		for (int i = 0; i < loops; i++) {
			g.setColor(PgsLookAndFeel.getControlShadow());
			g.fillRect(x + (i * 6), y, 2, 2);
			g.fillRect(3 + x + (i * 6), y + 3, 2, 2);

			g.setColor(ColorUtils.getTranslucentColor(PgsLookAndFeel.getControl(), 180));
			g.fillRect(1 + x + (i * 6), y + 1, 2, 2);
			g.fillRect(4 + x + (i * 6), y + 4, 2, 2);
		}
	}

	public static boolean hasFocus(Component component) {
		if (component instanceof Container) {
			Container c = (Container) component;
			for (int i = 0; i < c.getComponentCount(); i++) {
				if (hasFocus(c.getComponent(i))) {
					return true;
				}
			}
		}
		return component != null && component.isFocusOwner();
	}


	//--------------------------------------------------------------------------
	//public static Stroke borderStroke = new BasicStroke(1.4f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
	public static Stroke borderStroke = new BasicStroke(1.3f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
	public static Stroke rolloverBorderStroke = new BasicStroke(2.4f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);

	public static void regenerateBorderStroke() {
		if (PlafOptions.isClearBorderEnabled()) {
			rolloverBorderStroke = new BasicStroke(2.4f, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER);
		} else {
			rolloverBorderStroke = new BasicStroke(2.4f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
		}
	}

	public static void drawButtonBorder(Graphics g, int x, int y, int w, int h, Stroke st, Color c) {
		Graphics2D gfx = (Graphics2D) g;
		Stroke s = gfx.getStroke();
		gfx.setStroke(st);
		g.setColor(c);
		if (PlafOptions.isClearBorderEnabled()) {
			g.drawRect(x + 1, y + 1, w - 1, h - 1);
			// Stupid hack, but it works...
			g.drawLine(x, y, x, y + h);
		} else {
			drawRoundRect(g, x, y, w, h);
		}
		gfx.setStroke(s);
	}

	public static void drawButtonBorder(Graphics g, int x, int y, int w, int h, Color c) {
		if (PlafOptions.isClearBorderEnabled()) {
			g.setColor(c);
			g.drawRect(x, y, w, h);
		} else {
			Graphics2D gfx = (Graphics2D) g;
			Stroke s = gfx.getStroke();
			gfx.setStroke(borderStroke);
			g.setColor(c);
			drawRoundRect(g, x, y, w, h);
			gfx.setStroke(s);
		}
	}

	public static void drawButtonBorder(Graphics g, int x, int y, int w, int h, int c1, int c2, Color c) {
		if (PlafOptions.isClearBorderEnabled()) {
			g.setColor(c);
			g.drawRect(x, y, w, h);
		} else {
			Graphics2D gfx = (Graphics2D) g;
			Stroke s = gfx.getStroke();
			gfx.setStroke(borderStroke);
			g.setColor(c);
			drawRoundRect(g, x, y, w, h, c1, c2);
			gfx.setStroke(s);
		}
	}

	public static void drawDefaultButtonBorder(Graphics g, int x, int y, int w, int h, boolean isRollover) {
		clearButtonBorder(g, x, y, w, h);
		drawButtonBorder(g, x + 1, y + 1, w - 3, h - 3, borderStroke, PgsLookAndFeel.getPrimaryControlShadow());
		if (isRollover) {
			drawRolloverButtonBorder(g, x + 1, y + 1, w - 3, h - 3);
		}
		drawButtonBorder(g, x, y, w - 1, h - 1, PgsLookAndFeel.getPrimaryControlDarkShadow());
	}

	public static void drawButtonBorder(Graphics g, int x, int y, int w, int h) {
		clearButtonBorder(g, x, y, w, h);
		drawButtonBorder(g, x, y, w - 1, h - 1, PgsLookAndFeel.getControlDarkShadow());
	}

	public static void drawRolloverButtonBorder(Graphics g, int x, int y, int w, int h) {
		if (UIManager.getBoolean("Button.rolloverVistaStyle")) {
			drawButtonBorder(g, x, y, w - 1, h - 1, PgsLookAndFeel.getGlow());
		} else {
			drawButtonBorder(g, x + 1, y + 1, w - 3, h - 3, rolloverBorderStroke, PgsLookAndFeel.getGlow());
			PgsUtils.drawButtonBorder(g, x, y, w, h);
		}
	}

	private static void clearButtonBorder(Graphics g, int x, int y, int w, int h) {
		if (PlafOptions.isClearBorderEnabled()) {
			// We don't need to clear anything if we use ClearBorder
			return;
		}
		g.setColor(UIManager.getColor("Panel.background"));
		g.drawRect(x, y, w - 1, h - 1);
	}

	public static void drawDisabledBorder(Graphics g, int x, int y, int w, int h) {
		drawDisabledBorder(g, x, y, w, h, 4, 4);
	}

	public static void drawDisabledBorder(Graphics g, int x, int y, int w, int h, int c1, int c2) {
		clearButtonBorder(g, x, y, w, h);
		g.setColor(PgsLookAndFeel.getControlShadow());
		if (PlafOptions.isClearBorderEnabled()) {
			g.drawRect(x, y, w, h);
		} else {
			drawRoundRect(g, x, y, w, h, c1, c2);
		}
	}

	public static void drawRoundRect(Graphics g, int x, int y, int w, int h) {
		drawRoundRect(g, x, y, w, h, 4, 4);
	}

	public static void drawRoundRect(Graphics g, int x, int y, int w, int h, int c1, int c2) {
		Graphics2D gfx = (Graphics2D) g;
		gfx.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		gfx.drawRoundRect(x, y, w, h, c1, c2);
		gfx.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_DEFAULT);
	}

	// drawing text
	private static Map hintsMap, oldHintsMap;

	public static void installAntialiasing(Graphics g) {
		if (!PlafOptions.isAntialiasingEnabled()) {
			return;
		}
		Graphics2D gfx = (Graphics2D) g;
		if (hintsMap == null) {
			hintsMap = new HashMap();
			hintsMap.put(
					RenderingHints.KEY_FRACTIONALMETRICS,
					RenderingHints.VALUE_FRACTIONALMETRICS_ON);
			hintsMap.put(
					RenderingHints.KEY_TEXT_ANTIALIASING,
					RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
			hintsMap.put(
					RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);

			oldHintsMap = new HashMap();
			oldHintsMap.put(
					RenderingHints.KEY_FRACTIONALMETRICS,
					gfx.getRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS));
			oldHintsMap.put(
					RenderingHints.KEY_TEXT_ANTIALIASING,
					gfx.getRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING));
			oldHintsMap.put(
					RenderingHints.KEY_ANTIALIASING,
					gfx.getRenderingHint(RenderingHints.KEY_ANTIALIASING));
		}
		gfx.addRenderingHints(hintsMap);
	}

	public static void uninstallAntialiasing(Graphics g) {
		if (!PlafOptions.isAntialiasingEnabled()) {
			return;
		}
		((Graphics2D) g).addRenderingHints(oldHintsMap);
	}

	/*
		 * $ $ License.
		 *
		 * Copyright $ L2FProd.com
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

	/**
	 * This method fixes the display of HTML enabled components (like JEditorPanes
	 * and JTextPanes).
	 */
	public static void fixHtmlDisplay(JComponent component) {
		Font defaultFont = UIManager.getFont("Button.font");

		String stylesheet =
				"body { margin-top: 0; margin-bottom: 0; margin-left: 0; margin-right: 0; font-family: "
						+ defaultFont.getName()
						+ "; font-size: "
						+ defaultFont.getSize()
						+ "pt;	}"
						+ "a, p, li { margin-top: 0; margin-bottom: 0; margin-left: 0; margin-right: 0; font-family: "
						+ defaultFont.getName()
						+ "; font-size: "
						+ defaultFont.getSize()
						+ "pt;	}";

		try {
			HTMLDocument doc = null;
			if (component instanceof JEditorPane) {
				if (((JEditorPane) component).getDocument() instanceof HTMLDocument) {
					doc = (HTMLDocument) ((JEditorPane) component).getDocument();
				}
			} else {
				View v =
						(View) component.getClientProperty(
								javax.swing.plaf.basic.BasicHTML.propertyKey);
				if (v != null && v.getDocument() instanceof HTMLDocument) {
					doc = (HTMLDocument) v.getDocument();
				}
			}
			if (doc != null) {
				doc.getStyleSheet().loadRules(
						new java.io.StringReader(stylesheet),
						null);
			} // end of if (doc != null)
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
