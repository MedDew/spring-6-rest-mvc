package guru.springframework.spring6restmvc.controller;

import guru.springframework.spring6restmvc.model.BeerDTO;
import guru.springframework.spring6restmvc.repositories.BeerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class BeerControllerIT {

    @Autowired
    private BeerController beerController;

    @Autowired
    private BeerRepository beerRepository;

    @Test
    public void testListBeers() {
        List<BeerDTO> beerList = beerController.listBeers();

        assertThat(beerList.size()).isEqualTo(3);
    }

    @Transactional
    @Rollback
    @Test
    public void testEmptyList() {
        beerRepository.deleteAll();
        List<BeerDTO> beerList = beerController.listBeers();

        assertThat(beerList.size()).isEqualTo(0);
    }

}