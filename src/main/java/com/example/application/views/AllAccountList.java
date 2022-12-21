package com.example.application.views;

import com.example.application.data.dto.Account;
import com.example.application.data.service.AccountService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;

import javax.annotation.security.RolesAllowed;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@PageTitle("All Account List")
@Route(value = "allAccount", layout = MainLayout.class)
@RolesAllowed("ADMIN")
public class AllAccountList extends Div implements BeforeEnterObserver {

    private TextField accountName;
    private TextField accountNumber;
    private TextField balance;
    private TextField bank;
    private final Button cancel = new Button("Reset");
    private final Button save = new Button("Filter");

    private List<Account> accounts = new ArrayList<>();
    @Autowired
    private AccountService accountService;
    Grid<Account> accountGrid = new Grid<>(Account.class, false);

    public AllAccountList(AccountService accountService) {
        addClassNames("master-detail-view");
        SplitLayout splitLayout = new SplitLayout();
        accountGrid.addColumn("accountName").setAutoWidth(true);
        accountGrid.addColumn("accountNumber").setAutoWidth(true);
        accountGrid.addColumn("balance").setAutoWidth(true);
        accountGrid.addColumn("bank.name").setAutoWidth(true);
        createGridLayout(splitLayout);
        createEditorLayout(splitLayout);

        save.addClickListener(e -> {
            Map<String, String> map = new HashMap<>();
            map.put("accountName",accountName.getValue() );
            map.put("accountNumber",accountNumber.getValue() );
            map.put("balance",balance.getValue() );
            map.put("bank",bank.getValue());
            map.values().removeIf(ObjectUtils::isEmpty);
            accounts = accountService.filter(map);
            accountGrid.setItems(accounts);
            accountGrid.getColumnByKey("balance").setFooter(getTotal());

        });
        cancel.addClickListener(e->{
           accountName.setValue("");
           bank.setValue("");
           balance.setValue("");
           accountNumber.setValue("");
            accounts = accountService.filter(new HashMap<>());
            accountGrid.setItems(accounts);
            accountGrid.getColumnByKey("balance").setFooter(getTotal());

        });
        add(splitLayout);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        accounts = accountService.filter(new HashMap<>());
        accountGrid.setItems(accounts);
        accountGrid.getColumnByKey("balance").setFooter(getTotal());

    }
    public String getTotal() {
        DecimalFormat formatter = new DecimalFormat("#,###.00");
        return  "Rs: " + formatter.format(accounts.stream().mapToDouble(Account::getBalance).sum());
    }
    private void createEditorLayout(SplitLayout splitLayout) {
        Div editorLayoutDiv = new Div();
        editorLayoutDiv.setClassName("editor-layout");

        Div editorDiv = new Div();
        editorDiv.setClassName("editor");
        editorLayoutDiv.add(editorDiv);

        FormLayout formLayout = new FormLayout();
        accountName = new TextField("Account Name");
        accountNumber = new TextField("Account Number");
        balance = new TextField("Balance Greater Than");
        bank = new TextField("Bank Name");
        formLayout.add(accountNumber, accountName, balance, bank);

        editorDiv.add(formLayout);
        createButtonLayout(editorLayoutDiv);

        splitLayout.addToSecondary(editorLayoutDiv);
    }

    private void createButtonLayout(Div editorLayoutDiv) {
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setClassName("button-layout");
        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        buttonLayout.add(save, cancel);
        editorLayoutDiv.add(buttonLayout);
    }

    private void createGridLayout(SplitLayout splitLayout) {
        Div wrapper = new Div();
        wrapper.setClassName("grid-wrapper");
        splitLayout.addToPrimary(wrapper);
        wrapper.add(accountGrid);
    }

}
