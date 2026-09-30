# Development history and commit plan

This repository started with an uncommitted IntelliJ starter and no commits. Existing starter metadata was retained. Work was implemented and committed in stages with real authors and timestamps; no history was rewritten. The factory interface was introduced with abstractions so the Creator could compile before concrete factories existed. Business services accompanied the Creator; their behavioral tests were added at the original-system checkpoint.

| Stage | Commit | Content |
|---|---|---|
| 1 | `76461eb` | Direct-instantiation legacy prototype, Java 17 Maven configuration, preserved IDE settings |
| 2 | `7919742` | Component interfaces, family marker contract, typed aggregate, immutable domain values and abstract factory contract |
| 3 | `064c524` | Factory Method plans and creators; preparation, simulation and safety workflow |
| 4 | `c933b17` | Original three markers and nine calibrated concrete products |
| 5 | `14ce09c` | Original three concrete Abstract Factories |
| 6 | `86020f8` | Runtime platform/mode resolution, validated CLI and console report |
| 7 | `7e147a4` | 18 passing original-system tests and compatibility notes; three CLI demos executed |
| 8 | `54e0c62` | Mitsubishi extension, three new tests, only two existing registration files modified |
| 9 | `docs: add UML and architecture documentation` | Final UML, architecture audit and this history guide |
| 10 | `test: complete assignment test suite and defense notes` | 22-test suite, README, defense guide, Java 17 IDE language level and final verification |

The last two rows use commit subjects so this file need not embed its own commit hash. Use `git log --oneline --reverse` to see their generated hashes.

## Exact staging commands used by stage

These document how each development stage was recorded. They are not instructions to rerun staging against the final tree and manufacture a history.

```bash
# Stage 1: only the prototype existed.
git add pom.xml src/main/java .gitignore .idea
git commit -m 'feat: add initial direct-instantiation JDM build prototype'

# Stage 2: add abstraction contracts and domain records.
git add src/main/java
git commit -m 'refactor: introduce component abstractions and typed factory contract'

# Stage 3: add plans, creators and business services.
git add src/main/java
git commit -m 'feat: add factory method preparation workflow and business operations'

# Stage 4: add only the original three platform families.
git add src/main/java
git commit -m 'feat: introduce three calibrated JDM component families'

# Stage 5: add concrete factories for those families.
git add src/main/java
git commit -m 'refactor: create abstract factory platform architecture'

# Stage 6: add CLI/runtime composition.
git add src/main/java
git commit -m 'feat: add runtime factory selection and validated CLI'

# Stage 7: original system, already tested before Mitsubishi.
git add src/test docs/assignment-notes.md
git commit -m 'test: cover original business workflows and family consistency'

# Stage 8: five new production classes, two registrations, one new test file.
git add src/main/java/org/example/jdm/family/MitsubishiEvo9Family.java src/main/java/org/example/jdm/product/ecu/MitsubishiEvo9ECU.java src/main/java/org/example/jdm/product/suspension/MitsubishiEvo9Suspension.java src/main/java/org/example/jdm/product/brake/MitsubishiEvo9Brakes.java src/main/java/org/example/jdm/abstractfactory/MitsubishiEvo9Factory.java src/main/java/org/example/jdm/config/VehiclePlatform.java src/main/java/org/example/jdm/config/CarPlatformFactoryResolver.java src/test/java/org/example/jdm/abstractfactory/MitsubishiExtensionTest.java
git commit -m 'feat: add Mitsubishi Evo IX family with extension tests'

# Stage 9: document the actual final architecture.
git add docs/architecture.puml docs/assignment-notes.md docs/git-plan.md
git commit -m 'docs: add UML and architecture documentation'

# Stage 10: final defense materials and end-to-end CLI verification.
git add README.md docs/defense-guide.md src/test/java/org/example/jdm/app/ConsoleApplicationTest.java .idea/misc.xml
git commit -m 'test: complete assignment test suite and defense notes'
```

## Safe inspection during defense

```bash
git status
git log --oneline --decorate
git show 7e147a4:src/main/java/org/example/jdm/config/VehiclePlatform.java
git show --stat 54e0c62
git diff 7e147a4 54e0c62 -- src/main/java/org/example/jdm/factorymethod src/main/java/org/example/jdm/service src/main/java/org/example/jdm/domain
```

The final command produces no output: Mitsubishi did not change core business logic. No reset, rebase, force push, timestamp manipulation or fabricated authorship was used.
