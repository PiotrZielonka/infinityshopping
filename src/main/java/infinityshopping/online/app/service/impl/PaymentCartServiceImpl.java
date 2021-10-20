package infinityshopping.online.app.service.impl;

import infinityshopping.online.app.domain.Cart;
import infinityshopping.online.app.domain.PaymentCart;
import infinityshopping.online.app.domain.User;
import infinityshopping.online.app.repository.CartRepository;
import infinityshopping.online.app.repository.PaymentCartRepository;
import infinityshopping.online.app.repository.UserRepository;
import infinityshopping.online.app.service.PaymentCartService;
import infinityshopping.online.app.service.UserNotFoundException;
import infinityshopping.online.app.service.UserService;
import infinityshopping.online.app.service.dto.PaymentCartDTO;
import infinityshopping.online.app.service.mapper.PaymentCartMapper;
import infinityshopping.online.app.web.rest.errors.BadRequestAlertException;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PaymentCartServiceImpl implements PaymentCartService {

  private final Logger log = LoggerFactory.getLogger(PaymentCartServiceImpl.class);

  private static final String ENTITY_NAME = "Payment Cart ";

  private final PaymentCartRepository paymentCartRepository;

  private final UserRepository userRepository;

  private final CartRepository cartRepository;

  private final PaymentCartMapper paymentCartMapper;

  private final UserService userService;

  private Cart cart;


  public PaymentCartServiceImpl(PaymentCartRepository paymentCartRepository,
      PaymentCartMapper paymentCartMapper,
      UserRepository userRepository, UserService userService,
      CartRepository cartRepository) {
    this.paymentCartRepository = paymentCartRepository;
    this.paymentCartMapper = paymentCartMapper;
    this.userRepository = userRepository;
    this.userService = userService;
    this.cartRepository = cartRepository;
  }

  @Override
  @Transactional
  public PaymentCartDTO save(PaymentCartDTO paymentCartDto) {
    log.debug("Request to save only update PaymentCart : {}", paymentCartDto);

    PaymentCart paymentCartOfLoggedUser = new PaymentCart();

    User currentLoggedUser = new User();

    currentLoggedUser =
        userRepository.findOneByLogin(userService.getCurrentUserLogin())
        .orElseThrow(() -> new UserNotFoundException());

    paymentCartOfLoggedUser =
        paymentCartRepository.findByCartId(currentLoggedUser.getCart().getId())
        .orElseThrow(() ->
            new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));

    paymentCartOfLoggedUser = paymentCartMapper.toEntity(paymentCartDto);

    setCartIdToPaymentCartOfLoggedUserBecauseItIsNotInDto(
        paymentCartOfLoggedUser, currentLoggedUser);

    setAmountOfShipmentToProperCartOfUser(paymentCartOfLoggedUser);
    setAmountOfOrderToProperCartOfUser(paymentCartOfLoggedUser);

    paymentCartOfLoggedUser = paymentCartRepository.save(paymentCartOfLoggedUser);

    return paymentCartMapper.toDto(paymentCartOfLoggedUser);
  }

  private void setCartIdToPaymentCartOfLoggedUserBecauseItIsNotInDto(
      PaymentCart paymentCartOfLoggedUser, User currentLoggedUser) {
    paymentCartOfLoggedUser.setCart(currentLoggedUser.getCart());
  }

  private void setAmountOfShipmentToProperCartOfUser(PaymentCart paymentCartOfLoggedUser) {
    cart = cartRepository.findById(paymentCartOfLoggedUser.getCart().getId()).get();

    cart.setAmountOfShipmentNet(paymentCartOfLoggedUser.getPriceNet());
    cart.setAmountOfShipmentGross(paymentCartOfLoggedUser.getPriceGross());

    cartRepository.save(cart);
  }

  private void setAmountOfOrderToProperCartOfUser(PaymentCart paymentCartOfLoggedUser) {
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
    User currentLoggedUser = new User();

    currentLoggedUser = userRepository.findOneByLogin(userService.getCurrentUserLogin())
        .orElseThrow(() -> new UserNotFoundException());

    return paymentCartRepository.findByCartId(currentLoggedUser.getCart().getId())
        .map(paymentCartMapper::toDto);
  }
}
