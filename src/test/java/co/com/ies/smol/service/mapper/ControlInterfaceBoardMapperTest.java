package co.com.ies.smol.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ControlInterfaceBoardMapperTest {

    private ControlInterfaceBoardMapper controlInterfaceBoardMapper;

    @BeforeEach
    public void setUp() {
        controlInterfaceBoardMapper = new ControlInterfaceBoardMapperImpl();
    }
}
