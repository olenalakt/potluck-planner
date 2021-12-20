package com.example.springbootui.controller;

import com.example.springbootui.model.SupportForm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

@Controller
@Slf4j
public class SupportFormController {

    private static final List<String> membershipList = Arrays.asList("Platinum", "Gold", "Silver");

    @GetMapping("/support")
    public String complaintForm(Model model) {

        model.addAttribute("supportDetails", new SupportForm());
        model.addAttribute("membershipList", membershipList);

        return "support";
    }

    @PostMapping("/support")
    public String submitComplaint(@Valid @ModelAttribute("supportDetails") SupportForm supportDetails,
                                  BindingResult bindingResult, Model model) {

        model.addAttribute("supportDetails", supportDetails);
        model.addAttribute("membershipList", membershipList);

        if ( bindingResult.hasErrors()) {
            log.error("submitComplaint validation fails: supportDetails={}", supportDetails);
            return "support";
        }
        return "submit";
    }
}
