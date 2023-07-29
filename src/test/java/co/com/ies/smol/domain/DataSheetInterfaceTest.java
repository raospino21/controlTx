package co.com.ies.smol.domain;

import static org.assertj.core.api.Assertions.assertThat;

import co.com.ies.smol.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DataSheetInterfaceTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DataSheetInterface.class);
        DataSheetInterface dataSheetInterface1 = new DataSheetInterface();
        dataSheetInterface1.setId(1L);
        DataSheetInterface dataSheetInterface2 = new DataSheetInterface();
        dataSheetInterface2.setId(dataSheetInterface1.getId());
        assertThat(dataSheetInterface1).isEqualTo(dataSheetInterface2);
        dataSheetInterface2.setId(2L);
        assertThat(dataSheetInterface1).isNotEqualTo(dataSheetInterface2);
        dataSheetInterface1.setId(null);
        assertThat(dataSheetInterface1).isNotEqualTo(dataSheetInterface2);
    }
}
