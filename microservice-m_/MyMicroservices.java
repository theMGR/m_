/**
 * ============================================================================
 * MICROSERVICES MASTER ARCHITECTURE & SYSTEM DESIGN GUIDE (MyMicroservices.java)
 * ============================================================================
 * Comprehensive, production-grade guide covering Microservices Architecture,
 * Distributed Systems, Scalability, High Availability, Resiliency Patterns,
 * Messaging, Event-Driven Design, Observability, and Interview Q&A from
 * Mid-Level to Staff / Principal Distributed Systems Architect level.
 *
 * TABLE OF CONTENTS:
 *  1. Monolith vs Microservices Architecture (Conway's Law & Bounded Contexts)
 *  2. Database Sharding & Replication (Leader-Follower, Read Lag, Consistent Hashing)
 *  3. CQRS (Command Query Responsibility Segregation & Event-Driven Projections)
 *  4. Caching Strategies (Cache-Aside, Write-Through, Write-Behind, Stampede Locks)
 *  5. Load Balancing (Client-Side vs Server-Side, Round Robin, Least Connections)
 *  6. Asynchronous Processing (Worker Queues, Thread Pools, Reactive Backpressure)
 *  7. Synchronous vs Asynchronous Communication (Latency Cascades & Dual-Write Bug)
 *  8. Message Brokers (Apache Kafka Commit Log, Partitions, Offsets, Consumer Groups)
 *  9. API Gateway Pattern (Edge Routing, JWT Token Relay, BFF, Aggregator)
 * 10. Circuit Breakers (Resilience4j CLOSED/OPEN/HALF-OPEN State Machine)
 * 11. Idempotency (Idempotency Keys, Redis SETNX De-duplication Store)
 * 12. Distributed Transactions & Two-Phase Commit (2PC Coordinator & Blocking Flaws)
 * 13. Saga Pattern (Choreography vs Orchestration & Compensating Transactions)
 * 14. Distributed Authentication (Stateless JWT Verification, JWKS, mTLS)
 * 15. Distributed Tracing (Trace ID, Span ID, W3C TraceContext, Baggage)
 * 16. Centralized Logging (ELK Stack, Structured JSON, SLF4J MDC Trace ID)
 * 17. Production Observability (Grafana, Prometheus, Zipkin, 4 Golden Signals)
 * 18. Rate Limiter & Eureka Server (Token Bucket, Service Discovery, Heartbeats)
 * 19. Kafka vs RabbitMQ (Distributed Stream Commit Log vs AMQP Message Broker)
 * 20. Redis & Eviction Policies (LRU, LFU, Volatile-TTL, Sentinel vs Cluster)
 * 21. 12 Core Design Patterns of Microservices (Catalog & Simulators)
 * ============================================================================
 *
 * Each topic includes:
 *  1. Architectural Overview & System Design Core Definitions.
 *  2. Internal Mechanics & Low-Level Distributed System Mechanics.
 *  3. Top Tech Interview Gotchas, Pitfalls, & Production Failure Modes.
 *  4. 4 to 5 fully functional, runnable, executable code simulation engines.
 *
 * Requirements: Java 21 LTS or newer.
 * ============================================================================
 */

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import java.util.function.*;
import java.util.stream.*;

public class MyMicroservices {

    public static void main(String[] args) throws Exception {
        System.out.println("==================================================================");
        System.out.println("🚀 MICROSERVICES MASTER ARCHITECTURE & SYSTEM DESIGN (21 TOPICS)");
        System.out.println("==================================================================\n");

        topic1_MonolithVsMicroservices();
        topic2_DatabaseShardingAndReplication();
        topic3_CQRS();
        topic4_CachingStrategies();
        topic5_LoadBalancing();
        topic6_AsynchronousProcessing();
        topic7_SynchronousVsAsynchronous();
        topic8_MessageBrokersKafka();
        topic9_ApiGatewayPattern();
        topic10_CircuitBreakers();
        topic11_Idempotency();
        topic12_DistributedTransactionsAnd2PC();
        topic13_SagaPattern();
        topic14_DistributedAuthentication();
        topic15_DistributedTracing();
        topic16_CentralizedLoggingELK();
        topic17_ProductionObservabilityGrafanaZipkin();
        topic18_RateLimiterAndEurekaServer();
        topic19_KafkaVsRabbitMQ();
        topic20_RedisAndEvictionPolicies();
        topic21_TwelveDesignPatternsOfMicroservices();

        System.out.println("\n==================================================================");
        System.out.println("🎉 ALL 21 MICROSERVICES TOPICS EXECUTED WITH 100+ EXAMPLES!");
        System.out.println("==================================================================");
    }

    // ============================================================================
    // 1. MONOLITH VS MICROSERVICES
    // ============================================================================
    /**
     * ARCHITECTURAL & INTERVIEW NOTES - MONOLITH VS MICROSERVICES:
     * 1. Monolithic Architecture:
     *    - Single deployable artifact containing UI, business logic, and database access.
     *    - PROS: Simple deployment, straightforward end-to-end debugging, zero network latency between components, ACID transactions.
     *    - CONS: Tight coupling, single point of failure (a memory leak in reporting crashes the entire store),
     *      inflexible scaling (must scale whole app, not just hot path), large codebase slow CI/CD build bottlenecks.
     * 2. Microservices Architecture:
     *    - Suite of small, autonomous, independently deployable services organized around business capabilities (Domain-Driven Design / Bounded Context).
     *    - Each service has its own dedicated database (Database-per-Service).
     *    - PROS: Independent deployability, localized failure domains, polyglot tech stacks, targeted elasticity and autoscaling.
     *    - CONS: Distributed complexity (Network latency, partial failures, data consistency/Sagas, distributed tracing).
     *
     * EXAMINER / RECRUITER GOTCHA:
     * - Conway's Law: "Organizations design systems that mirror their communication structure."
     * - Distributed Monolith (Anti-Pattern): Services deployed separately but sharing a single database or tightly coupled synchronous REST calls.
     */
    static void topic1_MonolithVsMicroservices() {
        System.out.println("\n--- 1. MONOLITH VS MICROSERVICES ---");

        // Ex 1.1: Monolith In-Memory Direct Method Invocation (Zero network cost)
        class MonolithOrderModule {
            public String processOrder(String orderId) {
                // Direct in-process function call
                return "Monolith: Processed " + orderId + " in 0.1ms (In-Memory)";
            }
        }
        System.out.println("Ex 1.1 - " + new MonolithOrderModule().processOrder("ORD-1001"));

        // Ex 1.2: Microservices Decoupled Network RPC / REST Simulator
        class MicroserviceRpcSimulator {
            public String invokeRemoteService(String serviceUrl, String payload) {
                long latencyMs = 15; // Simulated network round-trip + serialization
                return "Microservice (" + serviceUrl + "): Remote ACK [Payload=" + payload + ", Latency=" + latencyMs + "ms]";
            }
        }
        System.out.println("Ex 1.2 - " + new MicroserviceRpcSimulator().invokeRemoteService("http://payment-service/api/v1/charge", "amt=500"));

        // Ex 1.3: Scalability Comparison (Independent vs Whole-App Scaling)
        class ServiceScalingMatrix {
            public Map<String, Integer> microservicesReplicas = Map.of("AuthService", 2, "PaymentService", 10, "ReportingService", 1);
            public int monolithReplicas = 10; // Must scale entire monolith 10x!
        }
        ServiceScalingMatrix matrix = new ServiceScalingMatrix();
        System.out.println("Ex 1.3 - Targeted Elastic Scaling: Hot payment service scaled to " + matrix.microservicesReplicas.get("PaymentService") + " instances vs Monolith " + matrix.monolithReplicas);

        // Ex 1.4: Bounded Context (Domain-Driven Design) Mapping
        Map<String, String> boundedContexts = Map.of(
                "Order Context", "Manages Order entity, cart state, checkout lifecycle",
                "Inventory Context", "Manages SKU stock, reservation locks, warehouse locations",
                "Payment Context", "Manages Stripe/PayPal gateways, refunds, merchant ledger"
        );
        System.out.println("Ex 1.4 - DDD Bounded Contexts: " + boundedContexts.keySet());

        // Ex 1.5: Distributed Monolith Pitfall Warning
        System.out.println("Ex 1.5 - Distributed Monolith Anti-pattern: Sharing 1 DB across 10 microservices causes lock contention and deployment locks");
    }

