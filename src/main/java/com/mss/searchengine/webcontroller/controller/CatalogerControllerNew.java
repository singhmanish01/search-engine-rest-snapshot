package com.mss.searchengine.webcontroller.controller;

//import com.mss.searchengine.constants.Messages;
//import com.mss.searchengine.service.CatalogerService;

import com.mss.searchengine.constants.Messages;
import com.mss.searchengine.dto.AuthenticationRequest;
import com.mss.searchengine.dto.AuthenticationResponse;
import com.mss.searchengine.dto.MyCatalogerDetails;
import com.mss.searchengine.model.Cataloger;
import com.mss.searchengine.service.CatalogerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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

    //perform logout
    @RequestMapping("/logout")
    public String doLogout(HttpServletRequest request, HttpServletResponse response){        
        HttpSession session = request.getSession(false);
        SecurityContextHolder.clearContext();
        session = request.getSession(false);
        if (session != null)
            session.invalidate();
        for (Cookie cookie : request.getCookies())
            cookie.setMaxAge(0);
        return "redirect:/";
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
