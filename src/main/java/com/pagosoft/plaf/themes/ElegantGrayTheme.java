/*
 * ElegantGrayTheme.java
 *
 * Created on 7. Mai 2005, 13:42
 */

package com.pagosoft.plaf.themes;

import com.pagosoft.plaf.PgsTheme;
import com.pagosoft.swing.ColorUtils;

import javax.swing.plaf.*;
import java.awt.*;

/**
 * None of these themes should be used by now. They're in no way looking good.
 *
 * @author Patrick Gotthardt
 */
public class ElegantGrayTheme extends PgsTheme {
	private static ElegantGrayTheme INSTANCE;

	private ElegantGrayTheme(
			String themeName, Color p1, Color p2, Color p3,
			Color s1, Color s2, Color s3, Color bl, Color wh, Object[] d) {
		super(themeName, p1, p2, p3, s1, s2, s3, bl, wh, d);
	}

	public static PgsTheme getInstance() {
		if (INSTANCE == null) {
			Color s1 = new Color(0x636363);
			Color s2 = new Color(0x999999);
			Color s3 = new Color(0xd4d0c8);
			Color p1 = new Color(0x8795a1);
			Color p2 = new Color(0x92bad9);
			Color p3 = new Color(0xc1d6e6);

			ColorUIResource menuBg = new ColorUIResource(ColorUtils.getSimiliarColor(s3, 1.125f));
			Object[] d = new Object[]{
					"PopupMenu.background", menuBg,
					"Menu.background", menuBg,
					"MenuItem.background", menuBg,
					"RadioButtonMenuItem.background", menuBg,
					"CheckBoxMenuItem.background", menuBg,

					"Button.rolloverVistaStyle", Boolean.TRUE,
					"glow", new ColorUIResource(ColorUtils.getSimiliarColor(p1, 1.0f)),

					"ToolBar.gradientStart", s3,
					"ToolBar.gradientEnd", s2,
					"ToolBarButton.rolloverBackground", s2,
					"ToolBarButton.rolloverBorderColor", s1,
					"ToolBarButton.isFlat", Boolean.FALSE,
					"ToolBarButton.rolloverGradientStart", s2,
					"ToolBarButton.rolloverGradientEnd", s2,
					"ToolBarButton.selectedGradientStart", s2,
					"ToolBarButton.selectedGradientEnd", s3
			};

			INSTANCE = new ElegantGrayTheme(
					"Elegant Gray", p1, p2, p3, s1, s2,
					s3, Color.black, Color.white, d);
		}
		return INSTANCE;
	}
}
