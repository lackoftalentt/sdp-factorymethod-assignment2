# JDM Track Build Configuration System

A Java 17 university design-patterns project that prepares Nissan Skyline GT-R R34, Toyota Supra MK4 and Mazda RX-7 FD builds for street, drift and track use. Mitsubishi Lancer Evolution IX was added afterward in a separate extension commit. Plain Java, Maven, JUnit 5 and PlantUML; no Spring, Lombok or application frameworks.

## Problem

A workshop needs three compatible component types for each vehicle: an ECU, suspension and brakes. Creating those parts directly inside the tuning workflow ties the client to concrete classes and makes every platform addition a workflow change. Preparation mode is a separate variation: the same platform needs different power envelopes, suspension settings, cooling and driver eligibility rules for street, drift and track.

## Initial Design Problems

Part A remains in [LegacyBuildConfigurator.java](src/main/java/org/example/jdm/legacy/LegacyBuildConfigurator.java). It directly creates three concrete legacy record types inside a platform switch and computes a simple performance score.

1. The client depends directly on concrete component constructors.
2. The platform switch grows with each supported family.
3. Adding a family requires modifying existing client code.
4. Every branch repeats the three-component construction recipe; other clients would need to duplicate it.
5. Components carry only platform strings: `LegacyBuild` accepts an R34 ECU with Supra suspension and RX-7 brakes.

The legacy version is deliberately small and preserved for before/after comparison. The final CLI uses the new architecture.

## Project Structure

```text
pom.xml                         Java 17 release, JUnit 5, executable JAR
src/main/java/org/example/
  Main.java                     JVM entry point and exit status
  jdm/
    app/                        CLI parsing and report presentation
    legacy/                     Part A direct-instantiation prototype
    family/                     PlatformFamily and four marker types
    product/ecu/                ECU interface, calibration base, four products
    product/suspension/         Suspension interface, calibration base, four products
    product/brake/              Brake interface, calibration base, four products
    abstractfactory/            Factory contract and four platform factories
    factorymethod/              Plans, abstract Creator and three concrete Creators
    config/                     Enums and centralized runtime resolvers
    domain/                     Typed build and immutable input/result records
    service/                    Safety validation and track simulation
src/test/java/org/example/jdm/   Seven test classes, 22 test methods
docs/                           UML, assignment notes, defense guide and Git history
```

## Factory Method

| Role | Implementation |
|---|---|
| Product | `PreparationPlan` |
| Concrete Products | `StreetPreparationPlan`, `DriftPreparationPlan`, `TrackPreparationPlan` |
| Creator | `BuildPreparationService` |
| Concrete Creators | `StreetBuildPreparationService`, `DriftBuildPreparationService`, `TrackBuildPreparationService` |
| Factory Method | `protected abstract PreparationPlan createPreparationPlan()` |

The base Creator's public, final `prepare()` workflow calls the overridable factory method, validates the plan against the driver requirements, obtains all three components from the Abstract Factory, tunes/configures them, constructs a typed build, executes simulation and safety validation, and returns a structured report.

Street allows 55% of safe boost, drift 75%, and track 100%. Novice drivers receive an additional 65% multiplier; the drift plan rejects novice drivers. Mode also controls suspension setup and brake cooling. Thus the created plan changes actual preparation behavior.

This is Factory Method because subclasses override a creation step used by their inherited business workflow. A static `create(type)` helper alone would select objects without that Creator/subclass relationship. `PreparationServiceResolver` only selects a Creator at application startup; the actual Factory Method remains inside that Creator hierarchy.

## Abstract Factory

`CarPlatformFactory<F extends PlatformFamily>` creates the three related abstract product types: `EngineControlUnit<F>`, `SuspensionSystem<F>` and `BrakeSystem<F>`. It also supplies immutable mass, traction and platform-name metadata.

