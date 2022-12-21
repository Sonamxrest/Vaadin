package com.example.application.views;

import com.example.application.data.dto.Bank;
import com.example.application.data.service.BankService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.security.RolesAllowed;

@PageTitle("Update Bank")
@Route(value = "updateBank", layout = MainLayout.class)
@RolesAllowed("ADMIN")
public class UpdateBank extends Div implements HasUrlParameter<Long> {
    private Bank bank;

    Binder<Bank> binder = new Binder<>(Bank.class);
    TextField name = new TextField("Name");
    TextField location = new TextField("Location");
    TextField valuation = new TextField("Valuation");
    @Autowired
    private BankService bankService;
    @Override
    public void setParameter(BeforeEvent event, Long parameter) {
        bank = bankService.getById(parameter);
        binder.setBean(bank);
        binder.bindInstanceFields(this);
    }
    public UpdateBank(BankService bankService) {

        VerticalLayout verticalLayout = new VerticalLayout(name, location, valuation);
        verticalLayout.setAlignItems(FlexComponent.Alignment.STRETCH);
        verticalLayout.add(new HorizontalLayout(new Button("Save",event -> {
            this.bankService.save(binder.getBean());
            UI.getCurrent().navigate("bank");
        })));
        add(verticalLayout);

    }


}
