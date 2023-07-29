package co.com.ies.smol.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DataSheetInterfaceMapperTest {

    private DataSheetInterfaceMapper dataSheetInterfaceMapper;

    @BeforeEach
    public void setUp() {
        dataSheetInterfaceMapper = new DataSheetInterfaceMapperImpl();
    }
}
