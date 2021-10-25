package infinityshopping.online.app.service.impl;

import infinityshopping.online.app.domain.Cart;
import infinityshopping.online.app.domain.Product;
import infinityshopping.online.app.domain.ProductInCart;
import infinityshopping.online.app.domain.User;
import infinityshopping.online.app.repository.CartRepository;
import infinityshopping.online.app.repository.PaymentCartRepository;
import infinityshopping.online.app.repository.ProductInCartRepository;
import infinityshopping.online.app.repository.ProductRepository;
import infinityshopping.online.app.repository.UserRepository;
import infinityshopping.online.app.security.SecurityUtils;
import infinityshopping.online.app.service.ProductInCartService;
import infinityshopping.online.app.service.UserNotFoundException;
import infinityshopping.online.app.service.dto.ProductInCartDTO;
import infinityshopping.online.app.service.mapper.ProductInCartMapper;
import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductInCartServiceImpl implements ProductInCartService {

  private final Logger log = LoggerFactory.getLogger(ProductInCartServiceImpl.class);

  private final ProductInCartRepository productInCartRepository;

  private final ProductInCartMapper productInCartMapper;

  private final ProductRepository productRepository;

  private final UserRepository userRepository;

  private final CartRepository cartRepository;

  private final PaymentCartRepository paymentCartRepository;

  private User currentLoggedUser;

  private User userForFindByCartId;

  private Product product;

  private Cart cart;

  private BigDecimal amountOfCartNet;

  private BigDecimal amountOfCartGross;

  private ProductInCart productInCart;

  private BigDecimal amountOfOrderNet;

  private BigDecimal amountOfOrderGross;

  private static final String ENTITY_NAME = "product";

  public ProductInCartServiceImpl(ProductInCartRepository productInCartRepository,
      ProductInCartMapper productInCartMapper,
      ProductRepository productRepository, UserRepository userRepository,
      CartRepository cartRepository,
      PaymentCartRepository paymentCartRepository) {
    this.productInCartRepository = productInCartRepository;
    this.productInCartMapper = productInCartMapper;
    this.productRepository = productRepository;
    this.userRepository = userRepository;
    this.cartRepository = cartRepository;
    this.paymentCartRepository = paymentCartRepository;
  }

  @Override
  @Transactional
  public ProductInCartDTO save(ProductInCartDTO productInCartDto) {
    log.debug("Request to save ProductInCart : {}", productInCartDto);
    ProductInCart productInCart = productInCartMapper.toEntity(productInCartDto);

    setTotalPriceNet(productInCart);
    setTotalPriceGross(productInCart);
    setSelectedProductByCurrentUserToCurrentUserOfCart(productInCart);
    setAmountOfCartNetToProperCartOfUser(productInCart);
    setAmountOfCartGrossToProperCartOfUser(productInCart);
    setAmountOfOrderNetToProperCartOfUser(productInCart);
    setAmountOfOrderGrossToProperCartOfUser(productInCart);
    productInCart = productInCartRepository.save(productInCart);

    return productInCartMapper.toDto(productInCart);
  }

  private void setSelectedProductByCurrentUserToCurrentUserOfCart(ProductInCart productInCart) {
    currentLoggedUser = checkIfUserExist();
    productInCart.setCart(currentLoggedUser.getCart());
    currentLoggedUser.getCart().addProductInCart(productInCart);
    productInCartRepository.save(productInCart);
  }

  private void setTotalPriceNet(ProductInCart productInCart) {
    productInCart.setTotalPriceNet(
        productInCart.getQuantity().multiply(productInCart.getPriceNet()));
    productInCartRepository.save(productInCart);
  }

  private void setTotalPriceGross(ProductInCart productInCart) {
    productInCart.setTotalPriceGross(
        productInCart.getQuantity().multiply(productInCart.getPriceGross()));
    productInCartRepository.save(productInCart);
  }

  private void setAmountOfCartNetToProperCartOfUser(ProductInCart productInCart) {
    cart = cartRepository.findById(productInCart.getCart().getId()).get();

    amountOfCartNet = cart.getAmountOfCartNet();

    amountOfCartNet = BigDecimal.ZERO;

    cart.getProductInCarts().forEach(productInCartForEach -> {
      amountOfCartNet = amountOfCartNet.add(productInCartForEach.getTotalPriceNet());
    });

    cart.setAmountOfCartNet(amountOfCartNet);
    cartRepository.save(cart);
  }

  private void setAmountOfCartGrossToProperCartOfUser(ProductInCart productInCart) {
    cart = cartRepository.findById(productInCart.getCart().getId()).get();

    amountOfCartGross = cart.getAmountOfCartGross();

    amountOfCartGross = BigDecimal.ZERO;

    cart.getProductInCarts().forEach(productInCartForEach -> {
      amountOfCartGross = amountOfCartGross.add(productInCartForEach.getTotalPriceGross());
    });

    cart.setAmountOfCartGross(amountOfCartGross);
    cartRepository.save(cart);
  }
  
  private void setAmountOfOrderNetToProperCartOfUser(ProductInCart productInCart) {
    cart = cartRepository.findById(productInCart.getCart().getId()).get();

    cart.setAmountOfOrderNet(cart.getAmountOfCartNet().add(cart.getAmountOfShipmentNet()));
    cartRepository.save(cart);
  }

  private void setAmountOfOrderGrossToProperCartOfUser(ProductInCart productInCart) {
    cart = cartRepository.findById(productInCart.getCart().getId()).get();

    cart.setAmountOfOrderGross(cart.getAmountOfCartGross().add(cart.getAmountOfShipmentGross()));
    cartRepository.save(cart);
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<ProductInCartDTO> findOne(Long id) {
    log.debug("Request to get ProductInCart : {}", id);
    return productInCartRepository.findById(id).map(productInCartMapper::toDto);
  }

  @Override
  @Transactional(readOnly = true)
  public List<ProductInCartDTO> findByCartId(Pageable pageable) {
    userForFindByCartId = checkIfUserExist();
    log.debug("Request to get all ProductInCart of current User");
    return productInCartRepository.findByCartId(userForFindByCartId.getCart().getId(), pageable)
        .stream()
        .map(productInCartMapper::toDto)
        .collect(Collectors.toCollection(LinkedList::new));
  }

  @Override
  @Transactional
  public void delete(Long id) {
    log.debug("Request to delete ProductInCart : {}", id);

    productInCart = productInCartRepository.findById(id).get();

    minusSelectedProductTotalPriceNetToProperAmountOfCartNet(productInCart);
    minusSelectedProductTotalPriceGrossToProperAmountOfCartGross(productInCart);
    minusSelectedProductTotalPriceNetToProperAmountOfOrderNet(productInCart);
    minusSelectedProductTotalPriceGrossToProperAmountOfOrderGross(productInCart);

    productInCartRepository.deleteById(id);
  }

  private void minusSelectedProductTotalPriceNetToProperAmountOfCartNet(
      ProductInCart productInCart) {
    cart = cartRepository.findById(productInCart.getCart().getId()).get();
    cart.setAmountOfCartNet(cart.getAmountOfCartNet().subtract(productInCart.getTotalPriceNet()));
    cartRepository.save(cart);
  }

  private void minusSelectedProductTotalPriceGrossToProperAmountOfCartGross(
      ProductInCart productInCart) {
    cart = cartRepository.findById(productInCart.getCart().getId()).get();
    cart.setAmountOfCartGross(cart.getAmountOfCartGross()
        .subtract(productInCart.getTotalPriceGross()));
    cartRepository.save(cart);
  }

  private void minusSelectedProductTotalPriceNetToProperAmountOfOrderNet(
      ProductInCart productInCart) {
    cart = cartRepository.findById(productInCart.getCart().getId()).get();
    cart.setAmountOfOrderNet(cart.getAmountOfOrderNet().subtract(productInCart.getTotalPriceNet()));
    cartRepository.save(cart);
  }

  private void minusSelectedProductTotalPriceGrossToProperAmountOfOrderGross(
      ProductInCart productInCart) {
    cart = cartRepository.findById(productInCart.getCart().getId()).get();
    cart.setAmountOfOrderGross(cart.getAmountOfOrderGross()
        .subtract(productInCart.getTotalPriceGross()));
    cartRepository.save(cart);
  }

  private User checkIfUserExist() {
    currentLoggedUser = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin()
            .orElseThrow(() -> new UserNotFoundException()))
        .orElseThrow(() -> new UserNotFoundException());
    return currentLoggedUser;
  }
}