    // ============================================================================
    // 2. DATABASE SHARDING AND REPLICATION
    // ============================================================================
    /**
     * ARCHITECTURAL & INTERVIEW NOTES - SHARDING & REPLICATION:
     * 1. Database Replication (Leader-Follower / Master-Slave):
     *    - Leader receives all WRITES (INSERT, UPDATE, DELETE).
     *    - Asynchronously or synchronously replicates data to Read Replicas (Followers).
     *    - Read scaling: Routes analytical and read-heavy queries (`SELECT`) to replicas.
     *    - Replication Lag Gotcha: Reading right after write might return stale data (Solution: Read-Your-Own-Writes routing to Leader).
     * 2. Database Sharding (Horizontal Partitioning):
     *    - Splits large tables across multiple independent database nodes.
     *    - Shard Key selection is critical: Must prevent "Hot Shards" (e.g. Sharding by `tenant_id` vs `hash(user_id)`).
     *    - Consistent Hashing minimizes data remapping when scaling nodes from N to N+1.
     */
    static void topic2_DatabaseShardingAndReplication() {
        System.out.println("\n--- 2. DATABASE SHARDING & REPLICATION ---");

        // Ex 2.1: Master-Slave Replication Simulator (Read/Write Splitting)
        class ReplicatedDatabaseCluster {
            private final Map<String, String> masterDb = new ConcurrentHashMap<>();
            private final List<Map<String, String>> readReplicas = List.of(new ConcurrentHashMap<>(), new ConcurrentHashMap<>());
            private final AtomicInteger roundRobin = new AtomicInteger(0);

            public void write(String key, String value) {
                masterDb.put(key, value);
                // Async replication to read replicas
                for (var replica : readReplicas) replica.put(key, value);
            }
            public String read(String key) {
                int replicaIdx = roundRobin.getAndIncrement() % readReplicas.size();
                return readReplicas.get(replicaIdx).get(key);
            }
        }
        ReplicatedDatabaseCluster cluster = new ReplicatedDatabaseCluster();
        cluster.write("user:101", "Alice Data");
        System.out.println("Ex 2.1 - Master-Slave Read/Write Splitting read: " + cluster.read("user:101"));

        // Ex 2.2: Horizontal Database Sharding Simulator (Hash-based Shard Router)
        class ShardRouter {
            private final int shardCount;
            private final List<Map<String, String>> shards;

            public ShardRouter(int numShards) {
                this.shardCount = numShards;
                this.shards = new ArrayList<>();
                for (int i = 0; i < numShards; i++) shards.add(new ConcurrentHashMap<>());
            }
            private int getShardIndex(String shardKey) {
                return Math.abs(shardKey.hashCode()) % shardCount;
            }
            public void put(String shardKey, String data) {
                int idx = getShardIndex(shardKey);
                shards.get(idx).put(shardKey, data);
            }
            public String get(String shardKey) {
                int idx = getShardIndex(shardKey);
                return "Shard#" + idx + " -> " + shards.get(idx).get(shardKey);
            }
        }
        ShardRouter sharding = new ShardRouter(4);
        sharding.put("cust_9981", "Profile 9981");
        sharding.put("cust_1234", "Profile 1234");
        System.out.println("Ex 2.2 - Sharded storage location: " + sharding.get("cust_9981"));
        System.out.println("Ex 2.2 - Sharded storage location: " + sharding.get("cust_1234"));

        // Ex 2.3: Consistent Hashing Ring Simulation
        class ConsistentHashRing {
            private final SortedMap<Integer, String> ring = new TreeMap<>();
            public void addNode(String node) { ring.put(node.hashCode(), node); }
            public String getNode(String key) {
                if (ring.isEmpty()) return null;
                int hash = key.hashCode();
                SortedMap<Integer, String> tailMap = ring.tailMap(hash);
                int targetHash = tailMap.isEmpty() ? ring.firstKey() : tailMap.firstKey();
                return ring.get(targetHash);
            }
        }
        ConsistentHashRing hashRing = new ConsistentHashRing();
        hashRing.addNode("DB-Node-A"); hashRing.addNode("DB-Node-B"); hashRing.addNode("DB-Node-C");
        System.out.println("Ex 2.3 - Consistent Hashing routed 'order_505' to: " + hashRing.getNode("order_505"));

        // Ex 2.4: Replication Lag Simulation & Read-Your-Own-Writes solution
        System.out.println("Ex 2.4 - Read-Your-Own-Writes pattern: Route read requests from the user who just updated to Leader for 2 seconds");

        // Ex 2.5: Resharding & Cross-Shard Joins warning
        System.out.println("Ex 2.5 - Cross-shard JOINs are anti-patterns in microservices; data must be pre-joined in DTO/CQRS views");
    }

    // ============================================================================
    // 3. CQRS (COMMAND QUERY RESPONSIBILITY SEGREGATION)
    // ============================================================================
    /**
     * ARCHITECTURAL & INTERVIEW NOTES - CQRS:
     * 1. What is CQRS:
     *    - Segregates operations into **Commands** (Writes: INSERT/UPDATE/DELETE, mutates state, returns void/ACK)
     *      and **Queries** (Reads: SELECT, returns data, zero side effects).
     * 2. Why Use CQRS:
     *    - Read and Write workloads have vastly different scale requirements (e.g. 10,000 reads : 1 write).
     *    - Write DB optimized for OLTP (Normalized relational tables, ACID, constraints).
     *    - Read DB optimized for OLAP / fast search (Denormalized Elasticsearch, Redis, Read-optimized views).
     * 3. Syncing Write & Read Models:
     *    - Commands emit Domain Events (via Kafka/RabbitMQ) ➔ Event Handler updates Read DB asynchronously (Eventual Consistency).
     */
    static void topic3_CQRS() {
        System.out.println("\n--- 3. CQRS (COMMAND QUERY RESPONSIBILITY SEGREGATION) ---");

        // Ex 3.1: Command Model (Write-Optimized)
        class UserWriteService {
            private final Map<String, String> writeDb = new ConcurrentHashMap<>();
            public void handleCommandCreateUser(String userId, String name, String email) {
                // Business validation & ACID write
                writeDb.put(userId, name + "|" + email);
            }
        }

        // Ex 3.2: Read Model (Denormalized Flat Document for high speed reads)
        class UserReadViewRepository {
            private final Map<String, Map<String, String>> readElasticView = new ConcurrentHashMap<>();
            public void syncEvent(String userId, String name, String email) {
                readElasticView.put(userId, Map.of("id", userId, "displayName", name, "contact", email, "indexedAt", Instant.now().toString()));
            }
            public Map<String, String> queryUserSummary(String userId) {
                return readElasticView.get(userId);
            }
        }

        // Ex 3.3: End-to-End CQRS Dispatcher
        UserWriteService writeService = new UserWriteService();
        UserReadViewRepository readRepo = new UserReadViewRepository();

        // 1. Client executes Command (Write)
        writeService.handleCommandCreateUser("usr_77", "Robert Architect", "rob@example.com");

        // 2. Event Sourced async projection sync
        readRepo.syncEvent("usr_77", "Robert Architect", "rob@example.com");

        // 3. Client executes Query (Read)
        Map<String, String> userView = readRepo.queryUserSummary("usr_77");
        System.out.println("Ex 3.3 - CQRS Denormalized Read View retrieved: " + userView);

        // Ex 3.4: Eventual Consistency window explanation
        System.out.println("Ex 3.4 - CQRS Eventual Consistency: Read DB is updated asynchronously within milliseconds via Kafka stream consumer");

        // Ex 3.5: When NOT to use CQRS
        System.out.println("Ex 3.5 - CQRS Trade-off: Do not use for simple CRUD apps; use when read/write scaling ratios or complex domain logic justify it");
    }

    // ============================================================================
    // 4. CACHING STRATEGIES
    // ============================================================================
    /**
     * ARCHITECTURAL & INTERVIEW NOTES - CACHING PATTERNS:
     * 1. Cache-Aside (Lazy Loading - Most Common):
     *    - App checks Cache. If HIT ➔ return. If MISS ➔ read DB, populate Cache, return.
     * 2. Write-Through:
     *    - App writes to Cache. Cache synchronously writes to DB before returning success.
     * 3. Write-Behind (Write-Back):
     *    - App writes to Cache. Cache returns immediately; asynchronously writes batches to DB (High throughput, risk of data loss on crash).
     * 4. Refresh-Ahead:
     *    - Cache automatically reloads hot keys before TTL expires.
     * 5. Production Hazards:
     *    - Cache Stampede (Thundering Herd): Thousands of concurrent requests hit DB simultaneously when a hot key expires.
     *      (Solution: Mutual exclusion Mutex lock on cache miss or probabilistic early expiration).
     *    - Cache Penetration: Queries for non-existent keys bypass cache and hit DB (Solution: Bloom Filters or caching null with short TTL).
     *    - Cache Avalanche: Multiple keys expiring at the same second (Solution: Add random jitter to TTL).
     */
    static void topic4_CachingStrategies() {
        System.out.println("\n--- 4. CACHING STRATEGIES ---");

        // Ex 4.1: Cache-Aside (Lazy Loading) Implementation
        class CacheAsideService {
            private final Map<String, String> cache = new ConcurrentHashMap<>();
            private final Map<String, String> db = Map.of("item_101", "MacBook Pro M3");

            public String getItem(String itemId) {
                if (cache.containsKey(itemId)) {
                    return "[CACHE_HIT] " + cache.get(itemId);
                }
                String val = db.get(itemId);
                if (val != null) {
                    cache.put(itemId, val);
                }
                return "[CACHE_MISS -> DB_FETCH] " + val;
            }
        }
        CacheAsideService cacheAside = new CacheAsideService();
        System.out.println("Ex 4.1 - 1st Call: " + cacheAside.getItem("item_101"));
        System.out.println("Ex 4.1 - 2nd Call: " + cacheAside.getItem("item_101"));

        // Ex 4.2: Write-Through Cache Simulator
        class WriteThroughStore {
            private final Map<String, String> cache = new ConcurrentHashMap<>();
            private final Map<String, String> db = new ConcurrentHashMap<>();

            public void save(String key, String value) {
                cache.put(key, value);
                db.put(key, value); // Synchronous DB write
            }
            public String get(String key) { return cache.get(key); }
        }
        WriteThroughStore wt = new WriteThroughStore();
        wt.save("product_1", "Gaming Monitor");
        System.out.println("Ex 4.2 - Write-Through Cache written and retrieved: " + wt.get("product_1"));

        // Ex 4.3: Cache Stampede (Mutex Lock Protection)
        class StampedeProtectedCache {
            private final Map<String, String> cache = new ConcurrentHashMap<>();
            private final Map<String, Object> locks = new ConcurrentHashMap<>();

            public String getWithMutex(String key, Supplier<String> dbLoader) {
                String val = cache.get(key);
                if (val != null) return val;

                Object lock = locks.computeIfAbsent(key, k -> new Object());
                synchronized (lock) {
                    val = cache.get(key); // Double check
                    if (val == null) {
                        val = dbLoader.get();
                        cache.put(key, val);
                    }
                    return val;
                }
            }
        }
        StampedeProtectedCache stampedeGuard = new StampedeProtectedCache();
        String result = stampedeGuard.getWithMutex("hot_deal_key", () -> "Black Friday 50% Off");
        System.out.println("Ex 4.3 - Stampede protected cache value: " + result);

        // Ex 4.4: TTL with Random Jitter (Prevents Cache Avalanche)
        int baseTtl = 3600;
        int jitteredTtl = baseTtl + new Random().nextInt(300); // 3600s - 3900s
        System.out.println("Ex 4.4 - TTL with Random Jitter calculated: " + jitteredTtl + "s (Prevents synchronized key expiration)");

        // Ex 4.5: Bloom Filter Concept (Prevents Cache Penetration)
        System.out.println("Ex 4.5 - Bloom Filter: Fast probabilistic check before querying cache/DB to reject non-existent keys");
    }

