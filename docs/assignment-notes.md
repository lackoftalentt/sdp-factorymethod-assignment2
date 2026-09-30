# Architecture verification and assignment notes

## Original-system checkpoint

Before adding Mitsubishi, the original system passed 18 JUnit tests. The packaged CLI was run for Nissan/Track, Toyota/Street and Mazda/Drift, with default 400 hp requests. Results were respectively 400, 400 and 387 tuned hp; handling scores were 115.0, 96.0 and 114.2. Mazda's drift plan limited the requested power. All three default demonstrations passed the workshop checks.

## Pattern audit

`BuildPreparationService` is the Factory Method Creator. Its protected `createPreparationPlan()` is overridden by Street, Drift and Track creators to instantiate the corresponding `PreparationPlan` product. The base `prepare()` method validates the plan, requests three components from `CarPlatformFactory<F>`, tunes/configures them, constructs `CarBuild<F>`, runs simulation and multi-component safety checks, and returns a report. It is a business workflow using an overridable creation step, not a static factory.

`CarPlatformFactory<F>` is the Abstract Factory. Every concrete factory produces ECU, suspension and brakes with the same family argument. The Creator imports only this interface, not concrete factories or products. Shared calibrated product implementations contain formulas; concrete products supply meaningful, platform-specific calibration values.

## Compile-time compatibility

The generic parameter `F extends PlatformFamily` represents platform identity. Java generics are invariant: `EngineControlUnit<NissanR34Family>` is not an `EngineControlUnit<PlatformFamily>`. The `CarBuild<F>` constructor requires `EngineControlUnit<F>`, `SuspensionSystem<F>` and `BrakeSystem<F>`, so the compiler cannot choose one F for three different concrete families.

Illustrative rejected construction (documentation only, intentionally not placed in test sources):

```java
// Given valid specification, tune, setups, mode and requirements:
// new CarBuild<NissanR34Family>(specification,
//     new NissanR34ECU(), new ToyotaSupraSuspension(), new MazdaRX7Brakes(),
//     tune, suspensionSetup, brakeSetup, mode, requirements);
// ERROR: ToyotaSupraSuspension cannot convert to SuspensionSystem<NissanR34Family>.
// Using new CarBuild<>(...) also fails: inference requires contradictory F values.
```

The runtime resolver returns `CarPlatformFactory<? extends PlatformFamily>`. A generic helper captures that unknown F for the entire workflow; no cast, raw type or runtime family check is required. As with all Java generic guarantees, deliberately using raw types, unchecked casts or falsely implementing a marker contract could bypass the design. Compilation enables `-Xlint:all -Werror`; this project does none of those things.

## Legacy limitations

`legacy/LegacyBuildConfigurator` preserves the direct-instantiation prototype. Its client constructs concrete legacy records in a growing switch. Adding a platform edits the client; repeated construction of three parts in every branch invites duplication and omissions. `LegacyBuild` accepts any platform-labelled ECU, suspension and brakes without a common type parameter, so cross-platform combinations are possible. The legacy data model is intentionally small, not a second full simulator.

## Model scope and units

All numerical values are illustrative workshop calibrations, not manufacturer specifications or physical safety certification. Horsepower and boost are hp/bar, mass is kg, ride height mm, temperature Celsius, braking distance m, track length km, thermal energy kJ and lap duration seconds. Handling and performance scores are dimensionless comparison scores.

Engine tuning caps boost using both the platform limit and the plan's fraction. The requested power can also be electronically limited below base power. Temperature reduces delivered power, with stronger heat sensitivity for the rotary engine. Track mode lowers ride height and raises grip and brake cooling; drift mode uses platform-specific grip calibration. Novice drivers receive a smaller boost envelope, and novice drift requests are rejected.

Safety combines engine power, mass, suspension handling, driver skill, surface grip, stopping distance and thermal capacity. Thermal load approximates one 140 km/h stop plus power-dependent repeated work over the session, adjusted for temperature. Simulation combines power-to-weight, handling, braking, traction and thermal fade to estimate average speed and lap duration. It returns results even when safety fails, so the report can explain the failure; the CLI returns exit code 2 for such a report.

## Fourth-family extension audit

Commit `54e0c62` introduces Mitsubishi only after the original system checkpoint `7e147a4`. It adds five production files and `MitsubishiExtensionTest.java`; only `config/VehiclePlatform.java` and `config/CarPlatformFactoryResolver.java` were modified. Verify with `git show --name-status 54e0c62`. Neither existing tests nor business algorithms changed for the extension. The existing client test loops over registered platforms, so it automatically includes Mitsubishi.

Mitsubishi has a 527.5 hp full-envelope ceiling, 1.5 bar boost limit, 0.0025/C heat sensitivity, 106 base handling, 145 mm street ride height, 1.12 wet-retention multiplier, 17,500 N brake force, 2,500 kJ base thermal capacity, 1,420 kg mass and 1.16 AWD traction factor. It retains better wet handling than the other calibrations while remaining below its dry handling score.

## SOLID and trade-offs

- Single Responsibility: CLI parses/presents, factories create, products model components, plans choose preparation constraints, and services calculate results.
- Open/Closed: adding Mitsubishi extends product implementations; preparation, validation and simulation are unchanged. Registration in the enum and resolver is an intentional composition-root modification.
- Liskov Substitution: concrete products obey the common units, tuning-envelope and configuration contracts.
- Interface Segregation: ECU, suspension and braking clients receive distinct role-specific interfaces.
- Dependency Inversion: the Creator uses abstract product/factory/plan contracts. The simple simulation and safety services are concrete collaborators because alternate implementations are not needed here.

The cost is more classes, generic signatures and explicit runtime registration. Abstract Factory makes adding a new family straightforward, but adding a fourth product type would require changing every factory. Factory Method is useful here because each Creator runs the same workflow with a different created plan; it would be unnecessary with a single fixed plan and no expected variation. Public marker contracts prevent accidental mixing of the supplied typed components, not deliberately dishonest third-party implementations.
