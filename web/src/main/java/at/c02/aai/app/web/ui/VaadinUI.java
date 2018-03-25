package at.c02.aai.app.web.ui;

import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Button;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;

@SpringUI(path = "/ui")
public class VaadinUI extends UI {

	private static final long serialVersionUID = 1L;

	@Override
    protected void init(VaadinRequest request) {
	    setContent(new Button("Click me", e -> Notification.show("Hello Spring+Vaadin user!")));
    }
}