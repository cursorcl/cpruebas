package cl.sisdef.hmi.view;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.logging.Level;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.BevelBorder;

import cl.sisdef.license.ProductKey;
import cl.sisdef.license.ProductKeyProvider;
import cl.sisdef.license.SerialProvider;
import cl.sisdef.license.impl.ProductKeyProviderImpl;
import cl.sisdef.license.impl.SerialTransport;
import cl.sisdef.license.impl.WMICSerialProvider;
import cl.sisdef.license.panel.specs.LicenseSpecContainer;
import cl.sisdef.license.panel.specs.LicenseSpecsPanel;
import cl.sisdef.license.panel.specs.SerialSpecType;
import cl.sisdef.license.util.UtilLicense;
import cl.sisdef.log.UtilLineLogging;
import cl.sisdef.util.Pair;

/**
 * This panel should appear when no product key token is detected.<br />
 * The panel must request the product key and provide the platform sequence (to
 * be sent to us) to generate a new product key.
 *
 * @author ayachan
 */
public class ProductKeyPanel extends JPanel implements ActionListener
{
  private static final long serialVersionUID = 4891726853609370659L;

  static final String TRANSPORT_TEXT = "Transport code";
  static final String TRANSPORT_TRANSLATE_TEXT = "Translate";

  static final String PRODUCT_ID_TEXT = "Product ID";
  static final String SERIAL_TEXT = "Serial";
  static final String NROLICENCE_TEXT = "Nro. Licencias";

  static final String SERIAL_RETRIEVE_TEXT = "Retrieve";

  static final String GENERATE_TEXT = "Generate";

  static final String PRODUCT_CODE_TEXT = "Product code";

  static final String USE_SERIAL_LBL = "Use Serial Data";
  static final String DATE_START_TEXT = "Start date";
  static final String DATE_END_TEXT = "End date";

  static final Font CODE_FONT = new Font("Lucida Console", Font.PLAIN, 14);

  LicenseSpecsPanel licenseSpecsPanel = null;

  JTextField text_transportCode = null;
  JButton button_translateCode = null;

  JTextField text_productId = null;
  JTextField text_serial = null;

  JTextField txtNroLicence = null;


  JButton button_serialRetrieve = null;

  JButton button_generate = null;

  JTextField label_ProductCode = null;

  JLabel label_message = null;

  private final ProductKey ex_pk;

  public ProductKeyPanel()
  {
    this(null);
  }

  public ProductKeyPanel(ProductKey pk)
  {
    super(new BorderLayout());
    this.ex_pk = pk;
    initialize();
  }

