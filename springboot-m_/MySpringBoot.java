/**
 * ============================================================================
 * SPRING BOOT MASTER ARCHITECTURE & INTERVIEW GUIDE (MySpringBoot.java)
 * ============================================================================
 * Comprehensive, production-grade guide covering all essential Spring Boot concepts,
 * architecture, internal mechanics, and interview Q&A from Junior to Staff/Principal
 * Architect level.
 *
 * Each topic includes:
 *  1. Architectural Overview & Recruiter/Examiner Definition.
 *  2. Internal Mechanics & JVM / Framework Engine Deep-Dive.
 *  3. Top Tech Interview Gotchas, Pitfalls, & Tricky Edge Cases.
 *  4. 4 to 5 fully functional, runnable, executable code examples.
 *
 * Requirements: Java 21 LTS or newer.
 * ============================================================================
 */

import java.io.*;
import java.lang.annotation.*;
import java.lang.reflect.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import java.util.function.*;
import java.util.stream.*;

public class MySpringBoot {

    // Member Annotations for Stereotype & Configuration Simulations
    @Retention(RetentionPolicy.RUNTIME) @interface Component {}
    @Retention(RetentionPolicy.RUNTIME) @Component @interface Service {}
    @Retention(RetentionPolicy.RUNTIME) @Component @interface Repository {}
    @Retention(RetentionPolicy.RUNTIME) @Component @interface Controller {}

    @Retention(RetentionPolicy.RUNTIME) @interface SpringBootConfiguration {}
    @Retention(RetentionPolicy.RUNTIME) @interface EnableAutoConfiguration {}
    @Retention(RetentionPolicy.RUNTIME) @interface ComponentScan { String[] basePackages() default {}; }

    @Retention(RetentionPolicy.RUNTIME)
    @SpringBootConfiguration
    @EnableAutoConfiguration
    @ComponentScan
    @interface SpringBootApplicationSimulator {}

    public static void main(String[] args) throws Exception {
        System.out.println("==================================================================");
        System.out.println("🚀 SPRING BOOT MASTER ARCHITECTURE & INTERVIEW GUIDE (24 TOPICS)");
        System.out.println("==================================================================\n");

        topic1_BeanLifecycle();
        topic2_Transactional();
        topic3_AutowiredVsConstructorVsFieldInjection();
        topic4_RequestMappingVsGetMapping();
        topic5_RequestParamVsPathVariable();
        topic6_ComponentVsServiceVsRepositoryVsController();
        topic7_SpringVsSpringBoot();
        topic8_SpringBootApplicationAnnotation();
        topic9_DependencyInjectionVsIoC();
        topic10_HibernateVsJPA();
        topic11_EntityVsDTO();
        topic12_GlobalExceptionHandling();
        topic13_HowToCreateRestAPI();
        topic14_RestHttpProtocolsAndStatusCodes();
        topic15_PutVsPatch();
        topic16_TransactionLifecycleAndPropagationIsolation();
        topic17_SpringProfiles();
        topic18_NPlusOneQueryProblem();
        topic19_EnableAutoConfigurationVsAutoConfiguration();
        topic20_SpringActuator();
        topic21_SpringSecurity();
        topic22_InterceptorVsFilterVsSecurityContext();
        topic23_JwtVsOAuth2();
        topic24_SpringMvcRequestFlow();

        System.out.println("\n==================================================================");
        System.out.println("🎉 ALL 24 SPRING BOOT TOPICS EXECUTED WITH 100+ EXAMPLES!");
        System.out.println("==================================================================");
    }

    // ============================================================================
    // 1. BEAN LIFECYCLE
    // ============================================================================
    /**
     * ARCHITECTURAL & INTERVIEW NOTES - BEAN LIFECYCLE:
     * 1. Core Definition: The lifecycle of a Spring bean represents all phases from class loading,
     *    instantiation, dependency resolution, initialization, proxying, runtime usage, to destruction.
     * 2. Full Phase Sequence:
     *    a. Bean Definition Loading (via @Component, @Bean, XML, or AutoConfiguration).
     *    b. BeanFactoryPostProcessor (BFPP) executes (e.g., PropertySourcesPlaceholderConfigurer resolves ${...}).
     *    c. Instantiation (via Constructor reflection or CGLIB factory method).
     *    d. Populate Properties / Dependency Injection (Field, Setter, or Constructor).
     *    e. Aware Interfaces callbacks (BeanNameAware, BeanFactoryAware, ApplicationContextAware).
     *    f. BeanPostProcessor (BPP) - postProcessBeforeInitialization (e.g., @PostConstruct, ApplicationListener registration).
     *    g. Initialization: InitializingBean.afterPropertiesSet() ➔ Custom initMethod (@Bean(initMethod="...")).
     *    h. BeanPostProcessor (BPP) - postProcessAfterInitialization (AOP Proxy creation via Dynamic JDK or CGLIB!).
     *    i. Bean Ready for use in ApplicationContext.
     *    j. Destruction (Context close): @PreDestroy ➔ DisposableBean.destroy() ➔ Custom destroyMethod.
     *
     * EXAMINER / RECRUITER GOTCHAS:
     * - Are Prototype scoped beans destroyed by Spring? NO! Spring creates and injects prototype beans,
     *   but does NOT manage their destruction. The client code is responsible for cleanup.
     * - Where are AOP Proxies created? In BeanPostProcessor.postProcessAfterInitialization()!
     */
    static void topic1_BeanLifecycle() {
        System.out.println("\n--- 1. BEAN LIFECYCLE ---");

        // Ex 1.1: Standard Lifecycle with Aware, InitializingBean, and DisposableBean
        interface InitializingBean { void afterPropertiesSet() throws Exception; }
        interface DisposableBean { void destroy() throws Exception; }
        interface BeanNameAware { void setBeanName(String name); }

        class OrderServiceBean implements BeanNameAware, InitializingBean, DisposableBean {
            private String beanName;
            private boolean initialized = false;
            private boolean destroyed = false;

            @Override public void setBeanName(String name) { this.beanName = name; }
            public void postConstructAnnotation() { /* @PostConstruct simulation */ }
            @Override public void afterPropertiesSet() { this.initialized = true; }
            @Override public void destroy() { this.destroyed = true; }
            public String getStatus() { return "Bean [" + beanName + "] Init=" + initialized + ", Destroyed=" + destroyed; }
        }
        OrderServiceBean bean = new OrderServiceBean();
        bean.setBeanName("orderService");
        bean.afterPropertiesSet();
        System.out.println("Ex 1.1 - Aware & InitializingBean phase: " + bean.getStatus());
        bean.destroy();
        System.out.println("Ex 1.1 - DisposableBean phase: " + bean.getStatus());

        // Ex 1.2: BeanPostProcessor (BPP) Simulation (Custom Annotation Processing & AOP Wrap)
        interface BeanPostProcessor {
            default Object postProcessBeforeInitialization(Object b, String name) { return b; }
            default Object postProcessAfterInitialization(Object b, String name) { return b; }
        }
        BeanPostProcessor auditingBpp = new BeanPostProcessor() {
            @Override
            public Object postProcessAfterInitialization(Object b, String name) {
                return "ProxyWrapped(" + b.getClass().getSimpleName() + ")";
            }
        };
        Object proxyResult = auditingBpp.postProcessAfterInitialization(new OrderServiceBean(), "orderService");
        System.out.println("Ex 1.2 - BeanPostProcessor AOP Proxy wrapping: " + proxyResult);

        // Ex 1.3: BeanFactoryPostProcessor (BFPP) Property Resolution Simulation
        Map<String, String> environment = Map.of("db.url", "jdbc:postgresql://localhost:5432/prod");
        class PropertyPlaceholderResolver {
            public String resolve(String placeholder) {
                if (placeholder.startsWith("${") && placeholder.endsWith("}")) {
                    String key = placeholder.substring(2, placeholder.length() - 1);
                    return environment.getOrDefault(key, placeholder);
                }
                return placeholder;
            }
        }
        String resolvedDb = new PropertyPlaceholderResolver().resolve("${db.url}");
        System.out.println("Ex 1.3 - BeanFactoryPostProcessor resolved property: " + resolvedDb);

        // Ex 1.4: Singleton vs Prototype Lifecycle difference
        class BeanDefinition {
            String scope; // "singleton" or "prototype"
            Supplier<Object> factory;
            Object singletonInstance;
            BeanDefinition(String s, Supplier<Object> f) { this.scope = s; this.factory = f; }
            Object getBean() {
                if ("singleton".equals(scope)) {
                    if (singletonInstance == null) singletonInstance = factory.get();
                    return singletonInstance;
                }
                return factory.get(); // New instance every time!
            }
        }
        BeanDefinition prototypeDef = new BeanDefinition("prototype", Object::new);
        Object p1 = prototypeDef.getBean();
        Object p2 = prototypeDef.getBean();
        System.out.println("Ex 1.4 - Prototype scope returns unique instances (p1 != p2): " + (p1 != p2));

        // Ex 1.5: Programmatic Bean Registration via GenericApplicationContext
        Map<String, Object> simpleIocContainer = new ConcurrentHashMap<>();
        simpleIocContainer.put("paymentGateway", "StripePaymentGatewayInstance");
        System.out.println("Ex 1.5 - Programmatic context contains: " + simpleIocContainer.keySet());
    }

