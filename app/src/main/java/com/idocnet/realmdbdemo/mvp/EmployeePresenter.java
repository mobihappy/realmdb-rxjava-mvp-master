package com.idocnet.realmdbdemo.mvp;

import android.util.Log;

import com.idocnet.realmdbdemo.model.Employee;
import com.idocnet.realmdbdemo.RealmService;
import com.idocnet.realmdbdemo.model.SaveRespone;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class EmployeePresenter {
    private EmployeeViewPresenter viewPresenter;
    private RealmService realmService;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public EmployeePresenter(RealmService realmService){
        this.realmService = realmService;
    }

    public void onAttach(EmployeeViewPresenter viewPresenter){
        this.viewPresenter = viewPresenter;
    }

    public void getEmployees(){
        compositeDisposable.add(realmService.readEmployee()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.computation())
                .subscribe(this::OnReceiveEmployee, this::OnError));
    }

    public void saveEmployee(Employee employee){
        compositeDisposable.add(realmService.addEmployee(employee)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.computation())
                .subscribe(this::OnReceiveSave, this::OnError));
    }

    public void updateEmployee(Employee employee){
        compositeDisposable.add(realmService.updateEmployee(employee)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.computation())
                .subscribe(this::OnReceiveSave, this::OnError));
    }

    public void deleteEmployee(String name){
        compositeDisposable.add(realmService.deleteEmployee(name)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.computation())
                .subscribe(this::OnReceiveSave, this::OnError));
    }

    public void deleteEmployeeBySkill(String skill){
        compositeDisposable.add(realmService.deleteEmployeeWithSkill(skill)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.computation())
                .subscribe(this::OnReceiveSave, this::OnError));
    }

    public void getEmployeesByAge(int age){
        compositeDisposable.add(realmService.getEmployeesByAge(age)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.computation())
                .subscribe(this::OnReceiveEmployee, this::OnError));
    }

    private void OnReceiveSave(SaveRespone respone){
        if (respone.isSuccess){
            viewPresenter.showSaveSuccess();
        }else {
            viewPresenter.showError("Can't save");
        }
    }

    private void OnReceiveEmployee(List<Employee> employees){
        viewPresenter.showEmployees(employees);
    }

    private void OnError(Throwable throwable){
        Log.d("GETEMPLOYEELOG", throwable.getMessage());
        viewPresenter.showError(throwable.getMessage());
    }

    public void onDisposable(){
        compositeDisposable.dispose();
    }
}
