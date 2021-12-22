package infinityshopping.online.app.service.impl;

import infinityshopping.online.app.domain.OrderMain;
import infinityshopping.online.app.domain.PaymentOrderMain;
import infinityshopping.online.app.domain.ProductInOrderMain;
import infinityshopping.online.app.domain.ShipmentOrderMain;
import infinityshopping.online.app.domain.User;
import infinityshopping.online.app.domain.enumeration.OrderMainStatusEnum;
import infinityshopping.online.app.repository.CartRepository;
import infinityshopping.online.app.repository.OrderMainRepository;
import infinityshopping.online.app.repository.PaymentOrderMainRepository;
import infinityshopping.online.app.repository.ProductInCartRepository;
import infinityshopping.online.app.repository.ProductInOrderMainRepository;
import infinityshopping.online.app.repository.ShipmentOrderMainRepository;
import infinityshopping.online.app.repository.UserRepository;
import infinityshopping.online.app.security.SecurityUtils;
import infinityshopping.online.app.service.OrderMainService;
import infinityshopping.online.app.service.dto.OrderMainDTO;
import infinityshopping.online.app.service.errors.UserNotFoundException;
import infinityshopping.online.app.service.mapper.OrderMainMapper;
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
public class OrderMainServiceImpl implements OrderMainService {
  private final Logger log = LoggerFactory.getLogger(OrderMainServiceImpl.class);

  private final CartRepository cartRepository;

  private final OrderMainRepository orderMainRepository;

  private final UserRepository userRepository;

  private final PaymentOrderMainRepository paymentOrderMainRepository;

  private final ShipmentOrderMainRepository shipmentOrderMainRepository;

  private final ProductInOrderMainRepository productInOrderMainRepository;

  private final ProductInCartRepository productInCartRepository;

  private final OrderMainMapper orderMainMapper;

  private User currentLoggedUser;


  public OrderMainServiceImpl(OrderMainRepository orderMainRepository,
      OrderMainMapper orderMainMapper, UserRepository userRepository,
      PaymentOrderMainRepository paymentOrderMainRepository,
      ShipmentOrderMainRepository shipmentOrderMainRepository,
      ProductInOrderMainRepository productInOrderMainRepository,
      ProductInCartRepository productInCartRepository,
      CartRepository cartRepository) {
    this.orderMainRepository = orderMainRepository;
    this.orderMainMapper = orderMainMapper;
    this.userRepository = userRepository;
    this.paymentOrderMainRepository = paymentOrderMainRepository;
    this.shipmentOrderMainRepository = shipmentOrderMainRepository;
    this.productInOrderMainRepository = productInOrderMainRepository;
    this.productInCartRepository = productInCartRepository;
    this.cartRepository = cartRepository;
  }


  @Override
  @Transactional
  public OrderMainDTO save(OrderMainDTO orderMainDto) {
    OrderMain orderMain = orderMainMapper.toEntity(orderMainDto);
    setProperOrderMainStatus(orderMain);
    setCurrentUserToNewOrderMain(orderMain);
    setProductsFromCartToNewOrderMain(orderMain);
    deleteProductsFromCart();
    createPaymentOrderMain(orderMain);
    createShipmentOrderMain(orderMain);
    setAmountsOfCartZero();
    setAmountsOfOrderZero();
    return orderMainMapper.toDto(orderMain);
  }

  @Override
  @Transactional
  public Optional<OrderMainDTO> partialUpdate(OrderMainDTO orderMainDto) {
    log.debug("Request to partially update OrderMain : {}", orderMainDto);

    return orderMainRepository
        .findById(orderMainDto.getId())
        .map(existingOrderMain -> {
          orderMainMapper.partialUpdate(existingOrderMain, orderMainDto);

          return existingOrderMain;
        })
        .map(orderMainRepository::save)
        .map(orderMainMapper::toDto);
  }

  private void setCurrentUserToNewOrderMain(OrderMain orderMain) {
    currentLoggedUser = checkIfUserExist();

    orderMain.setBuyerLogin(currentLoggedUser.getLogin());
    orderMain.setBuyerFirstName(currentLoggedUser.getFirstName());
    orderMain.setBuyerLastName(currentLoggedUser.getLastName());
    orderMain.setBuyerEmail(currentLoggedUser.getEmail());
    orderMain.setAmountOfCartNet(currentLoggedUser.getCart().getAmountOfCartNet());
    orderMain.setAmountOfCartGross(currentLoggedUser.getCart().getAmountOfCartGross());
    orderMain.setAmountOfShipmentNet(currentLoggedUser.getCart().getAmountOfShipmentNet());
    orderMain.setAmountOfShipmentGross(currentLoggedUser.getCart().getAmountOfShipmentGross());
    orderMain.setAmountOfOrderNet(currentLoggedUser.getCart().getAmountOfOrderNet());
    orderMain.setAmountOfOrderGross(currentLoggedUser.getCart().getAmountOfOrderGross());
    orderMainRepository.save(orderMain);
  }

  private void setProperOrderMainStatus(OrderMain orderMain) {
    currentLoggedUser = checkIfUserExist();

    orderMain.setOrderMainStatus(OrderMainStatusEnum.valueOf(
        String.valueOf(currentLoggedUser.getCart().getPaymentCart().getPaymentStatus())));
  }

