package infinityshopping.online.app.service.impl;

import infinityshopping.online.app.domain.Cart;
import infinityshopping.online.app.domain.Payment;
import infinityshopping.online.app.domain.PaymentCart;
import infinityshopping.online.app.domain.User;
import infinityshopping.online.app.repository.CartRepository;
import infinityshopping.online.app.repository.PaymentCartRepository;
import infinityshopping.online.app.repository.PaymentRepository;
import infinityshopping.online.app.repository.UserRepository;
import infinityshopping.online.app.security.SecurityUtils;
import infinityshopping.online.app.service.PaymentCartService;
import infinityshopping.online.app.service.UserNotFoundException;
import infinityshopping.online.app.service.dto.PaymentCartDTO;
import infinityshopping.online.app.service.mapper.PaymentCartMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PaymentCartServiceImpl implements PaymentCartService {

  private final Logger log = LoggerFactory.getLogger(PaymentCartServiceImpl.class);

  private final PaymentCartRepository paymentCartRepository;

  private final PaymentRepository paymentRepository;

  private final UserRepository userRepository;

  private final CartRepository cartRepository;

  private final PaymentCartMapper paymentCartMapper;

  private User currentLoggedUser;

  private Cart cart;


  public PaymentCartServiceImpl(PaymentCartRepository paymentCartRepository,
      PaymentRepository paymentRepository,
      PaymentCartMapper paymentCartMapper,
      UserRepository userRepository, CartRepository cartRepository) {
    this.paymentCartRepository = paymentCartRepository;
    this.paymentRepository = paymentRepository;
    this.paymentCartMapper = paymentCartMapper;
    this.userRepository = userRepository;
    this.cartRepository = cartRepository;
  }

  @Override
  @Transactional
  public PaymentCartDTO save(PaymentCartDTO paymentCartDto) {
    log.debug("Request to save only update PaymentCart : {}", paymentCartDto);

    currentLoggedUser = checkIfUserExist();

    Payment payment = findPaymentCartInPaymentRepositoryBecauseOnlyNameIsInDto(paymentCartDto);
    PaymentCart paymentCart = paymentCartMapper.toEntity(paymentCartDto);

    setCartIdToPaymentCartOfLoggedUserBecauseItIsNotInDto(
        paymentCart, currentLoggedUser);
    setProperValuesInPaymentCartFromPaymentBecauseAreNullInDto(paymentCart, payment);

    setAmountsOfShipmentToProperCartOfUser(paymentCart);
    setAmountsOfOrderToProperCartOfUser(paymentCart);

    paymentCart = paymentCartRepository.save(paymentCart);

    return paymentCartMapper.toDto(paymentCart);
  }

  private Payment findPaymentCartInPaymentRepositoryBecauseOnlyNameIsInDto(
      PaymentCartDTO paymentCartDto) {
    return paymentRepository.findByName(paymentCartDto.getName()).get();
  }

  private void setProperValuesInPaymentCartFromPaymentBecauseAreNullInDto(
      PaymentCart paymentCart, Payment payment) {
    paymentCart.setPriceNet(payment.getPriceNet());
    paymentCart.setVat(payment.getVat());
    paymentCart.setPriceGross(payment.getPriceGross());
    paymentCart.setPaymentStatus(payment.getPaymentStatus());
  }

  private void setCartIdToPaymentCartOfLoggedUserBecauseItIsNotInDto(
      PaymentCart paymentCartOfLoggedUser, User currentLoggedUser) {
    paymentCartOfLoggedUser.setCart(currentLoggedUser.getCart());
  }

  private void setAmountsOfShipmentToProperCartOfUser(PaymentCart paymentCartOfLoggedUser) {
    cart = cartRepository.findById(paymentCartOfLoggedUser.getCart().getId()).get();

    cart.setAmountOfShipmentNet(paymentCartOfLoggedUser.getPriceNet());
    cart.setAmountOfShipmentGross(paymentCartOfLoggedUser.getPriceGross());

    cartRepository.save(cart);
  }

  private void setAmountsOfOrderToProperCartOfUser(PaymentCart paymentCartOfLoggedUser) {
    cart = cartRepository.findById(paymentCartOfLoggedUser.getCart().getId()).get();

    cart.setAmountOfOrderNet(cart.getAmountOfCartNet().add(cart.getAmountOfShipmentNet()));
    cart.setAmountOfOrderGross(cart.getAmountOfCartGross().add(cart.getAmountOfShipmentGross()));

    cartRepository.save(cart);
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<PaymentCartDTO> findOne(Long id) {
    log.debug("Request to get PaymentCart : {}", id);
    return paymentCartRepository.findById(id).map(paymentCartMapper::toDto);
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<PaymentCartDTO> findByCartId() {
    log.debug("Request to get payment of cart of current logged user ");

    currentLoggedUser = checkIfUserExist();

    return paymentCartRepository.findByCartId(currentLoggedUser.getCart().getId())
        .map(paymentCartMapper::toDto);
  }

  private User checkIfUserExist() {
    currentLoggedUser = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin()
            .orElseThrow(() -> new UserNotFoundException()))
        .orElseThrow(() -> new UserNotFoundException());
    return currentLoggedUser;
  }
}
