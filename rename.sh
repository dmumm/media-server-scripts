echo ---------------------
echo rename.sh loaded
echo .....................
myString="$2"
drive="${myString:0:1}"
drive="${drive,,}"
echo $drive
newPath="${myString:3}"
echo $newPath
path="${newPath//'\'/"/"}"
cd "/mnt/$drive/"

if [[ "$1" == *"Compiled"* ]]; then
	:
	
elif [[ "$1" == *"Spotify"* ]]; then
	:
	
elif [[ "$1" == *"Music"* ]]; then
	:

elif [[ "$1" == *"Movies"* ]]; then

	echo "movie"
	filebot -rename -r "$path" --db TheMovieDB -non-strict --file-filter "none{ext =~  /jpg|png|m2ts|rar|r[0-9]+/ }{ fn.match(/sample|trailer/) }{ f =~ /Extras|Featurettes/ }" --format '/mnt/c/bin/movie_preset_withsubs.groovy' --conflict INDEX --action DUPLICATE --output "/mnt/$drive/"

elif [[ "$1" == *"TV"* ]]; then

	echo "tv"
	filebot -rename -r "$path" --db TheMovieDB::TV --order DVD -non-strict --file-filter f.video --format '/mnt/c/bin/tv_preset.groovy' --conflict INDEX --action DUPLICATE --output "/mnt/$drive/"


else #pseudo code 

	echo "other"
	filebot -rename -r "$path" --format "/Unsorted/{fn}" --conflict INDEX --action DUPLICATE --output "/mnt/$drive/"

fi
echo "renamed"