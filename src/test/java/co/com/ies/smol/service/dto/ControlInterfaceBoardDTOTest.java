package co.com.ies.smol.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import co.com.ies.smol.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ControlInterfaceBoardDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ControlInterfaceBoardDTO.class);
        ControlInterfaceBoardDTO controlInterfaceBoardDTO1 = new ControlInterfaceBoardDTO();
        controlInterfaceBoardDTO1.setId(1L);
        ControlInterfaceBoardDTO controlInterfaceBoardDTO2 = new ControlInterfaceBoardDTO();
        assertThat(controlInterfaceBoardDTO1).isNotEqualTo(controlInterfaceBoardDTO2);
        controlInterfaceBoardDTO2.setId(controlInterfaceBoardDTO1.getId());
        assertThat(controlInterfaceBoardDTO1).isEqualTo(controlInterfaceBoardDTO2);
        controlInterfaceBoardDTO2.setId(2L);
        assertThat(controlInterfaceBoardDTO1).isNotEqualTo(controlInterfaceBoardDTO2);
        controlInterfaceBoardDTO1.setId(null);
        assertThat(controlInterfaceBoardDTO1).isNotEqualTo(controlInterfaceBoardDTO2);
    }
}
