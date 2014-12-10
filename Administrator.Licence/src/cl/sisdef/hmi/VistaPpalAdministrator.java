package cl.sisdef.hmi;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.BevelBorder;

import net.miginfocom.swing.MigLayout;
import cl.sisdef.license.ProductKey;
import cl.sisdef.license.ProductKeyProvider;
import cl.sisdef.license.ProductKeyValidation;
import cl.sisdef.license.SerialProvider;
import cl.sisdef.license.UtilProductKey;
import cl.sisdef.license.impl.ProductKeyProviderImpl;
import cl.sisdef.license.impl.PropertyFileProductKeyStorage;
import cl.sisdef.license.impl.SerialTransport;
import cl.sisdef.license.impl.WMICSerialProvider;
import cl.sisdef.license.util.UtilLicense;
import cl.sisdef.log.UtilLineLogging;
import cl.sisdef.model.Register;
import cl.sisdef.model.columns.DateCellRenderer;
import cl.sisdef.model.columns.DatosCellRenderer;
import cl.sisdef.model.table.ModelTablaListData;
import cl.sisdef.sql.LicenceManager;
import cl.sisdef.util.Pair;

public class VistaPpalAdministrator extends JPanel implements ActionListener //, IDongleListener
{
    static final ProductKeyProvider codeProvider = new ProductKeyProviderImpl(null);
    static final Font CODE_FONT = new Font("Lucida Console", Font.PLAIN, 14);
    private static final int PRODUCT_ID = 20;
    static final String PRODUCT_KEYSTORE = "keystore";
    static public final Logger log = Logger.getLogger(VistaPpalAdministrator.class.getName());

    /**
     * Ancho de la columna de la fecha
     */
    private static final int             WIDTH1                  = 80;
    /**
     * Ancho de la columna de la fecha
     */
    private static final int             WIDTH2                  = 250;
    /**
     * Ancho de la columna de la fecha
     */
    private static final int             WIDTH3                  = 250;

    private String serialId = null;

//    private static IDongleVerification dongleVerif = null;
    /**
     * Atributo que representa el serial.
     */
    private static final long serialVersionUID = 1L;
    private JLabel lblTotal;
    private JLabel lblTotalLic;
    private JLabel lblLicenciasDisponibles;
    private JLabel lblNroDisponible;

    private JTabbedPane tabbedPane = null;
    private JPanel pnlLicence;
    private JPanel pnlLicenceData;
    private JPanel pnlListData;
    private JButton btnGenerate = null;
    private JTextField txtTransportCode = null;
    private JTextField txtProductCode = null;
    private JLabel lblToken;
    private JLabel lblLicencia;
    private JLabel label_message = null;
    /**
     * Atributo que representa el total de licencias.
     */
    private static String nroTotalLicences;
    /**
     * Atributo que representa las licencias que se han generado.
     */
    private static String licences;
    /**
     * Atributo que representa el productIDde la aplicación.
     */
    private static int productIdOwner;

    // Campos Ocultos *************
    private JTextField text_productId = null;
    private JTextField text_serial = null;
    //*****************************

    private LicenceManager manager;

    private JScrollPane sPListData;

    private JTable tablaListData;

    private ModelTablaListData modeloListData;
    /**
     * Constructor de la clase.
     */
    public VistaPpalAdministrator()
    {
        setManager(new LicenceManager());
        inicialize();
    }

    /**
     * Metodo.
     */
    private void inicialize()
    {
        setLayout(new BorderLayout(0, 0));
        setPreferredSize(new Dimension(600, 350));
        add(getTabbesPane(), BorderLayout.CENTER);
        getListLicences();
    }

    private void getListLicences()
    {
        final List<Register> listadoDatos = (List<Register>) getManager().getAllRegister();
        this.modeloListData.clearItems();
        if ( !listadoDatos.isEmpty() )
        {
            for ( final Register resgistro : listadoDatos )
            {
                this.modeloListData.addElement( resgistro );
            }
        }
    }

