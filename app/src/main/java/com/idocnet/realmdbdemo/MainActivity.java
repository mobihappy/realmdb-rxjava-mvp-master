package com.idocnet.realmdbdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.idocnet.realmdbdemo.model.Employee;
import com.idocnet.realmdbdemo.model.Skill;
import com.idocnet.realmdbdemo.mvp.EmployeePresenter;
import com.idocnet.realmdbdemo.mvp.EmployeeViewPresenter;

import java.util.List;

public class MainActivity extends AppCompatActivity implements EmployeeViewPresenter {
    private EditText edName, edAge, edSkill;
    private TextView tvData;
    RealmService realmService = new RealmService();
    private EmployeePresenter employeePresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        employeePresenter = new EmployeePresenter(realmService);
        employeePresenter.onAttach(this);

        edName = findViewById(R.id.edName);
        edAge = findViewById(R.id.edAge);
        edSkill = findViewById(R.id.edSkill);
        tvData = findViewById(R.id.tvData);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realmService.realmClose();
        employeePresenter.onDisposable();

    }

    public void saveClick(View view){
        employeePresenter.saveEmployee(employee());
    }

    public void updateClick(View view){
        employeePresenter.updateEmployee(employee());
    }

    public void getClick(View view){
        employeePresenter.getEmployees();
    }

    public void deleteClick(View view){
        employeePresenter.deleteEmployee(edName.getText().toString().trim());
    }

    public void deleteWithSkillClick(View view){
        employeePresenter.deleteEmployeeBySkill(edSkill.getText().toString().trim());
    }

    public void getWithAgeClick(View view){
        employeePresenter.getEmployeesByAge(Integer.parseInt(edAge.getText().toString().trim()));
    }

    private Employee employee(){
        Employee employee = new Employee();
        Skill skill = new Skill();
        skill.skill = edSkill.getText().toString();
        employee.name = edName.getText().toString();
        employee.age = Integer.parseInt(edAge.getText().toString());
        employee.skills.add(skill);

        return employee;
    }

    @Override
    public void showEmployees(List<Employee> employees) {
        tvData.setText("");
        for (Employee employee: employees){
            tvData.append(employee.name + " age: "+ employee.age + " skill: "+employee.skills.size() + "\n");
        }
    }

    @Override
    public void showSaveSuccess() {
        Toast.makeText(this, "Success", Toast.LENGTH_LONG).show();
    }

    @Override
    public void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }


}