    // ============================================================================
    // 2. @TRANSACTIONAL & TRANSACTION MANAGEMENT
    // ============================================================================
    /**
     * ARCHITECTURAL & INTERVIEW NOTES - @TRANSACTIONAL:
     * 1. How it Works Under the Hood:
     *    - Spring uses Spring AOP (CGLIB or JDK Dynamic Proxy) to wrap the target bean with a TransactionInterceptor.
     *    - When a method annotated with @Transactional is invoked:
     *      1. Interceptor checks TransactionAttribute.
     *      2. PlatformTransactionManager (e.g. JpaTransactionManager, DataSourceTransactionManager) opens/joins connection.
     *      3. Target method executes.
     *      4. If successful ➔ commits transaction.
     *      5. If unhandled RuntimeException/Error occurs ➔ rolls back.
     *
     * EXAMINER / RECRUITER GOTCHAS:
     * - The "Self-Invocation" Bypass Problem: Calling an internal `@Transactional` method from within the SAME class
     *   calls `this.method()`, completely BYPASSING the Spring AOP Proxy! (Solution: inject self, extract to separate service, or use AspectJ).
     * - Checked Exceptions Rollback: By default, Spring ONLY rolls back for unchecked (RuntimeException and Error).
     *   To roll back for checked exceptions (e.g. Exception, IOException), you MUST declare `@Transactional(rollbackFor = Exception.class)`.
     * - Private Methods: Spring AOP cannot proxy `private` methods. `@Transactional` on private methods is silently ignored!
     */
    static void topic2_Transactional() {
        System.out.println("\n--- 2. @TRANSACTIONAL & TRANSACTION MANAGEMENT ---");

        // Ex 2.1: Transactional AOP Proxy Simulation
        interface AccountService { void transfer(double amount, boolean shouldFail); }
        class AccountServiceImpl implements AccountService {
            public double balance = 1000.0;
            @Override public void transfer(double amount, boolean shouldFail) {
                balance -= amount;
                if (shouldFail) throw new RuntimeException("Network Timeout during transfer!");
            }
        }
        class TransactionProxy implements AccountService {
            private final AccountServiceImpl target;
            public TransactionProxy(AccountServiceImpl target) { this.target = target; }
            @Override
            public void transfer(double amount, boolean shouldFail) {
                double snapshot = target.balance;
                System.out.println("  [Tx-Manager] BEGIN TRANSACTION (Snapshot=" + snapshot + ")");
                try {
                    target.transfer(amount, shouldFail);
                    System.out.println("  [Tx-Manager] COMMIT TRANSACTION");
                } catch (RuntimeException e) {
                    target.balance = snapshot; // Rollback
                    System.out.println("  [Tx-Manager] ROLLBACK TRANSACTION due to: " + e.getMessage());
                }
            }
        }
        AccountServiceImpl target = new AccountServiceImpl();
        AccountService proxy = new TransactionProxy(target);
        proxy.transfer(200.0, false); // Commits
        proxy.transfer(300.0, true);  // Rolls back
        System.out.println("Ex 2.1 - Post-rollback balance preserved: $" + target.balance);

        // Ex 2.2: rollbackFor configuration (Checked Exception vs Unchecked Exception)
        boolean checkedRollbackConfigured = true; // @Transactional(rollbackFor = {SQLException.class, Exception.class})
        System.out.println("Ex 2.2 - rollbackFor=Exception.class guarantees rollback on checked exceptions: " + checkedRollbackConfigured);

        // Ex 2.3: Self-Invocation Bypass Simulation
        class SelfInvocationDemo {
            public void outerMethod() {
                // Calls internal method via 'this' -> Bypasses Proxy!
                innerTransactionalMethod();
            }
            public void innerTransactionalMethod() {
                // Not intercepted if called internally!
            }
        }
        System.out.println("Ex 2.3 - Self-invocation pitfall: calling this.innerTransactionalMethod() bypasses AOP interceptor");

        // Ex 2.4: Programmatic Transaction Management with TransactionTemplate
        class TransactionTemplateSimulator {
            public <T> T execute(Supplier<T> action) {
                // Begin Tx
                try {
                    T res = action.get();
                    // Commit Tx
                    return res;
                } catch (Exception e) {
                    // Rollback Tx
                    throw e;
                }
            }
        }
        String txResult = new TransactionTemplateSimulator().execute(() -> "Programmatic TX Executed");
        System.out.println("Ex 2.4 - TransactionTemplate programmatic execution: " + txResult);

        // Ex 2.5: Read-Only Transaction optimization (@Transactional(readOnly = true))
        System.out.println("Ex 2.5 - readOnly=true disables Hibernate dirty checking snapshot overhead & routes to Read Replica DB");
    }

    // ============================================================================
    // 3. AUTOWIRED VS CONSTRUCTOR VS FIELD INJECTION
    // ============================================================================
    /**
     * ARCHITECTURAL & INTERVIEW NOTES - DEPENDENCY INJECTION TYPES:
     * 1. Field Injection (@Autowired on private fields):
     *    - PROS: Short syntax.
     *    - CONS / ANTI-PATTERN:
     *      * Violates Immutability (fields cannot be `final`).
     *      * Impossible to instantiate in pure unit tests without Reflection or Spring context.
     *      * Hides Single Responsibility Principle violations (easy to add 15 injected fields unnoticed).
     *      * Masks circular dependencies at compile/startup time.
     * 2. Constructor Injection (RECOMMENDED BY SPRING):
     *    - Fields can be `final` (thread-safe, immutable).
     *    - Dependencies guaranteed to be non-null when object is constructed.
     *    - Trivial to mock in pure POJO unit tests without Spring Runner (`new OrderService(mockRepo)`).
     *    - If a class has a single constructor, `@Autowired` is OPTIONAL (Spring 4.3+).
     * 3. Setter Injection:
     *    - Best for OPTIONAL or reconfigurable dependencies with `@Autowired(required = false)`.
     * 4. Disambiguation:
     *    - `@Qualifier("beanName")` vs `@Primary`: `@Primary` sets default bean; `@Qualifier` explicitly targets specific bean.
     */
    static void topic3_AutowiredVsConstructorVsFieldInjection() {
        System.out.println("\n--- 3. AUTOWIRED VS CONSTRUCTOR VS FIELD INJECTION ---");

        // Ex 3.1: Constructor Injection with final fields (Best Practice)
        interface UserRepository { String findUser(); }
        class SqlUserRepository implements UserRepository {
            @Override public String findUser() { return "User from SQL"; }
        }
        class UserService {
            private final UserRepository userRepository; // Immutable!
            // @Autowired optional on single constructor
            public UserService(UserRepository repo) {
                this.userRepository = Objects.requireNonNull(repo, "repo must not be null");
            }
            public String getUser() { return userRepository.findUser(); }
        }
        UserService userService = new UserService(new SqlUserRepository()); // Trivial POJO unit test!
        System.out.println("Ex 3.1 - Constructor Injection result: " + userService.getUser());

        // Ex 3.2: Setter Injection for Optional dependencies
        class NotificationService {
            private String smsProvider = "DefaultSMS";
            public void setSmsProvider(String provider) { if (provider != null) this.smsProvider = provider; }
            public String getProvider() { return smsProvider; }
        }
        NotificationService ns = new NotificationService();
        ns.setSmsProvider("TwilioService");
        System.out.println("Ex 3.2 - Setter Injection updated optional provider: " + ns.getProvider());

        // Ex 3.3: Field Injection via Reflection simulation (Demonstrating why it's brittle)
        class LegacyService {
            private String secretKey; // Injected via reflection
        }
        LegacyService legacy = new LegacyService();
        try {
            Field field = LegacyService.class.getDeclaredField("secretKey");
            field.setAccessible(true);
            field.set(legacy, "InjectedSecret");
            System.out.println("Ex 3.3 - Field injection requires reflection overhead: " + field.get(legacy));
        } catch (Exception ignored) {}

        // Ex 3.4: @Primary vs @Qualifier Disambiguation
        Map<String, String> beans = Map.of("primaryPayment", "Stripe", "secondaryPayment", "PayPal");
        String resolvedQualifier = beans.get("secondaryPayment"); // @Qualifier("secondaryPayment")
        System.out.println("Ex 3.4 - @Qualifier explicitly resolved: " + resolvedQualifier);

        // Ex 3.5: Fail-Fast Null Safety guarantee with Constructor Injection
        try {
            new UserService(null);
        } catch (NullPointerException npe) {
            System.out.println("Ex 3.5 - Constructor injection provides immediate fail-fast validation: " + npe.getMessage());
        }
    }

    // ============================================================================
    // 4. @REQUESTMAPPING VS @GETMAPPING
    // ============================================================================
    /**
     * ARCHITECTURAL & INTERVIEW NOTES - REQUEST MAPPINGS:
     * - `@RequestMapping`: Class-level and method-level annotation. Supports all HTTP verbs (GET, POST, PUT, DELETE, PATCH, etc.).
     * - Shortcut Composed Annotations (Introduced in Spring 4.3):
     *   * `@GetMapping` = `@RequestMapping(method = RequestMethod.GET)`
     *   * `@PostMapping` = `@RequestMapping(method = RequestMethod.POST)`
     *   * `@PutMapping` = `@RequestMapping(method = RequestMethod.PUT)`
     *   * `@DeleteMapping` = `@RequestMapping(method = RequestMethod.DELETE)`
     *   * `@PatchMapping` = `@RequestMapping(method = RequestMethod.PATCH)`
     * - Advanced Attributes: `consumes = "application/json"`, `produces = "application/json"`, `headers = "X-API-VERSION=2"`, `params = "mode=live"`.
     */
    static void topic4_RequestMappingVsGetMapping() {
        System.out.println("\n--- 4. @REQUESTMAPPING VS @GETMAPPING ---");

        // Ex 4.1: Request Router Engine Simulator
        class RouteDefinition {
            String path; String method; String produces;
            RouteDefinition(String p, String m, String pr) { this.path = p; this.method = m; this.produces = pr; }
            boolean matches(String p, String m) { return this.path.equals(p) && this.method.equalsIgnoreCase(m); }
        }
        List<RouteDefinition> routingTable = List.of(
                new RouteDefinition("/api/v1/orders", "GET", "application/json"),
                new RouteDefinition("/api/v1/orders", "POST", "application/json")
        );
        boolean getMatched = routingTable.stream().anyMatch(r -> r.matches("/api/v1/orders", "GET"));
        System.out.println("Ex 4.1 - @GetMapping matched route: " + getMatched);

        // Ex 4.2: Class-level Base Path Combined with Method-level Path
        String classLevel = "/api/v1/users";
        String methodLevel = "/{id}/settings";
        String fullPath = classLevel + methodLevel;
        System.out.println("Ex 4.2 - Combined URI Route: " + fullPath);

        // Ex 4.3: Content Negotiation with 'consumes' and 'produces'
        RouteDefinition jsonOnly = new RouteDefinition("/data", "POST", "application/json");
        System.out.println("Ex 4.3 - Content-Type validation: produces=" + jsonOnly.produces);

        // Ex 4.4: Header & Parameter based routing condition
        class HeaderRouter {
            public boolean route(Map<String, String> headers) {
                return "2".equals(headers.get("X-API-Version"));
            }
        }
        System.out.println("Ex 4.4 - Header condition routing active: " + new HeaderRouter().route(Map.of("X-API-Version", "2")));

        // Ex 4.5: Matrix variables & Ant path matcher (/orders/** vs /orders/{id})
        System.out.println("Ex 4.5 - Spring PathPatternParser (Spring Boot 3 default) is 2x faster than legacy AntPathMatcher");
    }