    // ============================================================================
    // 5. LOAD BALANCING
    // ============================================================================
    /**
     * ARCHITECTURAL & INTERVIEW NOTES - LOAD BALANCING:
     * 1. Server-Side vs Client-Side Load Balancing:
     *    - Server-Side (NGINX, AWS ALB, F5): Client connects to Load Balancer IP; LB distributes traffic to backend instances.
     *    - Client-Side (Spring Cloud LoadBalancer, Envoy, gRPC): Client queries Service Registry (Eureka/Consul) for instance list
     *      and chooses a target server directly, eliminating a network hop.
     * 2. Algorithms:
     *    - Round Robin: Sequential rotation.
     *    - Weighted Round Robin: Distributes traffic proportional to server capacity/spec.
     *    - Least Connections: Routes to the instance currently handling the fewest active requests.
     *    - IP Hash: Routes same client IP to same server (Session stickiness).
     */
    static void topic5_LoadBalancing() {
        System.out.println("\n--- 5. LOAD BALANCING ---");

        // Ex 5.1: Round Robin Load Balancer
        class RoundRobinLoadBalancer {
            private final List<String> servers = List.of("10.0.0.1:8080", "10.0.0.2:8080", "10.0.0.3:8080");
            private final AtomicInteger position = new AtomicInteger(0);

            public String selectServer() {
                int idx = Math.abs(position.getAndIncrement()) % servers.size();
                return servers.get(idx);
            }
        }
        RoundRobinLoadBalancer rrlb = new RoundRobinLoadBalancer();
        System.out.println("Ex 5.1 - Round Robin routing: " + rrlb.selectServer() + ", " + rrlb.selectServer() + ", " + rrlb.selectServer());

        // Ex 5.2: Weighted Round Robin Load Balancer
        class WeightedLoadBalancer {
            private final List<String> weightedPool = new ArrayList<>();
            private final AtomicInteger idx = new AtomicInteger(0);

            public void addServer(String host, int weight) {
                for (int i = 0; i < weight; i++) weightedPool.add(host);
            }
            public String choose() {
                return weightedPool.get(Math.abs(idx.getAndIncrement()) % weightedPool.size());
            }
        }
        WeightedLoadBalancer wlb = new WeightedLoadBalancer();
        wlb.addServer("Large-Server (32GB)", 3); // 75% traffic
        wlb.addServer("Small-Server (8GB)", 1);  // 25% traffic
        System.out.println("Ex 5.2 - Weighted selection: " + wlb.choose() + ", " + wlb.choose() + ", " + wlb.choose());

        // Ex 5.3: Least Connections Load Balancer
        class LeastConnectionsLoadBalancer {
            record ServerNode(String ip, AtomicInteger activeConnections) {}
            private final List<ServerNode> nodes = List.of(
                    new ServerNode("10.0.1.1", new AtomicInteger(15)),
                    new ServerNode("10.0.1.2", new AtomicInteger(2)),  // Lowest!
                    new ServerNode("10.0.1.3", new AtomicInteger(8))
            );
            public String choose() {
                return nodes.stream().min(Comparator.comparingInt(n -> n.activeConnections.get())).get().ip;
            }
        }
        System.out.println("Ex 5.3 - Least Connections selected: " + new LeastConnectionsLoadBalancer().choose());

        // Ex 5.4: Client-side vs Server-side architecture comparison
        System.out.println("Ex 5.4 - Client-side (Spring Cloud LoadBalancer) eliminates extra network hop vs Server-side (AWS ALB)");

        // Ex 5.5: Health Check & Failover circuit ejection
        System.out.println("Ex 5.5 - Active Heartbeat Health Checks eject unresponsive instances from LB pool within 5 seconds");
    }

    // ============================================================================
    // 6. ASYNCHRONOUS PROCESSING
    // ============================================================================
    /**
     * ARCHITECTURAL & INTERVIEW NOTES - ASYNCHRONOUS PROCESSING:
     * - Decouples request ingestion from heavy computation (email sending, image processing, report generation).
     * - Immediate 202 Accepted response to client; task processed in background worker pool or queue.
     * - Prevents HTTP connection pool exhaustion (Tomcat thread starvation).
     */
    static void topic6_AsynchronousProcessing() throws Exception {
        System.out.println("\n--- 6. ASYNCHRONOUS PROCESSING ---");

        // Ex 6.1: CompletableFuture background execution
        CompletableFuture<String> asyncReportTask = CompletableFuture.supplyAsync(() -> {
            try { Thread.sleep(20); } catch (InterruptedException ignored) {}
            return "PDF Report Generated for Year 2026";
        });
        System.out.println("Ex 6.1 - HTTP Request returned '202 Accepted' immediately; Task completing in background: " + asyncReportTask.get());

        // Ex 6.2: Work Queue Producer-Consumer Pattern
        BlockingQueue<String> emailTaskQueue = new LinkedBlockingQueue<>();
        emailTaskQueue.offer("Email: Welcome Alice");
        emailTaskQueue.offer("Email: Password Reset Bob");
        System.out.println("Ex 6.2 - Worker picked async task from queue: " + emailTaskQueue.poll());

        // Ex 6.3: Spring @Async ThreadPoolExecutor Isolation
        ThreadPoolExecutor customAsyncPool = new ThreadPoolExecutor(2, 4, 60, TimeUnit.SECONDS, new ArrayBlockingQueue<>(100));
        customAsyncPool.submit(() -> System.out.println("Ex 6.3 - @Async task executed on dedicated worker thread: " + Thread.currentThread().getName()));
        customAsyncPool.shutdown();

        // Ex 6.4: Fire-and-Forget vs Polling / Webhook callback
        System.out.println("Ex 6.4 - Async Notification Models: Webhook Push (preferred) vs Client Polling (GET /jobs/{jobId}/status)");

        // Ex 6.5: Backpressure management in Reactive Streams
        System.out.println("Ex 6.5 - Reactive Streams Backpressure: Consumer signals demand (request(N)) to prevent fast producer memory saturation");
    }

    // ============================================================================
    // 7. SYNCHRONOUS VS ASYNCHRONOUS COMMUNICATION
    // ============================================================================
    /**
     * ARCHITECTURAL & INTERVIEW NOTES - SYNC VS ASYNC:
     * 1. Synchronous (REST / gRPC / FeignClient):
     *    - Request/Response model. Calling service blocks waiting for downstream response.
     *    - DANGER: Latency amplification (Service A ➔ Service B ➔ Service C).
     *    - Cascading Failure: If Service C is slow, Service B exhausts thread pool, crashing Service A.
     * 2. Asynchronous (Kafka / RabbitMQ / Event-Driven):
     *    - Fire-and-forget or Publish-Subscribe.
     *    - Temporal Decoupling: Producer and Consumer do not need to be online at the same time.
     *    - Higher availability, built-in queue buffering during traffic spikes.
     */
    static void topic7_SynchronousVsAsynchronous() {
        System.out.println("\n--- 7. SYNCHRONOUS VS ASYNCHRONOUS ---");

        // Ex 7.1: Synchronous Cascading Latency Simulation
        long syncTotalLatency = 50 + 70 + 120; // ServiceA (50ms) + ServiceB (70ms) + ServiceC (120ms)
        System.out.println("Ex 7.1 - Synchronous Chain: Total Latency = " + syncTotalLatency + "ms (Coupled availability)");

        // Ex 7.2: Asynchronous Event Decoupling Simulation
        class EventBusSimulator {
            private final List<String> eventLog = new ArrayList<>();
            public void publish(String event) {
                eventLog.add("Event [" + event + "] published in 1ms");
            }
        }
        EventBusSimulator bus = new EventBusSimulator();
        bus.publish("ORDER_CREATED_EVENT");
        System.out.println("Ex 7.2 - Asynchronous Publish: Immediate return; Consumer processes when ready");

        // Ex 7.3: Trade-off Comparison Matrix
        Map<String, String> syncVsAsync = Map.of(
                "Synchronous (REST/gRPC)", "Immediate consistency | High coupling | Cascading failure risk",
                "Asynchronous (Kafka/RabbitMQ)", "Eventual consistency | Loose temporal coupling | Resilient buffering"
        );
        syncVsAsync.forEach((k, v) -> System.out.println("Ex 7.3 - " + k + " -> " + v));

        // Ex 7.4: Dual-write problem in mixed sync/async
        System.out.println("Ex 7.4 - Dual-Write Pitfall: Saving to DB and publishing to Kafka without Transactional Outbox risks data inconsistency");

        // Ex 7.5: When to use which
        System.out.println("Ex 7.5 - Use Sync for real-time user query reads; Use Async for state mutations, notifications, and cross-service updates");
    }

