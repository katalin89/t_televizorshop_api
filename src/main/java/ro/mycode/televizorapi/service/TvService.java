package ro.mycode.televizorapi.service;


import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import ro.mycode.televizorapi.dtos.TelevizorDTO;
import ro.mycode.televizorapi.exceptii.ExceptieTvDBEmpty;
import ro.mycode.televizorapi.exceptii.ExistingTv;
import ro.mycode.televizorapi.exceptii.TvNotFoundException;
import ro.mycode.televizorapi.model.Televizor;
import ro.mycode.televizorapi.repository.TelevizorRepo;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class TvService {
    private TelevizorRepo televizorRepo;

    public TvService(TelevizorRepo televizorRepo) {
        this.televizorRepo = televizorRepo;
    }

    public List<Televizor> getAllTelevizoare() throws ExceptieTvDBEmpty {
        List<Televizor> televizoare = televizorRepo.findAll();
        if (televizoare.size() > 0) {
            return televizoare;
        }

        throw new ExceptieTvDBEmpty();
    }

    public void deleteTelevizorByModel(String model) throws TvNotFoundException {
        Televizor byModel = televizorRepo.findTelevizorByModel(model);
        if (byModel != null) {
            televizorRepo.deleteTelevizorByModel(model);
        } else {
            throw new TvNotFoundException();
        }

    }

    public void deleteTelevizorById(Long id) throws TvNotFoundException {
        Optional<Televizor> byId = televizorRepo.findById((id));
        if (byId.isPresent()) {
            televizorRepo.delete(byId.get());
        } else {
            throw new TvNotFoundException();
        }

    }

    /**/
    @Transactional
    @Modifying
    public void addTelevizor(Televizor televizor) throws TvNotFoundException {

        List<Televizor> tvWith = televizorRepo.findTvWith(televizor.getMarca(), televizor.getModel());

        if (tvWith.size() > 0) {
            throw new ExistingTv();
        }
        this.televizorRepo.saveAndFlush(televizor);

    }

    @Transactional
    @Modifying
    public  void sortByMarca(String marca)throws ExceptieTvDBEmpty{

        List<Televizor>televizoare=televizorRepo.getAllTelevizoare();
        if (televizoare.size()>0){
            televizoare=televizorRepo.sortByMarca();
        }else throw new ExceptieTvDBEmpty();

    }

    @Transactional
    @Modifying
    public  void sortByModel(String model) throws ExceptieTvDBEmpty{
        List<Televizor> televizoare=televizorRepo.getAllTelevizoare();
        if(televizoare.size()>0){
            televizoare=televizorRepo.sortByModel();
        }
    }

    @Transactional
    @Modifying
    public  void sortByPret(String pret) throws ExceptieTvDBEmpty{
        List<Televizor> televizoare=televizorRepo.getAllTelevizoare();
        if(televizoare.size()>0){
            televizoare=televizorRepo.sortByPret();
        }
    }



    @Transactional
    @Modifying

    public void update(@RequestBody TelevizorDTO televizor, String model) throws TvNotFoundException {
        Televizor t = televizorRepo.findTelevizorByModel(televizor.getModel());

        if (t == null) {
            throw new TvNotFoundException();
        }

        if (televizor.getMarca().equals("") == false) {
            t.setMarca(televizor.getMarca());
        }
        if (televizor.getModel().equals("") == false) {
            t.setModel(televizor.getModel());
        }

        if (televizor.getPret() != 0) {
            t.setPret(televizor.getPret());
        }

    }
}
