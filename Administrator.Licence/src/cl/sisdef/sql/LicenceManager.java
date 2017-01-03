package cl.sisdef.sql;

import java.io.ByteArrayInputStream;
import java.nio.ByteBuffer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import cl.sisdef.license.util.UtilLicense;
import cl.sisdef.model.Register;

/**
 * Maneja las interacciones con la base de datos. Particularmete tabla
 *
 * @author Jonnattan G.
 * @since 16/04/2013
 * @version 1.0 Copyright(c) - SISDEF
 */

public class LicenceManager extends SQLiteConnector
{
    private Connection connect = null;

    static public final String DEFAULT_CRYPT_PASSWORD = "MARIA TENIA UN CORDERITO BLANCO, OTRO NEGRO Y OTRO PINTADO";

    private final static String TABLE_DATA_LICENCE = "listgenerate";

    private String password;
    /**
     * Constructor de la clase.
     */
    public LicenceManager()
    {
        this.connect = getConnection();
        this.password = (password != null) ? password : DEFAULT_CRYPT_PASSWORD;
    }

    /**
     * Obtiene lista de licencias otorgadas.
     */
    public List<Register>  getAllRegister()
    {
        final List<Register> listData = new ArrayList<Register>();

        final String stSel = "SELECT serial, licence, date FROM " + TABLE_DATA_LICENCE;
        try
        {
            final PreparedStatement pstmt = this.connect.prepareStatement(stSel);
            final ResultSet rsC = pstmt.executeQuery();
            while (rsC.next())
            {
                final Register tuple = new Register();

                byte[] bytes = rsC.getBytes("serial");

                if (bytes != null)
                {
                    String valor = decodeData(bytes);
                    tuple.setSerial(valor);
                }

                bytes = rsC.getBytes("licence");
                if (bytes != null)
                {
                    String valor = decodeData(bytes);
                    tuple.setLicence(valor);
                }

                tuple.setDate(rsC.getLong("date"));

                listData.add(tuple);
            }
            pstmt.close();
            rsC.close();
            return listData;
        }
        catch (final Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }
    /**
     * Obtiene lista de licencias otorgadas.
     */
    public Register getRegisterById(String serial)
    {
        Register data = null;

        final String stSel = "SELECT serial, licence, date FROM " + TABLE_DATA_LICENCE + " WHERE serial = ?";
        try
        {
            final PreparedStatement pstmt = this.connect.prepareStatement(stSel);
            byte[] serialByteArray = serial.getBytes();
            ByteBuffer bb = ByteBuffer.allocate(serialByteArray.length);
            bb.put(serialByteArray);
            pstmt.setBytes(1, bb.array());

            final ResultSet rsC = pstmt.executeQuery();
            while (rsC.next())
            {
                data = new Register();

                byte[] bytes = rsC.getBytes("serial");
                if (bytes != null)
                {
                    String valor = new String(bytes, 0, bytes.length);
                    data.setSerial(valor);
                }

                bytes = rsC.getBytes("licence");
                if (bytes != null)
                {
                    String valor = new String(bytes, 0, bytes.length);
                    data.setLicence(valor);
                }

                data.setDate(rsC.getLong("date"));
            }
            pstmt.close();
            rsC.close();
            return data;
        }
        catch (final Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }
    /**
     * @param newLicence
     */
    public void insertPlan(final Register newLicence)
    {
        final String sqlSentence = "INSERT INTO " + TABLE_DATA_LICENCE
                + " ( serial, licence, date ) "
                + " VALUES ( ?, ?, ? )";

        try
        {
            final PreparedStatement pstmt = this.connect.prepareStatement(sqlSentence);

            byte[] serialByteArray = encodeData(newLicence.getSerial().getBytes());
            pstmt.setBinaryStream(1,new ByteArrayInputStream(serialByteArray),serialByteArray.length);

            byte[] licenceByteArray = encodeData(newLicence.getLicence().getBytes());
            pstmt.setBinaryStream(2,new ByteArrayInputStream(licenceByteArray),licenceByteArray.length);

            pstmt.setLong(3, newLicence.getDate());

            pstmt.close();
        }
        catch (final Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Metodo que encapsula la data.
     * @param valor
     *      valor a encapsular
     * @return Respuesta.
     */
    private byte[] encodeData(byte[] valor)
    {
//        byte[] scrambled = UtilLicense.scramble(valor, null);
        byte[] glyph = UtilLicense.encrypt(valor, this.password.getBytes());
        return glyph;
    }

    /**
     * Metodo.
     * @param valor
     * @return
     */
    private String decodeData(byte[] valor)
    {
        byte[] glyphBytes = UtilLicense.decrypt(valor, this.password.getBytes());
        String retorno = new String(glyphBytes, 0, glyphBytes.length);
        return retorno;
    }
}
