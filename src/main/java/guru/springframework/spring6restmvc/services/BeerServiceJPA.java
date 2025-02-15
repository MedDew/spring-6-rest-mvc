package guru.springframework.spring6restmvc.services;

import guru.springframework.spring6restmvc.entities.Beer;
import guru.springframework.spring6restmvc.mappers.BeerMapper;
import guru.springframework.spring6restmvc.model.BeerDTO;
import guru.springframework.spring6restmvc.repositories.BeerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;


@Service
@Primary
@RequiredArgsConstructor
public class BeerServiceJPA implements BeerService {

    private final BeerRepository beerRepository;
    private final BeerMapper beerMapper;


    @Override
    public Optional<BeerDTO> getBeerById(UUID id) {
        return Optional.ofNullable(
                beerMapper.beerToBeerDTO(
                        beerRepository.findById(id)
                        .orElse(null)
                )
        );
    }

    @Override
    public List<BeerDTO> listBeers() {
        return  beerRepository.findAll().stream()
                .map(beerMapper::beerToBeerDTO)
                .collect(Collectors.toList());
    }

    @Override
    public BeerDTO saveNewBeer(BeerDTO beer) {

        return beerMapper.beerToBeerDTO(
                beerRepository.save(
                        beerMapper.beerDTOToBeer(beer)
                ));
    }

    @Override
    public Optional<BeerDTO> updateBeerById(UUID beerId, BeerDTO beer) {
        AtomicReference<Optional<BeerDTO>> atomicReference = new AtomicReference<>();

        beerRepository.findById(beerId).ifPresentOrElse(foundBeer -> {
            foundBeer.setBeerName(beer.getBeerName());
            foundBeer.setBeerStyle(beer.getBeerStyle());
            foundBeer.setUpc(beer.getUpc());
            foundBeer.setPrice(beer.getPrice());
            atomicReference.set(
                    Optional.of(
                            beerMapper.beerToBeerDTO(
                                beerRepository.save(foundBeer)
                            )
                    )
            );
        },
            () -> {
            atomicReference.set(Optional.empty());
        });

        return atomicReference.get();
    }

    @Override
    public Boolean deleteBeerById(UUID beerId) {
        if(beerRepository.existsById(beerId)) {
            beerRepository.deleteById(beerId);
            return true;
        }
        return false;
    }

    @Override
    public Optional<BeerDTO> patchBeerById(UUID beerId, BeerDTO beer) {

        AtomicReference<Optional<BeerDTO>> atomicReference = new AtomicReference<>();

        beerRepository.findById(beerId).ifPresentOrElse(
                foundBeer -> {
                    if(beer.getBeerName()!= foundBeer.getBeerName()) {
                        foundBeer.setBeerName(beer.getBeerName());
                    }

                    if(beer.getBeerStyle()!= foundBeer.getBeerStyle()) {
                        foundBeer.setBeerStyle(beer.getBeerStyle());
                    }

                    if(beer.getUpc()!= foundBeer.getUpc()) {
                        foundBeer.setUpc(beer.getUpc());
                    }

                    if(beer.getQuantityOnHand() != foundBeer.getQuantityOnHand()) {
                        foundBeer.setQuantityOnHand(beer.getQuantityOnHand());
                    }

                    if(beer.getPrice()!= foundBeer.getPrice()) {
                        foundBeer.setPrice(beer.getPrice());
                    }

                    if(beer.getUpdateDate().isAfter(foundBeer.getUpdateDate())){
                        foundBeer.setUpdateDate(beer.getUpdateDate());
                    }

                    Beer patchedBeer = beerRepository.saveAndFlush(foundBeer);
                    atomicReference.set(
                            Optional.of(
                                    beerMapper.beerToBeerDTO(patchedBeer)
                            )
                    );
                },
                () -> atomicReference.set(Optional.empty())
        );

        return atomicReference.get();
    }
}
