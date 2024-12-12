#!/bin/bash
set -e

# Useful documentation and articles related to Compose metrics:
# Compose-metrics.md -> https://github.com/androidx/androidx/blob/androidx-main/compose/compiler/design/compiler-metrics.md
# "Optimize app performance" -> https://proandroiddev.com/optimize-app-performance-by-mastering-stability-in-jetpack-compose-69f40a8c785d
# "Composable metrics" -> https://chrisbanes.me/posts/composable-metrics/

CI_TOKEN=$USER_ACCESS_TOKEN
CI_APP_SLUG=slug
CI_APP_BRANCH=master
CI_APP_WORKFLOW=nightly
CI_API_HOST=https://ci.com
COMPOSE_METRICS_DIR="${CI_SOURCE_DIR}/compose_metrics"
COMPOSE_METRICS_LOG="${COMPOSE_METRICS_DIR}/aggregate_compose_metrics.json"
COMPOSE_METRICS_SUFFIX=*_release-module.json
COMPOSE_METRICS_FILENAME=compose-metrics.json

function download_statistics_file() {
  ARTIFACT_SLUG=$1
  API_ARTIFACT_DETAILS_REQUEST_URL="${CI_API_HOST}/apps/${CI_APP_SLUG}/builds/${BUILD_SLUG}/artifacts/${ARTIFACT_SLUG}"
  DOWNLOAD_URL=( $(curl -H "Authorization: ${CI_TOKEN}" "${API_ARTIFACT_DETAILS_REQUEST_URL}" | jq -r '.data.expiring_download_url') )
  curl -o $COMPOSE_METRICS_FILENAME $DOWNLOAD_URL
}

function create_new_statistics_file() {
    initial_json='{
        "statistics": {
            "avgUnstableArguments": 0,
            "avgUnstableClasses": 0,
            "avgUncertainClasses": 0,
            "unstableArgumentPercent": 0,
            "unstableClassPercent": 0,
            "uncertainClassPercent": 0,
            "skippableComposablePercent": 0,
            "restartableComposablePercent": 0
        },
        "previous": []
    }'
    echo "$initial_json" > $COMPOSE_METRICS_FILENAME
}