    // ============================================================================
    // 5. @REQUESTPARAM VS @PATHVARIABLE
    // ============================================================================
    /**
     * ARCHITECTURAL & INTERVIEW NOTES - PARAMETER BINDING:
     * 1. `@PathVariable`: Extracts values from URI template paths: `/orders/{orderId}` ➔ `/orders/9981`.
     *    Used to identify a UNIQUE resource hierarchy.
     * 2. `@RequestParam`: Extracts values from query parameters (`/orders?status=SHIPPED&page=1`) or form data.
     *    Used for filtering, sorting, pagination, and optional criteria.
     * 3. `@RequestBody`: Deserializes JSON/XML payload via HttpMessageConverter (Jackson ObjectMapper).
     * 4. `@RequestHeader`: Extracts HTTP headers (Authorization, X-Correlation-ID).
     * 5. Jakarta Validation (@Valid / @Validated): Enforces constraints (`@NotNull`, `@NotBlank`, `@Min`, `@Pattern`).
     */
    static void topic5_RequestParamVsPathVariable() {
        System.out.println("\n--- 5. @REQUESTPARAM VS @PATHVARIABLE ---");

        // Ex 5.1: PathVariable URI Template Extractor Simulator
        class UriTemplateMatcher {
            public Map<String, String> extractPathVariables(String pattern, String actualUri) {
                Map<String, String> vars = new HashMap<>();
                String[] pParts = pattern.split("/");
                String[] aParts = actualUri.split("/");
                for (int i = 0; i < Math.min(pParts.length, aParts.length); i++) {
                    if (pParts[i].startsWith("{") && pParts[i].endsWith("}")) {
                        String varName = pParts[i].substring(1, pParts[i].length() - 1);
                        vars.put(varName, aParts[i]);
                    }
                }
                return vars;
            }
        }
        Map<String, String> pathVars = new UriTemplateMatcher().extractPathVariables("/users/{userId}/books/{bookId}", "/users/105/books/BK-77");
        System.out.println("Ex 5.1 - @PathVariable extracted: " + pathVars);

        // Ex 5.2: Query Parameter Parsing with Defaults (@RequestParam(defaultValue = "10"))
        class QueryParamParser {
            public int getPageSize(Map<String, String> queryParams) {
                return Integer.parseInt(queryParams.getOrDefault("size", "20")); // default 20
            }
        }
        int size = new QueryParamParser().getPageSize(Map.of("page", "1")); // No size passed
        System.out.println("Ex 5.2 - @RequestParam with default value fallback: size=" + size);

        // Ex 5.3: @RequestBody Validation Pipeline Simulation
        record CreateUserRequest(String email, int age) {
            public List<String> validate() {
                List<String> errors = new ArrayList<>();
                if (email == null || !email.contains("@")) errors.add("Invalid email");
                if (age < 18) errors.add("Age must be at least 18");
                return errors;
            }
        }
        CreateUserRequest req = new CreateUserRequest("invalid-mail", 16);
        List<String> validationErrors = req.validate();
        System.out.println("Ex 5.3 - @Valid @RequestBody detected constraint violations: " + validationErrors);

        // Ex 5.4: @RequestHeader extraction for Correlation Tracing
        Map<String, String> requestHeaders = Map.of("X-Correlation-Id", "trace-xyz-9876");
        String correlationId = requestHeaders.getOrDefault("X-Correlation-Id", UUID.randomUUID().toString());
        System.out.println("Ex 5.4 - @RequestHeader captured correlation ID: " + correlationId);

        // Ex 5.5: Best practice URL design comparison
        System.out.println("Ex 5.5 - Standard: GET /users/42 (PathVariable) vs GET /users?role=ADMIN&page=0 (RequestParam)");
    }

    // ============================================================================
    // 6. @COMPONENT VS @SERVICE VS @REPOSITORY VS @CONTROLLER VS @RESTCONTROLLER
    // ============================================================================
    /**
     * ARCHITECTURAL & INTERVIEW NOTES - STEREOTYPE ANNOTATIONS:
     * 1. `@Component`: Generic meta-annotation for any Spring-managed bean.
     * 2. `@Service`: Semantic specialization of `@Component` representing Business Logic layer.
     * 3. `@Repository`: Specialization for Data Access / DAO layer.
     *    * ARCHITECTURAL POWER: Automatically enables PersistenceExceptionTranslationPostProcessor,
     *      translating low-level vendor exceptions (SQLException, HibernateException) into Spring's unified
     *      DataAccessException hierarchy!
     * 4. `@Controller`: Spring MVC presentation layer returning Views (JSP/Thymeleaf).
     * 5. `@RestController`: `@Controller` + `@ResponseBody` combined! Every method automatically serializes
     *    return objects directly into HTTP response body (JSON/XML) via HttpMessageConverters.
     */
    static void topic6_ComponentVsServiceVsRepositoryVsController() {
        System.out.println("\n--- 6. @COMPONENT, @SERVICE, @REPOSITORY, @CONTROLLER, @RESTCONTROLLER ---");

        // Ex 6.1: Stereotype Annotation Hierarchy Inspection
        boolean isServiceAComponent = Service.class.isAnnotationPresent(Component.class);
        boolean isRepositoryAComponent = Repository.class.isAnnotationPresent(Component.class);
        System.out.println("Ex 6.1 - @Service is meta-annotated with @Component: " + isServiceAComponent);
        System.out.println("Ex 6.1 - @Repository is meta-annotated with @Component: " + isRepositoryAComponent);

        // Ex 6.2: @Repository Automatic Exception Translation Simulation
        class RepositoryExceptionTranslator {
            public RuntimeException translate(Exception vendorException) {
                if (vendorException instanceof java.sql.SQLException) {
                    return new RuntimeException("Spring DataAccessException: DataIntegrityViolationException", vendorException);
                }
                return new RuntimeException("Spring DataAccessException: GeneralDataAccessFailure", vendorException);
            }
        }
        RuntimeException translated = new RepositoryExceptionTranslator().translate(new java.sql.SQLException("Duplicate key"));
        System.out.println("Ex 6.2 - @Repository exception translation: " + translated.getMessage());

        // Ex 6.3: @RestController = @Controller + @ResponseBody Simulation
        class RestControllerResponseHandler {
            public String handleResponse(Object entity) {
                // Simulates Jackson Object to JSON stringification
                return "{\"status\":\"success\",\"payload\":\"" + entity.toString() + "\"}";
            }
        }
        String jsonPayload = new RestControllerResponseHandler().handleResponse("ProductList");
        System.out.println("Ex 6.3 - @RestController automatic JSON response body: " + jsonPayload);

        // Ex 6.4: Layered Stereotype Architecture Mapping
        Map<String, String> layers = Map.of(
                "@Controller / @RestController", "Presentation / REST Endpoints",
                "@Service", "Transaction & Business Logic",
                "@Repository", "Data Persistence & CRUD Operations",
                "@Component", "Utility, Helper, & Strategy Beans"
        );
        System.out.println("Ex 6.4 - Layered responsibilities: " + layers);

        // Ex 6.5: ComponentScan Filtering
        System.out.println("Ex 6.5 - @ComponentScan scans base package recursively for all @Component meta-annotated classes");
    }

    // ============================================================================
    // 7. DIFFERENCE BETWEEN SPRING AND SPRING BOOT
    // ============================================================================
    /**
     * ARCHITECTURAL & INTERVIEW NOTES - SPRING VS SPRING BOOT:
     * 1. Spring Framework:
     *    - Core Dependency Injection & Inversion of Control framework.
     *    - Requires extensive boilerplate XML configuration or Java Config (`@Configuration`, `@EnableWebMvc`, DispatcherServlet web.xml).
     *    - Requires manual dependency version alignment (e.g. aligning Jackson, Hibernate, Spring core versions).
     *    - Requires external Web Container deployment (WAR deployed on external Tomcat/WildFly).
     * 2. Spring Boot:
     *    - Opinionated, convention-over-configuration rapid application development layer built on top of Spring.
     *    - Auto-Configuration: Automatically configures beans based on JARs on the classpath.
     *    - Starter POMs (e.g. `spring-boot-starter-web`, `spring-boot-starter-data-jpa`): Curated, battle-tested dependency management.
     *    - Embedded Web Server: Tomcat, Jetty, or Netty bundled directly inside executable JAR (`java -jar app.jar`).
     *    - Production-Ready Features: Spring Boot Actuator (Health, Metrics, Thread Dumps).
     */
    static void topic7_SpringVsSpringBoot() {
        System.out.println("\n--- 7. SPRING VS SPRING BOOT ---");

        // Ex 7.1: Comparison Matrix
        Map<String, String> comparison = Map.of(
                "Configuration", "Spring: Manual XML/Java Config | Spring Boot: Auto-Configuration",
                "Deployment", "Spring: External WAR in Tomcat | Spring Boot: Embedded Server Executable JAR",
                "Dependencies", "Spring: Manual version alignment | Spring Boot: Starter POMs with BOM",
                "Monitoring", "Spring: Custom build | Spring Boot: Built-in Spring Boot Actuator"
        );
        comparison.forEach((k, v) -> System.out.println("Ex 7.1 - " + k + " -> " + v));

        // Ex 7.2: Starter POM Dependency Aggregator Simulation
        class StarterWebSimulator {
            public List<String> getBundledDependencies() {
                return List.of("spring-web", "spring-webmvc", "tomcat-embed-core", "jackson-databind");
            }
        }
        System.out.println("Ex 7.2 - spring-boot-starter-web automatically imports: " + new StarterWebSimulator().getBundledDependencies());

        // Ex 7.3: Embedded Server Bootstrap Simulation (Embedded Tomcat)
        class EmbeddedServerLauncher {
            public String startServer(int port) {
                return "Embedded Tomcat 10.1 initialized on port: " + port + " (Zero external WAR install)";
            }
        }
        System.out.println("Ex 7.3 - " + new EmbeddedServerLauncher().startServer(8080));

        // Ex 7.4: Production Executable Fat JAR Structure
        System.out.println("Ex 7.4 - Fat JAR layout: BOOT-INF/classes, BOOT-INF/lib (all dependencies), org/springframework/boot/loader/JarLauncher");

        // Ex 7.5: Opinionated Defaults Concept
        System.out.println("Ex 7.5 - Opinionated Default: Adding H2 on classpath automatically configures in-memory DataSource & EntityManager");
    }

    // ============================================================================
    // 8. @SPRINGBOOTAPPLICATION
    // ============================================================================
    /**
     * ARCHITECTURAL & INTERVIEW NOTES - @SPRINGBOOTAPPLICATION:
     * - `@SpringBootApplication` is a composite 3-in-1 convenience annotation:
     *   1. `@SpringBootConfiguration`: Specialization of `@Configuration`. Marks the class as a configuration source for beans.
     *   2. `@EnableAutoConfiguration`: Enables Spring Boot's auto-configuration engine via AutoConfigurationImportSelector.
     *   3. `@ComponentScan`: Scans the current package and all sub-packages for `@Component`, `@Service`, `@Repository`, `@Controller`.
     *
     * EXAMINER / RECRUITER GOTCHA:
     * - What happens if you place your main class with `@SpringBootApplication` inside `com.example.app.main` while services are in `com.example.app.service`?
     *   `@ComponentScan` scans from the main class package downwards! Services won't be found unless `scanBasePackages` is configured!
     * - Conditional Annotations: `@ConditionalOnClass`, `@ConditionalOnMissingBean`, `@ConditionalOnProperty`, `@ConditionalOnWebApplication`.
     */
    static void topic8_SpringBootApplicationAnnotation() {
        System.out.println("\n--- 8. @SPRINGBOOTAPPLICATION ---");

        // Ex 8.1: Composite Annotation Decomposition
        System.out.println("Ex 8.1 - @SpringBootApplication bundles: @SpringBootConfiguration + @EnableAutoConfiguration + @ComponentScan");

        // Ex 8.2: @ConditionalOnMissingBean Simulation
        class AutoConfigurationRegistry {
            private final Map<String, Object> context = new HashMap<>();
            public void registerDefaultBeanIfMissing(String beanName, Supplier<Object> defaultSupplier) {
                context.computeIfAbsent(beanName, k -> defaultSupplier.get());
            }
            public Object get(String name) { return context.get(name); }
        }
        AutoConfigurationRegistry registry = new AutoConfigurationRegistry();
        registry.registerDefaultBeanIfMissing("dataSource", () -> "HikariCP-Default-DataSource");
        registry.registerDefaultBeanIfMissing("dataSource", () -> "Tomcat-DataSource"); // Ignored!
        System.out.println("Ex 8.2 - @ConditionalOnMissingBean retained original: " + registry.get("dataSource"));

        // Ex 8.3: @ConditionalOnProperty Feature Flag simulation
        class FeatureFlagConditional {
            public boolean isEnabled(Map<String, String> properties, String propertyKey) {
                return "true".equalsIgnoreCase(properties.get(propertyKey));
            }
        }
        boolean cacheEnabled = new FeatureFlagConditional().isEnabled(Map.of("app.cache.enabled", "true"), "app.cache.enabled");
        System.out.println("Ex 8.3 - @ConditionalOnProperty(name=\"app.cache.enabled\", havingValue=\"true\"): " + cacheEnabled);

        // Ex 8.4: SpringApplication.run() internal initialization stages
        List<String> bootstrapPhases = List.of(
                "1. Create SpringApplication instance",
                "2. Detect WebApplicationType (SERVLET vs REACTIVE)",
                "3. Load ApplicationContextInitializers & ApplicationListeners",
                "4. Prepare Environment & Profiles",
                "5. Refresh ApplicationContext & AutoConfigurations",
                "6. Execute CommandLineRunner & ApplicationRunner beans"
        );
        System.out.println("Ex 8.4 - SpringApplication.run() bootstrap sequence: " + bootstrapPhases.get(4));

        // Ex 8.5: Excluding specific auto-configurations
        System.out.println("Ex 8.5 - @SpringBootApplication(exclude = {DataSourceAutoConfiguration.class}) disables DB auto-config");
    }

