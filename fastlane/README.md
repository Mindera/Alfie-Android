fastlane documentation
----

# Installation

Make sure you have the latest version of the Xcode command line tools installed:

```sh
xcode-select --install
```

For _fastlane_ installation instructions, see [Installing _fastlane_](https://docs.fastlane.tools/#installing-fastlane)

# Available Actions

## Android

### android branch_validation

```sh
[bundle exec] fastlane android branch_validation
```

Pull Request validation

### android static_code_analysis

```sh
[bundle exec] fastlane android static_code_analysis
```

Static Code Analysis

### android execute_tests

```sh
[bundle exec] fastlane android execute_tests
```

Execute Unit Tests

### android validate_and_deploy

```sh
[bundle exec] fastlane android validate_and_deploy
```

Validate and Deploy Debug

### android deploy_debug

```sh
[bundle exec] fastlane android deploy_debug
```

Deploy Debug Build

### android deploy_beta

```sh
[bundle exec] fastlane android deploy_beta
```

Deploy Beta Build

### android deploy_release

```sh
[bundle exec] fastlane android deploy_release
```

Deploy Release Build

### android nightly

```sh
[bundle exec] fastlane android nightly
```

Nightly tasks

----

This README.md is auto-generated and will be re-generated every time [_fastlane_](https://fastlane.tools) is run.

More information about _fastlane_ can be found on [fastlane.tools](https://fastlane.tools).

The documentation of _fastlane_ can be found on [docs.fastlane.tools](https://docs.fastlane.tools).
