package com.example.application.views.departmentList;

import com.example.application.views.MainLayout;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.security.RolesAllowed;

@PageTitle("Department List")
@Route(value = "deptList", layout = MainLayout.class)
@Uses(Icon.class)
@AnonymousAllowed
@RolesAllowed("ADMIN")
public class DepartmentList extends Div {
//    private final Grid<Department> grid = new Grid<>(Department.class, false);
//    private final DepartmentService departmentService;
//
//    @Autowired
//    public DepartmentList(DepartmentService departmentService) {
//        this.departmentService = departmentService;
//        addClassNames("master-detail-view");
//        // Create UI
//        SplitLayout splitLayout = new SplitLayout();
//        createGridLayout(splitLayout);
//        grid.addColumn("id").setAutoWidth(true);
//        grid.addColumn("deptName").setAutoWidth(true);
//        grid.addColumn("deptType").setAutoWidth(true);
//        grid.setItems( this.departmentService.list());
//        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
//        grid.getHeaderRows().clear();
//        add(splitLayout);
//    }
//    private void createGridLayout(SplitLayout splitLayout) {
//        Div wrapper = new Div();
//        wrapper.setClassName("grid-wrapper");
//        splitLayout.addToPrimary(wrapper);
//        wrapper.add(grid);
//    }
}
