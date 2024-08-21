echo ---------------------
echo oncomplete.sh loaded
echo .....................
echo "$1" 
echo "$2" 
bash unRAR.sh "$1" "$2"
bash rename.sh "$1" "$2"
read