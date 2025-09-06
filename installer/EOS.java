import java.util.logging.Logger;
public class EOS {
	private static Logger log = Logger.getLogger(EOS.class.getName());

	public static void main(String[] args) {
		log.info(EOS.class.getResource("/./").toString());
	}
}