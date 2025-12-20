#!/usr/bin/env bash

# Example:
# $ bash scripts/convert-year.sh stories/2024

set -o errexit
set -o nounset
set -o pipefail
# set -o xtrace

convert="$HOME/Projects/smallgren.com/scripts/convert.sh"

# Parse the command-line arguments.
year_path="$1"

# Convert all of the images for that year.
for image in "${year_path}"/media/*.jpeg
do
  echo "Converting: $image"
  bash "$convert" "$image"
done
