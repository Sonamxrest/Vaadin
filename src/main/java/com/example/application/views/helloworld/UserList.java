package com.example.application.views.helloworld;

import com.example.application.data.dto.Customer;
import com.example.application.data.service.CustomerService;
import com.example.application.security.AuthenticatedUser;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.annotation.security.RolesAllowed;


@PageTitle("User List")
@Route(value = "hello", layout = MainLayout.class)
@Uses(Icon.class)
@RolesAllowed({"ADMIN","USER"})
public class UserList extends HorizontalLayout {

    private final CustomerService customerService;

    private final AuthenticatedUser authenticatedUser;

    private final Grid<Customer> grid = new Grid<>(Customer.class, false);

    Dialog dialog = new Dialog();
    Button deleteButton = new Button("Delete");

    Button cancelButton = new Button("Cancel", (e) -> dialog.close());

    @Autowired
    public UserList(CustomerService customerService, AuthenticatedUser authenticatedUser) {
        this.authenticatedUser = authenticatedUser;
        UserDetails customer = authenticatedUser.get().get();
        boolean isAdmin = customer.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"));
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        Button add = new Button(new Icon(VaadinIcon.PLUS));
        add.setEnabled(isAdmin);
        add.addThemeVariants(ButtonVariant.LUMO_ICON);
        add.getElement().setAttribute("aria-label", "Close");
        add.addClickListener(e->{
            UI.getCurrent().navigate("form");
        });
        horizontalLayout.add(add);
        horizontalLayout.setJustifyContentMode(JustifyContentMode.END);

        addClassNames("master-detail-view");
        deleteButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY,
                ButtonVariant.LUMO_ERROR);
        deleteButton.getStyle().set("margin-right", "auto");
        dialog.getFooter().add(deleteButton);
        cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        dialog.getFooter().add(cancelButton);

        this.customerService = customerService;
        // Create UI
        SplitLayout splitLayout = new SplitLayout();
        createGridLayout(splitLayout);
        add(horizontalLayout,splitLayout);
        grid.addColumn("id").setAutoWidth(true);
        grid.addColumn("firstName").setAutoWidth(true);
        grid.addColumn("lastName").setAutoWidth(true);
        grid.addColumn("username").setAutoWidth(true);
        grid.addColumn("balance").setAutoWidth(true);
        grid.addColumn(
                new ComponentRenderer<>(Button::new, (button, person) -> {
                    button.setEnabled(isAdmin);
                    button.addThemeVariants(ButtonVariant.LUMO_ICON,
                            ButtonVariant.LUMO_ERROR,
                            ButtonVariant.LUMO_TERTIARY);
                    button.addClickListener(e -> {
                        dialog.setHeaderTitle("Are you sure you want to delete this item?");
                        openDialog();
                        deleteButton.addClickListener(ev -> {
                            this.customerService.delete(person.getId());
                            dialog.close();
                            grid.setItems(this.customerService.list());

                        });
                    });
                    button.setIcon(new Icon(VaadinIcon.TRASH));
                })).setHeader("Manage");
        grid.addColumn(
                new ComponentRenderer<>(Button::new, (button, person) -> {
                    button.setEnabled(isAdmin);

                    button.addThemeVariants(ButtonVariant.LUMO_ICON,
                            ButtonVariant.LUMO_ERROR,
                            ButtonVariant.LUMO_TERTIARY);
                    button.addClickListener(e -> {
                        UI.getCurrent().navigate("addAccount/" + person.getId());
                    });
                    button.setIcon(new Icon(VaadinIcon.EDIT));
                })).setHeader("Add Account");
        grid.setItems(this.customerService.list());
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        grid.getHeaderRows().clear();
    }

    private void createGridLayout(SplitLayout splitLayout) {
        Div wrapper = new Div();
        wrapper.setClassName("grid-wrapper");
        splitLayout.addToPrimary(wrapper);
        wrapper.add(grid);
    }

    private void openDialog() {
        dialog.open();
    }

}
