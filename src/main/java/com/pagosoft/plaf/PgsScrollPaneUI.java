/*
 * PgsScrollPaneUI.java
 *
 * Created on 15. Mai 2005, 14:21
 */

package com.pagosoft.plaf;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.plaf.*;
import javax.swing.plaf.metal.*;
import java.awt.*;
import java.awt.event.*;

/**
 * @author pago
 */
public class PgsScrollPaneUI extends MetalScrollPaneUI {
	public static ComponentUI createUI(JComponent x) {
		return new PgsScrollPaneUI();
	}

	public void installUI(JComponent c) {
		super.installUI(c);
		//maybeRemoveBorder(scrollpane.getViewport().getView());
	}

	private ScrollPaneContainerListener scrollPaneContainerListener;

	public void installListeners(JScrollPane c) {
		super.installListeners(c);
		/*if (scrollPaneContainerListener == null) {
			scrollPaneContainerListener = new ScrollPaneContainerListener();
		}
		c.getViewport().addContainerListener(scrollPaneContainerListener);*/
	}

	public void uninstallListeners(JScrollPane c) {
		super.uninstallListeners(c);
		//c.getViewport().removeContainerListener(scrollPaneContainerListener);
	}

	private Border lastChildBorder;

	public void maybeRemoveBorder(Component c) {
		if (c instanceof JComponent) {
			JComponent child = (JComponent) c;
			lastChildBorder = child.getBorder();
			child.setBorder(null);
		}
	}

	public void maybeAddBorder(Component c) {
		if (c instanceof JComponent && lastChildBorder != null) {
			JComponent child = (JComponent) c;
			child.setBorder(lastChildBorder);
			lastChildBorder = null;
		}
	}

	protected class ScrollPaneContainerListener implements ContainerListener {
		public void componentAdded(ContainerEvent e) {
			maybeRemoveBorder(e.getChild());
		}

		public void componentRemoved(ContainerEvent e) {
			maybeAddBorder(e.getChild());
		}
	}
}
