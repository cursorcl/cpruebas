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
class Item extends JPanel
{
  /**
   * 
   */
  private final FontChooserComboBox fontChooserComboBox;

  private static final long serialVersionUID = -6357777836686860207L;

  final Font font;
  final boolean isSeparator;

  Item(FontChooserComboBox fontChooserComboBox, String fontName)
  {
    this.fontChooserComboBox = fontChooserComboBox;
    if (fontName != null)
    {
      this.font = new Font(fontName, Font.PLAIN, this.fontChooserComboBox.previewFontSize);
      this.isSeparator = false;
    }
    else
    {
      this.font = null;
      this.isSeparator = true;
    }

    this.setOpaque(true);

    if (!isSeparator)
    {
      this.setLayout(new FlowLayout(FlowLayout.LEFT));

      // font name in default font
      JLabel labelHelp = new JLabel(font.getName());
      this.add(labelHelp);

      // preview string in this font
      if (this.fontChooserComboBox.previewString != null)
      {
        // show only supported characters
        StringBuilder thisPreview = new StringBuilder();
        for (int i = 0; i < this.fontChooserComboBox.previewString.length(); i++)
        {
          char c = this.fontChooserComboBox.previewString.charAt(i);
          if (font.canDisplay(c))
            thisPreview.append(c);
        }
        JLabel labelFont = new JLabel(thisPreview.toString());
        labelFont.setFont(font);
        this.add(labelFont);
      }
    }
    else
    {
      // separator
      this.setLayout(new BorderLayout());
      this.add(new JSeparator(SwingConstants.HORIZONTAL), BorderLayout.CENTER);
    }
  }

  @Override
  public String toString()
  {
    if (font != null)
      return font.getFamily();
    else
      return "";
  }
}