    /**
     * Metodo.
     * @return
     */
    private JTabbedPane getTabbesPane()
    {
        if(this.tabbedPane == null)
        {
            this.tabbedPane = new JTabbedPane(JTabbedPane.TOP);
//            this.tabbedPane.setBorder(new TitledBorder(""));
            this.tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
            this.tabbedPane.addTab("Datos Licencia", null, getPanelLicence(), null);
            this.tabbedPane.addTab("Licencias Entregadas", null, getPanelListData(), null);
        }
        return this.tabbedPane;
    }
    /**
     * Metodo.
     * @return
     */
    private JPanel getPanelLicence()
    {
        if(this.pnlLicence == null)
        {
            this.pnlLicence = new JPanel();
            this.pnlLicence.setLayout(new BorderLayout());
            this.pnlLicence.add(getPanelLicenceData(), BorderLayout.CENTER);
            this.pnlLicence.add(getLabel_Mensage(), BorderLayout.SOUTH);
        }
        return this.pnlLicence;
    }
    /**
     * Metodo.
     * @return
     */
    private JPanel getPanelLicenceData()
    {
        if(this.pnlLicenceData == null)
        {
            this.pnlLicenceData = new JPanel();
            this.pnlLicenceData.setLayout(new MigLayout("", "[][18.00][][][][18.00][][][grow][]", "[][][12.00][][]"));

            lblTotal = new JLabel("Total Licencias:");
            this.pnlLicenceData.add(lblTotal, "cell 0 0,alignx center,aligny baseline");

            lblTotalLic = new JLabel("0");
            if(getNroTotalLicences() != null && (Integer.valueOf(getNroTotalLicences()) > 0))
            {
                lblTotalLic.setText(getNroTotalLicences());
            }
            this.pnlLicenceData.add(lblTotalLic, "cell 1 0,alignx center,aligny bottom");

            lblLicenciasDisponibles = new JLabel("Licencias Disponibles:");
            this.pnlLicenceData.add(lblLicenciasDisponibles, "cell 6 0,growx");

            lblNroDisponible = new JLabel("0");
            if(getNroLicencesDisp() != null && (Integer.valueOf(getNroLicencesDisp()) > 0))
            {
                lblNroDisponible.setText(getNroLicencesDisp());
            }
            this.pnlLicenceData.add(lblNroDisponible, "cell 7 0,alignx center");

            lblToken = new JLabel("Token:");
            this.pnlLicenceData.add(lblToken, "cell 0 1");
            this.pnlLicenceData.add(getTxtTransportCode(), "cell 1 1 8 1");

            this.pnlLicenceData.add(getBtnGenerate(), "cell 9 1");

            lblLicencia = new JLabel("Licencia:");
            this.pnlLicenceData.add(lblLicencia, "cell 0 3");

            this.pnlLicenceData.add(getTxtProductCode(), "cell 1 3 9 1");
        }
        return this.pnlLicenceData;
    }
    /**
     * Metodo.
     * @return
     */
    private JLabel getLabel_Mensage()
    {
        if(this.label_message == null)
        {
            this.label_message = new JLabel(" ");
            this.label_message.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
        }
        return this.label_message;
    }

    /**
     * Metodo.
     * @return
     */
    private JPanel getPanelListData()
    {
        if(this.pnlListData == null)
        {
            this.pnlListData = new JPanel();
            this.pnlListData.setLayout(new BorderLayout(0, 0));
            this.pnlListData.add(getSPListData(), BorderLayout.CENTER);
        }
        return this.pnlListData;
    }

    /**
     * Metodo que retorna el panel de scroll que contiene la tabla de listado de partes de fuerza.
     *
     * @return El scroll panel.
     */
    private JScrollPane getSPListData()
    {
        if ( this.sPListData == null )
        {
            this.sPListData = new JScrollPane();
            this.sPListData.setViewportView( getTablaPartesFuerza() );
        }
        return this.sPListData;
    }

    /**
     * Metodo que obtiene la tabla de el listado de partes de fuerza.
     *
     * @return La tabla de el listado de los partes de fuerza.
     */
    private JTable getTablaPartesFuerza()
    {
        if ( this.tablaListData == null )
        {
            this.tablaListData = new JTable();
            this.modeloListData = new ModelTablaListData();
            this.tablaListData.setModel( this.modeloListData );
            this.tablaListData.getColumnModel().getColumn( 0 ).setCellRenderer( new DateCellRenderer() );
            this.tablaListData.getColumnModel().getColumn( 0 ).setPreferredWidth( WIDTH1 );
            this.tablaListData.getColumnModel().getColumn( 0 ).setMaxWidth( WIDTH1 );
            this.tablaListData.getColumnModel().getColumn( 0 ).setMinWidth( WIDTH1 );
            this.tablaListData.getColumnModel().getColumn( 1 ).setCellRenderer( new DatosCellRenderer() );
            this.tablaListData.getColumnModel().getColumn( 1 ).setPreferredWidth( WIDTH2 );
            this.tablaListData.getColumnModel().getColumn( 1 ).setMinWidth( WIDTH2 );
            this.tablaListData.getColumnModel().getColumn( 1 ).setMaxWidth( WIDTH2 );
            this.tablaListData.getColumnModel().getColumn( 2 ).setCellRenderer( new DatosCellRenderer() );
            this.tablaListData.getColumnModel().getColumn( 2 ).setPreferredWidth( WIDTH3 );
            this.tablaListData.getColumnModel().getColumn( 2 ).setMinWidth( WIDTH3 );
            this.tablaListData.getColumnModel().getColumn( 2 ).setMaxWidth( WIDTH3 );
        }
        return this.tablaListData;
    }
    /**
     * Metodo.
     * @return
     */
    private JTextField getTxtTransportCode()
    {
        if(this.txtTransportCode == null)
        {
            this.txtTransportCode = new JTextField(50);
            this.txtTransportCode.setFont(CODE_FONT);
        }
        return this.txtTransportCode;
    }

