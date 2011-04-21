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
import javax.swing.plaf.metal.*;
import java.awt.*;

public class PgsTheme extends DefaultMetalTheme {
	private String name;
	private ColorUIResource primary1;
	private ColorUIResource primary2;
	private ColorUIResource primary3;
	private ColorUIResource secondary1;
	private ColorUIResource secondary2;
	private ColorUIResource secondary3;
	private ColorUIResource black;
	private ColorUIResource white;
	private FontUIResource menuFont;
	private Object[] defaults;

	public PgsTheme(String name) {
		super();
		this.name = name;
		if (PlafOptions.useBoldMenuFonts()) {
			menuFont = super.getMenuTextFont();
		} else {
			menuFont = new FontUIResource(super.getMenuTextFont().deriveFont(Font.PLAIN));
		}
		black = new ColorUIResource(Color.BLACK);
		white = new ColorUIResource(Color.WHITE);
	}

	/**
	 * Convert a common MetalTheme to a PgsTheme (using colors only)
	 * /// (seems to be not required!)/
	 * public PgsTheme(MetalTheme t) {
	 * this(
	 * t.getName(),
	 * t.getPrimaryControlDarkShadow(), t.getPrimaryControlShadow(), t.getPrimaryControl(),
	 * t.getControlDarkShadow(), t.getControlShadow(), t.getControl(),
	 * t.getControlInfo(), t.getControlHighlight()
	 * );
	 * }//
	 */


	public PgsTheme(
			Color p1, Color p2, Color p3,
			Color s1, Color s2, Color s3) {
		this("PgsTheme", p1, p2, p3, s1, s2, s3, Color.black, Color.white);
	}

	public PgsTheme(
			String themeName,
			Color p1, Color p2, Color p3,
			Color s1, Color s2, Color s3) {
		this(themeName, p1, p2, p3, s1, s2, s3, Color.black, Color.white);
	}

	public PgsTheme(
			Color p1, Color p2, Color p3,
			Color s1, Color s2, Color s3,
			Color bl, Color wh) {
		this("PgsTheme", p1, p2, p3, s1, s2, s3, bl, wh);
	}

	public PgsTheme(
			String themeName,
			Color p1, Color p2, Color p3,
			Color s1, Color s2, Color s3,
			Color bl, Color wh) {
		this(themeName, p1, p2, p3, s1, s2, s3, bl, wh, null);
	}

	public PgsTheme(
			String themeName,
			Color p1, Color p2, Color p3,
			Color s1, Color s2, Color s3,
			Color bl, Color wh, Object[] d) {
		super();
		// Common things
		name = themeName;
		// Primary colos
		primary1 = new ColorUIResource(p1);
		primary2 = new ColorUIResource(p2);
		primary3 = new ColorUIResource(p3);
		// Secondary colors
		secondary1 = new ColorUIResource(s1);
		secondary2 = new ColorUIResource(s2);
		secondary3 = new ColorUIResource(s3);
		// black 'n white
		black = new ColorUIResource(bl);
		white = new ColorUIResource(wh);
		// Custom entries
		defaults = d;
		if (PlafOptions.useBoldMenuFonts()) {
			menuFont = super.getMenuTextFont();
		} else {
			menuFont = new FontUIResource(super.getMenuTextFont().deriveFont(Font.PLAIN));
		}
	}

	public void setBlack(ColorUIResource black) {
		this.black = black;
	}

	public void setDefaults(Object[] defaults) {
		this.defaults = defaults;
	}

	public void setMenuFont(FontUIResource menuFont) {
		this.menuFont = menuFont;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPrimary1(ColorUIResource primary1) {
		this.primary1 = primary1;
	}

	public void setPrimary2(ColorUIResource primary2) {
		this.primary2 = primary2;
	}

	public void setPrimary3(ColorUIResource primary3) {
		this.primary3 = primary3;
	}

	public void setSecondary1(ColorUIResource secondary1) {
		this.secondary1 = secondary1;
	}

	public void setSecondary2(ColorUIResource secondary2) {
		this.secondary2 = secondary2;
	}

	public void setSecondary3(ColorUIResource secondary3) {
		this.secondary3 = secondary3;
	}

	public void setWhite(ColorUIResource white) {
		this.white = white;
	}

	public ColorUIResource getPrimary1() {
		return primary1;
	}

	public ColorUIResource getPrimary2() {
		return primary2;
	}

	public ColorUIResource getPrimary3() {
		return primary3;
	}

	public ColorUIResource getSecondary1() {
		return secondary1;
	}

	public ColorUIResource getSecondary2() {
		return secondary2;
	}

	public ColorUIResource getSecondary3() {
		return secondary3;
	}

	public ColorUIResource getBlack() {
		return black;
	}

	public ColorUIResource getWhite() {
		return white;
	}

	public String getName() {
		return name;
	}

	public FontUIResource getMenuTextFont() {
		return menuFont;
	}

	public void addCustomEntriesToTable(UIDefaults table) {
		if (defaults != null) {
			table.putDefaults(defaults);
		}
	}
}