| Family / Concrete Factory | ECU | Suspension | Brakes |
|---|---|---|---|
| `NissanR34Factory` | `NissanR34ECU` | `NissanR34Suspension` | `NissanR34Brakes` |
| `ToyotaSupraFactory` | `ToyotaSupraECU` | `ToyotaSupraSuspension` | `ToyotaSupraBrakes` |
| `MazdaRX7Factory` | `MazdaRX7ECU` | `MazdaRX7Suspension` | `MazdaRX7Brakes` |
| `MitsubishiEvo9Factory` (later extension) | `MitsubishiEvo9ECU` | `MitsubishiEvo9Suspension` | `MitsubishiEvo9Brakes` |

The business workflow depends on `CarPlatformFactory<F>` and product interfaces. Concrete factories encapsulate component constructors. Shared calibration base classes implement common formulas; concrete products bind an exact family type and supply distinct numerical behavior.

| Calibration | Nissan | Toyota | Mazda | Mitsubishi |
|---|---:|---:|---:|---:|
| Full safe boost, bar | 1.6 | 1.8 | 1.1 | 1.5 |
| Maximum modeled hp | 568 | 676 | 431 | 527.5 |
| Heat power-loss fraction per C above 20 C | .002 | .0015 | .004 | .0025 |
| Mass, kg | 1560 | 1490 | 1270 | 1420 |
| Base handling score | 100 | 96 | 112 | 106 |
| Wet retention beyond surface multiplier | 1.06 | .96 | .98 | 1.12 |
| Base brake thermal capacity, kJ | 2300 | 2600 | 1900 | 2500 |
| Traction factor | 1.12 | 1.00 | 1.02 | 1.16 |

These are illustrative calibrations, not manufacturer specifications. AWD contributes through traction and wet handling, the Supra has a larger power envelope and thermal capacity, and the RX-7 combines low mass with stronger heat sensitivity.

## Difference Between Patterns

**Factory Method:** Creator subclasses decide which `PreparationPlan` is created for the shared preparation workflow.

**Abstract Factory:** a selected platform factory produces a compatible family of ECU, suspension and brakes. A track Creator can work with any platform factory; changing the platform does not require changing its workflow.

## Compatibility

The same `F` appears on every component, the factory, `CarBuild<F>` and `BuildReport<F>`. For a `CarBuild<NissanR34Family>`, all three constructor arguments must be Nissan-family interfaces. Java generics are invariant, so `SuspensionSystem<ToyotaSupraFamily>` cannot be passed as `SuspensionSystem<NissanR34Family>` or silently widened to `SuspensionSystem<PlatformFamily>`.

```java
// Intentionally invalid documentation example, not compiled project source:
// new CarBuild<NissanR34Family>(specification,
//     new NissanR34ECU(), new ToyotaSupraSuspension(), new MazdaRX7Brakes(),
//     tune, suspensionSetup, brakeSetup, mode, requirements);
// Compilation fails: suspension and brake generic arguments disagree with F.
```

Type inference with `new CarBuild<>(...)` also fails on mixed families. There are no raw types, unchecked casts, warning suppressions or runtime family checks. Deliberately dishonest interface implementations or unchecked Java code could circumvent the contract; the guarantee covers ordinary typed use of these components. Runtime tests check coherent factory builds, while the type signatures enforce rejection of mixed construction.

## Runtime Selection

`VehiclePlatform.parse()` validates the external family name. `CarPlatformFactoryResolver` contains the only production switch that constructs concrete platform factories. `PreparationServiceResolver` similarly selects the Creator for the requested mode. After this composition step, business logic does not branch on vehicle family.

The platform resolver returns `CarPlatformFactory<? extends PlatformFamily>`. `ConsoleApplication.prepareTyped()` captures that unknown family as `F` and calls the generic Creator workflow, preserving compatibility without a cast.

```bash
java -jar target/jdm-track-build.jar --family=nissan --mode=track
java -jar target/jdm-track-build.jar --family=toyota --mode=street
java -jar target/jdm-track-build.jar --family=mazda --mode=drift
java -jar target/jdm-track-build.jar --family=mitsubishi --mode=track --wet=true
```

