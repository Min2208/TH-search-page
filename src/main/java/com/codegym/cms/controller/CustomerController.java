package com.codegym.cms.controller;

import com.codegym.cms.model.Customer;
import com.codegym.cms.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.data.domain.Pageable;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Optional;


@RestController
public class CustomerController {

    @Autowired
    private CustomerService customerService;


    @GetMapping("/customers")
    public ModelAndView searchCustomer(@RequestParam("search") Optional<String> name, Pageable pageable){
        Sort sort= new Sort(new Sort.Order(Sort.Direction.ASC, "email"));
        pageable= new PageRequest(pageable.getPageNumber(),5,sort);
        Page<Customer> customers;

        ModelAndView modelAndView=new ModelAndView("customers/list");
        if (name.isPresent()){
            customers=customerService.search(name.get(),pageable);
        }else {
            customers=customerService.findAll(pageable);
        }
        modelAndView.addObject("customers", customers);

        return modelAndView;


    }
    //Demo thu hanh Web service

    @RequestMapping(value = "/customers", method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Customer>> listAllCustomers() {
        List<Customer> customers = customerService.findAll();
        if (customers.isEmpty()) {
            return new ResponseEntity<List<Customer>>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<List<Customer>>(customers, HttpStatus.OK);
    }

//    @RequestMapping(value = "/customers/", method = RequestMethod.POST)
//    public ResponseEntity<Void> createCustomer(@RequestBody Customer customer, UriComponentsBuilder ucBuilder) {
//        customerService.save(customer);
//        HttpHeaders headers = new HttpHeaders();
//        headers.setLocation(ucBuilder.path("/customers/{id}").buildAndExpand(customer.getId()).toUri());
//        return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
//    }

    @RequestMapping(value = "/customers", method = RequestMethod.POST)
    public ResponseEntity<Void> addCustomer(@RequestBody Customer customer){

        customerService.save(customer);
        return new ResponseEntity<Void>(HttpStatus.CREATED);
    }

    @RequestMapping(value = "/customers/{id}",method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteCustomer(@PathVariable("id") long id){
        customerService.delete(id);
        return new ResponseEntity<Void>(HttpStatus.MOVED_PERMANENTLY);
    }
    @RequestMapping(value = "/customers/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Customer> updateCustomer(@PathVariable("id") long id, @RequestBody Customer customer) {
        System.out.println("Updating Customer " + id);

        Customer currentCustomer = customerService.findOne(id);

        if (currentCustomer == null) {
            System.out.println("Customer with id " + id + " not found");
            return new ResponseEntity<Customer>(HttpStatus.NOT_FOUND);
        }

        currentCustomer.setName(customer.getName());
        currentCustomer.setEmail(customer.getEmail());
        currentCustomer.setId(customer.getId());
        currentCustomer.setAddress(customer.getAddress());

        customerService.save(currentCustomer);
        return new ResponseEntity<Customer>(currentCustomer, HttpStatus.OK);
    }

}
