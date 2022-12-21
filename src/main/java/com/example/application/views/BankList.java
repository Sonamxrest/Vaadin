package com.example.application.views;

import com.example.application.data.dto.Bank;
import com.example.application.data.service.BankService;
import com.example.application.security.AuthenticatedUser;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.annotation.security.RolesAllowed;

@PageTitle("Bank List")
@Route(value = "bank", layout = MainLayout.class)
@Uses(Icon.class)
@RolesAllowed({"ADMIN","USER"})
public class BankList extends Div {
    private final BankService bankService;
    private final AuthenticatedUser authenticatedUser;
    Grid<Bank> grid = new Grid<>(Bank.class, false);
    public  BankList(BankService bankService, AuthenticatedUser authenticatedUser) {
        this.bankService = bankService;
        this.authenticatedUser = authenticatedUser;

        UserDetails customer = authenticatedUser.get().get();
        boolean isAdmin = customer.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"));
        addClassNames("master-detail-view");
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        Button add = new Button(new Icon(VaadinIcon.PLUS));
        add.setEnabled(isAdmin);
        add.addThemeVariants(ButtonVariant.LUMO_ICON);
        add.getElement().setAttribute("aria-label", "Close");
        add.addClickListener(e->{
            UI.getCurrent().navigate("addBank");
        });
        horizontalLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
        horizontalLayout.add(add);
        grid.addColumn("id").setAutoWidth(true);
        grid.addColumn("name").setAutoWidth(true);
        grid.addColumn("location").setAutoWidth(true);
        grid.addColumn("valuation").setAutoWidth(true);
        grid.addColumn(
                new ComponentRenderer<>(Button::new, (button, bank) -> {
                    button.setEnabled(isAdmin);
                    button.addThemeVariants(ButtonVariant.LUMO_ICON,
                            ButtonVariant.LUMO_ERROR,
                            ButtonVariant.LUMO_TERTIARY);
                    button.addClickListener(e -> {
                        UI.getCurrent().navigate("updateBank/"+ bank.getId());
                    });
                    button.setIcon(new Icon(VaadinIcon.EDIT));
                })).setHeader("Manage");
        grid.setItems(this.bankService.list());
        add(horizontalLayout, grid);
    }
}
