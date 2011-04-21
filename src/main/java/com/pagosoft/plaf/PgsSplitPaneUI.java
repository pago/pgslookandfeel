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

import com.pagosoft.swing.ShadowBorder;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.plaf.*;
import javax.swing.plaf.basic.*;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class PgsSplitPaneUI extends BasicSplitPaneUI {
	public static ComponentUI createUI(JComponent x) {
		return new PgsSplitPaneUI();
	}

	private SplitPaneContainerListener splitPaneContainerListener;
	private MyPropertyChangeHandler propertyChangeHandler;

	public void installDefaults() {
		super.installDefaults();

		maybeSetShadowBorder(splitPane.getLeftComponent());
		maybeSetShadowBorder(splitPane.getRightComponent());
	}

	public void uninstallDefaults() {
		super.uninstallDefaults();

		maybeRemoveShadowBorder(splitPane.getLeftComponent());
		maybeRemoveShadowBorder(splitPane.getRightComponent());
	}

	protected boolean isShadowBorder() {
		if (splitPane.getClientProperty("pgs.shadowBorder") != null) {
			return Boolean.TRUE.equals(splitPane.getClientProperty("pgs.shadowBorder"));
		}
		return PlafOptions.isShadowBorderUsed();
	}

	protected void maybeSetShadowBorder(Component c) {
		if ((c instanceof JComponent) && isShadowBorder()) {
			JComponent comp = (JComponent) c;
			comp.putClientProperty("pgs.savedBorder", comp.getBorder());
			if (comp.getBorder() instanceof BorderUIResource) {
				comp.setBorder(ShadowBorder.getInstance());
			} else {
				comp.setBorder(
						BorderFactory.createCompoundBorder(
								ShadowBorder.getInstance(),
								comp.getBorder()));
			}
		}
	}

	protected void maybeRemoveShadowBorder(Component c) {
		if ((c instanceof JComponent) && !isShadowBorder()) {
			((JComponent) c).setBorder((Border) ((JComponent) c).getClientProperty("pgs.savedBorder"));
		}
	}

	public void installListeners() {
		super.installListeners();
		if (splitPaneContainerListener == null) {
			splitPaneContainerListener = new SplitPaneContainerListener();
		}
		if (propertyChangeHandler == null) {
			propertyChangeHandler = new MyPropertyChangeHandler();
		}
		splitPane.addContainerListener(splitPaneContainerListener);
		splitPane.addPropertyChangeListener("pgs.shadowBorder", propertyChangeHandler);
	}

	public void uninstallListeners() {
		super.uninstallListeners();
		splitPane.removeContainerListener(splitPaneContainerListener);
		splitPane.removePropertyChangeListener("pgs.shadowBorder", propertyChangeHandler);
	}

	public BasicSplitPaneDivider createDefaultDivider() {
		return new PgsSplitPaneDivider(this);
	}

	protected class SplitPaneContainerListener implements ContainerListener {
		public void componentAdded(ContainerEvent e) {
			maybeSetShadowBorder(e.getChild());
		}

		public void componentRemoved(ContainerEvent e) {
			maybeRemoveShadowBorder(e.getChild());
		}
	}

	protected class MyPropertyChangeHandler implements PropertyChangeListener {
		public void propertyChange(PropertyChangeEvent e) {
			if (!e.getNewValue().equals(e.getOldValue())) {
				boolean use;
				if (e.getOldValue() != null) {
					use = ((Boolean) e.getOldValue()).booleanValue();
				} else {
					use = PlafOptions.isShadowBorderUsed();
				}
				if (use) {
					maybeRemoveShadowBorder(splitPane.getLeftComponent());
					maybeRemoveShadowBorder(splitPane.getRightComponent());
				}

				use = ((Boolean) e.getNewValue()).booleanValue();
				if (use) {
					maybeSetShadowBorder(splitPane.getLeftComponent());
					maybeSetShadowBorder(splitPane.getRightComponent());
				}
			}
		}
	}
}
