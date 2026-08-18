/**
 * ============================================================================
 * JAVA MASTER REFERENCE & INTERVIEW CHEATSHEET (MyJava.java)
 * ============================================================================
 * Comprehensive, production-grade guide covering all essential Java concepts
 * from Core OOP, JVM Internals, Concurrency, Collections, Modern Java 8-21 Features,
 * Functional Programming, to Virtual Threads.
 *
 * Each topic includes:
 *  1. Crisp Interview Definition (What an interviewer wants to hear).
 *  2. Internal Mechanics & JVM Architecture Notes.
 *  3. Common Interview Gotchas, Pitfalls, and Best Practices.
 *  4. 4 to 5 fully functional, runnable, in-depth code examples.
 *
 * Requirements: Java 21 LTS or newer.
 * ============================================================================
 */

import java.io.*;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.lang.ref.Cleaner;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import java.util.concurrent.locks.*;
import java.util.function.*;
import java.util.stream.*;

public class MyJava {

    public static void main(String[] args) throws Exception {
        System.out.println("==================================================================");
        System.out.println("🚀 JAVA 21 MASTER REFERENCE & INTERVIEW NOTES (41 TOPICS)");
        System.out.println("==================================================================\n");

        topic1_PillarsOfOOP();
        topic2_StaticVsFinal();
        topic3_AccessModifiers();
        topic4_InterfaceVsAbstractClass();
        topic5_ClassVsAbstractVsInterface();
        topic6_Multithreading();
        topic7_ThrowVsThrows();
        topic8_RunnableVsCallable();
        topic9_WaysToCreateThread();
        topic10_ThreadPool();
        topic11_ExecutorService();
        topic12_CompletableFutureVsFuture();
        topic13_ForkJoinPool();
        topic14_FailFastVsFailSafe();
        topic15_AtomicVsVolatile();
        topic16_SynchronizedVsLock();
        topic17_CircularDependencies();
        topic18_Deadlock();
        topic19_ComparatorVsComparable();
        topic20_CompareToVsComparing();
        topic21_SerializationVsDeserialization();
        topic22_StringStringBufferStringBuilder();
        topic23_StringPool();
        topic24_ExceptionHierarchy();
        topic25_TryCatchFinallyFinalize();
        topic26_CheckedVsUncheckedExceptions();
        topic27_CustomExceptions();
        topic28_DesignPatterns();
        topic29_JVM_MemoryManagement();
        topic30_ConstructorsInDetail();
        topic31_Generics();
        topic32_CollectionsListSetMap();
        topic33_FunctionalInterfacesAndLambda();
        topic34_BuiltInFunctionalInterfaces();
        topic35_StreamAPI();
        topic36_OptionalAPI();
        topic37_SealedClasses();
        topic38_Records();
        topic39_ConcurrentCollectionsAndPriorityQueue();
        topic40_StreamVsParallelStream();
        topic41_VirtualThreads();

        System.out.println("\n==================================================================");
        System.out.println("🎉 ALL 41 TOPICS WITH RUNNABLE CODE EXAMPLES EXECUTED SUCCESSFULLY!");
        System.out.println("==================================================================");
    }

    // ============================================================================
    // 1. 4 PILLARS OF OOPS
    // ============================================================================
    /**
     * INTERVIEW NOTES - 4 PILLARS OF OOP:
     * 1. Encapsulation: Binding data (state) and methods (behavior) together into a single unit (class),
     *    and restricting direct access to internal state using private variables with public getters/setters.
     *    Benefits: Data hiding, loose coupling, validation control, and maintenance flexibility.
     * 2. Inheritance: Mechanism where a child class acquires properties and behaviors of a parent class using 'extends'.
     *    Promotes code reusability (IS-A relationship). Java supports single inheritance of classes (to avoid the Diamond Problem)
     *    and multiple inheritance of behavior via interfaces.
     * 3. Polymorphism: "Many forms".
     *    - Compile-Time (Static Binding): Method Overloading (same name, different parameter type/count/order in same class).
     *    - Runtime (Dynamic Binding): Method Overriding (subclass provides specific implementation of parent method; resolved via vtable).
     * 4. Abstraction: Hiding internal complexity and showing only essential features to the user.
     *    Achieved via Abstract Classes (0-100% abstraction) and Interfaces (100% contract abstraction).
     *
     * INTERVIEW GOTCHA:
     * - Can you overload a method by changing only the return type? NO, compiler error because the method call is ambiguous.
     * - Can private or static methods be overridden? NO. Private methods are not visible; static methods undergo Method Hiding, not overriding.
     */
    static void topic1_PillarsOfOOP() {
        System.out.println("\n--- 1. 4 PILLARS OF OOPS ---");

        // Ex 1.1: Encapsulation (Data hiding & validation in setters)
        class BankAccount {
            private String accountNumber;
            private double balance;

            public BankAccount(String accNo, double initialBalance) {
                this.accountNumber = accNo;
                if (initialBalance >= 0) this.balance = initialBalance;
            }

            public void deposit(double amount) {
                if (amount > 0) balance += amount;
            }

            public double getBalance() { return balance; }
            public String getAccountNumber() { return accountNumber; }
        }
        BankAccount acc = new BankAccount("ACC-9871", 500.0);
        acc.deposit(150.0);
        System.out.println("Ex 1.1 - Encapsulation (Validated Balance): $" + acc.getBalance());

        // Ex 1.2: Inheritance (Hierarchy and code reuse with super)
        class Vehicle {
            protected String brand;
            public Vehicle(String brand) { this.brand = brand; }
            public String getDetails() { return "Vehicle brand: " + brand; }
        }
        class ElectricCar extends Vehicle {
            private int batteryKwh;
            public ElectricCar(String brand, int batteryKwh) {
                super(brand);
                this.batteryKwh = batteryKwh;
            }
            @Override
            public String getDetails() {
                return super.getDetails() + ", Battery: " + batteryKwh + "kWh";
            }
        }
        Vehicle tesla = new ElectricCar("Tesla", 82);
        System.out.println("Ex 1.2 - Inheritance: " + tesla.getDetails());

        // Ex 1.3: Polymorphism - Compile-Time (Method Overloading)
        class Calculator {
            public int add(int a, int b) { return a + b; }
            public double add(double a, double b) { return a + b; }
            public int add(int a, int b, int c) { return a + b + c; }
        }
        Calculator calc = new Calculator();
        System.out.println("Ex 1.3 - Compile-time Polymorphism (Overloading): " +
                calc.add(2, 3) + ", " + calc.add(2.5, 3.5) + ", " + calc.add(1, 2, 3));

        // Ex 1.4: Polymorphism - Runtime (Dynamic Method Dispatch)
        abstract class Shape {
            public abstract double area();
        }
        class Circle extends Shape {
            private double radius;
            public Circle(double r) { this.radius = r; }
            @Override public double area() { return Math.PI * radius * radius; }
        }
        class Rectangle extends Shape {
            private double w, h;
            public Rectangle(double w, double h) { this.w = w; this.h = h; }
            @Override public double area() { return w * h; }
        }
        List<Shape> shapes = List.of(new Circle(2.0), new Rectangle(3.0, 4.0));
        List<Double> areas = shapes.stream().map(Shape::area).map(a -> Math.round(a * 100.0) / 100.0).toList();
        System.out.println("Ex 1.4 - Runtime Polymorphism (Dynamic dispatch areas): " + areas);

        // Ex 1.5: Abstraction (Abstract Class with Template Method Pattern)
        abstract class PaymentProcessor {
            public final String processPayment(double amount) {
                validate(amount);
                return executeTransaction(amount);
            }
            private void validate(double amount) {
                if (amount <= 0) throw new IllegalArgumentException("Invalid amount");
            }
            protected abstract String executeTransaction(double amount);
        }
        PaymentProcessor pp = new PaymentProcessor() {
            @Override
            protected String executeTransaction(double amount) {
                return "Processed $" + amount + " via Stripe Gateway";
            }
        };
        System.out.println("Ex 1.5 - Abstraction (Template Method): " + pp.processPayment(100.0));
    }

    // ============================================================================
    // 2. STATIC VS FINAL
    // ============================================================================
    /**
     * INTERVIEW NOTES - STATIC VS FINAL:
     * 1. 'static':
     *    - Variable: Stored in Metaspace/Class-level memory. Exactly ONE copy shared among all instances.
     *    - Method: Belongs to the class, invoked without creating an instance. Cannot access instance fields ('this' or 'super').
     *    - Block: Executed once when class is loaded into memory by the ClassLoader.
     * 2. 'final':
     *    - Variable: Value cannot be reassigned once initialized. (For objects, reference is fixed; internal contents CAN mutate).
     *    - Method: Cannot be overridden by subclasses (allows JIT compiler inlining optimization).
     *    - Class: Cannot be inherited (e.g. String, Integer, System). Prevents modification of core behaviors.
     * 3. 'static final':
     *    - Constant variables. Evaluated at compile-time and inlined directly into bytecode (Constant Folding).
     *
     * INTERVIEW GOTCHA:
     * - Does a final reference make an object immutable? NO! 'final List<String> list' prevents 'list = new ArrayList<>()',
     *   but 'list.add("abc")' is completely valid.
     */
    static void topic2_StaticVsFinal() {
        System.out.println("\n--- 2. STATIC VS FINAL ---");

        // Ex 2.1: static variable (shared across all instances)
        class Counter {
            public static int globalCount = 0;
            public int instanceCount = 0;
            public Counter() { globalCount++; instanceCount++; }
        }
        new Counter(); new Counter(); new Counter();
        System.out.println("Ex 2.1 - static shared count: " + Counter.globalCount);

        // Ex 2.2: static method (Utility method, no 'this' context)
        class MathUtils {
            public static int clamp(int val, int min, int max) {
                return Math.max(min, Math.min(max, val));
            }
        }
        System.out.println("Ex 2.2 - static utility method clamp(150, 0, 100): " + MathUtils.clamp(150, 0, 100));

        // Ex 2.3: final variable (Cannot be reassigned; reference immutable)
        final List<String> immutableRefList = new ArrayList<>();
        immutableRefList.add("Element 1"); // Allowed: mutating contents
        // immutableRefList = new ArrayList<>(); // Compile Error!
        System.out.println("Ex 2.3 - final reference mutation allowed: " + immutableRefList);

        // Ex 2.4: final method and final class (Prevent overriding & extension)
        final class ImmutablePoint {
            private final int x, y;
            public ImmutablePoint(int x, int y) { this.x = x; this.y = y; }
            public final int getX() { return x; }
            public final int getY() { return y; }
        }
        ImmutablePoint pt = new ImmutablePoint(10, 20);
        System.out.println("Ex 2.4 - final class instance: (" + pt.getX() + ", " + pt.getY() + ")");

        // Ex 2.5: static final constants (Compile-time constant folding)
        class ConfigConstants {
            public static final String APP_NAME = "MyJavaEnterprise";
            public static final int MAX_CONNECTIONS = 500;
        }
        System.out.println("Ex 2.5 - static final constant: " + ConfigConstants.APP_NAME + " [Max=" + ConfigConstants.MAX_CONNECTIONS + "]");
    }

    // ============================================================================
    // 3. ACCESS MODIFIERS
    // ============================================================================
    /**
     * INTERVIEW NOTES - ACCESS MODIFIERS:
     * Visibility Levels (From most restrictive to least restrictive):
     * 1. private: Accessible ONLY within the same class.
     * 2. default (package-private): No keyword. Accessible within the SAME package only.
     * 3. protected: Accessible within the SAME package + Subclasses in ANY package.
     * 4. public: Accessible EVERYWHERE across the entire application and external modules.
     *
     * OVERRIDING RULE (Liskov Substitution Principle):
     * - An overriding method in a subclass CANNOT reduce visibility (e.g. protected -> private is ILLEGAL;
     *   protected -> public is LEGAL).
     */
    static void topic3_AccessModifiers() {
        System.out.println("\n--- 3. ACCESS MODIFIERS ---");

        // Ex 3.1: private modifier (Restricted to enclosing class)
        class Vault {
            private String secretKey = "VAULT_SECRET_999";
            public boolean unlock(String key) { return secretKey.equals(key); }
        }
        Vault vault = new Vault();
        System.out.println("Ex 3.1 - private member access via public method: " + vault.unlock("VAULT_SECRET_999"));

        // Ex 3.2: default (Package-Private) modifier
        class PackageLevelService {
            String serviceName = "PackageScopedService"; // default access
        }
        PackageLevelService pkgSvc = new PackageLevelService();
        System.out.println("Ex 3.2 - default package-private visibility: " + pkgSvc.serviceName);

        // Ex 3.3: protected modifier (Accessible in package & subclasses)
        class BaseEntity {
            protected Instant createdAt = Instant.now();
        }
        class UserEntity extends BaseEntity {
            public Instant getCreationDate() { return this.createdAt; }
        }
        System.out.println("Ex 3.3 - protected member inherited: " + new UserEntity().getCreationDate() != null);

        // Ex 3.4: public modifier (Accessible everywhere)
        class ApiEndpoint {
            public String getStatus() { return "HTTP 200 OK"; }
        }
        System.out.println("Ex 3.4 - public API endpoint: " + new ApiEndpoint().getStatus());

        // Ex 3.5: Modifier visibility summary demonstration
        Map<String, String> accessTable = Map.of(
                "private", "Same Class Only",
                "default", "Same Class + Same Package",
                "protected", "Same Class + Same Package + Subclasses (Any package)",
                "public", "Everywhere in Application"
        );
        System.out.println("Ex 3.5 - Access matrix levels defined: " + accessTable.keySet());
    }

