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

import javax.swing.plaf.ColorUIResource;
import java.awt.SystemColor;

public class NativeColorTheme extends PgsTheme {
	public NativeColorTheme() {
		super("Native Colors");

		setPrimary1(new ColorUIResource(SystemColor.activeCaption));
		setPrimary2(new ColorUIResource(SystemColor.textHighlight));
		setPrimary3(new ColorUIResource(SystemColor.inactiveCaption));
		setSecondary1(new ColorUIResource(SystemColor.controlDkShadow));
		setSecondary2(new ColorUIResource(SystemColor.controlShadow));
		setSecondary3(new ColorUIResource(SystemColor.control));
		setWhite(new ColorUIResource(SystemColor.text));
		setBlack(new ColorUIResource(SystemColor.textText));
	}
}
