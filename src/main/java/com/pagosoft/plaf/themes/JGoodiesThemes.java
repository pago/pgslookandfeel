/*
 * JGoodiesThemes.java
 *
 * Created on 10. April 2005, 11:16
 */

package com.pagosoft.plaf.themes;

import com.pagosoft.plaf.PgsTheme;

import javax.swing.plaf.*;
import javax.swing.plaf.metal.*;
import java.awt.*;

/**
 * @author pago
 */
public class JGoodiesThemes {
	private JGoodiesThemes() {
	}

	private static MetalTheme[] themes = new MetalTheme[21];

	public static MetalTheme getBrownSugar() {
		if (themes[0] == null) {
			themes[0] = new PgsTheme(
					"Brown Sugar",
					new ColorUIResource(83, 83, 61),
					new ColorUIResource(115, 107, 82),
					new ColorUIResource(156, 156, 123),

					new ColorUIResource(35, 33, 29),
					new ColorUIResource(105, 99, 87),
					new ColorUIResource(92, 87, 76),

					Color.WHITE, Color.BLACK);
		}
		return themes[0];
	}

	public static MetalTheme getDarkStar() {
		if (themes[1] == null) {
			themes[1] = new PgsTheme(
					"Dark Star",
					new ColorUIResource(83, 83, 61),
					new ColorUIResource(115, 107, 82),
					new ColorUIResource(156, 156, 123),

					new ColorUIResource(32, 32, 32),
					new ColorUIResource(96, 96, 96),
					new ColorUIResource(84, 84, 84),

					Color.WHITE, Color.BLACK);
		}
		return themes[1];
	}

	public static MetalTheme getDesertBlue() {
		if (themes[2] == null) {
			themes[2] = new PgsTheme("Desert Blue",
					Colors.GRAY_DARK,
					Colors.BLUE_LOW_MEDIUM,
					Colors.BLUE_LOW_LIGHTEST,

					Colors.GRAY_MEDIUM,
					new ColorUIResource(148, 144, 140),
					new ColorUIResource(211, 210, 204),

					Color.BLACK, Color.WHITE,

					new Object[]{
							"ScrollBar.thumbHighlight", Colors.BLUE_LOW_LIGHTEST,
							"ScrollBar.thumb", Colors.BLUE_MEDIUM_DARK,

							"ProgressBar.innerGradientStart", Colors.BLUE_LOW_LIGHTEST,
							"ProgressBar.innerGradientEnd", Colors.BLUE_MEDIUM_DARK
					});
		}
		return themes[2];
	}
}