    // ============================================================================
    // 4. INTERFACE VS ABSTRACT CLASS
    // ============================================================================
    /**
     * INTERVIEW NOTES - INTERFACE VS ABSTRACT CLASS:
     * 1. Abstract Class:
     *    - Represents an IS-A identity (e.g. Dog is an Animal).
     *    - Can maintain state (instance fields) and constructors.
     *    - Can have any access modifier for methods (private, protected, public).
     *    - Single inheritance only ('extends' one class).
     * 2. Interface:
     *    - Represents a CAN-DO capability or role contract (e.g. Flyable, Cloneable, Serializable).
     *    - Fields are implicitly 'public static final' constants. No instance state, no constructors.
     *    - Java 8+: Supports 'default' and 'static' methods with code bodies.
     *    - Java 9+: Supports 'private' methods for code reuse inside default methods.
     *    - Multiple inheritance permitted ('implements A, B, C').
     *
     * DIAMOND PROBLEM RESOLUTION IN INTERFACES:
     * - If class implements InterfaceA and InterfaceB both having default method 'foo()',
     *   the implementing class MUST override 'foo()' and explicitly invoke 'InterfaceA.super.foo()'.
     */
    static void topic4_InterfaceVsAbstractClass() {
        System.out.println("\n--- 4. INTERFACE VS ABSTRACT CLASS ---");

        // Ex 4.1: Abstract Class maintaining instance state & constructors
        abstract class Worker {
            protected String name;
            public Worker(String name) { this.name = name; }
            public String getName() { return name; }
            public abstract void doWork();
        }
        Worker w = new Worker("Dev Alex") {
            @Override public void doWork() { System.out.println("Alex writing code"); }
        };
        System.out.println("Ex 4.1 - Abstract class with state & constructor: " + w.getName());

        // Ex 4.2: Interface enabling multiple inheritance of type/behavior
        interface Printable { default String print() { return "Printing document"; } }
        interface Auditable { default String audit() { return "Auditing log"; } }
        class Report implements Printable, Auditable {}
        Report r = new Report();
        System.out.println("Ex 4.2 - Interface multiple inheritance: " + r.print() + " & " + r.audit());

        // Ex 4.3: Interface default & static methods
        interface MathService {
            static double pi() { return 3.14159; }
            default double square(double x) { return x * x; }
        }
        class StandardMath implements MathService {}
        System.out.println("Ex 4.3 - Interface static: " + MathService.pi() + ", default: " + new StandardMath().square(5));

        // Ex 4.4: Interface private helper methods (Java 9+)
        interface Logger {
            private String format(String level, String msg) {
                return "[" + level + "] " + msg;
            }
            default String info(String msg) { return format("INFO", msg); }
            default String error(String msg) { return format("ERROR", msg); }
        }
        Logger logger = new Logger() {};
        System.out.println("Ex 4.4 - Interface private helper: " + logger.info("System healthy"));

        // Ex 4.5: Multiple default methods diamond problem resolution
        interface Left { default String act() { return "Left"; } }
        interface Right { default String act() { return "Right"; } }
        class Resolved implements Left, Right {
            @Override public String act() { return Left.super.act() + "-" + Right.super.act(); }
        }
        System.out.println("Ex 4.5 - Diamond problem resolution: " + new Resolved().act());
    }

    // ============================================================================
    // 5. CLASS VS ABSTRACT VS INTERFACE
    // ============================================================================
    /**
     * INTERVIEW NOTES - CLASS VS ABSTRACT VS INTERFACE:
     * - Concrete Class: Full implementation, directly instantiable via 'new'.
     * - Abstract Class: Incomplete template, cannot be instantiated directly with 'new'.
     * - Interface: Pure capability contract, functional programming integration via Lambdas.
     * - Pattern Matching for instanceof (Java 16+): Eliminates boilerplate casting '(String) obj'.
     */
    static void topic5_ClassVsAbstractVsInterface() {
        System.out.println("\n--- 5. CLASS VS ABSTRACT VS INTERFACE ---");

        // Ex 5.1: Concrete Class (Fully implemented, directly instantiable)
        class ConcreteService {
            public String execute() { return "Concrete execution"; }
        }
        System.out.println("Ex 5.1 - Concrete Class instance: " + new ConcreteService().execute());

        // Ex 5.2: Abstract Class (Partial implementation, cannot be instantiated with 'new')
        abstract class BaseRepository {
            public void connect() { /* Connect DB */ }
            public abstract Object findById(long id);
        }
        System.out.println("Ex 5.2 - Abstract Class provides blueprint structure");

        // Ex 5.3: Interface (Pure contract / API declaration)
        interface MessageSender {
            void send(String to, String message);
        }
        MessageSender emailSender = (to, msg) -> {};
        System.out.println("Ex 5.3 - Interface implemented as Lambda: " + (emailSender != null));

        // Ex 5.4: Hybrid hierarchy: Concrete class extending Abstract class & implementing Interface
        abstract class BaseEntity { long id = 100L; }
        interface Auditable { String getAuditLog(); }
        class OrderEntity extends BaseEntity implements Auditable {
            @Override public String getAuditLog() { return "Order ID: " + id + " audited"; }
        }
        System.out.println("Ex 5.4 - Hybrid hierarchy: " + new OrderEntity().getAuditLog());

        // Ex 5.5: Pattern Matching with instanceof (Java 16+)
        Object obj = "Hello Java 21";
        if (obj instanceof String s && s.startsWith("Hello")) {
            System.out.println("Ex 5.5 - Pattern matching instanceof: length=" + s.length());
        }
    }

