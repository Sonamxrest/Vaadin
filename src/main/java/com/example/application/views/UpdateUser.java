package com.example.application.views;

import com.example.application.data.dto.Certificates;
import com.example.application.data.dto.Customer;
import com.example.application.data.dto.RoleType;
import com.example.application.data.service.CustomerService;
import com.example.application.security.AuthenticatedUser;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.*;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;

import javax.annotation.security.RolesAllowed;

@PageTitle("User Form")
@Route(value = "updateUser", layout = MainLayout.class)
@RolesAllowed({"ADMIN","USER"})
public class UpdateUser extends Div implements BeforeEnterObserver {
    @Autowired
    private CustomerService customerService;
    @Autowired
    private AuthenticatedUser authenticatedUser;

    private Customer customer = new Customer();
    TextField firstName = new TextField("First name");
    TextField lastName = new TextField("Last name");
    TextField username = new TextField("Username");
    TextField citizenshipNo = new TextField("Citizenship Number");
    TextField nationality = new TextField("Nationality");

    NumberField balance = new NumberField("Salary");

    ComboBox<RoleType> role = new ComboBox<>("Roles");
    ComboBox<String> gender = new ComboBox<>("Gender");
    Button save = new Button("Save");
    Button close = new Button("Cancel");
    Binder<Customer> userBinder = new Binder<>(Customer.class);
    Binder<Certificates> certificatesBinder = new Binder<>(Certificates.class);

    @Autowired
    public UpdateUser() {
        role.setItems(RoleType.values());
        gender.setItems("Male", "Female", "Other");
        role.setItemLabelGenerator(Enum::toString);
        addClassName("contact-form");
        VerticalLayout layout = new VerticalLayout();
        layout.add(firstName, lastName, balance, username, citizenshipNo, gender, nationality, role);
        layout.setAlignItems(FlexComponent.Alignment.STRETCH);
        add(layout, createButtonsLayout());

    }
    private HorizontalLayout createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        save.addClickListener((e) -> {
            Customer customer = userBinder.getBean();
            customer.setCertificates(certificatesBinder.getBean());
            customerService.save(customer);
            userBinder.setBean(new Customer());
            Notification.show("User Create", 200000, Notification.Position.TOP_END);
            UI.getCurrent().navigate("");
        });
        close.addClickListener(e->{
            UI.getCurrent().navigate("");
        });
        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);
        HorizontalLayout h =
                new HorizontalLayout(save, close);
        h.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
        return h;
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        UserDetails userDetails = authenticatedUser.get().get();
        Customer customer1 = customerService.getByUsername(userDetails.getUsername());
        userBinder.setBean(customer1);
        userBinder.bindInstanceFields(this);
        certificatesBinder.setBean(customer1.getCertificates());
        certificatesBinder.bindInstanceFields(this);
    }
}
