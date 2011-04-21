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

import com.pagosoft.swing.ColorUtils;

import javax.swing.*;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.InsetsUIResource;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.plaf.metal.MetalTheme;
import java.awt.Color;
import java.awt.Frame;
import java.awt.Insets;
import java.io.*;
import java.util.*;
import java.util.logging.*;

public class PgsLookAndFeel extends MetalLookAndFeel {
	private static MetalTheme currentTheme;

	private static Logger logger = Logger.getLogger("PgsLookAndFeel");

	static {
		loadUserSettings();
	}

	protected static void loadUserSettings() {
		InputStream in = PgsLookAndFeel.class.getResourceAsStream("/pgs.properties");

		if (in != null) {
			try {
				logger.finest("Found a pgs.properties! Going to setup from pgs.properties!");
				Properties props = new Properties();
				props.load(in);

				PlafOptions.useShadowBorder(getPropertyBoolValue(props, "pgs.shadowBorder", "false"));
				PlafOptions.useBoldFonts(getPropertyBoolValue(props, "swing.boldMetal", "false"));
				PlafOptions.useBoldMenuFonts(getPropertyBoolValue(props, "pgs.boldMenuFonts", "false"));
				PlafOptions.useExtraMargin(getPropertyBoolValue(props, "pgs.extraMargin", "true"));
				PlafOptions.useDisabledIcon(getPropertyBoolValue(props, "pgs.useDisabledIcon", "true"));
				PlafOptions.useToolBarIcon(getPropertyBoolValue(props, "pgs.useToolBarIcon", "true"));
				PlafOptions.setAntialiasingEnabled(getPropertyBoolValue(props, "pgs.aaEnabled", "false"));
				PlafOptions.setClearBorderEnabled(getPropertyBoolValue(props, "pgs.clearBorderEnabled", "false"));
				PlafOptions.setFixHtmlDisplayEnabled(getPropertyBoolValue(props, "pgs.fixHtmlDisplayEnabled", "true"));
				PlafOptions.setWheelTabbedPaneEnabled(getPropertyBoolValue(props, "pgs.wheelTabEnabled", "true"));
				PlafOptions.setTabbedPaneRightClickSelectionEnabled(getPropertyBoolValue(props, "pgs.tabbedPaneRightClickSelectionEnabled", "false"));
				PlafOptions.setTabReorderingEnabled(getPropertyBoolValue(props, "pgs.tabReorderingEnabled", "false"));
				PlafOptions.setPaintRolloverButtonBorder(getPropertyBoolValue(props, "pgs.paintRolloverButtonBorder", "true"));
				PlafOptions.setOfficeScrollBarEnabled(getPropertyBoolValue(props, "pgs.officeScrollBarEnabled", "false"));
				PlafOptions.setVistaStyle(getPropertyBoolValue(props, "pgs.vistaStyle", "false"));

				PlafOptions.setStyle(PlafOptions.MENUBAR, getPropertyStyleValue(props, "pgs.style.menuBar", "flat"));
				PlafOptions.setStyle(PlafOptions.MENU_ITEM, getPropertyStyleValue(props, "pgs.style.menuitem", "flat"));
				PlafOptions.setStyle(
						PlafOptions.MENUBARMENU, getPropertyStyleValue(props, "pgs.style.menuBarMenu", "flat"));
				PlafOptions
						.setStyle(PlafOptions.TOOLBAR, getPropertyStyleValue(props, "pgs.style.toolBar", "gradient"));
				PlafOptions.setStyle(
						PlafOptions.TOOLBARBUTTON, getPropertyStyleValue(props, "pgs.style.toolBarButton", "gradient"));
				PlafOptions.setStyle(PlafOptions.BUTTON, getPropertyStyleValue(props, "pgs.style.button", "gradient"));

				PgsTheme theme = null;

				String themeProperty = props.getProperty("theme");
				if(themeProperty != null) {
					theme = ThemeFactory.getTheme(themeProperty);
				} else {
					theme = ThemeFactory.createTheme(props);
				}

				if (theme != null) {
					setCurrentTheme(theme);
				} else {
					logger.warning("Could not create theme from pgs.properties");
				}
				logger.finest("pgs.properties have been loaded.");
			} catch (IOException ex) {
				logger.warning("Unexpected exception happened while loading properties: " + ex.toString());
			} finally {
				try {
					in.close();
				} catch(IOException e) {
					logger.warning("Unexpected exception happened while closing the stream: "+e.toString());
				}
			}
		}
	}

	private static boolean getPropertyBoolValue(Properties props, String key, String def) {
		return "true".equals(props.getProperty(key, def).trim().toLowerCase());
	}

