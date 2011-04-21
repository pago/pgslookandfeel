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

import com.pagosoft.OS;
import com.pagosoft.plaf.themes.ElegantGrayTheme;
import com.pagosoft.plaf.themes.SilverTheme;
import com.pagosoft.plaf.themes.VistaTheme;
import com.pagosoft.plaf.themes.NativeColorTheme;
import com.pagosoft.swing.ColorUtils;

import java.awt.Color;
import java.awt.Insets;
import java.io.*;
import java.util.*;
import java.util.regex.*;

public class ThemeFactory {
	public static final PgsTheme GRAY = createTheme("Gray", new Color(0x7997D1), new Color(0xABABAB), Color.black);
	public static final PgsTheme YELLOW = createTheme("Yellow", new Color(0xCCAA53), new Color(0xABABAB), Color.black);
	public static final PgsTheme RUBY = createTheme("Ruby", new Color(244, 10, 66), new Color(0xABABAB), Color.black);
	public static final PgsTheme GOLD = createTheme("Gold", new Color(0xFFDB29));
	public static final PgsTheme WIN = new PgsTheme(
			"Win",
			new Color(0x6080AC),  // p1
			new Color(0xFFCF31),  // p2
			new Color(0xF9E089),  // p3

			new Color(0x666554),
			new Color(0xDCDBCB),
			new Color(0xF1F0E3),
			Color.black,
			Color.white,
			getWinCustomEntries()
	);
	public static final PgsTheme GREEN = createTheme("Green", new Color(0x986847), new Color(0xEFEBE7), Color.BLACK);

	private static Object[] getWinCustomEntries() {
		Color s2 = new Color(0xDCDBCB);
		Color s3 = new Color(0xF1F0E3);
		Color p2 = new Color(0xF9E089);
		Color p3 = new Color(0xFFCF31);
		return new Object[]{
				"Button.rolloverGradientStart", Color.white,
				"Button.rolloverGradientEnd", s2,
				"Button.selectedGradientStart", p3,
				"Button.selectedGradientEnd", p2,

				"ToggleButton.rolloverGradientStart", Color.white,
				"ToggleButton.rolloverGradientEnd", s2,
				"ToggleButton.selectedGradientStart", p3,
				"ToggleButton.selectedGradientEnd", p2,

				"ToolBar.gradientStart", s3,
				"ToolBar.gradientEnd", s2,

				"ToolBarButton.rolloverGradientStart", p3,
				"ToolBarButton.rolloverGradientEnd", p2,
				"ToolBarButton.selectedGradientStart", p2,
				"ToolBarButton.selectedGradientEnd", p3,

				"ScrollBar.thumb", p3,
				"ScrollBar.thumbHighlight", p3.brighter(),

				"ProgressBar.gradientStart", p3,
				"ProgressBar.gradientEnd", p3.brighter()
		};
	}

	public static PgsTheme createTheme(Color primary) {
		return ThemeFactory.createTheme("PgsTheme", primary);
	}

	public static PgsTheme createTheme(String name, Color primary) {
		Color[] cs = ColorUtils.mixColors(primary);
		return new PgsTheme(
				name,
				cs[2], cs[1], cs[0], // pri
				cs[5], cs[4], cs[3], // sec
				cs[8], cs[7]); // black 'n white
	}

	public static PgsTheme createTheme(Color primary, Color secondary, Color text) {
		return ThemeFactory.createTheme("Custom PgsTheme", primary, secondary, text);
	}

	public static PgsTheme getDefaultTheme() {
		// I've been focusing development towards this theme so it should be in the best
		// state of all existing themes.
		return new SilverTheme();
	}

	public static PgsTheme createTheme(String name, Color primary, Color secondary, Color text) {
		return new PgsTheme(
				name,
				primary.darker(),	  // p1
				primary,			  // p2
				primary.brighter(),   // p3

				secondary.darker(),   // s1
				secondary,			  // s2
				secondary.brighter(), // s3
				text,
				ColorUtils.oposite(text)
		);
	}

	/**
	 * <p>Supported formats are:<br />
	 * {@code colors: [primary], [secondary], [text]}<br />
	 * {@code theme: [gray|ruby|win|yellow|gold]}<br />
	 * {@code res: [theme_source_file]}</p>
	 */
	public static PgsTheme getTheme(String theme) {
		if (theme == null) {
			throw new IllegalArgumentException("theme must not be null!");
		}
		if (theme.startsWith("theme:")) {
			String name = theme.substring(5).trim();
			return ThemeFactory.getThemeByName(name);
		} else if (theme.startsWith("colors:")) {
			String colors = theme.substring(6).trim();
			return ThemeFactory.getThemeByColors(colors);
		} else if (theme.startsWith("res:")) {
			String res = theme.substring(4).trim();
			File f = new File(res);
			if (f.exists()) {
				Properties props = new Properties();
				FileInputStream fis = null;
				try {
					fis = new FileInputStream(f);
					props.load(fis);
					fis.close();
					return ThemeFactory.createTheme(props);
				} catch (Exception ex) {
					ex.printStackTrace();
					return getDefaultTheme();
				} finally {
					if(fis != null) {
						try {
							fis.close();
						} catch(IOException e) {
							e.printStackTrace();
						}
					}
				}
			} else {
				return getDefaultTheme();
			}
		} else if(theme.length() > 0) {
			return ThemeFactory.getThemeByName(theme);
		}
		return ThemeFactory.getDefaultTheme();
	}

