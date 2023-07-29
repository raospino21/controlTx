package co.com.ies.smol.service.impl;

import co.com.ies.smol.domain.ControlInterfaceBoard;
import co.com.ies.smol.repository.ControlInterfaceBoardRepository;
import co.com.ies.smol.service.ControlInterfaceBoardService;
import co.com.ies.smol.service.dto.ControlInterfaceBoardDTO;
import co.com.ies.smol.service.mapper.ControlInterfaceBoardMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ControlInterfaceBoard}.
 */
@Service
@Transactional
public class ControlInterfaceBoardServiceImpl implements ControlInterfaceBoardService {

    private final Logger log = LoggerFactory.getLogger(ControlInterfaceBoardServiceImpl.class);

    private final ControlInterfaceBoardRepository controlInterfaceBoardRepository;

    private final ControlInterfaceBoardMapper controlInterfaceBoardMapper;

    public ControlInterfaceBoardServiceImpl(
        ControlInterfaceBoardRepository controlInterfaceBoardRepository,
        ControlInterfaceBoardMapper controlInterfaceBoardMapper
    ) {
        this.controlInterfaceBoardRepository = controlInterfaceBoardRepository;
        this.controlInterfaceBoardMapper = controlInterfaceBoardMapper;
    }

    @Override
    public ControlInterfaceBoardDTO save(ControlInterfaceBoardDTO controlInterfaceBoardDTO) {
        log.debug("Request to save ControlInterfaceBoard : {}", controlInterfaceBoardDTO);
        ControlInterfaceBoard controlInterfaceBoard = controlInterfaceBoardMapper.toEntity(controlInterfaceBoardDTO);
        controlInterfaceBoard = controlInterfaceBoardRepository.save(controlInterfaceBoard);
        return controlInterfaceBoardMapper.toDto(controlInterfaceBoard);
    }

    @Override
    public ControlInterfaceBoardDTO update(ControlInterfaceBoardDTO controlInterfaceBoardDTO) {
        log.debug("Request to update ControlInterfaceBoard : {}", controlInterfaceBoardDTO);
        ControlInterfaceBoard controlInterfaceBoard = controlInterfaceBoardMapper.toEntity(controlInterfaceBoardDTO);
        controlInterfaceBoard = controlInterfaceBoardRepository.save(controlInterfaceBoard);
        return controlInterfaceBoardMapper.toDto(controlInterfaceBoard);
    }

    @Override
    public Optional<ControlInterfaceBoardDTO> partialUpdate(ControlInterfaceBoardDTO controlInterfaceBoardDTO) {
        log.debug("Request to partially update ControlInterfaceBoard : {}", controlInterfaceBoardDTO);

        return controlInterfaceBoardRepository
            .findById(controlInterfaceBoardDTO.getId())
            .map(existingControlInterfaceBoard -> {
                controlInterfaceBoardMapper.partialUpdate(existingControlInterfaceBoard, controlInterfaceBoardDTO);

                return existingControlInterfaceBoard;
            })
            .map(controlInterfaceBoardRepository::save)
            .map(controlInterfaceBoardMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ControlInterfaceBoardDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ControlInterfaceBoards");
        return controlInterfaceBoardRepository.findAll(pageable).map(controlInterfaceBoardMapper::toDto);
    }

    public Page<ControlInterfaceBoardDTO> findAllWithEagerRelationships(Pageable pageable) {
        return controlInterfaceBoardRepository.findAllWithEagerRelationships(pageable).map(controlInterfaceBoardMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ControlInterfaceBoardDTO> findOne(Long id) {
        log.debug("Request to get ControlInterfaceBoard : {}", id);
        return controlInterfaceBoardRepository.findOneWithEagerRelationships(id).map(controlInterfaceBoardMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ControlInterfaceBoard : {}", id);
        controlInterfaceBoardRepository.deleteById(id);
    }
}
