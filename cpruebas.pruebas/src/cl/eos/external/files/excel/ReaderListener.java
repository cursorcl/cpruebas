package cl.eos.external.files.excel;

import cl.eos.external.files.utils.Register;

public interface ReaderListener {
	void onReadRegister(Register register);
	void onReadFile(String message);
}
