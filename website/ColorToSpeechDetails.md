#Color-to-speech application details


The color-to-speech application already contains some subparts.

## Bifröst gears interface
A basic interface to select colors in HSL space has been developped to test the other subparts, and distributed as a standelone application, named *Bifröst gears*.

![Using standard color names in French](screenshot/bifrost-2.png)

## Bifröst core: library components

### RGB to HSL
A very basic color RGB to HSL and HSL to RGB converter has been implemented.

### HSL to fuzzy colors

In Bifröst, a fuzzy color is described in each of the three color components (hue, saturation, lightness) by a series of concepts (such as blue, light or dark), each of them associated to a fuzzy value (between 0 and 1) that express the mesure of the match between the original color and this concept.

An [xml file](../app/src/main/res/xml/colors.xml) and the corresponding java processing has been developped to translate an HSL color into a fuzzy color. The xml file is containing for each concept parameters (defined by hand). This file is parsed by the java code, and these descriptions are used to generate the fuzzy description for each new color given to the system.

### Fuzzy color to semantic color

In Bifröst, a semantic color is a list of words that are describing the color for a human. This semantic description is deduced from a fuzzy colors using some rules defined for each language. A rule is defined by an expression on each of the three components of the color (hue, saturation, lightness), and by a list of elements that will become words after consolidation.

Several version has been introduced:
* [Basic color names in English](../app/src/main/res/xml/basic_color_names.xml)
* [Basic color names in French](../app/src/main/res/xml-fr/basic_color_names.xml)
* [Standard color names in English](../app/src/main/res/xml/standard_color_names.xml)
* [Standard color names in French](../app/src/main/res/xml-fr/standard_color_names.xml)

The accuracy of the color naming for a camera-to-speech application does not need to be high, for at least two reasons. First, the incertainty of the acquisition is high, since we are not controlling the lighting. Second, thea person that needs a color to speech system is probably more motivated in the global idea of a color, and not about the very high level details of the color is looking to (see for example the amont of [shades of red defined in Wikipedia](http://en.wikipedia.org/wiki/Shades_of_red)). Finally, blind from birth persons (that are possible users of Bifröst) are not aware about all the small variations of colors...

### Semantic color to speech

The final color to speech is done using the default TextToSpeech engine available on the smartphone.