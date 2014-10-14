/**
 * Feb 13, 2013 - ayachan
 */
package cl.sisdef.util;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeListener;

/**
 * Form utility methods family.
 * 
 * @author ayachan
 */
public class UtilForm
{

  // fonts definition

  static final String FONT_NAME = "Tahoma";
  static final int FONT_STYLE = Font.PLAIN;

  static final int FONT_XBIG_PX = 56;
  static final int FONT_VVBIG_PX = 30;
  static final int FONT_VBIG_PX = 20;
  static final int FONT_BIG_PX = 15;
  static final int FONT_MEDIUM_PX = 13;
  static final int FONT_SMALL_PX = 11;
  static final int FONT_VSMALL_PX = 9;

  static public enum ContextSpace
  {
    DEFAULT, MAIN, SECTION, SUBSECTION
  }

  /**
   * Fonts applied to typical application contexts.
   * 
   * @author ayachan
   */
  static public enum ContextFont
  {
    // (standard sizes)

    XBIG (FONT_NAME, FONT_STYLE, FONT_XBIG_PX),
    VVBIG (FONT_NAME, FONT_STYLE, FONT_VVBIG_PX),
    VBIG (FONT_NAME, FONT_STYLE, FONT_VBIG_PX),
    BIG (FONT_NAME, FONT_STYLE, FONT_BIG_PX),
    MEDIUM (FONT_NAME, FONT_STYLE, FONT_MEDIUM_PX),
    SMALL (FONT_NAME, FONT_STYLE, FONT_SMALL_PX),
    VSMALL (FONT_NAME, FONT_STYLE, FONT_VSMALL_PX),

    // (titled sizes)

    /**
     * Intended to be used on (big) titles.
     */
    TITLE (FONT_NAME, FONT_STYLE, FONT_VBIG_PX),
    /**
     * Intended to be used on border titles.
     */
    BORDER (FONT_NAME, FONT_STYLE,
        FONT_SMALL_PX, FONT_VBIG_PX, FONT_MEDIUM_PX, FONT_SMALL_PX),
    /**
     * Intended to be used on texts and labels.
     */
    TEXT (FONT_NAME, FONT_STYLE, FONT_MEDIUM_PX),
    /**
     * Intended to be used on buttons titles.
     */
    BUTTON (FONT_NAME, FONT_STYLE, FONT_SMALL_PX),
    /**
     * Intended to be used on tables.
     */
    TABLE (FONT_NAME, FONT_STYLE, FONT_SMALL_PX);

    final Font[] font;
    ContextFont(String fontName, int fontStyle, int ... fontSize)
    {
      font = new Font[fontSize.length];
      int index = 0;
      for (int size : fontSize)
        font[index] = new Font(fontName, fontStyle, size);
//      font = new Font(fontName, fontStyle, fontSize);
    }
    public Font getFont()
    {
      return getFont(ContextSpace.DEFAULT);
    }
    public Font getFont(ContextSpace contextSpace)
    {
      int index = contextSpace.ordinal();
      if (index >= font.length)
        index = font.length - 1;
      return font[index];
    }
  };

  static public enum ContextHeight
  {
    DEFAULT(20), BUTTON(20), AREA(40), ICON(30);

    int pixels;

    /**
     * 
     */
    private ContextHeight(int pixels)
    {
      this.pixels = pixels;
    }

    /**
     * @return the pixels
     */
    public int getPixels()
    {
      return pixels;
    }
  }

  static final int DEFAULT_INSETS_px = 2;
  static public final Insets DEFAULT_INSETS =
      new Insets(
          DEFAULT_INSETS_px, DEFAULT_INSETS_px,
          DEFAULT_INSETS_px, DEFAULT_INSETS_px);

  /**
   * Fix the dimension of a component.
   * <p>
   * <b>WARNING</b>:<br />
   * Dimensions are managed by layouts by default. Hand setting can (and will)
   * produce display inconsistencies.
   * </p>
   * 
   * @param component
   *          The component to resize.
   * @param size
   *          The size to fix.
   */
  static public void setDimension(JComponent component, Dimension size)
  {
    if (size == null)
      return;

    component.setPreferredSize(size);
    component.setMinimumSize(size);
    component.setMaximumSize(size);
  }

  /**
   * Set common attributes to the given component.<br />
   * This method is intended to be used only internally.
   * 
   * @param component
   *          The component to set attributes.
   * @param tip
   *          The tip string to set if not null.
   * @param font
   *          The font to use if not null, TEXT otherwise.
   * @param border
   *          The border to use if not null.
   */
  static public void setComponentAttributes(JComponent component,
      String tip, Font font, Border border)
  {
    if (font != null)
      component.setFont(font);
    else
      component.setFont(ContextFont.TEXT.getFont());

    if (tip != null)
      component.setToolTipText(tip);

    if (border != null)
      component.setBorder(border);
  }

