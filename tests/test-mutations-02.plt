set terminal epslatex size 13.7cm,8cm color colortex
#set terminal postscript eps size 3.5,2.62 enhanced color dl 3 font "Helvetica,11" 
#set encoding utf8
set output "../doc/figures/test-mutations-02.tex"
unset log   
set style fill solid border -1
set ytic auto
set bmargin 4
set xrange [0:450]
set xlabel '\footnotesize{Generation}'
set ylabel '\footnotesize{Diversit\"at}'

#key settings
set key at 400,80 right top 
set key width -6 samplen 1 spacing 1.05
#set key box ls 1
# define axis
# remove border on top and right and set color to gray
set style line 100 lc rgb 'black' lt 1 lw 4
set border 3 back ls 100
set tics out nomirror scale 1
# define grid
set style line 101 lc rgb '#808080' lt 0 lw 1
set grid xtics back ls 101

# color definitions
set style line 1 lc rgb '#9D2E2C' pt 1 ps 1 lt 1 lw 2
set style line 2 lc rgb '#FFD700' pt 2 ps 1 lt 1 lw 2 

set rmargin 2

plot 'data/test_mutations_shift_u120_02_05.dat' using 1:4 with points ls 2 title '\footnotesize{Verschiebende-Mutation}', \
     'data/test_mutations_none_u120_02_11.dat' using 1:4 with points ls 1 title '\footnotesize{keine Mutation}'

