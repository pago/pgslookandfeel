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
package com.pagosoft.plaf.themes;

import com.pagosoft.plaf.PgsTheme;
import com.pagosoft.plaf.PgsUtils;
import com.pagosoft.plaf.PlafOptions;
import com.pagosoft.swing.ColorUtils;

import javax.swing.*;
import javax.swing.plaf.ColorUIResource;
import java.awt.*;

public class SilverTheme extends PgsTheme {
	public SilverTheme() {
		super("SilverTheme");
		PlafOptions.setOfficeScrollBarEnabled(true);
		setPrimary1(new ColorUIResource(0x899196));
		setPrimary2(new ColorUIResource(0xACB5BD));
		setPrimary3(new ColorUIResource(0xCEDAE3));
		setSecondary1(new ColorUIResource(0x969189));
		setSecondary2(new ColorUIResource(0xBDB6AC));
		setSecondary3(new ColorUIResource(0xE3E3E3));

		ColorUIResource menuBg = new ColorUIResource(ColorUtils.getSimiliarColor(getSecondary3(), 1.1f));
		setDefaults(new Object[]{
			"PopupMenu.background", menuBg,
			"Menu.background", menuBg,
			"MenuItem.background", menuBg,
			"RadioButtonMenuItem.background", menuBg,
			"CheckBoxMenuItem.background", menuBg,

			"RadioButtonMenuItem.selectionBackground", new ColorUIResource(0xFAD68A),
			"CheckBoxMenuItem.selectionBackground", new ColorUIResource(0xFAD68A),

			"MenuBarMenu.selectedBackground", getPrimary2(),
			"MenuBarMenu.rolloverBorderColor", getControlShadow(),
			"MenuBarMenu.selectedBorderColor", getPrimary2(),
			"glow", new ColorUIResource(0xe7eb2a),

			"Menu.gradientStart", getPrimary3(),
			"Menu.gradientEnd", getPrimary2(),
			"Menu.gradientMiddle", getPrimary3(),
			"Menu.isFlat", Boolean.FALSE,
			"MenuBar.border", BorderFactory.createEmptyBorder(0,0,0,0),

			"MenuItem.gradientStart", getPrimary3(),
			"MenuItem.gradientEnd", getPrimary2(),
			"MenuItem.gradientMiddle", getPrimary3(),
			"MenuItem.isFlat", Boolean.FALSE,

			"CheckBoxMenuItem.gradientStart", getPrimary3(),
			"CheckBoxMenuItem.gradientEnd", getPrimary2(),
			"CheckBoxMenuItem.gradientMiddle", getPrimary3(),
			"CheckBoxMenuItem.isFlat", Boolean.FALSE,

			"RadioButtonMenuItem.gradientStart", getPrimary3(),
			"RadioButtonMenuItem.gradientEnd", getPrimary2(),
			"RadioButtonMenuItem.gradientMiddle", getPrimary3(),
			"RadioButtonMenuItem.isFlat", Boolean.FALSE,

			"ToolBarButton.selectedGradientStart", getPrimaryControl(),
			"ToolBarButton.selectedGradientEnd", getPrimaryControlDarkShadow()
		});

		PgsUtils.rolloverBorderStroke = new BasicStroke(1.8f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
	}
}