    // ============================================================================
    // 9. DEPENDENCY INJECTION (DI) VS INVERSION OF CONTROL (IOC)
    // ============================================================================
    /**
     * ARCHITECTURAL & INTERVIEW NOTES - IOC VS DI:
     * 1. Inversion of Control (IoC):
     *    - Broad software architectural design principle (The "Hollywood Principle": *Don't call us, we'll call you*).
     *    - Control of object creation, lifecycle, and flow execution is transferred from application code to a Framework/Container.
     *    - Examples of IoC: Template Method Pattern, Event Listeners, Spring Container.
     * 2. Dependency Injection (DI):
     *    - Specific subtype and design pattern implementing IoC.
     *    - Objects do not instantiate their dependencies; dependencies are "injected" from the outside (Constructor, Setter, Field).
     * 3. Spring IoC Container:
     *    - Represented by `BeanFactory` (basic lightweight container) and `ApplicationContext` (enterprise features: AOP, i18n, Events, Environment).
     */
    static void topic9_DependencyInjectionVsIoC() {
        System.out.println("\n--- 9. DEPENDENCY INJECTION VS INVERSION OF CONTROL ---");

        // Ex 9.1: Without IoC (Tightly Coupled - Object controls dependency creation)
        class Engine { public String sound() { return "V8 Roar"; } }
        class TightCar {
            private final Engine engine = new Engine(); // Hard-coded creation!
            public String drive() { return "Driving with " + engine.sound(); }
        }
        System.out.println("Ex 9.1 - Tightly coupled without IoC: " + new TightCar().drive());

        // Ex 9.2: With IoC & Dependency Injection (Decoupled via Interface & Inversion)
        interface EngineContract { String sound(); }
        class ElectricEngine implements EngineContract { @Override public String sound() { return "Silent Electric"; } }
        class DecoupledCar {
            private final EngineContract engine;
            public DecoupledCar(EngineContract e) { this.engine = e; } // Dependency Injected!
            public String drive() { return "Driving with " + engine.sound(); }
        }
        DecoupledCar car = new DecoupledCar(new ElectricEngine());
        System.out.println("Ex 9.2 - Decoupled with DI: " + car.drive());

        // Ex 9.3: Mini IoC Container Engine Simulator
        class SimpleIoCContainer {
            private final Map<Class<?>, Object> singletons = new HashMap<>();
            public <T> void register(Class<T> clazz, T instance) { singletons.put(clazz, instance); }
            @SuppressWarnings("unchecked")
            public <T> T getBean(Class<T> clazz) { return (T) singletons.get(clazz); }
        }
        SimpleIoCContainer ioc = new SimpleIoCContainer();
        ioc.register(EngineContract.class, new ElectricEngine());
        DecoupledCar iocCar = new DecoupledCar(ioc.getBean(EngineContract.class));
        System.out.println("Ex 9.3 - Mini IoC Container resolved bean: " + iocCar.drive());

        // Ex 9.4: BeanFactory vs ApplicationContext comparison
        System.out.println("Ex 9.4 - BeanFactory: Lazy bean loading (low memory footprint) | ApplicationContext: Eager loading + AOP + Events + i18n");

        // Ex 9.5: ApplicationEventPublisher (IoC Event-Driven Architecture)
        class UserRegisteredEvent { String email; UserRegisteredEvent(String e) { this.email = e; } }
        List<String> eventAudits = new ArrayList<>();
        Consumer<UserRegisteredEvent> listener = event -> eventAudits.add("Email sent to: " + event.email);
        listener.accept(new UserRegisteredEvent("dev@example.com"));
        System.out.println("Ex 9.5 - ApplicationContext Event Inversion: " + eventAudits);
    }

    // ============================================================================
    // 10. WHAT IS HIBERNATE AND JPA
    // ============================================================================
    /**
     * ARCHITECTURAL & INTERVIEW NOTES - JPA VS HIBERNATE:
     * 1. JPA (Jakarta Persistence API):
     *    - The official Java standard Specification / API standard for Object-Relational Mapping (ORM).
     *    - Interfaces: `EntityManager`, `EntityManagerFactory`, `EntityTransaction`, `@Entity`, `@Table`, `@Id`.
     *    - Contains NO executable implementation code.
     * 2. Hibernate ORM:
     *    - The most popular concrete Implementation of the JPA specification.
     *    - Implements `EntityManager` via `org.hibernate.Session`.
     * 3. Entity Lifecycle States (4 States):
     *    - Transient (New): `new User()`, not associated with any persistence context or DB row.
     *    - Persistent (Managed): Managed by EntityManager (`em.persist()`), changes tracked by Dirty Checking.
     *    - Detached: Was managed, but Session/EntityManager closed (`em.detach()`, `em.clear()`).
     *    - Removed: Marked for deletion (`em.remove()`), DB DELETE issued on commit.
     * 4. First-Level Cache (L1 Cache):
     *    - Bound to the current EntityManager / Session. Eliminates duplicate SQL queries for same ID within same transaction.
     */
    static void topic10_HibernateVsJPA() {
        System.out.println("\n--- 10. HIBERNATE VS JPA ---");

        // Ex 10.1: JPA (Specification) vs Hibernate (Implementation) architectural mapping
        System.out.println("Ex 10.1 - Architecture: JPA (javax.persistence / jakarta.persistence) = Interface Contract");
        System.out.println("Ex 10.1 - Architecture: Hibernate (org.hibernate) = ORM Implementation Engine");

        // Ex 10.2: 4 Entity Lifecycle States Simulator
        enum EntityState { TRANSIENT, PERSISTENT_MANAGED, DETACHED, REMOVED }
        class EntityLifecycleTracker {
            EntityState state = EntityState.TRANSIENT;
            void persist() { state = EntityState.PERSISTENT_MANAGED; }
            void detach() { state = EntityState.DETACHED; }
            void remove() { state = EntityState.REMOVED; }
        }
        EntityLifecycleTracker entity = new EntityLifecycleTracker();
        System.out.println("Ex 10.2 - Initial State: " + entity.state);
        entity.persist();
        System.out.println("Ex 10.2 - After em.persist(): " + entity.state);
        entity.detach();
        System.out.println("Ex 10.2 - After em.clear() / detach(): " + entity.state);

        // Ex 10.3: First-Level Cache (Persistence Context) Simulator
        class PersistenceContextL1Cache {
            private final Map<Long, String> l1Cache = new HashMap<>();
            public String findById(Long id, Function<Long, String> dbQuery) {
                if (l1Cache.containsKey(id)) {
                    return "L1_CACHE_HIT: " + l1Cache.get(id);
                }
                String data = dbQuery.apply(id);
                l1Cache.put(id, data);
                return "DB_QUERY_EXECUTED: " + data;
            }
        }
        PersistenceContextL1Cache em = new PersistenceContextL1Cache();
        String q1 = em.findById(101L, id -> "User_Alice");
        String q2 = em.findById(101L, id -> "User_Alice"); // Cached!
        System.out.println("Ex 10.3 - " + q1);
        System.out.println("Ex 10.3 - " + q2);

        // Ex 10.4: Hibernate Automatic Dirty Checking Simulation
        class ManagedEntity {
            String name; String initialSnapshot;
            ManagedEntity(String name) { this.name = name; this.initialSnapshot = name; }
            boolean isDirty() { return !name.equals(initialSnapshot); }
        }
        ManagedEntity managedUser = new ManagedEntity("Alice");
        managedUser.name = "Alice Updated";
        System.out.println("Ex 10.4 - Dirty Checking detected modification for automatic SQL UPDATE: " + managedUser.isDirty());

        // Ex 10.5: JPQL vs Native SQL
        System.out.println("Ex 10.5 - JPQL: 'SELECT u FROM User u WHERE u.age > :age' operates on Java Entity Classes, NOT DB Tables");
    }

    // ============================================================================
    // 11. WHAT IS ENTITY AND DTO
    // ============================================================================
    /**
     * ARCHITECTURAL & INTERVIEW NOTES - ENTITY VS DTO:
     * 1. Entity (`@Entity`):
     *    - Represents a database table structure and persistence mapping.
     *    - Managed by JPA EntityManager. Heavy lifecycle, dirty checking, lazy relations.
     * 2. DTO (Data Transfer Object):
     *    - Plain POJO or Java Record designed to transfer data between client and server across API boundaries.
     * 3. Why NEVER expose Entities directly in REST Controllers:
     *    - Security / Over-Posting Vulnerability: Clients might pass `isAdmin=true` or modify restricted fields.
     *    - Infinite JSON Recursion / Circular Reference: Bidirectional `@OneToMany` & `@ManyToOne` cause Jackson StackOverflowError.
     *    - API Coupling: Changing a DB column name breaks the public API contract.
     *    - Performance: Serializing entities triggers unwanted LazyInitializationExceptions.
     */
    static void topic11_EntityVsDTO() {
        System.out.println("\n--- 11. ENTITY VS DTO ---");

        // Ex 11.1: Database Entity vs Client DTO
        class UserEntity {
            Long id = 101L;
            String username = "john_doe";
            String passwordHash = "$2a$12$e8xXYZ_HASHED_PASSWORD"; // Sensitive!
            boolean isInternalAdmin = true; // Security restricted!
        }
        record UserResponseDTO(Long id, String username) {} // Clean, safe DTO!

        UserEntity entity = new UserEntity();
        UserResponseDTO dto = new UserResponseDTO(entity.id, entity.username);
        System.out.println("Ex 11.1 - Safe DTO excludes sensitive fields: " + dto);

        // Ex 11.2: MapStruct / ModelMapper pattern simulator
        class UserMapper {
            public static UserResponseDTO toDTO(UserEntity e) {
                return new UserResponseDTO(e.id, e.username);
            }
        }
        System.out.println("Ex 11.2 - Mapper converted entity to DTO: " + UserMapper.toDTO(entity));

        // Ex 11.3: Spring Data JPA Interface-based Projections
        interface UserSummaryProjection {
            String getUsername();
        }
        UserSummaryProjection projection = () -> "alice_projection";
        System.out.println("Ex 11.3 - Spring Data Interface Projection (Optimized SELECT subset): " + projection.getUsername());

        // Ex 11.4: Java 21 Record-based Constructor Expression in JPQL
        System.out.println("Ex 11.4 - JPQL DTO Query: 'SELECT new com.app.dto.UserDTO(u.id, u.name) FROM User u'");

        // Ex 11.5: Preventing Jackson Infinite Recursion
        System.out.println("Ex 11.5 - DTOs prevent @OneToMany infinite JSON recursion without needing @JsonManagedReference/@JsonBackReference");
    }

