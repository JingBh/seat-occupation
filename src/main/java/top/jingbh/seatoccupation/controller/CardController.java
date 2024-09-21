package top.jingbh.seatoccupation.controller;

import org.springframework.boot.info.BuildProperties;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;
import top.jingbh.seatoccupation.entity.OccupationCard;
import top.jingbh.seatoccupation.service.CardService;

@Controller
public class CardController {

    private final BuildProperties buildProperties;

    private final CardService cardService;

    public CardController(
        BuildProperties buildProperties,
        CardService cardService
    ) {
        this.buildProperties = buildProperties;
        this.cardService = cardService;
    }

    @GetMapping("/cards/{uid}")
    public String show(
        Model model,
        @PathVariable String uid
    ) {
        final OccupationCard card = cardService.getCardByUid(uid).orElseThrow(() ->
            new ResponseStatusException(HttpStatus.NOT_FOUND, "card not found")
        );

        model.addAttribute("card", card);
        model.addAttribute("version", buildProperties.getVersion());

        return "card";
    }
}
