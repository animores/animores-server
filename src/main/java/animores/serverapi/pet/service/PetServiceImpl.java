package animores.serverapi.pet.service;

import animores.serverapi.common.exception.CustomException;
import animores.serverapi.common.exception.ExceptionCode;
import animores.serverapi.pet.domain.Pet;
import animores.serverapi.pet.repository.PetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class PetServiceImpl implements PetService {

    private final PetRepository petRepository;

    @Override
    @Transactional(readOnly = true)
    public void checkAccountPets(Long accountId, List<Long> petIds) {

        Set<Long> petIdsSet = new HashSet<>(petIds);

        petRepository.findAllByAccount_id(accountId).stream()
            .map(Pet::getId)
            .forEach(petId -> {
                if (!petIdsSet.contains(petId)) {
                    throw new CustomException(ExceptionCode.ILLEGAL_PET_IDS);
                }
            });
    }
}