    // ============================================================================
    // 8. MESSAGE BROKERS (APACHE KAFKA)
    // ============================================================================
    /**
     * ARCHITECTURAL & INTERVIEW NOTES - APACHE KAFKA:
     * 1. Core Architecture:
     *    - Distributed, append-only, partitioned commit log.
     *    - Topic: Category/stream of messages.
     *    - Partition: Unit of parallelism and ordering. Messages with the same `message.key` always route to the SAME partition.
     *    - Offset: Sequential integer ID uniquely identifying each record within a partition.
     *    - Consumer Group: Group of consumers cooperating to consume topic partitions (1 partition assigned to exactly 1 consumer in group).
     * 2. Producer Acks:
     *    - `acks=0`: Fire & forget (fastest, risk of loss).
     *    - `acks=1`: Leader writes to local log before acking.
     *    - `acks=all` (`-1`): Leader + all In-Sync Replicas (ISR) commit before acking (Zero data loss guarantee).
     */
    static void topic8_MessageBrokersKafka() {
        System.out.println("\n--- 8. MESSAGE BROKERS (APACHE KAFKA) ---");

        // Ex 8.1: Kafka Topic Partition & Offset Append Simulator
        class KafkaPartition {
            private final int partitionId;
            private final List<String> records = new ArrayList<>();
            public KafkaPartition(int id) { this.partitionId = id; }
            public synchronized long append(String message) {
                records.add(message);
                return records.size() - 1; // Offset
            }
        }
        KafkaPartition partition0 = new KafkaPartition(0);
        long offset1 = partition0.append("Order #101 Created");
        long offset2 = partition0.append("Order #102 Created");
        System.out.println("Ex 8.1 - Kafka Partition 0 append offsets: " + offset1 + ", " + offset2);

        // Ex 8.2: Key-based Partition Routing (Guarantees Strict Per-Key Ordering)
        class KafkaProducerSimulator {
            public int calculatePartition(String key, int numPartitions) {
                return Math.abs(key.hashCode()) % numPartitions;
            }
        }
        KafkaProducerSimulator producer = new KafkaProducerSimulator();
        int partForUser1 = producer.calculatePartition("user_99", 3);
        int partForUser2 = producer.calculatePartition("user_99", 3);
        System.out.println("Ex 8.2 - Same Key ('user_99') consistently routes to same partition: " + (partForUser1 == partForUser2) + " (Partition " + partForUser1 + ")");

        // Ex 8.3: Consumer Group Partition Rebalance Simulator
        Map<String, List<Integer>> consumerGroupAllocation = Map.of(
                "Consumer-1", List.of(0, 1),
                "Consumer-2", List.of(2, 3)
        );
        System.out.println("Ex 8.3 - Consumer Group partition assignment across 4 partitions: " + consumerGroupAllocation);

        // Ex 8.4: Exactly-Once Semantics (EOS) with Transactional Producer
        System.out.println("Ex 8.4 - Kafka EOS: 'enable.idempotence=true' + 'transactional.id' enables atomic multi-partition writes");

        // Ex 8.5: Retention Policies (Time-based vs Log Compaction)
        System.out.println("Ex 8.5 - Log Compaction keeps only the latest value for each key, enabling Kafka as a state store");
    }

    // ============================================================================
    // 9. API GATEWAY PATTERN
    // ============================================================================
    /**
     * ARCHITECTURAL & INTERVIEW NOTES - API GATEWAY:
     * - Single Entry Point for all external clients (Web, Mobile, Third-party).
     * - Cross-Cutting Concerns Offloaded to Gateway:
     *   1. Reverse Proxy & Dynamic Routing (Spring Cloud Gateway, Kong, Envoy).
     *   2. Authentication & JWT Token Validation (Edge validation).
     *   3. SSL/TLS Termination.
     *   4. Centralized Rate Limiting (Redis Token Bucket).
     *   5. Request / Response Transformation & Header Enrichment.
     *   6. API Composition / Request Aggregation (BFF - Backend For Frontend).
     */
    static void topic9_ApiGatewayPattern() {
        System.out.println("\n--- 9. API GATEWAY PATTERN ---");

        // Ex 9.1: Dynamic Routing Rule Matcher Simulator
        class GatewayRoute {
            String pathPrefix; String targetServiceUrl;
            GatewayRoute(String p, String t) { this.pathPrefix = p; this.targetServiceUrl = t; }
            boolean matches(String path) { return path.startsWith(pathPrefix); }
        }
        List<GatewayRoute> routes = List.of(
                new GatewayRoute("/api/v1/orders", "lb://order-service"),
                new GatewayRoute("/api/v1/users", "lb://user-service"),
                new GatewayRoute("/api/v1/payments", "lb://payment-service")
        );
        String clientPath = "/api/v1/orders/101";
        GatewayRoute matched = routes.stream().filter(r -> r.matches(clientPath)).findFirst().orElseThrow();
        System.out.println("Ex 9.1 - API Gateway routed '" + clientPath + "' ➔ " + matched.targetServiceUrl);

        // Ex 9.2: Edge Authentication & Token Validation Filter
        class GatewayAuthFilter {
            public boolean validateAndEnrichHeader(String authHeader, Map<String, String> downstreamHeaders) {
                if (authHeader != null && authHeader.startsWith("Bearer valid_jwt_")) {
                    downstreamHeaders.put("X-User-Id", "usr_882");
                    downstreamHeaders.put("X-User-Roles", "ROLE_PREMIUM");
                    return true;
                }
                return false;
            }
        }
        Map<String, String> downstreamHeaders = new HashMap<>();
        boolean allowed = new GatewayAuthFilter().validateAndEnrichHeader("Bearer valid_jwt_token", downstreamHeaders);
        System.out.println("Ex 9.2 - Edge Auth passed & enriched downstream headers: " + downstreamHeaders);

        // Ex 9.3: BFF (Backend For Frontend) Pattern
        System.out.println("Ex 9.3 - BFF Pattern: Dedicated Mobile Gateway (/mobile/api) and Web Gateway (/web/api) tailored to client device payloads");

        // Ex 9.4: Gateway Request Aggregator (Combines User + Order in 1 client roundtrip)
        class ApiAggregator {
            public Map<String, Object> aggregate(String userId) {
                return Map.of("user", "Alice", "recentOrders", List.of("ORD-1", "ORD-2"));
            }
        }
        System.out.println("Ex 9.4 - API Gateway aggregated composite response: " + new ApiAggregator().aggregate("101"));

        // Ex 9.5: Global Rate Limiting at Gateway Edge
        System.out.println("Ex 9.5 - Rate Limiting at Gateway returns HTTP 429 Too Many Requests before requests touch downstream microservices");
    }

    // ============================================================================
    // 10. CIRCUIT BREAKERS (RESILIENCE4J)
    // ============================================================================
    /**
     * ARCHITECTURAL & INTERVIEW NOTES - CIRCUIT BREAKERS:
     * 1. The 3 States:
     *    - CLOSED (Normal): Requests pass through. Tracks failures in a sliding window (e.g. last 100 calls).
     *    - OPEN (Tripped): When failure rate exceeds threshold (e.g. > 50%), circuit OPENs.
     *      All incoming requests FAIL-FAST immediately without calling downstream service! Returns fallback.
     *    - HALF-OPEN (Testing Recovery): After a `waitDurationInOpenState` (e.g. 10s), circuit lets a trial batch of requests through.
     *      If successful ➔ Transitions back to CLOSED. If fails ➔ Returns to OPEN.
     * 2. Why Use Circuit Breakers:
     *    - Prevents thread starvation and cascading system crashes when a downstream service experiences an outage.
     */
    static void topic10_CircuitBreakers() {
        System.out.println("\n--- 10. CIRCUIT BREAKERS (RESILIENCE4J) ---");

        // Ex 10.1: 3-State Circuit Breaker Engine Simulator
        enum State { CLOSED, OPEN, HALF_OPEN }
        class CircuitBreakerSimulator {
            State state = State.CLOSED;
            int failureCount = 0;
            final int FAILURE_THRESHOLD = 3;

            public String execute(Supplier<String> remoteCall, Supplier<String> fallback) {
                if (state == State.OPEN) {
                    return "[CIRCUIT_OPEN - FAIL FAST] -> Fallback: " + fallback.get();
                }
                try {
                    String res = remoteCall.get();
                    failureCount = 0;
                    if (state == State.HALF_OPEN) state = State.CLOSED;
                    return "[SUCCESS] " + res;
                } catch (Exception e) {
                    failureCount++;
                    if (failureCount >= FAILURE_THRESHOLD) {
                        state = State.OPEN;
                    }
                    return "[FALLBACK_RETURNED] " + fallback.get();
                }
            }
        }
        CircuitBreakerSimulator cb = new CircuitBreakerSimulator();
        Supplier<String> failingCall = () -> { throw new RuntimeException("503 Downstream Down"); };
        Supplier<String> fallback = () -> "Cached Fallback Product Data";

        cb.execute(failingCall, fallback); // Fail 1
        cb.execute(failingCall, fallback); // Fail 2
        cb.execute(failingCall, fallback); // Fail 3 -> Trips to OPEN!
        String failFastResult = cb.execute(failingCall, fallback); // Short-circuited!
        System.out.println("Ex 10.1 - Circuit Breaker State after 3 failures: " + cb.state);
        System.out.println("Ex 10.1 - Execution Result: " + failFastResult);

        // Ex 10.2: Resilience4j Sliding Window Types (Count-based vs Time-based)
        System.out.println("Ex 10.2 - Resilience4j Sliding Windows: Count-based (evaluates last N calls) vs Time-based (evaluates last N seconds)");

        // Ex 10.3: Fallback Strategy Best Practices
        System.out.println("Ex 10.3 - Fallbacks: Return cached data, default static placeholder, or enqueue in Dead Letter Queue");

        // Ex 10.4: Bulkhead Pattern synergy
        System.out.println("Ex 10.4 - Bulkhead Isolation limits concurrent calls per downstream service to prevent pool exhaustion");

        // Ex 10.5: RateLimiter vs CircuitBreaker
        System.out.println("Ex 10.5 - RateLimiter protects self from being overloaded; CircuitBreaker protects downstream services from overload");
    }

