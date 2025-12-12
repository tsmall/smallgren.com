#!/usr/bin/env bash

set -o errexit
set -o nounset
set -o pipefail
set -o xtrace

# Parse the command-line arguments ...
input_file="$1"

# Get the filename without the extension ...
video_name="${input_file%.*}"

# Set up common ffmpeg flags ...
disabled_audio_flag='-an'
pixel_format_flag='-pix_fmt yuv420p'
video_bitrate_flags='-b:v 4M -maxrate 8M'
common_flags="${disabled_audio_flag} ${pixel_format_flag} ${video_bitrate_flags}"

# Convert to h264 ...
ffmpeg -i "${input_file}" ${common_flags} -c:v libx264     -f mp4  -profile:v high -pass 1    "${video_name}.h264.mp4"
ffmpeg -i "${input_file}" ${common_flags} -c:v libx264     -f mp4  -profile:v high         -y "${video_name}.h264.mp4"

# Convert to vp8 ...
ffmpeg -i "${input_file}" ${common_flags} -c:v libvpx      -f webm                 -pass 1    "${video_name}.vp8.webm"
ffmpeg -i "${input_file}" ${common_flags} -c:v libvpx      -f webm                         -y "${video_name}.vp8.webm"

# Convert to vp9 ...
ffmpeg -i "${input_file}" ${common_flags} -c:v libvpx-vp9  -f webm                 -pass 1    "${video_name}.vp9.webm"
ffmpeg -i "${input_file}" ${common_flags} -c:v libvpx-vp9  -f webm                         -y "${video_name}.vp9.webm"

# Clean up ...
rm ffmpeg*.*
