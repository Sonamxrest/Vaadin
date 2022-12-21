package com.example.application.views;

import com.example.application.data.dto.Account;
import com.example.application.data.dto.Bank;
import com.example.application.data.dto.Customer;
import com.example.application.data.service.BankService;
import com.example.application.data.service.CustomerService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.RequiredFieldConfigurator;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import org.hibernate.validator.internal.constraintvalidators.bv.NullValidator;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.security.RolesAllowed;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

@PageTitle("Add Account")
@Route(value = "addAccount", layout = MainLayout.class)
@RolesAllowed("ADMIN")
public class AddAccount extends Div implements HasUrlParameter<Long> {
    private Customer customer;

    private Grid<Account> accountGrid = new Grid<>(Account.class, false);
    @Autowired
    private CustomerService customerService;

    @Autowired
    private BankService bankService;
    BeanValidationBinder<Account> accountBinder = new BeanValidationBinder<>(Account.class);

    private List<Account> accounts = new ArrayList<>();
    TextField accountName = new TextField("Account Name");
    TextField accountNumber = new TextField("Account Number");
    TextField balance = new TextField("Balance Available");

    ComboBox<Bank> bank = new ComboBox<>("Bank");
    Button save = new Button("ADD");
    Button saveAll = new Button("Save");


    @Autowired
    public AddAccount(CustomerService customerService, BankService bankService) {
        Div errorsLayout = new Div();
        bank.setItems(bankService.list());
        addClassNames("master-detail-view");
        bank.setItemLabelGenerator(Bank::getName);
        save.setThemeName("primary");
        HorizontalLayout formLayout = new HorizontalLayout(accountName, accountNumber, balance, bank, save, saveAll);
        Div wrapperLayout = new Div(formLayout, errorsLayout);
        formLayout.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.BASELINE);
        wrapperLayout.setWidth("100%");
        save.addClickListener(e -> {
            accounts.add(accountBinder.getBean());
            accountBinder.setBean(new Account());
            accountGrid.setItems(accounts);
            accountGrid.getColumnByKey("balance").setFooter(getTotal());
        });
        accountGrid.addColumn("accountName").setAutoWidth(true);
        accountGrid.addColumn("accountNumber").setAutoWidth(true);
        accountGrid.addColumn("balance").setAutoWidth(true);
        accountGrid.addColumn("bank.name").setAutoWidth(true);
        accountGrid.addColumn(
                new ComponentRenderer<>(Button::new, (button, person) -> {
                    button.addThemeVariants(ButtonVariant.LUMO_ICON,
                            ButtonVariant.LUMO_ERROR,
                            ButtonVariant.LUMO_TERTIARY);
                    button.addClickListener(e -> {
                        accounts.removeIf(d -> d.getAccountNumber().equals(person.getAccountNumber()));
                        accountGrid.setItems(accounts);
                    });
                    button.setIcon(new Icon(VaadinIcon.TRASH));
                })).setHeader("Manage");
        add(wrapperLayout, accountGrid);

        saveAll.addClickListener(e -> {
            customer.setAccounts(accounts);
            this.customerService.save(customer);
            Notification.show("Saved");
            UI.getCurrent().navigate("");
        });
        accountBinder.setBean(new Account());
        accountBinder.bindInstanceFields(this);
        accountBinder.setRequiredConfigurator(RequiredFieldConfigurator.NOT_EMPTY.chain(RequiredFieldConfigurator.NOT_NULL));
//        accountBinder.addStatusChangeListener(d->{
//            save.setEnabled( accountBinder.validate().isOk());
//        });
//        save.setEnabled( accountBinder.validate().isOk());

    }
    public String getTotal() {
        DecimalFormat formatter = new DecimalFormat("#,###.00");
        return  "Rs: " + formatter.format(accounts.stream().mapToDouble(Account::getBalance).sum());
    }
    @Override
    public void setParameter(BeforeEvent event, Long parameter) {
        this.customer = this.customerService.getById(parameter);
        this.accounts.addAll(this.customer.getAccounts());
        accountGrid.setItems(accounts);
        accountGrid.getColumnByKey("balance").setFooter(getTotal());
    }
}
