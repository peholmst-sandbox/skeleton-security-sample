package com.example.skeletonsecurity.security.dev.ui.view;

import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.theme.lumo.LumoUtility;
import org.springframework.context.annotation.Profile;

/**
 * Login view for development.
 */
@Route(value = "login", autoLayout = false)
@PageTitle("Login")
@AnonymousAllowed
@Profile("dev")
public class LoginView extends Main implements BeforeEnterObserver {

    private final LoginForm login;

    public LoginView() {
        // Create the components
        var login18n = LoginI18n.createDefault();
        login18n.getForm().setUsername("Email");
        login18n.getForm().setTitle("Skeleton Security Sample");
        login18n.setAdditionalInformation("This is for development purposes only. You should not see this in production.");

        login = new LoginForm();
        login.setAction("login");
        login.setI18n(login18n);

        var userList = new UnorderedList();
        userList.add(new ListItem("user@example.com / tops3cr3t"));
        userList.add(new ListItem("admin@example.com / tops3cr3t"));

        // Configure the view
        setSizeFull();
        var exampleUsers = new Div(new H3("Example users"), userList);
        var centerDiv = new Div(login, exampleUsers);
        add(centerDiv);

        // Style the view
        addClassNames(LumoUtility.Display.FLEX, LumoUtility.JustifyContent.CENTER,
                LumoUtility.AlignItems.CENTER, LumoUtility.Background.CONTRAST_5);
        centerDiv.addClassNames(LumoUtility.Display.FLEX, LumoUtility.FlexDirection.COLUMN, LumoUtility.Gap.SMALL);
        exampleUsers.addClassNames(LumoUtility.Background.BASE, LumoUtility.Padding.LARGE);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if (event.getLocation()
                .getQueryParameters()
                .getParameters()
                .containsKey("error")) {
            login.setError(true);
        }
    }
}
