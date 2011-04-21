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
import javax.swing.plaf.*;
import javax.swing.plaf.basic.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;

public class PgsEditorPaneUI extends BasicEditorPaneUI {
	public static ComponentUI createUI(JComponent c) {
		return new PgsEditorPaneUI();
	}

	public final void paintSafely(Graphics g) {
		PgsUtils.installAntialiasing(g);
		super.paintSafely(g);
		PgsUtils.uninstallAntialiasing(g);
	}

	protected void propertyChange(PropertyChangeEvent evt) {
		super.propertyChange(evt);

		if (PlafOptions.isHtmlDisplayFixEnabled() && "editorKit".equals(evt.getPropertyName())) {
			PgsUtils.fixHtmlDisplay(getComponent());
		}
	}

	protected void uninstallListeners() {
		super.uninstallListeners();

		getComponent().removeMouseListener(TextComponentPopupHandler.getInstance());
	}

	protected void installListeners() {
		super.installListeners();

		getComponent().addMouseListener(TextComponentPopupHandler.getInstance());
		getComponent().addFocusListener(TextComponentFocusListener.getInstance());
	}
}
