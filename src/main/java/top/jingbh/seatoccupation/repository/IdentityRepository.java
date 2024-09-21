package top.jingbh.seatoccupation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import top.jingbh.seatoccupation.entity.Identity;

public interface IdentityRepository extends JpaRepository<Identity, String> {
}