All options use `--key=value`. Defaults: `family=nissan`, `mode=track`, `hp=400`, `skill=intermediate`, `intensity=0.7`, `temperature=25`, `length=3.5`, `wet=false`. Skills: `novice`, `intermediate`, `expert`. Intensity is in (0, 1], temperature in [-20, 55] C, track length in (0, 30] km, and requested hp in (0, 1500]. Requests beyond the plan/platform envelope are capped and explained; invalid ranges, unknown options, duplicates and unsupported family/mode names produce clear errors.

Exit codes: **0** for a report passing workshop checks, **1** for invalid input, **2** for a completed report with safety issues. Only the JVM entry point calls `System.exit`.

## Business Operations

1. **Prepare vehicle — `BuildPreparationService.prepare()`:** creates a plan, validates driver eligibility, requests a compatible component set, tunes the ECU, configures suspension/brakes and returns `BuildReport<F>`.
2. **Simulate track session — `TrackSimulationService.simulateTrackSession()`:** combines effective engine power, power-to-weight, suspension grip, braking distance, traction and thermal fade to estimate lap time and a performance score.
3. **Perform safety validation — `SafetyValidationService.performSafetyValidation()`:** checks engine power against driver/handling allowance, brake thermal load and stopping distance, plus suspension mode and drift eligibility. Returns a `SafetyResult` with issues and calculated values.

Components and setup/results are immutable. Business services return values and never print. An unsafe simulation remains available for diagnosis; a safety failure is explicitly reported. These calculations are educational comparisons, not real vehicle certification. Units, formulas and trade-offs are documented in [assignment notes](docs/assignment-notes.md).

## Adding Mitsubishi

The original system had 18 passing tests and three working CLI demos before Mitsubishi was added. Commit `54e0c62` contains the extension; its parent checkpoint retains only three platforms. The extension increased the suite to 21 tests, and final CLI integration coverage brought it to 22.

### NEW FILES

Five production files:

- `src/main/java/org/example/jdm/family/MitsubishiEvo9Family.java`
- `src/main/java/org/example/jdm/product/ecu/MitsubishiEvo9ECU.java`
- `src/main/java/org/example/jdm/product/suspension/MitsubishiEvo9Suspension.java`
- `src/main/java/org/example/jdm/product/brake/MitsubishiEvo9Brakes.java`
- `src/main/java/org/example/jdm/abstractfactory/MitsubishiEvo9Factory.java`

One test file:

- `src/test/java/org/example/jdm/abstractfactory/MitsubishiExtensionTest.java`

### MODIFIED FILES

Exactly two existing files in the extension commit:

- `src/main/java/org/example/jdm/config/VehiclePlatform.java` — added `MITSUBISHI`.
- `src/main/java/org/example/jdm/config/CarPlatformFactoryResolver.java` — registered `MitsubishiEvo9Factory`.

No changes to `BuildPreparationService`, `CarBuild`, preparation plans, simulation, safety algorithms, CLI or existing tests were required. The existing client test iterates registered platforms and automatically includes Mitsubishi. Documentation was completed in later documentation/finalization commits; it is not a hidden business-code change. Verify the exact extension with `git show --name-status 54e0c62`.

## Testing

**22 automated JUnit 5 test methods**, no disabled tests:

| Test class | Count | Evidence |
|---|---:|---|
| `OriginalFactoriesTest` | 4 | All nine original concrete products; typed coherent build |
| `ProductBehaviorTest` | 3 | Boost/power/heat differences, handling/wet grip, braking/thermal capacity |
| `PreparationWorkflowTest` | 3 | Street, drift and track plan behavior through the public Creator workflow |
| `RuntimeSelectionTest` | 3 | Original resolution, clean unsupported-family/mode errors, malformed arguments |
| `BusinessOperationsTest` | 5 | Preparation, thermal safety, power/grip/weather simulation effects, unchanged client for every factory, invalid domain inputs |
| `MitsubishiExtensionTest` | 3 | All three new products, calibration, resolver and typed wet-track workflow |
| `ConsoleApplicationTest` | 1 | All 12 family/mode combinations and unsafe-session exit status |

The count is test methods; loops additionally cover multiple scenarios within a method. There are more than three behavioral operation tests and more than two negative scenarios. Generic compile-time rejection is explained above rather than placing uncompilable code in test sources. Compilation uses `-Xlint:all -Werror` and `--release 17`.

