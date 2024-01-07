package animores.serverapi.to_do;

import animores.serverapi.config.BaseEntity;
import animores.serverapi.to_do.dto.request.ToDoCreateRequest;
import animores.serverapi.user.Account;
import animores.serverapi.user.Profile;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor
public class ToDo extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private String id;
	@ManyToOne(fetch = FetchType.LAZY)
	private Account account;
	@OneToMany
	private List<PetToDoRelationship> pets;
	@OneToOne(fetch = FetchType.LAZY)
	@Column(name="create_profile_id")
	private Profile createProfile;
	private Tag tag;
	private String title;
	private LocalDate date;
	private LocalTime time;

	//반복에 대한 내용 추가

	public static ToDo fromRequest(ToDoCreateRequest request, Account account) {
		ToDo toDo = new ToDo();
		toDo.title = request.title();
		toDo.account = account;
		toDo.date = request.date();
		toDo.time = request.time();
		return toDo;
	}
}