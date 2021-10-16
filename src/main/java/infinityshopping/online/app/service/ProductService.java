package infinityshopping.online.app.service;

import infinityshopping.online.app.service.dto.ProductDTO;
import infinityshopping.online.app.service.dto.ProductDtoImageNamePriceGross;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductService {

  ProductDTO save(ProductDTO productDto);

  Page<ProductDTO> findAll(Pageable pageable);

  Page<ProductDtoImageNamePriceGross> findAllImageNamePriceGross(Pageable pageable);

  Optional<ProductDTO> findOne(Long id);

  void delete(Long id);
}
