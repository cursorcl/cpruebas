package cl.eos.imp.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import cl.eos.api.EnumImport;
import cl.eos.api.ExceptionImport;
import cl.eos.imp.AImporter;

public class ImportFromExcel extends AImporter {

	private HSSFWorkbook workBook = null;

	@Override
	public EnumImport exceute() throws ExceptionImport {
		if (source == null) {
			throw new ExceptionImport("No hay fuente establecida.");
		}
		if (getWorkbook() == null) {
			throw new ExceptionImport(
					"La fuente de datos establecida es inválida.");
		}

		return null;
	}

	private HSSFWorkbook getWorkbook() {
		if (!(source instanceof File)) {
			return null;
		}

		File file = (File) source;
		try {
			FileInputStream fIS = new FileInputStream(file.getAbsoluteFile());
			POIFSFileSystem poi = new POIFSFileSystem(fIS);
			workBook = new HSSFWorkbook(poi);
		} catch (FileNotFoundException e) {
			return null;
		} catch (IOException e) {
			return null;
		}
		return workBook;
	}

}
