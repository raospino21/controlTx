package co.com.ies.smol.domain;

import static org.assertj.core.api.Assertions.assertThat;

import co.com.ies.smol.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ReceptionOrderTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReceptionOrder.class);
        ReceptionOrder receptionOrder1 = new ReceptionOrder();
        receptionOrder1.setId(1L);
        ReceptionOrder receptionOrder2 = new ReceptionOrder();
        receptionOrder2.setId(receptionOrder1.getId());
        assertThat(receptionOrder1).isEqualTo(receptionOrder2);
        receptionOrder2.setId(2L);
        assertThat(receptionOrder1).isNotEqualTo(receptionOrder2);
        receptionOrder1.setId(null);
        assertThat(receptionOrder1).isNotEqualTo(receptionOrder2);
    }
}
