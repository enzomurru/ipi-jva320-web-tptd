package com.ipi.jva320.controller;

import com.ipi.jva320.service.SalarieAideADomicileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {

    @Autowired
    SalarieAideADomicileService salarieAideADomicileService;

    @RequestMapping(value = {"/", "/home", "/home.html"})
    public String redirectToHome(final ModelMap model) {
        model.put("nbSalaries", Long.toString(salarieAideADomicileService.countSalaries()));
        return "home";
    }



}
