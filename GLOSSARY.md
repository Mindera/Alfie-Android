# Alfie Android

A native Android storefront for a fashion retailer, backed by a GraphQL BFF that fronts a commerce
platform. This glossary fixes the vocabulary used across the app and its tests.

## Language

### Test backends and builds

**Mock BFF**:
The BFF running on its mock platform: a fixed, seeded catalogue and in-memory carts, with no
commerce platform behind it. What integration and UI tests run against.
_Avoid_: fake backend, stub server, local BFF (a local BFF may be Live)

**Live BFF**:
The BFF fronting a real commerce platform's store, whose catalogue, stock and menus change outside
our control.
_Avoid_: real backend, prod BFF (Live includes dev and pre-prod stores)

**Seed anchor**:
A named piece of Mock BFF data that the BFF promises to keep stable. The only Mock BFF data a test
may pin to; anything else may change without notice.
_Avoid_: fixture, test product, seed data (in general)

**UI test build**:
The app variant the UI journeys run against: debug-derived, never distributed, with analytics and
crash reporting switched off.
_Avoid_: test flavour, UI test signal (the iOS mechanism), test mode
