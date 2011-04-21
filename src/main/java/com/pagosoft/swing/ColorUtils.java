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
package com.pagosoft.swing;

import java.awt.*;
import java.util.Map;
import java.util.TreeMap;

public class ColorUtils {
	/*
	public static List<Color> gradient(Color a, Color t, int precision) {
		int r = t.getRed()   - a.getRed();
		int g = t.getGreen() - a.getGreen();
		int b = t.getBlue()  - a.getBlue();
		int al = t.getAlpha() - a.getAlpha();
		ArrayList<Color> colors = new ArrayList<Color>();
		for(int i = 1; i < precision+1; i++) {
			System.out.printf("Color:%nRed: %s%nGreen: %s%nBlue: %s%n%n",
				(r/(i/precision))+a.getRed(),
				(g/(i/precision))+a.getGreen(),
				(b/(i/precision))+a.getBlue());
			colors.add(new Color(
				(r/(i/precision))+a.getRed(),
				(g/(i/precision))+a.getGreen(),
				(b/(i/precision))+a.getBlue(),
				(al/(i/precision))+a.getAlpha()));
		}
		return colors;
	}*/

	/**
	 * Can be:
	 * (255, 0, 0)
	 * 255, 0, 0
	 * #FF0000
	 * #F00
	 * red
	 */
	public static Color toColor(String str) {
		switch (str.charAt(0)) {
			case '(':
				int red, green, blue;
				int index;

				red = nextColorInt(str, 1);

				index = str.indexOf(',');
				green = nextColorInt(str, index + 1);

				index = str.indexOf(',', index + 1);
				blue = nextColorInt(str, index + 1);

				return new Color(red, green, blue);
			case '#':
				// Shorthand?
				if (str.length() == 4) {
					return new Color(
							getShorthandValue(str.charAt(1)),
							getShorthandValue(str.charAt(2)),
							getShorthandValue(str.charAt(3))
					);
				}
				else {
					return new Color(Integer.parseInt(str.substring(1), 16));
				}
			default:
				if(Character.isDigit(str.charAt(0))) {
					red = nextColorInt(str, 0);

					index = str.indexOf(',');
					green = nextColorInt(str, index + 1);

					index = str.indexOf(',', index + 1);
					blue = nextColorInt(str, index + 1);

					return new Color(red, green, blue);
				}
				return (Color) colorNamesMap.get(str);
		}
	}

	private static int nextColorInt(String str, int index) {
		// start with adjusting the start index
		while (index < str.length()) {
			char c = str.charAt(index);
			// a digit?
			if ('0' <= c && c <= '9') {
				break;
			}
			else {
				index++;
			}
		}
		// that's only the maximum limit!
		int colorLength = index;
		for (; colorLength < index + 3; colorLength++) {
			char c = str.charAt(colorLength);
			// not a digit?
			if (c < '0' || '9' < c) {
				break;
			}
		}
		return Integer.parseInt(str.substring(index, colorLength));
	}

	private static int getShorthandValue(char c) {
		c = Character.toUpperCase(c);
		if ('A' <= c && c <= 'F') {
			return colorShorthandTable[c - 'A' + 10];
		}
		return colorShorthandTable[c - '0'];
	}

	private static int[] colorShorthandTable = {
			0x00, 0x11, 0x22, 0x33, 0x44, 0x55, 0x66,
			0x77, 0x88, 0x99, 0xAA, 0xBB, 0xCC, 0xDD,
			0xEE, 0xFF
	};

	private static Map colorNamesMap;

	static {
		colorNamesMap = new TreeMap();
		colorNamesMap.put("white", new Color(0xFFFFFF));
		colorNamesMap.put("lightGray", new Color(0xC0C0C0));
		colorNamesMap.put("gray", new Color(0x808080));
		colorNamesMap.put("darkGray", new Color(0x404040));
		colorNamesMap.put("black", new Color(0x000000));
		colorNamesMap.put("red", new Color(0xFF0000));
		colorNamesMap.put("pink", new Color(0xFFAFAF));
		colorNamesMap.put("orange", new Color(0xFFC800));
		colorNamesMap.put("yellow", new Color(0xFFFF00));
		colorNamesMap.put("green", new Color(0x00FF00));
		colorNamesMap.put("magenta", new Color(0xFF00FF));
		colorNamesMap.put("cyan", new Color(0x00FFFF));
		colorNamesMap.put("blue", new Color(0x0000FF));
	}

	public static Color oposite(Color a) {
		return new Color(255 - a.getRed(), 255 - a.getGreen(), 255 - a.getBlue(), a.getAlpha());
	}

	public static Color subtract(Color a, Color b) {
		return new Color(
			Math.max(0, Math.min(255, a.getRed() - b.getRed())),
			Math.max(0, Math.min(255, a.getGreen() - b.getGreen())),
			Math.max(0, Math.min(255, a.getBlue() - b.getBlue())));
	}

