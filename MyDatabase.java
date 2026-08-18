/**
 * ============================================================================
 * DATABASE & SQL/NOSQL MASTER ARCHITECTURE & INTERVIEW GUIDE (MyDatabase.java)
 * ============================================================================
 * Comprehensive, production-grade guide covering Relational Databases (MySQL/PostgreSQL),
 * NoSQL, ACID vs BASE Transactions, Indexing (B+Tree/Hash), Partitioning & Sharding,
 * SQL Internals (WHERE vs HAVING, DELETE vs DROP vs TRUNCATE, GROUP BY, CASE WHEN),
 * Window Functions (2nd Highest Salary), Multi-table JOINs, Normalization (1NF-BCNF),
 * and Concurrency Locking (Optimistic vs Pessimistic).
 *
 * Each topic includes:
 *  1. Architectural Overview & Recruiter/Examiner Definition.
 *  2. Low-Level Database Engine Internals (Buffer Pool, WAL, B+Tree, Undo Logs).
 *  3. Top Tech Interview Gotchas, Query Optimization Pitfalls, & Tricky Edge Cases.
 *  4. 4 to 5 fully functional, runnable, executable SQL/Storage Engine simulations.
 *
 * Requirements: Java 21 LTS or newer.
 * ============================================================================
 */

import java.io.*;
import java.sql.SQLException;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import java.util.concurrent.locks.*;
import java.util.function.*;
import java.util.stream.*;

public class MyDatabase {

    public static void main(String[] args) throws Exception {
        System.out.println("==================================================================");
        System.out.println("🚀 DATABASE & SQL/NOSQL MASTER ARCHITECTURE GUIDE (17 TOPICS)");
        System.out.println("==================================================================\n");

        topic1_MySQLVsNoSQL();
        topic2_Transactions();
        topic3_ACIDVsBASE();
        topic4_Indexing();
        topic5_Partitioning();
        topic6_Sharding();
        topic7_WhereVsHaving();
        topic8_DeleteVsDropVsTruncate();
        topic9_GroupByAndCaseWhen();
        topic10_AggregateFunctions();
        topic11_Normalization();
        topic12_NormalForms1NFToBCNF();
        topic13_LazyVsEagerLoading();
        topic14_SQLJoins();
        topic15_FindSecondHighestSalary();
        topic16_JoinBetweenThreeTables();
        topic17_OptimisticVsPessimisticLocking();

        System.out.println("\n==================================================================");
        System.out.println("🎉 ALL 17 DATABASE TOPICS EXECUTED WITH 85+ RUNNABLE EXAMPLES!");
        System.out.println("==================================================================");
    }

    // ============================================================================
    // 1. WHAT IS MYSQL AND NOSQL
    // ============================================================================
    /**
     * ARCHITECTURAL & INTERVIEW NOTES - MYSQL VS NOSQL:
     * 1. MySQL (Relational / RDBMS):
     *    - Structured schema (Tables, Columns, Data Types, Foreign Keys).
     *    - Strict ACID transactions (Atomicity, Consistency, Isolation, Durability).
     *    - Storage Engine: InnoDB (B+Tree clustered index on Primary Key, Undo/Redo Logs, MVCC).
     *    - Best For: Financial ledgers, e-commerce checkouts, complex relational queries (JOINs).
     * 2. NoSQL (Not Only SQL):
     *    - Schema-flexible / dynamic schema. Horizontal scaling by default.
     *    - Categories:
     *      * Document Store: MongoDB (BSON documents, hierarchical nested data).
     *      * Key-Value Store: Redis (In-memory, sub-millisecond caching, session store).
     *      * Wide-Column Store: Apache Cassandra (LSM-Trees, massive write-heavy timeseries, IoT).
     *      * Graph Database: Neo4j (Nodes, Edges, Relationships, Social networks, Fraud detection).
     *
     * EXAMINER / RECRUITER GOTCHA:
     * - Scaling Comparison: SQL scales Vertically (larger CPU/RAM/SSD); NoSQL scales Horizontally (sharded commodity clusters).
     */
    static void topic1_MySQLVsNoSQL() {
        System.out.println("\n--- 1. MYSQL (RDBMS) VS NOSQL ---");

        // Ex 1.1: Relational Schema Record (Strict Schema & Foreign Key Mapping)
        record SqlUserRow(long id, String email, String country) {}
        record SqlOrderRow(long orderId, long userId, double amount) {} // Foreign Key: userId -> SqlUserRow.id

        SqlUserRow user = new SqlUserRow(101L, "dev@example.com", "USA");
        SqlOrderRow order = new SqlOrderRow(5001L, user.id(), 250.0);
        System.out.println("Ex 1.1 - RDBMS (MySQL) Normalized Relational Row with Foreign Key: User " + user.id() + " -> Order $" + order.amount());

        // Ex 1.2: Document NoSQL (MongoDB Nested BSON Document Simulator)
        class MongoDocument {
            private final Map<String, Object> doc = new LinkedHashMap<>();
            public void put(String key, Object val) { doc.put(key, val); }
            @Override public String toString() { return doc.toString(); }
        }
        MongoDocument userDoc = new MongoDocument();
        userDoc.put("_id", "obj_6619ef");
        userDoc.put("name", "Alice");
        userDoc.put("addresses", List.of(Map.of("city", "New York", "zip", "10001"), Map.of("city", "Austin", "zip", "73301")));
        System.out.println("Ex 1.2 - Document NoSQL (MongoDB Hierarchical Embedded Document): " + userDoc);

        // Ex 1.3: Key-Value NoSQL (Redis Sub-millisecond Hash Store Simulator)
        Map<String, String> redisCache = new ConcurrentHashMap<>();
        redisCache.put("session:token:9981", "{user: 'Alice', role: 'ADMIN', ttl: 3600}");
        System.out.println("Ex 1.3 - Key-Value NoSQL (Redis Session Store): " + redisCache.get("session:token:9981"));

        // Ex 1.4: Comparison Matrix
        Map<String, String> matrix = Map.of(
                "Data Model", "MySQL: Relational Tables | NoSQL: Document, Key-Value, Columnar, Graph",
                "Schema", "MySQL: Fixed/Strict DDL | NoSQL: Schema-less / Dynamic JSON",
                "Transactions", "MySQL: ACID (Immediate) | NoSQL: BASE (Eventual Consistency)",
                "Scaling", "MySQL: Vertical (Read Replicas) | NoSQL: Horizontal Distributed Sharding"
        );
        matrix.forEach((k, v) -> System.out.println("Ex 1.4 - " + k + " -> " + v));

        // Ex 1.5: Polyglot Persistence Architecture
        System.out.println("Ex 1.5 - Polyglot Persistence: Use MySQL for payments, Redis for caching, MongoDB for product catalog, Cassandra for audit logs");
    }

