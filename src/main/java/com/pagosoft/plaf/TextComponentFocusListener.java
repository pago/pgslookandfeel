package com.pagosoft.plaf;

import javax.swing.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class TextComponentFocusListener implements FocusListener {
	private static TextComponentFocusListener instance;

	public static TextComponentFocusListener getInstance() {
		if(instance == null) {
			instance = new TextComponentFocusListener();
		}
		return instance;
	}
	private TextComponentFocusListener() {}

	public void focusGained(FocusEvent e) {
		Object source = e.getSource();
		if(source instanceof JComponent) {
			((JComponent)source).repaint();
		}
	}

	public void focusLost(FocusEvent e) {
		Object source = e.getSource();
		if(source instanceof JComponent) {
			((JComponent)source).repaint();
		}
	}
}