	public static String toString(Color c) {
		String colString = Integer.toHexString(c.getRGB() & 0xffffff).toUpperCase();
		return "#000000".substring(0,7 - colString.length()).concat(colString);
	}

	public static Color getTranslucentColor(Color c, int alpha) {
		return new Color(c.getRed(), c.getGreen(), c.getBlue(), alpha);
	}

	public static Color getSimiliarColor(Color color, float factor) {
		return new Color(
				between((int)(color.getRed()*factor), 0, 255),
				between((int)(color.getGreen()*factor), 0, 255),
				between((int)(color.getBlue()*factor), 0, 255),
				color.getAlpha());
	}

	private static int between(int v, int min, int max) {
		return Math.max(min, Math.min(v, max));
	}

	public static Color getColor(Color color, float factor) {
		float[] hsbValues = new float[3];
		Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), hsbValues);
		return Color.getHSBColor(hsbValues[0], hsbValues[1], hsbValues[2] * factor);
	}

	/**
	 * <p>Returns an array of 9 colors which one could use very good together.</p>
	 * <p>The code to calculate the colors is taken from Twyst <http://www.colormixers.com/>.<br />
	 * I've just translated it into Java.</p>
	 *
	 * @author Twyst, Patrick Gotthardt
	 * @param c The base Color (returned as index 0)
	 * @return An Array of colors which are harmonic to the base color.
	 */
	public static Color[] mixColors(Color c) {
		Color[] result = new Color[9];

		result[0] = c;

		double[] hs = RGBtoHSV(c);
		double[] y = new double[3];
		double[] yx = new double[3];
		double[] p = new double[3];
		double[] pr = new double[3];

		// First two
		p[0] = y[0] = hs[0];
		p[1] = y[1] = hs[1];

		if(hs[2] > 70) {
			y[2] = hs[2] - 30;
			p[2] = hs[2] - 15;
		} else {
			y[2] = hs[2] + 30;
			p[2] = hs[2] + 15;
		}

		result[1] = HSVtoRGB(p);
		result[2] = HSVtoRGB(y);

		// Second three
		if(hs[0] >= 0 && hs[0] < 30) {
			pr[0] = yx[0] = y[0] = hs[0] + 20;
			pr[1] = yx[1] = y[1] = hs[1];
			y[2] = hs[2];

			if(hs[2] > 70) {
				yx[2] = hs[2] - 30;
				pr[2] = hs[2] - 15;
			} else {
				yx[2] = hs[2] + 30;
				pr[2] = hs[2] + 15;
			}
		}
		if(hs[0] >= 30 && hs[0] < 60) {
			pr[0] = yx[0] = y[0] = hs[0] + 150;
			y[1] = minMax(hs[1]-30, 0, 100);
			y[2] = minMax(hs[2]-20, 0, 100);

			pr[1] = yx[1] = minMax(hs[1]-70, 0, 100);
			yx[2] = minMax(hs[2]+20, 0, 100);
			pr[2] = hs[2];
		}
		if(hs[0] >= 60 && hs[0] < 180) {
			pr[0] = yx[0] = y[0] = hs[0]-40;
			pr[1] = yx[1] = y[1] = hs[1];

			y[2] = hs[2];

			if(hs[2] > 70) {
				yx[2] = hs[2] - 30;
				pr[2] = hs[2] - 15;
			} else {
				yx[2] = hs[2] + 30;
				pr[2] = hs[2] + 15;
			}
		}
		if(hs[0] >= 180 && hs[0] < 220) {
			pr[0] = yx[0] = hs[0] - 170;
			y[0] = hs[0] - 160;

			pr[1] = yx[1] = y[1] = hs[1];
			y[2] = hs[2];

			if(hs[2] > 70) {
				yx[2] = hs[2] - 30;
				pr[2] = hs[2] - 15;
			} else {
				yx[2] = hs[2] + 30;
				pr[2] = hs[2] + 15;
			}
		}
		if(hs[0] >= 220 && hs[0] < 300) {
			pr[0] = yx[0] = y[0] = hs[0];
			pr[1] = yx[1] = y[1] = minMax(hs[1]-60, 0, 100);
			y[2] = hs[2];

			if(hs[2] > 70) {
				yx[2] = hs[2] - 30;
				pr[2] = hs[2] - 15;
			} else {
				yx[2] = hs[2] + 30;
				pr[2] = hs[2] + 15;
			}
		}
		if(hs[0] >= 300) {
			if(hs[1] > 50) {
				pr[1] = yx[1] = y[1] = hs[1]-40;
			} else {
				pr[1] = yx[1] = y[1] = hs[1]+40;
			}

			pr[0] = yx[0] = y[0] = (hs[0]+20)%360;
			y[2] = hs[2];

			if(hs[2] > 70) {
				yx[2] = hs[2] - 30;
				pr[2] = hs[2] - 15;
			} else {
				yx[2] = hs[2] + 30;
				pr[2] = hs[2] + 15;
			}
		}

		result[3] = HSVtoRGB(y);
		result[4] = HSVtoRGB(pr);
		result[5] = HSVtoRGB(yx);

		// now change y again
		y[0] = y[1] = 0;
		y[2] = 100 - hs[2];
		result[6] = HSVtoRGB(y);
		y[2] = hs[2];
		result[7] = HSVtoRGB(y);

		// now change pr again
		pr[0] = pr[1] = 0;
		pr[2] = hs[2] >= 50 ? 0 : 100;
		result[8] = HSVtoRGB(pr);

		return result;
	}

	private static double minMax(double x, double min, double max) {
		if(x > max) return max;
		if(x < min) return min;
		return x;
	}

	/**
	 * <p>Returns the color object represented by the HSV.</p>
	 * <p>The code is taken from Twyst <http://www.colormixers.com/>.<br />
	 * I've just translated it into Java.</p>
	 *
	 * @author Twyst, Patrick Gotthardt
	 * @param data An double[] with three items (0=h; s=1; 2=v)
	 * @return The Color object based on the HSV values
	 */
	public static Color HSVtoRGB(double[] data) {
		if(data == null || data.length != 3) {
			throw new IllegalArgumentException("data must be an array of 3 items and must not be null!");
		}
		return HSVtoRGB(data[0], data[1], data[2]);
	}

	/**
	 * <p>Returns the color object represented by the HSV.</p>
	 * <p>The code is taken from Twyst <http://www.colormixers.com/>.<br />
	 * I've just translated it into Java.</p>
	 * <p>All values must be between 0 and 255!.</p>
	 *
	 * @author Twyst, Patrick Gotthardt
	 * @param h The "H"-value of the color.
	 * @param s The "S"-value of the color.
	 * @param v The "V"-value of the color.
	 * @return The Color object based on the HSV values
	 */
	public static Color HSVtoRGB(double h, double s, double v) {
		int r = 0,
			g = 0,
			b = 0;

		if(s == 0) {
			r = g = b = (int)Math.round(v * 2.55);
		} else {
			h = h/60;
			s = s/100;
			v = v/100;

			double i = Math.floor(h);
			double f = h-i;

			int p=(int)Math.round(255*(v*(1-s)));
			int q=(int)Math.round(255*(v*(1-s*f)));
			int t=(int)Math.round(255*(v*(1-s*(1-f))));
			int v2 = (int)Math.round(255*v);

			switch((int)i) {
				case 0:
					r = v2;
					g = t;
					b = p;
					break;
				case 1:
					r = q;
					g = v2;
					b = p;
					break;
				case 2:
					r = p;
					g = v2;
					b = t;
					break;
				case 3:
					r = p;
					g = q;
					b = v2;
					break;
				case 4:
					r = t;
					g = p;
					b = v2;
					break;
				default:
					r = v2;
					g = p;
					b = q;
					break;
			}
		}

		return new Color(r, g, b);
	}

	/**
	 * <p>Returns the HSV representation of the RGB-Color. All values are between 0 and 255.</p>
	 * <p>The code is taken from Twyst <http://www.colormixers.com/>.<br />
	 * I've just translated it into Java.</p>
	 *
	 * @author Twyst, Patrick Gotthardt
	 * @param c The color to be translated to HSV.
	 * @return An double[] with three items (0=h; s=1; 2=v)
	 */
	public static double[] RGBtoHSV(Color c) {
		double[] hsv = new double[3];
		int r = c.getRed(),
			g = c.getGreen(),
			b = c.getBlue();
		int min = Math.min(Math.min(r, g), b); // get the smallest of all
		int max = Math.max(Math.max(r, g), b); // get the biggest of all
		int delta = max - min;

		double h = 0, s;
		double v = 100*max/255;
		// Is it a gray color?
		if(delta == 0) {
			h = s = 0;
		} else {
			s = (100 * delta) / max;

			double del_r = (100*(((max-r)/6)+(max/2)))/delta;
			double del_g = (100*(((max-g)/6)+(max/2)))/delta;
			double del_b = (100*(((max-b)/6)+(max/2)))/delta;

			if(r == max) {
				h = 60 * (g-b) / delta;
			} else if(g == max) {
				h = 120+60 * (b-r) / delta;
			} else if(b == max) {
				h = 240+60*(r-g)/delta;
			}
			if(h < 0) {
				h = h + 360;
			}
		}
		hsv[0] = h;
		hsv[1] = s;
		hsv[2] = v;

		return hsv;
	}
}
