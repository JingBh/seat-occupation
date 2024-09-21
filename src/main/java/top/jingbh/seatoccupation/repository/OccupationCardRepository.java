package top.jingbh.seatoccupation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import top.jingbh.seatoccupation.entity.OccupationCard;

import java.util.Optional;

public interface OccupationCardRepository extends JpaRepository<OccupationCard, Long> {

    Optional<OccupationCard> findFirstByUserIdentityUid(String uid);
}
