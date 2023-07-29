package co.com.ies.smol.service.impl;

import co.com.ies.smol.domain.InterfaceBoard;
import co.com.ies.smol.repository.InterfaceBoardRepository;
import co.com.ies.smol.service.InterfaceBoardService;
import co.com.ies.smol.service.dto.InterfaceBoardDTO;
import co.com.ies.smol.service.mapper.InterfaceBoardMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link InterfaceBoard}.
 */
@Service
@Transactional
public class InterfaceBoardServiceImpl implements InterfaceBoardService {

    private final Logger log = LoggerFactory.getLogger(InterfaceBoardServiceImpl.class);

    private final InterfaceBoardRepository interfaceBoardRepository;

    private final InterfaceBoardMapper interfaceBoardMapper;

    public InterfaceBoardServiceImpl(InterfaceBoardRepository interfaceBoardRepository, InterfaceBoardMapper interfaceBoardMapper) {
        this.interfaceBoardRepository = interfaceBoardRepository;
        this.interfaceBoardMapper = interfaceBoardMapper;
    }

    @Override
    public InterfaceBoardDTO save(InterfaceBoardDTO interfaceBoardDTO) {
        log.debug("Request to save InterfaceBoard : {}", interfaceBoardDTO);
        InterfaceBoard interfaceBoard = interfaceBoardMapper.toEntity(interfaceBoardDTO);
        interfaceBoard = interfaceBoardRepository.save(interfaceBoard);
        return interfaceBoardMapper.toDto(interfaceBoard);
    }

    @Override
    public InterfaceBoardDTO update(InterfaceBoardDTO interfaceBoardDTO) {
        log.debug("Request to update InterfaceBoard : {}", interfaceBoardDTO);
        InterfaceBoard interfaceBoard = interfaceBoardMapper.toEntity(interfaceBoardDTO);
        interfaceBoard = interfaceBoardRepository.save(interfaceBoard);
        return interfaceBoardMapper.toDto(interfaceBoard);
    }

    @Override
    public Optional<InterfaceBoardDTO> partialUpdate(InterfaceBoardDTO interfaceBoardDTO) {
        log.debug("Request to partially update InterfaceBoard : {}", interfaceBoardDTO);

        return interfaceBoardRepository
            .findById(interfaceBoardDTO.getId())
            .map(existingInterfaceBoard -> {
                interfaceBoardMapper.partialUpdate(existingInterfaceBoard, interfaceBoardDTO);

                return existingInterfaceBoard;
            })
            .map(interfaceBoardRepository::save)
            .map(interfaceBoardMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<InterfaceBoardDTO> findAll(Pageable pageable) {
        log.debug("Request to get all InterfaceBoards");
        return interfaceBoardRepository.findAll(pageable).map(interfaceBoardMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<InterfaceBoardDTO> findOne(Long id) {
        log.debug("Request to get InterfaceBoard : {}", id);
        return interfaceBoardRepository.findById(id).map(interfaceBoardMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete InterfaceBoard : {}", id);
        interfaceBoardRepository.deleteById(id);
    }
}