    // ============================================================================
    // 12. GLOBAL EXCEPTION HANDLING
    // ============================================================================
    /**
     * ARCHITECTURAL & INTERVIEW NOTES - GLOBAL EXCEPTION HANDLING:
     * 1. `@ControllerAdvice` / `@RestControllerAdvice`:
     *    - Global interceptor for exceptions thrown across all `@RequestMapping` controllers.
     *    - Uses AOP (Aspect-Oriented Programming) to catch uncaught exceptions and return standardized error payloads.
     * 2. `@ExceptionHandler(CustomException.class)`:
     *    - Maps specific exception types to customized HTTP status codes and response bodies.
     * 3. RFC 7807 / RFC 9457 `ProblemDetail` (Spring 6 & Spring Boot 3 standard):
     *    - Standardized JSON specification for HTTP API error responses (`type`, `title`, `status`, `detail`, `instance`).
     */
    static void topic12_GlobalExceptionHandling() {
        System.out.println("\n--- 12. GLOBAL EXCEPTION HANDLING ---");

        // Ex 12.1: Custom Domain Exceptions
        class ResourceNotFoundException extends RuntimeException {
            public ResourceNotFoundException(String msg) { super(msg); }
        }

        // Ex 12.2: Standardized API Error Response Payload
        record ErrorResponse(Instant timestamp, int status, String error, String message, String path) {}

        // Ex 12.3: @RestControllerAdvice Simulator
        class GlobalExceptionHandlerSimulator {
            public ErrorResponse handleResourceNotFound(ResourceNotFoundException ex, String path) {
                return new ErrorResponse(Instant.now(), 404, "Not Found", ex.getMessage(), path);
            }
            public ErrorResponse handleGenericException(Exception ex, String path) {
                return new ErrorResponse(Instant.now(), 500, "Internal Server Error", "An unexpected error occurred", path);
            }
        }
        GlobalExceptionHandlerSimulator advice = new GlobalExceptionHandlerSimulator();
        ErrorResponse notFoundRes = advice.handleResourceNotFound(new ResourceNotFoundException("Product with ID 99 not found"), "/api/v1/products/99");
        System.out.println("Ex 12.3 - @RestControllerAdvice structured 404 response: " + notFoundRes);

        // Ex 12.4: Spring Boot 3 RFC 7807 ProblemDetail Structure
        Map<String, Object> problemDetail = Map.of(
                "type", "https://api.example.com/errors/not-found",
                "title", "Resource Not Found",
                "status", 404,
                "detail", "Order #501 does not exist"
        );
        System.out.println("Ex 12.4 - Spring Boot 3 / Spring 6 ProblemDetail (RFC 7807): " + problemDetail);

        // Ex 12.5: MethodArgumentNotValidException handler for Bean Validation errors
        Map<String, String> fieldErrors = Map.of("email", "Must be valid format", "age", "Must be >= 18");
        System.out.println("Ex 12.5 - Bean validation error aggregation: " + fieldErrors);
    }

    // ============================================================================
    // 13. HOW TO CREATE A REST API
    // ============================================================================
    /**
     * ARCHITECTURAL & INTERVIEW NOTES - REST API LAYERED ARCHITECTURE:
     * - Production Pattern: Controller ➔ Service ➔ Repository ➔ Database.
     * - Controller: Accepts HTTP requests, validates DTOs via `@Valid`, delegates to Service, returns `ResponseEntity<T>`.
     * - Service: Encapsulates Business logic and `@Transactional` boundaries.
     * - Repository: Performs Data Access with Spring Data JPA.
     * - Richardson Maturity Model:
     *   * Level 0: Swamp of POX (Single URI, POST only).
     *   * Level 1: Resources (Individual URIs).
     *   * Level 2: HTTP Verbs (GET, POST, PUT, DELETE + Status codes).
     *   * Level 3: HATEOAS (Hypermedia As The Engine Of Application State).
     */
    static void topic13_HowToCreateRestAPI() {
        System.out.println("\n--- 13. HOW TO CREATE A REST API ---");

        // Layer 1: DTO
        record ProductDTO(Long id, String name, double price) {}

        // Layer 2: Repository
        interface ProductRepository {
            ProductDTO save(ProductDTO p);
            Optional<ProductDTO> findById(Long id);
        }
        class InMemoryProductRepo implements ProductRepository {
            private final Map<Long, ProductDTO> db = new ConcurrentHashMap<>();
            private final AtomicLong idGen = new AtomicLong(1);
            @Override public ProductDTO save(ProductDTO p) {
                Long id = p.id() != null ? p.id() : idGen.getAndIncrement();
                ProductDTO saved = new ProductDTO(id, p.name(), p.price());
                db.put(id, saved);
                return saved;
            }
            @Override public Optional<ProductDTO> findById(Long id) { return Optional.ofNullable(db.get(id)); }
        }

        // Layer 3: Service
        class ProductService {
            private final ProductRepository repo;
            public ProductService(ProductRepository r) { this.repo = r; }
            public ProductDTO create(ProductDTO dto) { return repo.save(dto); }
            public ProductDTO getById(Long id) {
                return repo.findById(id).orElseThrow(() -> new RuntimeException("Product " + id + " not found"));
            }
        }

        // Layer 4: Controller
        class ProductRestController {
            private final ProductService service;
            public ProductRestController(ProductService s) { this.service = s; }
            public String createProduct(ProductDTO dto) {
                ProductDTO created = service.create(dto);
                return "HTTP 201 Created -> Location: /api/products/" + created.id() + " | Body: " + created;
            }
            public String getProduct(Long id) {
                ProductDTO p = service.getById(id);
                return "HTTP 200 OK -> Body: " + p;
            }
        }

        ProductRepository repo = new InMemoryProductRepo();
        ProductService svc = new ProductService(repo);
        ProductRestController controller = new ProductRestController(svc);

        System.out.println("Ex 13.1 - POST /api/products: " + controller.createProduct(new ProductDTO(null, "Gaming Laptop", 1500.0)));
        System.out.println("Ex 13.2 - GET /api/products/1: " + controller.getProduct(1L));
        System.out.println("Ex 13.3 - Layered separation guarantees separation of concerns and independent testability");
        System.out.println("Ex 13.4 - Pagination support via Pageable & Page<T> in Spring Data JPA");
        System.out.println("Ex 13.5 - HATEOAS links integration via RepresentationModelAssembler");
    }

    // ============================================================================
    // 14. REST PROTOCOL HTTP VERBS & STATUS CODES
    // ============================================================================
    /**
     * ARCHITECTURAL & INTERVIEW NOTES - HTTP VERBS & STATUS CODES:
     * - GET: Retrieve resource. Safe & Idempotent. Return 200 OK or 404 Not Found.
     * - POST: Create resource or process operation. NOT Safe & NOT Idempotent. Return 201 Created + Location header.
     * - PUT: Full update / Replace resource. Idempotent. Return 200 OK or 204 No Content.
     * - PATCH: Partial update. NOT necessarily idempotent. Return 200 OK.
     * - DELETE: Remove resource. Idempotent. Return 204 No Content.
     * - HEAD: Same as GET but returns headers only (no body). Check file size/caching.
     * - OPTIONS: Returns allowed HTTP methods (`Allow: GET, POST, OPTIONS`). Used by browser CORS pre-flight!
     *
     * Status Code Ranges:
     * - 2xx (Success): 200 OK, 201 Created, 204 No Content, 202 Accepted.
     * - 3xx (Redirection): 301 Moved Permanently, 304 Not Modified.
     * - 4xx (Client Error): 400 Bad Request, 401 Unauthorized, 403 Forbidden, 404 Not Found, 409 Conflict, 422 Unprocessable Entity.
     * - 5xx (Server Error): 500 Internal Server Error, 502 Bad Gateway, 503 Service Unavailable, 504 Gateway Timeout.
     */
    static void topic14_RestHttpProtocolsAndStatusCodes() {
        System.out.println("\n--- 14. REST PROTOCOLS & STATUS CODES ---");

        Map<String, String> verbs = Map.of(
                "GET", "Safe=YES, Idempotent=YES | Expected Status: 200 OK, 404 Not Found",
                "POST", "Safe=NO, Idempotent=NO  | Expected Status: 201 Created, 400 Bad Request",
                "PUT", "Safe=NO, Idempotent=YES | Expected Status: 200 OK, 204 No Content",
                "PATCH", "Safe=NO, Idempotent=NO | Expected Status: 200 OK",
                "DELETE", "Safe=NO, Idempotent=YES| Expected Status: 204 No Content"
        );
        verbs.forEach((k, v) -> System.out.println("Ex 14.1 - " + k + " -> " + v));

        // Ex 14.2: 401 Unauthorized (Authentication missing) vs 403 Forbidden (Authenticated but lacks role permission)
        System.out.println("Ex 14.2 - 401 Unauthorized (Who are you?) vs 403 Forbidden (You cannot access this admin resource!)");

        // Ex 14.3: 409 Conflict (Optimistic locking failure / Unique constraint violation)
        System.out.println("Ex 14.3 - 409 Conflict used for OptimisticLockingFailureException or duplicate email registration");

        // Ex 14.4: 202 Accepted (Asynchronous background processing)
        System.out.println("Ex 14.4 - 202 Accepted returned when task is accepted for async background execution via RabbitMQ/Kafka");

        // Ex 14.5: CORS Preflight OPTIONS Request handling
        System.out.println("Ex 14.5 - Browser sends OPTIONS preflight to verify 'Access-Control-Allow-Methods: GET, POST, PUT, DELETE'");
    }

