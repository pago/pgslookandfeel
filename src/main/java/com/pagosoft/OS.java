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
package com.pagosoft;

import java.io.File;

public class OS {
	private static boolean isMacOsX = false;
	private static boolean isWindows = false;
	private static boolean isWindowsXP = false;
	private static boolean isUnix = false;
  
	private static boolean isJava14 = false;
	private static boolean isJava15 = false;

	static {
		// Current Java version:
		String javaVersion = System.getProperty("java.version");
		isJava14 = (javaVersion.compareTo("1.4") >= 0);
		isJava15 = (javaVersion.compareTo("1.5") >= 0);
		
		/*if(System.getProperty("mrj.version") != null) {
			isMacOsX = true;
		} else {*/
			String os = System.getProperty("os.name").toLowerCase();
			// http://developer.apple.com/technotes/tn2002/tn2110.html#PARTONE
			if(os.startsWith("mac os x")) {
				isMacOsX = true;
			} else {
				isWindows = os.indexOf("windows") != -1;
				if(isWindows) {
					isWindowsXP = os.indexOf("windows xp") != -1;
				} else {
					isUnix = File.separatorChar == '/';
				}
			}
		//}
	}
	
	public static boolean isWindows() {
		return isWindows;
	}
	
	public static boolean isWindowsXP() {
		return isWindowsXP;
	}
	
	public static boolean isMacOsX() {
		return isMacOsX;
	}
	
	public static boolean isUnix() {
		return isUnix;
	}
	
	public static boolean isJava15() {
		return isJava15;
	}
	
	public static boolean isJava14() {
		return isJava14;
	}
}
