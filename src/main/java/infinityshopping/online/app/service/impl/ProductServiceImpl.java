package infinityshopping.online.app.service.impl;

import infinityshopping.online.app.domain.Product;
import infinityshopping.online.app.repository.ProductRepository;
import infinityshopping.online.app.service.ProductService;
import infinityshopping.online.app.service.dto.ProductDTO;
import infinityshopping.online.app.service.dto.ProductDtoImageNamePriceGross;
import infinityshopping.online.app.service.mapper.ProductMapper;
import java.math.BigDecimal;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductServiceImpl implements ProductService {

    private final Logger log = LoggerFactory.getLogger(ProductServiceImpl.class);

    private final ProductRepository productRepository;

    private final ProductMapper productMapper;

    public ProductServiceImpl(ProductRepository productRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    @Override
    @Transactional
    public ProductDTO save(ProductDTO productDto) {
        log.debug("Request to save Product : {}", productDto);
        Product product = productMapper.toEntity(productDto);
        setQuantityOne(product);
        addVat(product);
        product = productRepository.save(product);
        return productMapper.toDto(product);
    }

    private void setQuantityOne(Product product) {
        product.setQuantity(BigDecimal.ONE);
    }

    private void addVat(Product product) {
        product.setPriceGross(product.getPriceNet().add(product.getPriceNet().multiply(product.getVat().movePointLeft(2))));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Products");
        return productRepository.findAll(pageable).map(productMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductDtoImageNamePriceGross> findAllImageNamePriceGross(Pageable pageable) {
        log.debug("Request to get all Products only with Image Name PriceGross");
        return productRepository.findAll(pageable).map(productMapper::toDtoImageNamePriceGross);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProductDTO> findOne(Long id) {
        log.debug("Request to get Product : {}", id);
        return productRepository.findById(id).map(productMapper::toDto);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        log.debug("Request to delete Product : {}", id);
        productRepository.deleteById(id);
    }
}
