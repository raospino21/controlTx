package co.com.ies.smol.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ReceptionOrderMapperTest {

    private ReceptionOrderMapper receptionOrderMapper;

    @BeforeEach
    public void setUp() {
        receptionOrderMapper = new ReceptionOrderMapperImpl();
    }
}