    /**
     * Metodo.
     * @return
     */
    private JTextField getTxtProductCode()
    {
        if(this.txtProductCode == null)
        {
            this.txtProductCode = new JTextField(72);
            this.txtProductCode.setEditable(false);
            this.txtProductCode.setBorder(null);
            this.txtProductCode.setForeground(UIManager.getColor("Label.foreground"));
            this.txtProductCode.setBorder(BorderFactory.createEtchedBorder());
            this.txtProductCode.setFont(CODE_FONT);
        }
        return this.txtProductCode;
    }

    private JButton getBtnGenerate()
    {
        if(this.btnGenerate == null)
        {
            this.btnGenerate = new JButton("Generar");
            this.btnGenerate.addActionListener(this);
        }
        return this.btnGenerate;
    }

    /**
     * {@inheritDoc}.
     */
    @Override
    public void actionPerformed(ActionEvent event)
    {
        if (event.getSource() == getBtnGenerate())
        {
            String transport = getTxtTransportCode().getText();
            if(transport.trim().length() > 0)
            {
                Pair<Integer, byte[]> codes = SerialTransport.retrieveTransportSequence(transport);
                getTxtProductId().setText(Integer.toString(codes.getFirst().intValue()));
                getTxtSerial().setText(UtilLicense.bytesToHex(codes.getSecond()));

                Register register = getManager().getRegisterById(getTxtSerial().getText());
                if(register == null)
                {
                    generaSerial();
                }
                else
                {
                    // Existe licencia para el serial.
                    getTxtProductCode().setText(register.getLicence());
                }
            }
        }
    }
    /**
     * Metodo.
     */
    private void almacenaSerial()
    {
        Register register = new Register();
        register.setSerial(getTxtSerial().getText());
        register.setLicence(this.serialId);
        register.setDate(System.currentTimeMillis());

        try
        {
            getManager().insertPlan(register);
            this.modeloListData.addElement(register);
            updateLicence();

        }
        catch (Exception e)
        {
            displayMessage(e.toString());
        }
    }

    private void updateLicence()
    {
        SerialProvider serialProvider = new WMICSerialProvider();
        int cantidad = 1;
        if(licences != null)
        {
            cantidad = cantidad +  Integer.valueOf(licences);
        }
        licences = String.valueOf(cantidad);
        lblNroDisponible.setText(getNroLicencesDisp());

        String ambos = formatValueNumber(nroTotalLicences).concat(formatValueNumber(String.valueOf(cantidad)));
        byte[] nroLicences = UtilLicense.hexToBytes(ambos);

        // Se genera la nueva licencia para el administrador, incrementando el valor de las generadas.
        ProductKey pk = codeProvider.generateProductKey(productIdOwner, null, null, nroLicences);
        String newLicenceOwner = codeProvider.generateProductKeyToken(pk,serialProvider.getSerial());
        PropertyFileProductKeyStorage pks = new PropertyFileProductKeyStorage(new File(PRODUCT_KEYSTORE), null);
        pks.storeProductKey(newLicenceOwner);
    }

    /**
     * Metodo que genera la serial para el equipo solicitado.
     */
    private void generaSerial()
    {
        try
        {
            int productId = Integer.parseInt(getTxtProductId().getText());
            byte[] serial = null;
            serial = UtilLicense.hexToBytes(getTxtSerial().getText());

            ProductKey pk = codeProvider.generateProductKey(productId, null,null, null);
            this.serialId = codeProvider.generateProductKeyToken(pk,serial);
            almacenaSerial();

            getTxtProductCode().setText(this.serialId);
        }
        catch (Exception e)
        {
            displayMessage(e.toString());
        }
    }

    /**
     * Metodo.
     * @return
     */
    private JTextField getTxtProductId()
    {
        if(this.text_productId == null)
        {
            text_productId = new JTextField(20);
            text_productId.setFont(CODE_FONT);
         }
        return this.text_productId;
    }
    /**
     * Metodo.
     * @return
     */
    private JTextField getTxtSerial()
    {
        if(this.text_serial == null)
        {
            text_serial = new JTextField(20);
            text_serial.setFont(CODE_FONT);
         }
        return this.text_serial;
    }
    /**
     * Metodo.
     * @param message
     */
    private void displayMessage(String message)
    {
      label_message.setText(message);
    }

