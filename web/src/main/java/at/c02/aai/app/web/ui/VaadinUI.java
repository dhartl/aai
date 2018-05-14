package at.c02.aai.app.web.ui;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.annotations.Theme;

import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalSplitPanel;

import at.c02.aai.app.web.ui.components.MapComponent;



@SpringUI(path = "/ui")
@Theme("aai")
public class VaadinUI extends UI {

   

    private static final long serialVersionUID = 1L;
    
    @Autowired
    private MapComponent mapComponent;
    

    
    protected void init(VaadinRequest request) {
               
        VerticalSplitPanel vsp = new VerticalSplitPanel();
        vsp.setFirstComponent(mapComponent);
	setContent(vsp);
    }   
}