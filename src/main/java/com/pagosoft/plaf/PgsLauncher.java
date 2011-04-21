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

import java.awt.*;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * Main method wrapper for configuring the PgsLookAndFeel.
 *
 * @author <a href="mailto:jesse@odell.ca">Jesse Wilson</a>
 */
public class PgsLauncher {

	/**
	 * default theme colors
	 */
	private Color primary;
	private Color secondary;
	private Color text = Color.black;

	/**
	 * Launch using the specified arguments.
	 */
	private void launch(List arguments) throws Exception {
		// read the PGS arguments
		for (Iterator i = arguments.iterator(); i.hasNext();) {
			String argument = (String) i.next();
			if (argument.indexOf("=") != -1) {
				applyArgument(argument);
				i.remove();
			} else {
				break;
			}
		}

		// configure the look & feel
		if (primary != null && secondary != null) {
			PgsTheme customTheme = ThemeFactory.createTheme("custom", primary, secondary, text);
			PlafOptions.setCurrentTheme(customTheme);
		}
		PlafOptions.setAsLookAndFeel();
		PlafOptions.updateAllUIs();

		// handle the rest of the arguments
		String className = (String) arguments.remove(0);
		String[] mainMethodArgs = (String[]) arguments.toArray(new String[arguments.size()]);

		// launch our class
		Class mainClass = Class.forName(className);
		Method mainMethod = mainClass.getDeclaredMethod("main", new Class[]{String[].class});
		mainMethod.invoke(null, new Object[]{mainMethodArgs});
	}

	/**
	 * Applies the specified argument to this launcher.
	 */
	private void applyArgument(String argument) {
		String[] keyAndValue = argument.split("\\=", 2);
		String key = keyAndValue[0];
		String value = keyAndValue[1];

		if (key.equalsIgnoreCase("primary")) {
			primary = parseColor(value);
		} else if (key.equalsIgnoreCase("secondary")) {
			secondary = parseColor(value);
		} else if (key.equalsIgnoreCase("text")) {
			text = parseColor(value);
		} else {
			System.out.println("Unhandled key: \"" + key + "\"");
		}
	}

	/**
	 * Parses a color from the specified String.
	 */
	private static Color parseColor(String value) {
		if (value.startsWith("#")) {
			value = value.substring(1, value.length());
		} else if (value.startsWith("0x")) {
			value = value.substring(2, value.length());
		}

		return new Color(Integer.parseInt(value, 16));
	}

	/**
	 * When started via a main method, this creates a standalone issues browser.
	 */
	public static void main(String[] args) throws Exception {
		if (args.length < 1) {
			System.out.println(
					"Usage: PGSLauncher [primary=<color>] [secondary=<color>] [text=<color>] <mainclass> classargs...");
			System.out.println("		  <color> is in hex format, such as FF0000 for red");
			System.out.println("");
			System.out.println("Examples: PGSLauncher com.mycompany.UserInterface");
			//System.out.println("		  PGSLauncher primary=0000FF text=FF0000 com.mycompany.UserInterface");
			System.out.println("		  PGSLauncher primary=FF0000 secondary=0000FF com.mycompany.UserInterface");
			//System.out.println("		  PGSLauncher primary=FF0000 com.mycompany.UserInterface arg1 arg2");
			return;
		}

		// convert the arguments to a List for easier processing
		List argumentsList = new ArrayList();
		argumentsList.addAll(Arrays.asList(args));

		// launch using these arguments
		PgsLauncher launcher = new PgsLauncher();
		launcher.launch(argumentsList);
	}
}