    // ============================================================================
    // 15. DIFFERENCE BETWEEN PUT AND PATCH
    // ============================================================================
    /**
     * ARCHITECTURAL & INTERVIEW NOTES - PUT VS PATCH:
     * 1. PUT (Full Replacement):
     *    - Completely replaces the entire resource.
     *    - If client sends `{ "name": "New Name" }` and omits `"age"`, `"age"` is cleared / set to null/default!
     *    - Strictly IDEMPOTENT: Calling PUT 10 times produces the exact same DB state.
     * 2. PATCH (Partial Modification):
     *    - Modifies only the specified delta fields; unmentioned fields remain untouched in DB.
     *    - Specifications: JSON Merge Patch (RFC 7396) and JSON Patch (RFC 6902 with ops: add, remove, replace).
     */
    static void topic15_PutVsPatch() {
        System.out.println("\n--- 15. DIFFERENCE BETWEEN PUT AND PATCH ---");

        class UserProfile {
            String name = "Original Name";
            String email = "orig@example.com";
            int age = 30;
            @Override public String toString() { return "[name=" + name + ", email=" + email + ", age=" + age + "]"; }
        }

        // Ex 15.1: PUT (Complete Replacement)
        UserProfile putTarget = new UserProfile();
        // Client sends new payload with missing age -> age reset to default 0!
        putTarget.name = "Updated Name";
        putTarget.email = "updated@example.com";
        putTarget.age = 0; // Nullified/reset because not in PUT payload
        System.out.println("Ex 15.1 - PUT (Full replacement): " + putTarget);

        // Ex 15.2: PATCH (Partial Delta Update)
        UserProfile patchTarget = new UserProfile();
        // Client sends only: { "email": "new_email@example.com" }
        patchTarget.email = "new_email@example.com"; // name and age remain untouched!
        System.out.println("Ex 15.2 - PATCH (Partial update preserves untouched fields): " + patchTarget);

        // Ex 15.3: JSON Patch (RFC 6902 Operations) Simulator
        class JsonPatchOperation {
            String op; String path; String value;
            JsonPatchOperation(String o, String p, String v) { this.op = o; this.path = p; this.value = v; }
        }
        List<JsonPatchOperation> patchOps = List.of(
                new JsonPatchOperation("replace", "/email", "patch@example.com"),
                new JsonPatchOperation("remove", "/phoneNumber", null)
        );
        System.out.println("Ex 15.3 - RFC 6902 JSON Patch operations: " + patchOps.size() + " ops defined");

        // Ex 15.4: Idempotency comparison
        System.out.println("Ex 15.4 - PUT is guaranteed idempotent. PATCH is usually idempotent, but incremental operations (op: increment) are non-idempotent");

        // Ex 15.5: Spring implementation using Jackson JsonNullable or Map<String, Object>
        System.out.println("Ex 15.5 - Best Practice in Spring: Use org.openapitools.jackson.nullable.JsonNullable to distinguish between explicit null vs omitted field");
    }

    // ============================================================================
    // 16. TRANSACTION LIFECYCLE, PROPAGATION & ISOLATION
    // ============================================================================
    /**
     * ARCHITECTURAL & INTERVIEW NOTES - TRANSACTION PROPAGATION & ISOLATION:
     * 1. Transaction Propagation:
     *    - REQUIRED (Default): Joins current transaction if exists; creates new one if none.
     *    - REQUIRES_NEW: Always creates a NEW transaction, suspending the existing one.
     *    - NESTED: Executes within a nested transaction using Savepoints (Rollback only rolls back to savepoint).
     *    - SUPPORTS: Executes in transaction if exists; non-transactionally otherwise.
     *    - NOT_SUPPORTED: Always executes non-transactionally, suspending existing transaction.
     *    - MANDATORY: Must run within an existing transaction; throws exception if none exists.
     *    - NEVER: Throws exception if an active transaction exists.
     * 2. Transaction Isolation Levels:
     *    - READ_UNCOMMITTED: Allows Dirty Reads (reading uncommitted data).
     *    - READ_COMMITTED (PostgreSQL/Oracle default): Prevents Dirty Reads. Subject to Non-Repeatable Reads.
     *    - REPEATABLE_READ (MySQL InnoDB default): Prevents Non-Repeatable Reads. Subject to Phantom Reads.
     *    - SERIALIZABLE: Strict serialization. Zero anomalies, highest locking overhead.
     */
    static void topic16_TransactionLifecycleAndPropagationIsolation() {
        System.out.println("\n--- 16. TRANSACTION PROPAGATION & ISOLATION ---");

        // Ex 16.1: Propagation Matrix Simulation
        Map<String, String> propagations = Map.of(
                "REQUIRED (Default)", "Joins existing TX, or creates new one",
                "REQUIRES_NEW", "Suspends outer TX, creates completely independent inner TX",
                "NESTED", "Creates DB Savepoint within existing TX (partial rollback)",
                "MANDATORY", "Throws IllegalTransactionStateException if no outer TX exists",
                "NEVER", "Throws exception if called inside an active TX"
        );
        propagations.forEach((k, v) -> System.out.println("Ex 16.1 - Propagation." + k + " -> " + v));

        // Ex 16.2: REQUIRES_NEW Audit Log Pattern (Outer TX fails, Audit log still commits!)
        class AuditLogger {
            // @Transactional(propagation = Propagation.REQUIRES_NEW)
            public void logAudit(String event) {
                System.out.println("  [AuditLog REQUIRES_NEW] Committed audit independently: " + event);
            }
        }
        new AuditLogger().logAudit("ORDER_ATTEMPT_FAILED");
        System.out.println("Ex 16.2 - REQUIRES_NEW guarantees audit logging even when outer order creation rolls back");

        // Ex 16.3: Isolation Levels vs Concurrency Anomalies
        System.out.println("Ex 16.3 - Anomalies: Dirty Read (Reads uncommitted data) | Non-Repeatable Read (Row re-read changes) | Phantom Read (New rows appear)");

        // Ex 16.4: Isolation Level Matrix
        Map<String, String> isolations = Map.of(
                "READ_COMMITTED", "Prevents Dirty Read (Default in PostgreSQL, Oracle, SQL Server)",
                "REPEATABLE_READ", "Prevents Dirty Read + Non-Repeatable Read (Default in MySQL)",
                "SERIALIZABLE", "Prevents Dirty Read + Non-Repeatable Read + Phantom Read (Full locking)"
        );
        isolations.forEach((k, v) -> System.out.println("Ex 16.4 - Isolation." + k + " -> " + v));

        // Ex 16.5: Transaction Synchronization Manager (ThreadLocal inspection)
        System.out.println("Ex 16.5 - TransactionSynchronizationManager binds DB Connection to current thread via ThreadLocal");
    }

    // ============================================================================
    // 17. SPRING PROFILES & ENVIRONMENT MANAGEMENT
    // ============================================================================
    /**
     * ARCHITECTURAL & INTERVIEW NOTES - SPRING PROFILES:
     * - `@Profile({"dev", "staging", "prod"})`: Restricts bean creation to active environments.
     * - Configuration Files: `application.yml`, `application-dev.yml`, `application-prod.yml`.
     * - Activation: `-Dspring.profiles.active=prod`, `SPRING_PROFILES_ACTIVE=prod`, or `spring.config.activate.on-profile`.
     * - In Tests: `@ActiveProfiles("test")` with in-memory H2 / Testcontainers.
     */
    static void topic17_SpringProfiles() {
        System.out.println("\n--- 17. SPRING PROFILES ---");

        // Ex 17.1: Profile-based Bean Strategy
        interface DataSourceConfig { String getUrl(); }
        class DevDataSource implements DataSourceConfig {
            @Override public String getUrl() { return "jdbc:h2:mem:devdb"; }
        }
        class ProdDataSource implements DataSourceConfig {
            @Override public String getUrl() { return "jdbc:postgresql://prod-db-cluster:5432/main"; }
        }

        class ProfileBeanFactory {
            public DataSourceConfig getDataSource(String activeProfile) {
                return switch (activeProfile.toLowerCase()) {
                    case "prod" -> new ProdDataSource();
                    default -> new DevDataSource();
                };
            }
        }
        DataSourceConfig devDs = new ProfileBeanFactory().getDataSource("dev");
        DataSourceConfig prodDs = new ProfileBeanFactory().getDataSource("prod");
        System.out.println("Ex 17.1 - Active Profile 'dev' URL: " + devDs.getUrl());
        System.out.println("Ex 17.1 - Active Profile 'prod' URL: " + prodDs.getUrl());

        // Ex 17.2: Property precedence order in Spring Boot
        List<String> precedence = List.of(
                "1. Command-line arguments (--server.port=9090)",
                "2. Java System Properties (-Dserver.port=9090)",
                "3. OS Environment Variables (SERVER_PORT=9090)",
                "4. Profile-specific application-{profile}.yml",
                "5. Default application.yml inside JAR"
        );
        System.out.println("Ex 17.2 - Property Override Hierarchy: " + precedence.get(0));

        // Ex 17.3: Multi-document YAML in Spring Boot 2.4+
        System.out.println("Ex 17.3 - Multi-document YAML separator '---' with 'spring.config.activate.on-profile: prod'");

        // Ex 17.4: Default Profile fallback
        System.out.println("Ex 17.4 - spring.profiles.default=default applies when no active profile is provided");

        // Ex 17.5: @ActiveProfiles in Integration Tests
        System.out.println("Ex 17.5 - @SpringBootTest + @ActiveProfiles(\"test\") overrides production properties with MockBeans");
    }

    // ============================================================================
    // 18. N+1 QUERY PROBLEM IN HIBERNATE / JPA
    // ============================================================================
    /**
     * ARCHITECTURAL & INTERVIEW NOTES - N+1 QUERY PROBLEM:
     * 1. The Root Cause:
     *    - Fetching N parent entities (`SELECT * FROM orders`) where each parent has a `@ManyToOne` or `@OneToMany`
     *      relation configured with LAZY loading.
     *    - When looping over the N orders and accessing `order.getCustomer().getName()`, Hibernate executes 1 additional
     *      SQL query PER parent row ➔ 1 initial query + N sub-queries = N + 1 queries!
     * 2. The 4 Solutions:
     *    - Solution 1 (JOIN FETCH in JPQL): `SELECT o FROM Order o JOIN FETCH o.customer` (Executes single SQL JOIN).
     *    - Solution 2 (@EntityGraph): Declaratively forces eager join fetching for specific queries without touching Entity model.
     *    - Solution 3 (@BatchSize(size = 25)): Batches lazy load queries using SQL `IN (?, ?, ?...)`, reducing N queries to N/25.
     *    - Solution 4 (DTO Projections): Fetches only required columns in a single flat SQL query.
     */
    static void topic18_NPlusOneQueryProblem() {
        System.out.println("\n--- 18. N+1 QUERY PROBLEM ---");

        // Ex 18.1: N+1 Query Problem Simulation
        List<String> simulatedSqlQueries = new ArrayList<>();
        class DatabaseSimulator {
            public List<Long> getOrderIds() {
                simulatedSqlQueries.add("SELECT id FROM orders; (Query #1)");
                return List.of(101L, 102L, 103L);
            }
            public String getCustomerForOrder(Long orderId) {
                simulatedSqlQueries.add("SELECT * FROM customers WHERE order_id = " + orderId + "; (Sub-query)");
                return "Customer_For_" + orderId;
            }
        }
        DatabaseSimulator db = new DatabaseSimulator();
        List<Long> orderIds = db.getOrderIds();
        for (Long id : orderIds) {
            db.getCustomerForOrder(id); // Triggers N queries!
        }
        System.out.println("Ex 18.1 - N+1 Problem: 1 Initial Query + " + (simulatedSqlQueries.size() - 1) + " Subqueries executed = " + simulatedSqlQueries.size() + " total queries!");

        // Ex 18.2: Solution 1 - JOIN FETCH (Single Query Resolution)
        simulatedSqlQueries.clear();
        simulatedSqlQueries.add("SELECT o, c FROM orders o INNER JOIN customers c ON o.customer_id = c.id; (Single JOIN Query)");
        System.out.println("Ex 18.2 - Solution 1 (JOIN FETCH): " + simulatedSqlQueries.get(0));

        // Ex 18.3: Solution 2 - @EntityGraph(attributePaths = {"customer"})
        System.out.println("Ex 18.3 - Solution 2 (@EntityGraph): Dynamically applies SQL JOIN at repository method level");

        // Ex 18.4: Solution 3 - @BatchSize(size = 30)
        System.out.println("Ex 18.4 - Solution 3 (@BatchSize): Combines subqueries using 'WHERE id IN (?, ?, ...)' reducing 100 queries to 4");

        // Ex 18.5: Solution 4 - Direct DTO Projection
        System.out.println("Ex 18.5 - Solution 4 (DTO Projection): 'SELECT new OrderSummaryDTO(o.id, c.name) FROM Order o JOIN o.customer c'");
    }

