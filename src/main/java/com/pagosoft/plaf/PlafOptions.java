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
import java.util.HashMap;

public class PlafOptions {
	private static boolean vistaStyle;

	private PlafOptions() {
	}

	// --------------------------------------------------------
	// Global things
	public static void setAsLookAndFeel() {
		try {
			Class.forName("com.jidesoft.utils.Lm");
			// class exists => enable FastGradient
			enableJideFastGradient();
		} catch(ClassNotFoundException e) {
			// no real error - just a failed hope => ignore it
		}
		PgsLookAndFeel.setAsLookAndFeel();
	}

	public static void setCurrentTheme(MetalTheme t) {
		PgsLookAndFeel.setCurrentTheme(t);
	}

	public static void updateAllUIs() {
		PgsLookAndFeel.updateAllUIs();
	}

	private static Dimension defIconSize = new Dimension(10, 10);

	public static void setDefaultMenuItemIconSize(Dimension dim) {
		defIconSize = dim;
	}

	public static Dimension getDefaultMenuItemIconSize() {
		return defIconSize;
	}


	// --------------------------------------------------------
	// Narrows
	private static boolean useExtraMargin = true;

	public static boolean useExtraMargin() {
		return useExtraMargin;
	}

	public static void useExtraMargin(boolean use) {
		useExtraMargin = use;
	}

	// --------------------------------------------------------
	// Use bold fonts?
	private static boolean useBoldFonts = false;
	private static boolean useBoldMenuFonts = false;

	public static boolean useBoldFonts() {
		return useBoldFonts;
	}

	public static void useBoldFonts(boolean use) {
		useBoldFonts = use;
		UIManager.put("swing.boldMetal", Boolean.valueOf(use));
	}

	public static boolean useBoldMenuFonts() {
		return useBoldMenuFonts;
	}

	public static void useBoldMenuFonts(boolean use) {
		useBoldMenuFonts = use;
	}

	// --------------------------------------------------------
	// Component options
	private static boolean useShadowBorder = false;

	public static void useShadowBorder(boolean use) {
		useShadowBorder = use;
	}

	public static boolean isShadowBorderUsed() {
		return useShadowBorder;
	}

	private static boolean useToolBarIcon = true;

	public static void useToolBarIcon(boolean use) {
		useToolBarIcon = use;
	}

	public static boolean isToolBarIconUsed() {
		return useToolBarIcon;
	}

	private static boolean useDisabledIcon = true;

	public static void useDisabledIcon(boolean use) {
		useDisabledIcon = use;
	}

	public static boolean isDisabledIconUsed() {
		return useDisabledIcon;
	}

	private static boolean aaText = false;

	public static void setAntialiasingEnabled(boolean use) {
		aaText = use;
	}

	public static boolean isAntialiasingEnabled() {
		return aaText;
	}

	private static boolean isClearBorderEnabled = false;

	public static void setClearBorderEnabled(boolean enabled) {
		PgsUtils.regenerateBorderStroke();
		isClearBorderEnabled = enabled;
	}

	public static boolean isClearBorderEnabled() {
		return isClearBorderEnabled;
	}

	private static boolean fixHtmlDisplay = false;

	public static boolean isHtmlDisplayFixEnabled() {
		return fixHtmlDisplay;
	}

	public static void setFixHtmlDisplayEnabled(boolean aFixHtmlDisplayEnabled) {
		fixHtmlDisplay = aFixHtmlDisplayEnabled;
	}

	private static boolean wheelTabbedPaneEnabled = false;

	public static boolean isWheelTabbedPaneEnabled() {
		return wheelTabbedPaneEnabled;
	}

	public static void setWheelTabbedPaneEnabled(boolean enabled) {
		wheelTabbedPaneEnabled = enabled;
	}

	private static boolean tabbedPaneRightClickSelectionEnabled = false;

	public static boolean isTabbedPaneRightClickSelectionEnabled() {
		return tabbedPaneRightClickSelectionEnabled;
	}

	public static void setTabbedPaneRightClickSelectionEnabled(boolean enabled) {
		tabbedPaneRightClickSelectionEnabled = enabled;
	}

	private static boolean tabReorderingEnabled = false;

	public static boolean isTabReorderingEnabled() {
		return tabReorderingEnabled;
	}

	public static void setTabReorderingEnabled(boolean enabled) {
		tabReorderingEnabled = enabled;
	}

	private static boolean paintRolloverButtonBorder = true;

	public static boolean isPaintRolloverButtonBorder() {
		return paintRolloverButtonBorder;
	}

	public static void setPaintRolloverButtonBorder(boolean paintRolloverButtonBorder) {
		PlafOptions.paintRolloverButtonBorder = paintRolloverButtonBorder;
	}

	private static boolean officeScrollBarEnabled = false;

	public static boolean isOfficeScrollBarEnabled() {
		return officeScrollBarEnabled;
	}

	public static void setOfficeScrollBarEnabled(boolean officeScrollBarEnabled) {
		PlafOptions.officeScrollBarEnabled = officeScrollBarEnabled;
	}

	// --------------------------------------------------------
	// Style
	public static final int FLAT_STYLE = 0;
	public static final int GRADIENT_STYLE = 1;
	private static int defStyle = GRADIENT_STYLE;

	public static final String MENU_ITEM = "MenuItem";
	public static final String TOOLBAR = "ToolBar";
	public static final String BUTTON = "Button";
	public static final String TOOLBARBUTTON = "ToolBarButton";
	public static final String MENUBAR = "MenuBar";
	public static final String MENUBARMENU = "MenuBarMenu";

	private static HashMap styles = new HashMap();

	static {
		// Initialize some styles per default
		setStyle(MENU_ITEM, FLAT_STYLE);
		setStyle(MENUBAR, FLAT_STYLE);
		setStyle(MENUBARMENU, FLAT_STYLE);

		setStyle(TOOLBAR, GRADIENT_STYLE);
		setStyle(TOOLBARBUTTON, GRADIENT_STYLE);
	}

	public static void setDefaultStyle(int s) {
		if (s == FLAT_STYLE || s == GRADIENT_STYLE) {
			defStyle = s;
		} else {
			throw new IllegalArgumentException("The Style must be flat or gradient");
		}
	}

	public static void setStyle(String key, int s) {
		if (s == FLAT_STYLE || s == GRADIENT_STYLE) {
			styles.put(key, new Integer(s));
		} else {
			throw new IllegalArgumentException("The Style must be flat or gradient");
		}
	}

	public static int getStyle(String key) {
		if (styles.containsKey(key)) {
			return ((Integer) styles.get(key)).intValue();
		}
		return defStyle;
	}

	public static Boolean isFlat(String key) {
		return getStyle(key) == FLAT_STYLE ? Boolean.TRUE : Boolean.FALSE;
	}

	// Jide-stuff
	private static boolean fastGradient = false;

	@Deprecated
	public static void enableJideFastGradient() {
	}

	@Deprecated
	public static boolean isJideFastGradientEnabled() {
		return fastGradient;
	}

	public static boolean isVistaStyle() {
		return vistaStyle;
	}

	public static void setVistaStyle(boolean vistaStyle) {
		PlafOptions.vistaStyle = vistaStyle;
	}
}
