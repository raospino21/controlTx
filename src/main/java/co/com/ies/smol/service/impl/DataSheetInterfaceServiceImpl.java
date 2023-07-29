package co.com.ies.smol.service.impl;

import co.com.ies.smol.domain.DataSheetInterface;
import co.com.ies.smol.repository.DataSheetInterfaceRepository;
import co.com.ies.smol.service.DataSheetInterfaceService;
import co.com.ies.smol.service.dto.DataSheetInterfaceDTO;
import co.com.ies.smol.service.mapper.DataSheetInterfaceMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link DataSheetInterface}.
 */
@Service
@Transactional
public class DataSheetInterfaceServiceImpl implements DataSheetInterfaceService {

    private final Logger log = LoggerFactory.getLogger(DataSheetInterfaceServiceImpl.class);

    private final DataSheetInterfaceRepository dataSheetInterfaceRepository;

    private final DataSheetInterfaceMapper dataSheetInterfaceMapper;

    public DataSheetInterfaceServiceImpl(
        DataSheetInterfaceRepository dataSheetInterfaceRepository,
        DataSheetInterfaceMapper dataSheetInterfaceMapper
    ) {
        this.dataSheetInterfaceRepository = dataSheetInterfaceRepository;
        this.dataSheetInterfaceMapper = dataSheetInterfaceMapper;
    }

    @Override
    public DataSheetInterfaceDTO save(DataSheetInterfaceDTO dataSheetInterfaceDTO) {
        log.debug("Request to save DataSheetInterface : {}", dataSheetInterfaceDTO);
        DataSheetInterface dataSheetInterface = dataSheetInterfaceMapper.toEntity(dataSheetInterfaceDTO);
        dataSheetInterface = dataSheetInterfaceRepository.save(dataSheetInterface);
        return dataSheetInterfaceMapper.toDto(dataSheetInterface);
    }

    @Override
    public DataSheetInterfaceDTO update(DataSheetInterfaceDTO dataSheetInterfaceDTO) {
        log.debug("Request to update DataSheetInterface : {}", dataSheetInterfaceDTO);
        DataSheetInterface dataSheetInterface = dataSheetInterfaceMapper.toEntity(dataSheetInterfaceDTO);
        dataSheetInterface = dataSheetInterfaceRepository.save(dataSheetInterface);
        return dataSheetInterfaceMapper.toDto(dataSheetInterface);
    }

    @Override
    public Optional<DataSheetInterfaceDTO> partialUpdate(DataSheetInterfaceDTO dataSheetInterfaceDTO) {
        log.debug("Request to partially update DataSheetInterface : {}", dataSheetInterfaceDTO);

        return dataSheetInterfaceRepository
            .findById(dataSheetInterfaceDTO.getId())
            .map(existingDataSheetInterface -> {
                dataSheetInterfaceMapper.partialUpdate(existingDataSheetInterface, dataSheetInterfaceDTO);

                return existingDataSheetInterface;
            })
            .map(dataSheetInterfaceRepository::save)
            .map(dataSheetInterfaceMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DataSheetInterfaceDTO> findAll(Pageable pageable) {
        log.debug("Request to get all DataSheetInterfaces");
        return dataSheetInterfaceRepository.findAll(pageable).map(dataSheetInterfaceMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DataSheetInterfaceDTO> findOne(Long id) {
        log.debug("Request to get DataSheetInterface : {}", id);
        return dataSheetInterfaceRepository.findById(id).map(dataSheetInterfaceMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete DataSheetInterface : {}", id);
        dataSheetInterfaceRepository.deleteById(id);
    }
}
