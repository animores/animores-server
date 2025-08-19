package animores.serverapi.pet.repository.impl;

import static animores.serverapi.pet.entity.QPet.pet;
import static animores.serverapi.pet.entity.QPetImage.petImage;

import animores.serverapi.pet.dao.PetDao;
import animores.serverapi.pet.repository.PetCustomRepository;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PetCustomRepositoryImpl implements PetCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<PetDao> findAllByAccount_IdWithImages(String id) {
        return jpaQueryFactory.select(Projections.constructor(
                PetDao.class,
                pet.id,
                pet.name,
                petImage.url
            ))
            .from(pet)
            .join(pet.image, petImage)
            .where(pet.account.id.eq(id))
            .fetch();
    }
}