    // ============================================================================
    // 2. TRANSACTION
    // ============================================================================
    /**
     * ARCHITECTURAL & INTERVIEW NOTES - DATABASE TRANSACTIONS:
     * 1. Definition: A logical unit of work comprising one or more SQL operations that must either ALL succeed or ALL fail.
     * 2. Standard SQL Commands:
     *    - `START TRANSACTION` / `BEGIN`: Starts transaction boundary.
     *    - `COMMIT`: Persists all changes permanently to disk (WAL flushed).
     *    - `ROLLBACK`: Reverts all uncommitted changes back to pre-transaction state.
     *    - `SAVEPOINT savepoint_name`: Creates checkpoint within transaction for partial rollback.
     * 3. Internal Engine Mechanics (MySQL InnoDB / PostgreSQL):
     *    - Redo Log (WAL): Guarantees Durability (crash recovery replay).
     *    - Undo Log: Guarantees Atomicity (rollback generation) and MVCC read snapshots.
     */
    static void topic2_Transactions() {
        System.out.println("\n--- 2. DATABASE TRANSACTIONS ---");

        // Ex 2.1: Transaction Execution Engine Simulator with Rollback
        class BankAccountTable {
            private final Map<String, Double> accounts = new HashMap<>(Map.of("ACC_A", 1000.0, "ACC_B", 500.0));

            public boolean transferFunds(String from, String to, double amount, boolean forceCrash) {
                Map<String, Double> snapshot = new HashMap<>(accounts); // Savepoint / Undo Log
                System.out.println("  [BEGIN TRANSACTION] Snapshot captured: " + snapshot);
                try {
                    accounts.put(from, accounts.get(from) - amount);
                    if (forceCrash) throw new RuntimeException("Power outage / Network failure before commit!");
                    accounts.put(to, accounts.get(to) + amount);
                    System.out.println("  [COMMIT] Funds transferred successfully!");
                    return true;
                } catch (Exception e) {
                    accounts.clear();
                    accounts.putAll(snapshot); // ROLLBACK
                    System.out.println("  [ROLLBACK] Reverted changes due to error: " + e.getMessage());
                    return false;
                }
            }
        }
        BankAccountTable bank = new BankAccountTable();
        bank.transferFunds("ACC_A", "ACC_B", 200.0, false); // Commits
        System.out.println("Ex 2.1 - Post-commit balances: " + bank.accounts);
        bank.transferFunds("ACC_A", "ACC_B", 300.0, true);  // Rolls back
        System.out.println("Ex 2.1 - Post-rollback balances: " + bank.accounts);

        // Ex 2.2: Savepoint Partial Rollback Simulator
        class SavepointDemo {
            public List<String> executeWithSavepoint() {
                List<String> log = new ArrayList<>();
                log.add("INSERT 1");
                String savepoint = "SP_1";
                log.add("INSERT 2 (Inside Savepoint)");
                // Rollback to SP_1
                log.remove(log.size() - 1);
                log.add("INSERT 3");
                return log;
            }
        }
        System.out.println("Ex 2.2 - SAVEPOINT partial rollback: " + new SavepointDemo().executeWithSavepoint());

        // Ex 2.3: Autocommit Mode in Relational Databases
        System.out.println("Ex 2.3 - SET autocommit = 0; prevents every single SQL statement from automatically committing independently");

        // Ex 2.4: Write-Ahead Logging (WAL) & Crash Recovery
        System.out.println("Ex 2.4 - WAL (Redo Log): Engine writes changes to disk log sequentially BEFORE dirty pages are flushed to tablespace");

        // Ex 2.5: Distributed Two-Phase Commit (2PC) vs Local DB Transaction
        System.out.println("Ex 2.5 - Local DB Transactions rely on ACID; Distributed transactions across databases use Sagas or XA-2PC");
    }

    // ============================================================================
    // 3. ACID VS BASE
    // ============================================================================
    /**
     * ARCHITECTURAL & INTERVIEW NOTES - ACID VS BASE:
     * 1. ACID (Relational Database Paradigm - Strong Consistency):
     *    - Atomicity: All or nothing. (Handled via Undo Log).
     *    - Consistency: Moves database from one valid state to another, enforcing constraints (Foreign keys, Unique, Check).
     *    - Isolation: Concurrent transactions execute without interfering with one another (MVCC + Locks).
     *    - Durability: Once committed, data survives power failures and system crashes (Redo Log / WAL).
     * 2. BASE (Distributed NoSQL Paradigm - High Availability):
     *    - Basically Available: System remains operational during partitions (CAP: Availability over Consistency).
     *    - Soft State: System state may change over time even without new inputs due to background sync.
     *    - Eventual Consistency: Given no new updates, all replicas eventually converge to the same value.
     */
    static void topic3_ACIDVsBASE() {
        System.out.println("\n--- 3. ACID VS BASE ---");

        // Ex 3.1: ACID Atomicity & Consistency Enforcement
        class AcidEngine {
            public boolean executeOrder(double price, double accountBalance) {
                if (price <= 0 || accountBalance < price) {
                    return false; // Constraint Check Violation -> Abort
                }
                return true; // Atomic state transition
            }
        }
        System.out.println("Ex 3.1 - ACID Consistency Check: " + new AcidEngine().executeOrder(150.0, 500.0));

        // Ex 3.2: BASE Eventual Consistency Replicator Simulator
        class BaseReplicaSync {
            private String replicaA = "Value_V1";
            private String replicaB = "Value_V1";

            public void writeMaster(String newVal) {
                replicaA = newVal; // Primary updated immediately
            }
            public void backgroundReplicate() {
                replicaB = replicaA; // Replicas catch up asynchronously
            }
            public boolean isConsistent() { return replicaA.equals(replicaB); }
        }
        BaseReplicaSync base = new BaseReplicaSync();
        base.writeMaster("Value_V2");
        System.out.println("Ex 3.2 - BASE Soft State (Replicas divergent during sync window): Consistent=" + base.isConsistent());
        base.backgroundReplicate();
        System.out.println("Ex 3.2 - BASE Eventual Consistency reached: Consistent=" + base.isConsistent() + " (Replica=" + base.replicaB + ")");

        // Ex 3.3: Comparison Matrix
        Map<String, String> acidVsBase = Map.of(
                "Focus", "ACID: Data Accuracy & Financial Precision | BASE: High Availability & Low Latency",
                "Schema", "ACID: Rigid relational schemas | BASE: Dynamic schema-less documents",
                "Consistency", "ACID: Immediate / Linearizable | BASE: Eventual / Tunable Quorum",
                "Transactions", "ACID: Heavyweight two-phase locking | BASE: Lightweight Saga / compensation"
        );
        acidVsBase.forEach((k, v) -> System.out.println("Ex 3.3 - " + k + " -> " + v));

        // Ex 3.4: CAP Theorem Mapping
        System.out.println("Ex 3.4 - CAP Theorem: ACID databases are CP (Consistency + Partition Tolerance); BASE databases are AP (Availability + Partition Tolerance)");

        // Ex 3.5: Tunable Consistency in Cassandra (ONE, QUORUM, ALL)
        System.out.println("Ex 3.5 - Tunable Consistency: Read/Write Quorum (W + R > N) turns an AP BASE database into a strongly consistent store");
    }

