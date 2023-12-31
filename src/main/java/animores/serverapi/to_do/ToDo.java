package animores.serverapi.to_do;

import animores.serverapi.config.BaseEntity;
import animores.serverapi.user.Account;
import animores.serverapi.pet.Pet;
import animores.serverapi.user.Profile;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Entity
@NoArgsConstructor
public class ToDo extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private String id;

	@ManyToOne(fetch = FetchType.LAZY)
	private Account account;
	@ManyToOne(fetch = FetchType.LAZY)
	private Pet pet;
	@OneToOne(fetch = FetchType.LAZY)
	@Column(name="create_profile_id")
	private Profile createProfile;
	private Tag tag;
	private String title;
	private LocalDate date;
	private LocalTime time;

	//반복에 대한 내용 추가
	


}