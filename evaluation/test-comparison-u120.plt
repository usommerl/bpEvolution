set terminal epslatex size 13.7cm,8cm color colortex
#set terminal postscript eps size 3.5,2.62 enhanced color dl 3 font "Helvetica,11" 
#set encoding utf8
set output "../doc/figures/test-comparison-u120.tex"
set style data histogram
set style histogram clustered gap 1
set boxwidth 0.8
set style fill solid border -1
set ylabel '\footnotesize{Laufzeit in Sekunden}'
set xlabel '\footnotesize{Nummer der Probleminstanz (\texttt{u120\_})}'
set yrange[0:215]
set xrange[0:15]


#key settings
set key at 14,190 top right
set key samplen 3 spacing 1.15

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

set rmargin 2
set bmargin 4

plot 'data/test_comparison_u120_combined.dat' using 2:xticlabels(1) ls 1 title '\footnotesize{HGGA}', \
                                           '' using 4 ls 2 title '\footnotesize{EBPA}'

