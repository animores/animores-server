package animores.serverapi.pet.repository.impl;

import animores.serverapi.pet.dao.PetDao;
import animores.serverapi.pet.repository.PetCustomRepository;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static animores.serverapi.pet.entity.QBreed.breed;
import static animores.serverapi.pet.entity.QPet.pet;

@RequiredArgsConstructor
public class PetCustomRepositoryImpl implements PetCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<PetDao> findAllByAccount_IdWithImages(Long id) {
        return jpaQueryFactory.select(Projections.constructor(
                        PetDao.class,
                        pet.id,
                        pet.name,
                        breed.defaultImageUrl
                ))
                .from(pet)
                .join(pet.breed, breed)
                .where(pet.account.id.eq(id))
                .fetch();
    }
}