  private void createPaymentOrderMain(OrderMain orderMain) {
    currentLoggedUser = checkIfUserExist();

    PaymentOrderMain paymentOrderMain = new PaymentOrderMain();

    paymentOrderMain.setName(currentLoggedUser.getCart().getPaymentCart().getName());
    paymentOrderMain.setPriceNet(currentLoggedUser.getCart().getPaymentCart().getPriceNet());
    paymentOrderMain.setVat(currentLoggedUser.getCart().getPaymentCart().getVat());
    paymentOrderMain.setPriceGross(currentLoggedUser.getCart().getPaymentCart().getPriceGross());
    paymentOrderMain.setOrderMain(orderMain);

    paymentOrderMainRepository.save(paymentOrderMain);
    orderMain.setPaymentOrderMain(paymentOrderMain);
  }

  private void createShipmentOrderMain(OrderMain orderMain) {
    currentLoggedUser = checkIfUserExist();

    ShipmentOrderMain shipmentOrderMain = new ShipmentOrderMain();

    shipmentOrderMain.setFirstName(currentLoggedUser.getCart().getShipmentCart().getFirstName());
    shipmentOrderMain.setLastName(currentLoggedUser.getCart().getShipmentCart().getLastName());
    shipmentOrderMain.setStreet(currentLoggedUser.getCart().getShipmentCart().getStreet());
    shipmentOrderMain.setPostalCode(currentLoggedUser.getCart().getShipmentCart().getPostalCode());
    shipmentOrderMain.setCity(currentLoggedUser.getCart().getShipmentCart().getCity());
    shipmentOrderMain.setCountry(currentLoggedUser.getCart().getShipmentCart().getCountry());
    shipmentOrderMain.setPhoneToTheReceiver(
        currentLoggedUser.getCart().getShipmentCart().getPhoneToTheReceiver());
    shipmentOrderMain.setFirm(currentLoggedUser.getCart().getShipmentCart().getFirm());
    shipmentOrderMain.setTaxNumber(currentLoggedUser.getCart().getShipmentCart().getTaxNumber());
    shipmentOrderMain.setOrderMain(orderMain);

    shipmentOrderMainRepository.save(shipmentOrderMain);
    orderMain.setShipmentOrderMain(shipmentOrderMain);
  }

  private void setProductsFromCartToNewOrderMain(OrderMain orderMain) {
    currentLoggedUser = checkIfUserExist();

    currentLoggedUser.getCart().getProductInCarts().forEach(productInCart -> {
      ProductInOrderMain productInOrderMain = new ProductInOrderMain();
      OrderMain orderMainForLoop = orderMain;

      productInOrderMain.setCategory(productInCart.getCategory());
      productInOrderMain.setName(productInCart.getName());
      productInOrderMain.setQuantity(productInCart.getQuantity());
      productInOrderMain.setPriceNet(productInCart.getPriceNet());
      productInOrderMain.setVat(productInCart.getVat());
      productInOrderMain.setPriceGross(productInCart.getPriceGross());
      productInOrderMain.setTotalPriceNet(productInCart.getTotalPriceNet());
      productInOrderMain.setTotalPriceGross(productInCart.getTotalPriceGross());
      productInOrderMain.setStock(productInCart.getStock());
      productInOrderMain.setDescription(productInCart.getDescription());
      productInOrderMain.setImage(productInCart.getImage());
      productInOrderMain.setImageContentType(productInCart.getImageContentType());
      productInOrderMain.setProductId(productInCart.getProductId());
      productInOrderMain.setOrderMain(orderMainForLoop);
      productInOrderMainRepository.save(productInOrderMain);
      orderMainForLoop.addProductInOrderMain(productInOrderMain);
    });
  }

  private void deleteProductsFromCart() {
    currentLoggedUser = checkIfUserExist();

    currentLoggedUser.getCart().getProductInCarts().forEach(productInCart ->
        productInCartRepository.deleteById(productInCart.getId())
    );
  }

  private void setAmountsOfCartZero() {
    currentLoggedUser = checkIfUserExist();

    currentLoggedUser.getCart().setAmountOfCartNet(BigDecimal.ZERO);
    currentLoggedUser.getCart().setAmountOfCartGross(BigDecimal.ZERO);
    cartRepository.save(currentLoggedUser.getCart());
  }

  private void setAmountsOfOrderZero() {
    currentLoggedUser = checkIfUserExist();

    currentLoggedUser.getCart().setAmountOfOrderNet(BigDecimal.ZERO);
    currentLoggedUser.getCart().setAmountOfOrderGross(BigDecimal.ZERO);
    cartRepository.save(currentLoggedUser.getCart());
  }

  @Override
  @Transactional(readOnly = true)
  public List<OrderMainDTO> getAllOrderMainByCurrentUserLogin() {
    log.debug("Request to get all OrderMain by current user login");
    return orderMainRepository.getAllOrderMainByCurrentUserLogin().stream()
        .map(orderMainMapper::toDto)
        .collect(Collectors.toCollection(LinkedList::new));
  }

  @Override
  @Transactional(readOnly = true)
  public List<OrderMainDTO> findAll() {
    log.debug("Request to get all OrderMains");
    return orderMainRepository.findAll().stream()
        .map(orderMainMapper::toDto)
        .collect(Collectors.toCollection(LinkedList::new));
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<OrderMainDTO> findOne(Long id) {
    log.debug("Request to get OrderMain : {}", id);
    return orderMainRepository.findById(id)
        .map(orderMainMapper::toDto);
  }

  @Override
  @Transactional
  public void deleteOrderMainAndAllProductInOrdersByIdOrderMain(Long id) {
    log.debug("Request to delete OrderMain and all productInOrderMain "
        + "by id OrderMain : {}", id);
    orderMainRepository.deleteById(id);
  }

  private User checkIfUserExist() {
    return userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin()
            .orElseThrow(UserNotFoundException::new))
        .orElseThrow(UserNotFoundException::new);
  }
}
