package infinityshopping.online.app.service.impl;

import infinityshopping.online.app.domain.Payment;
import infinityshopping.online.app.repository.PaymentRepository;
import infinityshopping.online.app.service.AddVat;
import infinityshopping.online.app.service.PaymentService;
import infinityshopping.online.app.service.dto.PaymentDTO;
import infinityshopping.online.app.service.dto.PaymentDtoNamePriceGross;
import infinityshopping.online.app.service.mapper.PaymentMapper;
import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PaymentServiceImpl implements PaymentService, AddVat {

  private final Logger log = LoggerFactory.getLogger(PaymentServiceImpl.class);

  private final PaymentRepository paymentRepository;

  private final PaymentMapper paymentMapper;

  public PaymentServiceImpl(PaymentRepository paymentRepository, PaymentMapper paymentMapper) {
    this.paymentRepository = paymentRepository;
    this.paymentMapper = paymentMapper;
  }

  @Override
  @Transactional
  public PaymentDTO save(PaymentDTO paymentDto) {
    log.debug("Request to save Payment : {}", paymentDto);
    Payment payment = paymentMapper.toEntity(paymentDto);

    payment.setPriceGross(addVat(payment.getPriceNet(), payment.getVat()));

    payment = paymentRepository.save(payment);
    return paymentMapper.toDto(payment);
  }

  @Override
  @Transactional(readOnly = true)
  public List<PaymentDTO> findAll() {
    log.debug("Request to get all Payments");
    return paymentRepository.findAll().stream().map(paymentMapper::toDto)
        .collect(Collectors.toCollection(LinkedList::new));
  }

  @Override
  @Transactional(readOnly = true)
  public List<PaymentDtoNamePriceGross> findAllNamePriceGross() {
    log.debug("Request to get all Payments only with name and price gross");
    return paymentRepository
        .findAll()
        .stream()
        .map(paymentMapper::toDtoNamePriceGross)
        .collect(Collectors.toCollection(LinkedList::new));
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<PaymentDTO> findOne(Long id) {
    log.debug("Request to get Payment : {}", id);
    return paymentRepository.findById(id).map(paymentMapper::toDto);
  }

  @Override
  @Transactional
  public void delete(Long id) {
    log.debug("Request to delete Payment : {}", id);
    paymentRepository.deleteById(id);
  }
}
