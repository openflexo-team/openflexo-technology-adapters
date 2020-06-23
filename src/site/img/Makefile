all: model.png model2.png

%.pdf: %.tex
	latexmk -pdf $<

%.png: %.pdf
	magick -density 300 $*.pdf $*.png
