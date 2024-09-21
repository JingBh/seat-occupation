package top.jingbh.seatoccupation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import top.jingbh.seatoccupation.entity.User;

public interface UserRepository extends JpaRepository<User, String> {
}
