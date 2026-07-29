# Technology Adapters — Backlog

Persistent task list for TA work, near to long term. Companion to
[`TA-CATALOG.md`](./TA-CATALOG.md) (classification, gold references, quality checklist).

Status legend: `TODO` · `IN PROGRESS` · `BLOCKED` · `DONE` · `DEFERRED`.

---

## Long term / architecture

### T-ARCH-1 — Formalize a "service" Resource pattern  ·  `DEFERRED`

> Deferred by decision — revisit later. Full context captured below so it can be picked up
> without re-deriving the analysis.

**Problem.** OpenFlexo's `Resource` / `ResourceData` contract is designed for **documents**: a
file is fully parsed into an in-memory object tree, and `load`/`save` are symmetric (the whole
model is read and written back). A large sub-family of TAs does not work that way — they are
**service adapters**: the resource only holds a *connection/configuration*, and the actual domain
data is **queried live** against an external system and never persisted into the resource.

**Affected TAs (SVC family).** `jdbc`, `http`, `rest`, `kafka`, `opc-ua`, `oslc` (hybrid), `mcp`.
See the N1 column in the catalogue grid.

**Evidence of the tension.**
- `mcp` — the `.mcp` file is only JSON connection config (name/command/args/url); tools are fetched
  live via `client.listTools()` and tool results via `client.callTool(...)`, never persisted.
  Symptoms of forcing it into the document mould: `@DeclareFetchRequests({})` is empty,
  `MCPToolRole.makeFlexoConceptInstance` returns `null`, `performSave` only re-serializes the config,
  and there is no meaningful in-memory tree to navigate.
- `jdbc` — `JDBCConnection.getConnection()` opens a real `java.sql.Connection` lazily; tables/columns
  are read on demand via `DatabaseMetaData`, rows via live SQL (`SQLHelper` + commons-dbutils /
  Hibernate). Nothing is loaded exhaustively.
- `opc-ua` — `OPCServer.getClient()` connects a Milo `OpcUaClient`; the address space is walked on
  demand (`browse`), values read live (`readValue`).
- `http` / `rest` — the resource is an access point (URL); data is fetched per request (`HttpGet`,
  rest-assured), mapped through JSONPath, not materialized as a whole.
- `oslc` — **hybrid**: a REST/OAuth client (Apache Wink) that nonetheless tends to *materialize the
  service-provider catalogue* (`OSLCResourceResourceImpl.loadResourceData` →
  `convertAllCoreResourcesFromCatalog`). It sits between the two families.
- `kafka` — `KafkaServer.getProducer()` / consumers; pure message streaming, no in-memory document.

**Why this matters.**
- Contributors keep copying a document TA (e.g. csv) as the template for a service TA (mcp's
  `MCPServerResourceImpl` explicitly mirrors `CSVResourceImpl`), which imports assumptions that do
  not hold (symmetric save, exhaustive load, navigable tree) and leaves dead/empty hooks.
- FML integration for these TAs is ad hoc: some expose fetch requests, some don't; the
  connect/query/disconnect lifecycle is reinvented per TA.

**Options to evaluate (later).**
1. **Dedicated pattern, no framework change** — document a "service Resource" recipe (config-only
   resource + explicit `connect/query/disconnect` lifecycle + fetch-request-based FML access +
   no-op/undefined `save` of domain data) and pick a gold reference (`jdbc` is the most complete).
   Lowest risk; mostly conventions + a rule + maybe a small base class.
2. **First-class abstraction** — introduce a `ServiceResource` / `ServiceResourceData` (or a marker
   super-interface) in core so the framework stops assuming exhaustiveness for these, with a shared
   connection lifecycle. Higher blast radius (core change), needs care re: the strict layering.
3. **Status quo** — keep the single contract, accept the tension, only tidy per-TA smells.

**Recommended starting point when resumed.** Option 1 as a first step (formalize + gold reference
`jdbc`), keep Option 2 open if the recipe proves it needs framework support. Decide `oslc`'s
placement (true service vs catalogue-materializing document).

