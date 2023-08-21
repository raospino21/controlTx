package co.com.ies.smol.service.impl;

import co.com.ies.smol.domain.Contract;
import co.com.ies.smol.domain.Operator;
import co.com.ies.smol.domain.enumeration.ContractType;
import co.com.ies.smol.repository.ContractRepository;
import co.com.ies.smol.service.ContractService;
import co.com.ies.smol.service.dto.ContractDTO;
import co.com.ies.smol.service.dto.OperatorDTO;
import co.com.ies.smol.service.mapper.ContractMapper;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Contract}.
 */
@Service
@Transactional
public class ContractServiceImpl implements ContractService {

    private final Logger log = LoggerFactory.getLogger(ContractServiceImpl.class);

    private final ContractRepository contractRepository;

    private final ContractMapper contractMapper;

    public ContractServiceImpl(ContractRepository contractRepository, ContractMapper contractMapper) {
        this.contractRepository = contractRepository;
        this.contractMapper = contractMapper;
    }

    @Override
    public ContractDTO save(ContractDTO contractDTO) {
        log.debug("Request to save Contract : {}", contractDTO);
        Contract contract = contractMapper.toEntity(contractDTO);
        contract = contractRepository.save(contract);
        return contractMapper.toDto(contract);
    }

    @Override
    public ContractDTO update(ContractDTO contractDTO) {
        log.debug("Request to update Contract : {}", contractDTO);
        Contract contract = contractMapper.toEntity(contractDTO);
        contract = contractRepository.save(contract);
        return contractMapper.toDto(contract);
    }

    @Override
    public Optional<ContractDTO> partialUpdate(ContractDTO contractDTO) {
        log.debug("Request to partially update Contract : {}", contractDTO);

        return contractRepository
            .findById(contractDTO.getId())
            .map(existingContract -> {
                contractMapper.partialUpdate(existingContract, contractDTO);

                return existingContract;
            })
            .map(contractRepository::save)
            .map(contractMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ContractDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Contracts");
        return contractRepository.findAll(pageable).map(contractMapper::toDto);
    }

    public Page<ContractDTO> findAllWithEagerRelationships(Pageable pageable) {
        return contractRepository.findAllWithEagerRelationships(pageable).map(contractMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ContractDTO> findOne(Long id) {
        log.debug("Request to get Contract : {}", id);
        return contractRepository.findOneWithEagerRelationships(id).map(contractMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Contract : {}", id);
        contractRepository.deleteById(id);
    }

    @Override
    public List<ContractDTO> getContractByReference(String reference) {
        return contractMapper.toDto(contractRepository.getContractByReference(reference));
    }

    @Override
    public List<ContractDTO> findAllContractByOpeatorIn(List<Operator> operators) {
        return contractMapper.toDto(contractRepository.findAllByOperatorInAndFinishTimeIsNull(operators));
    }

    @Override
    public Optional<ContractDTO> findContractByOperator(Operator operator) {
        return contractRepository.findByOperator(operator).map(contractMapper::toDto);
    }

    @Override
    public Optional<ContractDTO> getContractByReferenceAndType(String reference, ContractType type) {
        return contractRepository.getByReferenceAndType(reference, type).map(contractMapper::toDto);
    }
}
