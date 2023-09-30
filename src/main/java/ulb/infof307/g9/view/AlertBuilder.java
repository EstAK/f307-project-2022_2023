package ulb.infof307.g9.view;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextArea;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * AlertBuilder is a helper class to build Alert popups of type INFO (by default), WARNING or ERROR
 * It allows to add a header, content and stack trace to the popup
 * and then display it with the show() method or get it with the build() method
 */
public class AlertBuilder {

    private final Alert alert;

    /**
     * Create a new AlertBuilder with a default type INFORMATION
     */
    public AlertBuilder() {
        alert = new Alert(AlertType.INFORMATION);
    }

    /**
     * Set the type of the popup to WARNING
     *
     * @return the AlertBuilder
     */
    public AlertBuilder warning() {
        alert.setAlertType(AlertType.WARNING);
        return this;
    }

    /**
     * Set the type of the popup to ERROR
     *
     * @return the AlertBuilder
     */
    public AlertBuilder error() {
        alert.setAlertType(AlertType.ERROR);
        return this;
    }

    /**
     * Add a header to the popup
     * The header is displayed in bold at the top of the popup
     *
     * @param header short description of the popup like "An error occurred"
     * @return the AlertBuilder
     */
    public AlertBuilder addHeader(String header) {
        alert.setHeaderText(header);
        return this;
    }

    /**
     * Add a content to the popup
     * The content is displayed below the header
     *
     * @param content longer description of the popup
     * @return the AlertBuilder
     */
    public AlertBuilder addContent(String content) {
        alert.setContentText(content);
        return this;
    }

    /**
     * Add a stack trace to the popup
     * The stack trace is displayed in a TextArea below the content
     * and can be expanded by the user
     *
     * @param stackTrace the stack trace to display or any other info a developer might want to see
     * @return the AlertBuilder
     */
    public AlertBuilder addStackTrace(String stackTrace) {
        TextArea textArea = new TextArea(stackTrace);
        textArea.setEditable(false);
        textArea.setWrapText(true);
        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);
        alert.getDialogPane().setExpandableContent(textArea);
        alert.getDialogPane().setExpanded(false);

        return this;
    }

    /**
     * Helper method to add a stack trace to the popup using an Exception
     *
     * @param e the exception to display
     * @return the AlertBuilder
     * @see #addStackTrace(String)
     */
    public AlertBuilder addStackTrace(Exception e) {
        // gets the stack trace as a string
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        String stackTrace = sw.toString();

        return addStackTrace(stackTrace);
    }

    /**
     * Helper method to add a header, content and stack trace to the popup using an Exception
     *
     * @param e the exception to display
     * @return the AlertBuilder
     * @see #addHeader(String)
     * @see #addContent(String)
     * @see #addStackTrace(Exception)
     */
    public AlertBuilder addException(Exception e) {
        return addHeader("Une erreur est survenue").addContent(e.getMessage()).addStackTrace(e);
    }

    /**
     * Show the popup
     * This method blocks until the user closes the popup
     */
    public void show() {
        alert.showAndWait();
    }

    /**
     * Build the Alert
     *
     * @return the Alert
     */
    public Alert build() {
        return alert;
    }
}