  void initialize()
  {
    JPanel contentPanel = new JPanel();
    BoxLayout layout = new BoxLayout(contentPanel, BoxLayout.Y_AXIS);
    contentPanel.setLayout(layout);

    /*
     * transport code
     */

    text_transportCode = new JTextField(50);
    text_transportCode.setBorder(BorderFactory
        .createTitledBorder(TRANSPORT_TEXT));
    text_transportCode.setFont(CODE_FONT);
    contentPanel.add(text_transportCode);

    JPanel translatePanel = new JPanel(new BorderLayout());
    button_translateCode = new JButton(TRANSPORT_TRANSLATE_TEXT);
    button_translateCode.addActionListener(this);
    translatePanel.add(button_translateCode, BorderLayout.EAST);
    contentPanel.add(translatePanel);

    /*
     * product id and serial
     */

    JPanel serialPanel = new JPanel(new GridLayout(1, 2));

    text_productId = new JTextField(20);
    text_productId.setBorder(BorderFactory.createTitledBorder(PRODUCT_ID_TEXT));
    text_productId.setFont(CODE_FONT);
    serialPanel.add(text_productId);

    txtNroLicence = new JTextField(20);
    txtNroLicence.setBorder(BorderFactory.createTitledBorder(NROLICENCE_TEXT));
    txtNroLicence.setFont(CODE_FONT);
    serialPanel.add(txtNroLicence);


    text_serial = new JTextField(20);
    text_serial.setBorder(BorderFactory.createTitledBorder(SERIAL_TEXT));
    text_serial.setFont(CODE_FONT);
    serialPanel.add(text_serial);

    contentPanel.add(serialPanel);

    JPanel serialRetrievePanel = new JPanel(new BorderLayout());
    button_serialRetrieve = new JButton(SERIAL_RETRIEVE_TEXT);
    button_serialRetrieve.addActionListener(this);
    serialRetrievePanel.add(button_serialRetrieve, BorderLayout.EAST);
    contentPanel.add(serialRetrievePanel);

    /*
     * dates
     */

    licenseSpecsPanel = new LicenseSpecsPanel(USE_SERIAL_LBL, DATE_START_TEXT, DATE_END_TEXT, null, new SerialSpecType[] {SerialSpecType.AUTOSET_SERIAL});
    if(ex_pk != null)
    {
      licenseSpecsPanel.setStartDate(ex_pk.getInitialDate());
      licenseSpecsPanel.setEndDate(ex_pk.getFinalDate());
    }
    contentPanel.add(licenseSpecsPanel);

    /*
     * generation
     */

    JPanel generatePanel = new JPanel(new BorderLayout());
    button_generate = new JButton(GENERATE_TEXT);
    button_generate.addActionListener(this);
    generatePanel.add(button_generate, BorderLayout.EAST);
    contentPanel.add(generatePanel);

    label_ProductCode = new JTextField(72);
    label_ProductCode.setEditable(false);
    label_ProductCode.setBorder(null);
    label_ProductCode.setForeground(UIManager.getColor("Label.foreground"));
    label_ProductCode.setFont(CODE_FONT);
    label_ProductCode.setBorder(BorderFactory
        .createTitledBorder(PRODUCT_CODE_TEXT));
    contentPanel.add(label_ProductCode);

    add(contentPanel, BorderLayout.CENTER);

    label_message = new JLabel(" ");
    label_message.setBorder(BorderFactory
        .createBevelBorder(BevelBorder.LOWERED));
    add(label_message, BorderLayout.SOUTH);
  }

  void displayMessage(String message)
  {
    label_message.setText(message);
  }

  static final SerialProvider serialProvider = new WMICSerialProvider();
  static final ProductKeyProvider codeProvider = new ProductKeyProviderImpl(
      null);

  @Override
  public void actionPerformed(ActionEvent event)
  {
    if (event.getSource() == button_translateCode)
    {
      try
      {
        String transport = text_transportCode.getText();
        Pair<Integer, byte[]> codes = SerialTransport
            .retrieveTransportSequence(transport);
        text_productId.setText(Integer.toString(codes.getFirst().intValue()));
        text_serial.setText(UtilLicense.bytesToHex(codes.getSecond()));
      }
      catch (Exception e)
      {
        displayMessage(e.toString());
      }
    }
    else if (event.getSource() == button_serialRetrieve)
    {
      try
      {
        text_serial.setText(UtilLicense.bytesToHex(serialProvider.getSerial()));
      }
      catch (Exception e)
      {
        displayMessage(e.toString());
      }
    }
    else if (event.getSource() == button_generate)
    {
      try
      {
        LicenseSpecContainer lsc = licenseSpecsPanel.getLicenseSpecs();
        int productId = Integer.parseInt(text_productId.getText());
        Date initialDate = lsc.isStartDateSelected() ? lsc.getStartDate()
            : null;
        Date endDate = lsc.isEndDateSelected() ? lsc.getEndDate() : null;

        byte[] serial = null;
        serial = UtilLicense.hexToBytes(text_serial.getText());

        byte[] nroLicences = null;
        String valor = txtNroLicence.getText();
        if(txtNroLicence.getText().length() < 4)
        {
            for(int index=0 ; index<(4 - txtNroLicence.getText().length()); index++)
            {
                valor = "0" + valor;
            }
        }
        nroLicences = UtilLicense.hexToBytes(valor);

        ProductKey pk = codeProvider.generateProductKey(productId, initialDate,
            endDate, nroLicences);
        label_ProductCode.setText(codeProvider.generateProductKeyToken(pk,
            serial));
      }
      catch (Exception e)
      {
        displayMessage(e.toString());
      }
    }

  }

  /**
   * @param args
   */
  public static void main(String[] args)
  {
    UtilLineLogging.initialize(Level.INFO);

    JFrame frame = new JFrame("Product Key Manager");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.getContentPane().add(new ProductKeyPanel(), BorderLayout.CENTER);
    frame.pack();
    frame.setVisible(true);
  }
}

