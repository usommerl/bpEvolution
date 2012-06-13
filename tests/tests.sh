#!/bin/bash

DATAFOLDER="/home/uwe/study/msc/4_semester/ealg/tests/data"
BUILDFOLDER="/home/uwe/study/msc/4_semester/ealg"
NUMBEROFRUNS=11
S=500
G=500
Q=1
PS='best'
R='mapped'
M='shift'
D='best-fit'
ES="tournament=3"
PREFIX='test_'
EXTENSION='.dat'

declare -a DECODINGFUNCTIONS=('next-fit' 'first-fit' 'best-fit')
declare -a MUTATIONS=('none' 'inversion' 'shift' 'exchange' 'shift,exchange')

cd $BUILDFOLDER

if [ "$2" == "decfuc" ]; then
    for i in $(eval echo {1..$NUMBEROFRUNS})
    do
       echo "Test Run $i"
       for DVAR in ${DECODINGFUNCTIONS[@]}
       do
         number=$(printf "%02d" $i)
         O="${DATAFOLDER}/${PREFIX}${2}_${DVAR}_${1}_${number}${EXTENSION}"
         sbt "run -s $S -g $G -d $DVAR -q $Q -ps $PS -r $R -m $M -es $ES -o $O  $1"
       done
    done
fi

if [ "$2" == "mutations" ]; then
    G=1000
    for i in $(eval echo {1..$NUMBEROFRUNS})
    do
       echo "Test Run $i"
       for MVAR in ${MUTATIONS[@]}
       do
         number=$(printf "%02d" $i)
         O="${DATAFOLDER}/${PREFIX}${2}_${MVAR}_${1}_${number}${EXTENSION}"
         sbt "run -s $S -g $G -d $D -q $Q -ps $PS -r $R -m $MVAR -es $ES -o $O  $1"
       done
    done
fi