    // ============================================================================
    // 4. INDEXING
    // ============================================================================
    /**
     * ARCHITECTURAL & INTERVIEW NOTES - DATABASE INDEXING:
     * 1. What is an Index: A sorted data structure that allows the database engine to find specific rows in $O(\log N)$ time
     *    instead of doing a full table scan ($O(N)$).
     * 2. Index Structures:
     *    - B+Tree (MySQL InnoDB / PostgreSQL default):
     *      * Balanced N-ary search tree.
     *      * Internal nodes store ONLY keys and child pointers (High fan-out, shallow depth: 3-4 levels for billions of rows).
     *      * Leaf nodes store keys + row pointers and are linked via Doubly-Linked List (Ultra-fast range queries `WHERE id BETWEEN 10 AND 50`).
     *    - Hash Index (Memory Engine): $O(1)$ point lookups (`WHERE id = 5`), but CANNOT do range queries.
     *    - LSM-Tree (Log-Structured Merge Tree - Cassandra/RocksDB): Optimized for ultra-fast writes (Appends to MemTable, flushes SSTables).
     * 3. Cost of Indexing: Slows down `INSERT`, `UPDATE`, `DELETE` operations because the B+Tree must be rebalanced and page splits occur.
     */
    static void topic4_Indexing() {
        System.out.println("\n--- 4. DATABASE INDEXING ---");

        // Ex 4.1: B+Tree Leaf Range Scan Simulator (Doubly-Linked Leaves)
        class BPlusTreeLeafNode {
            final List<Integer> keys;
            final List<String> rowPointers;
            BPlusTreeLeafNode next;

            BPlusTreeLeafNode(List<Integer> k, List<String> r) { this.keys = k; this.rowPointers = r; }
        }
        BPlusTreeLeafNode leaf2 = new BPlusTreeLeafNode(List.of(30, 40, 50), List.of("Row_30", "Row_40", "Row_50"));
        BPlusTreeLeafNode leaf1 = new BPlusTreeLeafNode(List.of(10, 20), List.of("Row_10", "Row_20"));
        leaf1.next = leaf2; // Doubly-linked leaf pointers

        // Range Query: WHERE id >= 10 AND id <= 40
        List<String> rangeResults = new ArrayList<>();
        BPlusTreeLeafNode curr = leaf1;
        while (curr != null) {
            for (int i = 0; i < curr.keys.size(); i++) {
                if (curr.keys.get(i) >= 10 && curr.keys.get(i) <= 40) {
                    rangeResults.add(curr.rowPointers.get(i));
                }
            }
            curr = curr.next;
        }
        System.out.println("Ex 4.1 - B+Tree Leaf Doubly-Linked Range Scan: " + rangeResults);

        // Ex 4.2: Full Table Scan O(N) vs Index Lookup O(log N)
        class QueryPlannerSimulation {
            public String choosePlan(boolean hasIndex, int totalRows) {
                if (hasIndex) {
                    int btreeDepth = (int) (Math.log(totalRows) / Math.log(100)) + 1;
                    return "INDEX SCAN: Found row in " + btreeDepth + " page reads [O(log N)]";
                }
                return "TABLE ACCESS FULL (SEQ SCAN): Scanned all " + totalRows + " rows [O(N)]";
            }
        }
        QueryPlannerSimulation planner = new QueryPlannerSimulation();
        System.out.println("Ex 4.2 - " + planner.choosePlan(false, 1000000));
        System.out.println("Ex 4.2 - " + planner.choosePlan(true, 1000000));

        // Ex 4.3: Hash Index point lookup limitation
        Map<Integer, String> hashIndex = Map.of(101, "Row_101", 102, "Row_102");
        System.out.println("Ex 4.3 - Hash Index O(1) exact match: " + hashIndex.get(101) + " (Range scans impossible with Hash Index)");

        // Ex 4.4: Leftmost Prefix Rule in Composite Indexes (Index on (A, B, C))
        System.out.println("Ex 4.4 - Composite Index (dept, salary, age): 'WHERE dept=5 AND salary>1000' uses index; 'WHERE salary>1000' CANNOT use index!");

        // Ex 4.5: Covering Index (Index-Only Scan)
        System.out.println("Ex 4.5 - Covering Index: When all queried SELECT columns exist inside the B+Tree index itself, zero disk table lookup is required");
    }

    // ============================================================================
    // 5. PARTITIONING
    // ============================================================================
    /**
     * ARCHITECTURAL & INTERVIEW NOTES - DATABASE PARTITIONING:
     * 1. Definition: Splitting a single large table within the SAME database instance into smaller, manageable chunks.
     * 2. Partitioning Types:
     *    - Range Partitioning: Partitions based on value ranges (e.g. `order_date` by Year/Month: `PARTITION p2024 VALUES LESS THAN (2025)`).
     *    - List Partitioning: Partitions based on a discrete list of values (e.g. `country_code IN ('US', 'CA')`).
     *    - Hash Partitioning: Distributes rows uniformly using `HASH(id) % N` to avoid hot partitions.
     *    - Key Partitioning: MySQL internal hashing on primary key.
     * 3. Partition Pruning:
     *    - The query optimizer examines the `WHERE` clause and scans ONLY the relevant partition, ignoring the rest!
     */
    static void topic5_Partitioning() {
        System.out.println("\n--- 5. DATABASE PARTITIONING ---");

        // Ex 5.1: Range Partitioning Engine Simulator with Partition Pruning
        class PartitionedOrderTable {
            private final Map<String, List<String>> partitions = Map.of(
                    "p_2024", new ArrayList<>(List.of("Order_2024_01", "Order_2024_02")),
                    "p_2025", new ArrayList<>(List.of("Order_2025_01")),
                    "p_2026", new ArrayList<>(List.of("Order_2026_01", "Order_2026_02"))
            );

            public List<String> queryOrdersByYear(int year) {
                String targetPartition = "p_" + year;
                System.out.println("  [Partition Pruning Active] Scanned ONLY: " + targetPartition + " (Skipped other " + (partitions.size() - 1) + " partitions)");
                return partitions.getOrDefault(targetPartition, List.of());
            }
        }
        PartitionedOrderTable table = new PartitionedOrderTable();
        System.out.println("Ex 5.1 - Query for Year 2026 result: " + table.queryOrdersByYear(2026));

        // Ex 5.2: List Partitioning by Geographical Region
        class ListPartitionManager {
            public String resolvePartition(String region) {
                return switch (region.toUpperCase()) {
                    case "US", "CA" -> "p_north_america";
                    case "UK", "DE", "FR" -> "p_europe";
                    default -> "p_asia_pacific";
                };
            }
        }
        System.out.println("Ex 5.2 - List Partition for 'DE': " + new ListPartitionManager().resolvePartition("DE"));

        // Ex 5.3: Hash Partitioning for Uniform Row Distribution
        int rowId = 88921;
        int partitionCount = 8;
        int targetPartition = Math.abs(Integer.hashCode(rowId)) % partitionCount;
        System.out.println("Ex 5.3 - Hash Partition target: HASH(" + rowId + ") % 8 = Partition #" + targetPartition);

        // Ex 5.4: Horizontal Partitioning vs Vertical Partitioning
        System.out.println("Ex 5.4 - Horizontal Partitioning: Splits rows into separate tables | Vertical Partitioning: Splits columns (BLOB/CLOB moved to separate table)");

        // Ex 5.5: Partition Maintenance (Fast Drop Partitions)
        System.out.println("Ex 5.5 - Data Archival: 'ALTER TABLE orders DROP PARTITION p_2020;' drops 100M rows instantly without generating massive Undo logs");
    }

    // ============================================================================
    // 6. SHARDING
    // ============================================================================
    /**
     * ARCHITECTURAL & INTERVIEW NOTES - DATABASE SHARDING:
     * - Partitioning vs Sharding:
     *   * Partitioning: Splits tables within the SAME physical database instance.
     *   * Sharding: Splits tables ACROSS DIFFERENT physical database servers/clusters (Horizontal Scaling).
     * - Shard Key: The attribute used to route queries to the correct server (e.g. `user_id`, `tenant_id`).
     * - Sharding Challenges:
     *   * Cross-shard JOINs are computationally expensive and network intensive.
     *   * Cross-shard Distributed Transactions require 2PC or Sagas.
     *   * Re-sharding when scaling from N to N+1 nodes (Consistent Hashing minimizes re-mapping).
     */
    static void topic6_Sharding() {
        System.out.println("\n--- 6. DATABASE SHARDING ---");

        // Ex 6.1: Cross-Database Shard Coordinator Simulator
        class ShardedCluster {
            private final Map<Integer, Map<String, String>> physicalShards = Map.of(
                    0, new ConcurrentHashMap<>(), // Node 1 (192.168.1.10)
                    1, new ConcurrentHashMap<>(), // Node 2 (192.168.1.11)
                    2, new ConcurrentHashMap<>()  // Node 3 (192.168.1.12)
            );

            public void insert(String shardKey, String value) {
                int shardId = Math.abs(shardKey.hashCode()) % physicalShards.size();
                physicalShards.get(shardId).put(shardKey, value);
            }
            public String find(String shardKey) {
                int shardId = Math.abs(shardKey.hashCode()) % physicalShards.size();
                return "Fetched from Node #" + shardId + ": " + physicalShards.get(shardId).get(shardKey);
            }
        }
        ShardedCluster cluster = new ShardedCluster();
        cluster.insert("user_alpha", "Profile Alpha");
        cluster.insert("user_beta", "Profile Beta");
        System.out.println("Ex 6.1 - " + cluster.find("user_alpha"));
        System.out.println("Ex 6.1 - " + cluster.find("user_beta"));

        // Ex 6.2: Entity-Group Sharding (Co-locating Orders with Customers)
        System.out.println("Ex 6.2 - Co-located Shard Key: Using 'customer_id' for both Customer and Order tables ensures orders reside on the SAME physical shard node");

        // Ex 6.3: Handling Hotspot / Celebrity Shard Problem
        System.out.println("Ex 6.3 - Hot Shard Fix: For high-traffic influencers, append random salt 'celebrity_id_0' to 'celebrity_id_9' to stripe traffic across shards");

        // Ex 6.4: Directory-Based Sharding Lookup Service
        Map<String, String> shardDirectory = Map.of("tenant_acme", "shard_db_cluster_03", "tenant_globex", "shard_db_cluster_01");
        System.out.println("Ex 6.4 - Directory Lookup: 'tenant_acme' mapped to " + shardDirectory.get("tenant_acme"));

        // Ex 6.5: Partitioning vs Sharding Summary
        System.out.println("Ex 6.5 - Partitioning = 1 Server / Multiple files | Sharding = Multiple Servers / Distributed Cluster");
    }

