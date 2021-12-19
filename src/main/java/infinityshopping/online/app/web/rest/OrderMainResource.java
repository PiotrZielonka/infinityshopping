package infinityshopping.online.app.web.rest;

import infinityshopping.online.app.domain.Product;
import infinityshopping.online.app.domain.User;
import infinityshopping.online.app.repository.OrderMainRepository;
import infinityshopping.online.app.repository.ProductRepository;
import infinityshopping.online.app.repository.UserRepository;
import infinityshopping.online.app.security.SecurityUtils;
import infinityshopping.online.app.service.OrderMainService;
import infinityshopping.online.app.service.errors.UserNotFoundException;
import infinityshopping.online.app.service.dto.OrderMainDTO;
import infinityshopping.online.app.web.rest.errors.BadRequestAlertException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;


@RestController
@RequestMapping("/api")
public class OrderMainResource {

  private final Logger log = LoggerFactory.getLogger(OrderMainResource.class);

  private static final String ENTITY_NAME = "orderMain";

  @Value("${jhipster.clientApp.name}")
  private String applicationName;

  private final OrderMainService orderMainService;

  private final OrderMainRepository orderMainRepository;

  private final ProductRepository productRepository;

  private final UserRepository userRepository;

  private User currentLoggedUser;

  private Product product;

  public OrderMainResource(OrderMainService orderMainService,
      OrderMainRepository orderMainRepository,
      ProductRepository productRepository,
      UserRepository userRepository) {
    this.orderMainService = orderMainService;
    this.orderMainRepository = orderMainRepository;
    this.productRepository = productRepository;
    this.userRepository = userRepository;
  }

  @PostMapping("/order-mains")
  @Transactional
  public ResponseEntity<OrderMainDTO> createOrderMain(@RequestBody OrderMainDTO orderMainDto)
      throws URISyntaxException {
    log.debug("REST request to save OrderMain : {}", orderMainDto);
    if (orderMainDto.getId() != null) {
      throw new BadRequestAlertException("A new orderMain cannot already have an ID", ENTITY_NAME,
          "idexists");
    }

    minusProperValueStockInProduct();

    OrderMainDTO result = orderMainService.save(orderMainDto);
    return ResponseEntity.created(new URI("/api/order-mains/" + result.getId()))
        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME,
            result.getId().toString()))
        .body(result);
  }

  @PatchMapping(value = "/order-mains/editStatus/{id}", consumes = {"application/json",
      "application/merge-patch+json"})
  public ResponseEntity<OrderMainDTO> partialUpdateOrderMain(
      @PathVariable(value = "id", required = false) final Long id,
      @RequestBody OrderMainDTO orderMainDto
  ) throws URISyntaxException {
    log.debug("REST request to partial update OrderMain partially : {}, {}", id, orderMainDto);
    if (orderMainDto.getId() == null) {
      throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
    }
    if (!Objects.equals(id, orderMainDto.getId())) {
      throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
    }

    if (!orderMainRepository.existsById(id)) {
      throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
    }

    Optional<OrderMainDTO> result = orderMainService.partialUpdate(orderMainDto);

    return ResponseUtil.wrapOrNotFound(result, HeaderUtil.createEntityUpdateAlert(
        applicationName, true, ENTITY_NAME, orderMainDto.getId().toString())
    );
  }

  @GetMapping("/order-mains/all")
  public List<OrderMainDTO> getAllOrderMains() {
    log.debug("REST request to get all OrderMains");
    return orderMainService.findAll();
  }

  @GetMapping("/order-mains/{id}")
  public ResponseEntity<OrderMainDTO> getOrderMain(@PathVariable Long id) {
    log.debug("REST request to get OrderMain : {}", id);
    Optional<OrderMainDTO> orderMainDto = orderMainService.findOne(id);
    return ResponseUtil.wrapOrNotFound(orderMainDto);
  }

  @DeleteMapping("/order-mains/delete/{id}")
  public ResponseEntity<Void> deleteOrderMainAndAllProductInOrderOrderMain(@PathVariable Long id) {
    log.debug("REST request to delete OrderMain : {}", id);
    orderMainService.deleteOrderMainAndAllProductInOrdersByIdOrderMain(id);
    return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(
        applicationName, true, ENTITY_NAME, id.toString())).build();
  }

  @GetMapping("/order-mains/currentUser")
  public List<OrderMainDTO> getAllOrderMainByCurrentUserLogin() {
    log.debug("REST request to get all OrderMains of currentUser");
    return orderMainService.getAllOrderMainByCurrentUserLogin();
  }

  private void minusProperValueStockInProduct() {
    currentLoggedUser = checkIfUserExist();

    currentLoggedUser.getCart().getProductInCarts().forEach(productInCart -> {

      product = productRepository.findById(productInCart.getProductId()).get();
      product.setStock(product.getStock().subtract(productInCart.getQuantity()));
      productRepository.save(product);

      if (product.getStock().compareTo(BigDecimal.ZERO) < 0) {
        throw new BadRequestAlertException("Someone bought a ", ENTITY_NAME,
            "fromYourCartAndWeDoNotHaveItYetCallUs");
      }

    });
  }

  private User checkIfUserExist() {
    return userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin()
            .orElseThrow(UserNotFoundException::new))
        .orElseThrow(UserNotFoundException::new);
  }
}
