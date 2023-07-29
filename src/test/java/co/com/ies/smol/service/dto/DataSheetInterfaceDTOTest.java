package co.com.ies.smol.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import co.com.ies.smol.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DataSheetInterfaceDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DataSheetInterfaceDTO.class);
        DataSheetInterfaceDTO dataSheetInterfaceDTO1 = new DataSheetInterfaceDTO();
        dataSheetInterfaceDTO1.setId(1L);
        DataSheetInterfaceDTO dataSheetInterfaceDTO2 = new DataSheetInterfaceDTO();
        assertThat(dataSheetInterfaceDTO1).isNotEqualTo(dataSheetInterfaceDTO2);
        dataSheetInterfaceDTO2.setId(dataSheetInterfaceDTO1.getId());
        assertThat(dataSheetInterfaceDTO1).isEqualTo(dataSheetInterfaceDTO2);
        dataSheetInterfaceDTO2.setId(2L);
        assertThat(dataSheetInterfaceDTO1).isNotEqualTo(dataSheetInterfaceDTO2);
        dataSheetInterfaceDTO1.setId(null);
        assertThat(dataSheetInterfaceDTO1).isNotEqualTo(dataSheetInterfaceDTO2);
    }
}
