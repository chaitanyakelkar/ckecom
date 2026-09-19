package org.ecommerce.ckecom.service;

import jakarta.validation.constraints.NotNull;
import org.ecommerce.ckecom.config.AppConstants;
import org.ecommerce.ckecom.exception.APIException;
import org.ecommerce.ckecom.exception.ResourceNotFoundException;
import org.ecommerce.ckecom.model.Category;
import org.ecommerce.ckecom.model.Product;
import org.ecommerce.ckecom.payload.ProductDTO;
import org.ecommerce.ckecom.payload.ProductResponse;
import org.ecommerce.ckecom.repository.CategoryRepository;
import org.ecommerce.ckecom.repository.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private FileService fileService;

    @Value("${project.image.path}")
    private String path;

    @Override
    public ProductDTO addProduct(ProductDTO productDTO, Long categoryId) throws ResourceNotFoundException, APIException {
        Optional<Category> categoryOptional = categoryRepository.findById(categoryId);
        if (categoryOptional.isEmpty()) throw new ResourceNotFoundException("Category", "categoryId", categoryId);
        Category category = categoryOptional.get();
        Product newProduct = modelMapper.map(productDTO, Product.class);
        Optional<Product> savedProductOptional = productRepository.findByProductName(newProduct.getProductName());
        if (savedProductOptional.isPresent()) throw new APIException("Product with productName = " + newProduct.getProductName() + " already Exists");
        newProduct.setCategory(category);
        newProduct.setImage("default.jpg");
        newProduct.setSpecialPrice(newProduct.getPrice() - newProduct.getPrice() * newProduct.getDiscount() * 0.01);
        Product savedProduct = productRepository.save(newProduct);
        return modelMapper.map(savedProduct, ProductDTO.class);
    }

    @Override
    public ProductResponse getAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) throws APIException{
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("ascending")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<Product> page = productRepository.findAll(pageable);
        return createProductResponse(page);
    }

    @Override
    public ProductResponse getAllProductsByCategory(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder, Long categoryId) throws APIException {
        Optional<Category> categoryOptional = categoryRepository.findById(categoryId);
        if (categoryOptional.isEmpty())
            throw new ResourceNotFoundException("Category", "categoryId", categoryId);
        Category category = categoryOptional.get();

        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("ascending")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<Product> page = productRepository.findAllByCategory(category, pageable);
        return createProductResponse(page);
    }

    @Override
    public ProductResponse getAllProductsByKeyword(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder, String keyword) throws APIException {
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("ascending")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<Product> page = productRepository.findAllByProductNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(keyword, keyword, pageable);
        return createProductResponse(page);
    }

    @Override
    public ProductDTO updateProduct(Long productId, ProductDTO productDTO) {
        Optional<Product> productOptional = productRepository.findById(productId);
        if (productOptional.isEmpty())
            throw new ResourceNotFoundException("Product", "productId", productId);
        Product savedProduct = productOptional.get();
        Product newProduct = modelMapper.map(productDTO, Product.class);
        savedProduct.updateProduct(newProduct);
        Product updatedProduct = productRepository.save(savedProduct);
        return modelMapper.map(updatedProduct, ProductDTO.class);
    }

    @Override
    public ProductDTO deleteProduct(Long productId) throws ResourceNotFoundException {
        Optional<Product> productOptional = productRepository.findById(productId);
        if (productOptional.isEmpty())
            throw new ResourceNotFoundException("Product", "productId", productId);
        productRepository.deleteById(productId);
        return modelMapper.map(productOptional.get(), ProductDTO.class);
    }

    @Override
    public ProductDTO updateProductImage(Long productId, MultipartFile image) throws ResourceNotFoundException, IOException {
        Optional<Product> productOptional = productRepository.findById(productId);
        if (productOptional.isEmpty())
            throw new ResourceNotFoundException("Product", "productId", productId);
        Product product = productOptional.get();
        String fileName = fileService.uploadImage(path, image);
        if (!product.getImage().equalsIgnoreCase(AppConstants.DEFAULT_IMAGE))
            fileService.deleteImage(path, product.getImage());
        product.setImage(fileName);
        Product updatedProduct = productRepository.save(product);
        return modelMapper.map(updatedProduct, ProductDTO.class);
    }

    @NotNull
    public ProductResponse createProductResponse(Page<Product> page){
        List<Product> products = page.getContent();
        if (products.isEmpty())
            throw new APIException("No Products Exist !");
        List<ProductDTO> productDTOS = products.stream().map(product -> modelMapper.map(product, ProductDTO.class)).toList();
        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDTOS);
        productResponse.setPageSize(page.getSize());
        productResponse.setPageNumber(page.getNumber());
        productResponse.setTotalElements(page.getTotalElements());
        productResponse.setTotalPages(page.getTotalPages());
        productResponse.setLastPage(page.isLast());
        return productResponse;
    }
}
