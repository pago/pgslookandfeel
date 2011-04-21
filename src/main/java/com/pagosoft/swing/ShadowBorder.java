/*
 * Copyright (c) 2004 JGoodies Karsten Lentzsch. All Rights Reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  o Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 *  o Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 *  o Neither the name of JGoodies Karsten Lentzsch nor the names of
 *    its contributors may be used to endorse or promote products derived
 *    from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS;
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE
 * OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.pagosoft.swing;

import java.awt.*;

import javax.swing.*;
import javax.swing.border.*;

public class ShadowBorder extends AbstractBorder {
	private static ShadowBorder instance;
	public static ShadowBorder getInstance() {
		if(instance == null) {
			instance = new ShadowBorder();
		}
		return instance;
	}

	private ShadowBorder() {}

	private Insets borderInsets = new Insets(0, 0, 5, 5);
	private Image shadow = new ImageIcon(ShadowBorder.class.getResource("shadow.png")).getImage();

	public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
		g.drawImage(shadow, x +  5, y + height - 5, x + 10, y + height, 0, 6, 5, 11, null, c);
		g.drawImage(shadow, x + 10, y + height - 5, x + width - 5, y + height, 5, 6, 6, 11, null, c);
		g.drawImage(shadow, x + width - 5, y + 5, x + width, y + 10, 6, 0, 11, 5, null, c);
		g.drawImage(shadow, x + width - 5, y + 10, x + width, y + height - 5, 6, 5, 11, 6, null, c);
		g.drawImage(shadow, x + width - 5, y + height - 5, x + width, y + height, 6, 6, 11, 11, null, c);
	}

	public Insets getBorderInsets(Component c) {
		return borderInsets;
	}

	public boolean isBorderOpaque() {
		return false;
	}
}
