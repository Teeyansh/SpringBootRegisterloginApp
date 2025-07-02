package in.tss.main.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import in.tss.main.dto.LoginDTO;
import in.tss.main.entities.User;
import in.tss.main.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import jakarta.validation.Valid;

@Controller
public class MyController {

    @Autowired
    private UserService userService;

 
    @GetMapping("/regPage")
    public String openRegpage(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/regForm")
    public String submitRegForm(@Valid @ModelAttribute("user") User user,
                                BindingResult result,
                                Model model) {
        if (result.hasErrors()) {
            return "register";
        }

        boolean status = userService.registerUser(user);

        if (status) {
            model.addAttribute("successMsg", "User registered successfully");
        } else {
            model.addAttribute("errorMsg", "User not registered due to some error");
        }
        return "register";
    }

    @GetMapping("/loginPage")
    public String openLoginPage(Model model) {
        model.addAttribute("loginDTO", new LoginDTO()); 
        return "login";
    }


    @PostMapping("/loginForm")
    public String submitLoginForm(
            @Valid @ModelAttribute("loginDTO") LoginDTO loginDTO,
            BindingResult result,
            Model model,
            HttpServletRequest request
    ) {
        System.out.println("✅ Login form submitted");
        System.out.println("Email: " + loginDTO.getEmail());
        System.out.println("Password: " + loginDTO.getPassword());

        if (result.hasErrors()) {
            result.getAllErrors().forEach(e -> System.out.println("❌ " + e.getDefaultMessage()));
            return "login";
        }

        User validUser = userService.loginUser(loginDTO.getEmail(), loginDTO.getPassword());

        if (validUser != null) {
            HttpSession session = request.getSession();
            session.setAttribute("loggedInUser", validUser.getName());
            model.addAttribute("modelName", validUser.getName());
            model.addAttribute("loggedInUser", validUser);
            return "profile";
        } else {
            model.addAttribute("errorMsg", "Email and password did not match");
            return "login";
        }
    }


    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return "redirect:/loginPage";
    }
}
