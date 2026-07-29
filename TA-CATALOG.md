# OpenFlexo Technology Adapters — Catalogue & classification

This document maps the ~22 technology adapters (TAs) in the workspace against a multi-dimensional
set of criteria (nature/capabilities **and** code state), and derives "gold" references per family
plus an upgrade roadmap.

All TAs share the same template (`TechnologyAdapter` + `ModelSlot` + `ResourceFactory` +
`Resource`, see `.claude/rules/technology-adapters.md`) but diverge widely in nature and maturity.
This catalogue is **living**: keep it up to date as the adapters evolve.

---

## 1. Classification criteria

### A. Nature (what the TA is / can do)

| Code | Criterion | Values |
|---|---|---|
| **N1** | Resource acquisition model | `DOC` exhaustive document (file fully parsed into a tree, symmetric load/save) · `SVC` queried service (config-only resource, live/ephemeral data) · `HYB` hybrid (service client that materializes a catalogue) |
| **N2** | Model provenance | `NATIF` serialized PAMELA model · `WRAP` wraps an external framework's object graph · `PARSER` dedicated grammar/parser · `CLIENT` protocol client |
| **N3** | Typing / metamodel | `TYPED` instances typed by a metamodel exposed to FML · `STRUCT` fixed hard-coded entity schema · `FLAT` untyped / no domain model |
| **N4** | Directionality | `R` read-only · `R/W` read-write · `R/W+GEN` + generation · `ACT` write = remote action |
| **N5** | FML integration depth (increasing) | `roles/actions` < `+fetch` < `+bindings/path` < `+reflection (VMI/FCI)` |
| **N6** | Technological space | standard-mature · specialized-heavy · aging · custom |

### B. Code state

| Code | Criterion |
|---|---|
| **C1** | Maturity — volume (LOC), git history depth, recency |
| **C2** | Module completeness — presence of `-ui`, `-test`, `-test-rc`, `-parser` |
| **C3** | Functional completeness — real `save` vs stub/placeholder/"Not implemented" |
| **C4** | Test quality — real & numerous / brittle (network) / auto-skipped (`assumeTrue`) / absent |
| **C5** | Conventions — license headers, `logger` vs `System.out`, PAMELA idioms, dead code |
| **C6** | Documentation — KNOWN_ISSUES, javadoc |

---

## 2. Classification grid

| TA | N1 | N2 | N3 | N4 | FML | Maturity | State signals |
|---|---|---|---|---|---|---|---|
| **xml** | DOC | PARSER (XSOM) | TYPED | R/W | reflection | **mature** | license 83/83 ; real tests (ReqIF/OTAWA/XSD) ; JDOM/auto-catch debt |
| emf | DOC | WRAP (EObject) | TYPED | R/W | high | **mature** | 576 commits ; model + metamodel ; structured converters |
| owl | DOC | WRAP (Jena) | TYPED | R/W | high | **mature** | 550 commits ; rich UI ; proven legacy base |
| **diagram** | DOC | NATIVE | STRUCT | R/W+GEN | high | **mature** | 46k LOC ; palettes/specs ; "native PAMELA" reference |
| **docx** | DOC | WRAP (docx4j) | STRUCT | R/W | medium | **mature** | 45 test files (best coverage) ; real WordML save |
| **xlsx** | DOC | WRAP (POI) | STRUCT | R/W+GEN | medium | **mature** | clean PAMELA model ; 15 converters ; generation |
| **jdbc** | SVC | CLIENT | FLAT→ORM | R/W+ACT | reflection | **mature** | dual SQL + Hibernate path ; 324 assertions |
| json | DOC | WRAP (Jackson) | STRUCT | R/W | medium | inter. | cleanest (1 TODO) ; small clean reference |
| markdown | DOC | WRAP (flexmark) | STRUCT | R/W | medium | inter. | clean ; real load/save |
| csv | DOC | PARSER | FLAT | R/W | fetch (broken) | inter. | ~253 tests but 7 failing ; `parseAndRetrieveObject` stub ; KNOWN_ISSUES |
| pptx | DOC | WRAP (POI/XSLF) | STRUCT | R/W | select+roles | inter. | migrated to real `.pptx` (OOXML/XSLF, POI 3.17) + PAMELA model ; textual FML + 3 `.fmlscript` + save round-trip test ; see `KNOWN_ISSUES.md` |
| pdf | DOC | WRAP (PDFBox) | STRUCT (thin) | R (+save) | low | inter. | 2 entities ; very thin domain model |
| opc-ua | SVC | CLIENT (Milo) | STRUCT | R (no FML SetValue) | medium | inter. | recent/clean, javadoc ; license 20/74 ; embedded test server |
| http | SVC | CLIENT | FLAT | R + limited write | fetch | inter. | XML-RPC stubbed ; residual FR debug ; brittle tests (GitHub) |
| rest | SVC | CLIENT (rest-assured) | FLAT | R/W (endpoints) | fetch | inter. | modern design but young ; `LeTestQueJexecute` (WIP) |
| oslc | HYB | CLIENT (Wink) | STRUCT | R/W (add/upd/rm) | medium | inter. | 2 modules ; aging lib ; dead code |
| **rhapsody** | DOC | PARSER (SableCC) | STRUCT (rich) | **R (write NOT IMPL)** | medium | inter. debt | 35 entities, large parser ; license 69/147 ; auto-catch |
| java | DOC | WRAP (Spoon) | STRUCT | **R (performSave stub)** | low | inter. weak | read-only AST |
| kafka | SVC | CLIENT | FLAT | R/W stream | low | **POC** | 0 tests ; 2 modules ; clean but unvalidated code |
| odt | DOC | WRAP | STRUCT | **save stubbed** | low | **POC** | 1 test, ~1000 LOC ; non-functional save |
| capella | DOC | `XX*` scaffold | — | R raw text | low | **POC** | unspecialized template ; target format not implemented |
| **mcp** | SVC | CLIENT (JSON-RPC) | FLAT | ACT (call tool) | none (`@DeclareFetchRequests({})`) | **POC** | no `-ui` module ; license 2/32 ; logic in interface ; `new MCPModelFactory(null,null)` ; auto-skipped tests |

