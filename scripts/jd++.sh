#!/bin/bash

out="out/"
if [ ! -e $out ]
then
    mkdir $out
fi


raw_cpp_files=""
cpp_files=""
jdpp_files=""

for a in "$@"
do
    let base_len=${#a}-4
    base=${a:0:$base_len}
    extension=${a:$base_len}
#    echo $base_len $base $extension

    if [ $extension == "jdpp" ]
    then
        jdpp_files=$jdpp_files" ../"$a
        cpp_files=$cpp_files" "$base"cpp"
    elif [ $extension == ".cpp" ]
    then
        raw_cpp_files=$raw_cpp_files" "$a

        cp $a $out
    fi

done
echo jdpp_files:  $jdpp_files
echo raw_cpp_files:  $raw_cpp_files
echo "cpp_files (to be produced):"  $cpp_files

cd $out
echo parsing jdpp files:
java -jar /Users/dlahr/code_and_repos/jd++/out/artifacts/jd___jar/jd++.jar $jdpp_files

echo compiling with g++
g++ $raw_cpp_files $cpp_files
