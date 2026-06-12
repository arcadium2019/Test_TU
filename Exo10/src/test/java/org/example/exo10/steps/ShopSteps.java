package org.example.exo10.steps;

import io.cucumber.java.Before;
import io.cucumber.java.en.*;
import org.example.exo10.model.Order;
import org.example.exo10.model.Product;
import org.example.exo10.model.User;
import org.example.exo10.repository.OrderRepository;
import org.example.exo10.repository.ProductRepository;
import org.example.exo10.repository.UserRepository;
import org.example.exo10.service.OrderService;
import org.example.exo10.service.ProductService;
import org.example.exo10.service.UserService;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ShopSteps {

    private UserRepository userRepository;
    private ProductRepository productRepository;
    private OrderRepository orderRepository;

    private UserService userService;
    private ProductService productService;
    private OrderService orderService;

    private final List<User> registeredUsers = new ArrayList<>();
    private final List<Product> registeredProducts = new ArrayList<>();
    private final Map<String, Order> registeredOrders = new HashMap<>();
    private final Map<String, Product> productsByName = new HashMap<>();

    private User lastUser;
    private List<Product> lastResults;
    private String lastOrderId;
    private Exception exception;

    @Before
    public void setUp() {
        userRepository = mock(UserRepository.class);
        productRepository = mock(ProductRepository.class);
        orderRepository = mock(OrderRepository.class);

        userService = new UserService(userRepository);
        productService = new ProductService(productRepository);
        orderService = new OrderService(orderRepository);

        registeredUsers.clear();
        registeredProducts.clear();
        registeredOrders.clear();
        productsByName.clear();
        lastUser = null;
        lastResults = null;
        lastOrderId = null;
        exception = null;

        when(userRepository.findByUsername(anyString())).thenAnswer(inv -> {
            String username = inv.getArgument(0);
            return registeredUsers.stream().filter(u -> u.getUsername().equals(username)).findFirst();
        });
        doAnswer(inv -> { registeredUsers.add(inv.getArgument(0)); return null; })
                .when(userRepository).save(any());

        when(productRepository.findByKeyword(anyString())).thenAnswer(inv -> {
            String kw = inv.getArgument(0).toString().toLowerCase();
            return registeredProducts.stream()
                    .filter(p -> p.getName().toLowerCase().contains(kw))
                    .collect(Collectors.toList());
        });
        when(productRepository.findByCategory(anyString())).thenAnswer(inv -> {
            String cat = inv.getArgument(0);
            return registeredProducts.stream()
                    .filter(p -> p.getCategory().equals(cat))
                    .collect(Collectors.toList());
        });
        when(productRepository.findByMaxPrice(anyDouble())).thenAnswer(inv -> {
            double max = (double) inv.getArgument(0);
            return registeredProducts.stream()
                    .filter(p -> p.getPrice() <= max)
                    .collect(Collectors.toList());
        });

        when(orderRepository.findById(anyString())).thenAnswer(inv ->
                Optional.ofNullable(registeredOrders.get(inv.getArgument(0))));
        doAnswer(inv -> { Order o = inv.getArgument(0); registeredOrders.put(o.getId(), o); return null; })
                .when(orderRepository).save(any());
    }

    // ---- User steps ----

    @Given("a user with username {string} already exists")
    public void aUserAlreadyExists(String username) {
        registeredUsers.add(new User(username, username + "@test.com", "somepass"));
    }

    @Given("a user with username {string} and password {string} exists")
    public void aUserWithPasswordExists(String username, String password) {
        registeredUsers.add(new User(username, username + "@test.com", password));
    }

    @When("the user registers with username {string}, email {string} and password {string}")
    public void theUserRegisters(String username, String email, String password) {
        try {
            lastUser = userService.createAccount(username, email, password);
        } catch (Exception e) {
            exception = e;
        }
    }

    @Then("the account is created successfully")
    public void theAccountIsCreatedSuccessfully() {
        assertNull(exception);
        assertNotNull(lastUser);
    }

    @When("the user logs in with username {string} and password {string}")
    public void theUserLogsIn(String username, String password) {
        try {
            lastUser = userService.login(username, password);
        } catch (Exception e) {
            exception = e;
        }
    }

    @Then("the login succeeds")
    public void theLoginSucceeds() {
        assertNull(exception);
        assertNotNull(lastUser);
    }

    // ---- Product steps ----

    @Given("a product named {string} in category {string} at price {double}")
    public void aProductNamed(String name, String category, double price) {
        Product product = new Product(name, price, category);
        registeredProducts.add(product);
        productsByName.put(name, product);
    }

    @When("the user searches for {string}")
    public void theUserSearchesFor(String keyword) {
        lastResults = productService.searchByKeyword(keyword);
    }

    @When("the user searches with a maximum price of {double}")
    public void theUserSearchesWithMaxPrice(double maxPrice) {
        lastResults = productService.searchByMaxPrice(maxPrice);
    }

    @When("the user browses the category {string}")
    public void theUserBrowsesCategory(String category) {
        lastResults = productService.browseByCategory(category);
    }

    @Then("the results contain {string}")
    public void theResultsContain(String name) {
        assertTrue(lastResults.stream().anyMatch(p -> p.getName().equals(name)));
    }

    @Then("the results do not contain {string}")
    public void theResultsDoNotContain(String name) {
        assertTrue(lastResults.stream().noneMatch(p -> p.getName().equals(name)));
    }

    // ---- Order steps ----

    @Given("an order with id {string} exists")
    public void anOrderExists(String orderId) {
        registeredOrders.put(orderId, new Order(orderId));
    }

    @Given("an order with id {string} exists with {string} in quantity {int}")
    public void anOrderExistsWithProduct(String orderId, String productName, int quantity) {
        Order order = new Order(orderId);
        Product placeholder = new Product(productName, 0.0, "");
        for (int i = 0; i < quantity; i++) {
            order.addProduct(placeholder);
        }
        registeredOrders.put(orderId, order);
    }

    @Given("no order with id {string} exists")
    public void noOrderExists(String orderId) {
        // registeredOrders has no entry for this id — mock returns Optional.empty()
    }

    @When("the user adds {string} to order {string}")
    public void theUserAddsProduct(String productName, String orderId) {
        lastOrderId = orderId;
        try {
            orderService.addProduct(orderId, productsByName.get(productName));
        } catch (Exception e) {
            exception = e;
        }
    }

    @When("the user removes {string} from order {string}")
    public void theUserRemovesProduct(String productName, String orderId) {
        lastOrderId = orderId;
        try {
            orderService.removeProduct(orderId, productsByName.get(productName));
        } catch (Exception e) {
            exception = e;
        }
    }

    @When("the user validates order {string}")
    public void theUserValidatesOrder(String orderId) {
        lastOrderId = orderId;
        try {
            orderService.validateOrder(orderId);
        } catch (Exception e) {
            exception = e;
        }
    }

    @Then("the product is confirmed as added to the order")
    public void theProductIsConfirmedAdded() {
        assertNull(exception);
    }

    @Then("the quantity of {string} in the order is {int}")
    public void theQuantityInOrderIs(String productName, int expectedQty) {
        assertEquals(expectedQty, registeredOrders.get(lastOrderId).getQuantity(productName));
    }

    @Then("{string} is no longer in the order")
    public void productIsNoLongerInOrder(String productName) {
        assertFalse(registeredOrders.get(lastOrderId).containsProduct(new Product(productName, 0.0, "")));
    }

    @Then("the order is confirmed")
    public void theOrderIsConfirmed() {
        assertNull(exception);
        assertTrue(registeredOrders.get(lastOrderId).isValidated());
    }

    @Then("an error is returned with message {string}")
    public void anErrorIsReturnedWithMessage(String message) {
        assertNotNull(exception);
        assertTrue(exception.getMessage().contains(message));
    }
}
