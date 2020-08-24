package ro.bogdan.shopappproduct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ProductController {

//    @Autowired
//    private ProductDao productDao;

    @PostMapping("/save-product")
    public ModelAndView saveProduct(@RequestParam("name") String name,
                                    @RequestParam("description") String description,
                                    @RequestParam("category") String category,
                                    @RequestParam("price") Double price) {
        Product product = new Product();
        product.setName(name);
        product.setDescription(description);
        product.setPrice(price);
        product.setCategory(category);


//        productDao.save(product);
        return new ModelAndView(("redirect:/list-products"));
    }

    @GetMapping("/list-products")
    public ModelAndView listProducts() {
        ModelAndView modelAndView = new ModelAndView("products");
//      modelAndView.addObject("products", productDao.findAll());
        return modelAndView;
    }


//    public Iterable<Product> products() {
    @GetMapping("/products")
    @ResponseBody
    public String products() {
//        return productDao.findAll();
        return "Toate produsele";
    }

    @GetMapping("/products/{id}")
    @ResponseBody
    public Product products(@PathVariable("id") int id) {
//        return productDao.findById(id).get();
        Product product = new Product();
        product.setName("Produsul meu");
        return product;
    }
}