	public static PgsTheme getThemeByName(String theme) {
		theme = theme.toLowerCase();
		if ("ruby".equals(theme)) {
			return ThemeFactory.RUBY;
		} else if ("win".equals(theme)) {
			return ThemeFactory.WIN;
		} else if ("yellow".equals(theme)) {
			return ThemeFactory.YELLOW;
		} else if ("gold".equals(theme)) {
			return ThemeFactory.GOLD;
		} else if("gray".equals(theme)) {
			return ThemeFactory.GRAY;
		} else if("green".equals(theme)) {
			return ThemeFactory.GREEN;
		} else if("elegantgray".equals(theme)) {
			return ElegantGrayTheme.getInstance();
		} else if("silver".equals(theme)) {
			return new SilverTheme();
		} else if("vista".equals(theme)) {
			return new VistaTheme();
		} else {
			try {
				Class cls = Class.forName(theme);
				if(PgsTheme.class.isAssignableFrom(cls)) {
					return (PgsTheme) cls.newInstance();
				}
			} catch(ClassNotFoundException e) {
				// ignore
			} catch(IllegalAccessException e) {
				// ignore
			} catch(InstantiationException e) {
				// ignore
			}
		}
		return ThemeFactory.getDefaultTheme();
	}

	public static PgsTheme getThemeByColors(String colors) {
		String[] color = colors.split(",?\\s*");
		if (color.length != 2) {
			throw new IllegalArgumentException(
					"Theme has the wrong format: '"
							+ colors + "', but should be '[primary], [secondary], [text]'");
		}
		return ThemeFactory.createTheme(
				Color.decode(color[0]),
				Color.decode(color[1]),
				Color.decode(color[2])
		);
	}

	// This Pattern splits on any whitespace and ","
	private static final Pattern SPLIT = Pattern.compile("\\s*,?\\s*");

	public static PgsTheme createTheme(Map map) {
		ArrayList data = new ArrayList();
		Iterator i = map.keySet().iterator();
		String value;
		String key;
		String name = "Custom PgsTheme";
		Color p1 = null, p2 = null, p3 = null,
				s1 = null, s2 = null, s3 = null,
				bl = Color.black, wh = Color.white;
		while (i.hasNext()) {
			key = i.next().toString();
			value = map.get(key).toString();

			if (key.equals("name")) {
				name = value;
			} else if (key.equals("primary1")) {
				p1 = Color.decode(value);
			} else if (key.equals("primary2")) {
				p2 = Color.decode(value);
			} else if (key.equals("primary3")) {
				p3 = Color.decode(value);
			} else if (key.equals("secondary1")) {
				s1 = Color.decode(value);
			} else if (key.equals("secondary2")) {
				s2 = Color.decode(value);
			} else if (key.equals("secondary3")) {
				s3 = Color.decode(value);
			} else if (key.equals("black")) {
				bl = Color.decode(value);
			} else if (key.equals("white")) {
				wh = Color.decode(value);
			} else if(!key.startsWith("pgs.") && !key.startsWith("swing.")) {
				data.add(key);
				if (value.startsWith("#")) {
					data.add(Color.decode(value));
				} else if (value.equals("true")) {
					data.add(Boolean.TRUE);
				} else if (value.equals("false")) {
					data.add(Boolean.FALSE);
				} else {
					String val[] = SPLIT.split(value);
					switch (val.length) {
						case 1:
							int in = Integer.parseInt(val[0]);
							data.add(new Insets(in, in, in, in));
							break;
						case 2:
							int in1 = Integer.parseInt(val[0]);
							int in2 = Integer.parseInt(val[1]);
							data.add(new Insets(in1, in2, in1, in2));
							break;
						case 4:
							data.add(
									new Insets(
											Integer.parseInt(val[0]),
											Integer.parseInt(val[1]),
											Integer.parseInt(val[2]),
											Integer.parseInt(val[3])));
							break;
					}
				}
			}
		}
		if(p1 != null && p2 != null && p3 != null && s1 != null && s2 != null && s3 != null) {
			return new PgsTheme(name, p1, p2, p3, s1, s2, s3, bl, wh, data.toArray());
		}
		return ThemeFactory.getDefaultTheme();
	}
}
