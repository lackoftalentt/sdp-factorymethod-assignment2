# Oral Defense Guide

## 1. What problem does Factory Method solve here?

Street, drift and track builds share the same preparation workflow but need different preparation policies. Factory Method lets each Creator subclass choose the plan used by that inherited workflow.

## 2. What is the Product in your Factory Method?

The Product is `PreparationPlan`. It defines the mode, allowed boost fraction, driver validation and plan name that the Creator uses.

## 3. What is the Creator?

`BuildPreparationService` is the abstract Creator. It owns the public `prepare()` workflow and declares the protected factory method.

## 4. What are the Concrete Creators?

They are `StreetBuildPreparationService`, `DriftBuildPreparationService` and `TrackBuildPreparationService`. Each overrides the creation step to instantiate its matching plan.

## 5. Where exactly is the Factory Method?

It is `protected abstract PreparationPlan createPreparationPlan()` in `BuildPreparationService`. The base `prepare()` method invokes it, and Java dispatches that call to the selected Creator subclass.

## 6. Why is this Factory Method and not a static factory?

An inherited business workflow delegates product creation to an overridable instance method. A static factory usually selects an object through a static helper; it does not provide this Creator/subclass workflow relationship.

## 7. What business logic exists in the Creator?

The Creator validates the plan, obtains three compatible components, tunes the engine and configures suspension and brakes. It creates a typed build, invokes simulation and safety validation, and assembles a report explaining limits and check results.

## 8. What problem does Abstract Factory solve?

A vehicle needs a coherent set of platform-specific components. Abstract Factory provides that set through one interface, keeping concrete component construction out of the business workflow.

## 9. What are your product types?

They are `EngineControlUnit<F>`, `SuspensionSystem<F>` and `BrakeSystem<F>`. These interfaces expose tuning, handling, stopping-distance and thermal-capacity behavior rather than only descriptions.

## 10. What are your product families?

The original families are Nissan Skyline GT-R R34, Toyota Supra MK4 and Mazda RX-7 FD. Mitsubishi Lancer Evolution IX is a later fourth-family extension recorded separately in Git.

## 11. Why do ECU, suspension and brakes belong to one family?

They are platform-specific workshop components with coordinated engine, mass, handling and braking calibrations. A Supra component should not accidentally become part of an R34 build just because both implement a broadly similar component interface.

## 12. How does your architecture prevent incompatible products?

Every product carries its platform marker as a generic argument. The `CarBuild<F>` constructor requires all three products to have the same F, so the compiler rejects mixed concrete families.

## 13. Why did you use generics?

Generics express compatibility before the program runs. They also allow one preparation workflow to handle every platform while preserving its exact family type throughout creation and reporting.

## 14. What does F mean?

F is a type parameter representing one platform family, bounded by `PlatformFamily`. For example, inside `NissanR34Factory` it is `NissanR34Family`; the marker is a type identity, not a runtime platform object.

## 15. Could Nissan ECU accidentally be combined with Toyota suspension?

Not in a correctly typed `CarBuild<F>` made from these implementations: Java cannot satisfy both different invariant generic arguments. Deliberate raw types, unchecked casts or a dishonest implementation could undermine Java's type contract, but the project uses none of those mechanisms and treats compiler warnings as errors.

## 16. Where are concrete factories instantiated?

Production platform factories are instantiated only in `CarPlatformFactoryResolver`. Tests instantiate factories directly to verify their contracts; factories themselves instantiate the component products.

## 17. Why is runtime factory selection centralized?

Concrete implementation selection must happen somewhere when input arrives as a string. Centralizing it in the composition root means a new platform changes registration without spreading platform branches through business algorithms.

## 18. Does your business logic know which concrete family is used?

No: the Creator uses `CarPlatformFactory<F>` and abstract product interfaces, while simulation and safety inspect a build through those interfaces. Names are used for presentation, not as conditions selecting business algorithms.

## 19. What happens when you add a fourth family?

I add a marker, three concrete products and a concrete factory, then register the new platform. The existing Creator, build aggregate and business services work through the same contracts without modification.

## 20. Which files changed when Mitsubishi was added?

Five production files were added: `MitsubishiEvo9Family`, `MitsubishiEvo9ECU`, `MitsubishiEvo9Suspension`, `MitsubishiEvo9Brakes` and `MitsubishiEvo9Factory`, plus `MitsubishiExtensionTest`. Only `VehiclePlatform.java` and `CarPlatformFactoryResolver.java` were modified in extension commit `54e0c62`; the README lists complete paths.

## 21. Did existing business logic change?

No business algorithm changed for Mitsubishi. `git diff 7e147a4 54e0c62 -- src/main/java/org/example/jdm/factorymethod src/main/java/org/example/jdm/service src/main/java/org/example/jdm/domain` produces no differences.

## 22. Which SOLID principles are demonstrated?

Responsibilities are separated between CLI, creation, component behavior, plans and business services; clients depend on product/factory abstractions. The three narrow product interfaces demonstrate interface segregation, substitutable products follow shared contracts, and the unchanged workflow demonstrates Open/Closed at the business layer.

## 23. Where is Open/Closed Principle demonstrated?

Mitsubishi extends the system through new implementations while existing preparation, simulation and safety logic remains unchanged. The enum and resolver still require registration, so the claim is about stable business algorithms, not that every file is closed to all edits.

## 24. What are the disadvantages of this architecture?

It introduces more classes, generic signatures and indirection than a small switch would require. New families are straightforward, but adding a new product category would require updating the abstract factory and every concrete factory; the numerical model is also only an educational approximation.

## 25. When would Factory Method be unnecessary?

If the program had one fixed preparation policy and no need to specialize creation inside a workflow, direct construction could be clearer. Constructor injection of a plan could also be sufficient in another design; this assignment deliberately demonstrates a genuine Creator hierarchy.

## 26. When would Abstract Factory be unnecessary?

If there were only one platform or components could vary independently without compatibility constraints, a family factory might add little value. Here, several related products must change together when the platform changes.

## 27. Why did you not simply use one large switch?

A large switch in the workflow would combine platform selection, concrete construction and preparation rules, making extensions touch business code. The final design keeps a small switch at startup and uses polymorphic behavior afterward.

## 28. What is the difference between your Factory Method and your factory resolver?

The resolver maps external enum choices to implementations and belongs to runtime wiring. Factory Method is the overridden `createPreparationPlan()` step inside the Creator's business workflow; Abstract Factory separately supplies a platform's compatible components.

## 29. What are your three business operations?

Preparation creates and configures a complete build, safety validation checks combined power/handling/braking constraints, and track simulation estimates performance using all three components. Each returns structured domain results, and console output is confined to the application layer.

## 30. Which tests prove that the design works?

The 22 JUnit tests cover concrete family creation, calibrated numerical differences, coherent typed builds, all three Creator workflows, runtime resolution, invalid inputs, business behavior and Mitsubishi. The unchanged multi-platform client test covers every registered family, and the CLI test covers all twelve platform/mode combinations; compile-time rejection is explained with a commented invalid construction in the README and assignment notes.

## Suggested five-minute demonstration

1. Show `LegacyBuildConfigurator` and its repeated direct construction.
2. Open `BuildPreparationService.prepare()` and point to the factory method and the three Abstract Factory calls.
3. Show `CarBuild<F>` and the commented mixed-family example in the README.
4. Run the Nissan/Track, Toyota/Street and Mazda/Drift commands from the README; compare power, handling and braking.
5. Show `git show --stat 54e0c62`, run Mitsubishi/Track and explain why the client did not change.
6. Run `mvn test`, then open `docs/architecture.puml` to identify the two pattern groups.
