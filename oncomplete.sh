#!/bin/bash
# 
# oncomplete.sh - Post-download processing script
# Usage: oncomplete.sh [category] [path]
#
# This script is triggered when a download completes and handles 
# extraction and renaming operations

echo "-------------------------"
echo "Post-download processing"
echo "-------------------------"
echo "Category: $1" 
echo "Path: $2" 

# Extract archives if present
bash unRAR.sh "$1" "$2"

# Rename files according to media type
bash rename.sh "$1" "$2"