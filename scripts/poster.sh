#!/usr/bin/env bash

set -o errexit
set -o nounset
set -o pipefail
# set -o xtrace

# Parse the command-line arguments ...
input_file="$1"

# Get the filename without the extension ...
video_name="${input_file%.*}"

# Format the output filename ...
poster_name="${video_name}-poster.jpg"

# Generate poster image ...
echo -n "Generating poster for '${input_file}' ... "
ffmpeg -i "${input_file}" -frames:v 1 -y "${poster_name}" &> /dev/null
echo "done."