  /**
   * Shortcut of the formal constructor.
   * @param text 
   * @param font 
   * @return 
   */
  static public JLabel createLabel(String text, Font font)
  {
    return createLabel(text, SwingConstants.CENTER, SwingConstants.CENTER,
        null, font, null);
  }

  /**
   * Create a label with the given attributes.
   * 
   * @param text
   *          The label text.
   * @param alignment_h
   *          The label horizontal alignment.
   * @param alignment_v
   *          The label vertical alignment.
   * @param tip
   *          The label tip message.
   * @param font
   *          The label font if not null, TEXT otherwise.
   * @param border
   *          The label border if not null;
   * @return The new label component.
   */
  static public JLabel createLabel(String text,
      int alignment_h, int alignment_v,
      String tip, Font font, Border border)
  {
    JLabel result = new JLabel(text);
    result.setHorizontalAlignment(alignment_h);
    result.setVerticalAlignment(alignment_v);

    setComponentAttributes(result, tip,
        font != null ? font : ContextFont.TEXT.getFont(),
        border);

    return result;
  }

  /**
   * Create a text with the given attributes.
   * 
   * @param text
   *          The initial text or null if none.
   * @param tip
   *          The component tip.
   * @param font
   *          The font if not null, TEXT otherwise.
   * @param border
   *          The border if not null.
   * @return The new text component.
   */
  static public JTextField createTextField(String text,
      String tip, Font font, Border border)
  {
    JTextField result = new JTextField(text);
    setComponentAttributes(result, tip,
        font != null ? font : ContextFont.TEXT.getFont(),
        border);

    return result;
  }

  static public JPasswordField createPasswordField(String text,
      String tip, Font font, Border border)
  {
    JPasswordField result = new JPasswordField(text);
    setComponentAttributes(result, tip, font, border);

    return result;
  }

  /**
   * Create a text area with the given attributes. </br>
   * to add this component to <b>Miglayout</b> append the property "wmin 10"
   * @param text
   * @param tip
   * @param font
   * @param border
   * @return
   */
  static public JTextArea createTextArea(String text,
      String tip, Font font, Border border)
  {
    JTextArea result = new JTextArea(text);
    setComponentAttributes(result, tip, font, border);

    result.setLineWrap(true);
    result.setWrapStyleWord(true);
    return result;
  }

  /**
   * Set the given properties to the given (abstract) button.<br />
   * This method is intended to be used only internally.
   * 
   * @param button
   *          The (abstract) button to assign the attributes.
   * @param actionCommand
   *          The listener to assign as action listener if not null.
   * @param actionListener
   *          The command string to report if not null.
   */
  static void setAbstractButtonAttributes(AbstractButton button,
      String actionCommand, ActionListener actionListener)
  {
    if (actionListener != null)
      button.addActionListener(actionListener);
    if (actionCommand != null)
      button.setActionCommand(actionCommand);
  }

  /**
   * Create a button with the given attributes.
   * 
   * @param title
   *          The title of the button.
   * @param tip
   *          The tip to display over the component.
   * @param font
   *          The font to use if not null, BUTTON otherwise.
   * @param border
   *          The border to use if not null.
   * @param actionListener
   *          The action listener if not null.
   * @param actionCommand
   *          The action command if not null.
   * @return The new button.
   */
  static public JButton createButton(String title,
      String tip, Font font, Border border,
      ActionListener actionListener, String actionCommand)
  {
    JButton result = new JButton(title);
    setComponentAttributes(result, tip,
        font != null ? font : ContextFont.BUTTON.getFont(),
        border);
    setAbstractButtonAttributes(result, actionCommand, actionListener);

    return result;
  }

  static public JButton createImageButton(Icon icon,
      String tip, Font font, Border border,
      ActionListener actionListener, String actionCommand)
  {
    JButton result = new JButton();
    result.setIcon(icon);

    setComponentAttributes(result, tip,
        font != null ? font : ContextFont.BUTTON.getFont(),
        border);
    setAbstractButtonAttributes(result, actionCommand, actionListener);

    return result;
  }

