package com.mss.searchengine.service;

import com.mss.searchengine.model.Cataloger;
import com.mss.searchengine.dto.MyCatalogerDetails;
import com.mss.searchengine.repository.CatalogerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

// interact between controller and repository
// here we provide validation logic on data
@Service
public class CatalogerService implements UserDetailsService {

    @Autowired
    private CatalogerRepository catalogerRepository;

    // fetch cataloger from database by email
    // return an object of UserDetails
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Cataloger> cataloger = catalogerRepository.findByEmail(email);
        System.out.println("I'm run ehehe" + email);
        cataloger.orElseThrow(() -> new UsernameNotFoundException("Not Found: " + email));

        return cataloger.map(MyCatalogerDetails::new).get();
    }

    // add a cataloger to database
    public void registerCataloger(Cataloger cataloger) {
        catalogerRepository.save(cataloger);
    }

    // return the email of cataloger using email
    public String getEmail(String email) {
        return catalogerRepository.findEmailByEmail(email);
    }

    public Cataloger findCatalogerByEmail(String email){
        Optional<Cataloger> optionalCataloger = catalogerRepository.findByEmail(email);
        return optionalCataloger.orElse(null);
    }

}