    // ============================================================================
    // 11. IDEMPOTENCY
    // ============================================================================
    /**
     * ARCHITECTURAL & INTERVIEW NOTES - IDEMPOTENCY:
     * - An operation is Idempotent if executing it N times produces the exact same side-effects as executing it once.
     * - HTTP Verbs: GET, PUT, DELETE, HEAD, OPTIONS are idempotent. POST is non-idempotent by default.
     * - Production Implementation for POST / Payments:
     *   1. Client generates a unique UUID `Idempotency-Key` in request header.
     *   2. Server checks Redis/DB for `Idempotency-Key` using atomic `SETNX` (Set if Not Exists).
     *   3. If key exists ➔ Return cached previous response immediately.
     *   4. If key is new ➔ Process transaction, store response with key, return result.
     */
    static void topic11_Idempotency() {
        System.out.println("\n--- 11. IDEMPOTENCY ---");

        // Ex 11.1: Idempotency Key De-duplication Store Simulator (Redis SETNX simulation)
        class IdempotentPaymentGateway {
            private final Map<String, String> idempotencyStore = new ConcurrentHashMap<>();

            public String processPayment(String idempotencyKey, double amount) {
                if (idempotencyStore.containsKey(idempotencyKey)) {
                    return "[CACHED_IDEMPOTENT_RESPONSE] Transaction already processed: " + idempotencyStore.get(idempotencyKey);
                }
                // Process fresh payment
                String txId = "TXN_" + UUID.randomUUID().toString().substring(0, 8);
                String receipt = "Paid $" + amount + " (TxId: " + txId + ")";
                idempotencyStore.put(idempotencyKey, receipt);
                return "[NEW_TRANSACTION] " + receipt;
            }
        }
        IdempotentPaymentGateway gateway = new IdempotentPaymentGateway();
        String idempotencyKey = "client-uuid-9876-5432";
        System.out.println("Ex 11.1 - 1st Attempt: " + gateway.processPayment(idempotencyKey, 100.0));
        System.out.println("Ex 11.1 - 2nd Retry (Network timeout retry): " + gateway.processPayment(idempotencyKey, 100.0));

        // Ex 11.2: Database Unique Constraint for Idempotency
        System.out.println("Ex 11.2 - DB Unique Constraint: 'UNIQUE INDEX (order_id, payment_ref)' guarantees duplicate DB writes throw duplicate key exception");

        // Ex 11.3: Natural Idempotency (Mathematical & State assignment)
        int balance = 500;
        // Non-idempotent: balance += 100 (repeated = 600, 700...)
        // Idempotent: status = "PAID" (repeated = still "PAID")
        System.out.println("Ex 11.3 - Natural Idempotency: 'status = PAID' is idempotent; 'balance += 10' is non-idempotent");

        // Ex 11.4: Consumer Idempotency in Kafka Message Processing
        System.out.println("Ex 11.4 - Kafka Consumer Idempotency: Store processed message offset/ID in DB table within same local transaction");

        // Ex 11.5: Idempotency Key TTL expiration
        System.out.println("Ex 11.5 - Idempotency Keys in Redis should have a 24-48 hour TTL to prevent unbounded memory growth");
    }

    // ============================================================================
    // 12. DISTRIBUTED TRANSACTIONS & TWO-PHASE COMMIT (2PC)
    // ============================================================================
    /**
     * ARCHITECTURAL & INTERVIEW NOTES - 2PC (TWO-PHASE COMMIT):
     * 1. How 2PC Works:
     *    - Phase 1 (Prepare / Voting Phase):
     *      Coordinator asks all participating nodes: "Can you commit this transaction?".
     *      Nodes lock resources, prepare undo/redo logs, and vote YES or NO.
     *    - Phase 2 (Commit / Rollback Phase):
     *      If ALL voted YES ➔ Coordinator sends `COMMIT`.
     *      If ANY voted NO ➔ Coordinator sends `ROLLBACK`.
     * 2. Why 2PC is an Anti-Pattern in Modern Microservices:
     *    - Blocking Protocol: Nodes hold database locks until Coordinator responds. High latency, throughput drops to lowest common denominator.
     *    - Single Point of Failure (SPOF): If Coordinator crashes during Phase 2, participants remain blocked holding locks indefinitely.
     *    - CAP Theorem: Sacrifices Availability (A) and Partition Tolerance (P) for Consistency (C).
     */
    static void topic12_DistributedTransactionsAnd2PC() {
        System.out.println("\n--- 12. DISTRIBUTED TRANSACTIONS & 2PC ---");

        // Ex 12.1: Two-Phase Commit Protocol Simulator
        interface Participant {
            boolean prepare();
            void commit();
            void rollback();
        }
        class DatabaseParticipant implements Participant {
            String name; boolean canCommit;
            DatabaseParticipant(String n, boolean ok) { this.name = n; this.canCommit = ok; }
            @Override public boolean prepare() { return canCommit; }
            @Override public void commit() { System.out.println("  [" + name + "] Phase 2: COMMITTED"); }
            @Override public void rollback() { System.out.println("  [" + name + "] Phase 2: ROLLED BACK"); }
        }

        class TwoPhaseCommitCoordinator {
            public boolean execute2PC(List<Participant> participants) {
                // Phase 1: Prepare
                boolean allAgreed = true;
                for (var p : participants) {
                    if (!p.prepare()) { allAgreed = false; break; }
                }
                // Phase 2: Commit or Abort
                if (allAgreed) {
                    for (var p : participants) p.commit();
                    return true;
                } else {
                    for (var p : participants) p.rollback();
                    return false;
                }
            }
        }
        List<Participant> clusterParticipants = List.of(
                new DatabaseParticipant("OrderDB", true),
                new DatabaseParticipant("InventoryDB", true),
                new DatabaseParticipant("PaymentGatewayDB", false) // Fails vote!
        );
        TwoPhaseCommitCoordinator coordinator = new TwoPhaseCommitCoordinator();
        boolean globalCommit = coordinator.execute2PC(clusterParticipants);
        System.out.println("Ex 12.1 - 2PC Global Transaction Result: " + (globalCommit ? "GLOBAL_COMMIT" : "GLOBAL_ABORT_ROLLBACK"));

        // Ex 12.2: The Blocking Lock Problem
        System.out.println("Ex 12.2 - 2PC Flaw: Participants hold row-level DB locks throughout Phase 1 & 2, causing severe thread starvation");

        // Ex 12.3: Coordinator Single Point of Failure (SPOF)
        System.out.println("Ex 12.3 - 2PC Coordinator Crash: If Coordinator dies between Phase 1 and 2, participants remain blocked in limbo");

        // Ex 12.4: Modern Microservices Alternative: SAGA Pattern
        System.out.println("Ex 12.4 - Modern Microservices prefer Saga Pattern (BASE / Eventual Consistency) over 2PC (ACID)");

        // Ex 12.5: CAP Theorem implications
        System.out.println("Ex 12.5 - In distributed network partitions (P), you must choose between Availability (AP - Saga) and Consistency (CP - 2PC)");
    }

    // ============================================================================
    // 13. SAGA PATTERN
    // ============================================================================
    /**
     * ARCHITECTURAL & INTERVIEW NOTES - SAGA PATTERN:
     * - Sequence of local database transactions coordinate across multiple microservices.
     * - Each local transaction updates its own database and publishes an event.
     * - If a step fails, the Saga executes Compensating Transactions in reverse order to undo changes (Eventual Consistency).
     * 1. Choreography-Based Saga:
     *    - Decentralized. Services publish and listen to domain events (e.g. Kafka topics).
     *    - PROS: Simple, no central orchestrator.
     *    - CONS: Difficult to track overall state; circular dependency risk for complex workflows.
     * 2. Orchestration-Based Saga:
     *    - Centralized Orchestrator (e.g. Camunda, Temporal, Spring StateMachine) coordinates calls and compensations.
     *    - PROS: Clear workflow visibility, easy error handling and monitoring.
     */
    static void topic13_SagaPattern() {
        System.out.println("\n--- 13. SAGA PATTERN ---");

        // Ex 13.1: Orchestration-Based Saga with Compensating Transactions
        class OrderSagaOrchestrator {
            public boolean executeCreateOrderSaga(boolean paymentSucceeds, boolean inventorySucceeds) {
                System.out.println("  [Saga Step 1] OrderService: Order Created (PENDING)");
                if (!paymentSucceeds) {
                    System.out.println("  [Saga Compensation 1] OrderService: Order CANCELLED");
                    return false;
                }
                System.out.println("  [Saga Step 2] PaymentService: Charged $100");

                if (!inventorySucceeds) {
                    System.out.println("  [Saga Compensation 2] PaymentService: Refunded $100 (Compensating TX)");
                    System.out.println("  [Saga Compensation 1] OrderService: Order CANCELLED (Compensating TX)");
                    return false;
                }
                System.out.println("  [Saga Step 3] InventoryService: Reserved Items");
                System.out.println("  [Saga Completed] Order Marked APPROVED");
                return true;
            }
        }
        OrderSagaOrchestrator saga = new OrderSagaOrchestrator();
        System.out.println("Ex 13.1 - Saga Execution with Inventory Failure:");
        boolean success = saga.executeCreateOrderSaga(true, false); // Payment OK, Inventory fails
        System.out.println("Ex 13.1 - Saga Result: " + (success ? "SUCCESS" : "COMPENSATED_ROLLBACK"));

        // Ex 13.2: Choreography vs Orchestration Architecture
        Map<String, String> sagaTypes = Map.of(
                "Choreography", "Event-driven pub/sub (Services listen & react to Kafka events)",
                "Orchestration", "Central state machine commands services and coordinates compensations"
        );
        sagaTypes.forEach((k, v) -> System.out.println("Ex 13.2 - Saga." + k + " -> " + v));

        // Ex 13.3: Transactional Outbox Pattern (Guarantees Atomic DB Save + Event Publish)
        System.out.println("Ex 13.3 - Outbox Pattern: Save Order + OutboxEvent in SAME local DB transaction; Debezium CDC publishes event to Kafka");

        // Ex 13.4: Semantic Rollback vs Technical Rollback
        System.out.println("Ex 13.4 - Compensating transactions perform business semantic rollback (Refund money, restock item), not DB log undo");

        // Ex 13.5: Handling Pivot Transactions
        System.out.println("Ex 13.5 - Pivot Transaction: The point of no return. Once Pivot succeeds, subsequent Saga steps must retry until success");
    }

