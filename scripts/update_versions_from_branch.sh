#!/bin/bash
set -e

# Validate if the format provided for the tag is {MAJOR}.{MINOR}.{PATCH}
# And commit current version CODE and NAME updates if conditions pass
function is_up_to_date() {
  CURRENT_CONFIG_VERSION=$(cat build.gradle.kts | sed -n -e "s/^.*versionNameConfig //p" | tr -cd '[:digit:]')
  if [ "$CURRENT_CONFIG_VERSION" == "$1" ]
  then
    echo true
  else
    echo false
  fi
}

function update_version_name() {
  VERSION_NAME=$1

  sed -i -e "s/val versionNameConfig by extra { .*/val versionNameConfig by extra { \"${VERSION_NAME}\" }/g" build.gradle.kts
}

function update_version_code() {
  VERSION_CODE=$(cat build.gradle.kts | sed -n -e "s/^.*versionCodeConfig //p" | tr -cd '[:digit:]')
  VERSION_CODE_UPDATE=$((VERSION_CODE+1))

  echo "Update Version Code to $VERSION_CODE_UPDATE"
  sed -i -e "s/val versionCodeConfig .*/val versionCodeConfig by extra { $VERSION_CODE_UPDATE } /g" build.gradle.kts
}

BRANCH="$CI_GIT_BRANCH"
echo "Release branch pushed: $BRANCH"
echo "BRANCH=$BRANCH"

FULL_VERSION=${BRANCH#*-}

MAJOR=$(echo "${FULL_VERSION}"  | cut -d "." -f1)
echo "MAJOR=$MAJOR"

MINOR=$(echo "${FULL_VERSION}"  | cut -d "." -f2)
echo "MINOR=$MINOR"

PATCH=$(echo "${FULL_VERSION}"  | cut -d "." -f3)
echo "PATCH=$PATCH"

if ! [[ $MAJOR =~ ^[0-9]{1,2}$ ]] ; then
    echo "Invalid Version -> Major: $MAJOR"
    exit 1
fi

if ! [[ $MINOR =~ ^[0-9]{1,2}$ ]] ; then
    echo "Invalid Version -> Minor: $MINOR"
    exit 1
fi

if ! [[ -z "$PATCH" || $PATCH =~ ^[0-9]{1}$ ]] ; then
    echo "Invalid Version -> Patch: $PATCH"
    exit 1
fi

IS_UP_TO_DATE=$(is_up_to_date "$MAJOR""$MINOR""$PATCH");
if [ "$IS_UP_TO_DATE" == false ]
then
  echo "Update all brands name and code versions"
  update_version_name "${FULL_VERSION}"
  update_version_code

  git config --global user.name "User Name"
  git config --global user.email "username@email.com"
  git commit -a -m "[Release] Update versions"
  git push --all
else
  echo "Versions already up to date"
fi