    // ============================================================================
    // 7. DIFF B/W WHERE AND HAVING
    // ============================================================================
    /**
     * ARCHITECTURAL & INTERVIEW NOTES - WHERE VS HAVING:
     * 1. WHERE Clause:
     *    - Filters individual ROWS *before* data is grouped or aggregated.
     *    - CANNOT use aggregate functions (`WHERE SUM(salary) > 5000` is SYNTAX ERROR).
     *    - Uses B+Tree indexes directly to prune table scans.
     * 2. HAVING Clause:
     *    - Filters GROUPS *after* `GROUP BY` and aggregate functions have executed.
     *    - Can use aggregate functions (`HAVING COUNT(*) > 5`, `HAVING AVG(salary) > 80000`).
     *    - Operates on intermediate in-memory temporary tables / hash tables created by GROUP BY.
     * 3. SQL Query Execution Order:
     *    `FROM` ➔ `JOIN` ➔ `WHERE` ➔ `GROUP BY` ➔ `HAVING` ➔ `SELECT` ➔ `DISTINCT` ➔ `ORDER BY` ➔ `LIMIT / OFFSET`.
     */
    static void topic7_WhereVsHaving() {
        System.out.println("\n--- 7. DIFFERENCE BETWEEN WHERE AND HAVING ---");

        record Employee(String name, String department, double salary, boolean active) {}
        List<Employee> employees = List.of(
                new Employee("Alice", "Engineering", 120000, true),
                new Employee("Bob", "Engineering", 110000, true),
                new Employee("Charlie", "HR", 60000, true),
                new Employee("Dave", "HR", 65000, false), // inactive
                new Employee("Eve", "Marketing", 90000, true)
        );

        // Ex 7.1: WHERE filtering rows before aggregation (active = true)
        List<Employee> whereFiltered = employees.stream().filter(Employee::active).toList();
        System.out.println("Ex 7.1 - WHERE (active = true) filtered out inactive Dave: count=" + whereFiltered.size());

        // Ex 7.2: GROUP BY + HAVING (Filtering aggregated department average salary > $100k)
        Map<String, Double> deptAvgSalaries = whereFiltered.stream()
                .collect(Collectors.groupingBy(Employee::department, Collectors.averagingDouble(Employee::salary)));

        Map<String, Double> havingFiltered = deptAvgSalaries.entrySet().stream()
                .filter(e -> e.getValue() > 100000.0) // HAVING AVG(salary) > 100000
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        System.out.println("Ex 7.2 - GROUP BY department averages: " + deptAvgSalaries);
        System.out.println("Ex 7.2 - HAVING AVG(salary) > 100,000 result: " + havingFiltered);

        // Ex 7.3: Syntax Error Demonstration explanation
        System.out.println("Ex 7.3 - Invalid SQL: 'SELECT dept FROM emp WHERE AVG(salary) > 5000' fails because WHERE evaluates before aggregation occurs!");

        // Ex 7.4: Combining WHERE and HAVING in the same SQL query
        System.out.println("Ex 7.4 - Standard Query: 'SELECT dept, AVG(salary) FROM emp WHERE active=1 GROUP BY dept HAVING AVG(salary) > 80000'");

        // Ex 7.5: Performance rule: Always filter rows in WHERE first
        System.out.println("Ex 7.5 - Performance Tip: Always filter out rows in WHERE clause early to reduce the volume of rows fed into GROUP BY memory");
    }

    // ============================================================================
    // 8. DELETE VS DROP VS TRUNCATE
    // ============================================================================
    /**
     * ARCHITECTURAL & INTERVIEW NOTES - DELETE VS DROP VS TRUNCATE:
     * 1. DELETE (DML - Data Manipulation Language):
     *    - Deletes specific rows matching `WHERE` condition (or all rows if no WHERE).
     *    - Row-by-row logging in Undo/Redo logs. Slow for large tables.
     *    - CAN be rolled back inside a transaction.
     *    - Fires Database Triggers (`ON DELETE`). Does NOT reset `AUTO_INCREMENT` counter.
     * 2. TRUNCATE (DDL - Data Definition Language):
     *    - Deletes ALL rows by deallocating and recreating data pages/extents.
     *    - Extremely FAST. Minimal logging (logs page deallocations, not individual rows).
     *    - Resets `AUTO_INCREMENT` counter back to 1. Does NOT fire triggers.
     *    - In MySQL, implicitly commits active transaction and cannot be rolled back!
     * 3. DROP (DDL - Data Definition Language):
     *    - Completely removes the table definition, data, indexes, constraints, and triggers from database catalog.
     */
    static void topic8_DeleteVsDropVsTruncate() {
        System.out.println("\n--- 8. DELETE VS DROP VS TRUNCATE ---");

        // Ex 8.1: Comparison Matrix
        Map<String, String> comparison = Map.of(
                "DELETE", "DML | Can use WHERE | Slow (Row-by-row Undo logs) | Rollbackable | Fires Triggers | Retains Auto-Increment",
                "TRUNCATE", "DDL | No WHERE clause | Ultra-Fast (Page deallocation) | Resets Auto-Increment to 1 | No Triggers",
                "DROP", "DDL | Destroys table structure + data + metadata completely from DB catalog"
        );
        comparison.forEach((k, v) -> System.out.println("Ex 8.1 - " + k + " -> " + v));

        // Ex 8.2: DELETE simulator with WHERE filter & Rollback
        List<String> deleteTable = new ArrayList<>(List.of("Row_1", "Row_2", "Row_3"));
        deleteTable.removeIf(r -> r.equals("Row_2")); // DELETE FROM table WHERE id = 2;
        System.out.println("Ex 8.2 - DELETE with WHERE result: " + deleteTable);

        // Ex 8.3: TRUNCATE simulator (Re-initializing storage collection & resetting sequence)
        class TruncateTableSimulator {
            List<String> rows = new ArrayList<>(List.of("DataA", "DataB", "DataC"));
            int autoIncrement = 50;
            void truncate() {
                rows.clear(); // Deallocates pages
                autoIncrement = 1; // Resets counter
            }
        }
        TruncateTableSimulator truncTable = new TruncateTableSimulator();
        truncTable.truncate();
        System.out.println("Ex 8.3 - TRUNCATE wiped data & reset autoIncrement: size=" + truncTable.rows.size() + ", nextId=" + truncTable.autoIncrement);

        // Ex 8.4: DROP Table Simulator
        class DatabaseSchemaSimulator {
            Map<String, Object> tables = new HashMap<>(Map.of("orders", new Object()));
            void dropTable(String tableName) { tables.remove(tableName); }
        }
        DatabaseSchemaSimulator db = new DatabaseSchemaSimulator();
        db.dropTable("orders");
        System.out.println("Ex 8.4 - DROP TABLE removed metadata definition from catalog: exists=" + db.tables.containsKey("orders"));

        // Ex 8.5: Foreign Key constraint restriction gotcha
        System.out.println("Ex 8.5 - Gotcha: TRUNCATE fails if the table is referenced by an active FOREIGN KEY constraint in another table");
    }