**Entry points to re-read (fast context rebuild).**
- `openflexo-mcp/mcp-ta/.../rm/MCPServerResourceImpl.java`, `.../model/MCPServer.java`,
  `.../fml/CallMCPTool.java`, `.../SelectMCPTool.java`
- `openflexo-jdbc/jdbc-ta/.../model/JDBCConnection.java`, `.../util/SQLHelper.java`, package `hbn/`
- `openflexo-opc-ua/opc-ua-ta/.../model/OPCServer.java`, `.../fml/editionaction/GetValue.java`
- `openflexo-oslc/oslc-ta/.../rm/OSLCResourceResourceImpl.java`

---

## Priority 1 — POCs to harden / decide

- **T-MCP-1** `TODO` — mcp: add a `-ui` module (currently missing).
- **T-MCP-2** `TODO` — mcp: add license headers (currently 2/32 files).
- **T-MCP-3** `TODO` — mcp: move `callToolWith*` business logic out of the `MCPServer` PAMELA
  interface into `@Implementation`/helpers; remove redundant overloads.
- **T-MCP-4** `TODO` — mcp: fix `new MCPModelFactory(null, null)` instantiated out of context
  (`ConnectMCPServer`, `SelectMCPTool`).
- **T-MCP-5** `TODO` — mcp: un-skip integration tests (provide a server fixture instead of
  `assumeTrue(npxRunnable)` that silently skips in CI).
- **T-KAFKA-1** `TODO` — kafka: add `-test`/`-test-rc` modules and real tests (currently 0), or
  explicitly declare POC status.
- **T-ODT-1** `TODO` — odt: implement a real `save` or explicitly mark POC.
- **T-CAPELLA-1** `TODO` — capella: specialize the `XX*` scaffold to the real EMF/Sirius format, or
  drop from scope.

## Priority 2 — Read-only adapters to complete

- **T-RHAP-1** `TODO` — rhapsody: implement `write()` (currently logs "Write NOT IMPLEMENTED").
- **T-JAVA-1** `TODO` — java: implement `performSave` (currently a stub).

## Priority 3 — Targeted gaps

- **T-CSV-1** `TODO` — csv: fix FML path bindings + `parseAndRetrieveObject` (see its KNOWN_ISSUES.md).
- **T-HTTP-1** `TODO` — http: finish XML-RPC support (currently stubbed).
- **T-REST-1** `TODO` — rest: clean up exploratory tests (`LeTestQueJexecute`, debug prints).
- **T-OSLC-1** `TODO` — oslc: migrate off aging Apache Wink; remove dead code.

## Priority 4 — Cross-cutting hygiene

- **T-HYG-1** `TODO` — complete missing license headers (opc-ua 20/74, rhapsody 69/147, rest, pdf,
  json, mcp).
- **T-HYG-2** `TODO` — reduce residual `System.out/err.print` debug in production code.

## Tooling / scaffolding

- **T-SCAF-1** `DONE` — hardened the `add-technology-adapter` skill + `xx-ta` template. Done:
  - Skill: stop recommending `openflexo-mcp`/`openflexo-csv` as "cleanest templates"; split guidance
    into *structure* (`xx-ta*` skeleton) vs *implementation patterns* (gold reference per family,
    linked to `TA-CATALOG.md` §3); added a quality checklist step (license/logger/no-logic-in-interface/
    factory-in-context/symmetric-save/KNOWN_ISSUES/module set); fixed registration + copy pointers.
  - Template `xx-ta`: replaced residual `System.out.println` debug with `logger` in
    `XXTechnologyAdapter`, `XXModelFactory`, `XXTextResourceImpl`, `AddXXLine` (removed the now-unused
    `@SuppressWarnings("unused")` on loggers).
  - Added `openflexo-technology-adapters/KNOWN_ISSUES.template.md` to copy into new TA repos.
  - Note: `xx-ta` already had license headers on all sources and a `-ui` module — those gaps
    (mcp) were local drift, not template defects.

---

*Keep this list in sync with `TA-CATALOG.md` §4 (roadmap). When a task is picked up, set it
`IN PROGRESS` and link the branch/PR; when done, mark `DONE` and update the catalogue grid.*
