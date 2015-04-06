#Color-to-speech application details

## Recently implemented

The color-to-speech application already contains some subparts.

### Temporary interface
A basic and temporary interface to select colors in HSL space has been developped to test the other subparts.

### RGB to HSL
A very basic color RGB to HSL and HSL to RGB converter has been implemented.

### HSL to fuzzy colors

In Bifröst, a fuzzy color is described in each of the three color components (hue, saturation, lightness) by a series of concepts (such as blue, light or dark), each of them associated to a fuzzy value (between 0 and 1) that express the mesure of the match between the original color and this concept.

An [xml file](../app/src/main/res/xml/colors.xml) and the corresponding java processing has been developped to translate an HSL color into a fuzzy color. The xml file is containing for each concept parameters (defined by hand). This file is parsed by the java code, and these descriptions are used to generate the fuzzy description for each new color given to the system.

### Fuzzy color to semantic color

In Bifröst, a semantic color is a list of words that are describing the color for a human. This semantic description is deduced from a fuzzy colors using some rules defined for each language. A rule is defined by an expression on each of the three components of the color (hue, saturation, lightness), and by a list of elements that will become words after consolidation.

