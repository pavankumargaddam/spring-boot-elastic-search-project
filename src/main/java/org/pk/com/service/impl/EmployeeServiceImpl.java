package org.pk.com.service.impl;

import org.pk.com.domain.Employee;
import org.pk.com.exception.ResourceNotFoundException;
import org.pk.com.repository.EmployeeRepository;
import org.pk.com.repository.search.EmployeeSearchRepository;
import org.pk.com.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static java.util.Objects.nonNull;

@Service
@Transactional
public class EmployeeServiceImpl implements EmployeeService {
    private static final Logger LOG = LoggerFactory.getLogger(EmployeeServiceImpl.class);

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EmployeeSearchRepository employeeSearchRepository;

    @Override
    public List<Employee> getAllEmployees() {
        LOG.debug("getAllEmployees method started");
        List<Employee> employeeList = new ArrayList<>();
        //Fetching the record from elastic search
        Iterable<Employee> employeeIterable = employeeSearchRepository.findAll();
        Iterator<Employee> iterator = employeeIterable.iterator();
        while(iterator.hasNext()){
            employeeList.add(iterator.next());
        }
        return employeeList;
    }

    @Override
    public Employee getEmployeeById(Long id) throws ResourceNotFoundException {
        LOG.debug("getEmployeeById method started {}",id);
        //Fetching the record from elastic search
        Employee employee = employeeSearchRepository.findById(id).orElse(null);
        if (nonNull(employee))
            return employee;
        else
            throw new ResourceNotFoundException("No employee record exist for given id");
    }

    @Override
    public Employee createOrUpdateEmployee(Employee employee) {
        LOG.debug("createOrUpdateEmployee method started {}",employee);
        //Fetching the record from elastic search
        Employee employeeRecord = employeeSearchRepository.findById(employee.getId()).orElse(null);
        Employee result;
        if (nonNull(employeeRecord)) {
            LOG.debug("update the record");
            employeeRecord.setEmail(employee.getEmail());
            employeeRecord.setFirstName(employee.getFirstName());
            employeeRecord.setLastName(employee.getLastName());
            result = employeeRepository.save(employeeRecord);
            //saving the result into elastic index employee
            employeeSearchRepository.save(result);
            return result;
        } else {
            LOG.debug("save the record");
            result =  employeeRepository.save(employee);
            //saving the result into elastic index employee
            employeeSearchRepository.save(result);
            return result;
        }
    }

    @Override
    public void deleteEmployeeById(Long id) throws ResourceNotFoundException {
        LOG.debug("deleteEmployeeById method started {}",id);
        Employee employee = employeeRepository.findById(id).orElse(null);
        if (nonNull(employee)) {
            employeeRepository.deleteById(id);
            //deleting the record from elastic index employee
            employeeSearchRepository.deleteById(id);
        }
        else
            throw new ResourceNotFoundException("No employee record exist for given id");
    }
}