  static public JLabel createImageTextLabel(Icon icon, String title,
      int textVerticalAlignment, int textHorizontalAlignment,
      String tip, Font font, Border border,
      ActionListener actionListener, String actionCommand)
  {
    JLabel result = new JLabel(title, icon, SwingConstants.CENTER);
    result.setVerticalAlignment(textVerticalAlignment);
    result.setHorizontalAlignment(textHorizontalAlignment);

    setComponentAttributes(result, tip,
        font != null ? font : ContextFont.BUTTON.getFont(),
        border);

    return result;
  }

  /**
   * Create a radio button with the given attributes.
   * 
   * @param title
   *          The title of the button.
   * @param tip
   *          The tip to display over the component.
   * @param font
   *          The font to use if not null, BUTTON otherwise.
   * @param border
   *          The border to use if not null.
   * @param actionListener
   *          The action listener if not null.
   * @param actionCommand
   *          The action command if not null.
   * @return The new radio button.
   */
  static public JRadioButton createRadioButton(String title,
      String tip, Font font, Border border,
      ActionListener actionListener, String actionCommand)
  {
    JRadioButton result = new JRadioButton(title);
    setComponentAttributes(result, tip,
        font != null ? font : ContextFont.BUTTON.getFont(),
        border);
    setAbstractButtonAttributes(result, actionCommand, actionListener);

    return result;
  }

  /**
   * Create a check box with the given attributes.
   * 
   * @param title
   *          The title of the button.
   * @param initialState
   *          TRUE if the control must start checked, FALSE otherwise.
   * @param tip
   *          The tip to display over the component.
   * @param font
   *          The font to use if not null, BUTTON otherwise.
   * @param border
   *          The border to use if not null.
   * @param actionListener
   *          The action listener if not null.
   * @param actionCommand
   *          The action command if not null.
   * @return The new radio button.
   */
  static public JCheckBox createCheckBox(String title, boolean initialState,
      String tip, Font font, Border border,
      ActionListener actionListener, String actionCommand)
  {
    JCheckBox result = new JCheckBox(title);
    result.setSelected(initialState);
    setComponentAttributes(result, tip,
        font != null ? font : ContextFont.BUTTON.getFont(),
        border);
    setAbstractButtonAttributes(result, actionCommand, actionListener);

    return result;
  }

  /**
   * Create a toggle button with the given attributes.
   * 
   * @param title
   *          The title of the button.
   * @param toggleState
   *          TRUE if the control must start pressed, FALSE otherwise.
   * @param tip
   *          The tip to display over the component.
   * @param font
   *          The font to use if not null, BUTTON otherwise.
   * @param border
   *          The border to use if not null.
   * @param actionListener
   *          The action listener if not null.
   * @param actionCommand
   *          The action command if not null.
   * @return The new toggle button.
   */
  static public JToggleButton createToggleButton(String title, boolean toggleState,
      String tip, Font font, Border border,
      ActionListener actionListener, String actionCommand)
  {
    JToggleButton result = new JToggleButton();
    result.setText(title);
    result.setSelected(toggleState);

    setComponentAttributes(result, tip,
        font != null ? font : ContextFont.BUTTON.getFont(),
        border);
    setAbstractButtonAttributes(result, actionCommand, actionListener);

    return result;
  }

  // spinner methods definitions

  /**
   * Create a spinner with the given attributes.
   * 
   * @param value
   *          The spinner initial value.
   * @param minValue
   *          The spinner minimum value.
   * @param maxValue
   *          The spinner maximum value.
   * @param step
   *          The spinner step.
   * @param tip
   *          The tip to display over the component.
   * @param font
   *          The font to use if not null, BUTTON otherwise.
   * @param border
   *          The border to use if not null.
   * @param listener
   *          The spinner change listener.
   * @return The new spinner control.
   */
  static public JSpinner createSpinner(int value, int minValue, int maxValue, int step,
      String tip, Font font, Border border,
      ChangeListener listener)
  {
    SpinnerModel model = new SpinnerNumberModel(value, minValue, maxValue, step);
    JSpinner result = createSpinner(model, tip, font, border, listener);
    return result;
  }

  /**
   * Shortcut of the formal constructor.
   * @param model 
   * @param font 
   * @return 
   */
  static public JSpinner createSpinner(SpinnerModel model, Font font)
  {
    return createSpinner(model, null, font, null, null);
  }

  /**
   * Create a spinner with the given model and attributes.
   * 
   * @param model
   *          The spinner model (initial value, minimum, maximum and step).
   * @param tip
   *          The tip to display over the component.
   * @param font
   *          The font to use if not null, BUTTON otherwise.
   * @param border
   *          The border to use if not null.
   * @param listener
   *          The spinner change listener.
   * @return The new spinner control.
   */
  static public JSpinner createSpinner(SpinnerModel model,
      String tip, Font font, Border border,
      ChangeListener listener)
  {
    JSpinner result = new JSpinner(model);

    setComponentAttributes(result, tip, font, border);

    if (listener != null)
      result.addChangeListener(listener);

    return result;
  }

