package top.jingbh.seatoccupation.controller;

import io.micrometer.common.util.StringUtils;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import top.jingbh.seatoccupation.dto.UserFormDto;
import top.jingbh.seatoccupation.entity.User;
import top.jingbh.seatoccupation.enums.OccupationStatusEnum;
import top.jingbh.seatoccupation.service.UserService;

@Controller
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/user")
    public String index(Model model) {
        final User user = userService.getCurrent();
        model.addAttribute("form", userService.getForm(user));
        return "user";
    }

    @PostMapping("/user")
    public String save(
        Model model,
        @Valid @ModelAttribute("form") UserFormDto dto,
        BindingResult bindingResult
    ) {
        if (dto.getOccupationStatus() == OccupationStatusEnum.TEMPORARY_LEAVE &&
            StringUtils.isBlank(dto.getOccupationMatter())) {
            bindingResult.rejectValue("occupationMatter", "notBlank", "不能为空");
        }
        if (dto.getOccupationStatus() == OccupationStatusEnum.TEMPORARY_LEAVE &&
            dto.getOccupationReturnsAt() == null) {
            bindingResult.rejectValue("occupationReturnsAt", "notBlank", "不能为空");
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("form", dto);
            return "user";
        }

        final User user = userService.getCurrent();
        userService.saveForm(user, dto);
        return "redirect:/user";
    }
}
