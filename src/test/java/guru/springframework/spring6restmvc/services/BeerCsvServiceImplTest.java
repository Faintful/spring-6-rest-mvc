package guru.springframework.spring6restmvc.services;

import guru.springframework.spring6restmvc.model.BeerCSVRecord;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest
class BeerCsvServiceImplTest {

    @Autowired
    BeerCsvService beerCsvService;

    @Test
    void convertCSV() throws FileNotFoundException {
        File file = ResourceUtils.getFile("classpath:csvdata/beers.csv");
        List<BeerCSVRecord> beerCSVRecordList = beerCsvService.convertCSV(file);
        log.info("Records found: " + beerCSVRecordList.size());
        assertThat(beerCSVRecordList.size()).isNotNull();
    }
}