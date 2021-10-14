package infinityshopping.online.app.service.mapper;

import infinityshopping.online.app.domain.Product;
import infinityshopping.online.app.service.dto.ProductDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {})
public interface ProductMapper extends EntityMapper<ProductDTO, Product> {}
