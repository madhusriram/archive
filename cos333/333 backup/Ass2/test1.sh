#!/bin/bash

clear

for (( i=1; i <= 9; i++))
do

./encode.py < test0$i > myencode
openssl enc -e -base64 < test0$i > decode.txt
openssl enc -d -base64 < decode.txt > theirs
./decode.py <decode.txt  >mine

cmp -b -l mine theirs
cmp -b -l myencode decode.txt

done

for (( i=10; i <= 15; i++))
do

./encode.py < test$i > myencode
openssl enc -e -base64 < test$i > decode.txt
openssl enc -d -base64 < decode.txt > theirs
./decode.py <decode.txt  >mine

cmp -b -l mine theirs
cmp -b -l myencode decode.txt

done
