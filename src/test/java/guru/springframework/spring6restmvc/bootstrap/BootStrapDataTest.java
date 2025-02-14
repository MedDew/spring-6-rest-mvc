package guru.springframework.spring6restmvc.bootstrap;

import guru.springframework.spring6restmvc.entities.Beer;
import guru.springframework.spring6restmvc.repositories.BeerRepository;
import guru.springframework.spring6restmvc.repositories.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.isNotNull;

@DataJpaTest
class BootStrapDataTest {

    private BootStrapData bootStrapData;

    @Autowired
    private BeerRepository beerRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @BeforeEach
    void setUp() {
        bootStrapData = new BootStrapData(beerRepository, customerRepository);
    }

    @Test
    public void testLoadData() throws Exception {
        bootStrapData.run(null);

        assertThat(beerRepository.count()).isEqualTo(3);
        assertThat(beerRepository.findAll().get(0)).extracting("id").isNotNull();
        assertThat(beerRepository.findAll().get(0)).extracting("version").isNotNull();

        assertThat(customerRepository.count()).isEqualTo(3);
        assertThat(customerRepository.findAll().get(0)).extracting("id").isNotNull();
        assertThat(customerRepository.findAll().get(0)).extracting("version").isNotNull();
    }

}