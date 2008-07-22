package com.tickets.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RootPanel;
import com.tickets.client.components.MainPanel;
import com.tickets.client.constants.Messages;
import com.tickets.client.screens.CoursesScreen;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Main implements EntryPoint {

    /**
     * This is the entry point method.
     */
    private MainPanel mainPanel;

    public void onModuleLoad() {
        MenuBar ticketsMenu = new MenuBar(true);
        ticketsMenu.addItem(Messages.m.check(), (Command) null);
        ticketsMenu.addItem(Messages.m.sale(), (Command) null);

        MenuBar timeTableMenu = new MenuBar(true);
        timeTableMenu.addItem(Messages.m.view(), (Command) null);
        timeTableMenu.addItem(Messages.m.courses(), new CoursesCommand());

        MenuBar menu = new MenuBar();
        menu.addItem(Messages.m.tickets(), ticketsMenu);
        menu.addItem(Messages.m.timeTable(), timeTableMenu);

        RootPanel.get().add(menu);

        mainPanel = new MainPanel();

        RootPanel.get().add(mainPanel);



//		Image img = new Image("http://code.google.com/webtoolkit/logo-185x175.png");
//		Button button = new Button("Click me");

//		VerticalPanel vPanel = new VerticalPanel();
//		// We can add style names.
//		vPanel.addStyleName("widePanel");
//		vPanel.setHorizontalAlignment(VerticalPanel.ALIGN_CENTER);
//		vPanel.add(img);
//		vPanel.add(button);

//		// Add image and button to the RootPanel
//		RootPanel.get().add(vPanel);

//		// Create the dialog box
//		final DialogBox dialogBox = new DialogBox();
//		dialogBox.setText("Welcome to GWT!");
//		dialogBox.setAnimationEnabled(true);
//		Button closeButton = new Button("close");
//		VerticalPanel dialogVPanel = new VerticalPanel();
//		dialogVPanel.setWidth("100%");
//		dialogVPanel.setHorizontalAlignment(VerticalPanel.ALIGN_CENTER);
//		dialogVPanel.add(closeButton);

//		closeButton.addClickListener(new ClickListener() {
//		public void onClick(Widget sender) {
//		dialogBox.hide();
//		}
//		});

//		// Set the contents of the Widget
//		dialogBox.setWidget(dialogVPanel);

//		button.addClickListener(new ClickListener() {
//		public void onClick(Widget sender) {
//		dialogBox.center();
//		dialogBox.show();
//		}
//		});
    }

    public AbsolutePanel getMainPanel() {
        return mainPanel;
    }

    class CoursesCommand implements Command {
        public void execute() {
            Panel p = getMainPanel();
            CoursesScreen screen = new CoursesScreen();
            p.add(screen);
        }
    }
}
