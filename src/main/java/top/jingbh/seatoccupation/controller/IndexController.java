package top.jingbh.seatoccupation.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.view.RedirectView;
import top.jingbh.seatoccupation.service.UserService;

@Controller
public class IndexController {

    private final UserService userService;

    public IndexController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public RedirectView index() {
        if (userService.getCurrent() != null) {
            return new RedirectView("/user");
        }

        return new RedirectView("https://github.com/JingBh/seat-occupation");
    }
}
