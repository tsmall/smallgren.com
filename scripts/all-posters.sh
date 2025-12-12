#!/usr/bin/env bash

set -o errexit
set -o nounset
set -o pipefail
# set -o xtrace

poster="$HOME/Projects/smallgren.com/scripts/poster.sh"

# Parse the command-line arguments ...
path="$1"

# Generate the poster images ...
for video in "${path}"/*.mov
do
    bash "${poster}" "${video}"
done
