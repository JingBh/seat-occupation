package top.jingbh.seatoccupation.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import top.jingbh.seatoccupation.enums.OccupationStatusEnum;

import java.time.LocalDateTime;

@Data
public class UserFormDto {

    private OccupationStatusEnum occupationStatus;

    private String occupationMatter;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime occupationReturnsAt;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime occupationLeavesAt;

    private String identityUid;

    private String identityName;

    private String identityDepartment;

    private String contactPhone;

    private String contactWechat;
}
