set terminal epslatex size 13.7cm,8cm color colortex
#set terminal postscript eps size 3.5,2.62 enhanced color dl 3 font "Helvetica,11" 
#set encoding utf8
set output "../doc/figures/test-mutations-01.tex"
set style data histogram
set style histogram clustered gap 1
set boxwidth 0.7
set style fill solid border -1
set ylabel '\shortstack{\footnotesize{Generation in der die erste minimale L\"osung} \\ \footnotesize{gefunden wurde (Median aller Testl\"aufe)}}'

set xrange[-0.5:0.5]
#key settings
set key at 0.055,970
set key samplen 3 spacing 1.15
#set key box ls 1

#set key box ls 1
# define axis
# remove border on top and right and set color to gray
set style line 100 lc rgb 'black' lt 1 lw 4
set border 3 back ls 100
set tics out nomirror scale 0
set ytics out scale 1
# define grid
set style line 101 lc rgb '#808080' lt 0 lw 1
set grid ytics back ls 101

# color definitions
set style line 1 lc rgb '#9D2E2C' pt 1 ps 1 lt 1 lw 1
set style line 2 lc rgb '#FFD700' pt 1 ps 1 lt 1 lw 1 
set style line 3 lc rgb '#7DB6D5' pt 1 ps 1 lt 1 lw 1 
set style line 4 lc rgb '#A9CF54' pt 1 ps 1 lt 1 lw 1 
set style line 5 lc rgb '#4A4747' pt 1 ps 1 lt 1 lw 1 

unset xtics

set rmargin 2

set label '\footnotesize{6/11}' at -0.367,320
set label '\footnotesize{11/11}' at -0.205,356
set label '\footnotesize{11/11}' at -0.041,419
set label '\footnotesize{8/11}' at 0.135,951
set label '\footnotesize{10/11}' at 0.29,472

plot 'data/test_mutations_combined.dat' using 1 ls 1 title '\footnotesize{keine Mutation}', \
                                              '' using 2 ls 2 title '\footnotesize{Verschiebende-Mutation}', \
                                              '' using 3 ls 3 title '\footnotesize{Vertauschende-Mutation}', \
                                              '' using 4 ls 4 title '\footnotesize{Invertierende-Mutation}', \
                                              '' using 5 ls 5 title '\footnotesize{Verschieben und Vertauschen}'

