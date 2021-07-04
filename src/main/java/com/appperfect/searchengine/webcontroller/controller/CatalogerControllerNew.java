package com.appperfect.searchengine.webcontroller.controller;

//import com.appperfect.searchengine.constants.Messages;
//import com.appperfect.searchengine.service.CatalogerService;

import com.appperfect.searchengine.constants.Messages;
import com.appperfect.searchengine.dto.AuthenticationRequest;
import com.appperfect.searchengine.dto.AuthenticationResponse;
import com.appperfect.searchengine.dto.MyCatalogerDetails;
import com.appperfect.searchengine.model.Cataloger;
import com.appperfect.searchengine.service.CatalogerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

// decide application flow control
@Controller
public class CatalogerControllerNew {


    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CatalogerService catalogerService;

    @Autowired
    private Messages messages;

    @Autowired
    private AuthenticationManager authenticationManager;

    // register a new cataloger in database with encrypted password
//    @PostMapping("/register")
//    public String registerCataloger(@RequestBody Cataloger cataloger) {
//        Cataloger cataloger1 = catalogerService.findCatalogerByEmail(cataloger.getEmail());
//        if (cataloger1 == null) {
//            String encodedPassword = passwordEncoder.encode(cataloger.getPassword());
//            cataloger.setPassword(encodedPassword);
//            catalogerService.registerCataloger(cataloger);
//            return messages.catalogerSuccessMsg;
//        } else {
//            return messages.catalogerFailureMsg;
//        }
//    }

    // authenticate a cataloger using userName and password
    @GetMapping("/showMyLoginPage")
    public String showLogin(Model model){
        model.addAttribute("auth",new AuthenticationRequest());
        return "login-page";
    }
    // return a unique JSon Web Token(Jwt)
    /*@PostMapping("/login")
    public ResponseEntity<?> createAuthenticationToken(@RequestParam("authReq") AuthenticationRequest authenticationRequest) throws Exception {

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail(), authenticationRequest.getPassword())
            );
        } catch (BadCredentialsException e) {
            throw new Exception("Incorrect credentials", e);
        }



        return ResponseEntity.ok(new AuthenticationResponse(jwt));
    }*/
    @RequestMapping("/")
    public String showHome(){
        System.out.println("hello");
        return "search-home";
    }
//    @GetMapping("/showMyLoginPage")
//    public String showLogin(Model model){
//        model.addAttribute("login",new MyCatalogerDetails())
//        return "login-page";
//    }
    @GetMapping("/register")
    public String showRegister(Model model){
        model.addAttribute("cat",new Cataloger());
        return "register-form";
    }

    // register a new cataloger in database with encrypted password
    @PostMapping("/add-user")
    public String registerCataloger(@ModelAttribute("cat") Cataloger cataloger) {
        Cataloger cataloger1 = catalogerService.findCatalogerByEmail(cataloger.getEmail());
        if (cataloger1 == null) {
            String encodedPassword = passwordEncoder.encode(cataloger.getPassword());
            cataloger.setPassword(encodedPassword);
            catalogerService.registerCataloger(cataloger);
            return "redirect:/";
//            return messages.catalogerSuccessMsg;
        } else {
            return messages.catalogerFailureMsg;
        }
    }
//    @GetMapping("/test")
//    public String main() {
//        return "testFile";
//    }
//
//    @PostMapping("/test")
//    public String upload(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
//        System.out.println("uploaded");
//        redirectAttributes.addFlashAttribute("message", "You successfully uploaded " + file.getOriginalFilename() + "!");
//
//        return "redirect:/";
//    }
}