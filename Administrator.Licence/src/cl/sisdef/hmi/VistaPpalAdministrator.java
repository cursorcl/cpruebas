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
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.BevelBorder;

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
import net.miginfocom.swing.MigLayout;

public class VistaPpalAdministrator extends JPanel implements ActionListener // ,
                                                                             // IDongleListener
{
    static final ProductKeyProvider codeProvider = new ProductKeyProviderImpl(null);
    static final Font CODE_FONT = new Font("Lucida Console", Font.PLAIN, 14);
    private static final int PRODUCT_ID = 20;
    static final String PRODUCT_KEYSTORE = "keystore";
    static public final Logger log = Logger.getLogger(VistaPpalAdministrator.class.getName());

    /**
     * Ancho de la columna de la fecha
     */
    private static final int WIDTH1 = 80;
    /**
     * Ancho de la columna de la fecha
     */
    private static final int WIDTH2 = 250;
    /**
     * Ancho de la columna de la fecha
     */
    private static final int WIDTH3 = 250;

    // private static IDongleVerification dongleVerif = null;
    /**
     * Atributo que representa el serial.
     */
    private static final long serialVersionUID = 1L;

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

    /**
     * @param args
     */
    public static void main(String[] args) {
        UtilLineLogging.initialize(Level.INFO);

        // if(verificaDongle())
        // {
        // first the product validation
        try {
            VistaPpalAdministrator.processProductAuthentication(VistaPpalAdministrator.PRODUCT_ID);

        } catch (final Exception e) {
            // HERE fail if invalid
            VistaPpalAdministrator.log.severe(String.format("Error de Licencia", e.toString()));
            System.exit(1);
        }

        final JFrame frame = new JFrame("Administrador de Licencias");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(new VistaPpalAdministrator(), BorderLayout.CENTER);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        // }
    }

    /**
     *
     * Metodo.
     * 
     * @param productId
     * @throws Exception
     */
    static public void processProductAuthentication(int productId) throws Exception {
        final PropertyFileProductKeyStorage pfk = new PropertyFileProductKeyStorage(
                new File(VistaPpalAdministrator.PRODUCT_KEYSTORE), null);
        final SerialProvider serialProvider = new WMICSerialProvider();
        final ProductKeyValidation pkv = UtilProductKey.validateProductKey(pfk, productId, serialProvider, true);
        if (pkv == null || pkv.isUsable() == false) {
            VistaPpalAdministrator.log.severe("Unable to validate product");
            throw new Exception("Unable to validate product");
        } else {
            VistaPpalAdministrator.productIdOwner = pkv.getPk().getProductId();
            final String nroLicence = UtilLicense.bytesToHex(pkv.getPk().getXtraBytes());
            // Los cuatro primero digitos correspondel al total.
            final String nroTotal = nroLicence.substring(0, 4);
            VistaPpalAdministrator.nroTotalLicences = Integer.valueOf(nroTotal).toString();
            // Los cuatro ultimos digitos corresponden a los entregados.
            if (nroLicence.length() > 4) {
                final String nroEntre = nroLicence.substring(4, nroLicence.length());
                VistaPpalAdministrator.licences = Integer.valueOf(nroEntre).toString();

                final VistaPpalAdministrator ventana = new VistaPpalAdministrator();
                final int cantList = ventana.getManager().getAllRegister().size();
                if (Integer.valueOf(VistaPpalAdministrator.licences) != cantList) {
                    VistaPpalAdministrator.log.severe("Error en numero de licencias");
                    throw new Exception("Error en numero de licencias");
                }
            }
        }
    }
    // /**
    // * Metodo.
    // * @return
    // */
    // private static boolean verificaDongle()
    // {
    // dongleVerif = VerificationFactory.getInstance();
    // dongleVerif.subscribe(new VistaPpalAdministrator());
    //// dongleVerif.subscribe(this);
    // return true;
    // }
    // /**
    // * {@inheritDoc}.
    // */
    // @Override
    // public void SoftwareCertified()
    // {
    // System.out.println("SoftwareCertified");
    // }
    // /**
    // * {@inheritDoc}.
    // */
    // @Override
    // public void SoftwareUncertified()
    // {
    // this.setVisible(false);
    // JOptionPane.showMessageDialog(null, "No es posible ejecutar la aplicación
    // de Adm. Licencias, falta dongle");
    // System.out.println("No existe dongle ");
    // System.exit(1);
    // }
    // /**
    // * {@inheritDoc}.
    // */
    // @Override
    // public void report(DongleResultEnum result)
    // {
    // System.out.printf("\t\t\t\t\t Result Dongle: %s \n",result);
    // }

    private String serialId = null;
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

    // Campos Ocultos *************
    private JTextField text_productId = null;

    private JTextField text_serial = null;
    // *****************************

    private LicenceManager manager;

    private JScrollPane sPListData;
    private JTable tablaListData;

    private ModelTablaListData modeloListData;

    /**
     * Constructor de la clase.
     */
    public VistaPpalAdministrator() {
        setManager(new LicenceManager());
        inicialize();
    }

    /**
     * {@inheritDoc}.
     */
    @Override
    public void actionPerformed(ActionEvent event) {
        if (event.getSource() == getBtnGenerate()) {
            final String transport = getTxtTransportCode().getText();
            if (transport.trim().length() > 0) {
                final Pair<Integer, byte[]> codes = SerialTransport.retrieveTransportSequence(transport);
                getTxtProductId().setText(Integer.toString(codes.getFirst().intValue()));
                getTxtSerial().setText(UtilLicense.bytesToHex(codes.getSecond()));

                final Register register = getManager().getRegisterById(getTxtSerial().getText());
                if (register == null) {
                    generaSerial();
                } else {
                    // Existe licencia para el serial.
                    getTxtProductCode().setText(register.getLicence());
                }
            }
        }
    }

    /**
     * Metodo.
     */
    private void almacenaSerial() {
        final Register register = new Register();
        register.setSerial(getTxtSerial().getText());
        register.setLicence(serialId);
        register.setDate(System.currentTimeMillis());

        try {
            getManager().insertPlan(register);
            modeloListData.addElement(register);
            updateLicence();

        } catch (final Exception e) {
            displayMessage(e.toString());
        }
    }

    /**
     * Metodo.
     * 
     * @param message
     */
    private void displayMessage(String message) {
        label_message.setText(message);
    }

    /**
     * Metodo.
     * 
     * @param number
     * @return
     */
    private String formatValueNumber(String number) {
        String valor = number;
        if (number.length() < 4) {
            for (int index = 0; index < 4 - number.length(); index++) {
                valor = "0" + valor;
            }
        }
        return valor;
    }

    /**
     * Metodo que genera la serial para el equipo solicitado.
     */
    private void generaSerial() {
        try {
            final int productId = Integer.parseInt(getTxtProductId().getText());
            byte[] serial = null;
            serial = UtilLicense.hexToBytes(getTxtSerial().getText());

            final ProductKey pk = VistaPpalAdministrator.codeProvider.generateProductKey(productId, null, null, null);
            serialId = VistaPpalAdministrator.codeProvider.generateProductKeyToken(pk, serial);
            almacenaSerial();

            getTxtProductCode().setText(serialId);
        } catch (final Exception e) {
            displayMessage(e.toString());
        }
    }

    private JButton getBtnGenerate() {
        if (btnGenerate == null) {
            btnGenerate = new JButton("Generar");
            btnGenerate.addActionListener(this);
        }
        return btnGenerate;
    }

    /**
     * Metodo.
     * 
     * @return
     */
    private JLabel getLabel_Mensage() {
        if (label_message == null) {
            label_message = new JLabel(" ");
            label_message.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
        }
        return label_message;
    }

    /**
     * Metodo que retorna el valor de licences.
     * 
     * @return valor de licences
     */
    public String getLicences() {
        return VistaPpalAdministrator.licences;
    }

    private void getListLicences() {
        final List<Register> listadoDatos = getManager().getAllRegister();
        modeloListData.clearItems();
        if (!listadoDatos.isEmpty()) {
            for (final Register resgistro : listadoDatos) {
                modeloListData.addElement(resgistro);
            }
        }
    }

    /**
     * Metodo que retorna el valor de manager.
     * 
     * @return valor de manager
     */
    public LicenceManager getManager() {
        return manager;
    }

    /**
     * Metodo que retorna el valor de las licencias disponibles.
     * 
     * @return valor de nroTotalLicences
     */
    public String getNroLicencesDisp() {
        int licEntregadas = 0;
        if (getLicences() != null) {
            licEntregadas = Integer.valueOf(getLicences());
        }
        final int lic = Integer.valueOf(getNroTotalLicences()) - licEntregadas;
        return String.valueOf(lic);
    }

    /**
     * Metodo que retorna el valor de nroTotalLicences.
     * 
     * @return valor de nroTotalLicences
     */
    public String getNroTotalLicences() {

        return VistaPpalAdministrator.nroTotalLicences;
    }

    /**
     * Metodo.
     * 
     * @return
     */
    private JPanel getPanelLicence() {
        if (pnlLicence == null) {
            pnlLicence = new JPanel();
            pnlLicence.setLayout(new BorderLayout());
            pnlLicence.add(getPanelLicenceData(), BorderLayout.CENTER);
            pnlLicence.add(getLabel_Mensage(), BorderLayout.SOUTH);
        }
        return pnlLicence;
    }

    /**
     * Metodo.
     * 
     * @return
     */
    private JPanel getPanelLicenceData() {
        if (pnlLicenceData == null) {
            pnlLicenceData = new JPanel();
            pnlLicenceData.setLayout(new MigLayout("", "[][18.00][][][][18.00][][][grow][]", "[][][12.00][][]"));

            lblTotal = new JLabel("Total Licencias:");
            pnlLicenceData.add(lblTotal, "cell 0 0,alignx center,aligny baseline");

            lblTotalLic = new JLabel("0");
            if (getNroTotalLicences() != null && Integer.valueOf(getNroTotalLicences()) > 0) {
                lblTotalLic.setText(getNroTotalLicences());
            }
            pnlLicenceData.add(lblTotalLic, "cell 1 0,alignx center,aligny bottom");

            lblLicenciasDisponibles = new JLabel("Licencias Disponibles:");
            pnlLicenceData.add(lblLicenciasDisponibles, "cell 6 0,growx");

            lblNroDisponible = new JLabel("0");
            if (getNroLicencesDisp() != null && Integer.valueOf(getNroLicencesDisp()) > 0) {
                lblNroDisponible.setText(getNroLicencesDisp());
            }
            pnlLicenceData.add(lblNroDisponible, "cell 7 0,alignx center");

            lblToken = new JLabel("Token:");
            pnlLicenceData.add(lblToken, "cell 0 1");
            pnlLicenceData.add(getTxtTransportCode(), "cell 1 1 8 1");

            pnlLicenceData.add(getBtnGenerate(), "cell 9 1");

            lblLicencia = new JLabel("Licencia:");
            pnlLicenceData.add(lblLicencia, "cell 0 3");

            pnlLicenceData.add(getTxtProductCode(), "cell 1 3 9 1");
        }
        return pnlLicenceData;
    }

    /**
     * Metodo.
     * 
     * @return
     */
    private JPanel getPanelListData() {
        if (pnlListData == null) {
            pnlListData = new JPanel();
            pnlListData.setLayout(new BorderLayout(0, 0));
            pnlListData.add(getSPListData(), BorderLayout.CENTER);
        }
        return pnlListData;
    }

    /**
     * Metodo que retorna el panel de scroll que contiene la tabla de listado de
     * partes de fuerza.
     *
     * @return El scroll panel.
     */
    private JScrollPane getSPListData() {
        if (sPListData == null) {
            sPListData = new JScrollPane();
            sPListData.setViewportView(getTablaPartesFuerza());
        }
        return sPListData;
    }

    /**
     * Metodo.
     * 
     * @return
     */
    private JTabbedPane getTabbesPane() {
        if (tabbedPane == null) {
            tabbedPane = new JTabbedPane(SwingConstants.TOP);
            // this.tabbedPane.setBorder(new TitledBorder(""));
            tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
            tabbedPane.addTab("Datos Licencia", null, getPanelLicence(), null);
            tabbedPane.addTab("Licencias Entregadas", null, getPanelListData(), null);
        }
        return tabbedPane;
    }

    /**
     * Metodo que obtiene la tabla de el listado de partes de fuerza.
     *
     * @return La tabla de el listado de los partes de fuerza.
     */
    private JTable getTablaPartesFuerza() {
        if (tablaListData == null) {
            tablaListData = new JTable();
            modeloListData = new ModelTablaListData();
            tablaListData.setModel(modeloListData);
            tablaListData.getColumnModel().getColumn(0).setCellRenderer(new DateCellRenderer());
            tablaListData.getColumnModel().getColumn(0).setPreferredWidth(VistaPpalAdministrator.WIDTH1);
            tablaListData.getColumnModel().getColumn(0).setMaxWidth(VistaPpalAdministrator.WIDTH1);
            tablaListData.getColumnModel().getColumn(0).setMinWidth(VistaPpalAdministrator.WIDTH1);
            tablaListData.getColumnModel().getColumn(1).setCellRenderer(new DatosCellRenderer());
            tablaListData.getColumnModel().getColumn(1).setPreferredWidth(VistaPpalAdministrator.WIDTH2);
            tablaListData.getColumnModel().getColumn(1).setMinWidth(VistaPpalAdministrator.WIDTH2);
            tablaListData.getColumnModel().getColumn(1).setMaxWidth(VistaPpalAdministrator.WIDTH2);
            tablaListData.getColumnModel().getColumn(2).setCellRenderer(new DatosCellRenderer());
            tablaListData.getColumnModel().getColumn(2).setPreferredWidth(VistaPpalAdministrator.WIDTH3);
            tablaListData.getColumnModel().getColumn(2).setMinWidth(VistaPpalAdministrator.WIDTH3);
            tablaListData.getColumnModel().getColumn(2).setMaxWidth(VistaPpalAdministrator.WIDTH3);
        }
        return tablaListData;
    }

    /**
     * Metodo.
     * 
     * @return
     */
    private JTextField getTxtProductCode() {
        if (txtProductCode == null) {
            txtProductCode = new JTextField(72);
            txtProductCode.setEditable(false);
            txtProductCode.setBorder(null);
            txtProductCode.setForeground(UIManager.getColor("Label.foreground"));
            txtProductCode.setBorder(BorderFactory.createEtchedBorder());
            txtProductCode.setFont(VistaPpalAdministrator.CODE_FONT);
        }
        return txtProductCode;
    }

    /**
     * Metodo.
     * 
     * @return
     */
    private JTextField getTxtProductId() {
        if (text_productId == null) {
            text_productId = new JTextField(20);
            text_productId.setFont(VistaPpalAdministrator.CODE_FONT);
        }
        return text_productId;
    }

    /**
     * Metodo.
     * 
     * @return
     */
    private JTextField getTxtSerial() {
        if (text_serial == null) {
            text_serial = new JTextField(20);
            text_serial.setFont(VistaPpalAdministrator.CODE_FONT);
        }
        return text_serial;
    }

    /**
     * Metodo.
     * 
     * @return
     */
    private JTextField getTxtTransportCode() {
        if (txtTransportCode == null) {
            txtTransportCode = new JTextField(50);
            txtTransportCode.setFont(VistaPpalAdministrator.CODE_FONT);
        }
        return txtTransportCode;
    }

    /**
     * Metodo.
     */
    private void inicialize() {
        setLayout(new BorderLayout(0, 0));
        setPreferredSize(new Dimension(600, 350));
        add(getTabbesPane(), BorderLayout.CENTER);
        getListLicences();
    }

    /**
     * Metodo que asigna el valor de manager.
     * 
     * @param manager
     *            El valor de manager
     */
    public void setManager(LicenceManager manager) {
        this.manager = manager;
    }

    private void updateLicence() {
        final SerialProvider serialProvider = new WMICSerialProvider();
        int cantidad = 1;
        if (VistaPpalAdministrator.licences != null) {
            cantidad = cantidad + Integer.valueOf(VistaPpalAdministrator.licences);
        }
        VistaPpalAdministrator.licences = String.valueOf(cantidad);
        lblNroDisponible.setText(getNroLicencesDisp());

        final String ambos = formatValueNumber(VistaPpalAdministrator.nroTotalLicences)
                .concat(formatValueNumber(String.valueOf(cantidad)));
        final byte[] nroLicences = UtilLicense.hexToBytes(ambos);

        // Se genera la nueva licencia para el administrador, incrementando el
        // valor de las generadas.
        final ProductKey pk = VistaPpalAdministrator.codeProvider
                .generateProductKey(VistaPpalAdministrator.productIdOwner, null, null, nroLicences);
        final String newLicenceOwner = VistaPpalAdministrator.codeProvider.generateProductKeyToken(pk,
                serialProvider.getSerial());
        final PropertyFileProductKeyStorage pks = new PropertyFileProductKeyStorage(
                new File(VistaPpalAdministrator.PRODUCT_KEYSTORE), null);
        pks.storeProductKey(newLicenceOwner);
    }

}