    // ============================================================================
    // 6. MULTITHREADING
    // ============================================================================
    /**
     * INTERVIEW NOTES - MULTITHREADING BASICS:
     * - 6 Thread States (Thread.State):
     *   1. NEW: Created but start() not called.
     *   2. RUNNABLE: Executing in JVM (may be waiting for OS CPU scheduling).
     *   3. BLOCKED: Waiting to acquire a synchronized monitor lock.
     *   4. WAITING: Waiting indefinitely for another thread (e.g. wait(), join(), LockSupport.park()).
     *   5. TIMED_WAITING: Waiting for a specified timeout (e.g. sleep(ms), wait(ms), join(ms)).
     *   6. TERMINATED: Run method completed or uncaught exception.
     *
     * INTERVIEW GOTCHA:
     * - Why must wait() and notify() be called inside a synchronized block?
     *   Because they require ownership of the object's Monitor Lock to avoid Race Conditions (Lost Wake-Up Problem).
     * - Difference between sleep() and wait():
     *   sleep() does NOT release monitor lock; wait() RELEASES the monitor lock.
     */
    static void topic6_Multithreading() throws Exception {
        System.out.println("\n--- 6. MULTITHREADING ---");

        // Ex 6.1: Basic Thread execution & name
        Thread t1 = new Thread(() -> {
            // Background task
        }, "Worker-Thread-1");
        t1.start();
        t1.join();
        System.out.println("Ex 6.1 - Thread created & finished: " + t1.getName());

        // Ex 6.2: Thread Lifecycle States
        Thread stateThread = new Thread(() -> {
            try { Thread.sleep(50); } catch (InterruptedException ignored) {}
        });
        System.out.println("Ex 6.2 - State after instantiation: " + stateThread.getState()); // NEW
        stateThread.start();
        System.out.println("Ex 6.2 - State after start: " + stateThread.getState()); // RUNNABLE
        stateThread.join();
        System.out.println("Ex 6.2 - State after completion: " + stateThread.getState()); // TERMINATED

        // Ex 6.3: Thread.join() for sequential synchronization
        List<String> collected = new CopyOnWriteArrayList<>();
        Thread workerA = new Thread(() -> collected.add("Step 1"));
        Thread workerB = new Thread(() -> collected.add("Step 2"));
        workerA.start(); workerA.join();
        workerB.start(); workerB.join();
        System.out.println("Ex 6.3 - join() coordinated order: " + collected);

        // Ex 6.4: Thread Interruption
        Thread interruptibleThread = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt(); // Restore interrupted status
                    break;
                }
            }
        });
        interruptibleThread.start();
        interruptibleThread.interrupt();
        interruptibleThread.join();
        System.out.println("Ex 6.4 - Thread cleanly stopped upon interrupt flag");

        // Ex 6.5: wait() and notifyAll() Producer-Consumer
        class MessageQueue {
            private String message;
            private boolean hasMessage = false;

            public synchronized void put(String msg) throws InterruptedException {
                while (hasMessage) wait();
                this.message = msg;
                hasMessage = true;
                notifyAll();
            }

            public synchronized String take() throws InterruptedException {
                while (!hasMessage) wait();
                hasMessage = false;
                notifyAll();
                return message;
            }
        }
        MessageQueue queue = new MessageQueue();
        Thread producer = new Thread(() -> {
            try { queue.put("Data-Payload-101"); } catch (InterruptedException ignored) {}
        });
        producer.start();
        String received = queue.take();
        producer.join();
        System.out.println("Ex 6.5 - wait/notify consumer received: " + received);
    }

    // ============================================================================
    // 7. THROW VS THROWS
    // ============================================================================
    /**
     * INTERVIEW NOTES - THROW VS THROWS:
     * - 'throw': Keyword used to explicitly trigger and throw an exception object inside a method body.
     * - 'throws': Keyword in method declaration signature declaring that this method may propagate checked exceptions.
     * - Exception Chaining: 'throw new CustomException("msg", cause)' preserves root cause in stack trace.
     */
    static void topic7_ThrowVsThrows() {
        System.out.println("\n--- 7. THROW VS THROWS ---");

        // Ex 7.1: 'throw' explicitly creating and throwing an exception instance
        try {
            int age = -5;
            if (age < 0) throw new IllegalArgumentException("Age cannot be negative: " + age);
        } catch (IllegalArgumentException e) {
            System.out.println("Ex 7.1 - 'throw' caught: " + e.getMessage());
        }

        // Ex 7.2: 'throws' declaring checked exception on method signature
        class FileService {
            public void readFile(String path) throws IOException {
                if (path == null) throw new IOException("Invalid file path");
            }
        }
        try {
            new FileService().readFile(null);
        } catch (IOException e) {
            System.out.println("Ex 7.2 - 'throws' in method signature caught: " + e.getMessage());
        }

        // Ex 7.3: Exception re-throwing
        try {
            try {
                throw new NullPointerException("Inner null error");
            } catch (NullPointerException npe) {
                throw new IllegalStateException("Outer wrapped error", npe);
            }
        } catch (IllegalStateException ise) {
            System.out.println("Ex 7.3 - Re-thrown exception with cause: " + ise.getCause().getMessage());
        }

        // Ex 7.4: Fast-fail validation helper with throw
        class Validator {
            public static <T> T requireNonNull(T obj, String message) {
                if (obj == null) throw new NullPointerException(message);
                return obj;
            }
        }
        String validated = Validator.requireNonNull("Valid Data", "Must not be null");
        System.out.println("Ex 7.4 - Fast-fail validator returned: " + validated);

        // Ex 7.5: Exception wrapping pattern (Checked -> Runtime)
        try {
            try {
                throw new ParseException("Invalid date format", 0);
            } catch (ParseException pe) {
                throw new RuntimeException("Unchecked translation", pe);
            }
        } catch (RuntimeException re) {
            System.out.println("Ex 7.5 - Checked wrapped in RuntimeException: " + re.getMessage());
        }
    }

    // ============================================================================
    // 8. RUNNABLE VS CALLABLE
    // ============================================================================
    /**
     * INTERVIEW NOTES - RUNNABLE VS CALLABLE:
     * - Runnable: 'public void run()', since Java 1.0. Cannot return a value, cannot throw checked exceptions.
     * - Callable<V>: 'public V call() throws Exception', since Java 5. Returns value of generic type V, can throw checked exceptions.
     * - FutureTask<V>: Bridges Callable to Thread (implements RunnableFuture = Runnable + Future).
     */
    static void topic8_RunnableVsCallable() throws Exception {
        System.out.println("\n--- 8. RUNNABLE VS CALLABLE ---");

        // Ex 8.1: Runnable (void return, cannot throw checked exceptions)
        Runnable runnableTask = () -> System.out.println("Ex 8.1 - Runnable executed (no return value)");
        runnableTask.run();

        // Ex 8.2: Callable<T> (returns value, can throw checked exceptions)
        Callable<Integer> callableTask = () -> {
            return 42 * 2;
        };
        System.out.println("Ex 8.2 - Callable direct call returned: " + callableTask.call());

        // Ex 8.3: FutureTask wrapping Callable for standalone thread execution
        FutureTask<String> futureTask = new FutureTask<>(() -> "FutureTask Result");
        new Thread(futureTask).start();
        System.out.println("Ex 8.3 - FutureTask.get(): " + futureTask.get());

        // Ex 8.4: ExecutorService submitting Runnable vs Callable
        try (ExecutorService pool = Executors.newFixedThreadPool(2)) {
            Future<?> runFuture = pool.submit(() -> {}); // returns null on get()
            Future<String> callFuture = pool.submit(() -> "Callable in Executor");
            System.out.println("Ex 8.4 - Runnable Future.get(): " + runFuture.get() + " | Callable Future.get(): " + callFuture.get());
        }

        // Ex 8.5: Exception propagation in Callable Future
        try (ExecutorService pool = Executors.newSingleThreadExecutor()) {
            Future<String> failedFuture = pool.submit(() -> {
                throw new IOException("Remote API unreachable");
            });
            try {
                failedFuture.get();
            } catch (ExecutionException e) {
                System.out.println("Ex 8.5 - Callable exception wrapped in ExecutionException: " + e.getCause().getClass().getSimpleName());
            }
        }
    }

    // ============================================================================
    // 9. HOW MANY WAYS TO CREATE A THREAD
    // ============================================================================
    /**
     * INTERVIEW NOTES - WAYS TO CREATE A THREAD:
     * 1. Extending java.lang.Thread class.
     * 2. Implementing java.lang.Runnable interface.
     * 3. Implementing java.util.concurrent.Callable<T> wrapped in FutureTask<T>.
     * 4. Anonymous Inner Class or Lambda Expression.
     * 5. ExecutorService Thread Pools.
     * 6. Virtual Threads via Thread.ofVirtual().start() (Java 21).
     *
     * INTERVIEW QUESTION: Why is implementing Runnable preferred over extending Thread?
     * - Java supports single class inheritance; implementing Runnable keeps inheritance open.
     * - Promotes separation of concerns (task logic decoupled from thread lifecycle management).
     * - Compatible with ExecutorService thread pool reuse.
     */
    static void topic9_WaysToCreateThread() throws Exception {
        System.out.println("\n--- 9. WAYS TO CREATE A THREAD ---");

        // Way 1: Extending Thread class
        class MyCustomThread extends Thread {
            public String result;
            @Override public void run() { result = "Way 1: Extends Thread"; }
        }
        MyCustomThread t1 = new MyCustomThread();
        t1.start(); t1.join();
        System.out.println("Ex 9.1 - " + t1.result);

        // Way 2: Implementing Runnable interface
        class MyRunnable implements Runnable {
            public String result;
            @Override public void run() { result = "Way 2: Implements Runnable"; }
        }
        MyRunnable myRunnable = new MyRunnable();
        Thread t2 = new Thread(myRunnable);
        t2.start(); t2.join();
        System.out.println("Ex 9.2 - " + myRunnable.result);

        // Way 3: Implementing Callable with FutureTask
        Callable<String> callable = () -> "Way 3: Callable + FutureTask";
        FutureTask<String> ft = new FutureTask<>(callable);
        new Thread(ft).start();
        System.out.println("Ex 9.3 - " + ft.get());

        // Way 4: Lambda Expression / Anonymous Runnable
        Thread t4 = new Thread(() -> {
            // Lambda execution
        }, "Lambda-Thread");
        t4.start(); t4.join();
        System.out.println("Ex 9.4 - Way 4: Thread with Lambda syntax (" + t4.getName() + ")");

        // Way 5: Virtual Threads (Java 21 Project Loom)
        Thread vThread = Thread.ofVirtual().name("Virtual-Worker").start(() -> {
            // Lightweight virtual thread execution
        });
        vThread.join();
        System.out.println("Ex 9.5 - Way 5: Virtual Thread (isVirtual=" + vThread.isVirtual() + ")");
    }

    // ============================================================================
    // 10. THREAD POOL
    // ============================================================================
    /**
     * INTERVIEW NOTES - THREAD POOL TYPES:
     * - FixedThreadPool: Fixed number of threads. Uses unbounded LinkedBlockingQueue (Risks OOM under high load).
     * - CachedThreadPool: Creates new threads as needed, terminates idle threads after 60s. SynchronousQueue (Risks CPU saturation).
     * - SingleThreadExecutor: Exactly 1 thread executing tasks sequentially in FIFO order.
     * - ScheduledThreadPoolExecutor: Periodic and delayed task execution (cron-like timers).
     * - WorkStealingPool (Java 8+): ForkJoinPool utilizing all CPU cores with work-stealing algorithm.
     *
     * PRODUCTION BEST PRACTICE:
     * - Do NOT use Executors factory methods in production! Instantiate ThreadPoolExecutor directly with bounded ArrayBlockingQueue
     *   and custom RejectedExecutionHandler.
     */
    static void topic10_ThreadPool() throws Exception {
        System.out.println("\n--- 10. THREAD POOL ---");

        // Ex 10.1: FixedThreadPool (Bounded thread count)
        try (ExecutorService fixedPool = Executors.newFixedThreadPool(3)) {
            List<Future<Integer>> futures = new ArrayList<>();
            for (int i = 1; i <= 3; i++) {
                final int taskId = i;
                futures.add(fixedPool.submit(() -> taskId * 10));
            }
            List<Integer> results = new ArrayList<>();
            for (var f : futures) results.add(f.get());
            System.out.println("Ex 10.1 - FixedThreadPool results: " + results);
        }

        // Ex 10.2: CachedThreadPool (Elastic, reuses threads or spawns new ones)
        try (ExecutorService cachedPool = Executors.newCachedThreadPool()) {
            Future<String> f = cachedPool.submit(() -> "CachedThreadPool Task Executed");
            System.out.println("Ex 10.2 - " + f.get());
        }

        // Ex 10.3: SingleThreadExecutor (Guaranteed sequential execution order)
        try (ExecutorService singlePool = Executors.newSingleThreadExecutor()) {
            List<Integer> sequence = new CopyOnWriteArrayList<>();
            singlePool.submit(() -> sequence.add(1));
            singlePool.submit(() -> sequence.add(2));
            singlePool.submit(() -> sequence.add(3)).get();
            System.out.println("Ex 10.3 - SingleThreadExecutor sequential order: " + sequence);
        }

        // Ex 10.4: ScheduledThreadPoolExecutor (Delayed & Periodic tasks)
        ScheduledExecutorService scheduledPool = Executors.newScheduledThreadPool(1);
        ScheduledFuture<String> scheduledFuture = scheduledPool.schedule(() -> "Scheduled Task Done", 20, TimeUnit.MILLISECONDS);
        System.out.println("Ex 10.4 - ScheduledExecutor: " + scheduledFuture.get());
        scheduledPool.shutdown();

        // Ex 10.5: WorkStealingPool (ForkJoin based parallelism)
        try (ExecutorService workStealingPool = Executors.newWorkStealingPool()) {
            Future<String> f = workStealingPool.submit(() -> "WorkStealingPool finished");
            System.out.println("Ex 10.5 - " + f.get());
        }
    }

    // ============================================================================
    // 11. EXECUTORSERVICES
    // ============================================================================
    /**
     * INTERVIEW NOTES - EXECUTORSERVICES:
     * - execute(Runnable) vs submit(Callable/Runnable):
     *   execute() returns void, throws exceptions directly to UncaughtExceptionHandler.
     *   submit() returns Future<T>, captures exceptions internally and rethrows wrapped in ExecutionException on get().
     * - Shutdown Flow:
     *   1. shutdown(): Stops accepting new tasks, finishes queued tasks.
     *   2. awaitTermination(timeout, unit): Waits for running tasks to conclude.
     *   3. shutdownNow(): Attempts to cancel active tasks via Thread.interrupt() and returns unexecuted tasks.
     */
    static void topic11_ExecutorService() throws Exception {
        System.out.println("\n--- 11. EXECUTORSERVICES ---");

        // Ex 11.1: submit() vs execute()
        try (ExecutorService executor = Executors.newFixedThreadPool(2)) {
            executor.execute(() -> {});
            Future<String> f = executor.submit(() -> "submit() with Future return");
            System.out.println("Ex 11.1 - " + f.get());
        }

        // Ex 11.2: invokeAll() (Runs all tasks and blocks until all complete)
        try (ExecutorService executor = Executors.newFixedThreadPool(3)) {
            List<Callable<String>> tasks = List.of(
                    () -> "Task 1", () -> "Task 2", () -> "Task 3"
            );
            List<Future<String>> results = executor.invokeAll(tasks);
            List<String> taskOutputs = new ArrayList<>();
            for (var res : results) taskOutputs.add(res.get());
            System.out.println("Ex 11.2 - invokeAll() results: " + taskOutputs);
        }

        // Ex 11.3: invokeAny() (Returns result of the fastest successful task)
        try (ExecutorService executor = Executors.newFixedThreadPool(3)) {
            List<Callable<String>> racers = List.of(
                    () -> { Thread.sleep(50); return "Slow"; },
                    () -> { Thread.sleep(5); return "Fast Winner"; }
            );
            String winner = executor.invokeAny(racers);
            System.out.println("Ex 11.3 - invokeAny() winner: " + winner);
        }

        // Ex 11.4: Graceful shutdown pattern
        ExecutorService shutdownPool = Executors.newFixedThreadPool(2);
        shutdownPool.submit(() -> "work");
        shutdownPool.shutdown();
        boolean terminated = shutdownPool.awaitTermination(1, TimeUnit.SECONDS);
        System.out.println("Ex 11.4 - Graceful shutdown terminated cleanly: " + terminated);

        // Ex 11.5: Custom ThreadPoolExecutor with Bounded Queue & Rejection Policy
        ThreadPoolExecutor customExecutor = new ThreadPoolExecutor(
                2, 4, 60L, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(10),
                new ThreadPoolExecutor.CallerRunsPolicy()
        );
        customExecutor.submit(() -> {});
        System.out.println("Ex 11.5 - Custom ThreadPoolExecutor active: PoolSize=" + customExecutor.getPoolSize());
        customExecutor.shutdown();
    }

    // ============================================================================
    // 12. COMPLETABLE FUTURE VS FUTURE
    // ============================================================================
    /**
     * INTERVIEW NOTES - COMPLETABLEFUTURE VS FUTURE:
     * - Legacy Future: Blocking get(), cannot be manually completed, cannot chain callbacks, cannot handle errors fluently.
     * - CompletableFuture (Java 8+): Non-blocking reactive promises.
     *   - Transformations: thenApply (sync map), thenApplyAsync (async map on ForkJoinPool).
     *   - Composition: thenCompose (monadic flatMap dependent futures), thenCombine (BiFunction combining 2 independent futures).
     *   - Error handling: exceptionally (fallback), handle (bi-function result + throwable).
     *   - Combinators: allOf (parallel batch), anyOf (fastest responder).
     */
    static void topic12_CompletableFutureVsFuture() throws Exception {
        System.out.println("\n--- 12. COMPLETABLE FUTURE VS FUTURE ---");

        // Ex 12.1: Legacy Future blocking limitation vs CompletableFuture non-blocking
        CompletableFuture<String> cf = CompletableFuture.supplyAsync(() -> "Hello")
                .thenApply(s -> s + " World")
                .thenApply(String::toUpperCase);
        System.out.println("Ex 12.1 - CompletableFuture fluent pipeline: " + cf.get());

        // Ex 12.2: thenCompose (Monadic chaining dependent async tasks)
        CompletableFuture<String> userFuture = CompletableFuture.supplyAsync(() -> "User_101")
                .thenCompose(userId -> CompletableFuture.supplyAsync(() -> "Profile for " + userId));
        System.out.println("Ex 12.2 - thenCompose result: " + userFuture.get());

        // Ex 12.3: thenCombine (Combining two independent async futures with BiFunction)
        CompletableFuture<Double> priceFuture = CompletableFuture.supplyAsync(() -> 100.0);
        CompletableFuture<Double> taxRateFuture = CompletableFuture.supplyAsync(() -> 0.08);
        CompletableFuture<Double> totalFuture = priceFuture.thenCombine(taxRateFuture, (price, tax) -> price * (1 + tax));
        System.out.println("Ex 12.3 - thenCombine calculated total: $" + totalFuture.get());

        // Ex 12.4: CompletableFuture.allOf() & anyOf() combinators
        CompletableFuture<String> f1 = CompletableFuture.supplyAsync(() -> "Service A");
        CompletableFuture<String> f2 = CompletableFuture.supplyAsync(() -> "Service B");
        CompletableFuture<Void> all = CompletableFuture.allOf(f1, f2);
        all.join();
        System.out.println("Ex 12.4 - allOf completed for: " + f1.join() + " & " + f2.join());

        // Ex 12.5: Exception handling via exceptionally() and handle()
        CompletableFuture<String> fallbackFuture = CompletableFuture.<String>supplyAsync(() -> {
            if (true) throw new RuntimeException("API Down");
            return "Real Data";
        }).exceptionally(ex -> "Fallback Cached Data (" + ex.getMessage() + ")");
        System.out.println("Ex 12.5 - exceptionally() recovery: " + fallbackFuture.get());
    }

    // ============================================================================
    // 13. FORKJOINPOOL
    // ============================================================================
    /**
     * INTERVIEW NOTES - FORKJOINPOOL & WORK-STEALING:
     * - Designed for CPU-intensive divide-and-conquer parallel tasks.
     * - Work-Stealing Algorithm: Each worker thread has its own double-ended queue (Deque). If a thread finishes its work,
     *   it steals subtasks from the tail of another busy thread's deque, minimizing thread idle time.
     * - RecursiveTask<V> (returns value) vs RecursiveAction (returns void).
     * - ForkJoinPool.commonPool() is used by Java Parallel Streams by default.
     */
    static void topic13_ForkJoinPool() {
        System.out.println("\n--- 13. FORKJOINPOOL ---");

        // Ex 13.1: RecursiveTask<V> (Parallel Divide and Conquer Array Sum)
        class ArraySumTask extends RecursiveTask<Long> {
            private final long[] array;
            private final int start, end;
            private static final int THRESHOLD = 1000;

            public ArraySumTask(long[] arr, int s, int e) {
                this.array = arr; this.start = s; this.end = e;
            }

            @Override
            protected Long compute() {
                if (end - start <= THRESHOLD) {
                    long sum = 0;
                    for (int i = start; i < end; i++) sum += array[i];
                    return sum;
                }
                int mid = (start + end) / 2;
                ArraySumTask left = new ArraySumTask(array, start, mid);
                ArraySumTask right = new ArraySumTask(array, mid, end);
                left.fork();
                long rightAns = right.compute();
                long leftAns = left.join();
                return leftAns + rightAns;
            }
        }
        long[] data = new long[5000];
        Arrays.fill(data, 1);
        ForkJoinPool fjPool = new ForkJoinPool(4);
        long totalSum = fjPool.invoke(new ArraySumTask(data, 0, data.length));
        System.out.println("Ex 13.1 - RecursiveTask parallel array sum: " + totalSum);

        // Ex 13.2: RecursiveAction (In-place transformation without return)
        class IncrementAction extends RecursiveAction {
            private final int[] arr;
            private final int start, end;
            public IncrementAction(int[] arr, int s, int e) { this.arr = arr; this.start = s; this.end = e; }
            @Override
            protected void compute() {
                if (end - start < 10) {
                    for (int i = start; i < end; i++) arr[i] += 10;
                } else {
                    int mid = (start + end) / 2;
                    invokeAll(new IncrementAction(arr, start, mid), new IncrementAction(arr, mid, end));
                }
            }
        }
        int[] numbers = {1, 2, 3, 4, 5};
        fjPool.invoke(new IncrementAction(numbers, 0, numbers.length));
        System.out.println("Ex 13.2 - RecursiveAction in-place transform: " + Arrays.toString(numbers));

        // Ex 13.3: Custom Parallelism ForkJoinPool vs Common Pool
        System.out.println("Ex 13.3 - CommonPool parallelism: " + ForkJoinPool.commonPool().getParallelism());

        // Ex 13.4: Work-Stealing algorithm mechanism verification
        System.out.println("Ex 13.4 - ForkJoinPool active thread count: " + fjPool.getActiveThreadCount());

        // Ex 13.5: Shutdown ForkJoinPool
        fjPool.shutdown();
        System.out.println("Ex 13.5 - ForkJoinPool shutdown complete: " + fjPool.isShutdown());
    }

    // ============================================================================
    // 14. FAIL-SAFE VS FAIL-FAST
    // ============================================================================
    /**
     * INTERVIEW NOTES - FAIL-FAST VS FAIL-SAFE:
     * 1. Fail-Fast (ArrayList, HashSet, HashMap):
     *    - Throws ConcurrentModificationException immediately if collection is structurally modified during iteration.
     *    - Mechanism: Maintains an internal 'modCount'. On every next() call, checks 'if (modCount != expectedModCount) throw'.
     * 2. Fail-Safe / Weakly Consistent (CopyOnWriteArrayList, ConcurrentHashMap):
     *    - Operates on a snapshot copy or weakly consistent bucket traversal. Never throws ConcurrentModificationException.
     *    - Cost: CopyOnWriteArrayList makes an O(N) copy on every write/add operation.
     */
    static void topic14_FailFastVsFailSafe() {
        System.out.println("\n--- 14. FAIL-SAFE VS FAIL-FAST ---");

        // Ex 14.1: Fail-Fast in ArrayList (Throws ConcurrentModificationException)
        List<String> fastList = new ArrayList<>(List.of("A", "B", "C"));
        try {
            for (String item : fastList) {
                if (item.equals("B")) fastList.remove(item);
            }
        } catch (ConcurrentModificationException e) {
            System.out.println("Ex 14.1 - Fail-Fast caught: ConcurrentModificationException on ArrayList");
        }

        // Ex 14.2: Safe removal via Iterator.remove() on Fail-Fast collection
        List<String> safeIteratorList = new ArrayList<>(List.of("A", "B", "C"));
        Iterator<String> it = safeIteratorList.iterator();
        while (it.hasNext()) {
            if (it.next().equals("B")) it.remove();
        }
        System.out.println("Ex 14.2 - Iterator.remove() safe list result: " + safeIteratorList);

        // Ex 14.3: Fail-Safe with CopyOnWriteArrayList (Iterates on snapshot copy)
        List<String> cowList = new CopyOnWriteArrayList<>(List.of("X", "Y", "Z"));
        for (String item : cowList) {
            if (item.equals("Y")) cowList.add("W");
        }
        System.out.println("Ex 14.3 - Fail-Safe CopyOnWriteArrayList modified successfully: " + cowList);

        // Ex 14.4: Fail-Safe with ConcurrentHashMap (Weakly consistent iterator)
        Map<String, Integer> concurrentMap = new ConcurrentHashMap<>(Map.of("k1", 1, "k2", 2));
        for (String key : concurrentMap.keySet()) {
            concurrentMap.put("k3", 3);
        }
        System.out.println("Ex 14.4 - Fail-Safe ConcurrentHashMap iteration keys: " + concurrentMap.keySet());

        // Ex 14.5: ModCount internal explanation
        System.out.println("Ex 14.5 - Fail-fast checks internal 'modCount != expectedModCount' on each next() step");
    }

    // ============================================================================
    // 15. ATOMIC VS VOLATILE
    // ============================================================================
    /**
     * INTERVIEW NOTES - ATOMIC VS VOLATILE:
     * 1. 'volatile':
     *    - Guarantees Visibility (reads/writes go directly to RAM, bypassing CPU core L1/L2 caches).
     *    - Guarantees Ordering (prevents compiler and CPU instruction reordering using Memory Barriers).
     *    - DOES NOT guarantee Atomicity for compound actions ('count++' is read-modify-write).
     * 2. Atomic Classes (AtomicInteger, AtomicReference):
     *    - Guarantees Visibility + Atomicity using CPU hardware-level Compare-And-Swap (CAS) instructions.
     *    - Completely lock-free.
     * 3. LongAdder:
     *    - Under ultra-high multithreaded contention, LongAdder stripes count across multiple cells to prevent CAS retry loops.
     */
    static void topic15_AtomicVsVolatile() throws Exception {
        System.out.println("\n--- 15. ATOMIC VS VOLATILE ---");

        // Ex 15.1: volatile visibility (prevents CPU core caching and memory reordering)
        class FlagWorker {
            public volatile boolean running = true;
        }
        FlagWorker fw = new FlagWorker();
        Thread worker = new Thread(() -> {
            while (fw.running) { /* spinning with guaranteed visibility */ }
        });
        worker.start();
        fw.running = false;
        worker.join();
        System.out.println("Ex 15.1 - volatile flag visibility stopped background thread");

        // Ex 15.2: volatile limitation (Compound action count++ is NOT atomic!)
        class VolatileCounter {
            public volatile int count = 0;
            public void increment() { count++; }
        }
        System.out.println("Ex 15.2 - volatile guarantees Visibility, NOT Atomicity for compound operations");

        // Ex 15.3: AtomicInteger (Lock-free hardware CAS - Compare-And-Swap)
        AtomicInteger atomicCount = new AtomicInteger(0);
        try (ExecutorService pool = Executors.newFixedThreadPool(4)) {
            for (int i = 0; i < 1000; i++) {
                pool.submit(atomicCount::incrementAndGet);
            }
        }
        System.out.println("Ex 15.3 - AtomicInteger thread-safe total: " + atomicCount.get());

        // Ex 15.4: AtomicReference (Atomic object state transitions)
        AtomicReference<String> state = new AtomicReference<>("INIT");
        boolean updated = state.compareAndSet("INIT", "RUNNING");
        System.out.println("Ex 15.4 - AtomicReference CAS update (" + updated + "): " + state.get());

        // Ex 15.5: LongAdder (High-contention striped cell counter)
        LongAdder adder = new LongAdder();
        adder.add(50);
        adder.increment();
        System.out.println("Ex 15.5 - LongAdder sum: " + adder.sum());
    }

    // ============================================================================
    // 16. SYNCHRONIZED VS LOCK
    // ============================================================================
    /**
     * INTERVIEW NOTES - SYNCHRONIZED VS REENTRANTLOCK:
     * - synchronized: Implicit monitor lock, managed by JVM bytecode (monitorenter/monitorexit).
     *   Always releases lock on exit or exception. Non-fair only. Cannot test lock or timeout.
     * - ReentrantLock: Explicit lock in java.util.concurrent.locks.
     *   Supports tryLock(timeout), fair/non-fair policies, multiple Conditions (await/signal).
     *   MUST be released in a 'finally' block to prevent deadlocks.
     * - StampedLock: Provides optimistic read mode with validation to eliminate read locks in low-write caches.
     */
    static void topic16_SynchronizedVsLock() throws Exception {
        System.out.println("\n--- 16. SYNCHRONIZED VS LOCK ---");

        // Ex 16.1: synchronized block & method (Intrinsic Monitor Lock)
        class SyncCounter {
            private int count = 0;
            public synchronized void inc() { count++; }
            public int get() { synchronized(this) { return count; } }
        }
        SyncCounter sc = new SyncCounter();
        sc.inc();
        System.out.println("Ex 16.1 - synchronized method count: " + sc.get());

        // Ex 16.2: ReentrantLock (Explicit lock & unlock in finally)
        Lock lock = new ReentrantLock();
        lock.lock();
        try {
            // Critical section
        } finally {
            lock.unlock();
        }
        System.out.println("Ex 16.2 - ReentrantLock acquired and released");

        // Ex 16.3: tryLock with timeout (Prevents thread starvation & deadlock)
        boolean acquired = lock.tryLock(10, TimeUnit.MILLISECONDS);
        if (acquired) {
            try {
                System.out.println("Ex 16.3 - tryLock acquired successfully with timeout");
            } finally {
                lock.unlock();
            }
        }

        // Ex 16.4: ReentrantReadWriteLock (Concurrent readers, exclusive writer)
        ReadWriteLock rwLock = new ReentrantReadWriteLock();
        rwLock.readLock().lock();
        try {
            // Concurrent read
        } finally {
            rwLock.readLock().unlock();
        }
        System.out.println("Ex 16.4 - ReentrantReadWriteLock readLock acquired");

        // Ex 16.5: StampedLock (Optimistic reading mode)
        StampedLock stampedLock = new StampedLock();
        long stamp = stampedLock.tryOptimisticRead();
        if (stampedLock.validate(stamp)) {
            System.out.println("Ex 16.5 - StampedLock optimistic read validated with zero lock contention");
        }
    }

    // ============================================================================
    // 17. CIRCULAR DEPENDENCIES
    // ============================================================================
    /**
     * INTERVIEW NOTES - CIRCULAR DEPENDENCIES:
     * - Occurs when BeanA requires BeanB in its constructor, while BeanB requires BeanA.
     * - Causes StackOverflowError or BeanCurrentlyInCreationException in DI frameworks (e.g. Spring).
     * - Solutions:
     *   1. Setter/Field Injection (Allows partial object instantiation).
     *   2. Lazy Provider via Supplier<T> or @Lazy.
     *   3. Architectural Refactoring: Introduce an Event Bus or extract common logic to ServiceC (Dependency Inversion).
     */
    static void topic17_CircularDependencies() {
        System.out.println("\n--- 17. CIRCULAR DEPENDENCIES ---");

        // Ex 17.1: The Problem - Direct circular references
        System.out.println("Ex 17.1 - Circular Dependency: ClassA needs ClassB in constructor, ClassB needs ClassA");

        // Ex 17.2: Resolution via Setter / Property Injection
        class ServiceA {
            private Object serviceB;
            public void setServiceB(Object b) { this.serviceB = b; }
            public boolean hasB() { return serviceB != null; }
        }
        class ServiceB {
            private ServiceA serviceA;
            public void setServiceA(ServiceA a) { this.serviceA = a; }
        }
        ServiceA a = new ServiceA();
        ServiceB b = new ServiceB();
        a.setServiceB(b);
        b.setServiceA(a);
        System.out.println("Ex 17.2 - Setter injection resolved circular reference: " + a.hasB());

        // Ex 17.3: Resolution via Provider / Lazy Initialization (Supplier<T>)
        class LazyServiceA {
            private final Supplier<Object> bSupplier;
            public LazyServiceA(Supplier<Object> bSupplier) { this.bSupplier = bSupplier; }
            public Object getB() { return bSupplier.get(); }
        }
        LazyServiceA lazyA = new LazyServiceA(() -> "Lazy Service B Instance");
        System.out.println("Ex 17.3 - Lazy Supplier<T> resolution: " + lazyA.getB());

        // Ex 17.4: Resolution via Mediator / Event Bus decoupling
        interface EventBus { void publish(String event); }
        class DecoupledA {
            public void doWork(EventBus bus) { bus.publish("A_COMPLETED"); }
        }
        DecoupledA decoupledA = new DecoupledA();
        decoupledA.doWork(event -> System.out.println("Ex 17.4 - Mediator event received: " + event));

        // Ex 17.5: Architecture best practice
        System.out.println("Ex 17.5 - Architectural fix: Extract shared logic into a common ServiceC (Dependency Inversion)");
    }

    // ============================================================================
    // 18. DEADLOCK
    // ============================================================================
    /**
     * INTERVIEW NOTES - DEADLOCK CONDITIONS & PREVENTION:
     * 4 Coffman Conditions required for Deadlock:
     * 1. Mutual Exclusion (Resource cannot be shared).
     * 2. Hold and Wait (Thread holding a resource waits for another).
     * 3. No Preemption (Resources cannot be forcibly confiscated).
     * 4. Circular Wait (T1 waits for T2, T2 waits for T1).
     *
     * How to break Deadlock:
     * - Break Circular Wait by enforcing a strict Global Lock Ordering.
     * - Break Hold and Wait by using Lock.tryLock() with timeouts.
     * - Detection: ManagementFactory.getThreadMXBean().findDeadlockedThreads().
     */
    static void topic18_Deadlock() throws Exception {
        System.out.println("\n--- 18. DEADLOCK ---");

        // Ex 18.1: Deadlock condition explanation (Mutual Exclusion, Hold and Wait, No Preemption, Circular Wait)
        System.out.println("Ex 18.1 - 4 Coffman Conditions required for Deadlock");

        // Ex 18.2: Deadlock Prevention via Global Lock Ordering (Acquire in fixed order)
        Object lockA = new Object();
        Object lockB = new Object();

        Runnable safeTask1 = () -> {
            synchronized (lockA) {
                synchronized (lockB) {
                    // Safe execution
                }
            }
        };
        Runnable safeTask2 = () -> {
            synchronized (lockA) {
                synchronized (lockB) {
                    // Safe execution
                }
            }
        };
        Thread t1 = new Thread(safeTask1);
        Thread t2 = new Thread(safeTask2);
        t1.start(); t2.start();
        t1.join(); t2.join();
        System.out.println("Ex 18.2 - Lock ordering prevented deadlock");

        // Ex 18.3: Deadlock Avoidance via ReentrantLock.tryLock() with Backoff
        Lock l1 = new ReentrantLock();
        Lock l2 = new ReentrantLock();
        boolean success = false;
        if (l1.tryLock(5, TimeUnit.MILLISECONDS)) {
            try {
                if (l2.tryLock(5, TimeUnit.MILLISECONDS)) {
                    try {
                        success = true;
                    } finally {
                        l2.unlock();
                    }
                }
            } finally {
                l1.unlock();
            }
        }
        System.out.println("Ex 18.3 - tryLock avoided potential deadlock: " + success);

        // Ex 18.4: Programmatic Deadlock Detection using ThreadMXBean
        ThreadMXBean mxBean = ManagementFactory.getThreadMXBean();
        long[] deadlockedThreads = mxBean.findDeadlockedThreads();
        System.out.println("Ex 18.4 - Deadlock detector check: " + (deadlockedThreads == null ? "0 Deadlocks Found" : deadlockedThreads.length + " Deadlocks"));

        // Ex 18.5: Coarse-grained locking alternative
        System.out.println("Ex 18.5 - Prefer high-level Concurrent data structures over nested synchronized blocks");
    }

    // ============================================================================
    // 19. COMPARATOR VS COMPARABLE
    // ============================================================================
    /**
     * INTERVIEW NOTES - COMPARABLE VS COMPARATOR:
     * 1. Comparable<T> (java.lang):
     *    - Defines single Natural Ordering inside the entity class itself via 'compareTo(T o)'.
     *    - Affects Collections.sort(list) and TreeSet.
     * 2. Comparator<T> (java.util):
     *    - Defines multiple Custom External sorting strategies via 'compare(T o1, T o2)'.
     *    - Fluent chaining with thenComparing(), reversed(), nullsFirst().
     */
    static void topic19_ComparatorVsComparable() {
        System.out.println("\n--- 19. COMPARATOR VS COMPARABLE ---");

        // Ex 19.1: Comparable<T> (Natural Ordering inside entity)
        class Employee implements Comparable<Employee> {
            String name; int id;
            public Employee(String name, int id) { this.name = name; this.id = id; }
            @Override public int compareTo(Employee o) { return Integer.compare(this.id, o.id); }
            @Override public String toString() { return name + "(" + id + ")"; }
        }
        List<Employee> emps = new ArrayList<>(List.of(new Employee("Charlie", 103), new Employee("Alice", 101), new Employee("Bob", 102)));
        Collections.sort(emps);
        System.out.println("Ex 19.1 - Comparable natural sort by ID: " + emps);

        // Ex 19.2: Comparator<T> (Custom External Sorting strategy)
        Comparator<Employee> byName = (e1, e2) -> e1.name.compareTo(e2.name);
        emps.sort(byName);
        System.out.println("Ex 19.2 - Comparator sort by Name: " + emps);

        // Ex 19.3: Comparator.reversed()
        emps.sort(byName.reversed());
        System.out.println("Ex 19.3 - Reversed Comparator sort: " + emps);

        // Ex 19.4: Multi-level sorting with thenComparing()
        List<Employee> duplicates = new ArrayList<>(List.of(
                new Employee("Alice", 200),
                new Employee("Alice", 100),
                new Employee("Bob", 150)
        ));
        duplicates.sort(Comparator.comparing((Employee e) -> e.name).thenComparingInt(e -> e.id));
        System.out.println("Ex 19.4 - Chained Comparator (Name -> ID): " + duplicates);

        // Ex 19.5: Handling null values with nullsFirst / nullsLast
        List<String> withNulls = new ArrayList<>(Arrays.asList("Beta", null, "Alpha"));
        withNulls.sort(Comparator.nullsFirst(Comparator.naturalOrder()));
        System.out.println("Ex 19.5 - nullsFirst sorting: " + withNulls);
    }

    // ============================================================================
    // 20. COMPARETO VS COMPARING
    // ============================================================================
    /**
     * INTERVIEW NOTES - COMPARETO VS COMPARING:
     * - compareTo(): Instance method on Comparable objects returning -1, 0, or +1.
     * - Comparator.comparing(): Java 8 static factory method taking a Key Extractor Function (e.g. Person::getName).
     * - Primitive Specializations: comparingInt(), comparingDouble() prevent autoboxing memory allocation.
     */
    static void topic20_CompareToVsComparing() {
        System.out.println("\n--- 20. COMPARETO VS COMPARING ---");

        // Ex 20.1: compareTo primitive and String comparisons
        System.out.println("Ex 20.1 - 'A'.compareTo('B'): " + "A".compareTo("B") + " | Integer.compare(5, 5): " + Integer.compare(5, 5));

        // Ex 20.2: Comparator.comparing key extractor
        class Product {
            String title; double price;
            public Product(String t, double p) { this.title = t; this.price = p; }
            public String getTitle() { return title; }
            public double getPrice() { return price; }
        }
        List<Product> products = new ArrayList<>(List.of(new Product("Laptop", 1200), new Product("Mouse", 25)));
        products.sort(Comparator.comparing(Product::getTitle));
        System.out.println("Ex 20.2 - Comparator.comparing(Product::getTitle): " + products.get(0).getTitle());

        // Ex 20.3: Comparator.comparingDouble (Avoids autoboxing overhead)
        products.sort(Comparator.comparingDouble(Product::getPrice));
        System.out.println("Ex 20.3 - comparingDouble(Product::getPrice): cheapest is " + products.get(0).getTitle());

        // Ex 20.4: Case-Insensitive comparing
        List<String> words = new ArrayList<>(List.of("banana", "Apple", "cherry"));
        words.sort(Comparator.comparing(String::toString, String.CASE_INSENSITIVE_ORDER));
        System.out.println("Ex 20.4 - Case-insensitive comparing: " + words);

        // Ex 20.5: Complex comparator combining multiple properties
        Comparator<Product> complexComp = Comparator.comparing(Product::getTitle)
                .thenComparingDouble(Product::getPrice)
                .reversed();
        System.out.println("Ex 20.5 - Complex compound comparator created: " + (complexComp != null));
    }

    // ============================================================================
    // 21. SERIALIZATION VS DESERIALIZATION
    // ============================================================================
    /**
     * INTERVIEW NOTES - SERIALIZATION:
     * - Serialization: Converting object graph into byte stream (ObjectOutputStream.writeObject()).
     * - Deserialization: Reconstructing object from byte stream (ObjectInputStream.readObject()).
     * - serialVersionUID: Unique version identifier. If class changes without matching serialVersionUID,
     *   throws InvalidClassException.
     * - 'transient': Prevents field from being serialized (deserializes to default 0/null).
     * - Externalizable: Interface extending Serializable with writeExternal/readExternal for 100% manual control.
     */
    static void topic21_SerializationVsDeserialization() throws Exception {
        System.out.println("\n--- 21. SERIALIZATION VS DESERIALIZATION ---");

        // Ex 21.1: Standard Serializable class with serialVersionUID and transient field
        class UserSession implements Serializable {
            @Serial private static final long serialVersionUID = 1L;
            String username;
            transient String sessionSecret;

            public UserSession(String user, String secret) {
                this.username = user;
                this.sessionSecret = secret;
            }
        }

        // Ex 21.2: Serializing object to Byte Array Stream
        UserSession original = new UserSession("admin_user", "SUPER_SECRET_TOKEN");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(original);
        }
        System.out.println("Ex 21.2 - Object serialized to byte stream (bytes=" + baos.size() + ")");

        // Ex 21.3: Deserializing object from Byte Array Stream
        UserSession deserialized;
        try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray()))) {
            deserialized = (UserSession) ois.readObject();
        }
        System.out.println("Ex 21.3 - Deserialized: username=" + deserialized.username +
                ", transient sessionSecret=" + deserialized.sessionSecret + " (omitted!)");

        // Ex 21.4: Custom Serialization hooks (writeObject / readObject)
        class CustomSecret implements Serializable {
            @Serial private static final long serialVersionUID = 1L;
            transient String pass = "Secret123";

            @Serial private void writeObject(ObjectOutputStream oos) throws IOException {
                oos.defaultWriteObject();
                oos.writeObject(new StringBuilder(pass).reverse().toString());
            }
            @Serial private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
                ois.defaultReadObject();
                String reversed = (String) ois.readObject();
                this.pass = new StringBuilder(reversed).reverse().toString();
            }
        }
        ByteArrayOutputStream baos2 = new ByteArrayOutputStream();
        new ObjectOutputStream(baos2).writeObject(new CustomSecret());
        CustomSecret cs = (CustomSecret) new ObjectInputStream(new ByteArrayInputStream(baos2.toByteArray())).readObject();
        System.out.println("Ex 21.4 - Custom writeObject/readObject restored pass: " + cs.pass);

        // Ex 21.5: Externalizable interface concept
        System.out.println("Ex 21.5 - Externalizable gives 100% manual control over stream via writeExternal/readExternal");
    }

    // ============================================================================
    // 22. STRING, STRINGBUFFER, AND STRINGBUILDER
    // ============================================================================
    /**
     * INTERVIEW NOTES - STRING VS STRINGBUFFER VS STRINGBUILDER:
     * 1. String: Immutable. Stored in String Constant Pool (SCP). Thread-safe by immutability.
     * 2. StringBuilder: Mutable. Not thread-safe. Maximum performance for single-threaded string concatenation.
     * 3. StringBuffer: Mutable. Thread-safe (All methods synchronized). Slower due to lock acquisition.
     * 4. Java 9 Compact Strings: Uses byte[] array with LATIN1 / UTF-16 coder flag, saving 50% memory for ASCII characters.
     */
    static void topic22_StringStringBufferStringBuilder() {
        System.out.println("\n--- 22. STRING, STRINGBUFFER, STRINGBUILDER ---");

        // Ex 22.1: String Immutability
        String s = "Hello";
        s.concat(" World");
        System.out.println("Ex 22.1 - String immutability: " + s);

        // Ex 22.2: StringBuilder (Fast, mutable, NOT thread-safe)
        StringBuilder sb = new StringBuilder("Start");
        sb.append(" -> Step 1").append(" -> Step 2");
        System.out.println("Ex 22.2 - StringBuilder mutable append: " + sb);

        // Ex 22.3: StringBuffer (Thread-safe, synchronized methods)
        StringBuffer sBuffer = new StringBuffer("Safe");
        sBuffer.append(" Threaded Buffer");
        System.out.println("Ex 22.3 - StringBuffer synchronized: " + sBuffer);

        // Ex 22.4: Performance benchmark comparison
        long start = System.nanoTime();
        StringBuilder benchSb = new StringBuilder();
        for (int i = 0; i < 5000; i++) benchSb.append("x");
        long sbTime = System.nanoTime() - start;
        System.out.println("Ex 22.4 - StringBuilder 5000 appends took: " + (sbTime / 1000) + " µs");

        // Ex 22.5: Useful StringBuilder methods (reverse, insert, delete)
        StringBuilder edit = new StringBuilder("ABCDEF");
        edit.insert(3, "-").reverse();
        System.out.println("Ex 22.5 - StringBuilder insert & reverse: " + edit);
    }

    // ============================================================================
    // 23. STRING POOL
    // ============================================================================
    /**
     * INTERVIEW NOTES - STRING CONSTANT POOL (SCP):
     * - Memory area inside Heap holding unique string literal constants.
     * - String s = "abc": Checks pool; reuses reference if present.
     * - String s = new String("abc"): Creates 2 objects (1 in pool if missing, 1 in heap).
     * - s.intern(): Returns pool reference.
     * - Compile-time Constant Folding: "a" + "b" == "ab" is TRUE because compiler folds literals.
     */
    static void topic23_StringPool() {
        System.out.println("\n--- 23. STRING POOL ---");

        // Ex 23.1: Literal in String Pool vs Heap Object created with 'new'
        String s1 = "Java";
        String s2 = "Java";
        String s3 = new String("Java");
        System.out.println("Ex 23.1 - s1 == s2 (Pool reference): " + (s1 == s2)); // true
        System.out.println("Ex 23.1 - s1 == s3 (Heap reference): " + (s1 == s3)); // false
        System.out.println("Ex 23.1 - s1.equals(s3) (Content value): " + s1.equals(s3)); // true

        // Ex 23.2: String.intern() moving heap string into String Pool
        String s4 = s3.intern();
        System.out.println("Ex 23.2 - s1 == s3.intern(): " + (s1 == s4)); // true

        // Ex 23.3: Compile-time constant folding into String Pool
        String a = "Hel" + "lo";
        String b = "Hello";
        System.out.println("Ex 23.3 - Constant folded literal 'Hel' + 'lo' == 'Hello': " + (a == b)); // true

        // Ex 23.4: Runtime concatenation creates new Heap object
        String prefix = "Hel";
        String runtimeCombined = prefix + "lo";
        System.out.println("Ex 23.4 - Runtime concatenated string == 'Hello': " + (runtimeCombined == b)); // false

        // Ex 23.5: String Deduplication (G1GC optimization)
        System.out.println("Ex 23.5 - G1GC JVM flag '-XX:+UseStringDeduplication' shares underlying byte[] arrays automatically");
    }

    // ============================================================================
    // 24. EXCEPTION HIERARCHY
    // ============================================================================
    /**
     * INTERVIEW NOTES - EXCEPTION HIERARCHY:
     * - Throwable: Root of all throwable objects in Java.
     *   1. Error: Serious JVM issues (OutOfMemoryError, StackOverflowError). Should NOT be caught.
     *   2. Exception: Recoverable program conditions.
     *      - Checked: Subclasses of Exception excluding RuntimeException. Enforced by compiler.
     *      - Unchecked (RuntimeException): Programming bugs (NullPointerException, ArithmeticException).
     */
    static void topic24_ExceptionHierarchy() {
        System.out.println("\n--- 24. EXCEPTION HIERARCHY ---");

        // Ex 24.1: Throwable root: Error vs Exception
        System.out.println("Ex 24.1 - Throwable hierarchy: Throwable -> [Error (Unrecoverable), Exception (Recoverable)]");

        // Ex 24.2: RuntimeException (Unchecked)
        try {
            int div = 10 / 0;
        } catch (ArithmeticException e) {
            System.out.println("Ex 24.2 - Unchecked RuntimeException caught: " + e.getClass().getSimpleName());
        }

        // Ex 24.3: Checked Exception (Must be handled or declared)
        try {
            Class.forName("com.nonexistent.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Ex 24.3 - Checked Exception caught: " + e.getClass().getSimpleName());
        }

        // Ex 24.4: Multi-catch block (Java 7+)
        try {
            if (true) throw new NumberFormatException("Invalid digit");
        } catch (IllegalArgumentException | SecurityException e) {
            System.out.println("Ex 24.4 - Multi-catch block captured: " + e.getClass().getSimpleName());
        }

        // Ex 24.5: Catch order rule (Subclass before Superclass)
        try {
            boolean condition = true;
            if (condition) throw new FileNotFoundException("data.csv not found");
            else throw new IOException("Generic socket I/O error");
        } catch (FileNotFoundException fnf) {
            System.out.println("Ex 24.5 - Specific FileNotFoundException caught before general IOException");
        } catch (IOException io) {
            System.out.println("Ex 24.5 - General IOException handler");
        }
    }

    // ============================================================================
    // 25. TRY CATCH FINALLY FINALIZE
    // ============================================================================
    /**
     * INTERVIEW NOTES - TRY CATCH FINALLY & FINALIZE:
     * - 'finally': Guaranteed execution block (unless System.exit(0) or JVM crash occurs).
     * - 'try-with-resources': Automatically closes AutoCloseable resources in reverse LIFO order.
     * - 'finalize()': Deprecated in Java 9, replaced by java.lang.ref.Cleaner.
     */
    static void topic25_TryCatchFinallyFinalize() {
        System.out.println("\n--- 25. TRY CATCH FINALLY FINALIZE ---");

        // Ex 25.1: Standard try-catch-finally execution guarantee
        StringBuilder log = new StringBuilder();
        try {
            log.append("Try ");
        } catch (Exception e) {
            log.append("Catch ");
        } finally {
            log.append("Finally");
        }
        System.out.println("Ex 25.1 - Execution order: " + log);

        // Ex 25.2: finally executes even after return
        class Helper {
            public static int testFinally() {
                try { return 10; } finally { /* runs before method returns */ }
            }
        }
        System.out.println("Ex 25.2 - Method with finally returned: " + Helper.testFinally());

        // Ex 25.3: try-with-resources with AutoCloseable (Modern Standard)
        class DatabaseConnection implements AutoCloseable {
            public boolean isClosed = false;
            @Override public void close() { isClosed = true; }
        }
        DatabaseConnection connRef;
        try (DatabaseConnection conn = new DatabaseConnection()) {
            connRef = conn;
        }
        System.out.println("Ex 25.3 - AutoCloseable closed automatically: " + connRef.isClosed);

        // Ex 25.4: Multiple auto-closeable resources reverse closing order
        List<String> closeOrder = new ArrayList<>();
        class Resource implements AutoCloseable {
            String name;
            Resource(String n) { this.name = n; }
            @Override public void close() { closeOrder.add(name); }
        }
        try (Resource r1 = new Resource("R1"); Resource r2 = new Resource("R2")) {
            // Body
        }
        System.out.println("Ex 25.4 - Reverse close order (LIFO): " + closeOrder);

        // Ex 25.5: finalize() Deprecation & java.lang.ref.Cleaner (Java 9+)
        Cleaner cleaner = Cleaner.create();
        class CleanableState implements Runnable {
            @Override public void run() { /* Cleaned up native resources */ }
        }
        Cleaner.Cleanable cleanable = cleaner.register(new Object(), new CleanableState());
        System.out.println("Ex 25.5 - Modern java.lang.ref.Cleaner replaced deprecated finalize()");
    }

    // ============================================================================
    // 26. COMPILE-TIME (CHECKED) VS RUN-TIME (UNCHECKED) EXCEPTIONS
    // ============================================================================
    /**
     * INTERVIEW NOTES - CHECKED VS UNCHECKED EXCEPTIONS:
     * - Checked: Conditions caller can anticipate and recover from (IOException, SQLException).
     * - Unchecked (RuntimeException): Programming bugs and logic errors (NullPointerException, IllegalArgumentException).
     */
    static void topic26_CheckedVsUncheckedExceptions() {
        System.out.println("\n--- 26. COMPILE-TIME (CHECKED) VS RUN-TIME (UNCHECKED) EXCEPTIONS ---");

        // Ex 26.1: Checked Exception (Compiler enforces handling)
        class CheckedDemo {
            public void validateFile() throws IOException {
                throw new IOException("File access forbidden");
            }
        }
        try { new CheckedDemo().validateFile(); } catch (IOException e) {
            System.out.println("Ex 26.1 - Checked exception enforced by compiler: " + e.getMessage());
        }

        // Ex 26.2: Unchecked Exception (Subclass of RuntimeException)
        try {
            String s = null;
            s.length();
        } catch (NullPointerException e) {
            System.out.println("Ex 26.2 - Unchecked RuntimeException caught at runtime: " + e.getClass().getSimpleName());
        }

        // Ex 26.3: Converting Checked Exception to Unchecked Wrapper
        try {
            try {
                throw new SQLException("DB Connection dropped");
            } catch (SQLException e) {
                throw new UncheckedIOException(new IOException(e));
            }
        } catch (RuntimeException re) {
            System.out.println("Ex 26.3 - Wrapped checked exception into unchecked: " + re.getClass().getSimpleName());
        }

        // Ex 26.4: Standard Unchecked Exception best practices
        try {
            throw new IllegalStateException("Order state is already COMPLETED");
        } catch (IllegalStateException e) {
            System.out.println("Ex 26.4 - IllegalStateException standard usage: " + e.getMessage());
        }

        // Ex 26.5: Rule of Thumb
        System.out.println("Ex 26.5 - Rule: Checked for recoverable business scenarios; Unchecked for programming defects");
    }

    // ============================================================================
    // 27. CUSTOM EXCEPTIONS
    // ============================================================================
    /**
     * INTERVIEW NOTES - CUSTOM EXCEPTIONS:
     * - Extend RuntimeException for domain business rule violations (ResourceNotFoundException).
     * - Always pass 'message' and 'cause' to 'super(message, cause)' constructors to prevent losing original stack trace.
     */
    static void topic27_CustomExceptions() {
        System.out.println("\n--- 27. CUSTOM EXCEPTIONS ---");

        // Ex 27.1: Custom Checked Exception
        class InsufficientFundsException extends Exception {
            private final double shortfall;
            public InsufficientFundsException(double shortfall) {
                super("Insufficient funds! Missing: $" + shortfall);
                this.shortfall = shortfall;
            }
            public double getShortfall() { return shortfall; }
        }
        try {
            throw new InsufficientFundsException(45.50);
        } catch (InsufficientFundsException e) {
            System.out.println("Ex 27.1 - Custom Checked Exception: " + e.getMessage() + " [Shortfall=" + e.getShortfall() + "]");
        }

        // Ex 27.2: Custom Unchecked Domain Exception
        class ResourceNotFoundException extends RuntimeException {
            public ResourceNotFoundException(String resource, long id) {
                super(resource + " not found with ID: " + id);
            }
        }
        try {
            throw new ResourceNotFoundException("User", 404L);
        } catch (ResourceNotFoundException e) {
            System.out.println("Ex 27.2 - Custom Unchecked Exception: " + e.getMessage());
        }

        // Ex 27.3: Custom Exception with Error Code & Metadata
        class ApiErrorException extends RuntimeException {
            private final int errorCode;
            public ApiErrorException(int code, String message) {
                super(message);
                this.errorCode = code;
            }
            public int getErrorCode() { return errorCode; }
        }
        try {
            throw new ApiErrorException(401, "Invalid Bearer Token");
        } catch (ApiErrorException e) {
            System.out.println("Ex 27.3 - ApiErrorException: HTTP " + e.getErrorCode() + " - " + e.getMessage());
        }

        // Ex 27.4: Cause-chaining constructor in custom exception
        class PaymentGatewayException extends RuntimeException {
            public PaymentGatewayException(String msg, Throwable cause) { super(msg, cause); }
        }
        try {
            throw new PaymentGatewayException("Gateway timeout", new TimeoutException("Socket timed out"));
        } catch (PaymentGatewayException e) {
            System.out.println("Ex 27.4 - Exception cause preserved: " + e.getCause().getClass().getSimpleName());
        }

        // Ex 27.5: Domain Exception Hierarchy
        System.out.println("Ex 27.5 - Best practice: Base domain exception 'AppException' extended by specific subtypes");
    }

    // ============================================================================
    // 28. DESIGN PATTERNS (FACTORY, SINGLETON, BUILDER)
    // ============================================================================
    /**
     * INTERVIEW NOTES - FACTORY, SINGLETON, BUILDER:
     * 1. Factory: Creational pattern. Decouples object instantiation logic from caller.
     * 2. Singleton: Ensures exactly 1 instance across JVM.
     *    - Bill Pugh Lazy Holder: Thread-safe, lazy initialized on class load, zero synchronization overhead.
     *    - Enum Singleton: 100% immune to Reflection and Serialization attacks.
     * 3. Builder: Prevents telescoping constructor anti-pattern. Builds immutable objects step-by-step with validation.
     */
    static void topic28_DesignPatterns() {
        System.out.println("\n--- 28. DESIGN PATTERNS (FACTORY, SINGLETON, BUILDER) ---");

        // Ex 28.1: Factory Pattern (Decoupled object creation)
        interface Notification { String send(); }
        class EmailNotification implements Notification { public String send() { return "Email sent"; } }
        class SMSNotification implements Notification { public String send() { return "SMS sent"; } }
        class NotificationFactory {
            public static Notification create(String type) {
                return switch (type.toUpperCase()) {
                    case "EMAIL" -> new EmailNotification();
                    case "SMS" -> new SMSNotification();
                    default -> throw new IllegalArgumentException("Unknown type");
                };
            }
        }
        Notification notif = NotificationFactory.create("SMS");
        System.out.println("Ex 28.1 - Factory Pattern created: " + notif.send());

        // Ex 28.2: Singleton Pattern - Bill Pugh Lazy Holder (Thread-safe & efficient)
        class AppConfigSingleton {
            private AppConfigSingleton() {}
            private static class Holder {
                private static final AppConfigSingleton INSTANCE = new AppConfigSingleton();
            }
            public static AppConfigSingleton getInstance() { return Holder.INSTANCE; }
        }
        AppConfigSingleton s1 = AppConfigSingleton.getInstance();
        AppConfigSingleton s2 = AppConfigSingleton.getInstance();
        System.out.println("Ex 28.2 - Bill Pugh Singleton identical reference (s1 == s2): " + (s1 == s2));

        // Ex 28.3: Singleton Pattern - Enum Singleton (Reflection & Serialization proof)
        System.out.println("Ex 28.3 - Enum Singleton: 'enum DatabaseConfig { INSTANCE; }' guarantees single instance");

        // Ex 28.4: Builder Pattern (Fluent API with validation)
        class HttpRequestConfig {
            private final String url;
            private final String method;
            private final int timeoutSeconds;

            private HttpRequestConfig(Builder b) {
                this.url = b.url;
                this.method = b.method;
                this.timeoutSeconds = b.timeoutSeconds;
            }

            public static class Builder {
                private String url;
                private String method = "GET";
                private int timeoutSeconds = 30;

                public Builder url(String u) { this.url = u; return this; }
                public Builder method(String m) { this.method = m; return this; }
                public Builder timeout(int s) { this.timeoutSeconds = s; return this; }
                public HttpRequestConfig build() {
                    if (url == null) throw new IllegalStateException("URL is mandatory");
                    return new HttpRequestConfig(this);
                }
            }
            @Override public String toString() { return method + " " + url + " (Timeout: " + timeoutSeconds + "s)"; }
        }
        HttpRequestConfig req = new HttpRequestConfig.Builder()
                .url("https://api.example.com/v1/users")
                .method("POST")
                .timeout(60)
                .build();
        System.out.println("Ex 28.4 - Builder Pattern: " + req);

        // Ex 28.5: Factory combined with Builder
        System.out.println("Ex 28.5 - Composite pattern: Factory returning pre-configured Builder presets");
    }

    // ============================================================================
    // 29. JVM MEMORY MANAGEMENT & GARBAGE COLLECTION
    // ============================================================================
    /**
     * INTERVIEW NOTES - JVM MEMORY & GC INTERNALS:
     * 1. Memory Regions:
     *    - Heap: Shared memory for objects. Divided into Young Gen (Eden, S0, S1) and Old Gen (Tenured).
     *    - Stack: Thread-private execution frames with local primitives and object references.
     *    - Metaspace (Java 8+): Native off-heap memory storing class metadata, method descriptors, constant pools.
     * 2. Generational GC Lifecycle:
     *    - All new objects allocated in Eden.
     *    - Minor GC clears Eden; survivors move to S0/S1 with incremented age.
     *    - Objects surviving tenuring threshold (default 15) promoted to Old Gen.
     * 3. Stop-The-World (STW):
     *    - Pauses application threads while GC determines reachable objects.
     *    - Modern GC: G1GC (Region-based default), ZGC (Sub-millisecond pause concurrent GC).
     * 4. Memory Leak: Unused objects remain referenced by static collections or unclosed listeners, preventing GC.
     */
    static void topic29_JVM_MemoryManagement() {
        System.out.println("\n--- 29. JVM MEMORY MANAGEMENT & GARBAGE COLLECTION ---");

        // Ex 29.1: Live Heap & Non-Heap (Metaspace) Memory telemetry via MXBean
        Runtime rt = Runtime.getRuntime();
        long maxMemMb = rt.maxMemory() / (1024 * 1024);
        long totalMemMb = rt.totalMemory() / (1024 * 1024);
        long freeMemMb = rt.freeMemory() / (1024 * 1024);
        System.out.println("Ex 29.1 - Heap Memory Pool: Max=" + maxMemMb + "MB, TotalAllocated=" + totalMemMb + "MB, Free=" + freeMemMb + "MB");

        // Ex 29.2: Memory Regions: Stack (Frames/Primitives) vs Heap (Objects) vs Metaspace (Class metadata)
        StackWalker walker = StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE);
        long stackDepth = walker.walk(Stream::count);
        System.out.println("Ex 29.2 - Memory Regions: Stack [Depth=" + stackDepth + " frames] | Heap [Dynamic objects] | Metaspace [Native class metadata]");

        // Ex 29.3: Generational Model Simulation (Eden -> Survivor S0/S1 -> Tenured Old Gen)
        class GenerationalGCSimulator {
            static class SimulatedObject {
                String id; int age = 0;
                SimulatedObject(String id) { this.id = id; }
            }
            List<SimulatedObject> eden = new ArrayList<>();
            List<SimulatedObject> survivor0 = new ArrayList<>();
            List<SimulatedObject> survivor1 = new ArrayList<>();
            List<SimulatedObject> tenuredOldGen = new ArrayList<>();
            final int TENURING_THRESHOLD = 3;

            void allocate(String id) { eden.add(new SimulatedObject(id)); }
            void minorGC(Set<String> liveIds) {
                List<SimulatedObject> fromSurvivor = survivor0.isEmpty() ? survivor1 : survivor0;
                List<SimulatedObject> toSurvivor = survivor0.isEmpty() ? survivor0 : survivor1;
                toSurvivor.clear();

                List<SimulatedObject> candidates = new ArrayList<>(eden);
                candidates.addAll(fromSurvivor);
                fromSurvivor.clear();
                eden.clear();

                for (SimulatedObject obj : candidates) {
                    if (liveIds.contains(obj.id)) {
                        obj.age++;
                        if (obj.age >= TENURING_THRESHOLD) {
                            tenuredOldGen.add(obj);
                        } else {
                            toSurvivor.add(obj);
                        }
                    }
                }
            }
        }
        GenerationalGCSimulator gcSim = new GenerationalGCSimulator();
        gcSim.allocate("LongLivedSession");
        gcSim.allocate("TempData");
        Set<String> reachable = Set.of("LongLivedSession");
        gcSim.minorGC(reachable); // Age 1 in S0
        gcSim.minorGC(reachable); // Age 2 in S1
        gcSim.minorGC(reachable); // Age 3 -> Promoted to Old Gen!
        System.out.println("Ex 29.3 - Generational GC Simulator: Promoted to Tenured Old Gen=" + gcSim.tenuredOldGen.size());

        // Ex 29.4: Memory Leak Simulation & WeakReference / WeakHashMap solution
        Map<String, String> weakCache = new WeakHashMap<>();
        String cacheKey = new String("CACHE_KEY_101");
        weakCache.put(cacheKey, "Cached Payload");
        System.out.println("Ex 29.4 - Memory Leak Prevention: WeakHashMap entry present before dereference: " + (weakCache.get("CACHE_KEY_101") != null));

        // Ex 29.5: Stop-The-World (STW) & GC Tuning Flags Guide
        System.out.println("Ex 29.5 - GC Tuning Flags: -Xms4g -Xmx4g -XX:+UseG1GC -XX:MaxGCPauseMillis=200 -XX:+UseZGC (Sub-millisecond STW pause)");
    }

    // ============================================================================
    // 30. CONSTRUCTOR IN DETAILS
    // ============================================================================
    /**
     * INTERVIEW NOTES - CONSTRUCTORS:
     * - Special method with no return type. Cannot be static, final, or abstract.
     * - 'this(...)': Constructor chaining within same class (must be first line).
     * - 'super(...)': Superclass constructor call (must be first line).
     * - Private constructor: Prevents instantiation of utility classes.
     */
    static void topic30_ConstructorsInDetail() {
        System.out.println("\n--- 30. CONSTRUCTORS IN DETAILS ---");

        // Ex 30.1: Default Compiler Constructor vs Parameterized Constructor
        class Person {
            String name; int age;
            public Person(String name, int age) { this.name = name; this.age = age; }
        }
        Person p1 = new Person("Sarah", 28);
        System.out.println("Ex 30.1 - Parameterized constructor: " + p1.name + " (" + p1.age + ")");

        // Ex 30.2: Constructor Chaining using this(...)
        class ServerConfig {
            String host; int port;
            public ServerConfig() { this("localhost", 8080); }
            public ServerConfig(String host) { this(host, 8080); }
            public ServerConfig(String host, int port) { this.host = host; this.port = port; }
        }
        ServerConfig cfg = new ServerConfig();
        System.out.println("Ex 30.2 - Constructor chaining this(...): " + cfg.host + ":" + cfg.port);

        // Ex 30.3: Superclass Constructor Invocation using super(...)
        class BaseItem {
            long id;
            public BaseItem(long id) { this.id = id; }
        }
        class InventoryItem extends BaseItem {
            String sku;
            public InventoryItem(long id, String sku) {
                super(id);
                this.sku = sku;
            }
        }
        InventoryItem item = new InventoryItem(501L, "SKU-XYZ");
        System.out.println("Ex 30.3 - super(...) invoked: id=" + item.id + ", sku=" + item.sku);

        // Ex 30.4: Copy Constructor
        class Point {
            int x, y;
            public Point(int x, int y) { this.x = x; this.y = y; }
            public Point(Point other) { this(other.x, other.y); }
        }
        Point pt1 = new Point(10, 20);
        Point pt2 = new Point(pt1);
        System.out.println("Ex 30.4 - Copy constructor clone: (" + pt2.x + ", " + pt2.y + ")");

        // Ex 30.5: Private Constructor in Utility Class
        class StringHelper {
            private StringHelper() { throw new UnsupportedOperationException("Utility class"); }
            public static boolean isBlank(String str) { return str == null || str.trim().isEmpty(); }
        }
        System.out.println("Ex 30.5 - Utility class with private constructor: isBlank('  ')=" + StringHelper.isBlank("  "));
    }

    // ============================================================================
    // 31. GENERICS
    // ============================================================================
    /**
     * INTERVIEW NOTES - GENERICS & PECS RULE:
     * - Compile-time type safety; eliminated manual casting.
     * - PECS Principle: Producer Extends, Consumer Super.
     *   - <? extends T>: Use when you only READ (Produce) items from collection (Covariance).
     *   - <? super T>: Use when you only WRITE (Consume) items to collection (Contravariance).
     * - Type Erasure: Generic type information is erased to bounds/Object in bytecode for backward compatibility.
     */
    static void topic31_Generics() {
        System.out.println("\n--- 31. GENERICS ---");

        // Ex 31.1: Generic Class
        class Box<T> {
            private T value;
            public Box(T v) { this.value = v; }
            public T getValue() { return value; }
        }
        Box<String> strBox = new Box<>("Generic Type Safety");
        System.out.println("Ex 31.1 - Generic Class Box<T>: " + strBox.getValue());

        // Ex 31.2: Generic Method with Type Parameter <E>
        class ArrayPrinter {
            public static <E> String joinArray(E[] elements) {
                return Arrays.toString(elements);
            }
        }
        Integer[] nums = {1, 2, 3};
        System.out.println("Ex 31.2 - Generic Method: " + ArrayPrinter.joinArray(nums));

        // Ex 31.3: Upper Bounded Wildcard (<? extends Number> - Covariant Read)
        class MathStats {
            public static double sumOfList(List<? extends Number> list) {
                double sum = 0.0;
                for (Number n : list) sum += n.doubleValue();
                return sum;
            }
        }
        System.out.println("Ex 31.3 - Upper Bounded <? extends Number> sum: " + MathStats.sumOfList(List.of(1, 2.5, 3)));

        // Ex 31.4: Lower Bounded Wildcard (<? super Integer> - Contravariant Write / PECS)
        List<Number> numList = new ArrayList<>();
        Consumer<List<? super Integer>> appender = list -> list.add(100);
        appender.accept(numList);
        System.out.println("Ex 31.4 - Lower Bounded <? super Integer> PECS: " + numList);

        // Ex 31.5: Type Erasure
        System.out.println("Ex 31.5 - Type Erasure: Generics exist at compile-time for safety; bytecode uses Object / bounds");
    }

    // ============================================================================
    // 32. COLLECTIONS: LIST, SET, MAP
    // ============================================================================
    /**
     * INTERVIEW NOTES - COLLECTIONS FRAMEWORK:
     * 1. List: Ordered collection, allows duplicates.
     *    - List vs ArrayList: List is the interface contract; ArrayList is the resizable dynamic array implementation (O(1) index access).
     *    - ArrayList vs LinkedList: ArrayList is faster for random access (cache-friendly contiguous memory); LinkedList uses doubly-linked nodes.
     * 2. Set: Unordered, unique elements.
     *    - HashSet: O(1) hash table lookup.
     *    - LinkedHashSet: Doubly-linked hash buckets preserving insertion order.
     *    - TreeSet: O(log N) Red-Black Tree sorted naturally or by Comparator.
     * 3. Map: Key-Value pairs.
     *    - HashMap: Buckets with Linked List; treeifies into Red-Black Tree when bucket count > 8 and capacity >= 64.
     *    - ConcurrentHashMap: Thread-safe without global locking. Uses bucket CAS and synchronized lock per bucket head.
     */
    static void topic32_CollectionsListSetMap() {
        System.out.println("\n--- 32. COLLECTIONS: LIST, SET, MAP ---");

        // Ex 32.1: Difference between List (Interface) and ArrayList (Concrete Class)
        List<String> listInterface = new ArrayList<>();
        listInterface.add("Decoupled Item");
        System.out.println("Ex 32.1 - List (Interface contract) vs ArrayList (Dynamic array implementation): " + listInterface.get(0));

        // Ex 32.2: ArrayList vs LinkedList (Array vs Doubly-Linked Nodes)
        List<Integer> arrayList = new ArrayList<>(List.of(1, 2, 3, 4, 5));
        List<Integer> linkedList = new LinkedList<>(arrayList);
        arrayList.add(2, 99);
        linkedList.add(2, 99);
        System.out.println("Ex 32.2 - ArrayList (Fast O(1) Random Access) vs LinkedList (Fast O(1) Deque Add/Remove First/Last): size=" + linkedList.size());

        // Ex 32.3: Set implementations: HashSet vs LinkedHashSet vs TreeSet
        Set<String> hashSet = new HashSet<>(List.of("Orange", "Apple", "Banana"));
        Set<String> linkedHashSet = new LinkedHashSet<>(List.of("Orange", "Apple", "Banana"));
        Set<String> treeSet = new TreeSet<>(List.of("Orange", "Apple", "Banana"));
        System.out.println("Ex 32.3 - HashSet (Hash table): " + hashSet.size() + " | LinkedHashSet: " + linkedHashSet + " | TreeSet (Red-Black): " + treeSet);

        // Ex 32.4: Map implementations: HashMap vs LinkedHashMap vs TreeMap
        Map<String, Integer> hashMap = new HashMap<>(Map.of("Gamma", 3, "Alpha", 1, "Beta", 2));
        Map<String, Integer> linkedHashMap = new LinkedHashMap<>(hashMap);
        Map<String, Integer> treeMap = new TreeMap<>(hashMap);
        System.out.println("Ex 32.4 - HashMap (Buckets): " + hashMap.keySet() + " | LinkedHashMap: " + linkedHashMap.keySet() + " | TreeMap: " + treeMap.keySet());

        // Ex 32.5: HashMap vs ConcurrentHashMap
        Map<String, String> chm = new ConcurrentHashMap<>();
        chm.put("node_1", "HEALTHY");
        chm.putIfAbsent("node_2", "STANDBY");
        System.out.println("Ex 32.5 - ConcurrentHashMap (Thread-safe bucket CAS / synchronized lock): " + chm);
    }

    // ============================================================================
    // 33. FUNCTIONAL INTERFACES & LAMBDA
    // ============================================================================
    /**
     * INTERVIEW NOTES - FUNCTIONAL INTERFACES & LAMBDAS:
     * - @FunctionalInterface: Exactly ONE Single Abstract Method (SAM). Can have multiple default & static methods.
     * - Lambda Expressions: Concise syntax implementing SAM. Compiled via 'invokedynamic' instruction (does NOT create separate .class files).
     * - Method References (::):
     *   1. Static: Class::staticMethod
     *   2. Instance of specific object: obj::instanceMethod
     *   3. Instance of arbitrary object: Class::instanceMethod
     *   4. Constructor: Class::new
     * - Variable Capture: Variables in enclosing scope must be 'final' or 'effectively final'.
     */
    static void topic33_FunctionalInterfacesAndLambda() {
        System.out.println("\n--- 33. FUNCTIONAL INTERFACES & LAMBDA ---");

        // Ex 33.1: Custom @FunctionalInterface
        @FunctionalInterface
        interface TriFunction<T, U, V, R> {
            R apply(T t, U u, V v);
        }
        TriFunction<Integer, Integer, Integer, Integer> sum3 = (a, b, c) -> a + b + c;
        System.out.println("Ex 33.1 - Custom @FunctionalInterface sum3(1, 2, 3): " + sum3.apply(1, 2, 3));

        // Ex 33.2: Default & Static methods in functional interface
        @FunctionalInterface
        interface Transformer<T> {
            T transform(T input);
            default Transformer<T> andThen(Transformer<T> next) {
                return (T in) -> next.transform(this.transform(in));
            }
        }
        Transformer<String> trim = String::trim;
        Transformer<String> upper = String::toUpperCase;
        Transformer<String> composed = trim.andThen(upper);
        System.out.println("Ex 33.2 - Composed functional default method: " + composed.transform("  hello  "));

        // Ex 33.3: Lambda syntax variations (Expression body vs Block body)
        Function<Integer, Integer> square = x -> x * x;
        Function<Integer, Integer> factorial = n -> {
            int res = 1;
            for (int i = 2; i <= n; i++) res *= i;
            return res;
        };
        System.out.println("Ex 33.3 - Expression Lambda square(4)=" + square.apply(4) + ", Block Lambda 5!=" + factorial.apply(5));

        // Ex 33.4: Method Reference Types (Static, Instance of Object, Instance of Class, Constructor)
        Function<String, Integer> staticRef = Integer::parseInt;
        String prefix = "User: ";
        Function<String, String> instanceBoundRef = prefix::concat;
        Function<String, String> instanceArbitraryRef = String::toUpperCase;
        Supplier<List<String>> constructorRef = ArrayList::new;
        System.out.println("Ex 33.4 - Method References: static=" + staticRef.apply("123") + ", constructor=" + constructorRef.get().getClass().getSimpleName());

        // Ex 33.5: Effectively final variables in Lambdas
        int factor = 10;
        Function<Integer, Integer> multiply = n -> n * factor;
        System.out.println("Ex 33.5 - Lambda closed over effectively final variable: " + multiply.apply(5));
    }

    // ============================================================================
    // 34. BUILT-IN FUNCTIONAL INTERFACES (PREDICATE, FUNCTION, CONSUMER, SUPPLIER)
    // ============================================================================
    /**
     * INTERVIEW NOTES - CORE FUNCTIONAL INTERFACES:
     * 1. Predicate<T>: T -> boolean. Methods: test(), and(), or(), negate().
     * 2. Function<T, R>: T -> R. Methods: apply(), andThen(), compose(), identity().
     * 3. Consumer<T>: T -> void. Methods: accept(), andThen(). Side effects.
     * 4. Supplier<T>: () -> T. Method: get(). Lazy instantiation and factories.
     * 5. Primitive Specializations (IntPredicate, DoubleFunction) prevent autoboxing CPU overhead.
     */
    static void topic34_BuiltInFunctionalInterfaces() {
        System.out.println("\n--- 34. PREDICATE, FUNCTION, CONSUMER, SUPPLIER ---");

        // Ex 34.1: Predicate<T> & BiPredicate (Boolean evaluation & composition)
        Predicate<Integer> isPositive = n -> n > 0;
        Predicate<Integer> isEven = n -> n % 2 == 0;
        Predicate<Integer> isPositiveAndEven = isPositive.and(isEven);
        System.out.println("Ex 34.1 - Predicate composition (6 is positive & even): " + isPositiveAndEven.test(6));

        // Ex 34.2: Function<T, R> & BiFunction (Transformation & andThen)
        Function<String, Integer> lengthFunc = String::length;
        Function<Integer, String> descFunc = len -> "Length: " + len;
        Function<String, String> pipeline = lengthFunc.andThen(descFunc);
        System.out.println("Ex 34.2 - Function chaining andThen: " + pipeline.apply("Enterprise"));

        // Ex 34.3: Consumer<T> & BiConsumer (Side-effects & logging)
        List<String> outputLogs = new ArrayList<>();
        Consumer<String> logger = outputLogs::add;
        Consumer<String> uppercasedLogger = s -> outputLogs.add(s.toUpperCase());
        logger.andThen(uppercasedLogger).accept("Event_A");
        System.out.println("Ex 34.3 - Consumer andThen executed: " + outputLogs);

        // Ex 34.4: Supplier<T> (Lazy instantiation & Factory)
        Supplier<Double> randomSupplier = Math::random;
        System.out.println("Ex 34.4 - Supplier produced: " + randomSupplier.get());

        // Ex 34.5: Primitive Specializations (IntPredicate, ToDoubleFunction, LongConsumer)
        IntPredicate isAdult = age -> age >= 18;
        System.out.println("Ex 34.5 - Primitive IntPredicate isAdult(21): " + isAdult.test(21));
    }

    // ============================================================================
    // 35. STREAM API (INTERMEDIATE VS TERMINAL OPERATORS)
    // ============================================================================
    /**
     * INTERVIEW NOTES - STREAM API:
     * - Pipeline: Source -> Intermediate Operations (Lazy) -> Terminal Operation (Eager, consumes stream).
     * - Intermediate: filter(), map(), flatMap() (flattens nested streams), distinct(), sorted(), skip(), limit(), peek().
     * - Terminal: collect(), reduce(), forEach(), count(), anyMatch(), allMatch(), findFirst().
     * - Streams cannot be reused once consumed!
     */
    static void topic35_StreamAPI() {
        System.out.println("\n--- 35. STREAM API ---");

        List<String> names = List.of("Alice", "Bob", "Charlie", "David", "Anna");

        // Ex 35.1: Intermediate filter() & map() + Terminal toList()
        List<String> filtered = names.stream()
                .filter(n -> n.startsWith("A"))
                .map(String::toUpperCase)
                .toList();
        System.out.println("Ex 35.1 - filter -> map -> toList: " + filtered);

        // Ex 35.2: flatMap() (Flattening nested collections)
        List<List<Integer>> nested = List.of(List.of(1, 2), List.of(3, 4), List.of(5));
        List<Integer> flattened = nested.stream()
                .flatMap(Collection::stream)
                .map(n -> n * 10)
                .toList();
        System.out.println("Ex 35.2 - flatMap flattened: " + flattened);

        // Ex 35.3: Intermediate distinct(), sorted(), limit(), skip()
        List<Integer> raw = List.of(5, 3, 1, 3, 2, 5, 4);
        List<Integer> processed = raw.stream()
                .distinct()
                .sorted()
                .skip(1)
                .limit(3)
                .toList();
        System.out.println("Ex 35.3 - distinct -> sorted -> skip -> limit: " + processed);

        // Ex 35.4: Terminal reduce() (Aggregation)
        int sum = List.of(10, 20, 30, 40).stream().reduce(0, Integer::sum);
        System.out.println("Ex 35.4 - reduce total sum: " + sum);

        // Ex 35.5: Terminal collect() with groupingBy & partitioningBy
        Map<Integer, List<String>> groupedByLength = names.stream()
                .collect(Collectors.groupingBy(String::length));
        Map<Boolean, List<String>> partitionedByA = names.stream()
                .collect(Collectors.partitioningBy(n -> n.startsWith("A")));
        System.out.println("Ex 35.5 - Collectors.groupingBy length: " + groupedByLength);
        System.out.println("Ex 35.5 - Collectors.partitioningBy start with A: " + partitionedByA.get(true));
    }

    // ============================================================================
    // 36. OPTIONAL API
    // ============================================================================
    /**
     * INTERVIEW NOTES - OPTIONAL API:
     * - Container object designed to prevent NullPointerExceptions in return types.
     * - Retrieval:
     *   - orElse(defaultValue): Evaluates default value eagerly even if present.
     *   - orElseGet(Supplier): Evaluates default value lazily only if absent.
     *   - orElseThrow(Supplier): Throws custom exception if absent.
     * - Best Practice: Do NOT use Optional as method parameters, class fields, or Map keys.
     */
    static void topic36_OptionalAPI() {
        System.out.println("\n--- 36. OPTIONAL API ---");

        // Ex 36.1: Creating Optional (of, ofNullable, empty)
        Optional<String> opt1 = Optional.of("Direct Value");
        Optional<String> opt2 = Optional.ofNullable(null);
        Optional<String> opt3 = Optional.empty();
        System.out.println("Ex 36.1 - Optional creation: isPresent=" + opt1.isPresent() + ", isEmpty=" + opt2.isEmpty());

        // Ex 36.2: Value retrieval: orElse vs orElseGet vs orElseThrow
        String val1 = opt2.orElse("Default Value");
        String val2 = opt2.orElseGet(() -> "Computed Default " + Instant.now());
        System.out.println("Ex 36.2 - orElse: '" + val1 + "' | orElseGet: '" + val2 + "'");

        // Ex 36.3: Transformation via map() and flatMap()
        Optional<Integer> lengthOpt = opt1.map(String::length);
        System.out.println("Ex 36.3 - Optional.map length: " + lengthOpt.orElse(0));

        // Ex 36.4: ifPresent() and ifPresentOrElse() (Java 9+)
        List<String> actions = new ArrayList<>();
        opt1.ifPresentOrElse(
                val -> actions.add("Found: " + val),
                () -> actions.add("Missing")
        );
        System.out.println("Ex 36.4 - ifPresentOrElse output: " + actions);

        // Ex 36.5: Converting Optional to Stream (Java 9+)
        List<String> fromOptionals = Stream.<Optional<String>>of(Optional.of("Alpha"), Optional.empty(), Optional.of("Beta"))
                .flatMap(Optional::stream)
                .toList();
        System.out.println("Ex 36.5 - Optional.stream() in Stream pipeline: " + fromOptionals);
    }

    // ============================================================================
    // 37. SEALED CLASSES (JAVA 17+)
    // ============================================================================
    /**
     * INTERVIEW NOTES - SEALED CLASSES (Java 17+):
     * - Restricts which classes or interfaces may extend or implement them using 'sealed' and 'permits'.
     * - Permitted subclasses must be explicitly declared as:
     *   1. final: Prevents further subclassing.
     *   2. sealed: Allows controlled further extension.
     *   3. non-sealed: Re-opens hierarchy for unrestricted subclassing.
     * - Exhaustive Pattern Matching: Eliminates the need for a 'default:' branch in switch expressions.
     */
    sealed interface VehiclePermitted permits Car, Truck, Motorcycle {}
    static final class Car implements VehiclePermitted { public String type() { return "Car"; } }
    static final class Truck implements VehiclePermitted { public String type() { return "Truck"; } }
    static non-sealed class Motorcycle implements VehiclePermitted { public String type() { return "Motorcycle"; } }

    sealed interface Result<T> permits Success, Failure {}
    static record Success<T>(T data) implements Result<T> {}
    static record Failure<T>(String error) implements Result<T> {}

    static void topic37_SealedClasses() {
        System.out.println("\n--- 37. SEALED CLASSES ---");

        // Ex 37.1: Sealed interface permits specific subtypes
        VehiclePermitted v1 = new Car();
        VehiclePermitted v2 = new Truck();
        VehiclePermitted v3 = new Motorcycle();
        System.out.println("Ex 37.1 - Sealed hierarchy instances: " + v1.getClass().getSimpleName() + ", " + v2.getClass().getSimpleName());

        // Ex 37.2: Final permitted subtype
        System.out.println("Ex 37.2 - 'final class Car' terminates further inheritance");

        // Ex 37.3: Non-sealed permitted subtype
        System.out.println("Ex 37.3 - 'non-sealed class Motorcycle' re-opens subclassing to anyone");

        // Ex 37.4: Exhaustive Pattern Matching switch (No default branch required!)
        String category = switch (v1) {
            case Car c -> "Passenger vehicle: " + c.type();
            case Truck t -> "Heavy cargo: " + t.type();
            case Motorcycle m -> "Two wheeler: " + m.type();
        };
        System.out.println("Ex 37.4 - Exhaustive switch pattern matching without default: " + category);

        // Ex 37.5: Domain Modeling with Sealed Records
        Result<String> res = new Success<>("Operation Successful");
        System.out.println("Ex 37.5 - Sealed Result algebraic data type: " + res);
    }

    // ============================================================================
    // 38. RECORDS (JAVA 16+)
    // ============================================================================
    /**
     * INTERVIEW NOTES - RECORDS (Java 16+):
     * - Transparent, immutable data carriers.
     * - Automatically generates: private final fields, canonical constructor, getter methods (e.g. name()),
     *   equals(), hashCode(), and toString().
     * - Compact Constructor: Used for validation/normalization without repeating field assignments.
     * - Cannot extend classes (implicitly extends java.lang.Record), but can implement interfaces.
     */
    static void topic38_Records() {
        System.out.println("\n--- 38. RECORDS ---");

        // Ex 38.1: Canonical Record (Immutable Data Carrier)
        record UserRecord(long id, String email) {}
        UserRecord u = new UserRecord(1001L, "dev@example.com");
        System.out.println("Ex 38.1 - Record generated getters & toString: id=" + u.id() + ", email=" + u.email() + " | " + u);

        // Ex 38.2: Compact Constructor (Validation & Normalization)
        record Money(double amount, String currency) {
            public Money {
                if (amount < 0) throw new IllegalArgumentException("Amount cannot be negative");
                currency = currency.toUpperCase();
            }
        }
        Money m = new Money(99.5, "usd");
        System.out.println("Ex 38.2 - Record compact constructor validated & normalized: " + m);

        // Ex 38.3: Custom Methods & Static Fields in Records
        record GeoPoint(double lat, double lon) {
            public static final GeoPoint ZERO = new GeoPoint(0, 0);
            public boolean isEquator() { return lat == 0.0; }
        }
        System.out.println("Ex 38.3 - Custom Record method isEquator: " + GeoPoint.ZERO.isEquator());

        // Ex 38.4: Record equals and hashCode built-in
        UserRecord uCopy = new UserRecord(1001L, "dev@example.com");
        System.out.println("Ex 38.4 - Record value-based equality (u.equals(uCopy)): " + u.equals(uCopy));

        // Ex 38.5: Record Pattern Matching (Java 21)
        Object obj = new Money(500, "EUR");
        if (obj instanceof Money(double amt, String cur)) {
            System.out.println("Ex 38.5 - Record deconstructed in pattern matching: amt=" + amt + ", cur=" + cur);
        }
    }

    // ============================================================================
    // 39. CONCURRENT COLLECTIONS & PRIORITY QUEUE
    // ============================================================================
    /**
     * INTERVIEW NOTES - CONCURRENT COLLECTIONS & PRIORITY QUEUE:
     * - ConcurrentHashMap: Lock-free reads and bucket-level write synchronization (no global locks).
     * - BlockingQueue: Thread-safe queue blocking on put() when full and take() when empty.
     * - PriorityQueue: Unbounded Binary Heap (Min-Heap by default, Max-Heap with Comparator.reverseOrder()).
     *   peek() is O(1), add()/poll() are O(log N).
     */
    static void topic39_ConcurrentCollectionsAndPriorityQueue() throws Exception {
        System.out.println("\n--- 39. CONCURRENT COLLECTIONS & PRIORITY QUEUE ---");

        // Ex 39.1: ConcurrentHashMap Atomic Merge & PutIfAbsent
        ConcurrentMap<String, Integer> wordCount = new ConcurrentHashMap<>();
        wordCount.put("java", 1);
        wordCount.merge("java", 1, Integer::sum);
        System.out.println("Ex 39.1 - ConcurrentHashMap atomic merge: " + wordCount);

        // Ex 39.2: BlockingQueue Producer-Consumer (ArrayBlockingQueue)
        BlockingQueue<String> bQueue = new ArrayBlockingQueue<>(2);
        bQueue.put("Msg-1");
        bQueue.put("Msg-2");
        System.out.println("Ex 39.2 - BlockingQueue taken: " + bQueue.take());

        // Ex 39.3: CopyOnWriteArraySet (Thread-safe Set)
        Set<String> cowSet = new CopyOnWriteArraySet<>(List.of("A", "B"));
        cowSet.add("C");
        System.out.println("Ex 39.3 - CopyOnWriteArraySet: " + cowSet);

        // Ex 39.4: PriorityQueue Min-Heap (Natural ordering priority)
        PriorityQueue<Integer> minHeap = new PriorityQueue<>(List.of(50, 10, 30, 20));
        List<Integer> extractedMin = new ArrayList<>();
        while (!minHeap.isEmpty()) extractedMin.add(minHeap.poll());
        System.out.println("Ex 39.4 - PriorityQueue Min-Heap polled order: " + extractedMin);

        // Ex 39.5: PriorityQueue Max-Heap with Custom Comparator
        PriorityQueue<Integer> maxHeap = new PriorityQueue<>(Comparator.reverseOrder());
        maxHeap.addAll(List.of(50, 10, 30, 20));
        System.out.println("Ex 39.5 - PriorityQueue Max-Heap polled top: " + maxHeap.poll());
    }

    // ============================================================================
    // 40. STREAM VS PARALLEL STREAM
    // ============================================================================
    /**
     * INTERVIEW NOTES - STREAM VS PARALLEL STREAM:
     * - Sequential Stream: Single-threaded execution.
     * - Parallel Stream: Divides work across ForkJoinPool.commonPool().
     * - When to use Parallel Stream: Large dataset (N > 10,000) + CPU-intensive operations + Cheap splitting (ArrayList, range)
     *   + Completely stateless & non-blocking operations.
     * - Pitfall: Mutating non-thread-safe state in parallel streams causes data corruption and race conditions.
     */
    static void topic40_StreamVsParallelStream() {
        System.out.println("\n--- 40. STREAM VS PARALLEL STREAM ---");

        List<Integer> dataset = IntStream.rangeClosed(1, 1000).boxed().toList();

        // Ex 40.1: Sequential Stream Execution
        long seqSum = dataset.stream().mapToLong(i -> i * 2L).sum();
        System.out.println("Ex 40.1 - Sequential Stream sum: " + seqSum);

        // Ex 40.2: Parallel Stream Execution (Uses ForkJoinPool.commonPool())
        long parSum = dataset.parallelStream().mapToLong(i -> i * 2L).sum();
        System.out.println("Ex 40.2 - Parallel Stream sum: " + parSum);

        // Ex 40.3: Thread-Safety Pitfall in Parallel Stream (Race conditions with non-thread-safe collection)
        List<Integer> safeThreadList = new CopyOnWriteArrayList<>();
        dataset.parallelStream().limit(10).forEach(safeThreadList::add);
        System.out.println("Ex 40.3 - Parallel Stream safe collection count: " + safeThreadList.size());

        // Ex 40.4: Ordering Guarantees: forEach vs forEachOrdered
        List<Integer> smallList = List.of(1, 2, 3, 4, 5);
        List<Integer> orderedResult = new ArrayList<>();
        smallList.parallelStream().forEachOrdered(orderedResult::add);
        System.out.println("Ex 40.4 - forEachOrdered preserves order: " + orderedResult);

        // Ex 40.5: Running Parallel Stream in a Custom ForkJoinPool
        ForkJoinPool customPool = new ForkJoinPool(2);
        try {
            long customSum = customPool.submit(() ->
                    dataset.parallelStream().reduce(0, Integer::sum)
            ).get();
            System.out.println("Ex 40.5 - Parallel stream in custom ForkJoinPool: " + customSum);
        } catch (Exception ignored) {
        } finally {
            customPool.shutdown();
        }
    }

    // ============================================================================
    // 41. VIRTUAL THREADS (JAVA 21 PROJECT LOOM)
    // ============================================================================
    /**
     * INTERVIEW NOTES - VIRTUAL THREADS (Java 21 Project Loom):
     * - Lightweight user-mode threads managed by JVM runtime, NOT 1:1 mapped to OS threads.
     * - Millions of Virtual Threads can run simultaneously on a small pool of Carrier OS threads.
     * - Carrier Unmounting: When a Virtual Thread performs blocking I/O (network, DB, Thread.sleep()),
     *   the JVM unmounts it from its Carrier OS thread and mounts another ready virtual thread.
     * - Eliminates the need for complex reactive programming (WebFlux/RxJava) for high-throughput I/O servers.
     * - Created via Thread.ofVirtual().start() or Executors.newVirtualThreadPerTaskExecutor().
     */
    static void topic41_VirtualThreads() throws Exception {
        System.out.println("\n--- 41. VIRTUAL THREADS (JAVA 21) ---");

        // Ex 41.1: Starting a Single Virtual Thread via Thread.ofVirtual()
        Thread vt = Thread.ofVirtual().name("vt-task").start(() -> {
            // Non-blocking lightweight task
        });
        vt.join();
        System.out.println("Ex 41.1 - Thread.ofVirtual() started and joined: isVirtual=" + vt.isVirtual());

        // Ex 41.2: Spawning 10,000 Lightweight Virtual Threads simultaneously
        int taskCount = 10000;
        AtomicInteger completedTasks = new AtomicInteger(0);
        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            for (int i = 0; i < taskCount; i++) {
                executor.submit(() -> {
                    completedTasks.incrementAndGet();
                    return null;
                });
            }
        }
        System.out.println("Ex 41.2 - 10,000 Virtual Threads executed concurrently: completed=" + completedTasks.get());

        // Ex 41.3: Virtual Thread Per Task Executor in try-with-resources
        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            Future<String> f = executor.submit(() -> {
                Thread.sleep(Duration.ofMillis(10));
                return "Virtual Task Finished";
            });
            System.out.println("Ex 41.3 - newVirtualThreadPerTaskExecutor: " + f.get());
        }

        // Ex 41.4: Carrier Thread unmounting demonstration
        System.out.println("Ex 41.4 - Virtual Threads unmount from Carrier OS thread during I/O / sleep, achieving massive throughput");

        // Ex 41.5: ThreadFactory for Virtual Threads
        ThreadFactory vFactory = Thread.ofVirtual().name("worker-vt-", 1).factory();
        Thread threadFromFactory = vFactory.newThread(() -> {});
        System.out.println("Ex 41.5 - Virtual Thread created via ThreadFactory: " + threadFromFactory.getName());
    }
}