    // ============================================================================
    // 19. @ENABLEAUTOCONFIGURATION VS @AUTOCONFIGURATION
    // ============================================================================
    /**
     * ARCHITECTURAL & INTERVIEW NOTES - AUTO-CONFIGURATION ENGINE:
     * 1. `@EnableAutoConfiguration`:
     *    - The annotation placed on client application entry point (bundled inside `@SpringBootApplication`).
     *    - Enables the AutoConfigurationImportSelector to scan and apply auto-configurations.
     * 2. `@AutoConfiguration` (Introduced in Spring Boot 3 / 2.7+):
     *    - Replaces `@Configuration` on auto-configuration classes to declare auto-configuration ordering:
     *      `@AutoConfigureBefore`, `@AutoConfigureAfter`, `after = DataSourceAutoConfiguration.class`.
     * 3. Discovery Mechanisms:
     *    - Spring Boot 2.x: Registered inside `META-INF/spring.factories` under `org.springframework.boot.autoconfigure.EnableAutoConfiguration`.
     *    - Spring Boot 3.x: Registered inside `META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports`.
     */
    static void topic19_EnableAutoConfigurationVsAutoConfiguration() {
        System.out.println("\n--- 19. @ENABLEAUTOCONFIGURATION VS @AUTOCONFIGURATION ---");

        // Ex 19.1: AutoConfiguration Import Selector Discovery Simulation
        class AutoConfigurationDiscoverySimulator {
            public List<String> loadImportsFile(String version) {
                if ("3.x".equals(version)) {
                    return List.of("META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports");
                }
                return List.of("META-INF/spring.factories (EnableAutoConfiguration key)");
            }
        }
        System.out.println("Ex 19.1 - Spring Boot 2 auto-config file: " + new AutoConfigurationDiscoverySimulator().loadImportsFile("2.x"));
        System.out.println("Ex 19.1 - Spring Boot 3 auto-config file: " + new AutoConfigurationDiscoverySimulator().loadImportsFile("3.x"));

        // Ex 19.2: Auto-Configuration Ordering (@AutoConfigureBefore / @AutoConfigureAfter)
        class AutoConfigOrderManager {
            public List<String> orderAutoConfigs() {
                return List.of("1. DataSourceAutoConfiguration", "2. HibernateJpaAutoConfiguration", "3. TransactionAutoConfiguration");
            }
        }
        System.out.println("Ex 19.2 - Ordered AutoConfiguration pipeline: " + new AutoConfigOrderManager().orderAutoConfigs());

        // Ex 19.3: Conditional Phase evaluation (OnClass, OnBean, OnProperty)
        System.out.println("Ex 19.3 - Auto-configuration conditions evaluated during ApplicationContext refresh Phase");

        // Ex 19.4: Creating a Custom Starter with AutoConfiguration
        System.out.println("Ex 19.4 - Custom Starter pattern: my-feature-spring-boot-starter (dependencies) + my-feature-spring-boot-autoconfigure (code)");

        // Ex 19.5: Inspecting auto-configuration reports
        System.out.println("Ex 19.5 - Start app with '--debug' or check '/actuator/conditions' to view full auto-configuration evaluation report");
    }

    // ============================================================================
    // 20. SPRING ACTUATOR
    // ============================================================================
    /**
     * ARCHITECTURAL & INTERVIEW NOTES - SPRING BOOT ACTUATOR:
     * 1. What is Actuator: Production-ready operational monitoring endpoints exposing health, metrics, environment, and thread dumps.
     * 2. Essential Endpoints:
     *    - `/actuator/health`: Application liveness and readiness probes (Kubernetes friendly).
     *    - `/actuator/metrics`: Micrometer metrics (JVM memory, CPU, HTTP request latencies).
     *    - `/actuator/prometheus`: Prometheus scrape format for Grafana dashboards.
     *    - `/actuator/env`: Environment variables and configuration properties (sanitized passwords).
     *    - `/actuator/threaddump`: Real-time JVM thread dumps to diagnose deadlocks and high CPU usage.
     * 3. Security: By default, only `/health` is exposed over Web. You must explicitly configure:
     *    `management.endpoints.web.exposure.include=health,info,metrics,prometheus`.
     */
    static void topic20_SpringActuator() {
        System.out.println("\n--- 20. SPRING ACTUATOR ---");

        // Ex 20.1: Custom HealthIndicator Implementation Simulation
        interface HealthIndicator {
            Map<String, Object> health();
        }
        class DatabaseHealthIndicator implements HealthIndicator {
            @Override
            public Map<String, Object> health() {
                boolean dbAlive = true;
                return Map.of("status", dbAlive ? "UP" : "DOWN", "details", Map.of("database", "PostgreSQL", "latencyMs", 4));
            }
        }
        System.out.println("Ex 20.1 - Custom HealthIndicator /actuator/health output: " + new DatabaseHealthIndicator().health());

        // Ex 20.2: Kubernetes Liveness & Readiness Probes
        Map<String, String> k8sProbes = Map.of(
                "livenessProbe", "/actuator/health/liveness (Checks if app is alive or needs container restart)",
                "readinessProbe", "/actuator/health/readiness (Checks if app is ready to accept HTTP traffic)"
        );
        System.out.println("Ex 20.2 - Kubernetes Probes: " + k8sProbes);

        // Ex 20.3: Micrometer Metrics Counter & Timer Simulation
        class MicrometerMetricsSimulator {
            private final AtomicLong requestCounter = new AtomicLong(0);
            public void recordRequest() { requestCounter.incrementAndGet(); }
            public Map<String, Object> getMetrics() {
                return Map.of("http.server.requests.count", requestCounter.get(), "jvm.memory.used.mb", Runtime.getRuntime().totalMemory() / (1024 * 1024));
            }
        }
        MicrometerMetricsSimulator micrometer = new MicrometerMetricsSimulator();
        micrometer.recordRequest(); micrometer.recordRequest();
        System.out.println("Ex 20.3 - /actuator/metrics telemetry: " + micrometer.getMetrics());

        // Ex 20.4: Actuator Web Exposure configuration in application.yml
        System.out.println("Ex 20.4 - Exposure config: 'management.endpoints.web.exposure.include: health,metrics,prometheus'");

        // Ex 20.5: InfoContributor custom build info
        Map<String, String> buildInfo = Map.of("app.version", "2.1.0", "git.commit", "a87fd1b", "java.version", "21");
        System.out.println("Ex 20.5 - /actuator/info output: " + buildInfo);
    }

    // ============================================================================
    // 21. SPRING SECURITY
    // ============================================================================
    /**
     * ARCHITECTURAL & INTERVIEW NOTES - SPRING SECURITY:
     * 1. Architecture Flow:
     *    - Servlet Filter Chain ➔ DelegatingFilterProxy ➔ FilterChainProxy ➔ SecurityFilterChain (List of Security Filters).
     * 2. Key Interfaces & Components:
     *    - SecurityFilterChain: Bean configuring URL authorization rules (`requestMatchers("/api/admin/**").hasRole("ADMIN")`).
     *    - Authentication: Object holding Principal (User), Credentials, and GrantedAuthorities (Roles).
     *    - AuthenticationManager / ProviderManager: Coordinates AuthenticationProviders (DaoAuthenticationProvider, JwtAuthenticationProvider).
     *    - UserDetailsService: Loads user by username from DB.
     *    - PasswordEncoder: One-way hashing (BCryptPasswordEncoder with salt).
     * 3. Authentication vs Authorization:
     *    - Authentication: "Who are you?" (401 Unauthorized if invalid).
     *    - Authorization: "What are you allowed to do?" (403 Forbidden if missing role).
     */
    static void topic21_SpringSecurity() {
        System.out.println("\n--- 21. SPRING SECURITY ---");

        // Ex 21.1: PasswordEncoder (BCrypt Hashing Simulation)
        class BCryptSimulator {
            public String encode(String rawPassword) {
                // Returns simulated secure salted hash
                return "$2a$10$" + Integer.toHexString(rawPassword.hashCode()) + "_HASHED_SALT";
            }
            public boolean matches(String raw, String hash) {
                return encode(raw).equals(hash);
            }
        }
        BCryptSimulator encoder = new BCryptSimulator();
        String hash = encoder.encode("P@ssword123");
        boolean valid = encoder.matches("P@ssword123", hash);
        System.out.println("Ex 21.1 - PasswordEncoder matched: " + valid + " (Hash=" + hash + ")");

        // Ex 21.2: UserDetails & GrantedAuthority model
        record UserDetailsModel(String username, String passwordHash, List<String> roles) {}
        UserDetailsModel user = new UserDetailsModel("admin", hash, List.of("ROLE_ADMIN", "ROLE_USER"));
        System.out.println("Ex 21.2 - UserDetails loaded: " + user.username() + ", Authorities=" + user.roles());

        // Ex 21.3: AuthenticationProvider & ProviderManager Authentication Flow
        class AuthenticationManagerSimulator {
            public boolean authenticate(String username, String rawPassword, UserDetailsModel user) {
                if (!user.username().equals(username)) return false;
                return encoder.matches(rawPassword, user.passwordHash());
            }
        }
        boolean authSuccess = new AuthenticationManagerSimulator().authenticate("admin", "P@ssword123", user);
        System.out.println("Ex 21.3 - AuthenticationManager authenticated successfully: " + authSuccess);

        // Ex 21.4: Modern SecurityFilterChain configuration (Spring Security 6 / Boot 3)
        System.out.println("Ex 21.4 - Modern Security 6: @Bean SecurityFilterChain filterChain(HttpSecurity http) with authorizeHttpRequests Lambda DSL");

        // Ex 21.5: Method Security (@PreAuthorize("hasRole('ADMIN')"))
        System.out.println("Ex 21.5 - @EnableMethodSecurity enables SpEL expressions: @PreAuthorize(\"hasRole('ADMIN') and #userId == principal.id\")");
    }

