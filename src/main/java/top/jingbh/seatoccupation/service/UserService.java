package top.jingbh.seatoccupation.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Service;
import top.jingbh.seatoccupation.dto.UserFormDto;
import top.jingbh.seatoccupation.entity.Contact;
import top.jingbh.seatoccupation.entity.Identity;
import top.jingbh.seatoccupation.entity.OccupationCard;
import top.jingbh.seatoccupation.entity.User;
import top.jingbh.seatoccupation.enums.ContactTypeEnum;
import top.jingbh.seatoccupation.enums.OccupationStatusEnum;
import top.jingbh.seatoccupation.repository.ContactRepository;
import top.jingbh.seatoccupation.repository.IdentityRepository;
import top.jingbh.seatoccupation.repository.OccupationCardRepository;
import top.jingbh.seatoccupation.repository.UserRepository;
import top.jingbh.seatoccupation.util.DateTimeUtils;

import java.util.Collection;

@Service
public class UserService {

    private final ContactRepository contactRepository;

    private final IdentityRepository identityRepository;

    private final OccupationCardRepository cardRepository;

    private final UserRepository userRepository;

    public UserService(
        ContactRepository contactRepository,
        IdentityRepository identityRepository,
        OccupationCardRepository cardRepository,
        UserRepository userRepository
    ) {
        this.contactRepository = contactRepository;
        this.identityRepository = identityRepository;
        this.cardRepository = cardRepository;
        this.userRepository = userRepository;
    }

    @Nullable
    public User getCurrent() {
        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth instanceof OAuth2AuthenticationToken &&
            auth.isAuthenticated() &&
            StringUtils.isNotBlank(auth.getName())) {
            return getOrCreate(auth.getName());
        }
        return null;
    }

    public User getOrCreate(String subject) {
        return userRepository.findById(subject)
            .orElseGet(() -> {
                User user = new User();
                user.setSubject(subject);
                user = userRepository.save(user);

                OccupationCard card = new OccupationCard();
                user.setCard(card);
                card.setUser(user);
                cardRepository.save(card);

                return user;
            });
    }

    public UserFormDto getForm(User user) {
        UserFormDto dto = new UserFormDto();

        final OccupationCard card = user.getCard();
        if (card != null) {
            dto.setOccupationStatus(user.getCard().getStatus());
            dto.setOccupationMatter(user.getCard().getMatter());
            dto.setOccupationReturnsAt(DateTimeUtils.instantToLocalDateTime(user.getCard().getReturnsAt()));
            dto.setOccupationLeavesAt(DateTimeUtils.instantToLocalDateTime(user.getCard().getLeavesAt()));
        }

        final Identity identity = user.getIdentity();
        if (identity != null) {
            dto.setIdentityUid(identity.getUid());
            dto.setIdentityName(identity.getName());
            dto.setIdentityDepartment(identity.getDepartment());
        }

        final Collection<Contact> contacts = user.getContacts();
        if (contacts != null) {
            contacts.forEach(contact -> {
                switch (contact.getType()) {
                    case PHONE -> dto.setContactPhone(contact.getValue());
                    case WECHAT -> dto.setContactWechat(contact.getValue());
                }
            });
        }

        return dto;
    }

    public void saveForm(User user, UserFormDto dto) {
        if (dto.getOccupationStatus() != null) {
            user.getCard().setStatus(dto.getOccupationStatus());
            switch (dto.getOccupationStatus()) {
                case OccupationStatusEnum.NOT_USING -> {
                    user.getCard().setMatter(null);
                    user.getCard().setReturnsAt(null);
                    user.getCard().setLeavesAt(null);
                }
                case OccupationStatusEnum.TEMPORARY_LEAVE -> {
                    user.getCard().setMatter(dto.getOccupationMatter());
                    user.getCard().setReturnsAt(DateTimeUtils.localDateTimeToInstant(dto.getOccupationReturnsAt()));
                    user.getCard().setLeavesAt(DateTimeUtils.localDateTimeToInstant(dto.getOccupationLeavesAt()));
                }
                case OccupationStatusEnum.USING -> {
                    user.getCard().setMatter(null);
                    user.getCard().setReturnsAt(null);
                    user.getCard().setLeavesAt(DateTimeUtils.localDateTimeToInstant(dto.getOccupationLeavesAt()));
                }
            }
            cardRepository.save(user.getCard());
        }

        if (StringUtils.isNotBlank(dto.getIdentityUid())) {
            Identity identity = identityRepository
                .findById(dto.getIdentityUid())
                .orElseGet(() -> {
                    Identity newIdentity = new Identity();
                    newIdentity.setUid(dto.getIdentityUid());
                    return newIdentity;
                });
            identity.setName(dto.getIdentityName());
            identity.setDepartment(dto.getIdentityDepartment());
            identityRepository.save(identity);
            user.setIdentity(identity);
            userRepository.save(user);
        }

        if (StringUtils.isNotBlank(dto.getContactPhone())) {
            Contact contact = contactRepository
                .findFirstByUserAndType(user, ContactTypeEnum.PHONE)
                .orElseGet(() -> {
                    Contact newContact = new Contact();
                    newContact.setUser(user);
                    newContact.setType(ContactTypeEnum.PHONE);
                    return newContact;
                });
            contact.setValue(dto.getContactPhone());
            contactRepository.save(contact);
        }
        if (StringUtils.isNotBlank(dto.getContactWechat())) {
            Contact contact = contactRepository
                .findFirstByUserAndType(user, ContactTypeEnum.WECHAT)
                .orElseGet(() -> {
                    Contact newContact = new Contact();
                    newContact.setUser(user);
                    newContact.setType(ContactTypeEnum.WECHAT);
                    return newContact;
                });
            contact.setValue(dto.getContactWechat());
            contactRepository.save(contact);
        }
    }
}
