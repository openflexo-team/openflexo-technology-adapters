all: HowToMakeATA.html

HowToMakeATA.html: HowToMakeATA.md pandoc.css
	gpp -T HowToMakeATA.md | pandoc -f markdown --css pandoc.css -M date="`LANG=en_US date "+%B %e, %Y"`" --data-dir=data_dir --template=template --mathjax --toc --filter pandoc-include-code -s -o HowToMakeATA.html
