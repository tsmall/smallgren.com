#!/usr/bin/env bash

set -o errexit
set -o nounset
set -o pipefail
set -o xtrace

# Parse the command-line arguments ...
input_file="$1"

# Get the filename without the extension ...
image_name="${input_file%.*}"

magick "${input_file}" "${image_name}.avif"
magick "${input_file}" "${image_name}.webp"
