#!/bin/bash
#
# unRAR.sh - Extracts RAR archives from download directory
# Usage: unRAR.sh [category] [path]
#

echo "-------------------------"
echo "Extracting archives"
echo "-------------------------"

# Extract parameters
source_path="$2"

# Convert Windows path to Unix path
drive="${source_path:0:1}"
drive="${drive,,}"
newPath="${source_path:3}"
path="${newPath//'\'/"/"}"

echo "Processing path: $path"

# Extract all RAR archives in the directory
7z e -an -air!"/mnt/$drive/$path/*.rar" -r -o"/mnt/$drive/$path"

echo "Extraction complete"