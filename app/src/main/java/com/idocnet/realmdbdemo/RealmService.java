package com.idocnet.realmdbdemo;

import com.idocnet.realmdbdemo.model.Employee;
import com.idocnet.realmdbdemo.model.SaveRespone;
import com.idocnet.realmdbdemo.model.Skill;

import java.util.List;

import io.reactivex.Observable;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.exceptions.RealmPrimaryKeyConstraintException;

public class RealmService {
    private Realm realm;

    public RealmService(){
        realm = Realm.getDefaultInstance();
    }

    public Observable<SaveRespone> addEmployee(final Employee employee){
        return Observable.create(emitter -> {
            realm = Realm.getDefaultInstance();
            realm.executeTransaction(realm -> {
                try {
                    realm.copyToRealm(employee);
                    SaveRespone respone = new SaveRespone();
                    respone.isSuccess = true;
                    respone.message = "Success";
                    emitter.onNext(respone);
                }catch (RealmPrimaryKeyConstraintException e){
                    emitter.onError(e);
                }
            });
        });

    }

    public Observable<List<Employee>> readEmployee(){
        return Observable.create(emitter -> {
            realm = Realm.getDefaultInstance();
            realm.executeTransaction(realm -> {
                try {
                    RealmResults<Employee> results = realm.where(Employee.class).findAll();
                    emitter.onNext(realm.copyFromRealm(results));
                }catch (Exception e){
                    emitter.onError(e);
                }
            });
        });
    }

    public Observable<SaveRespone> updateEmployee(Employee employee){
        return Observable.create(emitter -> {
            realm = Realm.getDefaultInstance();
            realm.executeTransaction(realm -> {
                try {
                    Employee employee1 = realm.where(Employee.class).equalTo(Employee.PROPERTY_NAME, employee.name).findFirst();
                    if (employee1 == null) {
                        employee1 = realm.createObject(Employee.class, employee.name);
                    }
                    employee1.age = employee.age;

                    Skill skill = realm.where(Skill.class).equalTo(Skill.PROPERTY_SKILL, employee.skills.get(0).skill).findFirst();
                    if (skill == null) {
                        skill = realm.createObject(Skill.class, employee.skills.get(0).skill);
                        realm.copyToRealm(skill);
                    }

                    if (!employee.skills.contains(skill)) {
                        employee.skills.add(skill);
                    }

                    emitter.onNext(new SaveRespone(true, "Success"));

                }catch (Exception e){
                    emitter.onError(e);
                }
            });
        });
    }

    public Observable<SaveRespone> deleteEmployee(String name){
        return Observable.create(emitter -> {
            realm = Realm.getDefaultInstance();
            realm.executeTransaction(realm -> {
                try {
                    Employee employee = realm.where(Employee.class).equalTo(Employee.PROPERTY_NAME, name).findFirst();
                    if (employee != null) {
                        employee.deleteFromRealm();
                    }

                    emitter.onNext(new SaveRespone(true, "Success"));
                }catch (Exception e){
                    emitter.onError(e);
                }

            });
        });
    }

    public Observable<SaveRespone> deleteEmployeeWithSkill(String skill){
        return Observable.create(emitter -> {
            realm = Realm.getDefaultInstance();
            realm.executeTransaction(realm -> {
                try {
                    RealmResults<Employee> employees = realm.where(Employee.class).equalTo("skills.skill", skill).findAll();
                    employees.deleteAllFromRealm();

                    emitter.onNext(new SaveRespone(true, "Success"));
                }catch (Exception e){
                    emitter.onError(e);
                }

            });
        });
    }

    public Observable<List<Employee>> getEmployeesByAge(int age){
        return Observable.create(emitter -> {
            realm = Realm.getDefaultInstance();
            realm.executeTransaction(realm -> {
                try {
                    RealmResults<Employee> results = realm.where(Employee.class).greaterThanOrEqualTo(Employee.PROPERTY_AGE, age).findAll();
                    emitter.onNext(realm.copyFromRealm(results));
                }catch (Exception e){
                    emitter.onError(e);
                }
            });
        });
    }

    public void realmClose(){
        realm.close();
    }


}
