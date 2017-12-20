package Server_MVC;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

import javafx.application.Platform;
import javafx.scene.control.TextArea;

/**
 * @author Bodo Gr�tter
 * 
 * A log-handler that writes to a TextArea. Platform.runLater is a means
 * of putting work onto the JavaFX application thread. Anything that
 * modifies with the GUI should be on this thread. In this case, the
 * critical line is textArea.setText(...).
 * 
 * Adapted from:
 * Prof. Bradley Richards, Package: ch.fhnw.richards.lecture08.webServer_v2, Class: TextAreaHandler.java
 */
public class TextAreaHandler extends Handler {

	private TextArea textArea = new TextArea();

	public void close() throws SecurityException{
		//nothing to do here
	}

	public void flush() {
		//nothing to do here
	}

	public TextArea getTextArea() {
		return this.textArea;
	}

	/**
	 * this method logs the log output into an textArea
	 * 
	 * @author Bodo Gruetter
	 * @param record, the new record in the Log
	 */
	public void publish(LogRecord record) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				StringWriter text = new StringWriter();
				PrintWriter out = new PrintWriter(text);
				out.println(textArea.getText());
				out.printf("[%s] [Thread-%d]: %s.%s -> %s", record.getLevel(), record.getThreadID(),
						record.getSourceClassName(), record.getSourceMethodName(), record.getMessage());
				textArea.setText(text.toString());
			}
		});
	}
}