---

## 3. "Gold" references per family

| Family | Reference | Backups |
|---|---|---|
| Typed / metamodel document | **xml** | emf, owl |
| Native PAMELA document | **diagram** | — |
| Office document R/W | **docx**, **xlsx** | — |
| Small clean document | **json** | markdown |
| Queried service | **jdbc** | opc-ua (recent) |

### "Gold" quality checklist (to imitate)

- [ ] License headers on **100%** of `.java` sources.
- [ ] `logger` everywhere — no residual `System.out/err.print`.
- [ ] PAMELA interface with **no business logic** (behaviour in `@Implementation` / default methods).
- [ ] PAMELA factory instantiated **within its resource context** (never `new XxxModelFactory(null, null)`).
- [ ] `save` **symmetric** to load and **tested**.
- [ ] `KNOWN_ISSUES.md` documenting assumed debt (see csv).
- [ ] `-ui`, `-test`, `-test-rc` modules present (`-parser` if the TA has a grammar).
- [ ] **Real** tests (not just `assumeTrue(...)` that silently skips in CI).

---

## 4. Upgrade roadmap

**Priority 1 — POCs to harden / decide**
- **mcp** — add a `-ui` module ; license headers ; move `callToolWith*` logic out of the `MCPServer`
  interface ; fix `new MCPModelFactory(null, null)` ; un-skip tests (server fixture). Decide the
  "service" pattern (§5).
- **kafka** — add tests (`-test`/`-test-rc`), otherwise assume POC status.
- **odt** — implement a real save or explicitly mark it POC.
- **capella** — specialize the `XX*` scaffold (EMF/Sirius format) or drop from scope.

**Priority 2 — Read-only to complete**
- **rhapsody** — implement `write()`. · **java** — implement `performSave`.

**Priority 3 — Targeted gaps**
- **csv** — FML path bindings + `parseAndRetrieveObject`. · **http** — XML-RPC. · **rest** — clean up
  exploratory tests. · **oslc** — migrate off Apache Wink.

**Priority 4 — Cross-cutting hygiene**
- Complete missing license headers (opc-ua, rhapsody, rest, pdf, json, mcp) ; reduce residual
  `System.out` debug.

---

## 5. Open question — "service" nature

The `SVC` family (jdbc, http, rest, kafka, opc-ua, oslc, mcp) is in tension with the
`Resource/ResourceData` model designed for documents. To be decided: formalize a dedicated
**service Resource pattern** (config-only resource + fetch requests + connect/query lifecycle) to
frame these TAs, rather than forcing them to look like document resources.

**Deferred** — full analysis and entry points captured in
[`TA-BACKLOG.md`](./TA-BACKLOG.md) → task `T-ARCH-1`.

---

*Method: objective metrics (LOC, git history, license/`System.out` counts, module presence) +
targeted reading of each TA's `*ResourceImpl`, `ResourceFactory`, `TechnologyAdapter` and PAMELA
models. Re-assess whenever an adapter changes significantly.*
