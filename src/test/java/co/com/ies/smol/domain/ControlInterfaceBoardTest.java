package co.com.ies.smol.domain;

import static org.assertj.core.api.Assertions.assertThat;

import co.com.ies.smol.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ControlInterfaceBoardTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ControlInterfaceBoard.class);
        ControlInterfaceBoard controlInterfaceBoard1 = new ControlInterfaceBoard();
        controlInterfaceBoard1.setId(1L);
        ControlInterfaceBoard controlInterfaceBoard2 = new ControlInterfaceBoard();
        controlInterfaceBoard2.setId(controlInterfaceBoard1.getId());
        assertThat(controlInterfaceBoard1).isEqualTo(controlInterfaceBoard2);
        controlInterfaceBoard2.setId(2L);
        assertThat(controlInterfaceBoard1).isNotEqualTo(controlInterfaceBoard2);
        controlInterfaceBoard1.setId(null);
        assertThat(controlInterfaceBoard1).isNotEqualTo(controlInterfaceBoard2);
    }
}
