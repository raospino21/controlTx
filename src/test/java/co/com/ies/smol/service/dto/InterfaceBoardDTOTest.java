package co.com.ies.smol.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import co.com.ies.smol.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class InterfaceBoardDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(InterfaceBoardDTO.class);
        InterfaceBoardDTO interfaceBoardDTO1 = new InterfaceBoardDTO();
        interfaceBoardDTO1.setId(1L);
        InterfaceBoardDTO interfaceBoardDTO2 = new InterfaceBoardDTO();
        assertThat(interfaceBoardDTO1).isNotEqualTo(interfaceBoardDTO2);
        interfaceBoardDTO2.setId(interfaceBoardDTO1.getId());
        assertThat(interfaceBoardDTO1).isEqualTo(interfaceBoardDTO2);
        interfaceBoardDTO2.setId(2L);
        assertThat(interfaceBoardDTO1).isNotEqualTo(interfaceBoardDTO2);
        interfaceBoardDTO1.setId(null);
        assertThat(interfaceBoardDTO1).isNotEqualTo(interfaceBoardDTO2);
    }
}