    /**
     * @param args
     */
    public static void main(String[] args)
    {
      UtilLineLogging.initialize(Level.INFO);

//      if(verificaDongle())
//      {
      // first the product validation
          try
          {
              processProductAuthentication(PRODUCT_ID);

          }
          catch (Exception e)
          {
            // HERE fail if invalid
              log.severe(String.format("Error de Licencia", e.toString()));
              System.exit(1);
          }

          JFrame frame = new JFrame("Administrador de Licencias");
          frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
          frame.getContentPane().add(new VistaPpalAdministrator(), BorderLayout.CENTER);
          frame.pack();
          frame.setLocationRelativeTo(null);
          frame.setVisible(true);
//      }
    }
    /**
    *
    * Metodo.
    * @param productId
    * @throws Exception
    */
   static public void processProductAuthentication(int productId) throws Exception
   {
         PropertyFileProductKeyStorage pfk = new PropertyFileProductKeyStorage(new File(PRODUCT_KEYSTORE), null);
         SerialProvider serialProvider = new WMICSerialProvider();
         ProductKeyValidation pkv = UtilProductKey.validateProductKey(pfk, productId, serialProvider, true);
         if (pkv == null || pkv.isUsable() == false)
         {
             log.severe("Unable to validate product");
             throw new Exception("Unable to validate product");
         }
         else
         {
             productIdOwner = pkv.getPk().getProductId();
             String nroLicence = UtilLicense.bytesToHex(pkv.getPk().getXtraBytes());
             //Los cuatro primero digitos correspondel al total.
             String nroTotal = nroLicence.substring(0, 4);
             nroTotalLicences = Integer.valueOf(nroTotal).toString();
             //Los cuatro ultimos digitos corresponden a los entregados.
             if(nroLicence.length() > 4)
             {
                 String nroEntre = nroLicence.substring(4, nroLicence.length());
                 licences = Integer.valueOf(nroEntre).toString();

                 VistaPpalAdministrator ventana = new VistaPpalAdministrator();
                 int cantList = ventana.getManager().getAllRegister().size();
                 if(Integer.valueOf(licences) != cantList)
                 {
                     log.severe("Error en numero de licencias");
                     throw new Exception("Error en numero de licencias");
                 }
             }
         }
   }
//    /**
//     * Metodo.
//     * @return
//     */
//    private static boolean verificaDongle()
//    {
//        dongleVerif = VerificationFactory.getInstance();
//        dongleVerif.subscribe(new VistaPpalAdministrator());
////        dongleVerif.subscribe(this);
//        return true;
//    }
//    /**
//     * {@inheritDoc}.
//     */
//    @Override
//    public void SoftwareCertified()
//    {
//        System.out.println("SoftwareCertified");
//    }
//    /**
//     * {@inheritDoc}.
//     */
//    @Override
//    public void SoftwareUncertified()
//    {
//        this.setVisible(false);
//        JOptionPane.showMessageDialog(null, "No es posible ejecutar la aplicación de Adm. Licencias, falta dongle");
//        System.out.println("No existe dongle ");
//        System.exit(1);
//    }
//    /**
//     * {@inheritDoc}.
//     */
//    @Override
//    public void report(DongleResultEnum result)
//    {
//        System.out.printf("\t\t\t\t\t Result Dongle: %s \n",result);
//    }

    /**
     * Metodo que retorna el valor de nroTotalLicences.
     * @return valor de nroTotalLicences
     */
    public String getNroTotalLicences()
    {

        return nroTotalLicences;
    }

    /**
     * Metodo que retorna el valor de las licencias disponibles.
     * @return valor de nroTotalLicences
     */
    public String getNroLicencesDisp()
    {
        int licEntregadas = 0;
        if(getLicences() != null)
        {
            licEntregadas = Integer.valueOf(getLicences());
        }
        int lic = Integer.valueOf(getNroTotalLicences()) - licEntregadas;
        return String.valueOf(lic);
    }

    /**
     * Metodo que retorna el valor de licences.
     * @return valor de licences
     */
    public String getLicences()
    {
        return licences;
    }

    /**
     * Metodo que retorna el valor de manager.
     * @return valor de manager
     */
    public LicenceManager getManager()
    {
        return manager;
    }

    /**
     * Metodo que asigna el valor de manager.
     * @param manager El valor de manager
     */
    public void setManager(LicenceManager manager)
    {
        this.manager = manager;
    }
    /**
     * Metodo.
     * @param number
     * @return
     */
    private String formatValueNumber(String number)
    {
        String valor = number;
        if(number.length() < 4)
        {
            for(int index=0 ; index<(4 - number.length()); index++)
            {
                valor = "0" + valor;
            }
        }
        return valor;
    }

}