    // ============================================================================
    // 14. DISTRIBUTED AUTHENTICATION
    // ============================================================================
    /**
     * ARCHITECTURAL & INTERVIEW NOTES - DISTRIBUTED AUTHENTICATION:
     * 1. Token-Based Authentication (Stateless JWT):
     *    - User authenticates with Auth Server (Keycloak / Okta / Cognito).
     *    - Receives a cryptographically signed JWT.
     *    - Microservices verify JWT signature locally using Auth Server's public key (JWKS) without making DB/network calls!
     * 2. API Gateway Token Relay:
     *    - Gateway validates JWT at the edge.
     *    - Gateway strips sensitive client cookies and forwards `Authorization: Bearer <token>` and `X-User-Id` downstream.
     */
    static void topic14_DistributedAuthentication() {
        System.out.println("\n--- 14. DISTRIBUTED AUTHENTICATION ---");

        // Ex 14.1: Asymmetric JWT Verification Simulator (Public/Private Key)
        class AsymmetricAuthSimulator {
            private final String publicJwksKey = "PUBLIC_KEY_RSA_2048";
            public boolean verifyTokenLocally(String jwtToken) {
                // Microservice verifies signature in-memory using cached public JWKS
                return jwtToken.endsWith(".valid_signature");
            }
        }
        AsymmetricAuthSimulator auth = new AsymmetricAuthSimulator();
        boolean isValid = auth.verifyTokenLocally("header.payload.valid_signature");
        System.out.println("Ex 14.1 - Microservice verified JWT locally with zero network auth calls: " + isValid);

        // Ex 14.2: API Gateway Token Relay Pattern
        Map<String, String> gatewayRelayedHeaders = Map.of(
                "Authorization", "Bearer eyJhbGciOiJSUzI1NiIs...",
                "X-Authenticated-User", "usr_1001",
                "X-User-Roles", "ROLE_USER,ROLE_ADMIN"
        );
        System.out.println("Ex 14.2 - Token Relay headers propagated downstream: " + gatewayRelayedHeaders.keySet());

        // Ex 14.3: Token Revocation & Blacklisting via Redis
        class TokenBlacklistService {
            private final Set<String> revokedTokenJtis = ConcurrentHashMap.newKeySet();
            public void revokeToken(String jti) { revokedTokenJtis.add(jti); }
            public boolean isRevoked(String jti) { return revokedTokenJtis.contains(jti); }
        }
        TokenBlacklistService blacklist = new TokenBlacklistService();
        blacklist.revokeToken("jwt_id_9918");
        System.out.println("Ex 14.3 - Instant Token Revocation check (Redis blacklist): " + blacklist.isRevoked("jwt_id_9918"));

        // Ex 14.4: OAuth2 Scopes vs RBAC Roles
        System.out.println("Ex 14.4 - Scopes (read:orders) define client permissions; Roles (ROLE_ADMIN) define user privileges");

        // Ex 14.5: mTLS (Mutual TLS) for Zero-Trust Service-to-Service Security
        System.out.println("Ex 14.5 - Service Mesh (Istio) enforces mTLS: Both client and server authenticate each other via X.509 certificates");
    }

    // ============================================================================
    // 15. DISTRIBUTED TRACING
    // ============================================================================
    /**
     * ARCHITECTURAL & INTERVIEW NOTES - DISTRIBUTED TRACING:
     * 1. Key Terminology (OpenTelemetry / W3C TraceContext):
     *    - Trace ID: Unique identifier for an entire end-to-end request flow across all microservices.
     *    - Span ID: Identifier for a single unit of work within a specific service.
     *    - Parent Span ID: Links spans together in a directed acyclic graph (DAG).
     *    - Baggage: Key-value metadata propagated across process boundaries (e.g. `tenant-id`, `user-id`).
     * 2. Header Propagation Standards:
     *    - W3C TraceContext: `traceparent: 00-4bf92f3577b34da6a3ce929d0e0e4736-00f067aa0ba902b7-01`
     *    - B3 Propagation: `X-B3-TraceId`, `X-B3-SpanId`.
     */
    static void topic15_DistributedTracing() {
        System.out.println("\n--- 15. DISTRIBUTED TRACING ---");

        // Ex 15.1: Trace Context Propagation Engine Simulator
        class TraceContext {
            final String traceId;
            final String spanId;
            final String parentSpanId;
            TraceContext(String t, String s, String p) { this.traceId = t; this.spanId = s; this.parentSpanId = p; }
            TraceContext createChildSpan() {
                return new TraceContext(this.traceId, UUID.randomUUID().toString().substring(0, 8), this.spanId);
            }
        }
        TraceContext rootGatewayTrace = new TraceContext("trace-root-9918", "span-gw-1", null);
        TraceContext orderServiceSpan = rootGatewayTrace.createChildSpan();
        TraceContext paymentServiceSpan = orderServiceSpan.createChildSpan();

        System.out.println("Ex 15.1 - Root Trace ID across all services: " + rootGatewayTrace.traceId);
        System.out.println("Ex 15.1 - OrderService Span: id=" + orderServiceSpan.spanId + ", parent=" + orderServiceSpan.parentSpanId);
        System.out.println("Ex 15.1 - PaymentService Span: id=" + paymentServiceSpan.spanId + ", parent=" + paymentServiceSpan.parentSpanId);

        // Ex 15.2: W3C TraceContext Header Format (W3C standard)
        String w3cHeader = "00-" + rootGatewayTrace.traceId + "-" + paymentServiceSpan.spanId + "-01";
        System.out.println("Ex 15.2 - W3C 'traceparent' header propagated in HTTP requests: " + w3cHeader);

        // Ex 15.3: Micrometer Tracing & OpenTelemetry Integration in Spring Boot 3
        System.out.println("Ex 15.3 - Spring Boot 3 uses Micrometer Tracing with OpenTelemetry / Brave bridge to export traces to Zipkin / Jaeger");

        // Ex 15.4: Baggage Context Propagation
        Map<String, String> baggage = Map.of("tenantId", "enterprise_corp_12");
        System.out.println("Ex 15.4 - Baggage metadata propagated across all service hops: " + baggage);

        // Ex 15.5: Trace Sampling Rate in High-Throughput systems
        System.out.println("Ex 15.5 - Sampling: In high-scale systems (100k req/s), sample 1-5% of traces to prevent storage exhaustion");
    }

    // ============================================================================
    // 16. CENTRALIZED LOGGING (ELK STACK)
    // ============================================================================
    /**
     * ARCHITECTURAL & INTERVIEW NOTES - CENTRALIZED LOGGING:
     * 1. ELK / EFK Architecture:
     *    - Elasticsearch: Distributed JSON search and analytics engine.
     *    - Logstash / Fluentd / Fluentbit: Log shipper and transformation pipeline.
     *    - Kibana: Visualization dashboards and query UI.
     * 2. Best Practices:
     *    - Output logs in Structured JSON format (Logstash Logback Encoder).
     *    - Inject `traceId` and `spanId` into SLF4J MDC (Mapped Diagnostic Context) so all logs for a request can be filtered with 1 query in Kibana.
     */
    static void topic16_CentralizedLoggingELK() {
        System.out.println("\n--- 16. CENTRALIZED LOGGING (ELK STACK) ---");

        // Ex 16.1: Structured JSON Log Record with MDC Trace Correlation
        record JsonLogEntry(Instant timestamp, String level, String service, String traceId, String message, Map<String, Object> context) {}
        JsonLogEntry logEntry = new JsonLogEntry(
                Instant.now(), "ERROR", "payment-service", "trace-root-9918",
                "Payment gateway timeout after 5000ms", Map.of("userId", "usr_101", "orderId", "ORD-99")
        );
        System.out.println("Ex 16.1 - Structured JSON Log formatted for Logstash: " + logEntry);

        // Ex 16.2: SLF4J MDC (Mapped Diagnostic Context) Simulator
        class MdcContextSimulator {
            private static final ThreadLocal<Map<String, String>> mdc = ThreadLocal.withInitial(HashMap::new);
            public static void put(String k, String v) { mdc.get().put(k, v); }
            public static String formatLog(String msg) { return "[" + mdc.get() + "] " + msg; }
            public static void clear() { mdc.remove(); }
        }
        MdcContextSimulator.put("traceId", "trace-root-9918");
        MdcContextSimulator.put("userId", "usr_101");
        System.out.println("Ex 16.2 - MDC Enriched Log Output: " + MdcContextSimulator.formatLog("Order processed successfully"));
        MdcContextSimulator.clear();

        // Ex 16.3: Log Aggregation Pipeline (Filebeat -> Logstash -> Elasticsearch -> Kibana)
        System.out.println("Ex 16.3 - Log Pipeline: Filebeat reads container stdout ➔ Logstash parses JSON ➔ Elasticsearch indexes ➔ Kibana displays");

        // Ex 16.4: Log Rotation and Retention Policies
        System.out.println("Ex 16.4 - Elasticsearch Index Lifecycle Management (ILM): Hot (SSD 7 days) -> Warm (HDD 30 days) -> Cold/Delete");

        // Ex 16.5: Masking PII (Personally Identifiable Information) in Logs
        System.out.println("Ex 16.5 - Security Guard: Regex Logback appender masks Credit Cards ('****-****-****-1234') and Passwords");
    }

