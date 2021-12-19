package infinityshopping.online.app.web.rest;

import infinityshopping.online.app.domain.Product;
import infinityshopping.online.app.repository.ProductInCartRepository;
import infinityshopping.online.app.repository.ProductRepository;
import infinityshopping.online.app.service.ProductInCartService;
import infinityshopping.online.app.service.dto.ProductInCartDTO;
import infinityshopping.online.app.service.errors.ProductNotFoundException;
import infinityshopping.online.app.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

@RestController
@RequestMapping("/api")
public class ProductInCartResource {

  private final Logger log = LoggerFactory.getLogger(ProductInCartResource.class);

  private static final String ENTITY_NAME = "productInCart";

  @Value("${jhipster.clientApp.name}")
  private String applicationName;

  private final ProductInCartService productInCartService;

  private final ProductInCartRepository productInCartRepository;

  private final ProductRepository productRepository;

  public ProductInCartResource(ProductInCartService productInCartService,
      ProductInCartRepository productInCartRepository,
      ProductRepository productRepository) {
    this.productInCartService = productInCartService;
    this.productInCartRepository = productInCartRepository;
    this.productRepository = productRepository;
  }

  @PostMapping("/product-in-carts")
  public ResponseEntity<ProductInCartDTO> createProductInCart(
      @RequestBody ProductInCartDTO productInCartDto) throws URISyntaxException {
    log.debug("REST request to save ProductInCart : {}", productInCartDto);
    if (productInCartDtoIdIsNotEqualNull(productInCartDto)) {
      throw new BadRequestAlertException("A new productInCart cannot already have an ID",
          ENTITY_NAME, "idexists");
    }
    if (userOrderedMoreProductQuantityThenIsInTheStock(productInCartDto)) {
      throw new BadRequestAlertException("We do not have that much of this",
          ENTITY_NAME, "inStock");
    }

    ProductInCartDTO result = productInCartService.save(productInCartDto);
    return ResponseEntity
        .created(new URI("/api/product-in-carts/" + result.getId()))
        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME,
            result.getId().toString()))
        .body(result);
  }

  @PutMapping("/product-in-carts/{id}")
  public ResponseEntity<ProductInCartDTO> updateProductInCart(
      @PathVariable(value = "id", required = false) final Long id,
      @RequestBody ProductInCartDTO productInCartDto
  ) throws URISyntaxException {
    log.debug("REST request to update ProductInCart : {}, {}", id, productInCartDto);
    if (productInCartDtoIdIsEqualNull(productInCartDto)) {
      throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
    }
    if (productInCartDtoIsNotEqualWithIdSended(productInCartDto, id)) {
      throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
    }
    if (productInCartDtoDoesNotExistInTheDatabase(productInCartDto)) {
      throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
    }
    if (userOrderedMoreProductQuantityThenIsInTheStock(productInCartDto)) {
      throw new BadRequestAlertException("We do not have that much of this",
          ENTITY_NAME, "inStock");
    }

    ProductInCartDTO result = productInCartService.save(productInCartDto);
    return ResponseEntity
        .ok()
        .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME,
            productInCartDto.getId().toString()))
        .body(result);
  }

  @GetMapping("/product-in-carts/{id}")
  public ResponseEntity<ProductInCartDTO> getProductInCart(@PathVariable Long id) {
    log.debug("REST request to get ProductInCart : {}", id);
    Optional<ProductInCartDTO> productInCartDto = productInCartService.findOne(id);
    return ResponseUtil.wrapOrNotFound(productInCartDto);
  }

  @GetMapping("/product-in-carts/userCart")
  public List<ProductInCartDTO> getAllProductInCartOfCurrentUser(
      Pageable pageable) {
    log.debug("REST request to get all ProductInCart of current User");
    return productInCartService.findByCartId(pageable);
  }

  @DeleteMapping("/product-in-carts/{id}")
  public ResponseEntity<Void> deleteProductInCart(@PathVariable Long id) {
    log.debug("REST request to delete ProductInCart : {}", id);
    productInCartService.delete(id);
    return ResponseEntity
        .noContent()
        .headers(
            HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
        .build();
  }

  private boolean productInCartDtoIdIsNotEqualNull(ProductInCartDTO productInCartDto) {
    return (productInCartDto.getId() != null);
  }

  private boolean productInCartDtoIdIsEqualNull(ProductInCartDTO productInCartDto) {
    return (productInCartDto.getId() == null);
  }

  private boolean productInCartDtoIsNotEqualWithIdSended(ProductInCartDTO productInCartDto,
      Long id) {
    return (!Objects.equals(id, productInCartDto.getId()));
  }

  private boolean productInCartDtoDoesNotExistInTheDatabase(ProductInCartDTO productInCartDto) {
    return (!productInCartRepository.existsById(productInCartDto.getId()));
  }

  private boolean userOrderedMoreProductQuantityThenIsInTheStock(
      ProductInCartDTO productInCartDto) {
    return (productInCartDto.getQuantity().compareTo(
        checkIfProductExist(productInCartDto).getStock()) > 0);
  }

  private Product checkIfProductExist(ProductInCartDTO productInCartDto) {
    return productRepository.findById(productInCartDto.getProductId())
        .orElseThrow(ProductNotFoundException::new);
  }
}
