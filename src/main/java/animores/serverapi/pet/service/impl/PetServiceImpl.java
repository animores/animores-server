package animores.serverapi.pet.service.impl;

import animores.serverapi.pet.repository.PetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PetServiceImpl {
    private final PetRepository petRepository;
}
