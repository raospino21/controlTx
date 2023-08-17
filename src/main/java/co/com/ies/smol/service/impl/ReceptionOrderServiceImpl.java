package co.com.ies.smol.service.impl;

import co.com.ies.smol.domain.ReceptionOrder;
import co.com.ies.smol.repository.ReceptionOrderRepository;
import co.com.ies.smol.service.ReceptionOrderService;
import co.com.ies.smol.service.dto.ReceptionOrderDTO;
import co.com.ies.smol.service.mapper.ReceptionOrderMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ReceptionOrder}.
 */
@Service
@Transactional
public class ReceptionOrderServiceImpl implements ReceptionOrderService {

    private final Logger log = LoggerFactory.getLogger(ReceptionOrderServiceImpl.class);

    private final ReceptionOrderRepository receptionOrderRepository;

    private final ReceptionOrderMapper receptionOrderMapper;

    public ReceptionOrderServiceImpl(ReceptionOrderRepository receptionOrderRepository, ReceptionOrderMapper receptionOrderMapper) {
        this.receptionOrderRepository = receptionOrderRepository;
        this.receptionOrderMapper = receptionOrderMapper;
    }

    @Override
    public ReceptionOrderDTO save(ReceptionOrderDTO receptionOrderDTO) {
        log.debug("Request to save ReceptionOrder : {}", receptionOrderDTO);
        ReceptionOrder receptionOrder = receptionOrderMapper.toEntity(receptionOrderDTO);
        receptionOrder = receptionOrderRepository.save(receptionOrder);
        return receptionOrderMapper.toDto(receptionOrder);
    }

    @Override
    public ReceptionOrderDTO update(ReceptionOrderDTO receptionOrderDTO) {
        log.debug("Request to update ReceptionOrder : {}", receptionOrderDTO);
        ReceptionOrder receptionOrder = receptionOrderMapper.toEntity(receptionOrderDTO);
        receptionOrder = receptionOrderRepository.save(receptionOrder);
        return receptionOrderMapper.toDto(receptionOrder);
    }

    @Override
    public Optional<ReceptionOrderDTO> partialUpdate(ReceptionOrderDTO receptionOrderDTO) {
        log.debug("Request to partially update ReceptionOrder : {}", receptionOrderDTO);

        return receptionOrderRepository
            .findById(receptionOrderDTO.getId())
            .map(existingReceptionOrder -> {
                receptionOrderMapper.partialUpdate(existingReceptionOrder, receptionOrderDTO);

                return existingReceptionOrder;
            })
            .map(receptionOrderRepository::save)
            .map(receptionOrderMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ReceptionOrderDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ReceptionOrders");
        return receptionOrderRepository.findAll(pageable).map(receptionOrderMapper::toDto);
    }

    public Page<ReceptionOrderDTO> findAllWithEagerRelationships(Pageable pageable) {
        return receptionOrderRepository.findAllWithEagerRelationships(pageable).map(receptionOrderMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ReceptionOrderDTO> findOne(Long id) {
        log.debug("Request to get ReceptionOrder : {}", id);
        return receptionOrderRepository.findOneWithEagerRelationships(id).map(receptionOrderMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ReceptionOrder : {}", id);
        receptionOrderRepository.deleteById(id);
    }
}
