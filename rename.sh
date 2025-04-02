#!/bin/bash
#
# rename.sh - Organizes and renames media files using filebot
# Usage: rename.sh [category] [path]
#

echo "-------------------------"
echo "Media file organization"
echo "-------------------------"

# Extract parameters
category="$1"
source_path="$2"

# Convert Windows path to Unix path
drive="${source_path:0:1}"
drive="${drive,,}"
newPath="${source_path:3}"
path="${newPath//'\'/"/"}"

# Change to the appropriate drive
cd "/mnt/$drive/"

# Process by category
if [[ "$category" == *"Compiled"* ]]; then
    echo "Skipping compiled content"
    
elif [[ "$category" == *"Music"* ]]; then
    echo "Processing music files"
    # Add music-specific processing here
    
elif [[ "$category" == *"Movies"* ]]; then
    echo "Processing movie files"
    filebot -rename -r "$path" --db TheMovieDB -non-strict \
      --file-filter "none{ext =~ /jpg|png|m2ts|rar|r[0-9]+/ }{ fn.match(/sample|trailer/) }{ f =~ /Extras|Featurettes/ }" \
      --format '/path/to/movie_preset.groovy' \
      --conflict INDEX --action DUPLICATE --output "/mnt/$drive/"

elif [[ "$category" == *"TV"* ]]; then
    echo "Processing TV show files"
    filebot -rename -r "$path" --db TheMovieDB::TV --order DVD -non-strict \
      --file-filter f.video \
      --format '/path/to/tv_preset.groovy' \
      --conflict INDEX --action DUPLICATE --output "/mnt/$drive/"

else
    echo "Processing miscellaneous files"
    filebot -rename -r "$path" --format "/Unsorted/{fn}" \
      --conflict INDEX --action DUPLICATE --output "/mnt/$drive/"
fi

echo "File organization complete"