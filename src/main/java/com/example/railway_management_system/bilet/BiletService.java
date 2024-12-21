package com.example.railway_management_system.bilet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Service
public class BiletService {

    private final BiletRepository biletRepository;

    @Autowired
    public BiletService(BiletRepository biletRepository) {
        this.biletRepository = biletRepository;
    }

    public List<Bilet> getBilete(Long utilizatorId) {
        return biletRepository.findBileteForUtilizator(utilizatorId);
    }

    public void stergereBilet(Long biletId) {
        biletRepository.deleteById(biletId);
    }
}
