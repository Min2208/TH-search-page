package com.codegym.cms.controller;

import com.codegym.cms.model.Customer;
import com.codegym.cms.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.data.domain.Pageable;

import java.util.Optional;


@Controller
@RequestMapping("customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;


    @GetMapping
    public ModelAndView searchCustomer(@RequestParam("search") Optional<String> name, Pageable pageable){
        Sort sort= new Sort(new Sort.Order(Sort.Direction.ASC, "email"));
        pageable= new PageRequest(pageable.getPageNumber(),5,sort);
        Page<Customer> customers;
        System.out.println(name.isPresent());
        ModelAndView modelAndView=new ModelAndView("customers/list");
        if (name.isPresent()){
            customers=customerService.search(name.get(),pageable);
        }else {
            customers=customerService.findAll(pageable);
        }
        modelAndView.addObject("customers", customers);

        return modelAndView;


    }

}