Final verification: `mvn clean test` and `mvn package` both completed with **BUILD SUCCESS**, 22 tests, zero failures/errors/skips. Packaged demos passed for the four families; invalid family/mode returned exit 1 and an overloaded session returned exit 2. A separate temporary `javac --release 17` check rejected mixed-family construction with incompatible equality constraints, while the coherent control compiled without warnings. Generated class-file major version is 61 (Java 17). The temporary negative example is not included in test sources or the 22-test count.

## Running

Requires Maven and a JDK 17 or later. The project targets the Java 17 language, API and class-file level. The provided environment has JDK 21, which is used to compile with `--release 17` and execute the verification commands.

```bash
mvn test
mvn clean test
mvn package
java -jar target/jdm-track-build.jar --family=nissan --mode=track
```

For IntelliJ IDEA, open `pom.xml` as a Maven project, select an installed JDK 17 or newer, reload Maven, and run `org.example.Main` with the arguments above. The project language level is 17. The preserved local SDK entry is 21; select your own installed SDK if that name is unavailable.

Useful defense demonstrations:

```bash
# Show each original platform and preparation mode.
java -jar target/jdm-track-build.jar --family=nissan --mode=track
java -jar target/jdm-track-build.jar --family=toyota --mode=street
java -jar target/jdm-track-build.jar --family=mazda --mode=drift
# Show the extension through the unchanged client.
java -jar target/jdm-track-build.jar --family=mitsubishi --mode=track --wet=true
# Show safe-envelope limiting rather than accepting impossible power.
java -jar target/jdm-track-build.jar --family=mazda --mode=track --hp=800
# Show structured safety failure and exit code 2.
java -jar target/jdm-track-build.jar --family=mazda --mode=track --length=30 --temperature=55 --intensity=1
# Show clean input errors and exit code 1.
java -jar target/jdm-track-build.jar --family=honda --mode=track
java -jar target/jdm-track-build.jar --family=nissan --mode=rally
```

## UML and Defense Materials

- [PlantUML architecture](docs/architecture.puml): both patterns, all products/factories, generic aggregate and clients. Open with a PlantUML-capable viewer or render with `java -jar plantuml.jar -tsvg docs/architecture.puml`.
- [Defense guide](docs/defense-guide.md): 30 concise oral-defense answers.
- [Assignment notes](docs/assignment-notes.md): compatibility example, original checkpoint, model assumptions and SOLID trade-offs.
- [Git history guide](docs/git-plan.md): ten real development stages and exact staging/commit commands.

## Assignment Requirement Mapping

| Part | Implementation / evidence |
|---|---|
| A — initial design | `legacy/LegacyBuildConfigurator`, initial commit, initial design problems above |
| B — Factory Method | `factorymethod` package, `PreparationWorkflowTest` |
| C — Abstract Factory | `abstractfactory` and `product` packages, original factory/product tests |
| D — compatibility | `PlatformFamily`, invariant product/factory generics, `CarBuild<F>`, typed workflow tests and rejected documentation example |
| E — runtime selection | `config` resolvers, `app/CommandLineOptions`, `ConsoleApplication`, `Main`, runtime/CLI tests |
| F — business operations | Creator preparation, `SafetyValidationService`, `TrackSimulationService`, behavioral tests |
| G — fourth family | Mitsubishi files and extension commit `54e0c62`; only registration files modified |
| H — UML | `docs/architecture.puml` |
| I — tests | `src/test/java`, 22 JUnit tests, Maven test/package verification |
| J — history | Ten incremental commits, `docs/git-plan.md` |

All critical checklist categories are covered: original domain; three product types and nine original products; behavioral differences; retained legacy version and five documented problems; genuine Factory Method with three plans/creators and a business workflow; Abstract Factory and compatible generic families; external runtime selection; three collaborating business operations; isolated fourth-family extension; matching UML; 22 tests including negative, compatibility and abstraction coverage; executable Maven project; ten meaningful commits; README and defense guide.
