package cl.eos.detection;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.Arrays;

import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;

import org.apache.pdfbox.exceptions.COSVisitorException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;
import org.apache.pdfbox.pdmodel.graphics.xobject.PDJpeg;
import org.apache.pdfbox.pdmodel.graphics.xobject.PDXObjectImage;

// #### THIS CODE CURRENTLY DOES NOT FUNCTION CORRECTLY - SEE INLINE COMMENTS IN THE CODE TO UNDERSTAND WHY ####
//
// Demonstrates converting a JavaFX SceneGraph to a pdf (just as a bitmapped image, not as vector graphics).
//   1. creating a snapshot of a JavaFX node.
//   2. creating a pdf from the snapshot (using apache pdfbox http://pdfbox.apache.org/).
//   3. saving the pdf to a file.
//   4. opening the saved pdf in a web browser so the web browser can trigger showing
//      the pdf in an appropriate pdf viewer (if the user has such a viewer installed).
public class PdfWithImageCreator extends Application {
	// icon courtesy of http://www.aha-soft.com/ not for commercial use (free
	// for non-commercial use).
	private static final String imageURL = "http://icons.iconarchive.com/icons/aha-soft/free-global-security/512/Global-Network-icon.png";

	private static final String PDF_PATH = Paths.get("exported.pdf")
			.toAbsolutePath().toString();

	@Override
	public void start(Stage stage) {
		final VBox layout = new VBox(20);

		ImageView imageView = new ImageView(new Image(imageURL));

		Button export = new Button("Export");
		export.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent actionEvent) {
				exportToPDF(layout, PDF_PATH);
			}
		});

		layout.setStyle("-fx-font-size: 20px;");
		layout.setAlignment(Pos.CENTER);
		layout.setPadding(new Insets(20));
		layout.getChildren().setAll(export, imageView);

		stage.setScene(new Scene(new Group(layout)));

		stage.show();
	}

	/**
	 * Snapshots the provided node. Encodes the snapshot in a pdf. Saves the pdf
	 * to the provided file path. Opens the pdf in the default system web
	 * browser.
	 *
	 * @param node
	 *            the node for which the snapshot is to be taken.
	 * @param filePath
	 *            the path where the pdf is to be saved.
	 */
	private void exportToPDF(Node node, String filePath) {
		PDDocument doc = null;
		PDPage page = null;
		PDPageContentStream content = null;
		PDXObjectImage ximage = null;

		try {
			// snapshot the node and convert it to an awt buffered image.
			Image fxImage = node.snapshot(null, null);
			BufferedImage bufferedImage = SwingFXUtils.fromFXImage(fxImage,
					null);

			// create a pdf containing the snapshot image.
			doc = new PDDocument();
			page = new PDPage();
			doc.addPage(page);

			content = new PDPageContentStream(doc, page);

			// alternate path A => try to create a PDJpeg from a
			// jpegInputStream.
			ximage = createPDJpegFromJpegStream(doc, bufferedImage);

			// alternate path B => try to create a PDJpeg from directly from a
			// BufferedImage directly.
			// ximage = createPDJpegFromBufferedImage(doc, bufferedImage);

			content.drawImage(ximage, 0, 0);
			content.close();

			// save the created image to disk.
			doc.save(filePath);

			System.out.println("Exported PDF to: " + filePath);

			// show the generated pdf in a web browser.
			// (if the browser is pdf enabled, this will display the pdf in the
			// web browser).
			getHostServices().showDocument(filePath);
		} catch (IOException | COSVisitorException ie) {
			ie.printStackTrace();
		} finally {
			try {
				if (content != null) {
					content.close();
				}
				if (doc != null) {
					doc.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	// #### THIS METHOD DOES NOT FUNCTION AS EXPECTED
	// alternate path => try to create a PDJpeg from a jpegInputStream.
	// when using a jpeg stream this doesn't work, the created pdf is not well
	// formed and
	// you end up with adobe pdf reader running out of memory trying to read the
	// resultant pdf.
	// Also outputs a weird message that I currently don't understand =>
	// INFO: About to return NULL from unhandled branch. filter =
	// COSName{DCTDecode}
	private PDXObjectImage createPDJpegFromJpegStream(PDDocument doc,
			BufferedImage bufferedImage) throws IOException {
		// provide the buffered image data as input to a jpeg input stream.
		ByteArrayOutputStream jpegOutputStream = new ByteArrayOutputStream();
		ImageOutputStream jpegImageOutputStream = ImageIO
				.createImageOutputStream(jpegOutputStream);
		ImageIO.write(bufferedImage, "jpeg", jpegImageOutputStream);
		InputStream jpegInputStream = new ByteArrayInputStream(Arrays.copyOf(
				jpegOutputStream.toByteArray(), jpegOutputStream.size()));

		// output created jpg file for debugging purposes
		// => when you view it is pink due to (I believe) an ImageIO bug.
		// you can see how the resultant image is pink by opening the image file
		// named in system.out in any image viewer.
		// this improper encoding of the jpeg data may be why the subsequent use
		// of it to generate a pdf
		// will generate a an invalid pdf.
		File file = new File("output.jpg");
		FileOutputStream fos = new FileOutputStream(file);
		fos.write(Arrays.copyOf(jpegOutputStream.toByteArray(),
				jpegOutputStream.size()));
		System.out.println(file.getAbsolutePath());

		return new PDJpeg(doc, jpegInputStream);
	}

	// #### THIS METHOD DOES NOT FUNCTION AS EXPECTED
	// alternate path => try to create a PDJpeg from directly from a
	// BufferedImage directly, get the following exception:
	// Exception in thread "JavaFX Application Thread"
	// java.lang.IllegalArgumentException: Raster IntegerInterleavedRaster:
	// width = 552 height = 616 #Bands = 1 xOff = 0 yOff = 0 dataOffset[0] 0 is
	// incompatible with ColorModel ColorModel: #pixelBits = 8 numComponents = 1
	// color space = java.awt.color.ICC_ColorSpace@125fe1b6 transparency = 1 has
	// alpha = false isAlphaPre = false
	// at java.awt.image.BufferedImage.<init>(BufferedImage.java:630)
	// Browsing the awt PDJpeg and awt code it appears that the BufferedImage
	// returned by JavaFX uses a
	// SinglePixelPackedSampleModel, but PDJpeg required the buffered image to
	// use a ComponentColorModel
	// and the two are incompatible. So the bufferedimage needs to be re-encoded
	// to a compatible
	// raster format that utilizes a SampleModel (i.e. a ComponentColorModel)
	// that is acceptable by PDJpeg.
	//
	private PDXObjectImage createPDJpegFromBufferedImage(PDDocument doc,
			BufferedImage bufferedImage) throws IOException {
		return new PDJpeg(doc, bufferedImage);
	}

	public static void main(String[] args) {
		launch(args);
	}
}
