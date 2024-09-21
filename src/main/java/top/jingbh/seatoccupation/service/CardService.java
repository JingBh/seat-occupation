package top.jingbh.seatoccupation.service;

import org.springframework.stereotype.Service;
import top.jingbh.seatoccupation.entity.OccupationCard;
import top.jingbh.seatoccupation.enums.OccupationStatusEnum;
import top.jingbh.seatoccupation.repository.OccupationCardRepository;

import java.time.Instant;
import java.util.Optional;

@Service
public class CardService {

    private final OccupationCardRepository cardRepository;

    public CardService(OccupationCardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    public Optional<OccupationCard> getCardByUid(String uid) {
        OccupationCard card = cardRepository.findFirstByUserIdentityUid(uid).orElse(null);
        if (card != null) {
            if (card.getStatus() == OccupationStatusEnum.TEMPORARY_LEAVE &&
                card.getReturnsAt().isBefore(Instant.now().minusSeconds(3600))) {
                card.setStatus(OccupationStatusEnum.USING);
            }
            if (card.getStatus() == OccupationStatusEnum.USING &&
                card.getLeavesAt().isBefore(Instant.now().minusSeconds(3600))) {
                card.setStatus(OccupationStatusEnum.NOT_USING);
            }
            cardRepository.save(card);
        }
        return Optional.ofNullable(card);
    }
}
