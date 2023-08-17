package co.com.ies.smol.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import co.com.ies.smol.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ReceptionOrderDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReceptionOrderDTO.class);
        ReceptionOrderDTO receptionOrderDTO1 = new ReceptionOrderDTO();
        receptionOrderDTO1.setId(1L);
        ReceptionOrderDTO receptionOrderDTO2 = new ReceptionOrderDTO();
        assertThat(receptionOrderDTO1).isNotEqualTo(receptionOrderDTO2);
        receptionOrderDTO2.setId(receptionOrderDTO1.getId());
        assertThat(receptionOrderDTO1).isEqualTo(receptionOrderDTO2);
        receptionOrderDTO2.setId(2L);
        assertThat(receptionOrderDTO1).isNotEqualTo(receptionOrderDTO2);
        receptionOrderDTO1.setId(null);
        assertThat(receptionOrderDTO1).isNotEqualTo(receptionOrderDTO2);
    }
}
