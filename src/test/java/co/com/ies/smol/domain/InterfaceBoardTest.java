package co.com.ies.smol.domain;

import static org.assertj.core.api.Assertions.assertThat;

import co.com.ies.smol.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class InterfaceBoardTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(InterfaceBoard.class);
        InterfaceBoard interfaceBoard1 = new InterfaceBoard();
        interfaceBoard1.setId(1L);
        InterfaceBoard interfaceBoard2 = new InterfaceBoard();
        interfaceBoard2.setId(interfaceBoard1.getId());
        assertThat(interfaceBoard1).isEqualTo(interfaceBoard2);
        interfaceBoard2.setId(2L);
        assertThat(interfaceBoard1).isNotEqualTo(interfaceBoard2);
        interfaceBoard1.setId(null);
        assertThat(interfaceBoard1).isNotEqualTo(interfaceBoard2);
    }
}