    // ============================================================================
    // 9. GROUPBY & SWITCHCASE (CASE WHEN)
    // ============================================================================
    /**
     * ARCHITECTURAL & INTERVIEW NOTES - GROUP BY & CASE WHEN:
     * 1. GROUP BY:
     *    - Groups rows that have the same values in specified columns into summary rows.
     *    - Any column in `SELECT` that is NOT in `GROUP BY` MUST be wrapped in an Aggregate function (`SUM`, `COUNT`, `AVG`).
     * 2. CASE WHEN ... THEN ... ELSE ... END:
     *    - Conditional logic expression inside SQL queries (Equivalent to `if-else` or `switch-case`).
     *    - Used for conditional aggregations: `SUM(CASE WHEN status='PAID' THEN amount ELSE 0 END)`.
     *    - Used for dynamic column categorization and data pivot transformations.
     */
    static void topic9_GroupByAndCaseWhen() {
        System.out.println("\n--- 9. GROUP BY & CASE WHEN ---");

        record Order(String category, double amount, String status) {}
        List<Order> orders = List.of(
                new Order("Electronics", 500.0, "PAID"),
                new Order("Electronics", 200.0, "REFUNDED"),
                new Order("Books", 50.0, "PAID"),
                new Order("Books", 30.0, "PAID")
        );

        // Ex 9.1: GROUP BY Category with Total Count and Revenue
        Map<String, Double> categoryRevenue = orders.stream()
                .collect(Collectors.groupingBy(Order::category, Collectors.summingDouble(Order::amount)));
        System.out.println("Ex 9.1 - GROUP BY category with SUM(amount): " + categoryRevenue);

        // Ex 9.2: SQL CASE WHEN Categorization Simulation
        class SqlCaseWhenClassifier {
            public String classifyAmount(double amt) {
                // CASE WHEN amount >= 500 THEN 'HIGH' WHEN amount >= 100 THEN 'MEDIUM' ELSE 'LOW' END
                if (amt >= 500.0) return "HIGH_VALUE";
                if (amt >= 100.0) return "MEDIUM_VALUE";
                return "LOW_VALUE";
            }
        }
        Map<String, String> orderTiers = orders.stream()
                .collect(Collectors.toMap(o -> o.category() + "_" + o.amount(), o -> new SqlCaseWhenClassifier().classifyAmount(o.amount()), (a, b) -> a));
        System.out.println("Ex 9.2 - SQL CASE WHEN classification: " + orderTiers);

        // Ex 9.3: Conditional Pivot Aggregation with SUM(CASE WHEN ...)
        double paidRevenue = orders.stream()
                .mapToDouble(o -> "PAID".equals(o.status()) ? o.amount() : 0.0)
                .sum();
        System.out.println("Ex 9.3 - Conditional Aggregation: SUM(CASE WHEN status='PAID' THEN amount ELSE 0 END) = $" + paidRevenue);

        // Ex 9.4: Simple CASE vs Searched CASE
        System.out.println("Ex 9.4 - Simple CASE (CASE gender WHEN 'M' THEN 1 ...) vs Searched CASE (CASE WHEN age > 65 THEN 'Senior' ...)");

        // Ex 9.5: Only_Full_Group_By SQL Mode
        System.out.println("Ex 9.5 - MySQL ONLY_FULL_GROUP_BY error: Prevents selecting non-aggregated columns that are not part of the GROUP BY clause");
    }

    // ============================================================================
    // 10. AGGREGATE FUNCTIONS (MIN, MAX, COUNT, SUM, AVG)
    // ============================================================================
    /**
     * ARCHITECTURAL & INTERVIEW NOTES - AGGREGATE FUNCTIONS:
     * - Performs a calculation on a set of values and returns a single scalar value.
     * - `COUNT(*)`: Counts total rows including NULLs.
     * - `COUNT(column)`: Counts non-NULL rows in that specific column.
     * - `COUNT(DISTINCT column)`: Counts unique non-NULL values.
     * - `SUM(column)` / `AVG(column)`: Ignores NULL values.
     * - `MIN(column)` / `MAX(column)`: Uses index directly for instant $O(1)$ lookup if index exists on column.
     */
    static void topic10_AggregateFunctions() {
        System.out.println("\n--- 10. AGGREGATE FUNCTIONS (MIN, MAX, COUNT, SUM, AVG) ---");

        List<Double> salaries = Arrays.asList(50000.0, 75000.0, 120000.0, null, 95000.0);

        // Ex 10.1: COUNT(*) vs COUNT(column)
        long countStar = salaries.size(); // 5
        long countCol = salaries.stream().filter(Objects::nonNull).count(); // 4 (Ignores NULL)
        System.out.println("Ex 10.1 - COUNT(*) = " + countStar + " | COUNT(salary) [Ignores NULL] = " + countCol);

        // Ex 10.2: SUM and AVG calculations
        double sum = salaries.stream().filter(Objects::nonNull).mapToDouble(Double::doubleValue).sum();
        double avg = salaries.stream().filter(Objects::nonNull).mapToDouble(Double::doubleValue).average().orElse(0.0);
        System.out.println("Ex 10.2 - SUM(salary) = $" + sum + " | AVG(salary) = $" + avg);

        // Ex 10.3: MIN and MAX calculations
        double min = salaries.stream().filter(Objects::nonNull).mapToDouble(Double::doubleValue).min().orElse(0.0);
        double max = salaries.stream().filter(Objects::nonNull).mapToDouble(Double::doubleValue).max().orElse(0.0);
        System.out.println("Ex 10.3 - MIN(salary) = $" + min + " | MAX(salary) = $" + max);

        // Ex 10.4: COUNT(DISTINCT column)
        List<String> departments = List.of("IT", "HR", "IT", "Sales", "HR");
        long distinctDeptCount = departments.stream().distinct().count();
        System.out.println("Ex 10.4 - COUNT(DISTINCT dept) = " + distinctDeptCount + " (Unique: " + departments.stream().distinct().toList() + ")");

        // Ex 10.5: Index optimization on MIN / MAX
        System.out.println("Ex 10.5 - B+Tree Index Optimization: 'SELECT MAX(id) FROM users' performs a single seek to the rightmost leaf node in O(1)");
    }

    // ============================================================================
    // 11. NORMALIZATION
    // ============================================================================
    /**
     * ARCHITECTURAL & INTERVIEW NOTES - DATABASE NORMALIZATION:
     * 1. Definition: The systematic process of organizing data in a relational database to minimize data redundancy
     *    and eliminate Update, Insertion, and Deletion anomalies.
     * 2. The 3 Anomalies Prevented:
     *    - Insertion Anomaly: Cannot insert a student without assigning them to a course.
     *    - Deletion Anomaly: Deleting the last student enrolled in a course unintentionally deletes the course details.
     *    - Update Anomaly: Changing a department head requires updating 10,000 employee rows; missing one causes data corruption.
     * 3. Denormalization: Intentionally introducing controlled redundancy in Read-heavy OLAP systems to avoid costly JOINs.
     */
    static void topic11_Normalization() {
        System.out.println("\n--- 11. DATABASE NORMALIZATION ---");

        // Ex 11.1: Un-normalized Table (Suffers from Update & Deletion Anomalies)
        record UnnormalizedRow(int empId, String empName, String deptName, String deptHead) {}
        List<UnnormalizedRow> unnormalized = List.of(
                new UnnormalizedRow(1, "Alice", "Engineering", "John Smith"),
                new UnnormalizedRow(2, "Bob", "Engineering", "John Smith") // Redundant deptHead!
        );
        System.out.println("Ex 11.1 - Un-normalized table contains duplicated deptHead: " + unnormalized.get(0).deptHead());

        // Ex 11.2: Normalized Relational Model (3NF)
        record Department(int deptId, String deptName, String deptHead) {}
        record NormalizedEmployee(int empId, String empName, int deptId) {}

        Department dept = new Department(10, "Engineering", "John Smith");
        List<NormalizedEmployee> normalizedEmployees = List.of(
                new NormalizedEmployee(1, "Alice", dept.deptId()),
                new NormalizedEmployee(2, "Bob", dept.deptId())
        );
        System.out.println("Ex 11.2 - Normalized: Changing deptHead requires updating exactly 1 row in Department table!");

        // Ex 11.3: Update Anomaly Elimination Demo
        Department updatedDept = new Department(10, "Engineering", "Sarah Connor"); // Single update!
        System.out.println("Ex 11.3 - Updated Department Head to: " + updatedDept.deptHead() + " (Employees automatically reflect update)");

        // Ex 11.4: Normalization vs Denormalization Trade-off
        System.out.println("Ex 11.4 - Trade-off: Normalization optimizes for WRITES (Zero redundancy) | Denormalization optimizes for READS (Zero JOINs)");

        // Ex 11.5: Read Heavy Caching / Read View Denormalization
        System.out.println("Ex 11.5 - OLTP transactions use 3NF; Data Warehouses (Snowflake, BigQuery) denormalize into Star/Snowflake schemas");
    }

