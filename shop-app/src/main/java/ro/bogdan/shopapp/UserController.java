package ro.bogdan.shopapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class UserController {
//     prin aceasta adnotare noi specificam sa ni se dea implementare. Nu scriem noi aceast implementare si Spring-Data-JPA genereaza
    @Autowired // folosim o interfata si folosim contractul stabilit de acea interfata
    private UserDao userDao; // programam dupa o interfata, in cadrul interfetei poate se ascunde o implementare

    @GetMapping("")
    public ModelAndView firstPage() {
        return new ModelAndView("login.html");
    }

    @GetMapping("/register")
    public ModelAndView registerPage() {
        return new ModelAndView("register.html");
    }

    @PostMapping("/register-action")
    public ModelAndView registerAction(@RequestParam("email") String email,
                                       @RequestParam("password") String password,
                                       @RequestParam("name") String name) {
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.setName(name);

        userDao.save(user);

        return new ModelAndView(("redirect:/"));
    }

    @PostMapping("/login-action")
    public ModelAndView loginAction(@RequestParam("email") String email,
                                    @RequestParam("password") String password) {
        User userByEmail = new User(); //asta e pusa de mn
        userByEmail = userDao.findByEmail(email);
        if (userByEmail.getPassword().equals(password)) {
            return new ModelAndView(("redirect:/dashboard"));
        } else {
            return new ModelAndView(("redirect:/"));
        }
    }

    @PostMapping("/buy")
    public ModelAndView buy(@RequestParam("id") Integer id) {
        // trebuie sa comunicam cu aplicatia responsabila cu cosul de cumparaturi

        // 1 - sa aflam cosul de cumparaturi pentru user-ul nostru
        RestTemplate restTemplate = new RestTemplate();
        Cart cart = new Cart();
        cart.setUserId(1);
        cart = restTemplate.postForObject("http://localhost:8082/carts", cart, Cart.class);

        // 2 - trebuie sa adaug noul produs in cosul de cumparaturi
        Product product = restTemplate.getForObject("http://localhost:8081/prodcuts/" + id, Product.class);
        CartProduct cartProduct = new CartProduct();
        cartProduct.setDescription(product.getDescription());
        cartProduct.setName(product.getName());
        cartProduct.setPrice(product.getPrice());
        restTemplate.postForObject("http://localhost:8082/carts/" + cart.getUserId() + "/products", product, Cart.class);
        return new ModelAndView(("redirect:/dashboard"));
    }

    @GetMapping("/dashboard")
    public ModelAndView dashboard() {

        ModelAndView modelAndView = new ModelAndView("dashboard.html");

        RestTemplate restTemplate = new RestTemplate();
        try {
            Cart cart = new Cart();
            cart.setUserId(1);
            cart = restTemplate.postForObject("http://localhost:8082/carts", cart, Cart.class);
            int cartSize = cart.getCartProducts().size();
            modelAndView.addObject("cartSize", cartSize);
        } catch (Exception e) {
            modelAndView.addObject("cartSize", 0);
        }

        try {
//            String allProducts = restTemplate.getForObject("http://localhost:8081/products", String.class);
        ResponseEntity<Product[]> responseEntity = restTemplate.getForEntity("http://localhost:8081/products", Product[].class);
        Product[] allProducts2 = responseEntity.getBody();
        System.out.println(allProducts2[0].getName());
        System.out.println(allProducts2);


        modelAndView.addObject("products", allProducts2);
        } catch (Exception e) {
        modelAndView.addObject("products", new Product[0]);
        }
        return modelAndView;
    }
}
