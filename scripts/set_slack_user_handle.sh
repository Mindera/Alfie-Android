#!/bin/bash
set -e

function get_slack_user_handle() {
  if [[ $GIT_CLONE_COMMIT_AUTHOR_EMAIL = "name@example.com" ]]
  then
      echo "<@id>"
  else
      echo "Unknown User"
  fi
}

USER_HANDLE=$(get_slack_user_handle);
envman add --key SLACK_COMMIT_USER_HANDLE --value "${USER_HANDLE}"