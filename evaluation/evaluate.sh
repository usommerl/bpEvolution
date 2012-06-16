#!/bin/bash

TESTNAME=$1
NUMBEROFRUNS=$2
PROBLEM=$3
G=$4

S=500
Q=1
PS='best'
R='mapped'
M='shift'
D='best-fit'
ES="tournament=3"


function execute {
   PREFIX='test'
   EXTENSION='.dat'
   DATAFOLDER="evaluation/data/"
   if [ $# -eq 1 ];then
       NUMBER=$(printf "%02d" $1)
       O="${DATAFOLDER}${PREFIX}_${TESTNAME}_${ALGORITHM}_${PROBLEM}_${NUMBER}${EXTENSION}"
   else
       O="${DATAFOLDER}${PREFIX}_${TESTNAME}_${PROBLEM}${EXTENSION}"
   fi
   sbt "run -s $S -g $G -d $D -q $Q -ps $PS -r $R -m $M -es $ES -o $O $PROBLEM"
}

BUILDFOLDER="../"
cd $BUILDFOLDER

if [ $# -gt 2 ]; then

    if [ "$TESTNAME" == "decfuc" ]; then
        declare -a ALGORITHMS=('next-fit' 'first-fit' 'best-fit')
        for TESTRUN in $(eval echo {1..$NUMBEROFRUNS})
        do
           for ALGORITHM in ${ALGORITHMS[@]}
           do
             D=$ALGORITHM
             execute $TESTRUN
           done
        done
    fi
    if [ "$TESTNAME" == "qualfunc" ]; then
        declare -a ALGORITHMS=('q1' 'q2')
        for TESTRUN in $(eval echo {1..$NUMBEROFRUNS})
        do
           for ALGORITHM in ${ALGORITHMS[@]}
           do
             Q=${ALGORITHM:1}
             execute $TESTRUN
           done
        done
    fi
    if [ "$TESTNAME" == "mutations" ]; then
        declare -a ALGORITHMS=('none' 'inversion' 'shift' 'exchange' 'shift,exchange')
        for TESTRUN in $(eval echo {1..$NUMBEROFRUNS})
        do
           for ALGORITHM in ${ALGORITHMS[@]}
           do
             M=$ALGORITHM
             execute $TESTRUN
           done
        done
    fi
    if [ "$TESTNAME" == "recombinations" ]; then
        declare -a ALGORITHMS=('ordered' 'mapped' 'random')
        for TESTRUN in $(eval echo {1..$NUMBEROFRUNS})
        do
           for ALGORITHM in ${ALGORITHMS[@]}
           do
             R=$ALGORITHM
             execute $TESTRUN
           done
        done
    fi

else
    
    R='random'
    G=$2
    if [ "$TESTNAME" == "comparison" ]; then
       # declare -a PROBLEMPREFIXES=('u120_' 't60_')
        declare -a PROBLEMPREFIXES=('t60_')
        for PROBLEMPREFIX in ${PROBLEMPREFIXES[@]}
        do
        for PROBLEMNUMBER in {0..19}
           do
             PROBLEM=${PROBLEMPREFIX}$(printf "%02d" $PROBLEMNUMBER)
             execute
           done
        done
    fi

fi
