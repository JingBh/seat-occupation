package top.jingbh.seatoccupation.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import top.jingbh.seatoccupation.enums.OccupationStatusEnum;

import java.time.Instant;

@Entity
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class OccupationCard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToOne(mappedBy = "card", fetch = FetchType.EAGER, optional = false)
    private User user;

    @Column(nullable = false)
    @ColumnDefault("0")
    @Enumerated(EnumType.ORDINAL)
    private OccupationStatusEnum status = OccupationStatusEnum.NOT_USING;

    private String matter;

    private Instant returnsAt;

    private Instant leavesAt;

    @CreationTimestamp
    private Instant createdAt;

    @UpdateTimestamp
    private Instant updatedAt;
}
