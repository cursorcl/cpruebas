package cl.eos.external.files.utils;

import org.apache.poi.ss.usermodel.Cell;

public class Constants {
	public static final int NRO_COLS = 5;
	public static final int IDCOLEGIO = 0;
	public static final int IDCURSO = 1;
	public static final int IDASIGNATURA = 2;
	public static final int RUT = 3;
	public static final int RESPUESTAS = 4;

	public static final int[] VALID_CELL_TYPE = { Cell.CELL_TYPE_NUMERIC, Cell.CELL_TYPE_NUMERIC, Cell.CELL_TYPE_NUMERIC,
			Cell.CELL_TYPE_STRING, Cell.CELL_TYPE_STRING };
	
	
	private Constants() {
		
	}
}
