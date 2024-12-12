#!/bin/bash
set -ex

# Create ZIP on the CI Deploy directory
(cd "${CI_SOURCE_DIR}/app/build/reports/kover/htmlRelease" && zip -r "${CI_DEPLOY_DIR}/coverage-android.zip" .)

# Copy report to CI HTML Report directory
HTML_REPORT_DIR="${CI_HTML_REPORT_DIR}/coverage-android"
mkdir -p "$HTML_REPORT_DIR" && cp -r "${CI_SOURCE_DIR}/app/build/reports/kover/htmlRelease/." "$HTML_REPORT_DIR"#!/bin/bash
set -ex

# Create ZIP on the CI Deploy directory
(cd "${CI_SOURCE_DIR}/app/build/reports/kover/htmlRelease" && zip -r "${CI_DEPLOY_DIR}/coverage-android.zip" .)

# Copy report to CI HTML Report directory
HTML_REPORT_DIR="${CI_HTML_REPORT_DIR}/coverage-android"
mkdir -p "$HTML_REPORT_DIR" && cp -r "${CI_SOURCE_DIR}/app/build/reports/kover/htmlRelease/." "$HTML_REPORT_DIR"