    // ============================================================================
    // 12. NORMAL FORMS (1NF, 2NF, 3NF, BCNF)
    // ============================================================================
    /**
     * ARCHITECTURAL & INTERVIEW NOTES - NORMAL FORMS DEEP DIVE:
     * 1. 1NF (First Normal Form):
     *    - Each column must contain Atomic (indivisible) values (No comma-separated lists `skills: Java, Python`).
     *    - Each record must be unique (Primary Key defined).
     * 2. 2NF (Second Normal Form):
     *    - Must be in 1NF.
     *    - Eliminates Partial Dependency: All non-key columns must depend on the WHOLE composite primary key (not just part of it).
     * 3. 3NF (Third Normal Form):
     *    - Must be in 2NF.
     *    - Eliminates Transitive Dependency: Non-key columns must NOT depend on other non-key columns ($A \rightarrow B \rightarrow C$).
     *    - "Every non-key attribute must provide a fact about the key, the whole key, and nothing but the key, so help me Codd."
     * 4. BCNF (Boyce-Codd Normal Form / 3.5NF):
     *    - Strict version of 3NF. For every functional dependency $X \rightarrow Y$, $X$ MUST be a Super Key / Candidate Key.
     */
    static void topic12_NormalForms1NFToBCNF() {
        System.out.println("\n--- 12. NORMAL FORMS (1NF, 2NF, 3NF, BCNF) ---");

        // Ex 12.1: 1NF Atomic Values Rule
        // Non-1NF: { id: 1, name: "Alice", phones: "111-222, 333-444" } -> Violates Atomicity!
        record PhoneEntry1NF(long userId, String phoneNumber) {}
        List<PhoneEntry1NF> phoneEntries = List.of(new PhoneEntry1NF(1, "111-222"), new PhoneEntry1NF(1, "333-444"));
        System.out.println("Ex 12.1 - 1NF Enforces Atomic values across rows: " + phoneEntries);

        // Ex 12.2: 2NF Partial Dependency Removal
        // Composite Key: (StudentID, CourseID). Column: CourseFee depends ONLY on CourseID (Partial Dependency)!
        // Solution: Split into StudentEnrollment(StudentID, CourseID) and Course(CourseID, CourseFee).
        System.out.println("Ex 12.2 - 2NF: Eliminated Partial Dependency by splitting CourseFee into dedicated Course table");

        // Ex 12.3: 3NF Transitive Dependency Removal
        // EmpID -> DeptID -> DeptCity (EmpID determines DeptCity transitively through DeptID).
        // Solution: Split into Employee(EmpID, DeptID) and Department(DeptID, DeptCity).
        System.out.println("Ex 12.3 - 3NF: Eliminated Transitive Dependency (Non-key column DeptCity moved to Department table)");

        // Ex 12.4: BCNF Super Key Requirement
        System.out.println("Ex 12.4 - BCNF (Boyce-Codd): Enforces that in every functional dependency X -> Y, X must be a Candidate Key");

        // Ex 12.5: Summary Checklist
        List<String> checklist = List.of(
                "1NF: Atomic columns + Unique primary key",
                "2NF: 1NF + No Partial functional dependencies",
                "3NF: 2NF + No Transitive dependencies",
                "BCNF: 3NF + Every determinant is a Candidate Key"
        );
        checklist.forEach(rule -> System.out.println("Ex 12.5 - " + rule));
    }

    // ============================================================================
    // 13. LAZY LOADING VS EAGER LOADING
    // ============================================================================
    /**
     * ARCHITECTURAL & INTERVIEW NOTES - LAZY VS EAGER LOADING:
     * 1. Eager Loading (`FetchType.EAGER`):
     *    - Loads associated child entities immediately in the initial database query using an SQL `LEFT JOIN`.
     *    - Risk: Leads to memory bloat and performance degradation if associations are large.
     * 2. Lazy Loading (`FetchType.LAZY`):
     *    - Delays loading child entities until they are explicitly accessed (`order.getItems()`).
     *    - Hibernate injects a dynamic CGLIB / ByteBuddy Proxy.
     *    - Risk: Triggers the **N+1 Query Problem** if accessed inside a loop.
     *    - Risk: `LazyInitializationException` occurs if accessed after Hibernate Session / EntityManager is closed!
     */
    static void topic13_LazyVsEagerLoading() {
        System.out.println("\n--- 13. LAZY LOADING VS EAGER LOADING ---");

        // Ex 13.1: Eager Loading Simulator (Single JOIN query fetches all data upfront)
        class EagerCustomer {
            String name = "Alice";
            List<String> orders = List.of("ORD-1", "ORD-2"); // Loaded immediately via SQL JOIN
        }
        System.out.println("Ex 13.1 - Eager Loading fetched Customer and Orders in 1 SQL JOIN: " + new EagerCustomer().orders);

        // Ex 13.2: Lazy Loading Proxy Simulator (Defers SQL query until getter invocation)
        class LazyOrdersProxy {
            private List<String> loadedOrders = null;
            public List<String> getOrders() {
                if (loadedOrders == null) {
                    System.out.println("  [Lazy Proxy Intercept] Fired SQL: SELECT * FROM orders WHERE customer_id = 101");
                    loadedOrders = List.of("ORD-101_A", "ORD-101_B");
                }
                return loadedOrders;
            }
        }
        LazyOrdersProxy lazyProxy = new LazyOrdersProxy();
        System.out.println("Ex 13.2 - Lazy proxy instantiated (Zero initial SQL queries)");
        System.out.println("Ex 13.2 - Calling getOrders(): " + lazyProxy.getOrders());

        // Ex 13.3: LazyInitializationException Simulator (Session Closed Gotcha)
        class HibernateSessionSimulator {
            boolean isSessionOpen = false;
            public void accessLazyField() {
                if (!isSessionOpen) {
                    throw new RuntimeException("org.hibernate.LazyInitializationException: could not initialize proxy - no Session");
                }
            }
        }
        try {
            new HibernateSessionSimulator().accessLazyField();
        } catch (RuntimeException e) {
            System.out.println("Ex 13.3 - Caught: " + e.getMessage());
        }

        // Ex 13.4: JPA Default Fetch Types Gotcha
        Map<String, String> jpaDefaults = Map.of(
                "@OneToMany / @ManyToMany", "Default is FetchType.LAZY (Safe)",
                "@ManyToOne / @OneToOne", "Default is FetchType.EAGER (Dangerous! Must explicitly override to LAZY)"
        );
        jpaDefaults.forEach((k, v) -> System.out.println("Ex 13.4 - " + k + " -> " + v));

        // Ex 13.5: Best Practice Solution (JOIN FETCH / @EntityGraph)
        System.out.println("Ex 13.5 - Solution: Keep all associations LAZY in entity model; use 'JOIN FETCH' dynamically in repository queries when needed");
    }