    // ============================================================================
    // 17. PRODUCTION OBSERVABILITY (GRAFANA, ZIPKIN, PROMETHEUS)
    // ============================================================================
    /**
     * ARCHITECTURAL & INTERVIEW NOTES - OBSERVABILITY:
     * 1. The 3 Pillars of Observability:
     *    - Metrics (Prometheus / Grafana): Aggregatable numerical time-series data (CPU, memory, request rate, error rate).
     *    - Traces (Zipkin / Jaeger): End-to-end request journeys with latency breakdowns.
     *    - Logs (ELK / Loki): Granular textual records of specific events.
     * 2. The 4 Golden Signals (Google SRE Book):
     *    - Latency: Time taken to serve a request.
     *    - Traffic: Demand placed on the system (Requests per second).
     *    - Errors: Rate of requests that fail (HTTP 5xx, exceptions).
     *    - Saturation: How full the service is (CPU %, Memory %, Connection pool usage).
     */
    static void topic17_ProductionObservabilityGrafanaZipkin() {
        System.out.println("\n--- 17. PRODUCTION OBSERVABILITY (GRAFANA & ZIPKIN) ---");

        // Ex 17.1: The 4 Golden Signals Telemetry Model
        record GoldenSignalsMetrics(double latencyP99Ms, long rpsTraffic, double errorRatePercent, double cpuSaturationPercent) {}
        GoldenSignalsMetrics signals = new GoldenSignalsMetrics(42.5, 4500, 0.02, 68.4);
        System.out.println("Ex 17.1 - 4 Golden Signals Telemetry: " + signals);

        // Ex 17.2: Zipkin Trace Span Waterfall Latency Analyzer
        class ZipkinSpan {
            String serviceName; long durationMs;
            ZipkinSpan(String s, long d) { this.serviceName = s; this.durationMs = d; }
        }
        List<ZipkinSpan> waterfall = List.of(
                new ZipkinSpan("api-gateway", 120),
                new ZipkinSpan("order-service", 95),
                new ZipkinSpan("payment-service (Bottleneck)", 85),
                new ZipkinSpan("database-sql-query", 80)
        );
        System.out.println("Ex 17.2 - Zipkin Waterfall Latency Trace:");
        waterfall.forEach(s -> System.out.println("  -> " + s.serviceName + " [" + s.durationMs + "ms]"));

        // Ex 17.3: Prometheus Metrics Exposition (/actuator/prometheus)
        String prometheusFormat = "http_server_requests_seconds_count{status=\"200\",uri=\"/api/orders\"} 14205";
        System.out.println("Ex 17.3 - Prometheus Counter Metric: " + prometheusFormat);

        // Ex 17.4: Grafana Alerting Rules
        System.out.println("Ex 17.4 - Grafana Alert Rule: 'ALERT HighErrorRate IF http_5xx_rate > 5% FOR 2m ➔ Trigger PagerDuty'");

        // Ex 17.5: APM (Application Performance Monitoring) Thread Dumps on high CPU
        System.out.println("Ex 17.5 - Automated APM diagnostics: Capture Thread Dump & Heap Dump when memory exceeds 90% threshold");
    }

    // ============================================================================
    // 18. RATE LIMITER & SERVICE DISCOVERY (EUREKA SERVER)
    // ============================================================================
    /**
     * ARCHITECTURAL & INTERVIEW NOTES - RATE LIMITING & EUREKA:
     * 1. Rate Limiting Algorithms:
     *    - Token Bucket: Tokens added at constant rate. Allows bursts up to bucket capacity.
     *    - Leaky Bucket: Requests enter queue and leak out at constant rate. Smooths bursts.
     *    - Sliding Window Log / Counter: Precise rolling window tracking.
     * 2. Netflix Eureka Service Registry:
     *    - Service Registration: Microservice starts up and registers its IP/Port with Eureka Server.
     *    - Heartbeat: Sends heartbeat every 30s. If no heartbeat for 90s, Eureka evicts the instance.
     *    - Self-Preservation Mode: If Eureka loses heartbeats from >15% instances due to network glitch,
     *      it stops evicting to protect valid services!
     */
    static void topic18_RateLimiterAndEurekaServer() {
        System.out.println("\n--- 18. RATE LIMITER & EUREKA SERVER ---");

        // Ex 18.1: Token Bucket Rate Limiter Engine Simulator
        class TokenBucketRateLimiter {
            private final int capacity;
            private double tokens;
            private final double refillRatePerSec;
            private Instant lastRefillTimestamp;

            public TokenBucketRateLimiter(int capacity, double refillRatePerSec) {
                this.capacity = capacity;
                this.tokens = capacity;
                this.refillRatePerSec = refillRatePerSec;
                this.lastRefillTimestamp = Instant.now();
            }

            public synchronized boolean allowRequest() {
                refill();
                if (tokens >= 1.0) {
                    tokens -= 1.0;
                    return true; // Request Allowed
                }
                return false; // HTTP 429 Too Many Requests
            }

            private void refill() {
                Instant now = Instant.now();
                double secondsElapsed = Duration.between(lastRefillTimestamp, now).toMillis() / 1000.0;
                tokens = Math.min(capacity, tokens + (secondsElapsed * refillRatePerSec));
                lastRefillTimestamp = now;
            }
        }
        TokenBucketRateLimiter limiter = new TokenBucketRateLimiter(2, 1.0); // Capacity 2, 1 token/sec
        System.out.println("Ex 18.1 - Request 1 allowed: " + limiter.allowRequest());
        System.out.println("Ex 18.1 - Request 2 allowed: " + limiter.allowRequest());
        System.out.println("Ex 18.1 - Request 3 (Over capacity): " + limiter.allowRequest() + " (HTTP 429 Rate Limited)");

        // Ex 18.2: Eureka Service Registry Simulator
        class EurekaServerSimulator {
            private final Map<String, List<String>> registry = new ConcurrentHashMap<>();
            public void register(String appName, String instanceAddress) {
                registry.computeIfAbsent(appName.toUpperCase(), k -> new CopyOnWriteArrayList<>()).add(instanceAddress);
            }
            public List<String> discover(String appName) {
                return registry.getOrDefault(appName.toUpperCase(), List.of());
            }
        }
        EurekaServerSimulator eureka = new EurekaServerSimulator();
        eureka.register("PAYMENT-SERVICE", "10.0.0.15:8081");
        eureka.register("PAYMENT-SERVICE", "10.0.0.16:8081");
        System.out.println("Ex 18.2 - Eureka Discovered instances for PAYMENT-SERVICE: " + eureka.discover("PAYMENT-SERVICE"));

        // Ex 18.3: Eureka Self-Preservation Mode Gotcha
        System.out.println("Ex 18.3 - Eureka Self-Preservation Mode: Disables instance eviction when widespread network partitions occur");

        // Ex 18.4: Distributed Rate Limiting via Redis + Lua Scripts
        System.out.println("Ex 18.4 - Distributed Rate Limiting: Redis executes atomic Lua scripts to manage shared token buckets across all API Gateways");

        // Ex 18.5: Eureka vs Kubernetes Native DNS Service Discovery
        System.out.println("Ex 18.5 - Cloud Native: Kubernetes CoreDNS ('http://order-service:8080') replaces Eureka in containerized K8s clusters");
    }

    // ============================================================================
    // 19. KAFKA VS RABBITMQ
    // ============================================================================
    /**
     * ARCHITECTURAL & INTERVIEW NOTES - KAFKA VS RABBITMQ:
     * 1. Architecture:
     *    - Kafka: Distributed, append-only partitioned commit log. Pull-based consumer model. Dumb broker, smart consumer.
     *    - RabbitMQ: Traditional AMQP Message Broker. Push-based consumer model. Smart broker, dumb consumer (Exchange ➔ Queue routing).
     * 2. Message Ordering:
     *    - Kafka: Strict ordering guaranteed PER PARTITION (using message keys).
     *    - RabbitMQ: FIFO queue per consumer; ordering breaks if multiple consumers consume concurrently from same queue.
     * 3. Retention & Replay:
     *    - Kafka: Messages persisted on disk for N days/weeks. Consumers can rewind offsets and replay history anytime.
     *    - RabbitMQ: Messages deleted once acknowledged by consumers (transient).
     * 4. Throughput:
     *    - Kafka: Millions of messages/sec (Zero-Copy OS pagecache). Ideal for Big Data, Event Sourcing, Metrics streams.
     *    - RabbitMQ: Tens of thousands of messages/sec. Ideal for complex routing (Topic, Fanout, Direct, Headers).
     */
    static void topic19_KafkaVsRabbitMQ() {
        System.out.println("\n--- 19. KAFKA VS RABBITMQ ---");

        // Ex 19.1: Architectural Comparison Matrix
        Map<String, String> brokerComparison = Map.of(
                "Architecture", "Kafka: Distributed Commit Log | RabbitMQ: AMQP Message Queue",
                "Model", "Kafka: Pull-based (Consumer controls rate) | RabbitMQ: Push-based",
                "Persistence & Replay", "Kafka: Retains data on disk (Replayable) | RabbitMQ: Deletes after ACK",
                "Throughput", "Kafka: Millions/sec (Zero-Copy) | RabbitMQ: Tens of thousands/sec",
                "Routing", "Kafka: Key-to-partition hashing | RabbitMQ: Complex Exchange Bindings (Direct/Topic/Fanout)"
        );
        brokerComparison.forEach((k, v) -> System.out.println("Ex 19.1 - " + k + " -> " + v));

        // Ex 19.2: RabbitMQ Exchange Routing Simulator
        class RabbitMqTopicExchange {
            public List<String> matchQueues(String routingKey) {
                if (routingKey.startsWith("order.")) return List.of("order_queue", "audit_queue");
                if (routingKey.startsWith("payment.")) return List.of("payment_queue", "audit_queue");
                return List.of("general_queue");
            }
        }
        System.out.println("Ex 19.2 - RabbitMQ Topic Exchange routed 'order.created' to: " + new RabbitMqTopicExchange().matchQueues("order.created"));

        // Ex 19.3: Kafka Offset Rewind & Replay Simulation
        class ReplayableConsumerSimulator {
            public List<String> replayFromOffset(List<String> log, int startOffset) {
                return log.subList(startOffset, log.size());
            }
        }
        List<String> partitionLog = List.of("Event 0", "Event 1", "Event 2", "Event 3");
        System.out.println("Ex 19.3 - Kafka Consumer rewound offset to 1: " + new ReplayableConsumerSimulator().replayFromOffset(partitionLog, 1));

        // Ex 19.4: Dead Letter Queue (DLQ) in RabbitMQ vs Kafka
        System.out.println("Ex 19.4 - Dead Letter Queue: Failed poisoned messages routed to DLQ after 3 failed retries with exponential backoff");

        // Ex 19.5: Decision Framework
        System.out.println("Ex 19.5 - Rule: Choose RabbitMQ for complex AMQP routing & RPC; Choose Kafka for high-throughput event streaming & replay");
    }

