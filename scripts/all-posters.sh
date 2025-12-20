#!/usr/bin/env bash

# Example:
# $ bash scripts/all-posters.sh stories/2024

set -o errexit
set -o nounset
set -o pipefail
# set -o xtrace

poster="$HOME/Projects/smallgren.com/scripts/poster.sh"

# Parse the command-line arguments.
year_path="$1"

# Generate the poster images.
for video in "${year_path}"/media/*.{mov,mp4}
do
    bash "${poster}" "${video}"
done
