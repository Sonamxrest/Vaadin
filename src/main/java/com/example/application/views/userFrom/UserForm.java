package com.example.application.views.userFrom;

import com.example.application.data.dto.Certificates;
import com.example.application.data.dto.Customer;
import com.example.application.data.dto.RoleType;
import com.example.application.data.service.CustomerService;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import org.springframework.beans.factory.annotation.Autowired;

@PageTitle("User Form")
@Route(value = "form", layout = MainLayout.class)
@RouteAlias(value = "", layout = MainLayout.class)
@AnonymousAllowed
public class UserForm extends VerticalLayout {
    @Autowired
    private CustomerService customerService;

    TextField firstName = new TextField("First name");
    TextField lastName = new TextField("Last name");
    TextField username = new TextField("Username");
    TextField citizenshipNo = new TextField("Citizenship Number");
    TextField nationality = new TextField("Nationality");

    NumberField balance = new NumberField("Salary");
    PasswordField password = new PasswordField("Password");

    ComboBox<RoleType> role = new ComboBox<>("Roles");
    ComboBox<String> gender = new ComboBox<>("Gender");
    Button save = new Button("Save");
    Button close = new Button("Cancel");
    Binder<Customer> userBinder = new Binder<>(Customer.class);
    Binder<Certificates> certificatesBinder = new Binder<>(Certificates.class);


    @Autowired
    public UserForm() {
        role.setItems(RoleType.values());
        gender.setItems("Male", "Female", "Other");
        role.setItemLabelGenerator(Enum::toString);
        addClassName("contact-form");
        VerticalLayout layout = new VerticalLayout();
        layout.add(firstName, lastName, balance, username, password, citizenshipNo, gender, nationality, role);
        layout.setAlignItems(Alignment.STRETCH);
        add(layout, createButtonsLayout());
        userBinder.setBean(new Customer());
        userBinder.bindInstanceFields(this);
        certificatesBinder.setBean(new Certificates());
        certificatesBinder.bindInstanceFields(this);
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
        h.setJustifyContentMode(JustifyContentMode.END);
        return h;
    }


}
