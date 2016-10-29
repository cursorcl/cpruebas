package com.xenoage.util.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

/**
 * The component for a list item.
 *
 * @author Andreas Wenger
 */
class Item extends JPanel {
    private static final long serialVersionUID = -6357777836686860207L;

    /**
     * 
     */
    private final FontChooserComboBox fontChooserComboBox;

    final Font font;
    final boolean isSeparator;

    Item(FontChooserComboBox fontChooserComboBox, String fontName) {
        this.fontChooserComboBox = fontChooserComboBox;
        if (fontName != null) {
            font = new Font(fontName, Font.PLAIN, this.fontChooserComboBox.previewFontSize);
            isSeparator = false;
        } else {
            font = null;
            isSeparator = true;
        }

        setOpaque(true);

        if (!isSeparator) {
            setLayout(new FlowLayout(FlowLayout.LEFT));

            // font name in default font
            final JLabel labelHelp = new JLabel(font.getName());
            this.add(labelHelp);

            // preview string in this font
            if (this.fontChooserComboBox.previewString != null) {
                // show only supported characters
                final StringBuilder thisPreview = new StringBuilder();
                for (int i = 0; i < this.fontChooserComboBox.previewString.length(); i++) {
                    final char c = this.fontChooserComboBox.previewString.charAt(i);
                    if (font.canDisplay(c))
                        thisPreview.append(c);
                }
                final JLabel labelFont = new JLabel(thisPreview.toString());
                labelFont.setFont(font);
                this.add(labelFont);
            }
        } else {
            // separator
            setLayout(new BorderLayout());
            this.add(new JSeparator(SwingConstants.HORIZONTAL), BorderLayout.CENTER);
        }
    }

    @Override
    public String toString() {
        if (font != null)
            return font.getFamily();
        else
            return "";
    }
}