package infinityshopping.online.app.web.rest;

import infinityshopping.online.app.domain.Product;
import infinityshopping.online.app.repository.ProductInOrderMainRepository;
import infinityshopping.online.app.repository.ProductRepository;
import infinityshopping.online.app.service.ProductInOrderMainService;
import infinityshopping.online.app.service.dto.ProductInOrderMainDTO;
import infinityshopping.online.app.service.errors.ProductNotFoundException;
import infinityshopping.online.app.web.rest.errors.BadRequestAlertException;
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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

@RestController
@RequestMapping("/api")
public class ProductInOrderMainResource {

  private final Logger log = LoggerFactory.getLogger(ProductInOrderMainResource.class);

  private static final String ENTITY_NAME = "productInOrderMain";

  @Value("${jhipster.clientApp.name}")
  private String applicationName;

  private final ProductInOrderMainService productInOrderMainService;

  private final ProductInOrderMainRepository productInOrderMainRepository;

  private final ProductRepository productRepository;


  public ProductInOrderMainResource(
      ProductInOrderMainService productInOrderMainService,
      ProductInOrderMainRepository productInOrderMainRepository,
      ProductRepository productRepository) {
    this.productInOrderMainService = productInOrderMainService;
    this.productInOrderMainRepository = productInOrderMainRepository;
    this.productRepository = productRepository;
  }

  @PutMapping("/product-in-order-mains/{id}")
  public ResponseEntity<ProductInOrderMainDTO> updateProductInOrderMain(
      @PathVariable(value = "id", required = false) final Long id,
      @RequestBody ProductInOrderMainDTO productInOrderMainDto
  ) throws URISyntaxException {
    log.debug("REST request to update ProductInOrderMain : {}, {}", id, productInOrderMainDto);
    if (productInOrderMainDto.getId() == null) {
      throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
    }
    if (!Objects.equals(id, productInOrderMainDto.getId())) {
      throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
    }

    if (!productInOrderMainRepository.existsById(id)) {
      throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
    }

    checkProperProductQuantity(productInOrderMainDto);

    ProductInOrderMainDTO result = productInOrderMainService.save(productInOrderMainDto);
    return ResponseEntity.ok()
        .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME,
            productInOrderMainDto.getId().toString()))
        .body(result);
  }

  @GetMapping("/product-in-order-mains/byId/{id}")
  public ResponseEntity<ProductInOrderMainDTO> getProductInOrderMain(@PathVariable Long id) {
    log.debug("REST request to get ProductInOrderMain : {}", id);
    Optional<ProductInOrderMainDTO> productInOrderMainDto = productInOrderMainService.findOne(id);
    return ResponseUtil.wrapOrNotFound(productInOrderMainDto);
  }

  @GetMapping("/product-in-order-mains/orderDetails/{id}")
  public List<ProductInOrderMainDTO> getAllProductInOrderMainsByIdOrderMain(
      @PathVariable Long id, Pageable pageable) {
    log.debug("REST request to get all ProductInOrder by id OrderMain");
    return productInOrderMainService.findByOrderMainId(id, pageable);
  }

  @DeleteMapping("/product-in-order-mains/{id}")
  public ResponseEntity<Void> deleteProductInOrderMain(@PathVariable Long id) {
    log.debug("REST request to delete ProductInOrderMain : {}", id);
    productInOrderMainService.delete(id);
    return ResponseEntity.noContent()
        .headers(
            HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
        .build();
  }

  private void checkProperProductQuantity(ProductInOrderMainDTO productInOrderMainDto) {
    Product product = checkIfProductExist(productInOrderMainDto);

    if (productInOrderMainDto.getQuantity().compareTo(product.getStock()) > 0) {
      throw new BadRequestAlertException("We do not have that much of this",
          ENTITY_NAME, "inStock");
    }
  }

  private Product checkIfProductExist(ProductInOrderMainDTO productInOrderMainDto) {
    return productRepository.findById(productInOrderMainDto.getProductId())
        .orElseThrow(ProductNotFoundException::new);
  }
}
