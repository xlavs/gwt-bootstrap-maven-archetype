package com.klavs.greetservlet.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.klavs.greetservlet.shared.FieldVerifier;
import org.gwtbootstrap3.client.ui.*;
import org.gwtbootstrap3.client.ui.constants.*;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class GWTBasicApplication implements EntryPoint {

    /**
     * The message displayed to the user when the server cannot be reached or
     * returns an error.
     */
    private static final String SERVER_ERROR = "An error occurred while "
            + "attempting to contact the server. Please check your network "
            + "connection and try again.";
    private static final String ERROR_MESSAGE_PLACEHOLDER = "";

    /**
     * Create a remote service proxy to talk to the server-side Greeting service.
     */
    private final GreetingServiceAsync greetingService = GWT.create(GreetingService.class);

    /**
     * A list of widgets managed by event handlers
     */
    private HelpBlock errorMessage;
    private TextBox nameField;
    private Button sendButton;
    private RpcMessageModalBox rpcMessageBox;

    /**
     * This is the entry point method.
     */
    public void onModuleLoad() {

        Container rootContainer = new Container();

        /*
         *  applicationPanel has fixed top padding value defined in greetuser.css.
         */
        rootContainer.setId("applicationPanel");
        makeUserQueryPanel(rootContainer);

        RootPanel.get("applicationContainer").add(rootContainer);

        // Focus the cursor on the name field when the app loads
        nameField.setFocus(true);
        nameField.selectAll();

        // Create the popup dialog box
        rpcMessageBox = new RpcMessageModalBox(sendButton);

        // Add a handler to send the name to the server
        ChangeEventHandler changeEventHandler = new ChangeEventHandler();
        sendButton.addClickHandler(changeEventHandler);
        nameField.addKeyUpHandler(changeEventHandler);
    }

    private void makeUserQueryPanel(Container container) {

        final org.gwtbootstrap3.client.ui.Panel panel = new org.gwtbootstrap3.client.ui.Panel();
        final PanelHeader panelHeader = new PanelHeader();
        panelHeader.add(new Heading(HeadingSize.H3, "Web Application Starter Project"));
        panel.add(panelHeader);
        container.add(panel);

        final PanelBody panelBody = new PanelBody();

        final Form form = new Form();
        form.setType(FormType.HORIZONTAL);

        final FieldSet fieldSet = new FieldSet();

        /*
         *  FieldSet has fixed height value in greetuser.css to reserve space
         *  for error message below the input field
         */
        fieldSet.setId("userFieldSet");

        nameField = new TextBox();
        nameField.setPlaceholder("Please enter your name");
        nameField.setText("GWT User");
        nameField.setTabIndex(1);

        final InputGroup inputGroup = new InputGroup();
        final InputGroupAddon inputGroupAddon = new InputGroupAddon();
        inputGroupAddon.setText("User");
        inputGroupAddon.setIcon(IconType.USER);
        inputGroup.add(inputGroupAddon);
        inputGroup.add(nameField);
        final InputGroupButton inputGroupButton = new InputGroupButton();
        sendButton = new Button("Send");

        // We can add style names to widgets
        sendButton.setTabIndex(999);

        inputGroupButton.add(sendButton);
        inputGroup.add(inputGroupButton);
        errorMessage = new HelpBlock();
        errorMessage.setText(ERROR_MESSAGE_PLACEHOLDER);
        fieldSet.add(inputGroup);
        fieldSet.add(errorMessage);

        form.add(fieldSet);

        panelBody.add(form);
        panel.add(panelBody);

    }

    /**
     * Inner class represents message box
     */
    class RpcMessageModalBox {
        private Modal modal; // replace with modal element from bootstrap
        private Button closeButton;
        private HTML textToServer;
        private HTML serverResponse;
        private Button callerButton;


        public RpcMessageModalBox(Button buttonToBoundTo) {
            this.callerButton = buttonToBoundTo;
            modal = new Modal();
            modal.setId("modalMessageBox");
            modal.setClosable(true);
            modal.setFade(true);
            modal.setDataBackdrop(ModalBackdrop.STATIC);
            modal.setDataKeyboard(true);
            modal.setTitle("Remote Procedure Call");

            final ModalBody modalBody = new ModalBody();
            modal.add(modalBody);
            final ModalFooter modalFooter = new ModalFooter();

            closeButton = new Button("Close");
            // We can set the id of a widget by accessing its Element
            closeButton.getElement().setId("closeButton");
            closeButton.setDataTarget("modalMessageBox");
            closeButton.setDataDismiss(ButtonDismiss.MODAL);
            modalFooter.add(closeButton);
            modal.add(modalFooter);

            textToServer = new HTML();
            serverResponse = new HTML();

            VerticalPanel dialogVPanel = new VerticalPanel();
            dialogVPanel.addStyleName("dialogVPanel");
            dialogVPanel.add(new HTML("<b>Sending name to the server:</b>"));
            dialogVPanel.add(textToServer);
            dialogVPanel.add(new HTML("<br><b>Server replies:</b>"));
            dialogVPanel.add(serverResponse);
            dialogVPanel.setHorizontalAlignment(VerticalPanel.ALIGN_RIGHT);

            modalBody.add(dialogVPanel);

            // Add a handler to close the DialogBox
            final ClickHandler buttonClickHandler = new ClickHandler() {
                public void onClick(ClickEvent event) {
                    modal.hide();
                    callerButton.setEnabled(true);
                    callerButton.setFocus(true);
                }
            };
            closeButton.addClickHandler(buttonClickHandler);
        }

        public void setTextToServer(String textToServer) {
            this.textToServer.setText(textToServer);
        }

        public void setServerResponse(String serverResponse) {
            this.serverResponse.setText(serverResponse);
        }

        public void makeRpcErrorMessage(String serverError) {
            modal.setTitle("Remote Procedure Call - Failure");
            serverResponse.addStyleName("serverResponseLabelError");
            serverResponse.setHTML(serverError);
            modal.show();
            closeButton.setFocus(true);
        }

        public void makeRpcSuccessMessage(String result) {
            modal.setTitle("Remote Procedure Call");
            serverResponse.removeStyleName("serverResponseLabelError");
            serverResponse.setHTML(result);
            modal.show();
            closeButton.setFocus(true);
        }
    }

    /**
     * A handler for the sendButton and nameField
     */
    class ChangeEventHandler implements ClickHandler, KeyUpHandler {

        /**
         * Fired when the user clicks on the sendButton.
         */
        public void onClick(ClickEvent event) {
            sendNameToServer();
        }

        /**
         * Fired when the user types in the nameField.
         */
        public void onKeyUp(KeyUpEvent event) {
            if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
                sendNameToServer();
            }
        }

        /**
         * Send the name from the nameField to the server and wait for a response.
         */
        private void sendNameToServer() {
            // First, we validate the input.
            errorMessage.setText(ERROR_MESSAGE_PLACEHOLDER);
            String textToServer = nameField.getText();
            if (!FieldVerifier.isValidName(textToServer)) {
                errorMessage.setText("Please enter at least four characters");
                return;
            }

            // Then, we send the input to the server.
            sendButton.setEnabled(false);
            rpcMessageBox.setTextToServer(textToServer);
            rpcMessageBox.setServerResponse("");

            final AsyncCallback<String> asyncCallback = new AsyncCallback<String>() {
                public void onFailure(Throwable caught) {
                    // Show the RPC error message to the user
                    rpcMessageBox.makeRpcErrorMessage(SERVER_ERROR);
                }

                public void onSuccess(String result) {
                    rpcMessageBox.makeRpcSuccessMessage(result);
                }
            };

            greetingService.greetServer(textToServer, asyncCallback);
        }
    }
}
