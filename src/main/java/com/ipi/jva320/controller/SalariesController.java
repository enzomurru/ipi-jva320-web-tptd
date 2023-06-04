package com.ipi.jva320.controller;

import com.ipi.jva320.exception.SalarieException;
import com.ipi.jva320.model.SalarieAideADomicile;
import com.ipi.jva320.service.SalarieAideADomicileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@Controller
public class SalariesController {
    @Autowired
    SalarieAideADomicileService salarieAideADomicileService;

    @GetMapping("/salaries/{id}")
    public String getSalariesDetails(@PathVariable Long id, final ModelMap model) {
        SalarieAideADomicile aide;
        aide = salarieAideADomicileService.getSalarie(id);
        if (aide == null) {
            return "redirect:/home";
        } else {
            model.put("details", aide);
            model.addAttribute("salarie", aide);
            System.out.println(aide);
            return "detail_Salarie";
        }
    }

    @PostMapping("/salaries/save")
    public String addNewSalarie(@ModelAttribute SalarieAideADomicile salarieAideADomicile, final ModelMap model) throws SalarieException {
        salarieAideADomicileService.creerSalarieAideADomicile(salarieAideADomicile);
        model.addAttribute("salariees", salarieAideADomicileService.getSalaries());
        return "list";
    }

    @PostMapping("/salaries/update")
    public String updateSalarie(@ModelAttribute SalarieAideADomicile salarieAideADomicile, final ModelMap model, @RequestParam(required = false) Long id) throws SalarieException {
        if (id != null) {
            salarieAideADomicile.setId(id);
        }
        System.out.println(salarieAideADomicile.getId());
        salarieAideADomicileService.updateSalarieAideADomicile(salarieAideADomicile);
        model.addAttribute("salariees", salarieAideADomicileService.getSalaries());
        return "list";
    }

    @RequestMapping("/salaries")
    public String redirectToListWithParams(@RequestParam(required = false, name = "nom") String nom,
                                           @RequestParam(required = false, name = "page", defaultValue = "1") int page,
                                           @RequestParam(required = false, name = "size", defaultValue = "10") int size,
                                           @RequestParam(required = false, name = "sortDirection", defaultValue = "desc") String sortDirection,
                                           @RequestParam(required = false, name = "sortProperty", defaultValue = "id") String sortProperty,
                                           final ModelMap model) {
        Pageable pageable;
        if (Objects.equals(sortDirection, "asc")) {
            pageable = PageRequest.of(page, size, Sort.by(sortProperty).descending());
        } else {
            pageable = PageRequest.of(page, size, Sort.by(sortProperty).ascending());
        }
        if (nom == null || nom.length() <= 0) {
            Page itemPages = salarieAideADomicileService.getSalaries(pageable);
            model.addAttribute("salariees", itemPages.getContent());
        } else {
            model.addAttribute("salariees", salarieAideADomicileService.getSalaries(nom));
        }
        model.put("page", page);
        model.put("sortdir", sortDirection);
        model.put("sortpro", sortProperty);
        if(page == 0) {
            model.put("disable", true);
        } else {
            model.put("disable", false);
        }

        return "list";
    }

    @RequestMapping("/salaries/aide/new")
    public String redirectToAdd(final ModelMap model) {
        SalarieAideADomicile aide = new SalarieAideADomicile();
        aide.setId(0L);
        model.put("details", aide);
        model.put("action", "save");
        model.addAttribute("salarie", new SalarieAideADomicile());
        return "detail_Salarie";
    }

    @GetMapping("/salaries/{id}/delete")
    public String deleteSalarie(@PathVariable Long id) throws SalarieException {
        salarieAideADomicileService.deleteSalarieAideADomicile(id);
        return "redirect:/home";
    }
}
