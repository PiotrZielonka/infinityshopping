package infinityshopping.online.app.service.impl;

import infinityshopping.online.app.domain.OrderMain;
import infinityshopping.online.app.domain.Payment;
import infinityshopping.online.app.domain.PaymentOrderMain;
import infinityshopping.online.app.repository.OrderMainRepository;
import infinityshopping.online.app.repository.PaymentOrderMainRepository;
import infinityshopping.online.app.repository.PaymentRepository;
import infinityshopping.online.app.service.PaymentOrderMainService;
import infinityshopping.online.app.service.dto.PaymentOrderMainDTO;
import infinityshopping.online.app.service.mapper.PaymentOrderMainMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PaymentOrderMainServiceImpl implements PaymentOrderMainService {

  private final Logger log = LoggerFactory.getLogger(PaymentOrderMainServiceImpl.class);

  private final PaymentOrderMainRepository paymentOrderMainRepository;

  private final PaymentRepository paymentRepository;

  private final OrderMainRepository orderMainRepository;

  private final PaymentOrderMainMapper paymentOrderMainMapper;

  private OrderMain orderMain;

  public PaymentOrderMainServiceImpl(
      PaymentOrderMainRepository paymentOrderMainRepository,
      PaymentRepository paymentRepository,
      OrderMainRepository orderMainRepository,
      PaymentOrderMainMapper paymentOrderMainMapper
  ) {
    this.paymentOrderMainRepository = paymentOrderMainRepository;
    this.paymentRepository = paymentRepository;
    this.orderMainRepository = orderMainRepository;
    this.paymentOrderMainMapper = paymentOrderMainMapper;
  }

  @Override
  @Transactional
  public PaymentOrderMainDTO save(PaymentOrderMainDTO paymentOrderMainDto) {
    log.debug("Request to save only update PaymentOrderMain : {}", paymentOrderMainDto);

    Payment payment = findPaymentOrderMainInPaymentRepositoryBecauseOnlyNameIsInDto(
        paymentOrderMainDto);
    PaymentOrderMain paymentOrderMain = paymentOrderMainMapper.toEntity(paymentOrderMainDto);

    setProperOrderMainIdToPaymentOrderMainBecauseItIsNotInDto(paymentOrderMain);
    setProperValuesInPaymentOrderMainFromPaymentBecauseAreNullInDto(paymentOrderMain, payment);
    setAmountsOfShipmentToProperOrderMain(paymentOrderMain);
    setAmountsOfOrderToProperOrderMain(paymentOrderMain);

    paymentOrderMain = paymentOrderMainRepository.save(paymentOrderMain);
    return paymentOrderMainMapper.toDto(paymentOrderMain);
  }

  private Payment findPaymentOrderMainInPaymentRepositoryBecauseOnlyNameIsInDto(
      PaymentOrderMainDTO paymentOrderMainDto) {
    return paymentRepository.findByName(paymentOrderMainDto.getName()).get();
  }

  private void setProperOrderMainIdToPaymentOrderMainBecauseItIsNotInDto(
      PaymentOrderMain paymentOrderMain) {

    paymentOrderMain.setOrderMain(paymentOrderMainRepository.findById(paymentOrderMain.getId())
        .get().getOrderMain());
  }

  private void setProperValuesInPaymentOrderMainFromPaymentBecauseAreNullInDto(
      PaymentOrderMain paymentOrderMain, Payment payment) {
    paymentOrderMain.setPriceNet(payment.getPriceNet());
    paymentOrderMain.setVat(payment.getVat());
    paymentOrderMain.setPriceGross(payment.getPriceGross());
  }

  private void setAmountsOfShipmentToProperOrderMain(PaymentOrderMain paymentOrderMain) {
    orderMain = orderMainRepository.findById(paymentOrderMain.getOrderMain().getId()).get();

    orderMain.setAmountOfShipmentNet(paymentOrderMain.getPriceNet());
    orderMain.setAmountOfShipmentGross(paymentOrderMain.getPriceGross());

    orderMainRepository.save(orderMain);
  }

  private void setAmountsOfOrderToProperOrderMain(PaymentOrderMain paymentOrderMain) {
    orderMain = orderMainRepository.findById(paymentOrderMain.getOrderMain().getId()).get();

    orderMain.setAmountOfOrderNet(
        orderMain.getAmountOfCartNet().add(orderMain.getAmountOfShipmentNet()));
    orderMain.setAmountOfOrderGross(
        orderMain.getAmountOfCartGross().add(orderMain.getAmountOfShipmentGross()));

    orderMainRepository.save(orderMain);
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<PaymentOrderMainDTO> findOne(Long id) {
    log.debug("Request to get PaymentOrderMain : {}", id);
    return paymentOrderMainRepository.findById(id).map(paymentOrderMainMapper::toDto);
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<PaymentOrderMainDTO> findByOrderMainId(Long id) {
    log.debug("Request to get PaymentOrderMain by id OrderMain");
    return paymentOrderMainRepository.findByOrderMainId(id)
        .map(paymentOrderMainMapper::toDto);
  }
}
