package cl.eos.clone;

import java.io.IOException;

public class Migrator {

	public static void main(String[] args) throws IOException, InterruptedException {
		Process p =Runtime.getRuntime().exec("res/mysql -u eosorio -p_l2j1rs2 -h 170.239.86.231 -e \"create database newdatabase\"");
		
		p =Runtime.getRuntime().exec("res/mysqldump -u eosorio -p_l2j1rs2 -h 170.239.86.231 -d cpruebas_base | res/mysql -u eosorio -p_l2j1rs2 -h 170.239.86.231 newdatabase");
	}

}