	private static int getPropertyStyleValue(Properties props, String key, String def) {
		return "flat".equals(props.getProperty(key, def).trim().toLowerCase()) ? PlafOptions.FLAT_STYLE
				: PlafOptions.GRADIENT_STYLE;
	}

	public PgsLookAndFeel() {
		super();
		UIManager.put("swing.boldMetal", PlafOptions.useBoldFonts() ? Boolean.TRUE : Boolean.FALSE);
	}

	public void initialize() {
		super.initialize();

		MetalLookAndFeel.setCurrentTheme(getCurrentTheme());
	}

	public void uninitialize() {
		super.uninitialize();
	}

	public static void setAsLookAndFeel() {
		try {
			UIManager.setLookAndFeel("com.pagosoft.plaf.PgsLookAndFeel");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void updateAllUIs() {
		Frame[] frames = Frame.getFrames();
		for (int i = 0; i < frames.length; i++) {
			if (frames[i] instanceof JFrame) {
				SwingUtilities.updateComponentTreeUI(frames[i]);
			}
		}
	}

	public static void setCurrentTheme(MetalTheme theme) {
		if (theme == null) {
			throw new IllegalArgumentException("theme must not be null!");
		}
		currentTheme = theme;
		MetalLookAndFeel.setCurrentTheme(theme);
	}

	public static MetalTheme getCurrentTheme() {
		if (currentTheme == null) {
			String theme = null;
			try {
				theme = System.getProperty("pgs.theme");
			} catch (Exception e) {
				logger.warning("Could not read system property 'pgs.theme'");
			}
			if (theme == null) {
				currentTheme = ThemeFactory.getDefaultTheme();
			} else {
				currentTheme = ThemeFactory.getTheme(theme);
			}
		}
		return currentTheme;
	}

	public String getID() {
		return "com.pagosoft.plaf.PgsLookAndFeel";
	}

	public String getName() {
		return "PgsLookAndFeel";
	}

	public String getDescription() {
		return "Much like the original MetalLookAndFeel, but with gradients on menus, buttons, etc.";
	}

	public boolean isNativeLookAndFeel() {
		return false;
	}

	public boolean isSupportedLookAndFeel() {
		return true;
	}

	protected void initClassDefaults(UIDefaults table) {
		super.initClassDefaults(table);
		String prefix = "com.pagosoft.plaf.Pgs";
		Object[] def = {
				"ButtonUI", prefix + "ButtonUI",
				"ToggleButtonUI", prefix + "ToggleButtonUI",
				"CheckBoxUI", prefix + "CheckBoxUI",
				"RadioButtonUI", prefix + "RadioButtonUI",
				"ComboBoxUI", prefix + "ComboBoxUI",
				"ToolBarUI", prefix + "ToolBarUI",
				"MenuUI", prefix + "MenuUI",
				"MenuBarUI", prefix + "MenuBarUI",
				"MenuItemUI", prefix + "MenuItemUI",
				"CheckBoxMenuItemUI", prefix + "CheckBoxMenuItemUI",
				"RadioButtonMenuItemUI", prefix + "RadioButtonMenuItemUI",
				"TabbedPaneUI", prefix + "TabbedPaneUI",
				"ProgressBarUI", prefix + "ProgressBarUI",
				"ScrollBarUI", prefix + "ScrollBarUI",
				"SplitPaneUI", prefix + "SplitPaneUI",
				"LabelUI", prefix + "LabelUI",
				"TextFieldUI", prefix + "TextFieldUI",
				"ToolTipUI", prefix + "ToolTipUI",
				"EditorPaneUI", prefix + "EditorPaneUI",
				"FormattedTextFieldUI", prefix + "FormattedTextFieldUI",
				"PasswordFieldUI", prefix + "PasswordFieldUI",
				"TextAreaUI", prefix + "TextAreaUI",
				"ScrollPaneUI", prefix + "ScrollPaneUI",
				"TableHeaderUI", prefix + "TableHeaderUI"
		};
		table.putDefaults(def);
	}

	protected void initComponentDefaults(UIDefaults table) {
		super.initComponentDefaults(table);
		// Get the MetalTheme
		MetalTheme theme = getCurrentTheme();
		// Often used objects
		Insets menuInsets = new InsetsUIResource(0, 2, 0, 3);
		Insets buttonMargin = PlafOptions.useExtraMargin()
				? new InsetsUIResource(1, 14, 1, 14)
				: new InsetsUIResource(1, 4, 1, 4);
		Color gradientStart = ColorUtils.getSimiliarColor(theme.getControl(), 1.4f); // was: theme.getControlHighlight()
		Color gradientEnd = ColorUtils.getSimiliarColor(theme.getControl(), 0.9f); // was: theme.getControlShadow()
		Color specialGradientStart = ColorUtils.getSimiliarColor(theme.getPrimaryControlShadow(), 1.4f);
		Color specialGradientEnd = ColorUtils.getSimiliarColor(theme.getPrimaryControlShadow(), 0.9f);

		Object[] uiDefaults = {
				"glow", ColorUtils.getTranslucentColor(theme.getPrimaryControl(), 175),

				"Button.border", PgsBorders.getButtonBorder(),
				"Button.margin", buttonMargin,
				"Button.rolloverBackground", theme.getControlShadow(),
				"Button.isFlat", PlafOptions.isFlat("Button"),
				"Button.gradientStart", gradientStart,
				"Button.gradientEnd", gradientEnd,
				"Button.rolloverGradientStart", gradientStart,
				"Button.rolloverGradientEnd", gradientEnd,
				"Button.selectedGradientStart", gradientEnd,
				"Button.selectedGradientEnd", gradientStart,
				"Button.rolloverVistaStyle", Boolean.FALSE,

				"ToggleButton.border", PgsBorders.getButtonBorder(),
				"ToggleButton.margin", buttonMargin,
				"ToggleButton.rolloverBackground", theme.getControlShadow(),
				"ToggleButton.isFlat", PlafOptions.isFlat("Button"),
				"ToggleButton.gradientStart", gradientStart,
				"ToggleButton.gradientEnd", gradientEnd,
				"ToggleButton.rolloverGradientStart", gradientStart,
				"ToggleButton.rolloverGradientEnd", gradientEnd,
				"ToggleButton.selectedGradientStart", ColorUtils.getSimiliarColor(theme.getControl(), 0.5f),
				"ToggleButton.selectedGradientEnd", ColorUtils.getSimiliarColor(theme.getControl(), 0.9f),

				"RadioButton.border", PgsBorders.getButtonBorder(),
				"RadioButton.icon", PgsIconFactory.getRadioButtonIcon(),

				"CheckBox.border", PgsBorders.getButtonBorder(),
				"CheckBox.icon", PgsIconFactory.getCheckBoxIcon(),

				"TabbedPane.tabGradientStart", gradientStart,
				"TabbedPane.tabGradientEnd", gradientEnd,
				"TabbedPane.focusPainted", Boolean.FALSE,
				"TabbedPane.buttonStyle.background", theme.getControl(),
				"TabbedPane.buttonStyle.selectedBackground",
				new ColorUIResource(ColorUtils.getTranslucentColor(theme.getPrimaryControlDarkShadow(), 100)),
//new ColorUIResource(new Color(theme.getPrimaryControlDarkShadow().getRed(), theme.getPrimaryControlDarkShadow().getGreen(), theme.getPrimaryControlDarkShadow().getBlue(), 100)),
				"TabbedPane.buttonStyle.rolloverBackground",
				new ColorUIResource(ColorUtils.getTranslucentColor(theme.getPrimaryControlShadow(), 100)),
//new ColorUIResource(new Color(theme.getPrimaryControlShadow().getRed(), theme.getPrimaryControlShadow().getGreen(), theme.getPrimaryControlShadow().getBlue(), 100)),
				"TabbedPane.buttonStyle.selectedBorder", theme.getPrimaryControlDarkShadow(),
				"TabbedPane.buttonStyle.rolloverBorder", theme.getPrimaryControlDarkShadow(),

				"ToolBar.gradientStart", gradientStart,
				"ToolBar.gradientEnd", gradientEnd,
				"ToolBar.border", PgsBorders.getToolBarBorder(),
				"ToolBar.borderColor", theme.getControlShadow(),

				"ToolBarButton.margin", new InsetsUIResource(3, 3, 3, 3),
				"ToolBarButton.rolloverBackground", theme.getPrimaryControl(),
				"ToolBarButton.rolloverBorderColor", theme.getPrimaryControlDarkShadow(),
				"ToolBarButton.isFlat", PlafOptions.isFlat("ToolBarButton"),
				"ToolBarButton.gradientStart", gradientStart,
				"ToolBarButton.gradientEnd", gradientEnd,
				"ToolBarButton.rolloverGradientStart", specialGradientStart,//theme.getPrimaryControl(),
				"ToolBarButton.rolloverGradientEnd", specialGradientEnd,//theme.getPrimaryControlShadow(),
				"ToolBarButton.selectedGradientStart", specialGradientEnd, //theme.getPrimaryControl(),
				"ToolBarButton.selectedGradientEnd", specialGradientStart, //theme.getPrimaryControlDarkShadow(),

				"ComboBox.border", PgsBorders.getTextFieldBorder(),
				"ComboBox.editorBorder", null,
				"ComboBox.selectionBackground", theme.getPrimaryControl(),
				"ComboBox.popup.border", BorderFactory.createLineBorder(theme.getControlDarkShadow()),

				"Menu.submenuPopupOffsetX", new Integer(0),
				"Menu.selectionBackground", theme.getPrimaryControl(),
				"Menu.selectedBorderMargin", menuInsets,
				"Menu.selectedBorderColor", theme.getPrimaryControlDarkShadow(),
				"Menu.gradientStart", specialGradientStart, //theme.getPrimaryControl(),
				"Menu.gradientEnd", specialGradientEnd, //theme.getPrimaryControlDarkShadow(),
				"Menu.borderPainted", Boolean.FALSE,

				"MenuItem.selectionBackground", theme.getPrimaryControl(),
				"MenuItem.selectedBorderMargin", menuInsets,
				"MenuItem.selectedBorderColor", theme.getPrimaryControlDarkShadow(),
				"MenuItem.gradientStart", specialGradientStart, //theme.getPrimaryControl(),
				"MenuItem.gradientEnd", specialGradientEnd, //theme.getPrimaryControlDarkShadow(),
				"MenuItem.isFlat", PlafOptions.isFlat("MenuItem"),
				"MenuItem.borderPainted", Boolean.FALSE,

				"CheckBoxMenuItem.selectionBackground", theme.getPrimaryControl(),
				"CheckBoxMenuItem.selectedBorderMargin", menuInsets,
				"CheckBoxMenuItem.selectedBorderColor", theme.getPrimaryControlDarkShadow(),
				"CheckBoxMenuItem.gradientStart", specialGradientStart, //theme.getPrimaryControl(),
				"CheckBoxMenuItem.gradientEnd", specialGradientEnd, //theme.getPrimaryControlDarkShadow(),
				"CheckBoxMenuItem.isFlat", PlafOptions.isFlat("MenuItem"),
				"CheckBoxMenuItem.borderPainted", Boolean.FALSE,
				"CheckBoxMenuItem.checkIcon", PgsIconFactory.getCheckBoxMenuItemIcon(),

				"RadioButtonMenuItem.selectionBackground", theme.getPrimaryControl(),
				"RadioButtonMenuItem.selectedBorderMargin", menuInsets,
				"RadioButtonMenuItem.selectedBorderColor", theme.getPrimaryControlDarkShadow(),
				"RadioButtonMenuItem.gradientStart", specialGradientStart, //theme.getPrimaryControl(),
				"RadioButtonMenuItem.gradientEnd", specialGradientEnd, //theme.getPrimaryControlDarkShadow(),
				"RadioButtonMenuItem.isFlat", PlafOptions.isFlat("MenuItem"),
				"RadioButtonMenuItem.borderPainted", Boolean.FALSE,
				"RadioButtonMenuItem.checkIcon", PgsIconFactory.getRadioButtonMenuItemIcon(),

				"MenuBar.isFlat", PlafOptions.isFlat("MenuBar"),
				"MenuBar.gradientStart", gradientStart,
				"MenuBar.gradientEnd", gradientEnd,

				"MenuBarMenu.isFlat", PlafOptions.isFlat("MenuBarMenu"),
				"MenuBarMenu.foreground", theme.getMenuForeground(),
				"MenuBarMenu.rolloverBorderColor", theme.getControlTextColor(),
				"MenuBarMenu.selectedBorderColor", theme.getPrimaryControlDarkShadow(),
				"MenuBarMenu.rolloverBackground", theme.getControlShadow(),
				"MenuBarMenu.selectedBackground", theme.getPrimaryControl(),
				"MenuBarMenu.rolloverBackgroundGradientStart", specialGradientStart, //theme.getPrimaryControl(),
				"MenuBarMenu.rolloverBackground.gradientStart", specialGradientStart,
				"MenuBarMenu.rolloverBackgroundGradientEnd", specialGradientEnd, //theme.getPrimaryControlDarkShadow(),
				"MenuBarMenu.rolloverBackground.gradientEnd", specialGradientEnd,
				"MenuBarMenu.selectedBackgroundGradientStart", specialGradientEnd,//theme.getPrimaryControlDarkShadow(),
				"MenuBarMenu.selectedBackgroundGradientEnd", specialGradientStart, //theme.getPrimaryControl(),
				"MenuBarMenu.selectedBackground.gradientStart", specialGradientEnd,
				//theme.getPrimaryControlDarkShadow(),
				"MenuBarMenu.selectedBackground.gradientEnd", specialGradientStart, //theme.getPrimaryControl(),

				"ProgressBar.border", PgsBorders.getComponentBorder(),
				"ProgressBar.gradientStart", gradientStart, //theme.getControl(),
				"ProgressBar.gradientEnd", gradientEnd, //theme.getControlShadow(),
				"ProgressBar.innerGradientStart", specialGradientStart, //theme.getPrimaryControl(),
				"ProgressBar.innerGradientEnd", specialGradientEnd, //theme.getPrimaryControlShadow(),
				"ProgressBar.innerDisabledGradientStart", gradientStart, //theme.getControl(),
				"ProgressBar.innerDisabledGradientEnd", gradientEnd, //theme.getControlShadow(),
				"ProgressBar.innerBorderColor", theme.getPrimaryControlDarkShadow(),
				"ProgressBar.innerDisabledBorderColor", theme.getControlDarkShadow(),

				"ScrollBar.track", theme.getControlShadow(),
				"ScrollBar.trackHighlight", theme.getControl(),
				"ScrollPane.border", BorderFactory.createLineBorder(PgsLookAndFeel.getControlDarkShadow(), 1),

				"TextField.border", PgsBorders.getTextFieldBorder(),
				"TextField.margin", new InsetsUIResource(2, 2, 2, 2),//new InsetsUIResource(0, 4, 0, 4),

				"FormattedTextField.border", PgsBorders.getTextFieldBorder(),
				"FormattedTextField.margin", new InsetsUIResource(2, 2, 2, 2),
				"PasswordField.border", PgsBorders.getTextFieldBorder(),
				"PasswordField.margin", new InsetsUIResource(2, 2, 2, 2),
				//"TextArea.border",								PgsBorders.getTextFieldBorder(),
				"TextArea.margin", new InsetsUIResource(2, 2, 2, 2),
				"EditorPane.border", PgsBorders.getTextFieldBorder(),
				"EditorPane.margin", new InsetsUIResource(2, 2, 2, 2),
				"TextPane.border", PgsBorders.getTextFieldBorder(),
				"TextPane.margin", new InsetsUIResource(2, 2, 2, 2),

				"Tooltip.gradientStart", gradientStart,
				"Tooltip.gradientEnd", gradientEnd,
				"ToolTip.border", BorderFactory.createLineBorder(theme.getControlDarkShadow(), 1),

				"Table.scrollPaneBorder", PgsBorders.getComponentBorder(),

				"TabbedPane.background", PgsLookAndFeel.getControlShadow(),
				"SplitPane.border", null,
				"SplitPaneDivider.border", null,

				"OptionPane.errorIcon", makeIcon(getClass(), "icons/Error.png"),
				"OptionPane.informationIcon", makeIcon(getClass(), "icons/Inform.png"),
				"OptionPane.warningIcon", makeIcon(getClass(), "icons/Warn.png"),
				"OptionPane.questionIcon", makeIcon(getClass(), "icons/Question.png"),

				"FileView.computerIcon", makeIcon(getClass(), "icons/Computer.png"),
				"FileView.directoryIcon", makeIcon(getClass(), "icons/TreeClosed.png"),
				"FileView.fileIcon", makeIcon(getClass(), "icons/File.png"),
				"FileView.floppyDriveIcon", makeIcon(getClass(), "icons/FloppyDrive.png"),
				"FileView.hardDriveIcon", makeIcon(getClass(), "icons/HardDrive.png"),
				"FileChooser.homeFolderIcon", makeIcon(getClass(), "icons/HomeFolder.png"),
				"FileChooser.newFolderIcon", makeIcon(getClass(), "icons/NewFolder.png"),
				"FileChooser.upFolderIcon", makeIcon(getClass(), "icons/UpFolder.png"),

				"Tree.closedIcon", makeIcon(getClass(), "icons/TreeClosed.png"),
				"Tree.openIcon", makeIcon(getClass(), "icons/TreeOpen.png"),
				"Tree.leafIcon", makeIcon(getClass(), "icons/TreeLeaf.png")
		};
		table.putDefaults(uiDefaults);
	}

	public Icon getDisabledSelectedIcon(JComponent component, Icon icon) {
		if ((icon instanceof ImageIcon)) {
			return PgsUtils.getDisabledButtonIcon(
					((ImageIcon) icon).getImage());
		}
		return null;
	}

	public static Color getGlow() {
		return UIManager.getColor("glow");
	}
}
