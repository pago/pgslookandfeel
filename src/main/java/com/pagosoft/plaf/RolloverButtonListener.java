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
import javax.swing.plaf.basic.*;
import java.awt.event.*;

/**
 * @author Patrick Gotthardt
 */
public class RolloverButtonListener extends BasicButtonListener {
	public RolloverButtonListener(AbstractButton b) {
		super(b);
		b.setRolloverEnabled(true);
	}

	public void mouseEntered(MouseEvent e) {
		super.mouseEntered(e);
		AbstractButton b = (AbstractButton) e.getSource();
		ButtonModel model = b.getModel();
		if (b.isRolloverEnabled() && !SwingUtilities.isLeftMouseButton(e)) {
			model.setRollover(true);
		}
		if (model.isPressed()) {
			model.setArmed(true);
		}
		b.repaint();
	}

	public void mouseExited(MouseEvent e) {
		super.mouseExited(e);
		AbstractButton b = (AbstractButton) e.getSource();
		ButtonModel model = b.getModel();
		if (b.isRolloverEnabled()) {
			model.setRollover(false);
		}
		if (model.isPressed()) {
			model.setArmed(false);
		}
		b.repaint();
	}
}
