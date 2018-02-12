package pl.maciejpajak.api;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pl.maciejpajak.domain.game.Sport;
import pl.maciejpajak.repository.SportRepository;

@RestController
@RequestMapping("/sports")
public class SportApi {
    
    private final SportRepository sportRepository;
    
    @Autowired
    public SportApi(SportRepository sportRepository) {
        this.sportRepository = sportRepository;
    }

    @GetMapping("/all")
    public Collection<Sport> getAllSports() {
        return sportRepository.findAllByVisible(true);
    }
    
}
