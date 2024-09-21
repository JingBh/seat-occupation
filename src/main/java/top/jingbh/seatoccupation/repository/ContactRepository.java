package top.jingbh.seatoccupation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import top.jingbh.seatoccupation.entity.Contact;
import top.jingbh.seatoccupation.entity.User;
import top.jingbh.seatoccupation.enums.ContactTypeEnum;

import java.util.Optional;

public interface ContactRepository extends JpaRepository<Contact, Long> {

    Optional<Contact> findFirstByUserAndType(User user, ContactTypeEnum type);
}
