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
import com.pagosoft.plaf.PlafOptions;

import javax.swing.plaf.*;
import java.awt.*;

/**
 * @author Patrick Gotthardt
 */
public class VistaTheme extends PgsTheme {
	public VistaTheme() {
		super("Vista");

		setSecondary3(new ColorUIResource(0xE7E7E7));
		setSecondary2(new ColorUIResource(0xFDFDFD));
		setSecondary1(new ColorUIResource(0x8E8F8F));

		setPrimary1(new ColorUIResource(0x3c7fb1));
		setPrimary2(new ColorUIResource(0xaadcf8));
		setPrimary3(new ColorUIResource(0xdff2fc));

		setBlack(new ColorUIResource(Color.BLACK));
		setWhite(new ColorUIResource(Color.WHITE));

		PlafOptions.setOfficeScrollBarEnabled(true);
		PlafOptions.setVistaStyle(true);
		PlafOptions.useBoldFonts(false);

		setDefaults(new Object[]{
			"MenuBar.isFlat", Boolean.FALSE,
			"MenuBar.gradientStart", new ColorUIResource(0x496791),
			"MenuBar.gradientMiddle", new ColorUIResource(0x3f5e89),
			"MenuBar.gradientEnd", new ColorUIResource(0x3a5a86),

			"MenuBarMenu.isFlat", Boolean.FALSE,
			"MenuBarMenu.foreground", getWhite(),
			"MenuBarMenu.rolloverBackground.gradientStart", new ColorUIResource(0x6987B1),
			"MenuBarMenu.rolloverBackground.gradientMiddle", new ColorUIResource(0x3f5e89),
			"MenuBarMenu.rolloverBackground.gradientEnd", new ColorUIResource(0x3a5a86),
			"MenuBarMenu.selectedBackground.gradientStart", new ColorUIResource(0x6987B1),
			"MenuBarMenu.selectedBackground.gradientMiddle", new ColorUIResource(0x3f5e89),
			"MenuBarMenu.selectedBackground.gradientEnd", new ColorUIResource(0x3a5a86),
			"MenuBarMenu.rolloverBorderColor", getPrimary3(),
			"MenuBarMenu.selectedBorderColor", getPrimary3(),

			"Menu.gradientStart", getPrimary3(),
			"Menu.gradientEnd", getPrimary2(),
			"Menu.gradientMiddle", getPrimary3(),
			"Menu.isFlat", Boolean.FALSE,

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

			"Button.rolloverGradientStart", getPrimary3(),
			"Button.rolloverGradientEnd", getPrimary2(),
			"Button.selectedGradientStart", getPrimary3(),
			"Button.selectedGradientEnd", getPrimary1(),
			"Button.rolloverVistaStyle", Boolean.TRUE,
			"glow", getPrimary1(),

			"ToggleButton.rolloverGradientStart", getPrimary3(),
			"ToggleButton.rolloverGradientEnd", getPrimary2(),
			"ToggleButton.selectedGradientStart", getPrimary3(),
			"ToggleButton.selectedGradientEnd", getPrimary1(),
		});
	}
}
