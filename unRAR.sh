echo ---------------------
echo unRAR.sh loaded
echo .....................
echo "$2"
myString="$2"
drive="${myString:0:1}"
drive="${drive,,}"
echo $drive
newPath="${myString:3}"
echo $newPath
path="${newPath//'\'/"/"}"
echo $path
7z e -an -air!"/mnt/$drive/$path/*.rar" -r -o"/mnt/$drive/$path"
