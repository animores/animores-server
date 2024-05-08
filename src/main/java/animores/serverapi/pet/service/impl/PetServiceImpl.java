package animores.serverapi.pet.service.impl;

import animores.serverapi.common.exception.CustomException;
import animores.serverapi.common.exception.ExceptionCode;
import animores.serverapi.pet.entity.Pet;
import animores.serverapi.pet.repository.PetRepository;
import animores.serverapi.pet.service.PetService;
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
    public List<Pet> checkAccountPets(Long accountId, List<Long> petIds) {
        List<Pet> pets = petRepository.findAllByAccount_id(accountId);

        if(petIds.isEmpty()){
            return pets;
        } else {
            Set<Long> petIdsSet = new HashSet<>(petIds);
            pets.forEach(pet -> {
                if(!petIdsSet.contains(pet.getId())){
                    throw new CustomException(ExceptionCode.ILLEGAL_PET_IDS);
                }
            });

            return pets.stream().filter(pet -> petIdsSet.contains(pet.getId())).toList();
        }
    }
}