    // ============================================================================
    // 20. REDIS & EVICTION POLICIES
    // ============================================================================
    /**
     * ARCHITECTURAL & INTERVIEW NOTES - REDIS & EVICTION POLICIES:
     * 1. What is Redis: In-memory, single-threaded (event loop based), sub-millisecond key-value data structure store.
     * 2. Eviction Policies (`maxmemory-policy` when memory is full):
     *    - `noeviction` (Default): Returns OOM errors for writes when memory limit is reached.
     *    - `allkeys-lru`: Evicts Least Recently Used keys out of ALL keys. (Best for general caching).
     *    - `volatile-lru`: Evicts LRU keys out of keys with an expire (TTL) set.
     *    - `allkeys-lfu`: Evicts Least Frequently Used keys out of ALL keys (Frequency based).
     *    - `volatile-lfu`: Evicts LFU keys out of keys with an expire set.
     *    - `allkeys-random` / `volatile-random`: Randomly evicts keys.
     *    - `volatile-ttl`: Evicts keys with shortest remaining TTL first.
     */
    static void topic20_RedisAndEvictionPolicies() {
        System.out.println("\n--- 20. REDIS & EVICTION POLICIES ---");

        // Ex 20.1: LRU (Least Recently Used) Cache Eviction Simulator
        class LruCacheSimulator<K, V> extends LinkedHashMap<K, V> {
            private final int maxCapacity;
            public LruCacheSimulator(int cap) {
                super(cap, 0.75f, true); // accessOrder = true
                this.maxCapacity = cap;
            }
            @Override
            protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
                return size() > maxCapacity;
            }
        }
        LruCacheSimulator<String, String> lruCache = new LruCacheSimulator<>(3);
        lruCache.put("k1", "val1");
        lruCache.put("k2", "val2");
        lruCache.put("k3", "val3");
        lruCache.get("k1"); // Access k1 (k2 becomes eldest!)
        lruCache.put("k4", "val4"); // Evicts k2!
        System.out.println("Ex 20.1 - Redis 'allkeys-lru' cache keys after eviction: " + lruCache.keySet());

        // Ex 20.2: LFU (Least Frequently Used) Counter Simulator
        class LfuItem {
            String val; int accessCount = 1;
            LfuItem(String v) { this.val = v; }
        }
        Map<String, LfuItem> lfuMap = new HashMap<>(Map.of(
                "rare_key", new LfuItem("val_rare"),       // count = 1 (Evicted first!)
                "popular_key", new LfuItem("val_popular")   // count = 10
        ));
        lfuMap.get("popular_key").accessCount = 10;
        String lfuEvictionCandidate = lfuMap.entrySet().stream().min(Comparator.comparingInt(e -> e.getValue().accessCount)).get().getKey();
        System.out.println("Ex 20.2 - Redis 'allkeys-lfu' identified eviction candidate: " + lfuEvictionCandidate);

        // Ex 20.3: Redis Eviction Policy Summary Table
        Map<String, String> policies = Map.of(
                "allkeys-lru", "Evicts least recently used keys (Standard caching recommendation)",
                "allkeys-lfu", "Evicts least frequently used keys (Protects frequently accessed keys)",
                "volatile-ttl", "Evicts keys with the shortest remaining TTL first",
                "noeviction", "Returns error on write commands when memory is full"
        );
        policies.forEach((k, v) -> System.out.println("Ex 20.3 - maxmemory-policy." + k + " -> " + v));

        // Ex 20.4: Redis Sentinel (HA Failover) vs Redis Cluster (Sharding)
        System.out.println("Ex 20.4 - Redis Sentinel: Automatic master failover (1 Master + Replicas) | Redis Cluster: Distributed sharding across 16,384 hash slots");

        // Ex 20.5: Redis Data Structures
        System.out.println("Ex 20.5 - Redis Data Types: String (Caching), Hash (Objects), List (Queues), Set (Unique tags), Sorted Set / ZSET (Leaderboards)");
    }

    // ============================================================================
    // 21. 12 DESIGN PATTERNS OF MICROSERVICES
    // ============================================================================
    /**
     * ARCHITECTURAL & INTERVIEW NOTES - 12 CORE MICROSERVICES PATTERNS:
     *  1. API Gateway Pattern: Single entry point, routing, auth, rate limiting.
     *  2. Aggregator Pattern: Combines data from multiple services into a single composite response.
     *  3. Circuit Breaker Pattern: Fails fast on downstream outages, preventing cascading failures.
     *  4. Saga Pattern: Manages distributed transactions across services via local transactions + compensations.
     *  5. CQRS Pattern: Separates Read and Write models for independent scaling.
     *  6. Event Sourcing Pattern: Persists state as an immutable append-only sequence of domain events.
     *  7. Database-per-Service Pattern: Strict data encapsulation; each service owns its DB.
     *  8. Transactional Outbox Pattern: Guarantees atomic DB write + message queue publish without dual-write bugs.
     *  9. Bulkhead Pattern: Isolates resources (thread pools, connection pools) to contain failures.
     * 10. Sidecar Pattern: Deploys helper components (logging, Envoy proxy, mTLS) alongside the main container.
     * 11. Externalized Configuration Pattern: Centralizes properties (Spring Cloud Config, Consul) per environment.
     * 12. Service Discovery Pattern: Dynamically discovers network locations of service instances (Eureka/Consul).
     */
    static void topic21_TwelveDesignPatternsOfMicroservices() {
        System.out.println("\n--- 21. 12 CORE DESIGN PATTERNS OF MICROSERVICES ---");

        List<String> twelvePatterns = List.of(
                "1. API Gateway Pattern (Edge routing, SSL termination, and rate limiting)",
                "2. Aggregator Pattern (BFF and composite service data merging)",
                "3. Circuit Breaker Pattern (Resilience4j fault isolation and fallbacks)",
                "4. Saga Pattern (Choreography / Orchestration distributed transactions)",
                "5. CQRS Pattern (Command Query Responsibility Segregation)",
                "6. Event Sourcing Pattern (Immutable event log state storage)",
                "7. Database-per-Service Pattern (Strict domain boundary encapsulation)",
                "8. Transactional Outbox Pattern (Dual-write mitigation with CDC / Debezium)",
                "9. Bulkhead Pattern (Thread pool and CPU isolation per dependency)",
                "10. Sidecar Pattern (Envoy Service Mesh proxy in Kubernetes pods)",
                "11. Externalized Configuration Pattern (Spring Cloud Config / Vault)",
                "12. Service Discovery Pattern (Dynamic registration via Eureka / Consul)"
        );

        // Ex 21.1: Listing the 12 Architecture Patterns
        twelvePatterns.forEach(p -> System.out.println("Ex 21.1 - " + p));

        // Ex 21.2: Aggregator Pattern Simulator
        class AggregatorService {
            public Map<String, Object> getCompositeDashboard(String userId) {
                return Map.of("user", "Alice", "balance", 1500.0, "activeTickets", List.of("TCK-101"));
            }
        }
        System.out.println("Ex 21.2 - Aggregator Pattern output: " + new AggregatorService().getCompositeDashboard("101"));

        // Ex 21.3: Event Sourcing Log Simulator
        class EventSourcedAccount {
            private final List<String> events = new ArrayList<>();
            public void deposit(int amt) { events.add("DEPOSITED_" + amt); }
            public void withdraw(int amt) { events.add("WITHDREW_" + amt); }
            public int reconstructBalance() {
                int bal = 0;
                for (String ev : events) {
                    if (ev.startsWith("DEPOSITED_")) bal += Integer.parseInt(ev.split("_")[1]);
                    if (ev.startsWith("WITHDREW_")) bal -= Integer.parseInt(ev.split("_")[1]);
                }
                return bal;
            }
        }
        EventSourcedAccount account = new EventSourcedAccount();
        account.deposit(200); account.deposit(300); account.withdraw(150);
        System.out.println("Ex 21.3 - Event Sourcing Reconstructed Balance from Event Log: $" + account.reconstructBalance());

        // Ex 21.4: Bulkhead Pattern Thread Pool Isolation
        ExecutorService paymentThreadPool = Executors.newFixedThreadPool(10);
        ExecutorService searchThreadPool = Executors.newFixedThreadPool(5);
        System.out.println("Ex 21.4 - Bulkhead Isolation: Payment pool failure does NOT exhaust Search pool capacity");
        paymentThreadPool.shutdown(); searchThreadPool.shutdown();

        // Ex 21.5: Sidecar Pattern in Kubernetes Pods
        System.out.println("Ex 21.5 - Sidecar Architecture: Container A (Spring Boot App) + Container B (Envoy Proxy for mTLS & tracing) in same Pod");
    }
}