function aggregate_and_merge_all_files() {
  skippableComposables=0
  restartableComposables=0
  readonlyComposables=0
  totalComposables=0
  restartGroups=0
  totalGroups=0
  staticArguments=0
  certainArguments=0
  knownStableArguments=0
  knownUnstableArguments=0
  unknownStableArguments=0
  totalArguments=0
  markedStableClasses=0
  inferredStableClasses=0
  inferredUnstableClasses=0
  inferredUncertainClasses=0
  effectivelyStableClasses=0
  totalClasses=0
  memoizedLambdas=0
  singletonLambdas=0
  singletonComposableLambdas=0
  composableLambdas=0
  totalLambdas=0

  # Create dir if it doesn't exist
  mkdir -p $COMPOSE_METRICS_DIR

  # Find all the files and copy them to a single destination so that they can be archived later
  # The temporary dir is necessary to prevent bash from tripping over itself doing too many things at once
  temp_dir=$(mktemp -d)
  files=$(find "${CI_SOURCE_DIR}" -name "${COMPOSE_METRICS_SUFFIX}")
  echo "$files" | xargs -I {} cp {} "$temp_dir"
  cp -r "$temp_dir"/* $COMPOSE_METRICS_DIR
  rm -r "$temp_dir"

  # Merge them all into a single log file
  for file in $files; do
    # Get the data we need from each file
    data=$(jq -r '.skippableComposables, .restartableComposables, .readonlyComposables, .totalComposables, .restartGroups,
      .totalGroups, .staticArguments, .certainArguments, .knownStableArguments, .knownUnstableArguments,
      .unknownStableArguments, .totalArguments, .markedStableClasses, .inferredStableClasses, .inferredUnstableClasses,
      .inferredUncertainClasses, .effectivelyStableClasses, .totalClasses, .memoizedLambdas, .singletonLambdas,
      .singletonComposableLambdas, .composableLambdas, .totalLambdas' "$file")

    # Parse it neatly
    values="$(IFS=$'\n' read -d '' -ra values <<< "$data" ; echo "${values[*]-}")"
    read -r -a array_values <<< "$values"

    # And add it all up
    skippableComposables=$((skippableComposables + array_values[0]))
    restartableComposables=$((restartableComposables + array_values[1]))
    readonlyComposables=$((readonlyComposables + array_values[2]))
    totalComposables=$((totalComposables + array_values[3]))
    restartGroups=$((restartGroups + array_values[4]))
    totalGroups=$((totalGroups + array_values[5]))
    staticArguments=$((staticArguments + array_values[6]))
    certainArguments=$((certainArguments + array_values[7]))
    knownStableArguments=$((knownStableArguments + array_values[8]))
    knownUnstableArguments=$((knownUnstableArguments + array_values[9]))
    unknownStableArguments=$((unknownStableArguments + array_values[10]))
    totalArguments=$((totalArguments + array_values[11]))
    markedStableClasses=$((markedStableClasses + array_values[12]))
    inferredStableClasses=$((inferredStableClasses + array_values[13]))
    inferredUnstableClasses=$((inferredUnstableClasses + array_values[14]))
    inferredUncertainClasses=$((inferredUncertainClasses + array_values[15]))
    effectivelyStableClasses=$((effectivelyStableClasses + array_values[16]))
    totalClasses=$((totalClasses + array_values[17]))
    memoizedLambdas=$((memoizedLambdas + array_values[18]))
    singletonLambdas=$((singletonLambdas + array_values[19]))
    singletonComposableLambdas=$((singletonComposableLambdas + array_values[20]))
    composableLambdas=$((composableLambdas + array_values[21]))
    totalLambdas=$((totalLambdas + array_values[22]))
  done

  # Create JSON file with aggregated data
  echo "{
    \"skippableComposables\": $skippableComposables,
    \"restartableComposables\": $restartableComposables,
    \"readonlyComposables\": $readonlyComposables,
    \"totalComposables\": $totalComposables,
    \"restartGroups\": $restartGroups,
    \"totalGroups\": $totalGroups,
    \"staticArguments\": $staticArguments,
    \"certainArguments\": $certainArguments,
    \"knownStableArguments\": $knownStableArguments,
    \"knownUnstableArguments\": $knownUnstableArguments,
    \"unknownStableArguments\": $unknownStableArguments,
    \"totalArguments\": $totalArguments,
    \"markedStableClasses\": $markedStableClasses,
    \"inferredStableClasses\": $inferredStableClasses,
    \"inferredUnstableClasses\": $inferredUnstableClasses,
    \"inferredUncertainClasses\": $inferredUncertainClasses,
    \"effectivelyStableClasses\": $effectivelyStableClasses,
    \"totalClasses\": $totalClasses,
    \"memoizedLambdas\": $memoizedLambdas,
    \"singletonLambdas\": $singletonLambdas,
    \"singletonComposableLambdas\": $singletonComposableLambdas,
    \"composableLambdas\": $composableLambdas,
    \"totalLambdas\": $totalLambdas
  }" > $COMPOSE_METRICS_LOG
}

echo "Start Compose metrics data aggregation and storage"
echo "Finding the latest nightly build and downloading Compose metrics artifact"
API_BUILDS_REQUEST_URL="${CI_API_HOST}/apps/${CI_APP_SLUG}/builds?sort_by=created_at&workflow=nightly&limit=1&status=1"
BUILD_SLUG=( $(curl -H "Authorization: ${CI_TOKEN}" "${API_BUILDS_REQUEST_URL}" | jq -r '.data[0].slug') )

if [ "$BUILD_SLUG" != '' ]; then
  echo "Previous nightly build found: $BUILD_SLUG"
  API_BUILD_ARTIFACTS_REQUEST_URL="${CI_API_HOST}/apps/${CI_APP_SLUG}/builds/${BUILD_SLUG}/artifacts"
  ARTIFACT_SLUG=( $(curl -H "Authorization: ${CI_TOKEN}" "${API_BUILD_ARTIFACTS_REQUEST_URL}" | jq -r '.data[] | select(.title == "'${COMPOSE_METRICS_FILENAME}'") | .slug') )

  # If there's a previous artifact, download and parse it
  if [ "$ARTIFACT_SLUG" != '' ]; then
    echo "Found artifact, slug is: $ARTIFACT_SLUG"
    echo "Previous metrics file exists, downloading it now..."
    download_statistics_file $ARTIFACT_SLUG
  else
    echo "No previous metrics were found in the latest build, creating a new file"
    create_new_statistics_file
  fi
else
  echo "Could not find a previous nightly build"
  create_new_statistics_file
fi

echo "Obtaining metrics from current nightly build..."
aggregate_and_merge_all_files

echo "Compose metrics for current build:"
METRICS_FILE_CONTENT=$(cat $COMPOSE_METRICS_FILENAME) # The aggregated metrics file
METRICS_LOG_CONTENT=$(cat $COMPOSE_METRICS_LOG) # The newly generated data file
echo $METRICS_LOG_CONTENT

# Add a timestamp to the new data
timestamp=$(date +"%Y-%m-%dT%H:%M:%SZ")
METRICS_LOG_CONTENT=$(echo $METRICS_LOG_CONTENT | jq --arg timestamp "$timestamp" '{timestamp: $timestamp} + .')

# Add new metrics element to 'previous' array in json, deleting oldest one if it exceeds the limit (30)
UPDATED_METRICS_FILE=$(echo "$METRICS_FILE_CONTENT" | jq --argjson new "$METRICS_LOG_CONTENT" '
  if (.previous | length) >= 30 then
    .previous = ([$new] + .previous[:-1])
  else
    .previous = [$new] + .previous
  end
')

# Calculate and load the new data into the metrics file
echo "Calculating Compose metrics between releases"
avgUnstableArguments=$(echo "$UPDATED_METRICS_FILE" | jq -r '.previous[].knownUnstableArguments | tonumber' | awk '{sum+=$1} END {print sum/NR}')
avgUnstableClasses=$(echo "$UPDATED_METRICS_FILE" | jq -r '.previous[].inferredUnstableClasses | tonumber' | awk '{sum+=$1} END {print sum/NR}')
avgUncertainClasses=$(echo "$UPDATED_METRICS_FILE" | jq -r '.previous[].inferredUncertainClasses | tonumber' | awk '{sum+=$1} END {print sum/NR}')
unstableArgumentPercent=$(awk 'BEGIN{printf "%.2f\n", ('$knownUnstableArguments' / '$totalArguments') * 100}')
unstableClassPercent=$(awk 'BEGIN{printf "%.2f\n", ('$inferredUnstableClasses' / '$totalClasses') * 100}')
uncertainClassPercent=$(awk 'BEGIN{printf "%.2f\n", ('$inferredUncertainClasses' / '$totalClasses') * 100}')
skippableComposablePercent=$(awk 'BEGIN{printf "%.2f\n", ('$skippableComposables' / '$totalComposables') * 100}')
restartableComposablePercent=$(awk 'BEGIN{printf "%.2f\n", ('$restartableComposables' / '$totalComposables') * 100}')

UPDATED_METRICS_FILE=$(echo "$UPDATED_METRICS_FILE" | jq '
  .statistics.avgUnstableArguments = $new_avg_unstable_arguments |
  .statistics.avgUnstableClasses = $new_avg_unstable_classes |
  .statistics.avgUncertainClasses = $new_avg_uncertain_classes |
  .statistics.unstableArgumentPercent = $new_unstable_argument_percent |
  .statistics.unstableClassPercent = $new_unstable_class_percent |
  .statistics.uncertainClassPercent = $new_uncertain_class_percent |
  .statistics.skippableComposablePercent = $new_skippable_composable_percent |
  .statistics.restartableComposablePercent = $new_restartable_composable_percent
' --argjson new_avg_unstable_arguments "$avgUnstableArguments" \
  --argjson new_avg_unstable_classes "$avgUnstableClasses" \
  --argjson new_avg_uncertain_classes "$avgUncertainClasses" \
  --argjson new_unstable_argument_percent "$unstableArgumentPercent" \
  --argjson new_unstable_class_percent "$unstableClassPercent" \
  --argjson new_uncertain_class_percent "$uncertainClassPercent" \
  --argjson new_skippable_composable_percent "$skippableComposablePercent" \
  --argjson new_restartable_composable_percent "$restartableComposablePercent"
)

# Save the updated data into the file
echo "$UPDATED_METRICS_FILE" > $COMPOSE_METRICS_FILENAME

# Create zip of all the log files
echo "Moving files to CI deploy directory, finalize the process"
(cd "$COMPOSE_METRICS_DIR" && zip -r "${CI_DEPLOY_DIR}/compose-metrics.zip" .)
cp $COMPOSE_METRICS_FILENAME $CI_DEPLOY_DIR