    // ============================================================================
    // 14. SQL JOINS (INNER, LEFT, RIGHT, FULL, SELF, CROSS)
    // ============================================================================
    /**
     * ARCHITECTURAL & INTERVIEW NOTES - SQL JOINS:
     * 1. INNER JOIN: Returns only rows where there is a match in BOTH tables.
     * 2. LEFT (OUTER) JOIN: Returns ALL rows from the Left table + matched rows from the Right table (NULLs for unmatched).
     * 3. RIGHT (OUTER) JOIN: Returns ALL rows from the Right table + matched rows from the Left table.
     * 4. FULL (OUTER) JOIN: Returns rows when there is a match in EITHER table (Union of Left + Right).
     * 5. SELF JOIN: A table joined with itself (Hierarchical parent-child, e.g., Employee -> Manager).
     * 6. CROSS JOIN: Cartesian product of both tables ($M \times N$ rows).
     */
    static void topic14_SQLJoins() {
        System.out.println("\n--- 14. SQL JOINS ---");

        record User(int id, String name) {}
        record Order(int orderId, int userId, String item) {}

        List<User> users = List.of(new User(1, "Alice"), new User(2, "Bob"), new User(3, "Charlie")); // Charlie has 0 orders
        List<Order> orders = List.of(new Order(101, 1, "Laptop"), new Order(102, 1, "Mouse"), new Order(103, 2, "Keyboard"), new Order(104, 99, "Monitor")); // 99 has no user

        // Ex 14.1: INNER JOIN (Intersection)
        List<String> innerJoin = new ArrayList<>();
        for (var u : users) {
            for (var o : orders) {
                if (u.id() == o.userId()) innerJoin.add(u.name() + " -> " + o.item());
            }
        }
        System.out.println("Ex 14.1 - INNER JOIN (Only matching): " + innerJoin);

        // Ex 14.2: LEFT JOIN (All users + matching orders or NULL)
        List<String> leftJoin = new ArrayList<>();
        for (var u : users) {
            boolean matched = false;
            for (var o : orders) {
                if (u.id() == o.userId()) {
                    leftJoin.add(u.name() + " -> " + o.item());
                    matched = true;
                }
            }
            if (!matched) leftJoin.add(u.name() + " -> NULL");
        }
        System.out.println("Ex 14.2 - LEFT JOIN (Retains unmatched Charlie): " + leftJoin);

        // Ex 14.3: RIGHT JOIN (All orders + matching users or NULL)
        List<String> rightJoin = new ArrayList<>();
        for (var o : orders) {
            String userName = users.stream().filter(u -> u.id() == o.userId()).map(User::name).findFirst().orElse("NULL");
            rightJoin.add(o.item() + " -> User: " + userName);
        }
        System.out.println("Ex 14.3 - RIGHT JOIN (Retains orphan order Monitor): " + rightJoin);

        // Ex 14.4: SELF JOIN (Employee -> Manager Hierarchy)
        record EmployeeNode(int id, String name, Integer managerId) {}
        List<EmployeeNode> staff = List.of(
                new EmployeeNode(1, "CEO Alex", null),
                new EmployeeNode(2, "Dev Manager Bob", 1),
                new EmployeeNode(3, "Engineer Charlie", 2)
        );
        List<String> selfJoinResults = staff.stream()
                .filter(e -> e.managerId() != null)
                .map(e -> e.name() + " reports to " + staff.stream().filter(m -> m.id() == e.managerId()).findFirst().get().name())
                .toList();
        System.out.println("Ex 14.4 - SELF JOIN (Manager Hierarchy): " + selfJoinResults);

        // Ex 14.5: Database Join Algorithms under the hood (Nested Loop vs Hash Join vs Merge Join)
        System.out.println("Ex 14.5 - DB Join Algorithms: Nested Loop (Index lookups), Hash Join (Large unsorted sets in memory), Merge Join (Pre-sorted indexes)");
    }

    // ============================================================================
    // 15. FIND 2ND HIGHEST SALARY
    // ============================================================================
    /**
     * ARCHITECTURAL & INTERVIEW NOTES - 2ND HIGHEST SALARY (FAANG CLASSIC):
     * Multiple Standard Approaches:
     * 1. Subquery with MAX():
     *    `SELECT MAX(salary) FROM Employee WHERE salary < (SELECT MAX(salary) FROM Employee);`
     * 2. LIMIT and OFFSET with DISTINCT:
     *    `SELECT DISTINCT salary FROM Employee ORDER BY salary DESC LIMIT 1 OFFSET 1;`
     * 3. Window Function (DENSE_RANK()):
     *    `WITH Ranked AS (SELECT salary, DENSE_RANK() OVER (ORDER BY salary DESC) as rnk FROM Employee) SELECT salary FROM Ranked WHERE rnk = 2;`
     *
     * EXAMINER GOTCHA:
     * - What if there are ties (e.g. two employees share the highest salary $120k)?
     *   `ROW_NUMBER()` fails! `LIMIT 1 OFFSET 1` without `DISTINCT` fails! `DENSE_RANK()` and `MAX() < MAX()` handle ties properly!
     */
    static void topic15_FindSecondHighestSalary() {
        System.out.println("\n--- 15. FIND 2ND HIGHEST SALARY ---");

        record EmpSalary(String name, double salary) {}
        List<EmpSalary> employees = List.of(
                new EmpSalary("Alice", 120000.0), // Rank 1 (Tie)
                new EmpSalary("Bob", 120000.0),   // Rank 1 (Tie)
                new EmpSalary("Charlie", 95000.0),// Rank 2 (Target!)
                new EmpSalary("Dave", 80000.0),   // Rank 3
                new EmpSalary("Eve", 60000.0)
        );

        // Ex 15.1: Approach 1 - Subquery MAX() Simulation
        double maxSalary = employees.stream().mapToDouble(EmpSalary::salary).max().orElse(0.0);
        double secondMaxSubquery = employees.stream()
                .mapToDouble(EmpSalary::salary)
                .filter(s -> s < maxSalary) // WHERE salary < (SELECT MAX(salary))
                .max()
                .orElse(0.0);
        System.out.println("Ex 15.1 - Approach 1 (MAX subquery): $" + secondMaxSubquery);

        // Ex 15.2: Approach 2 - DISTINCT + LIMIT 1 OFFSET 1
        Double secondDistinctLimit = employees.stream()
                .map(EmpSalary::salary)
                .distinct()
                .sorted(Comparator.reverseOrder())
                .skip(1) // OFFSET 1
                .findFirst() // LIMIT 1
                .orElse(null);
        System.out.println("Ex 15.2 - Approach 2 (DISTINCT ORDER BY DESC LIMIT 1 OFFSET 1): $" + secondDistinctLimit);

        // Ex 15.3: Approach 3 - DENSE_RANK() Window Function Simulator
        List<Double> distinctSorted = employees.stream().map(EmpSalary::salary).distinct().sorted(Comparator.reverseOrder()).toList();
        Map<Double, Integer> denseRankMap = new HashMap<>();
        for (int i = 0; i < distinctSorted.size(); i++) denseRankMap.put(distinctSorted.get(i), i + 1);

        Double secondDenseRankSalary = denseRankMap.entrySet().stream()
                .filter(e -> e.getValue() == 2) // WHERE rnk = 2
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(null);
        System.out.println("Ex 15.3 - Approach 3 (DENSE_RANK window function handles ties): $" + secondDenseRankSalary);

        // Ex 15.4: N-th Highest Salary Generic Formula
        System.out.println("Ex 15.4 - Generic N-th Highest Salary: 'SELECT DISTINCT salary FROM Employee ORDER BY salary DESC LIMIT 1 OFFSET (N-1)'");

        // Ex 15.5: Handling Table with < 2 Records (Returning NULL)
        List<Double> singleEmp = List.of(50000.0);
        Double secondOrNull = singleEmp.stream().distinct().sorted(Comparator.reverseOrder()).skip(1).findFirst().orElse(null);
        System.out.println("Ex 15.5 - Return NULL when table has only 1 row: " + secondOrNull);
    }

