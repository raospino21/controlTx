package co.com.ies.smol.service.impl;

import co.com.ies.smol.domain.Operator;
import co.com.ies.smol.repository.OperatorRepository;
import co.com.ies.smol.service.OperatorService;
import co.com.ies.smol.service.dto.OperatorDTO;
import co.com.ies.smol.service.mapper.OperatorMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Operator}.
 */
@Service
@Transactional
public class OperatorServiceImpl implements OperatorService {

    private final Logger log = LoggerFactory.getLogger(OperatorServiceImpl.class);

    private final OperatorRepository operatorRepository;

    private final OperatorMapper operatorMapper;

    public OperatorServiceImpl(OperatorRepository operatorRepository, OperatorMapper operatorMapper) {
        this.operatorRepository = operatorRepository;
        this.operatorMapper = operatorMapper;
    }

    @Override
    public OperatorDTO save(OperatorDTO operatorDTO) {
        log.debug("Request to save Operator : {}", operatorDTO);
        Operator operator = operatorMapper.toEntity(operatorDTO);
        operator = operatorRepository.save(operator);
        return operatorMapper.toDto(operator);
    }

    @Override
    public OperatorDTO update(OperatorDTO operatorDTO) {
        log.debug("Request to update Operator : {}", operatorDTO);
        Operator operator = operatorMapper.toEntity(operatorDTO);
        operator = operatorRepository.save(operator);
        return operatorMapper.toDto(operator);
    }

    @Override
    public Optional<OperatorDTO> partialUpdate(OperatorDTO operatorDTO) {
        log.debug("Request to partially update Operator : {}", operatorDTO);

        return operatorRepository
            .findById(operatorDTO.getId())
            .map(existingOperator -> {
                operatorMapper.partialUpdate(existingOperator, operatorDTO);

                return existingOperator;
            })
            .map(operatorRepository::save)
            .map(operatorMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OperatorDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Operators");
        return operatorRepository.findAll(pageable).map(operatorMapper::toDto);
    }

    public Page<OperatorDTO> findAllWithEagerRelationships(Pageable pageable) {
        return operatorRepository.findAllWithEagerRelationships(pageable).map(operatorMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<OperatorDTO> findOne(Long id) {
        log.debug("Request to get Operator : {}", id);
        return operatorRepository.findOneWithEagerRelationships(id).map(operatorMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Operator : {}", id);
        operatorRepository.deleteById(id);
    }
}
