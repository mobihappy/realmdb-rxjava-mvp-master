package com.idocnet.realmdbdemo.mvp;

import com.idocnet.realmdbdemo.model.Employee;

import java.util.List;

public interface EmployeeViewPresenter {
    void showSaveSuccess();
    void showEmployees(List<Employee> employees);
    void showError(String message);
}
