/*
 * PgsTableHeaderUI.java
 *
 * Created on 17. April 2005, 09:40
 */

package com.pagosoft.plaf;

import javax.swing.*;
import javax.swing.plaf.*;
import javax.swing.plaf.basic.*;
import javax.swing.table.*;
import java.awt.*;

/**
 * @author pago
 */
public class PgsTableHeaderUI extends BasicTableHeaderUI {
	public static ComponentUI createUI(JComponent h) {
		return new PgsTableHeaderUI();
	}

	/**
	 * Creates a new instance of PgsTableHeaderUI
	 */
	public PgsTableHeaderUI() {
		super();
	}

	protected void installDefaults() {
		super.installDefaults();

		header.setBorder(null);
		if(header.getDefaultRenderer() instanceof UIResource) {
			header.setDefaultRenderer(new PgsDefaultTableHeaderCellRenderer());
		}
	}

	private static class PgsDefaultTableHeaderCellRenderer extends DefaultTableCellRenderer
			implements UIResource {
		public PgsDefaultTableHeaderCellRenderer() {
			super();
			setHorizontalAlignment(JLabel.CENTER);
		}

		// implements javax.swing.table.TableCellRenderer
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
			super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

			if(column == table.getColumnCount()) {
				setBorder(new PgsBorders.Generic(new Insets(0, 0, 1, 0), PgsLookAndFeel.getControlDarkShadow()));
			} else {
				setBorder(new PgsBorders.Generic(new Insets(0, 0, 1, 1), PgsLookAndFeel.getControlDarkShadow()));
			}
			setBackground(PgsLookAndFeel.getControl());
			return this;
		}
	}

	public void paint(Graphics g, JComponent c) {
		if (header.getColumnModel().getColumnCount() <= 0) {
			return;
		}
		boolean ltr = header.getComponentOrientation().isLeftToRight();

		Rectangle clip = g.getClipBounds();
		Point left = clip.getLocation();
		Point right = new Point(clip.x + clip.width - 1, clip.y);
		TableColumnModel cm = header.getColumnModel();
		int cMin = header.columnAtPoint(ltr ? left : right);
		int cMax = header.columnAtPoint(ltr ? right : left);
		// This should never happen.
		if (cMin == -1) {
			cMin = 0;
		}
		// If the table does not have enough columns to fill the view we'll get -1.
		// Replace this with the index of the last column.
		if (cMax == -1) {
			cMax = cm.getColumnCount() - 1;
		}

		TableColumn draggedColumn = header.getDraggedColumn();
		int columnWidth;
		Rectangle cellRect = header.getHeaderRect(ltr ? cMin : cMax);
		TableColumn aColumn;
		if (ltr) {
			for (int column = cMin; column <= cMax; column++) {
				aColumn = cm.getColumn(column);
				columnWidth = aColumn.getWidth();
				cellRect.width = columnWidth;
				if (aColumn != draggedColumn) {
					paintCell(g, cellRect, column);
				}
				cellRect.x += columnWidth;
			}
		} else {
			for (int column = cMax; column >= cMin; column--) {
				aColumn = cm.getColumn(column);
				columnWidth = aColumn.getWidth();
				cellRect.width = columnWidth;
				if (aColumn != draggedColumn) {
					paintCell(g, cellRect, column);
				}
				cellRect.x += columnWidth;
			}
		}

		// Paint the dragged column if we are dragging.
		if (draggedColumn != null) {
			int draggedColumnIndex = viewIndexForColumn(draggedColumn);
			Rectangle draggedCellRect = header.getHeaderRect(draggedColumnIndex);

			// Draw a gray well in place of the moving column.
			g.setColor(header.getParent().getBackground());
			g.fillRect(
					draggedCellRect.x, draggedCellRect.y,
					draggedCellRect.width, draggedCellRect.height);

			draggedCellRect.x += header.getDraggedDistance();

			// Fill the background.
			g.setColor(header.getBackground());
			g.fillRect(
					draggedCellRect.x, draggedCellRect.y,
					draggedCellRect.width, draggedCellRect.height);

			paintCell(g, draggedCellRect, draggedColumnIndex);
		}

		// Remove all components in the rendererPane.
		rendererPane.removeAll();
	}

	private Component getHeaderRenderer(int columnIndex) {
		TableColumn aColumn = header.getColumnModel().getColumn(columnIndex);
		TableCellRenderer renderer = aColumn.getHeaderRenderer();
		if (renderer == null) {
			renderer = header.getDefaultRenderer();
		}
		return renderer.getTableCellRendererComponent(
				header.getTable(),
				aColumn.getHeaderValue(), false, false,
				-1, columnIndex);
	}

	private int viewIndexForColumn(TableColumn aColumn) {
		TableColumnModel cm = header.getColumnModel();
		for (int column = 0; column < cm.getColumnCount(); column++) {
			if (cm.getColumn(column) == aColumn) {
				return column;
			}
		}
		return -1;
	}

	private void paintCell(Graphics g, Rectangle cellRect, int columnIndex) {
		Component component = getHeaderRenderer(columnIndex);
		rendererPane.paintComponent(
				g, component, header, cellRect.x, cellRect.y,
				cellRect.width, cellRect.height, true);
	}
}
