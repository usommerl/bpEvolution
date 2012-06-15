!/bin/bash

DOCDIR=$(pwd)
TESTDIR=../evaluation

cd $TESTDIR
for file in *.plt
do
	gnuplot "$file"
done
cd $DOCDIR