    // ============================================================================
    // 16. JOIN BETWEEN 3 TABLES
    // ============================================================================
    /**
     * ARCHITECTURAL & INTERVIEW NOTES - 3-TABLE JOINS:
     * - Standard Many-to-Many Bridge Table structure:
     *   `Customer (1)` ➔ `Orders (M)` ➔ `Order_Items (M)` ➔ `Products (1)`
     * - Syntax:
     *   ```sql
     *   SELECT c.name, o.order_date, p.product_name, oi.quantity
     *   FROM Customers c
     *   INNER JOIN Orders o ON c.customer_id = o.customer_id
     *   INNER JOIN Order_Items oi ON o.order_id = oi.order_id
     *   INNER JOIN Products p ON oi.product_id = p.product_id;
     *   ```
     * - Join Elimination & Index Pushdown: Optimizers evaluate join order (e.g. smallest filtered table first) to minimize intermediate row sets.
     */
    static void topic16_JoinBetweenThreeTables() {
        System.out.println("\n--- 16. JOIN BETWEEN 3 TABLES ---");

        record Customer(int id, String name) {}
        record Order(int orderId, int customerId, String date) {}
        record OrderItem(int itemId, int orderId, String productName, double price) {}

        List<Customer> customers = List.of(new Customer(1, "Alice Enterprise"), new Customer(2, "Bob Corp"));
        List<Order> orders = List.of(new Order(1001, 1, "2026-08-18"), new Order(1002, 2, "2026-08-19"));
        List<OrderItem> items = List.of(
                new OrderItem(1, 1001, "Cloud Server License", 1200.0),
                new OrderItem(2, 1001, "SSL Certificate", 150.0),
                new OrderItem(3, 1002, "Domain Name", 25.0)
        );

        // Ex 16.1: 3-Table Multi-Join Pipeline Simulation
        record FullOrderReport(String customerName, String orderDate, String product, double price) {}

        List<FullOrderReport> report = new ArrayList<>();
        for (var c : customers) {
            for (var o : orders) {
                if (c.id() == o.customerId()) {
                    for (var it : items) {
                        if (o.orderId() == it.orderId()) {
                            report.add(new FullOrderReport(c.name(), o.date(), it.productName(), it.price()));
                        }
                    }
                }
            }
        }
        System.out.println("Ex 16.1 - 3-Table JOIN executed successfully (" + report.size() + " items):");
        report.forEach(r -> System.out.println("  -> Customer: " + r.customerName() + " | Date: " + r.orderDate() + " | Item: " + r.product() + " ($" + r.price() + ")"));

        // Ex 16.2: SQL EXPLAIN Plan Join Ordering
        System.out.println("Ex 16.2 - EXPLAIN Plan: Database optimizer re-orders joins to start with the most selective table first");

        // Ex 16.3: Indexing Foreign Keys for fast multi-joins
        System.out.println("Ex 16.3 - Indexing Rule: Always create B+Tree indexes on foreign key columns (orders.customer_id, order_items.order_id) to avoid full scans");

        // Ex 16.4: Combining INNER JOIN and LEFT JOIN in 3-table queries
        System.out.println("Ex 16.4 - Mixed Joins: 'FROM Customers c LEFT JOIN Orders o ON ... LEFT JOIN OrderItems oi ON ...' preserves customers with zero orders");

        // Ex 16.5: Aggregation across 3 joined tables
        double totalRevenue = report.stream().mapToDouble(FullOrderReport::price).sum();
        System.out.println("Ex 16.5 - Aggregation across joined tables: Total Revenue = $" + totalRevenue);
    }

    // ============================================================================
    // 17. OPTIMISTIC AND PESSIMISTIC LOCKING
    // ============================================================================
    /**
     * ARCHITECTURAL & INTERVIEW NOTES - OPTIMISTIC VS PESSIMISTIC LOCKING:
     * 1. Optimistic Locking:
     *    - Assumes conflicts are RARE. Does NOT lock the database row.
     *    - Uses a `@Version` integer or timestamp column.
     *    - SQL: `UPDATE account SET balance = 400, version = version + 1 WHERE id = 1 AND version = 5;`
     *    - If another transaction modified the row in the meantime, updated row count is 0 ➔ Throws `OptimisticLockException`!
     *    - Best For: Read-heavy applications, high concurrency, low collision rates (e.g. e-commerce product edits).
     * 2. Pessimistic Locking:
     *    - Assumes conflicts are FREQUENT. Exclusively locks the database row immediately upon reading.
     *    - SQL: `SELECT * FROM account WHERE id = 1 FOR UPDATE;`
     *    - Other transactions must WAIT until this transaction commits or rolls back.
     *    - Best For: High-contention financial money transfers, inventory flash sales where stock cannot oversell.
     */
    static void topic17_OptimisticVsPessimisticLocking() {
        System.out.println("\n--- 17. OPTIMISTIC VS PESSIMISTIC LOCKING ---");

        // Ex 17.1: Optimistic Locking Simulator with @Version Conflict Detection
        class OptimisticAccountEntity {
            int id = 101;
            double balance = 1000.0;
            int version = 1;

            public synchronized boolean updateBalance(double newBalance, int expectedVersion) {
                if (this.version != expectedVersion) {
                    System.out.println("  [CONFLICT DETECTED] Row was updated by another thread! Expected version=" + expectedVersion + ", Actual version=" + this.version);
                    return false; // Throws OptimisticLockException
                }
                this.balance = newBalance;
                this.version++;
                return true;
            }
        }
        OptimisticAccountEntity optAccount = new OptimisticAccountEntity();
        // User A and User B both read Version 1 simultaneously
        int userAVersion = optAccount.version;
        int userBVersion = optAccount.version;

        boolean userASuccess = optAccount.updateBalance(900.0, userAVersion); // Commits, version becomes 2
        boolean userBSuccess = optAccount.updateBalance(800.0, userBVersion); // Fails! Expected 1, actual 2
        System.out.println("Ex 17.1 - Optimistic User A Update (Success): " + userASuccess + " [New Version=" + optAccount.version + "]");
        System.out.println("Ex 17.1 - Optimistic User B Update (OptimisticLockException): " + userBSuccess);

        // Ex 17.2: Pessimistic Locking Simulator (SELECT ... FOR UPDATE)
        class PessimisticLockStore {
            private final Lock rowLock = new ReentrantLock();
            private double inventoryStock = 1;

            public boolean purchaseItem(String buyer) {
                rowLock.lock(); // SELECT ... FOR UPDATE (Blocks competing threads)
                try {
                    if (inventoryStock > 0) {
                        inventoryStock--;
                        System.out.println("  [PESSIMISTIC_LOCK] " + buyer + " acquired lock and bought last item!");
                        return true;
                    }
                    return false;
                } finally {
                    rowLock.unlock(); // Commit / Release DB lock
                }
            }
        }
        PessimisticLockStore pessimisticStore = new PessimisticLockStore();
        pessimisticStore.purchaseItem("Buyer 1");
        boolean buyer2Result = pessimisticStore.purchaseItem("Buyer 2");
        System.out.println("Ex 17.2 - Pessimistic Lock prevented overselling: Buyer 2 succeeded = " + buyer2Result);

        // Ex 17.3: Comparison Matrix
        Map<String, String> lockingMatrix = Map.of(
                "Mechanism", "Optimistic: Version check on UPDATE | Pessimistic: DB row lock (SELECT FOR UPDATE)",
                "Performance", "Optimistic: High throughput (Zero lock wait) | Pessimistic: Blocks competing threads",
                "Deadlock Risk", "Optimistic: Zero deadlock risk | Pessimistic: Risk of deadlocks if lock ordering differs",
                "Best For", "Optimistic: Read-heavy / Low conflict | Pessimistic: Flash sales / Money deduction"
        );
        lockingMatrix.forEach((k, v) -> System.out.println("Ex 17.3 - " + k + " -> " + v));

        // Ex 17.4: PESSIMISTIC_READ (Shared Lock) vs PESSIMISTIC_WRITE (Exclusive Lock)
        System.out.println("Ex 17.4 - Lock Modes: PESSIMISTIC_READ ('LOCK IN SHARE MODE') vs PESSIMISTIC_WRITE ('FOR UPDATE')");

        // Ex 17.5: Handling OptimisticLockException with Exponential Backoff Retry
        System.out.println("Ex 17.5 - Spring Retry: Use @Retryable(retryFor = ObjectOptimisticLockingFailureException.class, maxAttempts = 3) to automatically retry optimistic updates");
    }
}