    // ============================================================================
    // 22. INTERCEPTOR VS FILTER VS SECURITYCONTEXT & SECURITYCONTEXTHOLDER
    // ============================================================================
    /**
     * ARCHITECTURAL & INTERVIEW NOTES - FILTER VS INTERCEPTOR VS CONTEXT:
     * 1. Servlet Filter (javax.servlet.Filter / jakarta.servlet.Filter):
     *    - Executes at the Servlet Container level BEFORE the request reaches DispatcherServlet.
     *    - Can modify HttpServletRequest/HttpServletResponse wrappers (e.g., GZIP, CORS, Authentication, Logging).
     * 2. HandlerInterceptor (Spring MVC):
     *    - Executes INSIDE DispatcherServlet, between HandlerMapping and Controller.
     *    - Methods: `preHandle()`, `postHandle()`, `afterCompletion()`. Has access to target HandlerMethod metadata.
     * 3. SecurityContextHolder:
     *    - Stores the SecurityContext (current authenticated user) using a ThreadLocal strategy by default.
     *    - Access user anywhere: `SecurityContextHolder.getContext().getAuthentication()`.
     */
    static void topic22_InterceptorVsFilterVsSecurityContext() {
        System.out.println("\n--- 22. FILTER VS INTERCEPTOR VS SECURITYCONTEXT ---");

        // Ex 22.1: Execution Order Pipeline Simulator
        List<String> lifecycleExecution = new ArrayList<>();
        Runnable filterStep = () -> lifecycleExecution.add("1. Servlet Filter (Pre)");
        Runnable interceptorPre = () -> lifecycleExecution.add("2. HandlerInterceptor.preHandle()");
        Runnable controllerStep = () -> lifecycleExecution.add("3. Controller Method Executed");
        Runnable interceptorPost = () -> lifecycleExecution.add("4. HandlerInterceptor.postHandle()");
        Runnable filterPost = () -> lifecycleExecution.add("5. Servlet Filter (Post Response)");

        filterStep.run();
        interceptorPre.run();
        controllerStep.run();
        interceptorPost.run();
        filterPost.run();
        System.out.println("Ex 22.1 - Request Execution Pipeline: " + lifecycleExecution);

        // Ex 22.2: SecurityContextHolder ThreadLocal Simulation
        class SecurityContextHolderSimulator {
            private static final ThreadLocal<String> context = new ThreadLocal<>();
            public static void setPrincipal(String user) { context.set(user); }
            public static String getPrincipal() { return context.get(); }
            public static void clear() { context.remove(); }
        }
        SecurityContextHolderSimulator.setPrincipal("authenticated_john");
        System.out.println("Ex 22.2 - SecurityContextHolder retrieved authenticated principal: " + SecurityContextHolderSimulator.getPrincipal());
        SecurityContextHolderSimulator.clear();

        // Ex 22.3: HandlerInterceptor preHandle() authentication guard simulation
        class AuthInterceptor {
            public boolean preHandle(String authHeader) {
                return authHeader != null && authHeader.startsWith("Bearer ");
            }
        }
        System.out.println("Ex 22.3 - HandlerInterceptor preHandle authorized: " + new AuthInterceptor().preHandle("Bearer token_abc123"));

        // Ex 22.4: OncePerRequestFilter guarantees single execution per request dispatch
        System.out.println("Ex 22.4 - OncePerRequestFilter prevents duplicate filter execution during internal forwards / async dispatches");

        // Ex 22.5: MDC (Mapped Diagnostic Context) logging in Filters
        System.out.println("Ex 22.5 - Filter populates SLF4J MDC with 'traceId' and clears it in finally block to prevent ThreadLocal memory leaks");
    }

    // ============================================================================
    // 23. JWT VS OAUTH2 / OPENID CONNECT
    // ============================================================================
    /**
     * ARCHITECTURAL & INTERVIEW NOTES - JWT & OAUTH2 / OIDC:
     * 1. JWT (JSON Web Token - RFC 7519):
     *    - Self-contained, stateless authentication token consisting of 3 Base64URL-encoded parts:
     *      `Header.Payload.Signature`.
     *    - Header: Algorithm & Token type (`{"alg": "HS256", "typ": "JWT"}`).
     *    - Payload: Claims (`sub`, `roles`, `exp`, `iat`, `iss`).
     *    - Signature: `HMACSHA256(Base64(Header) + "." + Base64(Payload), secretKey)` or RSA asymmetric signature.
     * 2. OAuth2 Framework:
     *    - Authorization delegation framework (Not an authentication protocol!).
     *    - 4 Roles: Resource Owner (User), Client (SPA/Mobile App), Authorization Server (Keycloak/Okta/Auth0), Resource Server (Spring Boot API).
     *    - Grant Types: Authorization Code Grant with PKCE (Standard for modern web/mobile apps), Client Credentials Grant (Machine-to-Machine).
     * 3. OpenID Connect (OIDC):
     *    - Identity layer built ON TOP OF OAuth2. Adds the `ID Token` (JWT) for user authentication.
     */
    static void topic23_JwtVsOAuth2() {
        System.out.println("\n--- 23. JWT VS OAUTH2 / OIDC ---");

        // Ex 23.1: JWT Token Generation & Structure Simulator
        class JwtSimulator {
            public String createToken(String subject, String role) {
                String header = Base64.getUrlEncoder().withoutPadding().encodeToString("{\"alg\":\"HS256\",\"typ\":\"JWT\"}".getBytes(StandardCharsets.UTF_8));
                String payload = Base64.getUrlEncoder().withoutPadding().encodeToString(("{\"sub\":\"" + subject + "\",\"role\":\"" + role + "\"}").getBytes(StandardCharsets.UTF_8));
                String signature = Integer.toHexString((header + "." + payload + ".SECRET_KEY_123").hashCode());
                return header + "." + payload + "." + signature;
            }
            public boolean verify(String token) {
                String[] parts = token.split("\\.");
                if (parts.length != 3) return false;
                String expectedSig = Integer.toHexString((parts[0] + "." + parts[1] + ".SECRET_KEY_123").hashCode());
                return expectedSig.equals(parts[2]);
            }
        }
        JwtSimulator jwt = new JwtSimulator();
        String token = jwt.createToken("user_alex", "ROLE_ADMIN");
        System.out.println("Ex 23.1 - Generated Stateless JWT: " + token);
        System.out.println("Ex 23.1 - Verified JWT Signature: " + jwt.verify(token));

        // Ex 23.2: Symmetric (HMAC-SHA256) vs Asymmetric (RSA / Public-Private Key) Signing
        System.out.println("Ex 23.2 - Symmetric (HS256): Same shared secret for Auth & Resource Server | Asymmetric (RS256): Private key signs, Public key verifies (JWKS)");

        // Ex 23.3: OAuth2 Authorization Code Grant Flow with PKCE
        List<String> oauth2Steps = List.of(
                "1. User clicks login ➔ Redirect to Auth Server with code_challenge (PKCE)",
                "2. User authenticates on Auth Server ➔ Auth Server redirects back with Authorization Code",
                "3. Client exchanges Code + code_verifier for Access Token & ID Token",
                "4. Client accesses Resource Server with 'Authorization: Bearer <JWT>'"
        );
        System.out.println("Ex 23.3 - OAuth2 Authorization Code Flow with PKCE: " + oauth2Steps.get(2));

        // Ex 23.4: OAuth2 Roles Breakdown
        Map<String, String> oauth2Roles = Map.of(
                "Resource Owner", "The End User",
                "Client", "Frontend App (React/Angular/Mobile)",
                "Authorization Server", "Issues Tokens (Keycloak / Auth0 / Okta)",
                "Resource Server", "Spring Boot REST API protecting data"
        );
        System.out.println("Ex 23.4 - OAuth2 Core Roles: " + oauth2Roles);

        // Ex 23.5: Spring Security as OAuth2 Resource Server
        System.out.println("Ex 23.5 - Spring Boot Resource Server config: 'spring.security.oauth2.resourceserver.jwt.issuer-uri=https://auth.example.com'");
    }

    // ============================================================================
    // 24. SPRING MVC REQUEST FLOW
    // ============================================================================
    /**
     * ARCHITECTURAL & INTERVIEW NOTES - SPRING MVC REQUEST FLOW:
     * - Complete 8-Step Lifecycle:
     *   1. HTTP Request arrives at Servlet Container (Tomcat).
     *   2. Passes through Filter Chain (Security, Logging, CORS).
     *   3. Arrives at `DispatcherServlet` (The Front Controller).
     *   4. `DispatcherServlet` queries `HandlerMapping` (e.g. RequestMappingHandlerMapping) to locate matching Controller.
     *   5. Returns `HandlerExecutionChain` (Handler + Interceptors).
     *   6. `HandlerAdapter` (RequestMappingHandlerAdapter) invokes Controller method, resolving arguments (@RequestParam, @RequestBody).
     *   7. Controller executes business logic via Services.
     *   8. Response Transformation:
     *      - For `@RestController`: `HttpMessageConverter` (Jackson) serializes DTO directly to JSON response stream.
     *      - For standard MVC: `ViewResolver` resolves HTML template (Thymeleaf).
     */
    static void topic24_SpringMvcRequestFlow() {
        System.out.println("\n--- 24. SPRING MVC REQUEST FLOW ---");

        // Ex 24.1: DispatcherServlet Pipeline Simulator
        class DispatcherServletSimulator {
            public String processRequest(String httpMethod, String path) {
                // Step 1: Filter Chain
                String step1 = "FilterChain -> ";
                // Step 2: DispatcherServlet
                String step2 = "DispatcherServlet -> ";
                // Step 3: HandlerMapping
                String step3 = "HandlerMapping (Found /api/orders) -> ";
                // Step 4: HandlerInterceptor preHandle
                String step4 = "Interceptor.preHandle() -> ";
                // Step 5: HandlerAdapter & Controller execution
                String step5 = "HandlerAdapter -> OrderController.getOrder() -> ";
                // Step 6: HttpMessageConverter JSON serialization
                String step6 = "HttpMessageConverter (Jackson JSON) -> 200 OK Response";
                return step1 + step2 + step3 + step4 + step5 + step6;
            }
        }
        String pipeline = new DispatcherServletSimulator().processRequest("GET", "/api/orders/101");
        System.out.println("Ex 24.1 - End-to-End Spring MVC Flow: " + pipeline);

        // Ex 24.2: HandlerMapping vs HandlerAdapter role separation
        System.out.println("Ex 24.2 - HandlerMapping: Identifies WHICH controller handles request | HandlerAdapter: Handles HOW to invoke it");

        // Ex 24.3: HttpMessageConverter Content-Negotiation (Jackson ObjectMapper)
        class JacksonConverterSimulator {
            public String convertToJson(Object obj) {
                return "{\"type\":\"" + obj.getClass().getSimpleName() + "\",\"timestamp\":\"" + Instant.now() + "\"}";
            }
        }
        System.out.println("Ex 24.3 - HttpMessageConverter payload serialization: " + new JacksonConverterSimulator().convertToJson(new Object()));

        // Ex 24.4: ViewResolver (Thymeleaf/JSP) vs @ResponseBody
        System.out.println("Ex 24.4 - ViewResolver resolves logical view name 'index' to '/templates/index.html' (Omitted in @RestController)");

        // Ex 24.5: Async Request Processing (DeferredResult & CompletableFuture)
        System.out.println("Ex 24.5 - Spring MVC Async: Controller returns DeferredResult/CompletableFuture, freeing Tomcat thread during long operations");
    }
}
