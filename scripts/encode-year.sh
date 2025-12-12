#!/usr/bin/env bash

# Example:
# $ bash scripts/encode-year.sh resources/stories/2024

set -o errexit
set -o nounset
set -o pipefail
# set -o xtrace

encode="$HOME/Projects/smallgren.com/scripts/encode.sh"

# Parse the command-line arguments.
year_path="$1"

# Convert all of the images for that year.
for video in "${year_path}"/media/*.{mov,mp4}
do
  echo "Converting: $video"
  bash "$encode" "$video"
done
