# The Bag lives on the BFF Cart

The Bag moves from an in-memory list to the BFF's Cart, which becomes its source of truth
([ALFMOB-568](https://mindera.atlassian.net/browse/ALFMOB-568)). iOS's spikes on the same questions
([ALFMOB-494](https://mindera.atlassian.net/browse/ALFMOB-494),
[ALFMOB-495](https://mindera.atlassian.net/browse/ALFMOB-495)) were still open, so Android decided
on its own rather than wait. Where web had already decided ([ALFMOB-460](https://mindera.atlassian.net/browse/ALFMOB-460)),
Android follows it.

## Decisions

- **The Cart id lives in a proto DataStore, unencrypted.** Web keeps it in plain client storage too.
  It's a bearer secret for an anonymous guest Cart, which isn't worth the cost of encryption. It
  gets a DataStore of its own, so dropping it can't touch other preferences.
- **A Cart is created on the first add, not at launch.** `createCart` takes the line, so the first
  add is one round-trip. This matches web.
- **Writes are pessimistic.** Every Cart mutation returns the whole Cart, which replaces the Bag
  wholesale. There's no rollback to get wrong. Swipe-to-remove hides the line while the call is in
  flight and restores it, with an error, if the call fails.
- **One Cart call at a time.** The `BagRepository` implementation runs every Cart call, the launch
  fetch included, behind a mutex. Without it, a double-tap on Add to Bag before any Cart exists
  mints two Carts and one Cart's line silently disappears from the Bag. Out-of-order responses
  could also bring back a line that was just removed. Serialising fixes both with no UI changes.
  `updateCart` changes only the lines it names, so iOS's worry that it overwrites the whole Bag
  doesn't apply.
- **An unknown or expired Cart id is a fresh start.** Shopify and BigCommerce answer `NOT_FOUND`.
  The Mock BFF and SCAYLE return an empty Cart instead. On `NOT_FOUND` the app drops the id. A
  launch fetch then shows the empty Bag, and an add retries once as `createCart`. An empty Cart
  keeps its id, because adds to it still work. Cart calls read the GraphQL `extensions.code`,
  because the app's generic error mapper doesn't.
- **The Bag line row takes its other fields from the product, fetched by slug.** `CartItem` has no
  brand, colour, size, was-price or stock. Android's Bag already fetches each product from the
  BFF, so it keeps doing that, now keyed by `CartItem.slug`. Quantity, prices and totals come from
  the Cart. If a product fetch fails, that line falls back to the Cart's name, image and price. So
  Android needs no BFF change. iOS planned one to enrich `CartItem`; this is an alternative for
  them.
- **No quantity stepper, so no `updateCart`.** The Bag has no control to change a quantity in place,
  and its visual design is out of scope. Adding a variant that's already in the Bag merges into its
  line on the BFF.

## Considered options

- **Enrich `CartItem` on the BFF, as iOS plans.** Rejected for Android: it would be a cross-repo
  dependency for fields the app already fetches.
- **Optimistic writes with rollback**, which web's PDP uses. Rejected: every mutation returns the
  whole Cart, which makes pessimistic writes cheap to get right. It also makes optimistic ones
  easy to get subtly wrong.
- **Disable the Bag while a write is in flight.** Rejected: it blocks the UI to prevent a race that
  serialising Cart calls removes for free.