  // combo box methods definitions

  /**
   * Create a combo box with the given attributes.
   * 
   * @param tip
   *          The tip to display over the component.
   * @param font
   *          The font to use if not null, BUTTON otherwise.
   * @param border
   *          The border to use if not null.
   * @param itemListener
   *          The item listener if not null.
   * @param actionCommand
   *          The action command if not null.
   * @return The new combo box component.
   */
  static public <T> JComboBox<T> createComboBox(
      String tip, Font font, Border border,
      ItemListener itemListener, String actionCommand)
  {
    JComboBox<T> result = new JComboBox<T>();
    setComponentAttributes(result, tip, font, border);

    // same as with an abstract button
    if (itemListener != null)
      result.addItemListener(itemListener);
    if (actionCommand != null)
      result.setActionCommand(actionCommand);

    return result;
  }
  
  /**
   * Create a combo box with the given attributes.
   * @param items
   *          an array of objects to insert into the combo box
   * @param tip
   *          The tip to display over the component.
   * @param font
   *          The font to use if not null, BUTTON otherwise.
   * @param border
   *          The border to use if not null.
   * @param itemListener 
   *          The item listener if not null.
   * @param actionCommand
   *          The action command if not null.
   * @return The new combo box component.
   */
  static public JComboBox<Object> createComboBox(Object [] items,
      String tip, Font font, Border border,
      ItemListener itemListener, String actionCommand)
  {
    JComboBox<Object> result = new JComboBox<Object>(items);
    setComponentAttributes(result, tip, font, border);

    // same as with an abstract button
    if (itemListener != null)
      result.addItemListener(itemListener);
    if (actionCommand != null)
      result.setActionCommand(actionCommand);

    return result;
  }
  
  /**
   * Create a combo box with the given attributes.
   * 
   * @param tip
   *          The tip to display over the component.
   * @param font
   *          The font to use if not null, BUTTON otherwise.
   * @param border
   *          The border to use if not null.
   * @param actionListener
   *          The action listener if not null.
   * @param actionCommand
   *          The action command if not null.
   * @return The new combo box component.
   */
  static public <T> JComboBox<T> createComboBox(
      String tip, Font font, Border border,
      ActionListener actionListener, String actionCommand)
  {
    JComboBox<T> result = new JComboBox<T>();
    setComponentAttributes(result, tip, font, border);

    // same as with an abstract button
    if (actionListener != null)
      result.addActionListener(actionListener);
    if (actionCommand != null)
      result.setActionCommand(actionCommand);

    return result;
  }

  /**
   * Create a new bevel border.<br />
   * Intended to be called directly inside the above method calls.
   * 
   * @param bevelBorder
   *          The bevel border type.
   * @return The new border.
   */
  static public Border createBorder(int bevelBorder)
  {
    return BorderFactory.createBevelBorder(bevelBorder);
  }

  /**
   * Create a new titled border.
   * 
   * @param title
   *          The titled border title.
   * @param titleFont
   *          The border title font if not null, BORDER otherwise.
   * @return The new border.
   */
  static public Border createBorder(String title, Font titleFont)
  {
    TitledBorder border = BorderFactory.createTitledBorder(title);
    if (titleFont != null)
      border.setTitleFont(titleFont);
    else
      border.setTitleFont(ContextFont.BORDER.getFont());
    return border;
  }

  /**
   * Compose a bevel plus titled border.
   * @param title
   * @param titleFont
   * @param bevelBorder
   * @return
   */
  static public Border createBorder(String title, Font titleFont, int bevelBorder)
  {
    // first the bevel border
    Border innerBorder = null;
    switch (bevelBorder)
    {
      case BevelBorder.RAISED:
      case BevelBorder.LOWERED:
      {
        innerBorder = createBorder(bevelBorder);
      }
        break;
      default:
      {
        
      }
    }

    // last the titled border
    TitledBorder titledBorder = null;
    if (innerBorder == null)
      titledBorder = (TitledBorder) createBorder(title, titleFont);
    else
    {
      titledBorder = BorderFactory.createTitledBorder(innerBorder, title);
      if (titleFont != null)
        titledBorder.setTitleFont(titleFont);
      else
        titledBorder.setTitleFont(ContextFont.BORDER.getFont());
    }
    return titledBorder;
  }

  // (this class can't be instanced)
  private UtilForm()
  {
  }
}
